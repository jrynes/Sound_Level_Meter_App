package com.admarvel.android.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.admarvel.android.ads.AdMarvelInternalWebView;
import java.lang.ref.WeakReference;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.admarvel.android.util.f */
public class AdMarvelSensorManager {
    private static AdMarvelSensorManager f1006m;
    private int f1007a;
    private float f1008b;
    private Sensor f1009c;
    private Sensor f1010d;
    private SensorManager f1011e;
    private WeakReference<AdMarvelInternalWebView> f1012f;
    private Boolean f1013g;
    private boolean f1014h;
    private String f1015i;
    private String f1016j;
    private String f1017k;
    private boolean f1018l;
    private SensorEventListener f1019n;

    /* renamed from: com.admarvel.android.util.f.1 */
    class AdMarvelSensorManager implements SensorEventListener {
        final float f993a;
        final /* synthetic */ AdMarvelSensorManager f994b;
        private long f995c;
        private long f996d;
        private long f997e;
        private long f998f;
        private float f999g;
        private float[] f1000h;
        private float[] f1001i;
        private float[] f1002j;
        private final float[] f1003k;
        private final float[] f1004l;
        private final float[] f1005m;

        AdMarvelSensorManager(AdMarvelSensorManager adMarvelSensorManager) {
            this.f994b = adMarvelSensorManager;
            this.f995c = 0;
            this.f996d = 0;
            this.f997e = 0;
            this.f998f = 0;
            this.f999g = 0.0f;
            this.f1003k = new float[]{0.0f, 0.0f, 0.0f};
            this.f1004l = new float[]{0.0f, 0.0f, 0.0f};
            this.f1005m = new float[]{0.0f, 0.0f, 0.0f};
            this.f993a = 0.8f;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            this.f995c = System.currentTimeMillis();
            if (event.sensor.getType() == 1) {
                this.f1000h = (float[]) event.values.clone();
            }
            if (event.sensor.getType() == 2) {
                this.f1002j = (float[]) event.values.clone();
            }
            if (!(this.f1000h == null || this.f1002j == null)) {
                float[] fArr = new float[9];
                if (SensorManager.getRotationMatrix(fArr, new float[9], this.f1000h, this.f1002j)) {
                    float[] fArr2 = new float[3];
                    SensorManager.getOrientation(fArr, fArr2);
                    float round = ((float) Math.round(Math.toDegrees((double) fArr2[0]) * 2.0d)) / 2.0f;
                    if (((double) round) < 0.0d) {
                        round += 360.0f;
                    }
                    this.f994b.m585b(round);
                }
            }
            if (this.f1000h == null) {
                return;
            }
            if (this.f997e == 0) {
                this.f997e = this.f995c;
                this.f998f = this.f995c;
                this.f1001i = (float[]) this.f1000h.clone();
                return;
            }
            this.f996d = this.f995c - this.f997e;
            if (this.f996d > 0) {
                if (this.f995c - this.f998f >= ((long) this.f994b.f1007a)) {
                    this.f999g = (Math.abs(((((this.f1000h[0] + this.f1000h[1]) + this.f1000h[2]) - this.f1001i[0]) - this.f1001i[1]) - this.f1001i[2]) / ((float) (this.f995c - this.f998f))) * 100.0f;
                    this.f998f = this.f995c;
                    if (this.f999g > this.f994b.f1008b) {
                        this.f994b.m580a(this.f999g);
                    }
                }
                this.f1005m[0] = (this.f1005m[0] * 0.8f) + (this.f1000h[0] * 0.19999999f);
                this.f1005m[1] = (this.f1005m[1] * 0.8f) + (this.f1000h[1] * 0.19999999f);
                this.f1005m[2] = (this.f1005m[2] * 0.8f) + (this.f1000h[2] * 0.19999999f);
                this.f1003k[0] = this.f1000h[0] - this.f1005m[0];
                this.f1003k[1] = this.f1000h[1] - this.f1005m[1];
                this.f1003k[2] = this.f1000h[2] - this.f1005m[2];
                this.f1004l[0] = this.f1000h[0] - this.f1001i[0];
                this.f1004l[1] = this.f1000h[1] - this.f1001i[1];
                this.f1004l[2] = this.f1000h[2] - this.f1001i[2];
                this.f994b.m581a(this.f1000h[0], this.f1000h[1], this.f1000h[2], this.f1003k[0], this.f1003k[1], this.f1003k[2], this.f1004l[0], this.f1004l[1], this.f1004l[2]);
                this.f1001i = (float[]) this.f1000h.clone();
                this.f997e = this.f995c;
            }
        }
    }

