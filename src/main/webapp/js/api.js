const first = window.location.pathname.split('/')[1];
const autoBase = first && !first.includes('.') ? `/${first}` : '';


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
        }
    },

    bills: {
        generate(reservationId, serviceCharges, discountAmount) {
            return API.request("/api/bills", {
                method: "POST",
                body: JSON.stringify({ reservationId, serviceCharges, discountAmount })
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
    }
};
