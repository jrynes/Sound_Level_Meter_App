package com.nostra13.universalimageloader.core.download;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.Zone;

public class SlowNetworkImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    /* renamed from: com.nostra13.universalimageloader.core.download.SlowNetworkImageDownloader.1 */
    static /* synthetic */ class C12691 {
        static final /* synthetic */ int[] f2286x4730d1a3;

        static {
            f2286x4730d1a3 = new int[Scheme.values().length];
            try {
                f2286x4730d1a3[Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f2286x4730d1a3[Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public SlowNetworkImageDownloader(ImageDownloader wrappedDownloader) {
        this.wrappedDownloader = wrappedDownloader;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        InputStream imageStream = this.wrappedDownloader.getStream(imageUri, extra);
        switch (C12691.f2286x4730d1a3[Scheme.ofUri(imageUri).ordinal()]) {
            case Zone.PRIMARY /*1*/:
            case Zone.SECONDARY /*2*/:
                return new FlushedInputStream(imageStream);
            default:
                return imageStream;
        }
    }
}
