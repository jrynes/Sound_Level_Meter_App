package com.google.android.gms.internal;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.zzn;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.ads.internal.zzr;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Map;
import java.util.Set;
import org.apache.activemq.transport.discovery.multicast.MulticastDiscoveryAgent;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

@zzhb
public class zzfn extends zzfs {
    static final Set<String> zzDa;
    private AdSizeParcel zzCh;
    private String zzDb;
    private boolean zzDc;
    private int zzDd;
    private int zzDe;
    private int zzDf;
    private int zzDg;
    private final Activity zzDh;
    private ImageView zzDi;
    private LinearLayout zzDj;
    private zzft zzDk;
    private PopupWindow zzDl;
    private RelativeLayout zzDm;
    private ViewGroup zzDn;
    private int zzoG;
    private int zzoH;
    private final zzjp zzpD;
    private final Object zzpV;

    /* renamed from: com.google.android.gms.internal.zzfn.1 */
    class C08401 implements OnClickListener {
        final /* synthetic */ zzfn zzDo;

        C08401(zzfn com_google_android_gms_internal_zzfn) {
            this.zzDo = com_google_android_gms_internal_zzfn;
        }

        public void onClick(View view) {
            this.zzDo.zzp(true);
        }
    }

    static {
        zzDa = zzmr.zzc("top-left", "top-right", "top-center", "center", "bottom-left", "bottom-right", "bottom-center");
    }

    public zzfn(zzjp com_google_android_gms_internal_zzjp, zzft com_google_android_gms_internal_zzft) {
        super(com_google_android_gms_internal_zzjp, "resize");
        this.zzDb = "top-right";
        this.zzDc = true;
        this.zzDd = 0;
        this.zzDe = 0;
        this.zzoH = -1;
        this.zzDf = 0;
        this.zzDg = 0;
        this.zzoG = -1;
        this.zzpV = new Object();
        this.zzpD = com_google_android_gms_internal_zzjp;
        this.zzDh = com_google_android_gms_internal_zzjp.zzhP();
        this.zzDk = com_google_android_gms_internal_zzft;
    }

    private int[] zzeM() {
        if (!zzeO()) {
            return null;
        }
        if (this.zzDc) {
            return new int[]{this.zzDd + this.zzDf, this.zzDe + this.zzDg};
        }
        int[] zzf = zzr.zzbC().zzf(this.zzDh);
        int[] zzh = zzr.zzbC().zzh(this.zzDh);
        int i = zzf[0];
        int i2 = this.zzDd + this.zzDf;
        int i3 = this.zzDe + this.zzDg;
        if (i2 < 0) {
            i2 = 0;
        } else if (this.zzoG + i2 > i) {
            i2 = i - this.zzoG;
        }
        if (i3 < zzh[0]) {
            i3 = zzh[0];
        } else if (this.zzoH + i3 > zzh[1]) {
            i3 = zzh[1] - this.zzoH;
        }
        return new int[]{i2, i3};
    }

    private void zzh(Map<String, String> map) {
        if (!TextUtils.isEmpty((CharSequence) map.get(SettingsJsonConstants.ICON_WIDTH_KEY))) {
            this.zzoG = zzr.zzbC().zzaD((String) map.get(SettingsJsonConstants.ICON_WIDTH_KEY));
        }
        if (!TextUtils.isEmpty((CharSequence) map.get(SettingsJsonConstants.ICON_HEIGHT_KEY))) {
            this.zzoH = zzr.zzbC().zzaD((String) map.get(SettingsJsonConstants.ICON_HEIGHT_KEY));
        }
        if (!TextUtils.isEmpty((CharSequence) map.get("offsetX"))) {
            this.zzDf = zzr.zzbC().zzaD((String) map.get("offsetX"));
        }
        if (!TextUtils.isEmpty((CharSequence) map.get("offsetY"))) {
            this.zzDg = zzr.zzbC().zzaD((String) map.get("offsetY"));
        }
        if (!TextUtils.isEmpty((CharSequence) map.get("allowOffscreen"))) {
            this.zzDc = Boolean.parseBoolean((String) map.get("allowOffscreen"));
        }
        String str = (String) map.get("customClosePosition");
        if (!TextUtils.isEmpty(str)) {
            this.zzDb = str;
        }
    }

    public void zza(int i, int i2, boolean z) {
        synchronized (this.zzpV) {
            this.zzDd = i;
            this.zzDe = i2;
            if (this.zzDl != null && z) {
                int[] zzeM = zzeM();
                if (zzeM != null) {
                    this.zzDl.update(zzn.zzcS().zzb(this.zzDh, zzeM[0]), zzn.zzcS().zzb(this.zzDh, zzeM[1]), this.zzDl.getWidth(), this.zzDl.getHeight());
                    zzd(zzeM[0], zzeM[1]);
                } else {
                    zzp(true);
                }
            }
        }
    }

