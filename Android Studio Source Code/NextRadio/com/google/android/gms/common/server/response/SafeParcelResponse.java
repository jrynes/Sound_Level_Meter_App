package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.google.android.gms.internal.zzmn;
import com.google.android.gms.internal.zzmo;
import com.google.android.gms.internal.zznb;
import com.google.android.gms.internal.zznc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.activemq.transport.stomp.Stomp;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable {
    public static final zze CREATOR;
    private final String mClassName;
    private final int mVersionCode;
    private final FieldMappingDictionary zzamT;
    private final Parcel zzana;
    private final int zzanb;
    private int zzanc;
    private int zzand;

    static {
        CREATOR = new zze();
    }

    SafeParcelResponse(int versionCode, Parcel parcel, FieldMappingDictionary fieldMappingDictionary) {
        this.mVersionCode = versionCode;
        this.zzana = (Parcel) zzx.zzz(parcel);
        this.zzanb = 2;
        this.zzamT = fieldMappingDictionary;
        if (this.zzamT == null) {
            this.mClassName = null;
        } else {
            this.mClassName = this.zzamT.zzrB();
        }
        this.zzanc = 2;
    }

    private SafeParcelResponse(SafeParcelable safeParcelable, FieldMappingDictionary dictionary, String className) {
        this.mVersionCode = 1;
        this.zzana = Parcel.obtain();
        safeParcelable.writeToParcel(this.zzana, 0);
        this.zzanb = 1;
        this.zzamT = (FieldMappingDictionary) zzx.zzz(dictionary);
        this.mClassName = (String) zzx.zzz(className);
        this.zzanc = 2;
    }

    private static HashMap<Integer, Entry<String, Field<?, ?>>> zzN(Map<String, Field<?, ?>> map) {
        HashMap<Integer, Entry<String, Field<?, ?>>> hashMap = new HashMap();
        for (Entry entry : map.entrySet()) {
            hashMap.put(Integer.valueOf(((Field) entry.getValue()).zzrs()), entry);
        }
        return hashMap;
    }

    public static <T extends FastJsonResponse & SafeParcelable> SafeParcelResponse zza(T t) {
        String canonicalName = t.getClass().getCanonicalName();
        return new SafeParcelResponse((SafeParcelable) t, zzb(t), canonicalName);
    }

    private static void zza(FieldMappingDictionary fieldMappingDictionary, FastJsonResponse fastJsonResponse) {
        Class cls = fastJsonResponse.getClass();
        if (!fieldMappingDictionary.zzb(cls)) {
            Map zzrl = fastJsonResponse.zzrl();
            fieldMappingDictionary.zza(cls, zzrl);
            for (String str : zzrl.keySet()) {
                Field field = (Field) zzrl.get(str);
                Class zzrt = field.zzrt();
                if (zzrt != null) {
                    try {
                        zza(fieldMappingDictionary, (FastJsonResponse) zzrt.newInstance());
                    } catch (Throwable e) {
                        throw new IllegalStateException("Could not instantiate an object of type " + field.zzrt().getCanonicalName(), e);
                    } catch (Throwable e2) {
                        throw new IllegalStateException("Could not access object of type " + field.zzrt().getCanonicalName(), e2);
                    }
                }
            }
        }
    }

    private void zza(StringBuilder stringBuilder, int i, Object obj) {
        switch (i) {
            case Tokenizer.EOF /*0*/:
            case Zone.PRIMARY /*1*/:
            case Zone.SECONDARY /*2*/:
            case Protocol.GGP /*3*/:
            case Type.MF /*4*/:
            case Service.RJE /*5*/:
            case Protocol.TCP /*6*/:
                stringBuilder.append(obj);
            case Service.ECHO /*7*/:
                stringBuilder.append("\"").append(zznb.zzcU(obj.toString())).append("\"");
            case Protocol.EGP /*8*/:
                stringBuilder.append("\"").append(zzmo.zzj((byte[]) obj)).append("\"");
            case Service.DISCARD /*9*/:
                stringBuilder.append("\"").append(zzmo.zzk((byte[]) obj));
                stringBuilder.append("\"");
            case Protocol.BBN_RCC_MON /*10*/:
                zznc.zza(stringBuilder, (HashMap) obj);
            case Service.USERS /*11*/:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i);
        }
    }

    private void zza(StringBuilder stringBuilder, Field<?, ?> field, Parcel parcel, int i) {
        switch (field.zzrk()) {
            case Tokenizer.EOF /*0*/:
                zzb(stringBuilder, (Field) field, zza(field, Integer.valueOf(zza.zzg(parcel, i))));
            case Zone.PRIMARY /*1*/:
                zzb(stringBuilder, (Field) field, zza(field, zza.zzk(parcel, i)));
            case Zone.SECONDARY /*2*/:
                zzb(stringBuilder, (Field) field, zza(field, Long.valueOf(zza.zzi(parcel, i))));
            case Protocol.GGP /*3*/:
                zzb(stringBuilder, (Field) field, zza(field, Float.valueOf(zza.zzl(parcel, i))));
            case Type.MF /*4*/:
                zzb(stringBuilder, (Field) field, zza(field, Double.valueOf(zza.zzn(parcel, i))));
            case Service.RJE /*5*/:
                zzb(stringBuilder, (Field) field, zza(field, zza.zzo(parcel, i)));
            case Protocol.TCP /*6*/:
                zzb(stringBuilder, (Field) field, zza(field, Boolean.valueOf(zza.zzc(parcel, i))));
            case Service.ECHO /*7*/:
                zzb(stringBuilder, (Field) field, zza(field, zza.zzp(parcel, i)));
            case Protocol.EGP /*8*/:
            case Service.DISCARD /*9*/:
                zzb(stringBuilder, (Field) field, zza(field, zza.zzs(parcel, i)));
            case Protocol.BBN_RCC_MON /*10*/:
                zzb(stringBuilder, (Field) field, zza(field, zzl(zza.zzr(parcel, i))));
            case Service.USERS /*11*/:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown field out type = " + field.zzrk());
        }
    }

    private void zza(StringBuilder stringBuilder, String str, Field<?, ?> field, Parcel parcel, int i) {
        stringBuilder.append("\"").append(str).append("\":");
        if (field.zzrv()) {
            zza(stringBuilder, field, parcel, i);
        } else {
            zzb(stringBuilder, field, parcel, i);
        }
    }

    private void zza(StringBuilder stringBuilder, Map<String, Field<?, ?>> map, Parcel parcel) {
        HashMap zzN = zzN(map);
        stringBuilder.append('{');
        int zzau = zza.zzau(parcel);
        Object obj = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            Entry entry = (Entry) zzN.get(Integer.valueOf(zza.zzca(zzat)));
            if (entry != null) {
                if (obj != null) {
                    stringBuilder.append(Stomp.COMMA);
                }
                zza(stringBuilder, (String) entry.getKey(), (Field) entry.getValue(), parcel, zzat);
                obj = 1;
            }
        }
        if (parcel.dataPosition() != zzau) {
            throw new zza.zza("Overread allowed size end=" + zzau, parcel);
        }
        stringBuilder.append('}');
    }

    private static FieldMappingDictionary zzb(FastJsonResponse fastJsonResponse) {
        FieldMappingDictionary fieldMappingDictionary = new FieldMappingDictionary(fastJsonResponse.getClass());
        zza(fieldMappingDictionary, fastJsonResponse);
        fieldMappingDictionary.zzrz();
        fieldMappingDictionary.zzry();
        return fieldMappingDictionary;
    }

    private void zzb(StringBuilder stringBuilder, Field<?, ?> field, Parcel parcel, int i) {
        if (field.zzrq()) {
            stringBuilder.append("[");
            switch (field.zzrk()) {
                case Tokenizer.EOF /*0*/:
                    zzmn.zza(stringBuilder, zza.zzv(parcel, i));
                    break;
                case Zone.PRIMARY /*1*/:
                    zzmn.zza(stringBuilder, zza.zzx(parcel, i));
                    break;
                case Zone.SECONDARY /*2*/:
                    zzmn.zza(stringBuilder, zza.zzw(parcel, i));
                    break;
                case Protocol.GGP /*3*/:
                    zzmn.zza(stringBuilder, zza.zzy(parcel, i));
                    break;
                case Type.MF /*4*/:
                    zzmn.zza(stringBuilder, zza.zzz(parcel, i));
                    break;
                case Service.RJE /*5*/:
                    zzmn.zza(stringBuilder, zza.zzA(parcel, i));
                    break;
                case Protocol.TCP /*6*/:
                    zzmn.zza(stringBuilder, zza.zzu(parcel, i));
                    break;
                case Service.ECHO /*7*/:
                    zzmn.zza(stringBuilder, zza.zzB(parcel, i));
                    break;
                case Protocol.EGP /*8*/:
                case Service.DISCARD /*9*/:
                case Protocol.BBN_RCC_MON /*10*/:
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                case Service.USERS /*11*/:
                    Parcel[] zzF = zza.zzF(parcel, i);
                    int length = zzF.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        if (i2 > 0) {
                            stringBuilder.append(Stomp.COMMA);
                        }
                        zzF[i2].setDataPosition(0);
                        zza(stringBuilder, field.zzrx(), zzF[i2]);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown field type out.");
            }
            stringBuilder.append("]");
            return;
        }
        switch (field.zzrk()) {
            case Tokenizer.EOF /*0*/:
                stringBuilder.append(zza.zzg(parcel, i));
            case Zone.PRIMARY /*1*/:
                stringBuilder.append(zza.zzk(parcel, i));
            case Zone.SECONDARY /*2*/:
                stringBuilder.append(zza.zzi(parcel, i));
            case Protocol.GGP /*3*/:
                stringBuilder.append(zza.zzl(parcel, i));
            case Type.MF /*4*/:
                stringBuilder.append(zza.zzn(parcel, i));
            case Service.RJE /*5*/:
                stringBuilder.append(zza.zzo(parcel, i));
            case Protocol.TCP /*6*/:
                stringBuilder.append(zza.zzc(parcel, i));
            case Service.ECHO /*7*/:
                stringBuilder.append("\"").append(zznb.zzcU(zza.zzp(parcel, i))).append("\"");
            case Protocol.EGP /*8*/:
                stringBuilder.append("\"").append(zzmo.zzj(zza.zzs(parcel, i))).append("\"");
            case Service.DISCARD /*9*/:
                stringBuilder.append("\"").append(zzmo.zzk(zza.zzs(parcel, i)));
                stringBuilder.append("\"");
            case Protocol.BBN_RCC_MON /*10*/:
                Bundle zzr = zza.zzr(parcel, i);
                Set<String> keySet = zzr.keySet();
                keySet.size();
                stringBuilder.append("{");
                int i3 = 1;
                for (String str : keySet) {
                    if (i3 == 0) {
                        stringBuilder.append(Stomp.COMMA);
                    }
                    stringBuilder.append("\"").append(str).append("\"");
                    stringBuilder.append(Headers.SEPERATOR);
                    stringBuilder.append("\"").append(zznb.zzcU(zzr.getString(str))).append("\"");
                    i3 = 0;
                }
                stringBuilder.append("}");
            case Service.USERS /*11*/:
                Parcel zzE = zza.zzE(parcel, i);
                zzE.setDataPosition(0);
                zza(stringBuilder, field.zzrx(), zzE);
            default:
                throw new IllegalStateException("Unknown field type out");
        }
    }

    private void zzb(StringBuilder stringBuilder, Field<?, ?> field, Object obj) {
        if (field.zzrp()) {
            zzb(stringBuilder, (Field) field, (ArrayList) obj);
        } else {
            zza(stringBuilder, field.zzrj(), obj);
        }
    }

    private void zzb(StringBuilder stringBuilder, Field<?, ?> field, ArrayList<?> arrayList) {
        stringBuilder.append("[");
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                stringBuilder.append(Stomp.COMMA);
            }
            zza(stringBuilder, field.zzrj(), arrayList.get(i));
        }
        stringBuilder.append("]");
    }

    public static HashMap<String, String> zzl(Bundle bundle) {
        HashMap<String, String> hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            hashMap.put(str, bundle.getString(str));
        }
        return hashMap;
    }

    public int describeContents() {
        zze com_google_android_gms_common_server_response_zze = CREATOR;
        return 0;
    }

    public int getVersionCode() {
        return this.mVersionCode;
    }

    public String toString() {
        zzx.zzb(this.zzamT, (Object) "Cannot convert to JSON on client side.");
        Parcel zzrD = zzrD();
        zzrD.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        zza(stringBuilder, this.zzamT.zzcR(this.mClassName), zzrD);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zze com_google_android_gms_common_server_response_zze = CREATOR;
        zze.zza(this, out, flags);
    }

    protected Object zzcN(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    protected boolean zzcO(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public Parcel zzrD() {
        switch (this.zzanc) {
            case Tokenizer.EOF /*0*/:
                this.zzand = zzb.zzav(this.zzana);
                zzb.zzI(this.zzana, this.zzand);
                this.zzanc = 2;
                break;
            case Zone.PRIMARY /*1*/:
                zzb.zzI(this.zzana, this.zzand);
                this.zzanc = 2;
                break;
        }
        return this.zzana;
    }

    FieldMappingDictionary zzrE() {
        switch (this.zzanb) {
            case Tokenizer.EOF /*0*/:
                return null;
            case Zone.PRIMARY /*1*/:
                return this.zzamT;
            case Zone.SECONDARY /*2*/:
                return this.zzamT;
            default:
                throw new IllegalStateException("Invalid creation type: " + this.zzanb);
        }
    }

    public Map<String, Field<?, ?>> zzrl() {
        return this.zzamT == null ? null : this.zzamT.zzcR(this.mClassName);
    }
}
