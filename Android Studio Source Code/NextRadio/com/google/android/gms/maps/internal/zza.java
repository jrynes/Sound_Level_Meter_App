package com.google.android.gms.maps.internal;

import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Zone;

public final class zza {
    public static Boolean zza(byte b) {
        switch (b) {
            case Tokenizer.EOF /*0*/:
                return Boolean.FALSE;
            case Zone.PRIMARY /*1*/:
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    public static byte zze(Boolean bool) {
        return bool != null ? bool.booleanValue() ? (byte) 1 : (byte) 0 : (byte) -1;
    }
}
