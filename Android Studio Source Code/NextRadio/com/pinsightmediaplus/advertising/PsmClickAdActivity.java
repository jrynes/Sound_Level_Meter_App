package com.pinsightmediaplus.advertising;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.admarvel.android.ads.Constants;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.WKSRecord.Service;

@SuppressLint({"SetJavaScriptEnabled", "InlinedApi"})
public class PsmClickAdActivity extends Activity {
    private static final String TAG = "AdActivity";
    private Handler mHandler;
    private String mUrl;
    private WebView webview;

    /* renamed from: com.pinsightmediaplus.advertising.PsmClickAdActivity.1 */
    class C13131 implements Runnable {
        C13131() {
        }

        public void run() {
            PsmClickAdActivity.this.webview.getSettings().setSupportZoom(true);
            PsmClickAdActivity.this.webview.getSettings().setBuiltInZoomControls(true);
            if (VERSION.SDK_INT >= 11) {
                PsmClickAdActivity.this.webview.getSettings().setDisplayZoomControls(false);
            }
            PsmClickAdActivity.this.webview.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
            PsmClickAdActivity.this.webview.getSettings().setJavaScriptEnabled(true);
            PsmClickAdActivity.this.webview.getSettings().setPluginState(PluginState.ON_DEMAND);
            PsmClickAdActivity.this.webview.getSettings().setUseWideViewPort(true);
            PsmClickAdActivity.this.webview.getSettings().setLoadWithOverviewMode(true);
            PsmClickAdActivity.this.webview.getSettings().setGeolocationEnabled(true);
            PsmClickAdActivity.this.webview.getSettings().setDomStorageEnabled(true);
            PsmClickAdActivity.this.webview.getSettings().setAppCacheEnabled(true);
            PsmClickAdActivity.this.webview.getSettings().setDatabaseEnabled(true);
            PsmClickAdActivity.this.webview.setWebViewClient(new MyWebViewClient());
            PsmClickAdActivity.this.webview.setWebChromeClient(new WebChromeClient());
            PsmClickAdActivity.this.webview.getSettings().setDatabasePath(PsmClickAdActivity.this.webview.getContext().getDir("database", 0).getPath());
            PsmClickAdActivity.this.webview.loadUrl(PsmClickAdActivity.this.mUrl);
            PsmClickAdActivity.this.showWebView();
        }
    }

    class MyWebViewClient extends WebViewClient {
        MyWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                Log.d(PsmClickAdActivity.TAG, "shouldOverrideUrlLoading, url=" + url);
                url = url.replace("admarvelsdkexternal", Stomp.EMPTY).replace("admarvelexternal", Stomp.EMPTY);
                Intent intent;
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    boolean isVideo = false;
                    try {
                        int idxQuestionmark = url.indexOf("?");
                        if (idxQuestionmark != -1) {
                            String temp = url.substring(0, idxQuestionmark);
                            if (temp.endsWith(".mp4") || temp.endsWith(".m3u8")) {
                                isVideo = true;
                            }
                        }
                        if (isVideo) {
                            intent = new Intent("android.intent.action.VIEW");
                            intent.setFlags(268435456);
                            if (Build.MANUFACTURER.equals("HTC")) {
                                intent.setDataAndType(Uri.parse(url), WebRequest.CONTENT_TYPE_HTML);
                            } else {
                                intent.setDataAndType(Uri.parse(url), "video/mp4");
                            }
                            if (intent.resolveActivity(PsmClickAdActivity.this.getPackageManager()) != null) {
                                PsmClickAdActivity.this.startActivity(intent);
                            }
                            view.stopLoading();
                            PsmClickAdActivity.this.finish();
                            return true;
                        }
                    } catch (Throwable e) {
                        Log.d(PsmClickAdActivity.TAG, e.toString());
                    }
                    return false;
                }
                intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                if (intent.resolveActivity(PsmClickAdActivity.this.getPackageManager()) != null) {
                    PsmClickAdActivity.this.startActivity(intent);
                }
                PsmClickAdActivity.this.finish();
                return true;
            } catch (Exception e2) {
                Log.d(PsmClickAdActivity.TAG, e2.toString());
            }
        }
    }

    public PsmClickAdActivity() {
        this.mHandler = new Handler();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mUrl = savedInstanceState.getString(SettingsJsonConstants.APP_URL_KEY);
        } else {
            this.mUrl = getIntent().getStringExtra(SettingsJsonConstants.APP_URL_KEY);
        }
        try {
            RelativeLayout screen = new RelativeLayout(this);
            this.webview = new WebView(this);
            if (VERSION.SDK_INT >= 16) {
                Method method = this.webview.getSettings().getClass().getMethod("setAllowUniversalAccessFromFileURLs", new Class[]{Boolean.TYPE});
                if (method != null) {
                    method.invoke(this.webview.getSettings(), new Object[]{Boolean.valueOf(true)});
                }
            }
            screen.addView(this.webview, new LayoutParams(-1, -1));
            setContentView(screen, new ViewGroup.LayoutParams(-1, -1));
        } catch (Throwable e) {
            Log.d(TAG, e.toString());
        }
    }

    public void onSaveInstanceState(Bundle b) {
        b.putString(SettingsJsonConstants.APP_URL_KEY, this.mUrl);
        super.onSaveInstanceState(b);
    }

    public void onResume() {
        super.onResume();
        if (this.mUrl != null) {
            setUrl(this.mUrl);
        }
    }

    public void onStop() {
        super.onStop();
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && onBackKeyPressed()) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean onBackKeyPressed() {
        try {
            this.webview.stopLoading();
            this.webview.getSettings().setSupportZoom(false);
            this.webview.setWebViewClient(null);
            this.webview.setWebChromeClient(null);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return false;
    }

    public static void onPauseWebview(WebView webinstance) {
        try {
            Class.forName("android.webkit.WebView").getMethod("onPause", null).invoke(webinstance, null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
        }
    }

    void setUrl(String url) {
        try {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                this.mUrl = url;
            } else {
                this.mUrl = "http://" + url;
            }
            this.webview.setVisibility(8);
            int delayMilliseconds = Constants.ANIMATION_DURATION;
            if (VERSION.SDK_INT >= 16) {
                delayMilliseconds = 300;
            }
            this.mHandler.postDelayed(new C13131(), (long) delayMilliseconds);
        } catch (Throwable e) {
            Log.d(TAG, e.toString());
        }
    }

    protected void showWebView() {
        if (this.webview != null && this.webview.getVisibility() != 0) {
            this.webview.setVisibility(0);
            this.webview.requestFocus(Service.CISCO_FNA);
        }
    }
}
