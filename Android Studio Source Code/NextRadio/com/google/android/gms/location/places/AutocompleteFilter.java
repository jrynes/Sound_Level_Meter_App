package com.google.android.gms.location.places;

import android.os.Parcel;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutocompleteFilter implements SafeParcelable {
    public static final zzc CREATOR;
    public static final int TYPE_FILTER_ADDRESS = 2;
    public static final int TYPE_FILTER_CITIES = 5;
    public static final int TYPE_FILTER_ESTABLISHMENT = 34;
    public static final int TYPE_FILTER_GEOCODE = 1007;
    public static final int TYPE_FILTER_NONE = 0;
    public static final int TYPE_FILTER_REGIONS = 4;
    final int mVersionCode;
    final boolean zzaPg;
    final List<Integer> zzaPh;
    final int zzaPi;

    public static final class Builder {
        private boolean zzaPg;
        private int zzaPi;

        public Builder() {
            this.zzaPg = false;
            this.zzaPi = AutocompleteFilter.TYPE_FILTER_NONE;
        }

        public AutocompleteFilter build() {
            return new AutocompleteFilter(1, this.zzaPg, AutocompleteFilter.zzhJ(this.zzaPi));
        }

        public Builder setTypeFilter(int typeFilter) {
            this.zzaPi = typeFilter;
            return this;
        }
    }

    static {
        CREATOR = new zzc();
    }

    AutocompleteFilter(int versionCode, boolean includeQueryPredictions, List<Integer> typeFilterAsList) {
        boolean z = true;
        this.mVersionCode = versionCode;
        this.zzaPh = typeFilterAsList;
        this.zzaPi = zzg(typeFilterAsList);
        if (this.mVersionCode < 1) {
            if (includeQueryPredictions) {
                z = false;
            }
            this.zzaPg = z;
            return;
        }
        this.zzaPg = includeQueryPredictions;
    }

    @Deprecated
    public static AutocompleteFilter create(@Nullable Collection<Integer> placeTypes) {
        return new Builder().setTypeFilter(zzg(placeTypes)).build();
    }

    private static int zzg(@Nullable Collection<Integer> collection) {
        return (collection == null || collection.isEmpty()) ? TYPE_FILTER_NONE : ((Integer) collection.iterator().next()).intValue();
    }

    private static List<Integer> zzhJ(int i) {
        return Arrays.asList(new Integer[]{Integer.valueOf(i)});
    }

    public int describeContents() {
        return TYPE_FILTER_NONE;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AutocompleteFilter)) {
            return false;
        }
        return this.zzaPi == this.zzaPi && this.zzaPg == ((AutocompleteFilter) object).zzaPg;
    }

    @Deprecated
    public Set<Integer> getPlaceTypes() {
        return new HashSet(zzhJ(this.zzaPi));
    }

    public int getTypeFilter() {
        return this.zzaPi;
    }

    public int hashCode() {
        Object[] objArr = new Object[TYPE_FILTER_ADDRESS];
        objArr[TYPE_FILTER_NONE] = Boolean.valueOf(this.zzaPg);
        objArr[1] = Integer.valueOf(this.zzaPi);
        return zzw.hashCode(objArr);
    }

    public String toString() {
        return zzw.zzy(this).zzg("includeQueryPredictions", Boolean.valueOf(this.zzaPg)).zzg("typeFilter", Integer.valueOf(this.zzaPi)).toString();
    }

    public void writeToParcel(Parcel parcel, int flags) {
        zzc.zza(this, parcel, flags);
    }
}
