(function () {
  "use strict";

  function youtubeVideoId(value) {
    if (!value) return "";
    try {
      const url = new URL(value, window.location.origin);
      if (url.hostname === "youtu.be") {
        return url.pathname.split("/").filter(Boolean)[0] ?? "";
      }
      if (url.hostname.endsWith("youtube.com") || url.hostname.endsWith("youtube-nocookie.com")) {
        if (url.searchParams.get("v")) return url.searchParams.get("v");
        const parts = url.pathname.split("/").filter(Boolean);
        const markerIndex = parts.findIndex((part) => ["embed", "shorts", "live"].includes(part));
        return markerIndex >= 0 ? (parts[markerIndex + 1] ?? "") : "";
      }
      return "";
    } catch {
      return "";
    }
  }

  function youtubeEmbedUrl(value) {
    const id = youtubeVideoId(value);
    return id ? `https://www.youtube-nocookie.com/embed/${encodeURIComponent(id)}` : "";
  }

  function renderHeader(detailPage) {
    const target = document.querySelector("[data-site-header]");
    if (!target) return;

    const homeHref = detailPage ? "/" : "#home";
    const portfolioHref = detailPage ? "/#portfolio" : "#portfolio";
    const adminAuthenticated = target.dataset.adminAuthenticated === "true";
    const headerBrand = '<a href="/" class="brand brand-home" aria-label="SW 작품전 홈"><span class="brand-mark">HOME</span></a>';
    const adminButton = adminAuthenticated
      ? '<a class="admin-login-button" href="/admin/dashboard"><span class="key-icon" aria-hidden="true"></span><span>관리자 페이지</span></a>'
      : '<a class="admin-login-button" href="/admin/login"><span class="key-icon" aria-hidden="true"></span><span>관리자 로그인</span></a>';

    target.innerHTML = `
      <header class="site-header navbar navbar-expand-xl">
        <div class="header-inner">
          <div class="brand-admin-group">
            ${headerBrand}
            ${adminButton}
          </div>
          <nav class="desktop-nav" aria-label="주요 메뉴">
            <a href="${homeHref}" data-nav="home">Home</a>
            <a href="${portfolioHref}" data-nav="portfolio">Projects</a>
          </nav>
          <div class="header-actions">
            <span class="campus-name">한국폴리텍대학 서울강서캠퍼스</span>
          </div>
        </div>
      </header>`;

    const updateActive = () => {
      const active = detailPage || window.scrollY > 420 ? "portfolio" : "home";
      target.querySelectorAll("[data-nav]").forEach((link) => {
        link.classList.toggle("active", link.dataset.nav === active);
      });
    };
    updateActive();
    if (!detailPage) window.addEventListener("scroll", updateActive, { passive: true });
  }

  function renderFooter() {
    const target = document.querySelector("[data-site-footer]");
    if (!target) return;
    target.innerHTML = `
      <footer>
        <div class="container footer-inner">
          <a href="/" class="brand"><span class="brand-mark">SW</span><span>SW 작품전</span></a>
          <p>2026 Korea polytechnics.</p>
          <a href="#top">TOP ↑</a>
        </div>
      </footer>`;
  }

  function observeReveals(root = document) {
    const elements = root.querySelectorAll(".reveal:not(.is-visible)");
    if (window.matchMedia("(prefers-reduced-motion: reduce)").matches) {
      elements.forEach((item) => item.classList.add("is-visible"));
      return;
    }
    const observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        }
      });
    }, { threshold: 0.12 });
    elements.forEach((item) => observer.observe(item));
  }

  function createVideoModal({ title, team, youtubeUrl, detailHref }) {
    const embedUrl = youtubeEmbedUrl(youtubeUrl);
    if (!embedUrl) return;

    const layer = document.createElement("div");
    layer.className = "modal fade project-video-modal";
    layer.tabIndex = -1;
    layer.setAttribute("aria-hidden", "true");
    layer.innerHTML = `
      <div class="modal-dialog modal-dialog-centered modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <div>
              <span class="section-kicker">PROJECT FILM</span>
              <h2 class="modal-title"></h2>
              <p></p>
            </div>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="영상 닫기"></button>
          </div>
          <div class="modal-body">
            <div class="modal-video-frame ratio ratio-16x9">
              <iframe
                title="작품 소개 영상"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen></iframe>
            </div>
          </div>
          <div class="modal-footer">
            <a class="button button-primary" href="${detailHref || "/"}">상세 페이지 보기 <span aria-hidden="true">→</span></a>
          </div>
        </div>
      </div>`;

    layer.querySelector(".modal-title").textContent = title;
    layer.querySelector(".modal-header p").textContent = team;
    const iframe = layer.querySelector("iframe");
    iframe.src = embedUrl;
    iframe.title = `${title} 작품 소개 영상`;

    document.body.append(layer);
    const modal = window.bootstrap.Modal.getOrCreateInstance(layer);
    layer.addEventListener("hidden.bs.modal", () => {
      iframe.src = "";
      modal.dispose();
      layer.remove();
    }, { once: true });
    modal.show();
  }

  function bindVideoPreviews(root = document) {
    root.querySelectorAll("[data-video-preview]").forEach((button) => {
      button.addEventListener("click", () => {
        createVideoModal({
          title: button.dataset.videoTitle ?? "프로젝트",
          team: button.dataset.videoTeam ?? "",
          youtubeUrl: button.dataset.videoUrl ?? "",
          detailHref: button.dataset.detailHref ?? "/"
        });
      });
    });
  }

  document.addEventListener("DOMContentLoaded", () => {
    renderHeader(!["list", "detail"].includes(document.body.dataset.page) || document.body.dataset.page === "detail");
    renderFooter();
    observeReveals();
    bindVideoPreviews();
  });

  window.ExhibitionUI = {
    bindVideoPreviews,
    createVideoModal,
    observeReveals,
    youtubeEmbedUrl,
    youtubeVideoId
  };
})();
