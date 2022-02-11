package com.admarvel.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import com.admarvel.android.ads.AdMarvelUtils;
import com.admarvel.android.ads.FullScreenControls.FullScreenControls;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.jar.JarFile;
import org.apache.activemq.transport.stomp.Stomp;

public class AdMarvelBitmapDrawableUtils {
    private static final String ADMARVEL_BITMAP_ASSEST_URL = "http://admarvel.s3.amazonaws.com/sdk/assets/adm_bmp/";
    public static final String ADMARVEL_CUSTOM_BACK_BUTTON = "rd2KvT4Zi0NK9A0CejrW35pciQ002l";
    public static final String ADMARVEL_CUSTOM_BACK_DISABLE_BUTTON = "MKlWddpjgeIrBuZxnd37tVjz7OM7fu";
    public static final String ADMARVEL_CUSTOM_CLOSE_BUTTON = "OCkD4Sjg8bN0h8bDad6gWa5t51Zy5I";
    public static final String ADMARVEL_CUSTOM_DONE_BUTTON = "USuSmQSCwrDx1CXCp4oqplFBGTQAZM";
    public static final String ADMARVEL_CUSTOM_MUTE_BUTTON = "u2nfwuKbaKzVwGmUNmk7wFVXHwzy7S";
    public static final String ADMARVEL_CUSTOM_OPEN_URL_BUTTON = "nRdOcYYrMLotmPFqlTcjFIf7isxM5t";
    public static final String ADMARVEL_CUSTOM_OPEN_URL_DISABLED_BUTTON = "H2GFZHEOwlzVJk4cBatArzxlb2XOJH";
    public static final String ADMARVEL_CUSTOM_PAUSE_BUTTON = "d2UpTWfkssmtbKnAqIR6WGdcMl4Gg8";
    public static final String ADMARVEL_CUSTOM_PLAY_BUTTON = "aSrZSorTa7PztrNZ4FuMvViHEaGfDV";
    public static final String ADMARVEL_CUSTOM_REPLAY_BUTTON = "ej5yqIooDTRYYsXEJuN4eJOh7buHJI";
    public static final String ADMARVEL_CUSTOM_RESUME_BUTTON = "UPuOOqinUE2sqnnpe8MYG7PzHVVl5p";
    public static final String ADMARVEL_CUSTOM_RESUME_DISABLE_BUTTON = "NCLuZlSXjDualh2uti1kSm8vWlELL9";
    public static final String ADMARVEL_CUSTOM_STOP_BUTTON = "yz6T5xsau00Hqg556ez5NwTY2IsOW1";
    public static final String ADMARVEL_CUSTOM_TIME_BUTTON = "I9Kg1IJB4gtPeHhlB0pvKW5yqcRf2o";
    public static final String ADMARVEL_CUSTOM_UNMUTE_BUTTON = "kOy0RFIzirRqTweJUasQ2qaqYyPhm4";

    /* renamed from: com.admarvel.android.util.AdMarvelBitmapDrawableUtils.a */
    public enum C0265a {
        BACK_DISABLE,
        BACK,
        CLOSE,
        DONE,
        MUTE,
        PAUSE,
        PLAY,
        REPLAY,
        RESUME_DISABLE,
        RESUME,
        STOP,
        TIME,
        UNMUTE,
        OPEN_URL_DISABLED,
        OPEN_URL;
        
        private BitmapDrawable f950p;
        private int f951q;

        private byte[] m523a(String str) {
            String[] split = str.split(Stomp.COMMA);
            int length = split.length;
            byte[] bArr = new byte[length];
            for (int i = 0; i < length; i++) {
                bArr[i] = Byte.parseByte(split[i]);
            }
            this.f951q = bArr.length;
            return bArr;
        }

