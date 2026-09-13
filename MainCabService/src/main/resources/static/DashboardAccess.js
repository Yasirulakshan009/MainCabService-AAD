(function () {
    const token = localStorage.getItem("jwtToken");
    const role = localStorage.getItem("userRole");

    if (!token || (role !== "ADMIN" && role !== "STAFF")) {
        return;
    }

    const dashboardBtn = document.createElement("a");
    dashboardBtn.href = "pos.html";
    dashboardBtn.id = "backToDashboardBtn";
    dashboardBtn.innerHTML = '<i class="ri-home-gear-line"></i> ';

    dashboardBtn.style.position = "fixed";
    dashboardBtn.style.bottom = "24px";
    dashboardBtn.style.left = "24px";
    dashboardBtn.style.zIndex = "9999";
    dashboardBtn.style.background = "#1a73e8";
    dashboardBtn.style.color = "#ffffff";
    dashboardBtn.style.padding = "10px 18px";
    dashboardBtn.style.borderRadius = "30px";
    dashboardBtn.style.textDecoration = "none";
    dashboardBtn.style.fontFamily = "inherit";
    dashboardBtn.style.fontSize = "14px";
    dashboardBtn.style.fontWeight = "600";
    dashboardBtn.style.boxShadow = "0 4px 12px rgba(0,0,0,0.25)";
    dashboardBtn.style.display = "flex";
    dashboardBtn.style.alignItems = "center";
    dashboardBtn.style.gap = "6px";

    dashboardBtn.addEventListener("mouseenter", () => {
        dashboardBtn.style.background = "#1558b0";
    });
    dashboardBtn.addEventListener("mouseleave", () => {
        dashboardBtn.style.background = "#1a73e8";
    });

    document.body.appendChild(dashboardBtn);
})();