(function () {
  "use strict";

  document.addEventListener("DOMContentLoaded", () => {
    if (document.documentElement.dataset.renderMode !== "static") return;
    if (document.body.dataset.page !== "student-form") return;
    if (!/\/edit\/?$/.test(window.location.pathname)) return;

    document.querySelector("[data-student-form-kicker]").textContent = "EDIT STUDENT";
    document.querySelector("[data-student-form-title]").textContent = "학생 수정";
    document.querySelector("[data-student-submit]").textContent = "수정 완료";
  });
})();