        public void m524a(Context context, String str, View view) {
            if (str != null) {
                if (this.f950p == null) {
                    int b = Dips.m606b(context.getResources().getDisplayMetrics().xdpi, context);
                    this.f950p = new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(m523a(str), 0, this.f951q));
                    this.f950p.setTargetDensity(b);
                }
                AdMarvelBitmapDrawableUtils.setDrawable(view, this.f950p);
            }
        }
    }

    /* renamed from: com.admarvel.android.util.AdMarvelBitmapDrawableUtils.b */
    private static class C0266b extends AsyncTask<String, Void, BitmapDrawable> {
        private WeakReference<Context> f952a;
        private WeakReference<View> f953b;

        public C0266b(Context context, View view) {
            this.f952a = new WeakReference(context);
            this.f953b = new WeakReference(view);
        }

        protected BitmapDrawable m525a(String... strArr) {
            Object e;
            String str = Stomp.EMPTY;
            try {
                String str2 = AdMarvelBitmapDrawableUtils.ADMARVEL_BITMAP_ASSEST_URL + strArr[0] + ".png";
                try {
                    Bitmap decodeStream = BitmapFactory.decodeStream(new URL(str2).openStream());
                    if (!(this.f952a == null || this.f952a.get() == null)) {
                        return new BitmapDrawable(((Context) this.f952a.get()).getResources(), decodeStream);
                    }
                } catch (Exception e2) {
                    e = e2;
                    str = str2;
                    Logging.log("Error downloading image: " + str + " " + e);
                    return null;
                }
            } catch (Exception e3) {
                e = e3;
                Logging.log("Error downloading image: " + str + " " + e);
                return null;
            }
            return null;
        }

        protected void m526a(BitmapDrawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);
            AdMarvelBitmapDrawableUtils.setDrawable((View) this.f953b.get(), bitmapDrawable);
        }

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return m525a((String[]) x0);
        }

        protected /* synthetic */ void onPostExecute(Object x0) {
            m526a((BitmapDrawable) x0);
        }
    }

    private AdMarvelBitmapDrawableUtils() {
    }

    public static BitmapDrawable bitmapFromJar(String source, Context context) {
        InputStream inputStream;
        Exception e;
        Throwable th;
        InputStream inputStream2 = null;
        String str = "admarvel_bitmaps/" + source + ".png";
        JarFile jarFile = null;
        if (context != null) {
            try {
                String file = context.getClassLoader().getResource(str).getFile();
                if (file.startsWith("file:")) {
                    file = file.substring(5);
                }
                int indexOf = file.indexOf("!");
                if (indexOf > 0) {
                    file = file.substring(0, indexOf);
                }
                jarFile = new JarFile(file);
                try {
                    inputStream = jarFile.getInputStream(jarFile.getJarEntry(str));
                    try {
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(inputStream));
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                jarFile.close();
                            } catch (Exception e2) {
                            }
                        }
                        return bitmapDrawable;
                    } catch (Exception e3) {
                        e = e3;
                        try {
                            e.printStackTrace();
                            if (inputStream != null) {
                                return null;
                            }
                            try {
                                inputStream.close();
                                jarFile.close();
                                return null;
                            } catch (Exception e4) {
                                return null;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                    jarFile.close();
                                } catch (Exception e5) {
                                }
                            }
                            throw th;
                        }
                    }
                } catch (Exception e6) {
                    e = e6;
                    inputStream = null;
                    e.printStackTrace();
                    if (inputStream != null) {
                        return null;
                    }
                    inputStream.close();
                    jarFile.close();
                    return null;
                } catch (Throwable th3) {
                    inputStream = null;
                    th = th3;
                    if (inputStream != null) {
                        inputStream.close();
                        jarFile.close();
                    }
                    throw th;
                }
            } catch (Exception e7) {
                e = e7;
                jarFile = null;
                inputStream = null;
                e.printStackTrace();
                if (inputStream != null) {
                    return null;
                }
                inputStream.close();
                jarFile.close();
                return null;
            } catch (Throwable th32) {
                jarFile = null;
                inputStream = null;
                th = th32;
                if (inputStream != null) {
                    inputStream.close();
                    jarFile.close();
                }
                throw th;
            }
        } else if (null == null) {
            return null;
        } else {
            try {
                inputStream2.close();
                jarFile.close();
                return null;
            } catch (Exception e8) {
                return null;
            }
        }
    }

    public static void bitmapFromLocalFolder(String source, Context context, View view) {
        Exception e;
        Throwable th;
        int i = 1;
        if (context != null) {
            File file = new File(context.getDir("adm_assets", 0).getAbsoluteFile() + "/adm_bmp/" + source + ".png");
            if (file.exists()) {
                InputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream(file);
                    try {
                        setDrawable(view, new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(fileInputStream)));
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (Exception e2) {
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        try {
                            e.printStackTrace();
                            if (fileInputStream == null) {
                                try {
                                    fileInputStream.close();
                                } catch (Exception e4) {
                                }
                                i = 0;
                            } else {
                                i = 0;
                            }
                            if (i != 0) {
                                Logging.log("Admarvel bitmap assest file-: Not Exists. Trying to download...");
                                try {
                                    new C0266b(context, view).execute(new String[]{source});
                                } catch (Exception e5) {
                                    Logging.log("Error downloading image: ");
                                    return;
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (Exception e6) {
                                }
                            }
                            throw th;
                        }
                    }
                } catch (Exception e7) {
                    e = e7;
                    fileInputStream = null;
                    e.printStackTrace();
                    if (fileInputStream == null) {
                        i = 0;
                    } else {
                        fileInputStream.close();
                        i = 0;
                    }
                    if (i != 0) {
                        Logging.log("Admarvel bitmap assest file-: Not Exists. Trying to download...");
                        new C0266b(context, view).execute(new String[]{source});
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = null;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            }
            i = 0;
            if (i != 0) {
                Logging.log("Admarvel bitmap assest file-: Not Exists. Trying to download...");
                new C0266b(context, view).execute(new String[]{source});
            }
        }
    }

    public static String convertBitmapTobyteArray(int ResId, Context cntxt) {
        Bitmap decodeResource = BitmapFactory.decodeResource(cntxt.getResources(), ResId);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeResource.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < toByteArray.length - 1; i++) {
            stringBuilder.append(toByteArray[i]);
            stringBuilder.append(Stomp.COMMA);
        }
        stringBuilder.append(toByteArray[toByteArray.length - 1]);
        return stringBuilder.toString();
    }

    public static String convertBitmapTobyteArray(Bitmap bitmap) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < toByteArray.length - 1; i++) {
            stringBuilder.append(toByteArray[i]);
            stringBuilder.append(Stomp.COMMA);
        }
        stringBuilder.append(toByteArray[toByteArray.length - 1]);
        return stringBuilder.toString();
    }

    private static void getBackwardDisableDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_BACK_DISABLE_BUTTON) == null) {
            bitmapFromLocalFolder("backward_disable", context, v);
            return;
        }
        try {
            C0265a.BACK_DISABLE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_BACK_DISABLE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for back_disable");
            bitmapFromLocalFolder("backward_disable", context, v);
        }
    }

    private static void getBackwardDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_BACK_BUTTON) == null) {
            bitmapFromLocalFolder("backward", context, v);
            return;
        }
        try {
            C0265a.BACK.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_BACK_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for back ");
            bitmapFromLocalFolder("backward", context, v);
        }
    }

    public static void getBitMapDrawable(String imageName, Context context, View v) {
        if (imageName.equals("backward_disable")) {
            getBackwardDisableDrawable(context, v);
        } else if (imageName.equals("backward")) {
            getBackwardDrawable(context, v);
        } else if (imageName.equals("close")) {
            getCloseDrawable(context, v);
        } else if (imageName.equals("done")) {
            getDoneDrawable(context, v);
        } else if (imageName.equals("mute")) {
            getMuteDrawable(context, v);
        } else if (imageName.equals("open_url_disabled")) {
            getOpenUrlDisabledDrawable(context, v);
        } else if (imageName.equals("open_url")) {
            getOpenUrlDrawable(context, v);
        } else if (imageName.equals("pause")) {
            getPauseDrawable(context, v);
        } else if (imageName.equals("play_movie")) {
            getPlayDrawable(context, v);
        } else if (imageName.equals("replay")) {
            getReplayDrawable(context, v);
        } else if (imageName.equals("resume_disable")) {
            getResumeDisableDrawable(context, v);
        } else if (imageName.equals("resume")) {
            getResumeDrawable(context, v);
        } else if (imageName.equals("stop")) {
            getStopDrawable(context, v);
        } else if (imageName.equals("time")) {
            getTimeDrawable(context, v);
        } else if (imageName.equals("unmute")) {
            getUnmuteDrawable(context, v);
        }
    }

    private static void getCloseDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_CLOSE_BUTTON) == null) {
            bitmapFromLocalFolder("close", context, v);
            return;
        }
        try {
            C0265a.CLOSE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_CLOSE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for close ");
            bitmapFromLocalFolder("close", context, v);
        }
    }

    private static void getDoneDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_DONE_BUTTON) == null) {
            bitmapFromLocalFolder("done", context, v);
            return;
        }
        try {
            C0265a.DONE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_DONE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for done ");
            bitmapFromLocalFolder("done", context, v);
        }
    }

    private static void getMuteDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_MUTE_BUTTON) == null) {
            bitmapFromLocalFolder("mute", context, v);
            return;
        }
        try {
            C0265a.MUTE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_MUTE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for mute ");
            bitmapFromLocalFolder("mute", context, v);
        }
    }

    private static void getOpenUrlDisabledDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_OPEN_URL_DISABLED_BUTTON) == null) {
            bitmapFromLocalFolder("open_url_disabled", context, v);
            return;
        }
        try {
            C0265a.OPEN_URL_DISABLED.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_OPEN_URL_DISABLED_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for open_url_disabled ");
            bitmapFromLocalFolder("open_url_disabled", context, v);
        }
    }

    private static void getOpenUrlDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_OPEN_URL_BUTTON) == null) {
            bitmapFromLocalFolder("open_url", context, v);
            return;
        }
        try {
            C0265a.OPEN_URL.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_OPEN_URL_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for open_url  ");
            bitmapFromLocalFolder("open_url", context, v);
        }
    }

    private static void getPauseDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_PAUSE_BUTTON) == null) {
            bitmapFromLocalFolder("pause", context, v);
            return;
        }
        try {
            C0265a.PAUSE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_PAUSE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for pause  ");
            bitmapFromLocalFolder("pause", context, v);
        }
    }

    private static void getPlayDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_PLAY_BUTTON) == null) {
            bitmapFromLocalFolder("play_movie", context, v);
            return;
        }
        try {
            C0265a.PLAY.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_PLAY_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for play  ");
            bitmapFromLocalFolder("play_movie", context, v);
        }
    }

    private static void getReplayDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_REPLAY_BUTTON) == null) {
            bitmapFromLocalFolder("replay", context, v);
            return;
        }
        try {
            C0265a.REPLAY.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_REPLAY_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for replay  ");
            bitmapFromLocalFolder("replay", context, v);
        }
    }

    private static void getResumeDisableDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_RESUME_DISABLE_BUTTON) == null) {
            bitmapFromLocalFolder("resume_disable", context, v);
            return;
        }
        try {
            C0265a.RESUME_DISABLE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_RESUME_DISABLE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for resume disable  ");
            bitmapFromLocalFolder("resume_disable", context, v);
        }
    }

    private static void getResumeDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_RESUME_BUTTON) == null) {
            bitmapFromLocalFolder("resume", context, v);
            return;
        }
        try {
            C0265a.RESUME.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_RESUME_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for resume  ");
            bitmapFromLocalFolder("resume", context, v);
        }
    }

    private static void getStopDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_STOP_BUTTON) == null) {
            bitmapFromLocalFolder("stop", context, v);
            return;
        }
        try {
            C0265a.STOP.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_STOP_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for resume  ");
            bitmapFromLocalFolder("stop", context, v);
        }
    }

    private static void getTimeDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_TIME_BUTTON) == null) {
            bitmapFromLocalFolder("time", context, v);
            return;
        }
        try {
            C0265a.TIME.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_TIME_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for resume  ");
            bitmapFromLocalFolder("time", context, v);
        }
    }

    private static void getUnmuteDrawable(Context context, View v) {
        if (AdMarvelUtils.getAdMarvelOptionalFlags() == null || AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_UNMUTE_BUTTON) == null) {
            bitmapFromLocalFolder("unmute", context, v);
            return;
        }
        try {
            C0265a.UNMUTE.m524a(context, (String) AdMarvelUtils.getAdMarvelOptionalFlags().get(ADMARVEL_CUSTOM_UNMUTE_BUTTON), v);
        } catch (Exception e) {
            Logging.log("Error in setting custom bitmap for unmute  ");
            bitmapFromLocalFolder("unmute", context, v);
        }
    }

    private static void setDrawable(View view, BitmapDrawable drawable) {
        if (view != null && drawable != null) {
            try {
                if (view instanceof FullScreenControls) {
                    ((FullScreenControls) view).setBackgroundDrawable(drawable);
                } else if (view instanceof ImageView) {
                    ((ImageView) view).setImageDrawable(drawable);
                }
            } catch (Exception e) {
            }
        }
    }

    public static void writeByteArrayFromBitMap(int ResId, Context cntxt, String fileName) {
        File file;
        Object e;
        PrintWriter printWriter = null;
        Bitmap decodeResource = BitmapFactory.decodeResource(cntxt.getResources(), ResId);
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeResource.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] toByteArray = byteArrayOutputStream.toByteArray();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            int i;
            File file2 = new File(Environment.getExternalStorageDirectory(), "AdmBitmapTemp");
            if (!file2.exists()) {
                file2.mkdir();
            }
            try {
                file = new File(file2, fileName + ".txt");
                try {
                    file.createNewFile();
                } catch (Exception e2) {
                    e = e2;
                    System.out.println("ex: " + e);
                    if (file == null) {
                        try {
                            printWriter = new PrintWriter(file.getAbsolutePath(), HttpRequest.CHARSET_UTF8);
                        } catch (FileNotFoundException e3) {
                            e3.printStackTrace();
                        } catch (UnsupportedEncodingException e4) {
                            e4.printStackTrace();
                        }
                        if (printWriter == null) {
                            stringBuilder.append("{");
                            printWriter.print("{");
                            for (i = 0; i < toByteArray.length; i++) {
                                stringBuilder.append(toByteArray[i]);
                                stringBuilder.append(Stomp.COMMA);
                                printWriter.print(toByteArray[i]);
                                printWriter.print(Stomp.COMMA);
                            }
                            printWriter.print("}");
                            stringBuilder.append("}");
                            if (printWriter == null) {
                                printWriter.close();
                            }
                        }
                    }
                }
            } catch (Exception e5) {
                e = e5;
                file = null;
                System.out.println("ex: " + e);
                if (file == null) {
                    printWriter = new PrintWriter(file.getAbsolutePath(), HttpRequest.CHARSET_UTF8);
                    if (printWriter == null) {
                        stringBuilder.append("{");
                        printWriter.print("{");
                        for (i = 0; i < toByteArray.length; i++) {
                            stringBuilder.append(toByteArray[i]);
                            stringBuilder.append(Stomp.COMMA);
                            printWriter.print(toByteArray[i]);
                            printWriter.print(Stomp.COMMA);
                        }
                        printWriter.print("}");
                        stringBuilder.append("}");
                        if (printWriter == null) {
                            printWriter.close();
                        }
                    }
                }
            }
            if (file == null) {
                printWriter = new PrintWriter(file.getAbsolutePath(), HttpRequest.CHARSET_UTF8);
                if (printWriter == null) {
                    stringBuilder.append("{");
                    printWriter.print("{");
                    for (i = 0; i < toByteArray.length; i++) {
                        stringBuilder.append(toByteArray[i]);
                        stringBuilder.append(Stomp.COMMA);
                        printWriter.print(toByteArray[i]);
                        printWriter.print(Stomp.COMMA);
                    }
                    printWriter.print("}");
                    stringBuilder.append("}");
                    if (printWriter == null) {
                        printWriter.close();
                    }
                }
            }
        } catch (Exception e52) {
            System.out.println("e: " + e52);
        }
    }
}
