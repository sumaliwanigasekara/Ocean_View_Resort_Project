(function () {
    const first = window.location.pathname.split('/')[1];
    const basePath = first && !first.includes('.') ? `/${first}` : '';

    async function requireAuth() {
        try {
            const res = await fetch(`${basePath}/api/auth/me`, {
                method: 'GET',
                credentials: 'include',
                headers: { Accept: 'application/json' }
            });

            if (!res.ok) {
                window.location.replace('index.html');
                return false;
            }

            const contentType = res.headers.get('content-type') || '';
            if (contentType.includes('application/json')) {
                const body = await res.json().catch(() => null);
                if (body && body.success === false) {
                    window.location.replace('index.html');
                    return false;
                }
            }

            return true;
        } catch (e) {
            window.location.replace('index.html');
            return false;
        }
    }

    window.requireAuth = requireAuth;
})();
