package com.amazon.device.associates;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.amazon.device.associates.ProductPopoverActivity.C0350a;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Zone;

/* renamed from: com.amazon.device.associates.b */
public class ProductDetailHelper {
    private static final String f1228a;
    private static Boolean f1229b;
    private boolean f1230c;
    private boolean f1231d;
    private String f1232e;
    private final C0350a f1233f;
    private final RelativeLayout f1234g;
    private int f1235h;
    private int f1236i;
    private WebView f1237j;
    private RelativeLayout f1238k;
    private TextView f1239l;
    private ProgressBar f1240m;
    private ImageView f1241n;
    private Drawable f1242o;

    /* renamed from: com.amazon.device.associates.b.1 */
    class ProductDetailHelper implements OnTouchListener {
        final /* synthetic */ ProductDetailHelper f1216a;

        ProductDetailHelper(ProductDetailHelper productDetailHelper) {
            this.f1216a = productDetailHelper;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.f1216a.f1237j.loadUrl("javascript:(function() {hideMessageBox();})();");
            }
            return false;
        }
    }

    /* renamed from: com.amazon.device.associates.b.2 */
    class ProductDetailHelper extends WebViewClient {
        boolean f1217a;
        boolean f1218b;
        final /* synthetic */ String f1219c;
        final /* synthetic */ ProductDetailHelper f1220d;

        ProductDetailHelper(ProductDetailHelper productDetailHelper, String str) {
            this.f1220d = productDetailHelper;
            this.f1219c = str;
            this.f1217a = false;
            this.f1218b = false;
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            this.f1220d.f1231d = true;
        }

        public void onPageFinished(WebView webView, String str) {
            if (!this.f1217a) {
                if (this.f1220d.f1231d) {
                    Log.m1013a(ProductDetailHelper.f1228a, "Error loading UDP page in webview.");
                    new MetricsRecorderCall("UDPPopoverPageLoadFailed").m973d();
                    this.f1220d.f1233f.m655a(PopoverStatus.FAILED);
                } else if (this.f1220d.m838c(str)) {
                    Log.m1013a(ProductDetailHelper.f1228a, "onPageFinished: " + str);
                    if (!this.f1218b) {
                        this.f1220d.f1237j.loadUrl(this.f1219c);
                        this.f1218b = true;
                    }
                    this.f1220d.f1240m.setVisibility(8);
                    this.f1220d.f1239l.setVisibility(8);
                    this.f1220d.f1237j.setVisibility(0);
                    this.f1220d.f1241n.setVisibility(0);
                    if (!ProductDetailHelper.f1229b.booleanValue() || (ProductDetailHelper.f1229b.booleanValue() && this.f1220d.f1231d)) {
                        this.f1217a = true;
                        Log.m1013a(ProductDetailHelper.f1228a, "onPageFinished completed. Setting flag to true!!");
                    }
                } else {
                    br.m912d(this.f1220d.m841d(str));
                    this.f1220d.f1233f.m655a(PopoverStatus.FAILED);
                    Log.m1018c(ProductDetailHelper.f1228a, "Popover dismissed due to security reasons. Opening the url in browser");
                }
            }
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (!((ProductDetailHelper.f1229b.booleanValue() && str.matches("^((http|https)://)(www[.])?(amazon[.])(com)(/gp/dmusic/device/mp3/store)([/])?(\\Q?ie=UTF8&*Version*=1&*entries*=0#\\E)(album|track)(/)?(([A-Za-z0-9])*)?.*")) || str.matches("^((http|https)://)(www[.])?(amazon[.])(com)(/gp/dmusic/device/mp3/store/)(album|track)(/)?(([A-Za-z0-9])*)?.*")) || this.f1217a) {
                Log.m1013a(ProductDetailHelper.f1228a, "Calling OverrideLinkInvocation for url : " + str);
                br.m912d(this.f1220d.m841d(str));
                this.f1220d.f1233f.m655a(PopoverStatus.SUCCESS);
                return true;
            }
            Log.m1013a(ProductDetailHelper.f1228a, "Avoiding the redirect for " + str);
            return false;
        }
    }

    /* renamed from: com.amazon.device.associates.b.3 */
    class ProductDetailHelper extends WebChromeClient {
        final /* synthetic */ ProductDetailHelper f1221a;

        ProductDetailHelper(ProductDetailHelper productDetailHelper) {
            this.f1221a = productDetailHelper;
        }

        public void onProgressChanged(WebView webView, int i) {
            this.f1221a.f1240m.setProgress(i);
        }
    }

    /* renamed from: com.amazon.device.associates.b.4 */
    static /* synthetic */ class ProductDetailHelper {
        static final /* synthetic */ int[] f1222a;

        static {
            f1222a = new int[ProductDetailHelper.values().length];
            try {
                f1222a[ProductDetailHelper.MP3_ALBUM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1222a[ProductDetailHelper.MP3_TRACK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    /* renamed from: com.amazon.device.associates.b.a */
    private enum ProductDetailHelper {
        MP3_ALBUM("Digital Music Album"),
        MP3_TRACK("Digital Music Track"),
        OTHERS("Others");
        
        private final String f1227d;

        private ProductDetailHelper(String str) {
            this.f1227d = str;
        }

        public static ProductDetailHelper m825a(String str) {
            if (str == null) {
                return OTHERS;
            }
            for (ProductDetailHelper productDetailHelper : ProductDetailHelper.values()) {
                if (productDetailHelper.f1227d.equals(str)) {
                    return productDetailHelper;
                }
            }
            return OTHERS;
        }
    }

    static {
        f1228a = ProductDetailHelper.class.getName();
        f1229b = null;
    }

    public ProductDetailHelper(RelativeLayout relativeLayout, C0350a c0350a) {
        this.f1230c = false;
        this.f1231d = false;
        this.f1232e = null;
        this.f1234g = relativeLayout;
        this.f1233f = c0350a;
    }

    protected void m853a(String str) {
        String e = br.m913e(this.f1232e);
        if (e.charAt(e.length() - 1) == '?') {
            e = e.substring(0, e.length() - 1);
        }
        Log.m1013a(f1228a, "Original URL : " + this.f1232e + ". New UDP URL" + e);
        if (m850k()) {
            this.f1237j.loadUrl(e);
            new av().execute(new ag[]{new bh(br.m908a(this.f1232e, "tag"), br.m908a(this.f1232e, "linkCode"), str)});
            return;
        }
        br.m912d(m841d(e));
        this.f1233f.m655a(PopoverStatus.SUCCESS);
    }

    protected void m852a() {
        this.f1238k = new RelativeLayout(this.f1234g.getContext());
        this.f1238k.setLayoutParams(new LayoutParams(-1, -1));
        this.f1238k.setGravity(17);
        m848i();
        m845f();
        this.f1234g.addView(this.f1238k);
    }

    private void m845f() {
        String e;
        this.f1237j = new WebView(this.f1234g.getContext());
        int round = Math.round(((float) this.f1235h) * 0.08f);
        int round2 = Math.round(((float) this.f1235h) * 0.08f);
        int round3 = Math.round(((float) this.f1236i) * 0.08f);
        int round4 = Math.round(((float) this.f1236i) * 0.08f);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -1);
        layoutParams.setMargins(round3, round, round4, round2);
        this.f1237j.setLayoutParams(layoutParams);
        this.f1237j.setVisibility(8);
        this.f1237j.getSettings().setJavaScriptEnabled(true);
        if (this.f1230c) {
            this.f1237j.getSettings().setDomStorageEnabled(true);
        }
        if (f1229b.booleanValue()) {
            this.f1237j.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166  Safari/535.19");
        }
        this.f1237j.setWebChromeClient(m846g());
        bu buVar = (bu) ((bv) al.m752a(bv.class)).m778j();
        if (buVar != null) {
            e = buVar.m926e();
        } else {
            e = null;
        }
        if (e == null) {
            this.f1233f.m655a(PopoverStatus.FAILED);
            return;
        }
        if (f1229b.booleanValue()) {
            e = e.replace("$CHARACTER", "<br />");
        } else {
            e = e.replace("$CHARACTER", Stomp.EMPTY);
        }
        this.f1237j.setWebViewClient(m835b(e));
        this.f1237j.setOnTouchListener(m847h());
        this.f1238k.addView(this.f1237j);
        m829a(round, round4);
    }

    private WebChromeClient m846g() {
        return new ProductDetailHelper(this);
    }

    private OnTouchListener m847h() {
        return new ProductDetailHelper(this);
    }

    private WebViewClient m835b(String str) {
        return new ProductDetailHelper(this, str);
    }

    private void m829a(int i, int i2) {
        this.f1241n = new ImageView(this.f1234g.getContext());
        int intrinsicWidth = this.f1242o.getIntrinsicWidth();
        int intrinsicHeight = this.f1242o.getIntrinsicHeight();
        this.f1241n.setImageDrawable(this.f1242o);
        this.f1241n.setVisibility(8);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(intrinsicWidth, intrinsicHeight);
        layoutParams.leftMargin = (this.f1236i - i2) - (intrinsicWidth / 2);
        layoutParams.topMargin = i - (intrinsicHeight / 2);
        Log.m1013a(f1228a, "Positioning the x image at : " + layoutParams.leftMargin + " " + layoutParams.topMargin);
        this.f1238k.addView(this.f1241n, layoutParams);
    }

    private void m848i() {
        this.f1239l = new TextView(this.f1234g.getContext());
        this.f1239l.setWidth(Math.round(((float) this.f1236i) * 0.5f));
        this.f1239l.setText("Loading product detail from Amazon");
        this.f1239l.setGravity(17);
        this.f1239l.setTextSize(15.0f);
        this.f1239l.setTextColor(-1);
        this.f1239l.setPadding(0, 15, 0, 0);
        this.f1239l.setVisibility(0);
        this.f1238k.addView(this.f1239l);
        this.f1240m = new ProgressBar(this.f1234g.getContext(), null, 16842872);
        this.f1240m.setVisibility(0);
        this.f1240m.setLayoutParams(new LayoutParams(Math.round(((float) this.f1236i) * 0.5f), 15));
        this.f1238k.addView(this.f1240m);
    }

    private void m830a(Rect rect) {
        this.f1235h = rect.bottom - rect.top;
        this.f1236i = rect.right - rect.left;
    }

    private void m849j() {
        if (f1229b != null) {
            return;
        }
        if (Build.MANUFACTURER == null || !Build.MANUFACTURER.equalsIgnoreCase("AMAZON")) {
            f1229b = Boolean.valueOf(false);
        } else {
            f1229b = Boolean.valueOf(true);
        }
    }

    protected void m854a(String str, Rect rect) throws Exception {
        this.f1242o = this.f1234g.getResources().getDrawable(bp.m898a("x", "drawable"));
        if (this.f1242o == null) {
            Log.m1016b(f1228a, "Unable to render Product detail popover. Required resource image is not found");
            throw new NullPointerException("Images not found in the resouces folder");
        }
        m830a(rect);
        m849j();
        bs a = PopoverCacheManager.m1002a(str);
        if (a == null) {
            Log.m1013a(f1228a, "Invalid asin." + str);
            this.f1233f.m655a(PopoverStatus.INVALID_PRODUCT_ID);
            return;
        }
        RetailURLTemplates retailURLTemplates = (RetailURLTemplates) ((be) al.m752a(be.class)).m778j();
        ProductDetailHelper a2 = m827a(a);
        m831a(a2);
        this.f1232e = m851a(retailURLTemplates, str, a2);
        Log.m1013a(f1228a, "UDP Page URL: " + this.f1232e);
    }

    private ProductDetailHelper m827a(bs bsVar) {
        if (bsVar != null) {
            return ProductDetailHelper.m825a(bsVar.m922h());
        }
        return ProductDetailHelper.OTHERS;
    }

    private void m831a(ProductDetailHelper productDetailHelper) {
        if (ProductDetailHelper.MP3_ALBUM.equals(productDetailHelper) || ProductDetailHelper.MP3_TRACK.equals(productDetailHelper)) {
            this.f1230c = true;
        }
    }

    public String m851a(RetailURLTemplates retailURLTemplates, String str, ProductDetailHelper productDetailHelper) {
        CharSequence a = br.m907a(str);
        String str2 = null;
        if (a == null) {
            Log.m1013a(f1228a, "ASIN not received. Providing Amazon homepage Url");
            str2 = br.m904a();
        } else if (retailURLTemplates != null) {
            str2 = m828a(retailURLTemplates, productDetailHelper);
        }
        if (str2 == null) {
            str2 = m828a(((be) al.m752a(be.class)).m868a(), productDetailHelper);
        }
        if (!str2.startsWith("http://")) {
            str2 = "http://" + str2;
        }
        if (str2.contains("$ASIN")) {
            str2 = str2.replace("$ASIN", a);
        }
        return str2.replace("$SUBTAG", bp.m901b());
    }

    private boolean m850k() {
        if (!this.f1230c || VERSION.SDK_INT >= 11) {
            return true;
        }
        return false;
    }

    private String m828a(RetailURLTemplates retailURLTemplates, ProductDetailHelper productDetailHelper) {
        if (!m850k() || (f1229b.booleanValue() && !this.f1230c)) {
            return (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1347f);
        }
        switch (ProductDetailHelper.f1222a[productDetailHelper.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                return (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1348g);
            case Zone.SECONDARY /*2*/:
                return (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1349h);
            default:
                return (String) retailURLTemplates.m974a().get(RetailURLTemplates.f1347f);
        }
    }

    private boolean m838c(String str) {
        if (str.matches("^((http|https)://)(www[.])?(amazon[.])(com|ca|cn|de|es|fr|it|in|co[.]jp|co[.]uk)(/gp/aw/d)?(/)?(([A-Za-z0-9])*)[?]?") || str.matches("^((http|https)://)(www[.])?(amazon[.])(com)(/gp/dmusic/device/mp3/store/)(album|track)(/)?(([A-Za-z0-9])*)?.*") || (f1229b.booleanValue() && str.matches("^((http|https)://)(www[.])?(amazon[.])(com)(/gp/dmusic/device/mp3/store)([/])?(\\Q?ie=UTF8&*Version*=1&*entries*=0#\\E)(album|track)(/)?(([A-Za-z0-9])*)?.*"))) {
            Log.m1013a(f1228a, "Whitelisted url");
            return true;
        }
        Log.m1013a(f1228a, "Not whitelisted url");
        return false;
    }

    private String m841d(String str) {
        String a = br.m908a(this.f1232e, "tag");
        String a2 = br.m908a(this.f1232e, "linkCode");
        String a3 = br.m908a(this.f1232e, "ascsubtag");
        a = "tag=" + a + "&linkCode=" + a2;
        if (a3 == null || a3.isEmpty()) {
            a2 = a;
        } else {
            a2 = a + "&ascsubtag=" + a3;
        }
        if (a2 == null) {
            return str;
        }
        a = br.m913e(str);
        if (a.charAt(a.length() - 1) == '?' || a.charAt(a.length() - 1) == '/') {
            a = a.substring(0, a.length() - 1);
        }
        int indexOf = a.indexOf("#");
        int indexOf2 = a.indexOf("?");
        if (indexOf == -1 || indexOf2 == -1) {
            if (indexOf2 != -1) {
                return a.substring(0, indexOf2 + 1) + a2 + "&" + a.substring(indexOf2 + 1);
            }
            if (indexOf != -1) {
                return a.substring(0, indexOf) + "?" + a2 + a.substring(indexOf);
            }
            return a + "?" + a2;
        } else if (indexOf < indexOf2) {
            return a.substring(0, indexOf) + "?" + a2 + a.substring(indexOf);
        } else {
            return a.substring(0, indexOf2 + 1) + a2 + "&" + a.substring(indexOf2 + 1);
        }
    }

    public boolean m855b() {
        return this.f1230c;
    }

    public WebView m856c() {
        return this.f1237j;
    }
}