    void zzc(int i, int i2) {
        if (this.zzDk != null) {
            this.zzDk.zza(i, i2, this.zzoG, this.zzoH);
        }
    }

    void zzd(int i, int i2) {
        zzb(i, i2 - zzr.zzbC().zzh(this.zzDh)[0], this.zzoG, this.zzoH);
    }

    public void zze(int i, int i2) {
        this.zzDd = i;
        this.zzDe = i2;
    }

    boolean zzeL() {
        return this.zzoG > -1 && this.zzoH > -1;
    }

    public boolean zzeN() {
        boolean z;
        synchronized (this.zzpV) {
            z = this.zzDl != null;
        }
        return z;
    }

    boolean zzeO() {
        int[] zzf = zzr.zzbC().zzf(this.zzDh);
        int[] zzh = zzr.zzbC().zzh(this.zzDh);
        int i = zzf[0];
        int i2 = zzf[1];
        if (this.zzoG < 50 || this.zzoG > i) {
            zzb.zzaK("Width is too small or too large.");
            return false;
        } else if (this.zzoH < 50 || this.zzoH > i2) {
            zzb.zzaK("Height is too small or too large.");
            return false;
        } else if (this.zzoH == i2 && this.zzoG == i) {
            zzb.zzaK("Cannot resize to a full-screen ad.");
            return false;
        } else {
            if (this.zzDc) {
                int i3;
                String str = this.zzDb;
                boolean z = true;
                switch (str.hashCode()) {
                    case -1364013995:
                        if (str.equals("center")) {
                            z = true;
                            break;
                        }
                        break;
                    case -1012429441:
                        if (str.equals("top-left")) {
                            z = false;
                            break;
                        }
                        break;
                    case -655373719:
                        if (str.equals("bottom-left")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1163912186:
                        if (str.equals("bottom-right")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1288627767:
                        if (str.equals("bottom-center")) {
                            z = true;
                            break;
                        }
                        break;
                    case 1755462605:
                        if (str.equals("top-center")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case Tokenizer.EOF /*0*/:
                        i3 = this.zzDf + this.zzDd;
                        i2 = this.zzDe + this.zzDg;
                        break;
                    case Zone.PRIMARY /*1*/:
                        i3 = ((this.zzDd + this.zzDf) + (this.zzoG / 2)) - 25;
                        i2 = this.zzDe + this.zzDg;
                        break;
                    case Zone.SECONDARY /*2*/:
                        i3 = ((this.zzDd + this.zzDf) + (this.zzoG / 2)) - 25;
                        i2 = ((this.zzDe + this.zzDg) + (this.zzoH / 2)) - 25;
                        break;
                    case Protocol.GGP /*3*/:
                        i3 = this.zzDf + this.zzDd;
                        i2 = ((this.zzDe + this.zzDg) + this.zzoH) - 50;
                        break;
                    case Type.MF /*4*/:
                        i3 = ((this.zzDd + this.zzDf) + (this.zzoG / 2)) - 25;
                        i2 = ((this.zzDe + this.zzDg) + this.zzoH) - 50;
                        break;
                    case Service.RJE /*5*/:
                        i3 = ((this.zzDd + this.zzDf) + this.zzoG) - 50;
                        i2 = ((this.zzDe + this.zzDg) + this.zzoH) - 50;
                        break;
                    default:
                        i3 = ((this.zzDd + this.zzDf) + this.zzoG) - 50;
                        i2 = this.zzDe + this.zzDg;
                        break;
                }
                if (i3 < 0 || i3 + 50 > i || r2 < zzh[0] || r2 + 50 > zzh[1]) {
                    return false;
                }
            }
            return true;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zzi(java.util.Map<java.lang.String, java.lang.String> r13) {
        /*
        r12 = this;
        r4 = -1;
        r5 = 1;
        r3 = 0;
        r6 = r12.zzpV;
        monitor-enter(r6);
        r1 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x0011;
    L_0x000a:
        r1 = "Not an activity context. Cannot resize.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
    L_0x0010:
        return;
    L_0x0011:
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r1 = r1.zzaN();	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x0023;
    L_0x0019:
        r1 = "Webview is not yet available, size is not set.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x0020:
        r1 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        throw r1;
    L_0x0023:
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r1 = r1.zzaN();	 Catch:{ all -> 0x0020 }
        r1 = r1.zzui;	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0034;
    L_0x002d:
        r1 = "Is interstitial. Cannot resize an interstitial.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x0034:
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r1 = r1.zzhY();	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0043;
    L_0x003c:
        r1 = "Cannot resize an expanded banner.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x0043:
        r12.zzh(r13);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzeL();	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x0053;
    L_0x004c:
        r1 = "Invalid width and height options. Cannot resize.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x0053:
        r1 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r7 = r1.getWindow();	 Catch:{ all -> 0x0020 }
        if (r7 == 0) goto L_0x0061;
    L_0x005b:
        r1 = r7.getDecorView();	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x0068;
    L_0x0061:
        r1 = "Activity context is not ready, cannot get window or decor view.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x0068:
        r8 = r12.zzeM();	 Catch:{ all -> 0x0020 }
        if (r8 != 0) goto L_0x0075;
    L_0x006e:
        r1 = "Resize location out of screen or close button is not visible.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x0075:
        r1 = com.google.android.gms.ads.internal.client.zzn.zzcS();	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r9 = r12.zzoG;	 Catch:{ all -> 0x0020 }
        r9 = r1.zzb(r2, r9);	 Catch:{ all -> 0x0020 }
        r1 = com.google.android.gms.ads.internal.client.zzn.zzcS();	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r10 = r12.zzoH;	 Catch:{ all -> 0x0020 }
        r10 = r1.zzb(r2, r10);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r1 = r1.getView();	 Catch:{ all -> 0x0020 }
        r2 = r1.getParent();	 Catch:{ all -> 0x0020 }
        if (r2 == 0) goto L_0x01d5;
    L_0x0099:
        r1 = r2 instanceof android.view.ViewGroup;	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x01d5;
    L_0x009d:
        r0 = r2;
        r0 = (android.view.ViewGroup) r0;	 Catch:{ all -> 0x0020 }
        r1 = r0;
        r11 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r11 = r11.getView();	 Catch:{ all -> 0x0020 }
        r1.removeView(r11);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDl;	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x01ce;
    L_0x00ae:
        r2 = (android.view.ViewGroup) r2;	 Catch:{ all -> 0x0020 }
        r12.zzDn = r2;	 Catch:{ all -> 0x0020 }
        r1 = com.google.android.gms.ads.internal.zzr.zzbC();	 Catch:{ all -> 0x0020 }
        r2 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r2 = r2.getView();	 Catch:{ all -> 0x0020 }
        r1 = r1.zzk(r2);	 Catch:{ all -> 0x0020 }
        r2 = new android.widget.ImageView;	 Catch:{ all -> 0x0020 }
        r11 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r2.<init>(r11);	 Catch:{ all -> 0x0020 }
        r12.zzDi = r2;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDi;	 Catch:{ all -> 0x0020 }
        r2.setImageBitmap(r1);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r1 = r1.zzaN();	 Catch:{ all -> 0x0020 }
        r12.zzCh = r1;	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDn;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDi;	 Catch:{ all -> 0x0020 }
        r1.addView(r2);	 Catch:{ all -> 0x0020 }
    L_0x00dd:
        r1 = new android.widget.RelativeLayout;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r1.<init>(r2);	 Catch:{ all -> 0x0020 }
        r12.zzDm = r1;	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDm;	 Catch:{ all -> 0x0020 }
        r2 = 0;
        r1.setBackgroundColor(r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDm;	 Catch:{ all -> 0x0020 }
        r2 = new android.view.ViewGroup$LayoutParams;	 Catch:{ all -> 0x0020 }
        r2.<init>(r9, r10);	 Catch:{ all -> 0x0020 }
        r1.setLayoutParams(r2);	 Catch:{ all -> 0x0020 }
        r1 = com.google.android.gms.ads.internal.zzr.zzbC();	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDm;	 Catch:{ all -> 0x0020 }
        r11 = 0;
        r1 = r1.zza(r2, r9, r10, r11);	 Catch:{ all -> 0x0020 }
        r12.zzDl = r1;	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDl;	 Catch:{ all -> 0x0020 }
        r2 = 1;
        r1.setOutsideTouchable(r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDl;	 Catch:{ all -> 0x0020 }
        r2 = 1;
        r1.setTouchable(r2);	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDl;	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDc;	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x01dd;
    L_0x0115:
        r1 = r5;
    L_0x0116:
        r2.setClippingEnabled(r1);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDm;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r2 = r2.getView();	 Catch:{ all -> 0x0020 }
        r9 = -1;
        r10 = -1;
        r1.addView(r2, r9, r10);	 Catch:{ all -> 0x0020 }
        r1 = new android.widget.LinearLayout;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r1.<init>(r2);	 Catch:{ all -> 0x0020 }
        r12.zzDj = r1;	 Catch:{ all -> 0x0020 }
        r2 = new android.widget.RelativeLayout$LayoutParams;	 Catch:{ all -> 0x0020 }
        r1 = com.google.android.gms.ads.internal.client.zzn.zzcS();	 Catch:{ all -> 0x0020 }
        r9 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r10 = 50;
        r1 = r1.zzb(r9, r10);	 Catch:{ all -> 0x0020 }
        r9 = com.google.android.gms.ads.internal.client.zzn.zzcS();	 Catch:{ all -> 0x0020 }
        r10 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r11 = 50;
        r9 = r9.zzb(r10, r11);	 Catch:{ all -> 0x0020 }
        r2.<init>(r1, r9);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDb;	 Catch:{ all -> 0x0020 }
        r9 = r1.hashCode();	 Catch:{ all -> 0x0020 }
        switch(r9) {
            case -1364013995: goto L_0x01f6;
            case -1012429441: goto L_0x01e0;
            case -655373719: goto L_0x0201;
            case 1163912186: goto L_0x0217;
            case 1288627767: goto L_0x020c;
            case 1755462605: goto L_0x01eb;
            default: goto L_0x0155;
        };	 Catch:{ all -> 0x0020 }
    L_0x0155:
        r1 = r4;
    L_0x0156:
        switch(r1) {
            case 0: goto L_0x0222;
            case 1: goto L_0x022e;
            case 2: goto L_0x023a;
            case 3: goto L_0x0241;
            case 4: goto L_0x024d;
            case 5: goto L_0x0259;
            default: goto L_0x0159;
        };	 Catch:{ all -> 0x0020 }
    L_0x0159:
        r1 = 10;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        r1 = 11;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
    L_0x0163:
        r1 = r12.zzDj;	 Catch:{ all -> 0x0020 }
        r3 = new com.google.android.gms.internal.zzfn$1;	 Catch:{ all -> 0x0020 }
        r3.<init>(r12);	 Catch:{ all -> 0x0020 }
        r1.setOnClickListener(r3);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDj;	 Catch:{ all -> 0x0020 }
        r3 = "Close button";
        r1.setContentDescription(r3);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDm;	 Catch:{ all -> 0x0020 }
        r3 = r12.zzDj;	 Catch:{ all -> 0x0020 }
        r1.addView(r3, r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDl;	 Catch:{ RuntimeException -> 0x0265 }
        r2 = r7.getDecorView();	 Catch:{ RuntimeException -> 0x0265 }
        r3 = 0;
        r4 = com.google.android.gms.ads.internal.client.zzn.zzcS();	 Catch:{ RuntimeException -> 0x0265 }
        r5 = r12.zzDh;	 Catch:{ RuntimeException -> 0x0265 }
        r7 = 0;
        r7 = r8[r7];	 Catch:{ RuntimeException -> 0x0265 }
        r4 = r4.zzb(r5, r7);	 Catch:{ RuntimeException -> 0x0265 }
        r5 = com.google.android.gms.ads.internal.client.zzn.zzcS();	 Catch:{ RuntimeException -> 0x0265 }
        r7 = r12.zzDh;	 Catch:{ RuntimeException -> 0x0265 }
        r9 = 1;
        r9 = r8[r9];	 Catch:{ RuntimeException -> 0x0265 }
        r5 = r5.zzb(r7, r9);	 Catch:{ RuntimeException -> 0x0265 }
        r1.showAtLocation(r2, r3, r4, r5);	 Catch:{ RuntimeException -> 0x0265 }
        r1 = 0;
        r1 = r8[r1];	 Catch:{ all -> 0x0020 }
        r2 = 1;
        r2 = r8[r2];	 Catch:{ all -> 0x0020 }
        r12.zzc(r1, r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r2 = new com.google.android.gms.ads.internal.client.AdSizeParcel;	 Catch:{ all -> 0x0020 }
        r3 = r12.zzDh;	 Catch:{ all -> 0x0020 }
        r4 = new com.google.android.gms.ads.AdSize;	 Catch:{ all -> 0x0020 }
        r5 = r12.zzoG;	 Catch:{ all -> 0x0020 }
        r7 = r12.zzoH;	 Catch:{ all -> 0x0020 }
        r4.<init>(r5, r7);	 Catch:{ all -> 0x0020 }
        r2.<init>(r3, r4);	 Catch:{ all -> 0x0020 }
        r1.zza(r2);	 Catch:{ all -> 0x0020 }
        r1 = 0;
        r1 = r8[r1];	 Catch:{ all -> 0x0020 }
        r2 = 1;
        r2 = r8[r2];	 Catch:{ all -> 0x0020 }
        r12.zzd(r1, r2);	 Catch:{ all -> 0x0020 }
        r1 = "resized";
        r12.zzao(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x01ce:
        r1 = r12.zzDl;	 Catch:{ all -> 0x0020 }
        r1.dismiss();	 Catch:{ all -> 0x0020 }
        goto L_0x00dd;
    L_0x01d5:
        r1 = "Webview is detached, probably in the middle of a resize or expand.";
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
    L_0x01dd:
        r1 = r3;
        goto L_0x0116;
    L_0x01e0:
        r5 = "top-left";
        r1 = r1.equals(r5);	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0155;
    L_0x01e8:
        r1 = r3;
        goto L_0x0156;
    L_0x01eb:
        r3 = "top-center";
        r1 = r1.equals(r3);	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0155;
    L_0x01f3:
        r1 = r5;
        goto L_0x0156;
    L_0x01f6:
        r3 = "center";
        r1 = r1.equals(r3);	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0155;
    L_0x01fe:
        r1 = 2;
        goto L_0x0156;
    L_0x0201:
        r3 = "bottom-left";
        r1 = r1.equals(r3);	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0155;
    L_0x0209:
        r1 = 3;
        goto L_0x0156;
    L_0x020c:
        r3 = "bottom-center";
        r1 = r1.equals(r3);	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0155;
    L_0x0214:
        r1 = 4;
        goto L_0x0156;
    L_0x0217:
        r3 = "bottom-right";
        r1 = r1.equals(r3);	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x0155;
    L_0x021f:
        r1 = 5;
        goto L_0x0156;
    L_0x0222:
        r1 = 10;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        r1 = 9;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        goto L_0x0163;
    L_0x022e:
        r1 = 10;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        r1 = 14;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        goto L_0x0163;
    L_0x023a:
        r1 = 13;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        goto L_0x0163;
    L_0x0241:
        r1 = 12;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        r1 = 9;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        goto L_0x0163;
    L_0x024d:
        r1 = 12;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        r1 = 14;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        goto L_0x0163;
    L_0x0259:
        r1 = 12;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        r1 = 11;
        r2.addRule(r1);	 Catch:{ all -> 0x0020 }
        goto L_0x0163;
    L_0x0265:
        r1 = move-exception;
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0020 }
        r2.<init>();	 Catch:{ all -> 0x0020 }
        r3 = "Cannot show popup window: ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0020 }
        r1 = r1.getMessage();	 Catch:{ all -> 0x0020 }
        r1 = r2.append(r1);	 Catch:{ all -> 0x0020 }
        r1 = r1.toString();	 Catch:{ all -> 0x0020 }
        r12.zzam(r1);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDm;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r2 = r2.getView();	 Catch:{ all -> 0x0020 }
        r1.removeView(r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDn;	 Catch:{ all -> 0x0020 }
        if (r1 == 0) goto L_0x02a8;
    L_0x028f:
        r1 = r12.zzDn;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzDi;	 Catch:{ all -> 0x0020 }
        r1.removeView(r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzDn;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r2 = r2.getView();	 Catch:{ all -> 0x0020 }
        r1.addView(r2);	 Catch:{ all -> 0x0020 }
        r1 = r12.zzpD;	 Catch:{ all -> 0x0020 }
        r2 = r12.zzCh;	 Catch:{ all -> 0x0020 }
        r1.zza(r2);	 Catch:{ all -> 0x0020 }
    L_0x02a8:
        monitor-exit(r6);	 Catch:{ all -> 0x0020 }
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzfn.zzi(java.util.Map):void");
    }

    public void zzp(boolean z) {
        synchronized (this.zzpV) {
            if (this.zzDl != null) {
                this.zzDl.dismiss();
                this.zzDm.removeView(this.zzpD.getView());
                if (this.zzDn != null) {
                    this.zzDn.removeView(this.zzDi);
                    this.zzDn.addView(this.zzpD.getView());
                    this.zzpD.zza(this.zzCh);
                }
                if (z) {
                    zzao(MulticastDiscoveryAgent.DEFAULT_HOST_STR);
                    if (this.zzDk != null) {
                        this.zzDk.zzbf();
                    }
                }
                this.zzDl = null;
                this.zzDm = null;
                this.zzDn = null;
                this.zzDj = null;
            }
        }
    }
}
