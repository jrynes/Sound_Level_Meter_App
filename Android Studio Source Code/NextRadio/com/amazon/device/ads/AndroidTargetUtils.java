package com.amazon.device.ads;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewDatabase;
import android.widget.ImageButton;

class AndroidTargetUtils {
    public static final AndroidClassAdapter defaultAndroidClassAdapter;
    private static boolean isWebViewCheckedAndOk;

    public static class AndroidClassAdapter {
        private final AndroidBuildInfo androidBuildInfo;

        public class WebSettingsAdapter {
            private final WebSettings webSettings;

            public WebSettingsAdapter(WebSettings webSettings) {
                this.webSettings = webSettings;
            }

            public void setMediaPlaybackRequiresUserGesture(boolean require) {
                if (AndroidClassAdapter.this.isAtLeastAndroidAPI(17)) {
                    JellyBeanMR1TargetUtils.setMediaPlaybackRequiresUserGesture(this.webSettings, require);
                }
            }
        }

        public AndroidClassAdapter(AndroidBuildInfo androidBuildInfo) {
            this.androidBuildInfo = androidBuildInfo;
        }

        public WebSettingsAdapter withWebSettings(WebSettings webSettings) {
            return new WebSettingsAdapter(webSettings);
        }

        private boolean isAtLeastAndroidAPI(int api) {
            return AndroidTargetUtils.isAtLeastAndroidAPI(this.androidBuildInfo, api);
        }
    }

    @TargetApi(5)
    private static class EclairTargetUtils {
        private EclairTargetUtils() {
        }

        protected static BitmapDrawable getNewBitmapDrawable(Resources r, String f) {
            return new BitmapDrawable(r, f);
        }
    }

    @TargetApi(8)
    private static class FroyoTargetUtils {
        private FroyoTargetUtils() {
        }

        protected static String getPackageCodePath(Context context) {
            return context.getPackageCodePath();
        }

        protected static int getRotation(Display display) {
            return display.getRotation();
        }
    }

    @TargetApi(9)
    private static class GingerbreadTargetUtils {
        private GingerbreadTargetUtils() {
        }

        protected static void editorApply(Editor editor) {
            editor.apply();
        }
    }

    @TargetApi(11)
    private static class HoneycombTargetUtils {
        private HoneycombTargetUtils() {
        }

        public static boolean isInstanceOfSQLiteDatabaseLockedException(SQLiteException e) {
            return e instanceof SQLiteDatabaseLockedException;
        }

        public static final void disableHardwareAcceleration(View view) {
            view.setLayerType(1, null);
        }

        protected static final <T> void executeAsyncTaskWithThreadPooling(MobileAdsAsyncTask<T, ?, ?> task, T... params) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }

        protected static void hideActionBar(Activity activity) {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }

        protected static void removeJavascriptInterface(WebView webView, String name) {
            webView.removeJavascriptInterface(name);
        }

