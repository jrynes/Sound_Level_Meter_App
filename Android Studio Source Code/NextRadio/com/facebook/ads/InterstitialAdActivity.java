package com.facebook.ads;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.facebook.ads.internal.C0474g;
import com.facebook.ads.internal.view.C0557h;
import com.facebook.ads.internal.view.C0557h.C0406a;
import com.facebook.ads.internal.view.C0558f;
import com.facebook.ads.internal.view.C0566m;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

public class InterstitialAdActivity extends Activity {
    public static final String PREDEFINED_ORIENTATION_KEY = "predefinedOrientationKey";
    public static final String VIDEO_PLAY_REPORT_URL = "videoPlayReportURL";
    public static final String VIDEO_TIME_REPORT_URL = "videoTimeReportURL";
    public static final String VIDEO_URL = "videoURL";
    public static final String VIEW_TYPE = "viewType";
    private RelativeLayout f1446a;
    private C0474g f1447b;
    private int f1448c;
    private int f1449d;
    private int f1450e;
    private boolean f1451f;
    private String f1452g;
    private C0557h f1453h;
    private int f1454i;

    /* renamed from: com.facebook.ads.InterstitialAdActivity.1 */
    class C04051 implements OnClickListener {
        final /* synthetic */ InterstitialAdActivity f1441a;

        C04051(InterstitialAdActivity interstitialAdActivity) {
            this.f1441a = interstitialAdActivity;
        }

        public void onClick(View view) {
            this.f1441a.finish();
        }
    }

    /* renamed from: com.facebook.ads.InterstitialAdActivity.2 */
    class C04072 implements C0406a {
        final /* synthetic */ InterstitialAdActivity f1442a;

        C04072(InterstitialAdActivity interstitialAdActivity) {
            this.f1442a = interstitialAdActivity;
        }

        public void m1082a(View view) {
            this.f1442a.f1446a.addView(view);
            if (this.f1442a.f1447b != null) {
                this.f1442a.f1446a.addView(this.f1442a.f1447b);
            }
        }

        public void m1083a(String str) {
            this.f1442a.m1090a(str);
        }
    }

    /* renamed from: com.facebook.ads.InterstitialAdActivity.3 */
    class C04083 implements C0406a {
        final /* synthetic */ InterstitialAdActivity f1443a;

        C04083(InterstitialAdActivity interstitialAdActivity) {
            this.f1443a = interstitialAdActivity;
        }

        public void m1084a(View view) {
            this.f1443a.f1446a.addView(view);
            if (this.f1443a.f1447b != null) {
                this.f1443a.f1446a.addView(this.f1443a.f1447b);
            }
        }

        public void m1085a(String str) {
            this.f1443a.m1090a(str);
        }
    }

    /* renamed from: com.facebook.ads.InterstitialAdActivity.4 */
    static /* synthetic */ class C04094 {
        static final /* synthetic */ int[] f1444a;

        static {
            f1444a = new int[Type.values().length];
            try {
                f1444a[Type.VIDEO.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1444a[Type.DISPLAY.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public enum Type {
        DISPLAY,
        VIDEO
    }

    public InterstitialAdActivity() {
        this.f1451f = false;
        this.f1454i = -1;
    }

    private void m1087a(int i, int i2) {
        int i3 = i2 >= i ? 1 : 0;
        int rotation = ((WindowManager) getSystemService("window")).getDefaultDisplay().getRotation();
        if (i3 != 0) {
            switch (rotation) {
                case Zone.PRIMARY /*1*/:
                case Zone.SECONDARY /*2*/:
                    setRequestedOrientation(9);
                    return;
                default:
                    setRequestedOrientation(1);
                    return;
            }
        }
        switch (rotation) {
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
                setRequestedOrientation(8);
            default:
                setRequestedOrientation(0);
        }
    }

    private void m1088a(Intent intent, Bundle bundle) {
        if (bundle != null) {
            this.f1448c = bundle.getInt("lastRequestedOrientation", -1);
            this.f1452g = bundle.getString("adInterstitialUniqueId");
            this.f1453h.m1607a(intent, bundle);
            this.f1451f = true;
            return;
        }
        this.f1449d = intent.getIntExtra("displayWidth", 0);
        this.f1450e = intent.getIntExtra("displayHeight", 0);
        this.f1452g = intent.getStringExtra("adInterstitialUniqueId");
        this.f1453h.m1607a(intent, bundle);
    }

    private void m1090a(String str) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(str + Headers.SEPERATOR + this.f1452g));
    }

    public void finish() {
        this.f1446a.removeAllViews();
        this.f1453h.m1610c();
        m1090a("com.facebook.ads.interstitial.dismissed");
        super.finish();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(Flags.FLAG5, Flags.FLAG5);
        this.f1446a = new RelativeLayout(this);
        this.f1446a.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        setContentView(this.f1446a, new LayoutParams(-1, -1));
        Intent intent = getIntent();
        if (intent.getBooleanExtra("useNativeCloseButton", false)) {
            this.f1447b = new C0474g(this);
            this.f1447b.setId(100002);
            this.f1447b.setOnClickListener(new C04051(this));
        }
        switch (C04094.f1444a[((Type) intent.getSerializableExtra(VIEW_TYPE)).ordinal()]) {
            case Zone.PRIMARY /*1*/:
                this.f1453h = new C0566m(this, new C04072(this));
                break;
            default:
                this.f1453h = new C0558f(this, new C04083(this));
                break;
        }
        this.f1454i = intent.getIntExtra(PREDEFINED_ORIENTATION_KEY, -1);
        m1088a(intent, bundle);
        m1090a("com.facebook.ads.interstitial.displayed");
    }

    public void onPause() {
        super.onPause();
        this.f1453h.m1606a();
    }

    public void onRestart() {
        super.onRestart();
        this.f1451f = true;
    }

    public void onResume() {
        super.onResume();
        this.f1453h.m1609b();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.f1453h.m1608a(bundle);
        bundle.putInt("lastRequestedOrientation", this.f1448c);
        bundle.putString("adInterstitialUniqueId", this.f1452g);
    }

    public void onStart() {
        super.onStart();
        if (this.f1454i != -1) {
            setRequestedOrientation(this.f1454i);
        } else if (!(this.f1449d == 0 || this.f1450e == 0)) {
            if (!this.f1451f) {
                m1087a(this.f1449d, this.f1450e);
            } else if (this.f1448c >= 0) {
                setRequestedOrientation(this.f1448c);
                this.f1448c = -1;
            }
        }
        this.f1451f = false;
    }

    public void setRequestedOrientation(int i) {
        this.f1448c = i;
        super.setRequestedOrientation(i);
    }
}
