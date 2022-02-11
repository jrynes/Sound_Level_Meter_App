package com.amazon.device.associates;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.amazon.device.associates.ProductPopoverActivity.C0350a;
import io.fabric.sdk.android.services.common.AbstractSpiCall;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.fabric.sdk.android.services.network.HttpRequest;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.Zone;

/* renamed from: com.amazon.device.associates.d */
public class ProductPreviewHelper {
    private static final String f1313a;
    private final RelativeLayout f1314b;
    private final C0350a f1315c;
    private RelativeLayout f1316d;
    private WebView f1317e;
    private final Drawable f1318f;
    private final Drawable f1319g;
    private final Drawable f1320h;
    private final Drawable f1321i;
    private final float f1322j;
    private int f1323k;
    private int f1324l;
    private int f1325m;
    private int f1326n;
    private final int f1327o;
    private int f1328p;
    private ProductPreviewHelper f1329q;
    private String f1330r;
    private final OnTouchListener f1331s;

    /* renamed from: com.amazon.device.associates.d.1 */
    static /* synthetic */ class ProductPreviewHelper {
        static final /* synthetic */ int[] f1307a;

        static {
            f1307a = new int[ProductPreviewHelper.values().length];
            try {
                f1307a[ProductPreviewHelper.UP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1307a[ProductPreviewHelper.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1307a[ProductPreviewHelper.DOWN.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f1307a[ProductPreviewHelper.LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* renamed from: com.amazon.device.associates.d.a */
    private enum ProductPreviewHelper {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    static {
        f1313a = ProductPreviewHelper.class.getName();
    }

    public ProductPreviewHelper(RelativeLayout relativeLayout, C0350a c0350a) {
        this.f1323k = 300;
        this.f1324l = 160;
        this.f1329q = null;
        this.f1331s = new ProductPreviewHelper(this);
        this.f1314b = relativeLayout;
        this.f1315c = c0350a;
        int identifier = relativeLayout.getResources().getIdentifier("status_bar_height", "dimen", AbstractSpiCall.ANDROID_CLIENT_TYPE);
        if (identifier > 0) {
            this.f1328p = relativeLayout.getResources().getDimensionPixelSize(identifier);
        }
        this.f1318f = relativeLayout.getResources().getDrawable(bp.m898a("quickaction_arrow_up", "drawable"));
        this.f1319g = relativeLayout.getResources().getDrawable(bp.m898a("quickaction_arrow_right", "drawable"));
        this.f1320h = relativeLayout.getResources().getDrawable(bp.m898a("quickaction_arrow_down", "drawable"));
        this.f1321i = relativeLayout.getResources().getDrawable(bp.m898a("quickaction_arrow_left", "drawable"));
        if (this.f1318f == null || this.f1319g == null || this.f1320h == null || this.f1321i == null) {
            Log.m1016b(f1313a, "Resouce images required for Popover window is not found");
            throw new NullPointerException("Images not found in the resouces folder");
        }
        relativeLayout.setGravity(51);
        this.f1322j = relativeLayout.getResources().getDisplayMetrics().density;
        this.f1324l = Math.round(((float) this.f1324l) * this.f1322j);
        this.f1323k = Math.round(((float) this.f1323k) * this.f1322j);
        this.f1327o = Math.round(7.0f * this.f1322j);
        Log.m1013a(f1313a + " Default Config", String.format("Scale %f, Height %d, Width %d", new Object[]{Float.valueOf(this.f1322j), Integer.valueOf(this.f1324l), Integer.valueOf(this.f1323k)}));
    }

    public void m954a(Rect rect, Rect rect2) {
        this.f1316d = new RelativeLayout(this.f1314b.getContext());
        this.f1316d.setLayoutParams(new LayoutParams(-2, -2));
        this.f1316d.setBackgroundDrawable(this.f1314b.getResources().getDrawable(bp.m898a("bg_custom", "drawable")));
        this.f1316d.setPadding(this.f1327o, this.f1327o, this.f1327o, this.f1327o);
        m946b(rect, rect2);
        if (this.f1317e != null) {
            this.f1316d.addView(this.f1317e);
            m952h(rect, rect2);
            m953i(rect, rect2);
        }
        this.f1314b.addView(this.f1316d);
    }

    private void m946b(Rect rect, Rect rect2) {
        CookieSyncManager.createInstance(this.f1314b.getContext());
        CookieManager instance = CookieManager.getInstance();
        instance.setAcceptCookie(false);
        instance.removeAllCookie();
        Rect c = m947c(rect, rect2);
        Log.m1013a(f1313a + " Location:", this.f1329q.toString());
        this.f1325m = c.width();
        this.f1326n = c.height();
        Log.m1013a(f1313a + " Dimension:", this.f1325m + ", " + this.f1326n);
        this.f1317e = new WebView(this.f1314b.getContext());
        this.f1317e.setLayoutParams(new LayoutParams(this.f1325m, this.f1326n));
        this.f1317e.getSettings().setJavaScriptEnabled(false);
        this.f1317e.setVerticalScrollBarEnabled(false);
        this.f1317e.setHorizontalScrollBarEnabled(false);
        this.f1317e.loadDataWithBaseURL("blarg://ignored", Stomp.EMPTY, WebRequest.CONTENT_TYPE_HTML, HttpRequest.CHARSET_UTF8, Stomp.EMPTY);
        this.f1317e.setOnTouchListener(this.f1331s);
    }

    private Rect m947c(Rect rect, Rect rect2) {
        Rect d = m948d(rect, rect2);
        Rect e = m949e(rect, rect2);
        Rect f = m950f(rect, rect2);
        Rect g = m951g(rect, rect2);
        int width = d.width() * d.height();
        int width2 = e.width() * e.height();
        int width3 = f.width() * f.height();
        int width4 = g.width() * g.height();
        if (width >= width2 && width >= width3 && width >= width4) {
            this.f1329q = ProductPreviewHelper.UP;
            return d;
        } else if (width2 >= width && width2 >= width3 && width2 >= width4) {
            this.f1329q = ProductPreviewHelper.RIGHT;
            return e;
        } else if (width3 < width || width3 < width2 || width3 < width4) {
            this.f1329q = ProductPreviewHelper.LEFT;
            return g;
        } else {
            this.f1329q = ProductPreviewHelper.DOWN;
            return f;
        }
    }

    private Rect m948d(Rect rect, Rect rect2) {
        int i = this.f1323k;
        int i2 = this.f1324l;
        if (i2 > (rect2.top - rect.top) - this.f1320h.getIntrinsicHeight()) {
            i2 = ((rect2.top - rect.top) - this.f1320h.getIntrinsicHeight()) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = (int) (((((double) i2) * 1.0d) / ((double) this.f1324l)) * ((double) this.f1323k));
        }
        if (i > rect.right - rect.left) {
            i2 = (rect.right - rect.left) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = i2;
            i2 = (int) (((((double) i2) * 1.0d) / ((double) this.f1323k)) * ((double) this.f1324l));
        }
        return new Rect(0, 0, i, i2);
    }

    private Rect m949e(Rect rect, Rect rect2) {
        int i = this.f1323k;
        int i2 = this.f1324l;
        if (i > (rect.right - rect2.right) - this.f1321i.getIntrinsicWidth()) {
            i2 = ((rect.right - rect2.right) - this.f1321i.getIntrinsicWidth()) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = i2;
            i2 = (int) (((((double) i2) * 1.0d) / ((double) this.f1323k)) * ((double) this.f1324l));
        }
        if (i2 > rect.bottom - rect.top) {
            i2 = (rect.bottom - rect.top) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = (int) (((((double) i2) * 1.0d) / ((double) this.f1324l)) * ((double) this.f1323k));
        }
        return new Rect(0, 0, i, i2);
    }

    private Rect m950f(Rect rect, Rect rect2) {
        int i = this.f1323k;
        int i2 = this.f1324l;
        if (i2 > (rect.bottom - rect2.bottom) - this.f1318f.getIntrinsicHeight()) {
            i2 = ((rect.bottom - rect2.bottom) - this.f1318f.getIntrinsicHeight()) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = (int) (((((double) i2) * 1.0d) / ((double) this.f1324l)) * ((double) this.f1323k));
        }
        if (i > rect.right - rect.left) {
            i2 = (rect.right - rect.left) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = i2;
            i2 = (int) (((((double) i2) * 1.0d) / ((double) this.f1323k)) * ((double) this.f1324l));
        }
        return new Rect(0, 0, i, i2);
    }

    private Rect m951g(Rect rect, Rect rect2) {
        int i = this.f1323k;
        int i2 = this.f1324l;
        if (i > (rect2.left - rect.left) - this.f1319g.getIntrinsicWidth()) {
            i2 = ((rect2.left - rect.left) - this.f1319g.getIntrinsicWidth()) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = i2;
            i2 = (int) (((((double) i2) * 1.0d) / ((double) this.f1323k)) * ((double) this.f1324l));
        }
        if (i2 > rect.bottom - rect.top) {
            i2 = (rect.bottom - rect.top) - (this.f1327o * 2);
            if (i2 < 0) {
                i2 = 0;
            }
            i = (int) (((((double) i2) * 1.0d) / ((double) this.f1324l)) * ((double) this.f1323k));
        }
        return new Rect(0, 0, i, i2);
    }

    private void m952h(Rect rect, Rect rect2) {
        int centerX;
        int i = 0;
        this.f1316d.measure(0, 0);
        int measuredWidth = this.f1316d.getMeasuredWidth();
        int measuredHeight = this.f1316d.getMeasuredHeight();
        int i2 = (int) (5.0f * this.f1322j);
        switch (ProductPreviewHelper.f1307a[this.f1329q.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                centerX = (rect2.centerX() - rect.left) - (measuredWidth / 2);
                if (centerX >= 0) {
                    if (centerX + measuredWidth > rect.right - rect.left) {
                        i = rect.right - measuredWidth;
                    } else {
                        i = centerX;
                    }
                }
                measuredWidth = (((rect2.top - rect.top) - measuredHeight) - this.f1320h.getIntrinsicHeight()) + i2;
                centerX = i;
                break;
            case Zone.SECONDARY /*2*/:
                centerX = ((rect2.right - rect.left) + this.f1321i.getIntrinsicWidth()) - i2;
                measuredWidth = (rect2.centerY() - rect.top) - (measuredHeight / 2);
                if (measuredWidth >= 0) {
                    if (measuredWidth + measuredHeight > rect.bottom - rect.top) {
                        measuredWidth = (rect.bottom - rect.top) - measuredHeight;
                        break;
                    }
                }
                measuredWidth = 0;
                break;
                break;
            case Protocol.GGP /*3*/:
                centerX = (rect2.centerX() - rect.left) - (measuredWidth / 2);
                if (centerX >= 0) {
                    if (centerX + measuredWidth > rect.right - rect.left) {
                        i = rect.right - measuredWidth;
                    } else {
                        i = centerX;
                    }
                }
                measuredWidth = ((rect2.bottom - rect.top) + this.f1318f.getIntrinsicHeight()) - i2;
                centerX = i;
                break;
            case Type.MF /*4*/:
                centerX = (((rect2.left - rect.left) - measuredWidth) - this.f1319g.getIntrinsicWidth()) + i2;
                measuredWidth = (rect2.centerY() - rect.top) - (measuredHeight / 2);
                if (measuredWidth >= 0) {
                    if (measuredWidth + measuredHeight > rect.bottom - rect.top) {
                        measuredWidth = (rect.bottom - rect.top) - measuredHeight;
                        break;
                    }
                }
                measuredWidth = 0;
                break;
                break;
            default:
                centerX = 0;
                measuredWidth = 0;
                break;
        }
        LayoutParams layoutParams = (LayoutParams) this.f1316d.getLayoutParams();
        layoutParams.leftMargin = centerX;
        layoutParams.topMargin = (rect.top - this.f1328p) + measuredWidth;
    }

    private void m953i(Rect rect, Rect rect2) {
        Drawable drawable;
        int intrinsicWidth;
        int centerX;
        int i;
        int i2 = 0;
        View imageView = new ImageView(this.f1314b.getContext());
        switch (ProductPreviewHelper.f1307a[this.f1329q.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                drawable = this.f1320h;
                intrinsicWidth = drawable.getIntrinsicWidth();
                i2 = drawable.getIntrinsicHeight();
                centerX = (rect2.centerX() - rect.left) - (intrinsicWidth / 2);
                i = (rect2.top - rect.top) - i2;
                break;
            case Zone.SECONDARY /*2*/:
                drawable = this.f1321i;
                intrinsicWidth = drawable.getIntrinsicWidth();
                i2 = drawable.getIntrinsicHeight();
                centerX = rect2.right - rect.left;
                i = (rect2.centerY() - rect.top) - (i2 / 2);
                break;
            case Protocol.GGP /*3*/:
                drawable = this.f1318f;
                intrinsicWidth = drawable.getIntrinsicWidth();
                i2 = drawable.getIntrinsicHeight();
                centerX = (rect2.centerX() - rect.left) - (intrinsicWidth / 2);
                i = rect2.bottom - rect.top;
                break;
            case Type.MF /*4*/:
                drawable = this.f1319g;
                intrinsicWidth = drawable.getIntrinsicWidth();
                i2 = drawable.getIntrinsicHeight();
                centerX = (rect2.left - rect.left) - intrinsicWidth;
                i = (rect2.centerY() - rect.top) - (i2 / 2);
                break;
            default:
                i = 0;
                centerX = 0;
                drawable = null;
                intrinsicWidth = 0;
                break;
        }
        imageView.setImageDrawable(drawable);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(intrinsicWidth, i2);
        layoutParams.leftMargin = centerX;
        layoutParams.topMargin = (rect.top - this.f1328p) + i;
        this.f1314b.addView(imageView, layoutParams);
    }

    public void m955a(String str) throws Exception {
        String a = m944a(str, ((int) (((float) this.f1325m) / this.f1322j)) - 10, ((int) (((float) this.f1326n) / this.f1322j)) - 10);
        if (a == null) {
            Log.m1013a(f1313a, "Invalid ASIN: " + str);
            this.f1315c.m655a(PopoverStatus.INVALID_PRODUCT_ID);
            return;
        }
        this.f1317e.loadDataWithBaseURL("blarg://ignored", a, WebRequest.CONTENT_TYPE_HTML, HttpRequest.CHARSET_UTF8, Stomp.EMPTY);
        new av().execute(new ag[]{new bh(br.m908a(this.f1330r, "tag"), br.m908a(this.f1330r, "linkCode"), str)});
    }

    private String m944a(String str, int i, int i2) throws Exception {
        String a = ((bu) ((AsyncPopoverHtmlTemplateTask) al.m752a(AsyncPopoverHtmlTemplateTask.class)).m778j()).m924a();
        bs a2 = PopoverCacheManager.m1002a(str);
        if (a2 == null || !a2.m921g()) {
            return null;
        }
        this.f1330r = br.m911c(str);
        a = a.replace("$WIDTH", String.valueOf(i)).replace("$HEIGHT", String.valueOf(i2)).replace("$TITLE", a2.m916b());
        String a3 = a2.m915a();
        return a.replace("$ICON_SRC", a3.substring(0, a3.lastIndexOf(ActiveMQDestination.PATH_SEPERATOR)) + "._SL" + ((int) (((double) i) * 0.3d)) + EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR + a3.substring(a3.lastIndexOf(ActiveMQDestination.PATH_SEPERATOR))).replace("$PRICE", a2.m917c()).replace("$REVIEW_COUNT", a2.m919e()).replace("$RETAIL_URL", this.f1330r == null ? a2.m920f() : this.f1330r).replace("$RATING_DISPLACEMENT", m942a(a2.m918d()));
    }

    private String m942a(double d) {
        int i = (int) d;
        if (d == ((double) i)) {
            i = 65 - (i * 13);
        } else {
            i = 193 - (i * 13);
        }
        return String.valueOf(i);
    }
}
