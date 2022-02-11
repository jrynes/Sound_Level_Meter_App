package com.google.android.gms.location;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Comparator;

public class DetectedActivity implements SafeParcelable {
    public static final DetectedActivityCreator CREATOR;
    public static final int IN_VEHICLE = 0;
    public static final int ON_BICYCLE = 1;
    public static final int ON_FOOT = 2;
    public static final int RUNNING = 8;
    public static final int STILL = 3;
    public static final int TILTING = 5;
    public static final int UNKNOWN = 4;
    public static final int WALKING = 7;
    public static final Comparator<DetectedActivity> zzaNy;
    public static final int[] zzaNz;
    private final int mVersionCode;
    int zzaNA;
    int zzaNB;

    /* renamed from: com.google.android.gms.location.DetectedActivity.1 */
    static class C09271 implements Comparator<DetectedActivity> {
        C09271() {
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return zza((DetectedActivity) obj, (DetectedActivity) obj2);
        }

        public int zza(DetectedActivity detectedActivity, DetectedActivity detectedActivity2) {
            int compareTo = Integer.valueOf(detectedActivity2.getConfidence()).compareTo(Integer.valueOf(detectedActivity.getConfidence()));
            return compareTo == 0 ? Integer.valueOf(detectedActivity.getType()).compareTo(Integer.valueOf(detectedActivity2.getType())) : compareTo;
        }
    }

    static {
        zzaNy = new C09271();
        zzaNz = new int[]{IN_VEHICLE, ON_BICYCLE, ON_FOOT, UNKNOWN, TILTING, 6, WALKING, RUNNING, 10, 11, 12, 13, 14};
        CREATOR = new DetectedActivityCreator();
    }

    public DetectedActivity(int activityType, int confidence) {
        this.mVersionCode = ON_BICYCLE;
        this.zzaNA = activityType;
        this.zzaNB = confidence;
    }

    public DetectedActivity(int versionCode, int activityType, int confidence) {
        this.mVersionCode = versionCode;
        this.zzaNA = activityType;
        this.zzaNB = confidence;
    }

    private int zzhn(int i) {
        return i > 15 ? UNKNOWN : i;
    }

    public static String zzho(int i) {
        switch (i) {
            case IN_VEHICLE /*0*/:
                return "IN_VEHICLE";
            case ON_BICYCLE /*1*/:
                return "ON_BICYCLE";
            case ON_FOOT /*2*/:
                return "ON_FOOT";
            case STILL /*3*/:
                return "STILL";
            case UNKNOWN /*4*/:
                return "UNKNOWN";
            case TILTING /*5*/:
                return "TILTING";
            case WALKING /*7*/:
                return "WALKING";
            case RUNNING /*8*/:
                return "RUNNING";
            default:
                return Integer.toString(i);
        }
    }

    public int describeContents() {
        return IN_VEHICLE;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DetectedActivity detectedActivity = (DetectedActivity) o;
        return this.zzaNA == detectedActivity.zzaNA && this.zzaNB == detectedActivity.zzaNB;
    }

    public int getConfidence() {
        return this.zzaNB;
    }

    public int getType() {
        return zzhn(this.zzaNA);
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public int hashCode() {
        Object[] objArr = new Object[ON_FOOT];
        objArr[IN_VEHICLE] = Integer.valueOf(this.zzaNA);
        objArr[ON_BICYCLE] = Integer.valueOf(this.zzaNB);
        return zzw.hashCode(objArr);
    }

    public String toString() {
        return "DetectedActivity [type=" + zzho(getType()) + ", confidence=" + this.zzaNB + "]";
    }

    public void writeToParcel(Parcel out, int flags) {
        DetectedActivityCreator.zza(this, out, flags);
    }
}
