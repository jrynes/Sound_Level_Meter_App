package com.google.android.gms.ads.internal.request;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzhb;
import com.google.android.gms.internal.zzna;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@zzhb
public final class LargeParcelTeleporter implements SafeParcelable {
    public static final Creator<LargeParcelTeleporter> CREATOR;
    final int mVersionCode;
    ParcelFileDescriptor zzIq;
    private Parcelable zzIr;
    private boolean zzIs;

    /* renamed from: com.google.android.gms.ads.internal.request.LargeParcelTeleporter.1 */
    class C06141 implements Runnable {
        final /* synthetic */ OutputStream zzIt;
        final /* synthetic */ byte[] zzIu;
        final /* synthetic */ LargeParcelTeleporter zzIv;

        C06141(LargeParcelTeleporter largeParcelTeleporter, OutputStream outputStream, byte[] bArr) {
            this.zzIv = largeParcelTeleporter;
            this.zzIt = outputStream;
            this.zzIu = bArr;
        }

        public void run() {
            Throwable e;
            Closeable dataOutputStream;
            try {
                dataOutputStream = new DataOutputStream(this.zzIt);
                try {
                    dataOutputStream.writeInt(this.zzIu.length);
                    dataOutputStream.write(this.zzIu);
                    if (dataOutputStream == null) {
                        zzna.zzb(this.zzIt);
                    } else {
                        zzna.zzb(dataOutputStream);
                    }
                } catch (IOException e2) {
                    e = e2;
                    try {
                        zzb.zzb("Error transporting the ad response", e);
                        zzr.zzbF().zzb(e, true);
                        if (dataOutputStream != null) {
                            zzna.zzb(this.zzIt);
                        } else {
                            zzna.zzb(dataOutputStream);
                        }
                    } catch (Throwable th) {
                        e = th;
                        if (dataOutputStream != null) {
                            zzna.zzb(this.zzIt);
                        } else {
                            zzna.zzb(dataOutputStream);
                        }
                        throw e;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                dataOutputStream = null;
                zzb.zzb("Error transporting the ad response", e);
                zzr.zzbF().zzb(e, true);
                if (dataOutputStream != null) {
                    zzna.zzb(dataOutputStream);
                } else {
                    zzna.zzb(this.zzIt);
                }
            } catch (Throwable th2) {
                e = th2;
                dataOutputStream = null;
                if (dataOutputStream != null) {
                    zzna.zzb(dataOutputStream);
                } else {
                    zzna.zzb(this.zzIt);
                }
                throw e;
            }
        }
    }

    static {
        CREATOR = new zzl();
    }

    LargeParcelTeleporter(int versionCode, ParcelFileDescriptor parcelFileDescriptor) {
        this.mVersionCode = versionCode;
        this.zzIq = parcelFileDescriptor;
        this.zzIr = null;
        this.zzIs = true;
    }

    public LargeParcelTeleporter(SafeParcelable teleportee) {
        this.mVersionCode = 1;
        this.zzIq = null;
        this.zzIr = teleportee;
        this.zzIs = false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.zzIq == null) {
            Parcel obtain = Parcel.obtain();
            try {
                this.zzIr.writeToParcel(obtain, 0);
                byte[] marshall = obtain.marshall();
                this.zzIq = zzf(marshall);
            } finally {
                obtain.recycle();
            }
        }
        zzl.zza(this, dest, flags);
    }

    public <T extends SafeParcelable> T zza(Creator<T> creator) {
        if (this.zzIs) {
            if (this.zzIq == null) {
                zzb.m1672e("File descriptor is empty, returning null.");
                return null;
            }
            Closeable dataInputStream = new DataInputStream(new AutoCloseInputStream(this.zzIq));
            try {
                byte[] bArr = new byte[dataInputStream.readInt()];
                dataInputStream.readFully(bArr, 0, bArr.length);
                zzna.zzb(dataInputStream);
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.unmarshall(bArr, 0, bArr.length);
                    obtain.setDataPosition(0);
                    this.zzIr = (SafeParcelable) creator.createFromParcel(obtain);
                    this.zzIs = false;
                } finally {
                    obtain.recycle();
                }
            } catch (Throwable e) {
                throw new IllegalStateException("Could not read from parcel file descriptor", e);
            } catch (Throwable th) {
                zzna.zzb(dataInputStream);
            }
        }
        return (SafeParcelable) this.zzIr;
    }

    protected <T> ParcelFileDescriptor zzf(byte[] bArr) {
        Closeable autoCloseOutputStream;
        Throwable e;
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
            autoCloseOutputStream = new AutoCloseOutputStream(createPipe[1]);
            try {
                new Thread(new C06141(this, autoCloseOutputStream, bArr)).start();
                return createPipe[0];
            } catch (IOException e2) {
                e = e2;
            }
        } catch (IOException e3) {
            e = e3;
            autoCloseOutputStream = parcelFileDescriptor;
            zzb.zzb("Error transporting the ad response", e);
            zzr.zzbF().zzb(e, true);
            zzna.zzb(autoCloseOutputStream);
            return parcelFileDescriptor;
        }
    }
}
