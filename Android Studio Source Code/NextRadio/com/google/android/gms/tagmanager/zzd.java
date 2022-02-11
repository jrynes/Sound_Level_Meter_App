package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.Uri;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Map;

class zzd implements zzb {
    private final Context context;

    public zzd(Context context) {
        this.context = context;
    }

    public void zzQ(Map<String, Object> map) {
        Object obj;
        Object obj2 = map.get("gtm.url");
        if (obj2 == null) {
            obj = map.get("gtm");
            if (obj != null && (obj instanceof Map)) {
                obj = ((Map) obj).get(SettingsJsonConstants.APP_URL_KEY);
                if (obj != null && (obj instanceof String)) {
                    String queryParameter = Uri.parse((String) obj).getQueryParameter("referrer");
                    if (queryParameter != null) {
                        zzax.zzn(this.context, queryParameter);
                        return;
                    }
                    return;
                }
            }
        }
        obj = obj2;
        if (obj != null) {
        }
    }
}
