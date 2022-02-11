package com.google.android.gms.tagmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tagmanager.ContainerHolder.ContainerAvailableListener;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Zone;

class zzo implements ContainerHolder {
    private Status zzUX;
    private final Looper zzagr;
    private boolean zzapK;
    private Container zzbhU;
    private Container zzbhV;
    private zzb zzbhW;
    private zza zzbhX;
    private TagManager zzbhY;

    public interface zza {
        String zzGd();

        void zzGf();

        void zzfT(String str);
    }

    private class zzb extends Handler {
        private final ContainerAvailableListener zzbhZ;
        final /* synthetic */ zzo zzbia;

        public zzb(zzo com_google_android_gms_tagmanager_zzo, ContainerAvailableListener containerAvailableListener, Looper looper) {
            this.zzbia = com_google_android_gms_tagmanager_zzo;
            super(looper);
            this.zzbhZ = containerAvailableListener;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Zone.PRIMARY /*1*/:
                    zzfV((String) msg.obj);
                default:
                    zzbg.m1675e("Don't know how to handle this message.");
            }
        }

        public void zzfU(String str) {
            sendMessage(obtainMessage(1, str));
        }

        protected void zzfV(String str) {
            this.zzbhZ.onContainerAvailable(this.zzbia, str);
        }
    }

    public zzo(Status status) {
        this.zzUX = status;
        this.zzagr = null;
    }

    public zzo(TagManager tagManager, Looper looper, Container container, zza com_google_android_gms_tagmanager_zzo_zza) {
        this.zzbhY = tagManager;
        if (looper == null) {
            looper = Looper.getMainLooper();
        }
        this.zzagr = looper;
        this.zzbhU = container;
        this.zzbhX = com_google_android_gms_tagmanager_zzo_zza;
        this.zzUX = Status.zzagC;
        tagManager.zza(this);
    }

    private void zzGe() {
        if (this.zzbhW != null) {
            this.zzbhW.zzfU(this.zzbhV.zzGb());
        }
    }

    public synchronized Container getContainer() {
        Container container = null;
        synchronized (this) {
            if (this.zzapK) {
                zzbg.m1675e("ContainerHolder is released.");
            } else {
                if (this.zzbhV != null) {
                    this.zzbhU = this.zzbhV;
                    this.zzbhV = null;
                }
                container = this.zzbhU;
            }
        }
        return container;
    }

    String getContainerId() {
        if (!this.zzapK) {
            return this.zzbhU.getContainerId();
        }
        zzbg.m1675e("getContainerId called on a released ContainerHolder.");
        return Stomp.EMPTY;
    }

    public Status getStatus() {
        return this.zzUX;
    }

    public synchronized void refresh() {
        if (this.zzapK) {
            zzbg.m1675e("Refreshing a released ContainerHolder.");
        } else {
            this.zzbhX.zzGf();
        }
    }

    public synchronized void release() {
        if (this.zzapK) {
            zzbg.m1675e("Releasing a released ContainerHolder.");
        } else {
            this.zzapK = true;
            this.zzbhY.zzb(this);
            this.zzbhU.release();
            this.zzbhU = null;
            this.zzbhV = null;
            this.zzbhX = null;
            this.zzbhW = null;
        }
    }

    public synchronized void setContainerAvailableListener(ContainerAvailableListener listener) {
        if (this.zzapK) {
            zzbg.m1675e("ContainerHolder is released.");
        } else if (listener == null) {
            this.zzbhW = null;
        } else {
            this.zzbhW = new zzb(this, listener, this.zzagr);
            if (this.zzbhV != null) {
                zzGe();
            }
        }
    }

    String zzGd() {
        if (!this.zzapK) {
            return this.zzbhX.zzGd();
        }
        zzbg.m1675e("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
        return Stomp.EMPTY;
    }

    public synchronized void zza(Container container) {
        if (!this.zzapK) {
            if (container == null) {
                zzbg.m1675e("Unexpected null container.");
            } else {
                this.zzbhV = container;
                zzGe();
            }
        }
    }

    public synchronized void zzfR(String str) {
        if (!this.zzapK) {
            this.zzbhU.zzfR(str);
        }
    }

    void zzfT(String str) {
        if (this.zzapK) {
            zzbg.m1675e("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
        } else {
            this.zzbhX.zzfT(str);
        }
    }
}
