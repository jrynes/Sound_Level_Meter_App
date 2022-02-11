package com.amazon.device.ads;

import android.content.Context;
import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;

class AdUserAgentManager extends BasicUserAgentManager {
    private final ThreadRunner threadRunner;

    /* renamed from: com.amazon.device.ads.AdUserAgentManager.1 */
    class C02931 implements Runnable {
        final /* synthetic */ Context val$context;

        C02931(Context context) {
            this.val$context = context;
        }

        public void run() {
            AdUserAgentManager.this.setUserAgentString(WebViewFactory.getInstance().createWebView(this.val$context).getSettings().getUserAgentString());
        }
    }

    public AdUserAgentManager() {
        this(new ThreadRunner());
    }

    AdUserAgentManager(ThreadRunner threadRunner) {
        this.threadRunner = threadRunner;
    }

    void buildAndSetUserAgentString(Context context) {
        this.threadRunner.execute(new C02931(context), ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
    }
}
