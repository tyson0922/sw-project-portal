(function () {
  "use strict";

  const categories = [
    "FRAMEWORK",
    "LANGUAGE",
    "DATABASE",
    "CLOUD",
    "WEB_SERVER",
    "PROTOCOL",
    "AUTH",
    "AI",
    "EXTERNAL_API",
    "ETC"
  ];

  let previewTechnologySequence = 0;

  function normalizeYoutubeUrl(value) {
    const id = window.ExhibitionUI.youtubeVideoId(value);
    if (!id) return value;
    return `https://www.youtube.com/watch?v=${id}`;
  }

  document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("[data-project-form]");
    if (!form) return;

    const staticPreview = document.documentElement.dataset.renderMode === "static";
    const editPreview = staticPreview && /\/edit\/?$/.test(window.location.pathname);
    const status = document.querySelector("[data-form-status]");
    const studentError = document.querySelector("[data-student-error]");
    const technologyError = document.querySelector("[data-technology-error]");
    const selected = new Map(categories.map((category) => [category, new Map()]));
    const context = window.PROJECT_FORM_CONTEXT ?? { technologies: [], technologiesByCategory: {} };

    if (staticPreview) {
      const editMode = editPreview;
      document.body.dataset.formMode = editMode ? "edit" : "create";
      document.querySelector("[data-form-kicker]").textContent = editMode ? "EDIT PROJECT" : "REGISTER PROJECT";
      document.querySelector("[data-form-title]").textContent = editMode ? "프로젝트 수정" : "프로젝트 등록";
      document.querySelector("[data-form-description]").textContent = editMode
        ? "등록된 작품의 정보와 관계 데이터를 수정합니다."
        : "MariaDB에 저장할 작품 정보와 학생·기술 관계를 등록합니다.";
      document.querySelector("[data-submit-label]").textContent = editMode ? "수정 완료" : "프로젝트 등록";

      if (!editMode) {
        form.elements.year.value = String(new Date().getFullYear());
      }
    }

    const technologyByName = new Map(
      (context.technologies ?? []).map((technology) => [String(technology.name).toLocaleLowerCase("ko"), technology])
    );
    Object.entries(context.technologiesByCategory ?? {}).forEach(([category, names]) => {
      const targetCategory = selected.has(category) ? category : "ETC";
      names.forEach((name) => {
        const technology = technologyByName.get(String(name).toLocaleLowerCase("ko"));
        if (technology) selected.get(targetCategory).set(Number(technology.id), { id: Number(technology.id), name: technology.name });
      });
    });

    const youtubeInput = form.elements.youtubeUrl;
    if (youtubeInput.value) youtubeInput.value = normalizeYoutubeUrl(youtubeInput.value);

    function allSelectedIds() {
      return new Set([...selected.values()].flatMap((items) => [...items.keys()]));
    }

    function renderCategory(category) {
      const row = document.querySelector(`[data-stack-category="${category}"]`);
      const list = row.querySelector("[data-tag-list]");
      list.replaceChildren(...[...selected.get(category).values()].map((technology) => {
        const tag = document.createElement("span");
        tag.className = "editable-tag";
        tag.append(document.createTextNode(technology.name));

        const hidden = document.createElement("input");
        hidden.type = "hidden";
        hidden.name = "technologyIds";
        hidden.value = String(technology.id);
        tag.append(hidden);

        const remove = document.createElement("button");
        remove.type = "button";
        remove.className = "tag-remove";
        remove.setAttribute("aria-label", `${technology.name} 기술 삭제`);
        remove.textContent = "×";
        remove.addEventListener("click", () => {
          selected.get(category).delete(technology.id);
          renderCategory(category);
        });
        tag.append(remove);
        return tag;
      }));
    }

    function addTechnology(category, technology) {
      const id = Number(technology.id);
      if (!Number.isFinite(id) || allSelectedIds().has(id)) return;
      selected.get(category).set(id, { id, name: String(technology.name) });
      renderCategory(category);
      technologyError.hidden = true;
    }

    async function searchTechnologies(query, category) {
      if (staticPreview) {
        return [];
      }

      const response = await fetch(`/admin/api/technologies/search?q=${encodeURIComponent(query)}`, {
        headers: { Accept: "application/json" }
      });
      if (!response.ok) throw new Error(`기술 검색 실패: ${response.status}`);
      return response.json();
    }

    async function createTechnology(name, category) {
      if (staticPreview) {
        return { id: ++previewTechnologySequence, name, category };
      }

      const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
      const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;
      const headers = { "Content-Type": "application/json", Accept: "application/json" };
      if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

      const response = await fetch("/admin/api/technologies", {
        method: "POST",
        headers,
        body: JSON.stringify({ name, category })
      });
      if (!response.ok) throw new Error(`기술 등록 실패: ${response.status}`);
      return response.json();
    }

    function renderSuggestions(row, results, category) {
      const box = row.querySelector("[data-tech-suggestions]");
      box.replaceChildren(...results.map((technology) => {
        const button = document.createElement("button");
        button.type = "button";
        button.className = "tech-suggestion";
        button.textContent = technology.name;
        button.addEventListener("mousedown", (event) => {
          event.preventDefault();
          addTechnology(category, technology);
          row.querySelector("[data-tag-entry]").value = "";
          box.replaceChildren();
        });
        return button;
      }));
    }

    categories.forEach((category) => {
      const row = document.querySelector(`[data-stack-category="${category}"]`);
      const input = row.querySelector("[data-tag-entry]");
      let searchTimer;

      renderCategory(category);
      input.addEventListener("input", () => {
        window.clearTimeout(searchTimer);
        const query = input.value.trim();
        if (!query) {
          row.querySelector("[data-tech-suggestions]").replaceChildren();
          return;
        }
        searchTimer = window.setTimeout(async () => {
          try {
            renderSuggestions(row, await searchTechnologies(query, category), category);
          } catch {
            row.querySelector("[data-tech-suggestions]").replaceChildren();
            status.textContent = "기술 검색에 실패했습니다. 로그인 상태와 서버 연결을 확인해 주세요.";
          }
        }, 180);
      });

      input.addEventListener("keydown", async (event) => {
        if (event.key !== "Enter") return;
        event.preventDefault();
        const name = input.value.trim();
        if (!name) return;

        input.disabled = true;
        try {
          const results = await searchTechnologies(name, category);
          const exact = results.find((technology) => technology.name.toLocaleLowerCase("ko") === name.toLocaleLowerCase("ko"));
          addTechnology(category, exact ?? await createTechnology(name, category));
          input.value = "";
          row.querySelector("[data-tech-suggestions]").replaceChildren();
          status.textContent = "";
        } catch {
          status.textContent = "기술을 등록할 수 없습니다. 로그인 상태와 서버 연결을 확인해 주세요.";
        } finally {
          input.disabled = false;
          input.focus();
        }
      });

      input.addEventListener("blur", () => {
        window.setTimeout(() => row.querySelector("[data-tech-suggestions]").replaceChildren(), 120);
      });
    });

    form.addEventListener("submit", (event) => {
      youtubeInput.setCustomValidity("");
      const normalizedYoutube = normalizeYoutubeUrl(youtubeInput.value.trim());
      if (!window.ExhibitionUI.youtubeVideoId(normalizedYoutube)) {
        youtubeInput.setCustomValidity("지원되는 YouTube 영상 URL을 입력해 주세요.");
      } else {
        youtubeInput.value = normalizedYoutube;
      }

      const hasStudent = Boolean(form.querySelector('input[name="studentIds"]:checked'));
      const hasTechnology = allSelectedIds().size > 0;
      studentError.hidden = hasStudent;
      technologyError.hidden = hasTechnology;

      if (!form.checkValidity() || !hasStudent || !hasTechnology) {
        event.preventDefault();
        form.classList.add("was-validated");
        form.reportValidity();
        return;
      }

      if (staticPreview) {
        event.preventDefault();
        status.textContent = "백엔드 연결 후 저장이 완료되면 관리자 대시보드로 이동합니다.";
      }
    });
  });
})();
