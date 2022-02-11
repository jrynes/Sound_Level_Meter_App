package com.admarvel.android.ads;

import com.google.android.gms.tagmanager.DataLayer;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.io.Serializable;
import java.util.ArrayList;

/* renamed from: com.admarvel.android.ads.b */
public class AdMarvelEvent implements Serializable {
    private ArrayList<AdMarvelEvent> f507a;
    private boolean f508b;

    /* renamed from: com.admarvel.android.ads.b.a */
    class AdMarvelEvent implements Serializable {
        final /* synthetic */ AdMarvelEvent f503a;
        private String f504b;
        private boolean f505c;
        private String[] f506d;

        public AdMarvelEvent(AdMarvelEvent adMarvelEvent) {
            this.f503a = adMarvelEvent;
        }

        public String m258a() {
            return this.f504b;
        }

        public void m259a(String str) {
            this.f504b = str;
        }

        public void m260a(boolean z) {
            this.f505c = z;
        }

        public void m261a(String[] strArr) {
            this.f506d = strArr;
        }

        public String[] m262b() {
            return this.f506d;
        }

        public boolean m263c() {
            return this.f505c;
        }
    }

    public AdMarvelEvent(AdMarvelXMLElement adMarvelXMLElement) {
        this.f507a = new ArrayList();
        if (adMarvelXMLElement != null && adMarvelXMLElement.getChildren().containsKey("eventTracker")) {
            int size = ((ArrayList) adMarvelXMLElement.getChildren().get("eventTracker")).size();
            for (int i = 0; i < size; i++) {
                AdMarvelXMLElement adMarvelXMLElement2 = (AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement.getChildren().get("eventTracker")).get(i);
                if (adMarvelXMLElement2 != null) {
                    AdMarvelEvent adMarvelEvent = new AdMarvelEvent(this);
                    String str = (String) adMarvelXMLElement2.getAttributes().get(DataLayer.EVENT_KEY);
                    if (str != null && str.length() > 0) {
                        adMarvelEvent.m259a(str);
                        str = (String) adMarvelXMLElement2.getAttributes().get("reward");
                        if (str == null || str.length() <= 0 || !str.equalsIgnoreCase("1")) {
                            adMarvelEvent.m260a(false);
                        } else {
                            adMarvelEvent.m260a(true);
                        }
                        ArrayList arrayList = new ArrayList();
                        int size2 = ((ArrayList) adMarvelXMLElement2.getChildren().get(SettingsJsonConstants.APP_URL_KEY)).size();
                        for (int i2 = 0; i2 < size2; i2++) {
                            arrayList.add(((AdMarvelXMLElement) ((ArrayList) adMarvelXMLElement2.getChildren().get(SettingsJsonConstants.APP_URL_KEY)).get(i)).getData());
                        }
                        adMarvelEvent.m261a((String[]) arrayList.toArray(new String[arrayList.size()]));
                        this.f507a.add(adMarvelEvent);
                    }
                }
            }
        }
        m265a(true);
    }

    public ArrayList<AdMarvelEvent> m264a() {
        return this.f507a;
    }

    public void m265a(boolean z) {
        this.f508b = z;
    }

    public boolean m266b() {
        return this.f508b;
    }
}
