const first = window.location.pathname.split('/')[1];
const autoBase = first && !first.includes('.') ? `/${first}` : '';
let authMeCache = null;
let authMePromise = null;


const API = {
    basePath: autoBase,

    async request(url, options = {}) {
        const res = await fetch(`${API.basePath}${url}`, {
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            ...options
        });
        const contentType = res.headers.get("content-type") || "";
        if (contentType.includes("application/json")) {
            return res.json();
        }
        return {
            success: false,
            message: `Request failed (${res.status})`
        };
    },

    auth: {
        login(email, password) {
            return API.request("/api/auth/login", {
                method: "POST",
                body: JSON.stringify({ email, password })
            });
        },
        me() {
            return API.request("/api/auth/me", {
                method: "GET"
            });
        }
    },

    bills: {
        generate(reservationId, serviceCharges, discountAmount) {
            return API.request("/api/bills", {
                method: "POST",
                body: JSON.stringify({ reservationId, serviceCharges, discountAmount })
            });
        },
        list(status = "") {
            return API.request(status ? `/api/bills?status=${encodeURIComponent(status)}` : "/api/bills");
        },
        getById(billId) {
            return API.request(`/api/bills/${billId}`);
        },
        markAsPaid(billId, paymentMethod) {
            return API.request(`/api/bills/${billId}/pay`, {
                method: "POST",
                body: JSON.stringify({ paymentMethod })
            });
        }
    },

    reservations: {
        create(payload) {
            return API.request("/api/reservations", {
                method: "POST",
                body: JSON.stringify(payload)
            });
        },
        getById(id) {
            return API.request(`/api/reservations/${id}`);
        },
        search(from, to) {
            return API.request(`/api/reservations?from=${from}&to=${to}`);
        }
    },

    guests: {
        create(payload) {
            return API.request("/api/guests", {
                method: "POST",
                body: JSON.stringify(payload)
            });
        },
        search(term) {
            return API.request(`/api/guests?q=${encodeURIComponent(term)}`);
        },
        getById(id) {
            return API.request(`/api/guests/${id}`);
        }
    },

        reports: {
            getDashboard() {
                return API.request("/api/reports?type=dashboard");
            },
            get(type, from, to) {
                return API.request(`/api/reports?type=${type}&from=${from}&to=${to}`);
            }
        },

    rooms: {
        create(payload) {
            return API.request("/api/rooms", {
                method: "POST",
                body: JSON.stringify(payload)
            });
        },
        list() {
            return API.request("/api/rooms");
        }
    },

    isAuthenticated() {
        return document.cookie.includes("ovr_role");
    },

    async getCurrentUser(forceRefresh = false) {
        if (forceRefresh) {
            authMeCache = null;
            authMePromise = null;
        }

        if (authMeCache) {
            return authMeCache;
        }

        if (!authMePromise) {
            authMePromise = API.auth.me()
                .then((res) => (res && res.success ? res : null))
                .catch(() => null);
        }

        authMeCache = await authMePromise;
        return authMeCache;
    },

    applyRoleVisibility(role) {
        const isManager = (role || "").toUpperCase() === "MANAGER";
        document.querySelectorAll('[data-role="MANAGER"]').forEach((el) => {
            el.classList.toggle("hidden", !isManager);
        });
    },

    async initAccessControl() {
        const user = await API.getCurrentUser();
        if (!user) {
            window.location.href = "index.html";
            return;
        }

        const role = (user.role || "").toUpperCase();
        API.applyRoleVisibility(role);

        const requiredRole = (document.body?.dataset?.requiredRole || "").toUpperCase();
        if (requiredRole && role !== requiredRole) {
            window.location.href = "dashboard.html";
            return;
        }

        window.dispatchEvent(new CustomEvent("ovr:role-ready", {
            detail: { role, user }
        }));
    }
};

document.addEventListener("DOMContentLoaded", () => {
    API.initAccessControl();
});
