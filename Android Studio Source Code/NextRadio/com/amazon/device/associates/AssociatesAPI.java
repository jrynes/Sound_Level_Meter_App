package com.amazon.device.associates;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import java.util.Set;
import org.xbill.DNS.KEYRecord.Flags;

public class AssociatesAPI {
    static ShoppingService f1069a;
    static LinkService f1070b;
    private static final String f1071c;

    public static class Config {
        private final String f1066a;
        private final Context f1067b;
        private Set<String> f1068c;

        public Config(String str, Context context) {
            this.f1068c = null;
            ar.m782a((Object) str, "appKey");
            ar.m782a((Object) context, "appContext");
            this.f1066a = str;
            this.f1067b = context;
        }

        public void setPrefetchProducts(Set<String> set) {
            ar.m782a((Object) set, "productIds");
            this.f1068c = set;
        }

        String m647a() {
            return this.f1066a;
        }

        Context m648b() {
            return this.f1067b;
        }

        Set<String> m649c() {
            return this.f1068c;
        }
    }

    static {
        f1071c = AssociatesAPI.class.getSimpleName();
    }

    private AssociatesAPI() {
    }

    public static void initialize(Config config) {
        boolean z = false;
        ar.m782a((Object) config, "config");
        Context applicationContext = config.m648b().getApplicationContext();
        try {
            ApplicationInfo applicationInfo = applicationContext.getPackageManager().getApplicationInfo(applicationContext.getPackageName(), Flags.FLAG8);
            if (!(applicationInfo == null || applicationInfo.metaData == null)) {
                z = applicationInfo.metaData.getBoolean("com.amazon.device.associates.ENABLE_TESTING", false);
            }
        } catch (NameNotFoundException e) {
        }
        if (f1069a == null) {
            f1069a = new ShoppingServiceImpl(applicationContext, z);
        }
        if (f1070b == null) {
            f1070b = new bd(applicationContext, config.m647a(), config.m649c());
        }
        Log.m1018c(f1071c, "Mobile Associates API initialization complete. SDK Version 1.0.64.0");
    }

    public static ShoppingService getShoppingService() throws NotInitializedException {
        m650a();
        return f1069a;
    }

    public static LinkService getLinkService() throws NotInitializedException {
        m650a();
        return f1070b;
    }

    private static void m650a() throws NotInitializedException {
        if (f1069a == null || f1070b == null) {
            throw new NotInitializedException();
        }
    }
}
