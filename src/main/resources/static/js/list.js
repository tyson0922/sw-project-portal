(function () {
  "use strict";

  const categoryLabels = {
    FRAMEWORK: "프레임워크",
    LANGUAGE: "사용 언어",
    DATABASE: "데이터베이스",
    CLOUD: "클라우드",
    WEB_SERVER: "웹 서버",
    PROTOCOL: "통신 프로토콜",
    AUTH: "인증 및 보안",
    AI: "AI",
    EXTERNAL_API: "API 활용",
    ETC: "기타 기술"
  };

  function formatStat(value) {
    return String(value).padStart(2, "0");
  }

  document.addEventListener("DOMContentLoaded", () => {
    const staticPreview = document.documentElement.dataset.renderMode === "static";
    const carousel = document.querySelector("#exhibitionCarousel");
    const slides = [...carousel.querySelectorAll("[data-project-slide]")];
    const controls = [...carousel.querySelectorAll("[data-carousel-control]")];
    const indicators = carousel.querySelector(".carousel-indicators");
    const projectColumns = [...document.querySelectorAll("[data-project-column]")];
    const participantCount = document.querySelector("[data-exhibition-participant-count]");
    const projectCount = document.querySelector("[data-exhibition-project-count]");

    document.querySelectorAll("[data-category-label]").forEach((label) => {
      label.textContent = categoryLabels[label.dataset.category] ?? label.dataset.category;
    });

    const studentNames = new Set(projectColumns.flatMap((column) => (
      String(column.dataset.projectStudents ?? "")
        .split("|")
        .map((name) => name.trim())
        .filter(Boolean)
    )));
    if (staticPreview) {
      participantCount.textContent = formatStat(studentNames.size);
      projectCount.textContent = formatStat(projectColumns.length);
    }

    const hasMultipleSlides = slides.length > 1;
    controls.forEach((control) => {
      control.hidden = !hasMultipleSlides;
      control.disabled = !hasMultipleSlides;
    });
    indicators.hidden = !hasMultipleSlides;
    if (hasMultipleSlides) {
      window.bootstrap.Carousel.getOrCreateInstance(carousel, {
        interval: 6500,
        ride: "carousel",
        touch: true,
        wrap: true
      });
    }

    if (!staticPreview) return;

    const form = document.querySelector("#projectFilterForm");
    const activeFilters = document.querySelector("[data-active-filters]");
    const resultCount = document.querySelector("[data-result-count]");
    const grid = document.querySelector("[data-project-grid]");
    const empty = document.querySelector("[data-empty-state]");
    const params = new URLSearchParams(window.location.search);

    form.elements.year.value = params.get("year") ?? "";
    form.elements.studentName.value = params.get("studentName") ?? "";
    const usesAiControl = form.querySelector('input[name="usesAi"]');
    usesAiControl.checked = params.get("usesAi") === "true";
    const selectedTechnologyIds = new Set(params.getAll("technologyIds"));
    form.querySelectorAll('input[name="technologyIds"]').forEach((input) => {
      input.checked = selectedTechnologyIds.has(input.value);
    });

    function selectedFilters() {
      return {
        year: form.elements.year.value.trim(),
        studentName: form.elements.studentName.value.trim(),
        usesAi: usesAiControl.checked,
        technologyIds: [...form.querySelectorAll('input[name="technologyIds"]:checked')].map((input) => input.value)
      };
    }

    function renderActiveFilters(filters) {
      const labels = [];
      if (filters.year) labels.push(filters.year);
      if (filters.studentName) labels.push(`학생: ${filters.studentName}`);
      if (filters.usesAi) labels.push("AI 활용");
      filters.technologyIds.forEach((id) => {
        const input = form.querySelector(`input[name="technologyIds"][value="${CSS.escape(id)}"]`);
        const name = input?.closest("label")?.querySelector("span")?.textContent?.trim();
        if (name) labels.push(name);
      });
      if (!labels.length) labels.push("전체 작품");

      activeFilters.replaceChildren(...labels.map((label) => {
        const pill = document.createElement("span");
        pill.className = "active-filter-pill";
        pill.textContent = label;
        return pill;
      }));
    }

    function applyStaticFilters() {
      const filters = selectedFilters();
      const normalizedName = filters.studentName.toLocaleLowerCase("ko");
      let visibleCount = 0;

      projectColumns.forEach((column) => {
        const technologies = new Set(String(column.dataset.projectTechnologies ?? "").split(",").filter(Boolean));
        const visible = (!filters.year || column.dataset.projectYear === filters.year)
          && (!normalizedName || String(column.dataset.projectStudents ?? "").toLocaleLowerCase("ko").includes(normalizedName))
          && (!filters.usesAi || column.dataset.projectAi === "true")
          && (!filters.technologyIds.length || filters.technologyIds.some((id) => technologies.has(id)));
        column.hidden = !visible;
        if (visible) visibleCount += 1;
      });

      resultCount.textContent = formatStat(visibleCount);
      grid.hidden = visibleCount === 0;
      empty.hidden = visibleCount > 0;
      renderActiveFilters(filters);
    }

    form.addEventListener("submit", (event) => {
      event.preventDefault();
      const data = new FormData(form);
      const query = new URLSearchParams();
      const year = String(data.get("year") ?? "").trim();
      const studentName = String(data.get("studentName") ?? "").trim();
      if (year) query.set("year", year);
      if (studentName) query.set("studentName", studentName);
      if (data.get("usesAi")) query.set("usesAi", "true");
      data.getAll("technologyIds").forEach((id) => query.append("technologyIds", String(id)));
      window.history.replaceState({}, "", `/${query.size ? `?${query}` : ""}#portfolio`);
      applyStaticFilters();
      window.bootstrap.Modal.getOrCreateInstance(document.querySelector("#projectFilterModal")).hide();
    });

    applyStaticFilters();
  });
})();
