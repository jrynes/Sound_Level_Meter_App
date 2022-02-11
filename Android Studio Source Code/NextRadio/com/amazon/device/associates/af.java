package com.amazon.device.associates;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.webkit.WebView;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;

/* compiled from: DirectShopping */
public class af {
    private static af f1180a;
    private long f1181b;

    static {
        f1180a = null;
    }

    private af(Context context, String str, Set<String> set) {
        this.f1181b = System.currentTimeMillis();
        if (context == null || str == null || Stomp.EMPTY.equals(str)) {
            Log.m1016b("MAIN", "Initialize failed due to invalid input.");
            throw new IllegalArgumentException("Invalid input.");
        }
        try {
            bp.m900a(context, str);
            al.m753a();
            if (set != null) {
                PopoverCacheManager.m1008a((String[]) set.toArray(new String[0]));
            } else {
                PopoverCacheManager.m1008a(null);
            }
        } catch (Exception e) {
            Log.m1016b("MAIN", "Initialize failed");
            new MetricsRecorderCall("initializeFailed", e.getClass().getSimpleName()).m973d();
        }
    }

    public static af m719a(Context context, String str, Set<String> set) {
        if (f1180a == null) {
            f1180a = new af(context, str, set);
        }
        return f1180a;
    }

    public void m723a(OpenRetailPageRequest openRetailPageRequest) {
        if (openRetailPageRequest != null) {
            String b;
            if (openRetailPageRequest instanceof OpenProductPageRequest) {
                b = br.m910b(((OpenProductPageRequest) openRetailPageRequest).getProductId());
            } else if (openRetailPageRequest instanceof OpenSearchPageRequest) {
                b = br.m905a((OpenSearchPageRequest) openRetailPageRequest);
            } else {
                b = br.m904a();
            }
            br.m912d(b);
        }
    }

    public final boolean m724a(WebView webView, String str) {
        return ah.m726a(webView, str);
    }

    public PopoverStatus m722a(View view, OpenProductPageRequest openProductPageRequest, RequestId requestId) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f1181b < 1000 || !m721a()) {
            return PopoverStatus.FAILED;
        }
        this.f1181b = currentTimeMillis;
        if (openProductPageRequest == null || openProductPageRequest.getProductId() == null || openProductPageRequest.getProductId().trim().equals(Stomp.EMPTY)) {
            return PopoverStatus.INVALID_PRODUCT_ID;
        }
        try {
            m720a(view, openProductPageRequest.getProductId(), "PRODUCT_PREVIEW", requestId);
            return null;
        } catch (ActivityNotFoundException e) {
            Log.m1016b("MAIN", "Please add ProductPopoverActivity to your AndroidMenifest.xml for MobileAssociates functionality.");
            return PopoverStatus.FAILED;
        } catch (RuntimeException e2) {
            new MetricsRecorderCall("previewActivityFailedToStart", e2.getClass().getSimpleName()).m973d();
            return PopoverStatus.FAILED;
        }
    }

    private void m720a(View view, String str, String str2, RequestId requestId) {
        Intent intent = new Intent(bp.m899a(), ProductPopoverActivity.class);
        intent.setFlags(268435456);
        intent.putExtra("productId", str);
        intent.putExtra("popoverType", str2);
        intent.putExtra("requestId", requestId.toString());
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        intent.putExtra("clickeViewRect", new int[]{iArr[0], iArr[1], iArr[0] + view.getWidth(), iArr[1] + view.getHeight()});
        View rootView = view.getRootView();
        rootView.getLocationOnScreen(iArr);
        intent.putExtra("rootViewRect", new int[]{iArr[0], iArr[1], iArr[0] + rootView.getWidth(), iArr[1] + rootView.getHeight()});
        rootView.findViewById(16908290).getLocationOnScreen(iArr);
        intent.putExtra("parentViewRect", new int[]{iArr[0], iArr[1], iArr[0] + rootView.getWidth(), iArr[1] + rootView.getHeight()});
        bp.m899a().startActivity(intent);
    }

    public PopoverStatus m725b(View view, OpenProductPageRequest openProductPageRequest, RequestId requestId) {
        if (!m721a()) {
            return PopoverStatus.FAILED;
        }
        if (openProductPageRequest == null || openProductPageRequest.getProductId() == null || openProductPageRequest.getProductId().trim().equals(Stomp.EMPTY)) {
            return PopoverStatus.INVALID_PRODUCT_ID;
        }
        try {
            m720a(view, openProductPageRequest.getProductId().trim(), "PRODUCT_DETAIL", requestId);
            return null;
        } catch (ActivityNotFoundException e) {
            Log.m1016b("MAIN", "Please add ProductPopoverActivity to your AndroidMenifest.xml for MobileAssociates functionality.");
            return PopoverStatus.FAILED;
        } catch (RuntimeException e2) {
            new MetricsRecorderCall("UDPActivityFailedToStart", e2.getClass().getSimpleName()).m973d();
            return PopoverStatus.FAILED;
        }
    }

    private boolean m721a() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) bp.m899a().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected()) {
                return true;
            }
            return false;
        } catch (SecurityException e) {
            Log.m1016b("MAIN", "Please add ACCESS_NETWORK_STATE permission in your Android manifest file for MobileAssociates functionality.");
            return false;
        }
    }
}
