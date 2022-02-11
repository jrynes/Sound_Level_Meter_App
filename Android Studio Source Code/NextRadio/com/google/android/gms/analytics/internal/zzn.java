package com.google.android.gms.analytics.internal;

import android.content.Context;
import com.google.android.gms.common.internal.zzx;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class zzn extends zzd {
    private volatile String zzPO;
    private Future<String> zzRr;

    /* renamed from: com.google.android.gms.analytics.internal.zzn.1 */
    class C06691 implements Callable<String> {
        final /* synthetic */ zzn zzRs;

        C06691(zzn com_google_android_gms_analytics_internal_zzn) {
            this.zzRs = com_google_android_gms_analytics_internal_zzn;
        }

        public /* synthetic */ Object call() throws Exception {
            return zzkp();
        }

        public String zzkp() throws Exception {
            return this.zzRs.zzkm();
        }
    }

    /* renamed from: com.google.android.gms.analytics.internal.zzn.2 */
    class C06702 implements Callable<String> {
        final /* synthetic */ zzn zzRs;

        C06702(zzn com_google_android_gms_analytics_internal_zzn) {
            this.zzRs = com_google_android_gms_analytics_internal_zzn;
        }

        public /* synthetic */ Object call() throws Exception {
            return zzkp();
        }

        public String zzkp() throws Exception {
            return this.zzRs.zzkn();
        }
    }

    protected zzn(zzf com_google_android_gms_analytics_internal_zzf) {
        super(com_google_android_gms_analytics_internal_zzf);
    }

    private boolean zzh(Context context, String str) {
        boolean z = false;
        zzx.zzcM(str);
        zzx.zzcE("ClientId should be saved from worker thread");
        FileOutputStream fileOutputStream = null;
        try {
            zza("Storing clientId", str);
            fileOutputStream = context.openFileOutput("gaClientId", 0);
            fileOutputStream.write(str.getBytes());
            z = true;
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    zze("Failed to close clientId writing stream", e);
                }
            }
        } catch (FileNotFoundException e2) {
            zze("Error creating clientId file", e2);
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e3) {
                    zze("Failed to close clientId writing stream", e3);
                }
            }
        } catch (IOException e32) {
            zze("Error writing to clientId file", e32);
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e322) {
                    zze("Failed to close clientId writing stream", e322);
                }
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e3222) {
                    zze("Failed to close clientId writing stream", e3222);
                }
            }
        }
        return z;
    }

    private String zzkn() {
        String zzko = zzko();
        try {
            return !zzh(zzjo().getContext(), zzko) ? "0" : zzko;
        } catch (Exception e) {
            zze("Error saving clientId file", e);
            return "0";
        }
    }

    protected String zzac(Context context) {
        FileInputStream openFileInput;
        FileInputStream fileInputStream;
        Object e;
        Throwable th;
        zzx.zzcE("ClientId should be loaded from worker thread");
        try {
            openFileInput = context.openFileInput("gaClientId");
            try {
                byte[] bArr = new byte[36];
                int read = openFileInput.read(bArr, 0, bArr.length);
                if (openFileInput.available() > 0) {
                    zzbg("clientId file seems corrupted, deleting it.");
                    openFileInput.close();
                    context.deleteFile("gaClientId");
                    if (openFileInput == null) {
                        return null;
                    }
                    try {
                        openFileInput.close();
                        return null;
                    } catch (IOException e2) {
                        zze("Failed to close client id reading stream", e2);
                        return null;
                    }
                } else if (read < 14) {
                    zzbg("clientId file is empty, deleting it.");
                    openFileInput.close();
                    context.deleteFile("gaClientId");
                    if (openFileInput == null) {
                        return null;
                    }
                    try {
                        openFileInput.close();
                        return null;
                    } catch (IOException e22) {
                        zze("Failed to close client id reading stream", e22);
                        return null;
                    }
                } else {
                    openFileInput.close();
                    String str = new String(bArr, 0, read);
                    zza("Read client id from disk", str);
                    if (openFileInput != null) {
                        try {
                            openFileInput.close();
                        } catch (IOException e3) {
                            zze("Failed to close client id reading stream", e3);
                        }
                    }
                    return str;
                }
            } catch (FileNotFoundException e4) {
                fileInputStream = openFileInput;
                if (fileInputStream != null) {
                    return null;
                }
                try {
                    fileInputStream.close();
                    return null;
                } catch (IOException e222) {
                    zze("Failed to close client id reading stream", e222);
                    return null;
                }
            } catch (IOException e5) {
                e = e5;
                try {
                    zze("Error reading client id file, deleting it", e);
                    context.deleteFile("gaClientId");
                    if (openFileInput != null) {
                        return null;
                    }
                    try {
                        openFileInput.close();
                        return null;
                    } catch (IOException e2222) {
                        zze("Failed to close client id reading stream", e2222);
                        return null;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (openFileInput != null) {
                        try {
                            openFileInput.close();
                        } catch (IOException e22222) {
                            zze("Failed to close client id reading stream", e22222);
                        }
                    }
                    throw th;
                }
            }
        } catch (FileNotFoundException e6) {
            fileInputStream = null;
            if (fileInputStream != null) {
                return null;
            }
            fileInputStream.close();
            return null;
        } catch (IOException e7) {
            e = e7;
            openFileInput = null;
            zze("Error reading client id file, deleting it", e);
            context.deleteFile("gaClientId");
            if (openFileInput != null) {
                return null;
            }
            openFileInput.close();
            return null;
        } catch (Throwable th3) {
            openFileInput = null;
            th = th3;
            if (openFileInput != null) {
                openFileInput.close();
            }
            throw th;
        }
    }

    protected void zziJ() {
    }

    public String zzkk() {
        String str;
        zzjv();
        synchronized (this) {
            if (this.zzPO == null) {
                this.zzRr = zzjo().zzc(new C06691(this));
            }
            if (this.zzRr != null) {
                try {
                    this.zzPO = (String) this.zzRr.get();
                } catch (InterruptedException e) {
                    zzd("ClientId loading or generation was interrupted", e);
                    this.zzPO = "0";
                } catch (ExecutionException e2) {
                    zze("Failed to load or generate client id", e2);
                    this.zzPO = "0";
                }
                if (this.zzPO == null) {
                    this.zzPO = "0";
                }
                zza("Loaded clientId", this.zzPO);
                this.zzRr = null;
            }
            str = this.zzPO;
        }
        return str;
    }

    String zzkl() {
        synchronized (this) {
            this.zzPO = null;
            this.zzRr = zzjo().zzc(new C06702(this));
        }
        return zzkk();
    }

    String zzkm() {
        String zzac = zzac(zzjo().getContext());
        return zzac == null ? zzkn() : zzac;
    }

    protected String zzko() {
        return UUID.randomUUID().toString().toLowerCase();
    }
}
