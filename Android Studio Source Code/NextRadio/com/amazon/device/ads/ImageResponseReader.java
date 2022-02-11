package com.amazon.device.ads;

import android.graphics.Bitmap;
import java.io.InputStream;
import org.json.JSONObject;

public class ImageResponseReader extends ResponseReader {
    final GraphicsUtils graphicsUtils;

    public /* bridge */ /* synthetic */ void enableLog(boolean x0) {
        super.enableLog(x0);
    }

    public /* bridge */ /* synthetic */ InputStream getInputStream() {
        return super.getInputStream();
    }

    public /* bridge */ /* synthetic */ JSONObject readAsJSON() {
        return super.readAsJSON();
    }

    public /* bridge */ /* synthetic */ String readAsString() {
        return super.readAsString();
    }

    public /* bridge */ /* synthetic */ void setExternalLogTag(String x0) {
        super.setExternalLogTag(x0);
    }

    ImageResponseReader(ResponseReader reader, GraphicsUtils graphicsUtils) {
        super(reader.getInputStream());
        this.graphicsUtils = graphicsUtils;
    }

    public Bitmap readAsBitmap() {
        return this.graphicsUtils.createBitmapImage(getInputStream());
    }
}
