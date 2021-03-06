package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Message;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebView.WebViewTransport;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.internal.overlay.zzd;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

@TargetApi(11)
@zzhb
public class zzjv extends WebChromeClient {
    private final zzjp zzpD;

    /* renamed from: com.google.android.gms.internal.zzjv.1 */
    static class C09011 implements OnCancelListener {
        final /* synthetic */ JsResult zzOh;

        C09011(JsResult jsResult) {
            this.zzOh = jsResult;
        }

        public void onCancel(DialogInterface dialog) {
            this.zzOh.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjv.2 */
    static class C09022 implements OnClickListener {
        final /* synthetic */ JsResult zzOh;

        C09022(JsResult jsResult) {
            this.zzOh = jsResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzOh.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjv.3 */
    static class C09033 implements OnClickListener {
        final /* synthetic */ JsResult zzOh;

        C09033(JsResult jsResult) {
            this.zzOh = jsResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzOh.confirm();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjv.4 */
    static class C09044 implements OnCancelListener {
        final /* synthetic */ JsPromptResult zzOi;

        C09044(JsPromptResult jsPromptResult) {
            this.zzOi = jsPromptResult;
        }

        public void onCancel(DialogInterface dialog) {
            this.zzOi.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjv.5 */
    static class C09055 implements OnClickListener {
        final /* synthetic */ JsPromptResult zzOi;

        C09055(JsPromptResult jsPromptResult) {
            this.zzOi = jsPromptResult;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzOi.cancel();
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjv.6 */
    static class C09066 implements OnClickListener {
        final /* synthetic */ JsPromptResult zzOi;
        final /* synthetic */ EditText zzOj;

        C09066(JsPromptResult jsPromptResult, EditText editText) {
            this.zzOi = jsPromptResult;
            this.zzOj = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.zzOi.confirm(this.zzOj.getText().toString());
        }
    }

    /* renamed from: com.google.android.gms.internal.zzjv.7 */
    static /* synthetic */ class C09077 {
        static final /* synthetic */ int[] zzOk;

        static {
            zzOk = new int[MessageLevel.values().length];
            try {
                zzOk[MessageLevel.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                zzOk[MessageLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                zzOk[MessageLevel.LOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                zzOk[MessageLevel.TIP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                zzOk[MessageLevel.DEBUG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public zzjv(zzjp com_google_android_gms_internal_zzjp) {
        this.zzpD = com_google_android_gms_internal_zzjp;
    }

    private final Context zza(WebView webView) {
        if (!(webView instanceof zzjp)) {
            return webView.getContext();
        }
        zzjp com_google_android_gms_internal_zzjp = (zzjp) webView;
        Context zzhP = com_google_android_gms_internal_zzjp.zzhP();
        return zzhP == null ? com_google_android_gms_internal_zzjp.getContext() : zzhP;
    }

    private static void zza(Builder builder, String str, JsResult jsResult) {
        builder.setMessage(str).setPositiveButton(17039370, new C09033(jsResult)).setNegativeButton(17039360, new C09022(jsResult)).setOnCancelListener(new C09011(jsResult)).create().show();
    }

    private static void zza(Context context, Builder builder, String str, String str2, JsPromptResult jsPromptResult) {
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        View textView = new TextView(context);
        textView.setText(str);
        View editText = new EditText(context);
        editText.setText(str2);
        linearLayout.addView(textView);
        linearLayout.addView(editText);
        builder.setView(linearLayout).setPositiveButton(17039370, new C09066(jsPromptResult, editText)).setNegativeButton(17039360, new C09055(jsPromptResult)).setOnCancelListener(new C09044(jsPromptResult)).create().show();
    }

    private final boolean zziv() {
        return zzr.zzbC().zza(this.zzpD.getContext().getPackageManager(), this.zzpD.getContext().getPackageName(), "android.permission.ACCESS_FINE_LOCATION") || zzr.zzbC().zza(this.zzpD.getContext().getPackageManager(), this.zzpD.getContext().getPackageName(), "android.permission.ACCESS_COARSE_LOCATION");
    }

    public final void onCloseWindow(WebView webView) {
        if (webView instanceof zzjp) {
            zzd zzhS = ((zzjp) webView).zzhS();
            if (zzhS == null) {
                zzb.zzaK("Tried to close an AdWebView not associated with an overlay.");
                return;
            } else {
                zzhS.close();
                return;
            }
        }
        zzb.zzaK("Tried to close a WebView that wasn't an AdWebView.");
    }

    public final boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String str = "JS: " + consoleMessage.message() + " (" + consoleMessage.sourceId() + Headers.SEPERATOR + consoleMessage.lineNumber() + ")";
        if (str.contains("Application Cache")) {
            return super.onConsoleMessage(consoleMessage);
        }
        switch (C09077.zzOk[consoleMessage.messageLevel().ordinal()]) {
            case Zone.PRIMARY /*1*/:
                zzb.m1672e(str);
                break;
            case Zone.SECONDARY /*2*/:
                zzb.zzaK(str);
                break;
            case Protocol.GGP /*3*/:
            case Type.MF /*4*/:
                zzb.zzaJ(str);
                break;
            case Service.RJE /*5*/:
                zzb.zzaI(str);
                break;
            default:
                zzb.zzaJ(str);
                break;
        }
        return super.onConsoleMessage(consoleMessage);
    }

    public final boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        WebViewTransport webViewTransport = (WebViewTransport) resultMsg.obj;
        WebView webView = new WebView(view.getContext());
        webView.setWebViewClient(this.zzpD.zzhU());
        webViewTransport.setWebView(webView);
        resultMsg.sendToTarget();
        return true;
    }

    public final void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        long j = 5242880 - totalUsedQuota;
        if (j <= 0) {
            quotaUpdater.updateQuota(currentQuota);
            return;
        }
        if (currentQuota == 0) {
            if (estimatedSize > j || estimatedSize > 1048576) {
                estimatedSize = 0;
            }
        } else if (estimatedSize == 0) {
            estimatedSize = Math.min(Math.min(PlaybackStateCompat.ACTION_PREPARE_FROM_URI, j) + currentQuota, 1048576);
        } else {
            if (estimatedSize <= Math.min(1048576 - currentQuota, j)) {
                currentQuota += estimatedSize;
            }
            estimatedSize = currentQuota;
        }
        quotaUpdater.updateQuota(estimatedSize);
    }

    public final void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        if (callback != null) {
            callback.invoke(origin, zziv(), true);
        }
    }

    public final void onHideCustomView() {
        zzd zzhS = this.zzpD.zzhS();
        if (zzhS == null) {
            zzb.zzaK("Could not get ad overlay when hiding custom view.");
        } else {
            zzhS.zzfl();
        }
    }

    public final boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
        return zza(zza(webView), url, message, null, result, null, false);
    }

    public final boolean onJsBeforeUnload(WebView webView, String url, String message, JsResult result) {
        return zza(zza(webView), url, message, null, result, null, false);
    }

    public final boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
        return zza(zza(webView), url, message, null, result, null, false);
    }

    public final boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult result) {
        return zza(zza(webView), url, message, defaultValue, null, result, true);
    }

    public final void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
        long j = PlaybackStateCompat.ACTION_PREPARE_FROM_URI + spaceNeeded;
        if (5242880 - totalUsedQuota < j) {
            quotaUpdater.updateQuota(0);
        } else {
            quotaUpdater.updateQuota(j);
        }
    }

    public final void onShowCustomView(View view, CustomViewCallback customViewCallback) {
        zza(view, -1, customViewCallback);
    }

    protected final void zza(View view, int i, CustomViewCallback customViewCallback) {
        zzd zzhS = this.zzpD.zzhS();
        if (zzhS == null) {
            zzb.zzaK("Could not get ad overlay when showing custom view.");
            customViewCallback.onCustomViewHidden();
            return;
        }
        zzhS.zza(view, customViewCallback);
        zzhS.setRequestedOrientation(i);
    }

    protected boolean zza(Context context, String str, String str2, String str3, JsResult jsResult, JsPromptResult jsPromptResult, boolean z) {
        try {
            Builder builder = new Builder(context);
            builder.setTitle(str);
            if (z) {
                zza(context, builder, str2, str3, jsPromptResult);
            } else {
                zza(builder, str2, jsResult);
            }
        } catch (Throwable e) {
            zzb.zzd("Fail to display Dialog.", e);
        }
        return true;
    }
}
