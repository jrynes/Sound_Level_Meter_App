package com.google.android.gms.location.places;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.places.internal.zzn;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.xbill.DNS.WKSRecord.Service;

public class PlaceLikelihoodBuffer extends AbstractDataBuffer<PlaceLikelihood> implements Result {
    private final Context mContext;
    private final Status zzUX;
    private final String zzaPy;
    private final int zzvr;

    public static class zza {
        static int zzhP(int i) {
            switch (i) {
                case ActiveMQPrefetchPolicy.DEFAULT_INPUT_STREAM_PREFETCH /*100*/:
                case Service.HOSTNAME /*101*/:
                case Service.ISO_TSAP /*102*/:
                case Service.X400 /*103*/:
                case Service.X400_SND /*104*/:
                case Service.CSNET_NS /*105*/:
                case 106:
                case Service.RTELNET /*107*/:
                case 108:
                    return i;
                default:
                    throw new IllegalArgumentException("invalid source: " + i);
            }
        }
    }

    public PlaceLikelihoodBuffer(DataHolder dataHolder, int source, Context context) {
        super(dataHolder);
        this.mContext = context;
        this.zzUX = PlacesStatusCodes.zzhU(dataHolder.getStatusCode());
        this.zzvr = zza.zzhP(source);
        if (dataHolder == null || dataHolder.zzpZ() == null) {
            this.zzaPy = null;
        } else {
            this.zzaPy = dataHolder.zzpZ().getString("com.google.android.gms.location.places.PlaceLikelihoodBuffer.ATTRIBUTIONS_EXTRA_KEY");
        }
    }

    public static int zzH(Bundle bundle) {
        return bundle.getInt("com.google.android.gms.location.places.PlaceLikelihoodBuffer.SOURCE_EXTRA_KEY");
    }

    public PlaceLikelihood get(int position) {
        return new zzn(this.zzahi, position, this.mContext);
    }

    @Nullable
    public CharSequence getAttributions() {
        return this.zzaPy;
    }

    public Status getStatus() {
        return this.zzUX;
    }

    public String toString() {
        return zzw.zzy(this).zzg(SettingsJsonConstants.APP_STATUS_KEY, getStatus()).zzg("attributions", this.zzaPy).toString();
    }
}