    static {
        f1006m = null;
    }

    private AdMarvelSensorManager() {
        this.f1007a = ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        this.f1008b = 2.0f;
        this.f1014h = false;
        this.f1015i = null;
        this.f1016j = null;
        this.f1017k = null;
        this.f1018l = false;
        this.f1019n = new AdMarvelSensorManager(this);
    }

    public static AdMarvelSensorManager m579a() {
        if (f1006m == null) {
            f1006m = new AdMarvelSensorManager();
        }
        return f1006m;
    }

    private void m580a(float f) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f1012f.get();
        if (adMarvelInternalWebView != null && this.f1015i != null) {
            adMarvelInternalWebView.m315e(this.f1015i + "()");
        }
    }

    private void m581a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f1012f.get();
        if (adMarvelInternalWebView != null && this.f1016j != null) {
            adMarvelInternalWebView.m315e(this.f1016j + "(" + f + Stomp.COMMA + f2 + Stomp.COMMA + f3 + Stomp.COMMA + f4 + Stomp.COMMA + f5 + Stomp.COMMA + f6 + Stomp.COMMA + f7 + Stomp.COMMA + f8 + Stomp.COMMA + f9 + ")");
        }
    }

    private void m585b(float f) {
        AdMarvelInternalWebView adMarvelInternalWebView = (AdMarvelInternalWebView) this.f1012f.get();
        if (adMarvelInternalWebView != null && this.f1017k != null) {
            adMarvelInternalWebView.m315e(this.f1017k + "(" + f + ")");
        }
    }

    private void m587d() {
        this.f1015i = null;
        this.f1016j = null;
        this.f1017k = null;
    }

    public void m588a(Context context, AdMarvelInternalWebView adMarvelInternalWebView) {
        this.f1012f = new WeakReference(adMarvelInternalWebView);
        if (!this.f1014h) {
            this.f1011e = (SensorManager) context.getSystemService("sensor");
            this.f1009c = this.f1011e.getDefaultSensor(1);
            this.f1010d = this.f1011e.getDefaultSensor(2);
            this.f1014h = this.f1011e.registerListener(this.f1019n, this.f1009c, 3);
        }
        if (this.f1018l) {
            this.f1011e.registerListener(this.f1019n, this.f1010d, 3);
            this.f1018l = false;
        }
    }

    public void m589a(String str) {
        if (str != null) {
            this.f1015i = str;
        }
    }

    public void m590a(String str, String str2) {
        if (str != null) {
            this.f1008b = new Float(str).floatValue();
        }
        if (str2 != null) {
            this.f1007a = Integer.parseInt(str2) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        }
    }

    public boolean m591a(Context context) {
        if (this.f1013g == null) {
            if (context != null) {
                this.f1011e = (SensorManager) context.getSystemService("sensor");
                this.f1013g = new Boolean(this.f1011e.getSensorList(1).size() > 0);
            } else {
                this.f1013g = Boolean.FALSE;
            }
        }
        return this.f1013g.booleanValue();
    }

    public void m592b(String str) {
        if (str != null) {
            this.f1016j = str;
        }
    }

    public boolean m593b() {
        return this.f1014h;
    }

    public boolean m594b(Context context) {
        if (this.f1013g == null) {
            if (context != null) {
                this.f1011e = (SensorManager) context.getSystemService("sensor");
                this.f1013g = new Boolean(this.f1011e.getSensorList(2).size() > 0);
            } else {
                this.f1013g = Boolean.FALSE;
            }
        }
        return this.f1013g.booleanValue();
    }

    public void m595c() {
        this.f1014h = false;
        try {
            if (!(this.f1011e == null || this.f1019n == null)) {
                this.f1011e.unregisterListener(this.f1019n);
            }
        } catch (Exception e) {
        }
        m587d();
    }

    public void m596c(String str) {
        if (str != null) {
            this.f1018l = true;
            this.f1017k = str;
        }
    }
}