        protected static void enableHardwareAcceleration(Window window) {
            window.setFlags(ViewCompat.MEASURED_STATE_TOO_SMALL, ViewCompat.MEASURED_STATE_TOO_SMALL);
        }
    }

    @TargetApi(17)
    private static class JellyBeanMR1TargetUtils {
        private JellyBeanMR1TargetUtils() {
        }

        public static void setMediaPlaybackRequiresUserGesture(WebSettings webSettings, boolean require) {
            webSettings.setMediaPlaybackRequiresUserGesture(require);
        }
    }

    @TargetApi(16)
    private static class JellyBeanTargetUtils {
        private JellyBeanTargetUtils() {
        }

        public static void setBackgroundForLinerLayout(View view, Drawable background) {
            view.setBackground(background);
        }

        protected static void setImageButtonAlpha(ImageButton imageButton, int alpha) {
            imageButton.setImageAlpha(alpha);
        }

        public static void hideStatusBar(Activity activity) {
            activity.getWindow().getDecorView().setSystemUiVisibility(4);
        }
    }

    @TargetApi(19)
    private static class KitKatTargetUtils {

        /* renamed from: com.amazon.device.ads.AndroidTargetUtils.KitKatTargetUtils.1 */
        static class C02961 implements Runnable {
            final /* synthetic */ boolean val$enable;

            C02961(boolean z) {
                this.val$enable = z;
            }

            public void run() {
                WebView.setWebContentsDebuggingEnabled(this.val$enable);
            }
        }

        private KitKatTargetUtils() {
        }

        public static void enableWebViewDebugging(boolean enable) {
            ThreadUtils.executeOnMainThread(new C02961(enable));
        }
    }

    static {
        isWebViewCheckedAndOk = false;
        defaultAndroidClassAdapter = new AndroidClassAdapter(new AndroidBuildInfo());
    }

    private AndroidTargetUtils() {
    }

    public static AndroidClassAdapter getDefaultAndroidClassAdapter() {
        return defaultAndroidClassAdapter;
    }

    public static boolean isAtLeastAndroidAPI(int api) {
        return VERSION.SDK_INT >= api;
    }

    public static boolean isAtLeastAndroidAPI(AndroidBuildInfo androidBuildInfo, int api) {
        return androidBuildInfo.getSDKInt() >= api;
    }

    public static boolean isAtOrBelowAndroidAPI(int api) {
        return VERSION.SDK_INT <= api;
    }

    public static boolean isAtOrBelowAndroidAPI(AndroidBuildInfo androidBuildInfo, int api) {
        return androidBuildInfo.getSDKInt() <= api;
    }

    public static boolean isAndroidAPI(int api) {
        return VERSION.SDK_INT == api;
    }

    public static boolean isAndroidAPI(AndroidBuildInfo androidBuildInfo, int api) {
        return androidBuildInfo.getSDKInt() == api;
    }

    public static boolean isBetweenAndroidAPIs(AndroidBuildInfo androidBuildInfo, int lowerApi, int upperApi) {
        return isAtLeastAndroidAPI(androidBuildInfo, lowerApi) && isAtOrBelowAndroidAPI(androidBuildInfo, upperApi);
    }

    public static BitmapDrawable getNewBitmapDrawable(Resources r, String f) {
        if (isAtLeastAndroidAPI(5)) {
            return EclairTargetUtils.getNewBitmapDrawable(r, f);
        }
        return new BitmapDrawable(f);
    }

    public static boolean isInstanceOfSQLiteDatabaseLockedException(SQLiteException e) {
        return HoneycombTargetUtils.isInstanceOfSQLiteDatabaseLockedException(e);
    }

    public static final void disableHardwareAcceleration(View view) {
        HoneycombTargetUtils.disableHardwareAcceleration(view);
    }

    public static String getPackageCodePath(Context context) {
        return FroyoTargetUtils.getPackageCodePath(context);
    }

    public static void editorApply(Editor editor) {
        GingerbreadTargetUtils.editorApply(editor);
    }

    public static void setImageButtonAlpha(ImageButton imageButton, int alpha) {
        if (isAtLeastAndroidAPI(16)) {
            JellyBeanTargetUtils.setImageButtonAlpha(imageButton, alpha);
        } else {
            imageButton.setAlpha(alpha);
        }
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (isAtLeastAndroidAPI(16)) {
            JellyBeanTargetUtils.setBackgroundForLinerLayout(view, drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static int getOrientation(Display display) {
        if (isAtLeastAndroidAPI(8)) {
            return FroyoTargetUtils.getRotation(display);
        }
        return display.getOrientation();
    }

    public static <T> void executeAsyncTask(MobileAdsAsyncTask<T, ?, ?> task, T... params) {
        if (isAtLeastAndroidAPI(11)) {
            HoneycombTargetUtils.executeAsyncTaskWithThreadPooling(task, params);
        } else {
            task.execute(params);
        }
    }

    public static void hideActionAndStatusBars(Activity activity) {
        if (isAtLeastAndroidAPI(11)) {
            HoneycombTargetUtils.hideActionBar(activity);
        }
        if (isAtLeastAndroidAPI(16)) {
            JellyBeanTargetUtils.hideStatusBar(activity);
        }
    }

    public static void removeJavascriptInterface(WebView webView, String interfaceName) {
        HoneycombTargetUtils.removeJavascriptInterface(webView, interfaceName);
    }

    public static boolean isWebViewOk(Context c) {
        if (isAtOrBelowAndroidAPI(8) && !isWebViewCheckedAndOk) {
            if (WebViewDatabase.getInstance(c) == null) {
                return false;
            }
            SQLiteDatabase cdb = null;
            try {
                cdb = c.openOrCreateDatabase("webviewCache.db", 0, null);
                if (cdb != null) {
                    cdb.close();
                }
                isWebViewCheckedAndOk = true;
            } catch (SQLiteException e) {
                boolean isDatabaseLocked = isDatabaseLocked(e);
                if (cdb == null) {
                    return isDatabaseLocked;
                }
                cdb.close();
                return isDatabaseLocked;
            } catch (Throwable th) {
                if (cdb != null) {
                    cdb.close();
                }
            }
        }
        return true;
    }

    protected static boolean isDatabaseLocked(SQLiteException e) {
        if (isAtLeastAndroidAPI(11)) {
            return isInstanceOfSQLiteDatabaseLockedException(e);
        }
        return StringUtils.doesExceptionContainLockedDatabaseMessage(e);
    }

    public static void enableWebViewDebugging(boolean enable) {
        if (isAtLeastAndroidAPI(19)) {
            KitKatTargetUtils.enableWebViewDebugging(enable);
        }
    }

    public static void enableHardwareAcceleration(Window window) {
        if (isAtLeastAndroidAPI(11)) {
            HoneycombTargetUtils.enableHardwareAcceleration(window);
        }
    }
}
