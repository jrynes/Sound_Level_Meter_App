package com.facebook.ads.internal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.admarvel.android.ads.Constants;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdView.Type;
import com.google.android.gms.analytics.ecommerce.Promotion;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers.Send;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.facebook.ads.internal.adapters.o */
public class C0456o extends C0433b {
    private final C0448p f1674b;
    private Type f1675c;
    private boolean f1676d;
    private boolean f1677e;
    private boolean f1678f;
    private View f1679g;
    private List<View> f1680h;

    public C0456o(Context context, C0417c c0417c, C0448p c0448p) {
        super(context, c0417c);
        this.f1674b = c0448p;
    }

    private String m1340b(View view) {
        try {
            return m1341c(view).toString();
        } catch (JSONException e) {
            return "Json exception";
        }
    }

    private JSONObject m1341c(View view) {
        boolean z = true;
        int i = 0;
        JSONObject jSONObject = new JSONObject();
        jSONObject.putOpt(Name.MARK, Integer.valueOf(view.getId()));
        jSONObject.putOpt(Name.LABEL, view.getClass());
        jSONObject.putOpt("origin", String.format("{x:%d, y:%d}", new Object[]{Integer.valueOf(view.getTop()), Integer.valueOf(view.getLeft())}));
        jSONObject.putOpt("size", String.format("{h:%d, w:%d}", new Object[]{Integer.valueOf(view.getHeight()), Integer.valueOf(view.getWidth())}));
        if (this.f1680h == null || !this.f1680h.contains(view)) {
            z = false;
        }
        jSONObject.putOpt("clickable", Boolean.valueOf(z));
        Object obj = DeviceInfo.ORIENTATION_UNKNOWN;
        if (view instanceof TextView) {
            obj = "text";
        } else if (view instanceof Button) {
            obj = "button";
        } else if (view instanceof ImageView) {
            obj = Constants.NATIVE_AD_IMAGE_ELEMENT;
        } else if (view instanceof MediaView) {
            obj = "mediaview";
        } else if (view instanceof ViewGroup) {
            obj = "viewgroup";
        }
        jSONObject.putOpt(Send.TYPE, obj);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            JSONArray jSONArray = new JSONArray();
            while (i < viewGroup.getChildCount()) {
                jSONArray.put(m1341c(viewGroup.getChildAt(i)));
                i++;
            }
            jSONObject.putOpt("list", jSONArray);
        }
        return jSONObject;
    }

    private String m1342d(View view) {
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            return Stomp.EMPTY;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            createBitmap.setDensity(view.getResources().getDisplayMetrics().densityDpi);
            view.draw(new Canvas(createBitmap));
            OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            createBitmap.compress(CompressFormat.JPEG, this.f1674b.m1232e(), byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (Exception e) {
            return Stomp.EMPTY;
        }
    }

    public void m1343a(View view) {
        this.f1679g = view;
    }

    public void m1344a(Type type) {
        this.f1675c = type;
    }

    public void m1345a(List<View> list) {
        this.f1680h = list;
    }

    public void m1346a(boolean z) {
        this.f1676d = z;
    }

    protected void m1347b() {
        if (this.f1674b != null) {
            Map hashMap = new HashMap();
            if (this.a != null) {
                hashMap.put("mil", Boolean.valueOf(this.a.m1105a()));
                hashMap.put("eil", Boolean.valueOf(this.a.m1106b()));
                hashMap.put("eil_source", this.a.m1107c());
            }
            if (this.f1675c != null) {
                hashMap.put("nti", String.valueOf(this.f1675c.getValue()));
            }
            if (this.f1676d) {
                hashMap.put("nhs", String.valueOf(this.f1676d));
            }
            if (this.f1677e) {
                hashMap.put("nmv", String.valueOf(this.f1677e));
            }
            if (this.f1678f) {
                hashMap.put("nmvap", String.valueOf(this.f1678f));
            }
            if (this.f1679g != null && this.f1674b.m1229b()) {
                hashMap.put(Promotion.ACTION_VIEW, m1340b(this.f1679g));
            }
            if (this.f1679g != null && this.f1674b.m1231d()) {
                hashMap.put("snapshot", m1342d(this.f1679g));
            }
            this.f1674b.m1226a(hashMap);
        }
    }

    public void m1348b(boolean z) {
        this.f1677e = z;
    }

    public void m1349c(boolean z) {
        this.f1678f = z;
    }
}
