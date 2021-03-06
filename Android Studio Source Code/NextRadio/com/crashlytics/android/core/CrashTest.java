package com.crashlytics.android.core;

import android.os.AsyncTask;
import io.fabric.sdk.android.Fabric;

public class CrashTest {

    /* renamed from: com.crashlytics.android.core.CrashTest.1 */
    class C03641 extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ long val$delayMs;

        C03641(long j) {
            this.val$delayMs = j;
        }

        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(this.val$delayMs);
            } catch (InterruptedException e) {
            }
            CrashTest.this.throwRuntimeException("Background thread crash");
            return null;
        }
    }

    public void throwRuntimeException(String message) {
        throw new RuntimeException(message);
    }

    public int stackOverflow() {
        return stackOverflow() + ((int) Math.random());
    }

    public void indexOutOfBounds() {
        Fabric.getLogger().d(CrashlyticsCore.TAG, "Out of bounds value: " + new int[2][10]);
    }

    public void crashAsyncTask(long delayMs) {
        new C03641(delayMs).execute(new Void[]{(Void) null});
    }

    public void throwFiveChainedExceptions() {
        try {
            privateMethodThatThrowsException("1");
        } catch (Exception ex) {
            throw new RuntimeException("2", ex);
        } catch (Exception ex2) {
            try {
                throw new RuntimeException("3", ex2);
            } catch (Exception ex22) {
                try {
                    throw new RuntimeException("4", ex22);
                } catch (Exception ex222) {
                    throw new RuntimeException("5", ex222);
                }
            }
        }
    }

    private void privateMethodThatThrowsException(String message) {
        throw new RuntimeException(message);
    }
}
