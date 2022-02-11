package io.fabric.sdk.android.services.common;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.cache.MemoryValueCache;
import io.fabric.sdk.android.services.cache.ValueLoader;

public class InstallerPackageNameProvider {
    private static final String NO_INSTALLER_PACKAGE_NAME = "";
    private final MemoryValueCache<String> installerPackageNameCache;
    private final ValueLoader<String> installerPackageNameLoader;

    class 1 implements ValueLoader<String> {
        1() {
        }

        public String load(Context context) throws Exception {
            String installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName());
            return installerPackageName == null ? InstallerPackageNameProvider.NO_INSTALLER_PACKAGE_NAME : installerPackageName;
        }
    }

    public InstallerPackageNameProvider() {
        this.installerPackageNameLoader = new 1();
        this.installerPackageNameCache = new MemoryValueCache();
    }

    public String getInstallerPackageName(Context appContext) {
        try {
            String name = (String) this.installerPackageNameCache.get(appContext, this.installerPackageNameLoader);
            if (NO_INSTALLER_PACKAGE_NAME.equals(name)) {
                return null;
            }
            return name;
        } catch (Exception e) {
            Fabric.getLogger().e(Fabric.TAG, "Failed to determine installer package name", e);
            return null;
        }
    }
}
