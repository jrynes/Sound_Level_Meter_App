package com.nostra13.universalimageloader.core.download;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.Zone;

public class NetworkDeniedImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    /* renamed from: com.nostra13.universalimageloader.core.download.NetworkDeniedImageDownloader.1 */
    static /* synthetic */ class C12681 {
        static final /* synthetic */ int[] f2285x4730d1a3;

        static {
            f2285x4730d1a3 = new int[Scheme.values().length];
            try {
                f2285x4730d1a3[Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f2285x4730d1a3[Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public NetworkDeniedImageDownloader(ImageDownloader wrappedDownloader) {
        this.wrappedDownloader = wrappedDownloader;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        switch (C12681.f2285x4730d1a3[Scheme.ofUri(imageUri).ordinal()]) {
            case Zone.PRIMARY /*1*/:
            case Zone.SECONDARY /*2*/:
                throw new IllegalStateException();
            default:
                return this.wrappedDownloader.getStream(imageUri, extra);
        }
    }
}
