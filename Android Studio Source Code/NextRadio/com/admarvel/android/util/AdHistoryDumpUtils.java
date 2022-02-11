package com.admarvel.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import com.admarvel.android.ads.AdFetcher;
import com.admarvel.android.ads.AdMarvelUtils;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.activemq.jndi.ReadOnlyContext;
import org.apache.activemq.transport.stomp.Stomp;

/* renamed from: com.admarvel.android.util.a */
public class AdHistoryDumpUtils {
    private static volatile AdHistoryDumpUtils f969b;
    private static WeakReference<Context> f970e;
    PrintWriter f971a;
    private int f972c;
    private File f973d;
    private String f974f;

    /* renamed from: com.admarvel.android.util.a.1 */
    class AdHistoryDumpUtils implements FilenameFilter {
        final /* synthetic */ AdHistoryDumpUtils f954a;

        AdHistoryDumpUtils(AdHistoryDumpUtils adHistoryDumpUtils) {
            this.f954a = adHistoryDumpUtils;
        }

        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".zip");
        }
    }

    /* renamed from: com.admarvel.android.util.a.a */
    private class AdHistoryDumpUtils implements Runnable {
        final /* synthetic */ AdHistoryDumpUtils f955a;
        private final String f956b;
        private final int f957c;

        public AdHistoryDumpUtils(AdHistoryDumpUtils adHistoryDumpUtils, String str, int i) {
            this.f955a = adHistoryDumpUtils;
            this.f956b = str;
            this.f957c = i;
        }

        public void run() {
            try {
                new AdHistoryDumpUtils(this.f955a, this.f956b).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Integer[]{Integer.valueOf(this.f957c)});
            } catch (Throwable e) {
                Logging.log(Log.getStackTraceString(e));
            }
        }
    }

    /* renamed from: com.admarvel.android.util.a.b */
    public class AdHistoryDumpUtils extends AsyncTask<Integer, Void, String> {
        final /* synthetic */ AdHistoryDumpUtils f958a;
        private final String f959b;
        private final String f960c;

        public AdHistoryDumpUtils(AdHistoryDumpUtils adHistoryDumpUtils, String str) {
            this.f958a = adHistoryDumpUtils;
            this.f960c = str;
            this.f959b = this.f960c + ".zip";
        }

        protected String m527a(Integer... numArr) {
            try {
                int intValue = numArr[0].intValue();
                File[] listFiles = this.f958a.f973d.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    return null;
                }
                int i;
                int a;
                int i2;
                int b = intValue > this.f958a.f972c ? this.f958a.f972c : intValue;
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (Object add : listFiles) {
                    arrayList2.add(add);
                }
                Context context = (Context) AdHistoryDumpUtils.f970e.get();
                String str = this.f958a.f973d.getAbsolutePath() + "/log_";
                String str2 = this.f958a.f973d.getAbsolutePath() + "/ss_";
                String str3 = this.f958a.f973d.getAbsolutePath() + "/sse_";
                String str4 = this.f958a.f973d.getAbsolutePath() + "/ssr_";
                if (context != null) {
                    a = this.f958a.m552c(context);
                    i2 = 0;
                    while (true) {
                        i = i2 + 1;
                        if (i2 == b) {
                            break;
                        }
                        if (a <= 0) {
                            a = this.f958a.f972c;
                        }
                        File file = new File(str + a + ".txt");
                        if (file.exists()) {
                            arrayList.add(file.getAbsolutePath());
                        }
                        file = new File(str2 + a + ".jpg");
                        if (file.exists()) {
                            arrayList.add(file.getAbsolutePath());
                        }
                        file = new File(str3 + a + ".jpg");
                        if (file.exists()) {
                            arrayList.add(file.getAbsolutePath());
                        }
                        file = new File(str4 + a + ".jpg");
                        if (file.exists()) {
                            arrayList.add(file.getAbsolutePath());
                        }
                        a--;
                        i2 = i;
                    }
                }
                String[] strArr = new String[arrayList.size()];
                Iterator it = arrayList.iterator();
                a = 0;
                while (it.hasNext()) {
                    i2 = a + 1;
                    strArr[a] = (String) it.next();
                    a = i2;
                }
                String str5 = this.f958a.f973d.getAbsolutePath() + ReadOnlyContext.SEPARATOR + this.f959b;
                new Compress(strArr, str5).m603a();
                String str6 = ("http://sdk-rh.admarvel.com/adhistory/upload?" + "platform=android") + "&deviceid=" + OptionalUtils.getId(context);
                this.f958a.m547a((((context != null ? str6 + "&appid=" + context.getPackageName() : str6) + "&zipguid=" + this.f960c) + "&timestamp=" + System.currentTimeMillis()) + AdFetcher.lastAdRequestPostString, str5);
                return str5;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void m528a(String str) {
            super.onPostExecute(str);
        }

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return m527a((Integer[]) x0);
        }

        protected /* synthetic */ void onPostExecute(Object x0) {
            m528a((String) x0);
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private AdHistoryDumpUtils(Context context) {
        this.f972c = 20;
        this.f974f = "admarvel_adHistory";
        this.f971a = null;
        if (context == null) {
            f970e = null;
            return;
        }
        f970e = new WeakReference(context);
        Context context2 = (Context) f970e.get();
        try {
            this.f973d = new File(context.getDir("adm_assets", 0).getAbsoluteFile(), this.f974f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.f973d != null) {
            if (this.f973d.mkdir()) {
                m545a(context2, 0);
            }
            File[] listFiles = this.f973d.listFiles(new AdHistoryDumpUtils(this));
            if (listFiles != null && listFiles.length > 0) {
                for (File delete : listFiles) {
                    delete.delete();
                }
            }
        }
    }

    private synchronized void m545a(Context context, int i) {
        Editor edit = context.getSharedPreferences("Ad_history_counter", 0).edit();
        edit.putInt("ad_dump_count", i);
        edit.commit();
    }

    private void m547a(String str, String str2) {
        File file;
        Throwable e;
        try {
            file = new File(str2);
            try {
                MultipartUtility multipartUtility = new MultipartUtility(str, "utf-8");
                multipartUtility.m625a("fileUpload", file);
                multipartUtility.m624a();
                if (file != null && !file.delete()) {
                    Logging.log("Unable to delete AdHistory zip file");
                }
            } catch (Exception e2) {
                e = e2;
                try {
                    Logging.log(Log.getStackTraceString(e));
                    if (file != null) {
                    }
                } catch (Throwable th) {
                    e = th;
                    Logging.log("Unable to delete AdHistory zip file");
                    throw e;
                }
            }
        } catch (Exception e3) {
            e = e3;
            file = null;
            Logging.log(Log.getStackTraceString(e));
            if (file != null && !file.delete()) {
                Logging.log("Unable to delete AdHistory zip file");
            }
        } catch (Throwable th2) {
            e = th2;
            file = null;
            if (!(file == null || file.delete())) {
                Logging.log("Unable to delete AdHistory zip file");
            }
            throw e;
        }
    }

    private double m548b() {
        double d = 0.0d;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/proc/meminfo", "r");
            Matcher matcher = Pattern.compile("(\\d+)").matcher(randomAccessFile.readLine());
            String str = Stomp.EMPTY;
            while (matcher.find()) {
                str = matcher.group(1);
            }
            randomAccessFile.close();
            d = Double.parseDouble(str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return d;
    }

    public static AdHistoryDumpUtils m550b(Context context) {
        if (f969b == null && context != null) {
            f969b = new AdHistoryDumpUtils(context);
        }
        f970e = new WeakReference(context);
        return f969b;
    }

    private void m551b(int i) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/sse_");
            stringBuilder.append(i);
            stringBuilder.append(".jpg");
            File file = new File(this.f973d.getAbsolutePath() + stringBuilder);
            if (file != null && file.exists()) {
                file.delete();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("/ssr_");
            stringBuilder.append(i);
            stringBuilder.append(".jpg");
            file = new File(this.f973d.getAbsolutePath() + stringBuilder);
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized int m552c(Context context) {
        return context.getSharedPreferences("Ad_history_counter", 0).getInt("ad_dump_count", 0);
    }

    private boolean m553d(Context context) {
        double b;
        try {
            b = m548b();
            if (b > 0.0d) {
                b /= 1024.0d;
                return b == -1.0d && b < 450.0d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        b = -1.0d;
        if (b == -1.0d) {
        }
    }

    private boolean m554e(Context context) {
        return VERSION.SDK_INT < 10 ? true : m553d(context);
    }

    public synchronized int m555a(Context context) {
        int i;
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ad_history_counter", 0);
        i = (sharedPreferences.getInt("ad_dump_count", 0) % this.f972c) + 1;
        Editor edit = sharedPreferences.edit();
        edit.putInt("ad_dump_count", i);
        edit.commit();
        return i;
    }

    public String m556a(int i) {
        String uuid = UUID.randomUUID().toString();
        if (AdMarvelUtils.getAndroidSDKVersion() >= 11) {
            AdMarvelThreadExecutorService.m597a().m598b().execute(new AdHistoryDumpUtils(this, uuid, i));
        } else {
            new AdHistoryDumpUtils(this, uuid).execute(new Integer[]{Integer.valueOf(i)});
        }
        return uuid;
    }

    public synchronized void m557a(String str) {
        Bitmap bitmap;
        Context context = (Context) f970e.get();
        if (context instanceof Activity) {
            if (m554e(context)) {
                Logging.log("AdHistory Capture: Screen Dump discarded");
            } else {
                bitmap = null;
                try {
                    View rootView = ((Activity) context).getWindow().getDecorView().findViewById(16908290).findViewById(16908290).getRootView();
                    if (rootView != null) {
                        Boolean valueOf = Boolean.valueOf(rootView.isDrawingCacheEnabled());
                        rootView.setDrawingCacheEnabled(true);
                        OutputStream fileOutputStream = new FileOutputStream(this.f973d.getAbsolutePath() + str);
                        if (fileOutputStream != null) {
                            if (((Activity) context).getResources().getDisplayMetrics().density > 1.0f) {
                                if (!rootView.getDrawingCache().compress(CompressFormat.JPEG, 15, fileOutputStream)) {
                                    Logging.log("AdHistory Capture: Screen Dump failed");
                                }
                            } else if (!rootView.getDrawingCache().compress(CompressFormat.JPEG, 60, fileOutputStream)) {
                                Logging.log("AdHistory Capture: Screen Dump failed");
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            if (!valueOf.booleanValue()) {
                                rootView.setDrawingCacheEnabled(false);
                            }
                        }
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    } else if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (FileNotFoundException e) {
                    Logging.log(e.getMessage());
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (IOException e2) {
                    Logging.log(e2.getMessage());
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (OutOfMemoryError e4) {
                    e4.printStackTrace();
                    AdMarvelUtils.disableLogDump();
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } catch (Throwable th) {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
            }
        }
    }

    public void m558a(String str, int i) {
        if (f970e != null && f970e.get() != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/log_");
            stringBuilder.append(i);
            stringBuilder.append(".txt");
            try {
                this.f971a = new PrintWriter(this.f973d.getAbsolutePath() + stringBuilder.toString(), HttpRequest.CHARSET_UTF8);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
            if (this.f971a != null) {
                this.f971a.print(str);
                this.f971a.close();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("/ss_");
            stringBuilder.append(i);
            stringBuilder.append(".jpg");
            m557a(stringBuilder.toString());
            m551b(i);
        }
    }
}
