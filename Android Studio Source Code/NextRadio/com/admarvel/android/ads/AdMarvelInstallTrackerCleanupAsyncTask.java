package com.admarvel.android.ads;

import android.os.AsyncTask;
import android.os.Environment;
import java.io.File;
import org.apache.activemq.jndi.ReadOnlyContext;

/* renamed from: com.admarvel.android.ads.e */
public class AdMarvelInstallTrackerCleanupAsyncTask extends AsyncTask<Object, Object, Object> {
    private static long f511a;

    static {
        f511a = 864000000;
    }

    protected Object doInBackground(Object... params) {
        try {
            if ("mounted".equals(Environment.getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory(), ReadOnlyContext.SEPARATOR + Utils.m205d("adm_tracker_dir"));
                if (file.exists()) {
                    for (File file2 : file.listFiles()) {
                        if (System.currentTimeMillis() - file2.lastModified() > f511a) {
                            file2.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
