package com.billpilot.security;

import java.util.UUID;

public final class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_ORG = new ThreadLocal<>();

    private TenantContext() {}

    public static UUID getCurrentOrgId() {
        return CURRENT_ORG.get();
    }

    public static void setCurrentOrgId(UUID orgId) {
        CURRENT_ORG.set(orgId);
    }

    public static void clear() {
        CURRENT_ORG.remove();
    }
}
