package com.google.android.gms.location.places.internal;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceImpl.zza;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.activemq.transport.stomp.Stomp;

public class zzr extends zzt implements Place {
    private final String zzaPH;

    public zzr(DataHolder dataHolder, int i, Context context) {
        super(dataHolder, i);
        this.zzaPH = zzG("place_id", Stomp.EMPTY);
    }

    private PlaceImpl zzzA() {
        PlaceImpl zzzx = new zza().zzeo(getAddress().toString()).zzy(zzzq()).zzem(getId()).zzan(zzzr()).zza(getLatLng()).zzf(zzzo()).zzen(getName().toString()).zzep(getPhoneNumber().toString()).zzhX(getPriceLevel()).zzg(getRating()).zzx(getPlaceTypes()).zza(getViewport()).zzo(getWebsiteUri()).zzzx();
        zzzx.setLocale(getLocale());
        return zzzx;
    }

    private List<String> zzzq() {
        return zzb("place_attributions", Collections.emptyList());
    }

    public /* synthetic */ Object freeze() {
        return zzzw();
    }

    public CharSequence getAddress() {
        return zzG("place_address", Stomp.EMPTY);
    }

    public CharSequence getAttributions() {
        return zzc.zzj(zzzq());
    }

    public String getId() {
        return this.zzaPH;
    }

    public LatLng getLatLng() {
        return (LatLng) zza("place_lat_lng", LatLng.CREATOR);
    }

    public Locale getLocale() {
        Object zzG = zzG("place_locale", Stomp.EMPTY);
        return !TextUtils.isEmpty(zzG) ? new Locale(zzG) : Locale.getDefault();
    }

    public CharSequence getName() {
        return zzG("place_name", Stomp.EMPTY);
    }

    public CharSequence getPhoneNumber() {
        return zzG("place_phone_number", Stomp.EMPTY);
    }

    public List<Integer> getPlaceTypes() {
        return zza("place_types", Collections.emptyList());
    }

    public int getPriceLevel() {
        return zzz("place_price_level", -1);
    }

    public float getRating() {
        return zzb("place_rating", (float) GroundOverlayOptions.NO_DIMENSION);
    }

    public LatLngBounds getViewport() {
        return (LatLngBounds) zza("place_viewport", LatLngBounds.CREATOR);
    }

    public Uri getWebsiteUri() {
        String zzG = zzG("place_website_uri", null);
        return zzG == null ? null : Uri.parse(zzG);
    }

    public float zzzo() {
        return zzb("place_level_number", 0.0f);
    }

    public boolean zzzr() {
        return zzl("place_is_permanently_closed", false);
    }

    public Place zzzw() {
        return zzzA();
    }
}
