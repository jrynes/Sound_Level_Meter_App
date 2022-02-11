package com.google.android.gms.location.places.internal;

import android.support.annotation.Nullable;
import android.text.style.CharacterStyle;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.internal.AutocompletePredictionEntity.SubstringEntity;
import java.util.Collections;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp;

public class zzb extends zzt implements AutocompletePrediction {
    public zzb(DataHolder dataHolder, int i) {
        super(dataHolder, i);
    }

    private String zzzh() {
        return zzG("ap_description", Stomp.EMPTY);
    }

    private String zzzi() {
        return zzG("ap_primary_text", Stomp.EMPTY);
    }

    private String zzzj() {
        return zzG("ap_secondary_text", Stomp.EMPTY);
    }

    private List<SubstringEntity> zzzk() {
        return zza("ap_matched_subscriptions", SubstringEntity.CREATOR, Collections.emptyList());
    }

    private List<SubstringEntity> zzzl() {
        return zza("ap_primary_text_matched", SubstringEntity.CREATOR, Collections.emptyList());
    }

    private List<SubstringEntity> zzzm() {
        return zza("ap_secondary_text_matched", SubstringEntity.CREATOR, Collections.emptyList());
    }

    public /* synthetic */ Object freeze() {
        return zzzf();
    }

    public String getDescription() {
        return zzzh();
    }

    public CharSequence getFullText(@Nullable CharacterStyle matchStyle) {
        return zzc.zza(zzzh(), zzzk(), matchStyle);
    }

    public List<SubstringEntity> getMatchedSubstrings() {
        return zzzk();
    }

    public String getPlaceId() {
        return zzG("ap_place_id", null);
    }

    public List<Integer> getPlaceTypes() {
        return zza("ap_place_types", Collections.emptyList());
    }

    public CharSequence getPrimaryText(@Nullable CharacterStyle matchStyle) {
        return zzc.zza(zzzi(), zzzl(), matchStyle);
    }

    public CharSequence getSecondaryText(@Nullable CharacterStyle matchStyle) {
        return zzc.zza(zzzj(), zzzm(), matchStyle);
    }

    public AutocompletePrediction zzzf() {
        return AutocompletePredictionEntity.zza(getPlaceId(), getPlaceTypes(), zzzg(), zzzh(), zzzk(), zzzi(), zzzl(), zzzj(), zzzm());
    }

    public int zzzg() {
        return zzz("ap_personalization_type", 6);
    }
}
