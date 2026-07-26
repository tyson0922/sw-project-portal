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

  document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("[data-category-label]").forEach((label) => {
      label.textContent = categoryLabels[label.dataset.category] ?? label.dataset.category;
    });

    const backLink = document.querySelector("[data-history-back]");
    backLink?.addEventListener("click", (event) => {
      if (document.referrer && new URL(document.referrer).origin === window.location.origin) {
        event.preventDefault();
        window.history.back();
      }
    });
  });
})();
