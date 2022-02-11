package com.amazon.device.ads;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageButton;

/* compiled from: ImageViewFactory */
class ImageButtonFactory implements ImageViewFactory {
    ImageButtonFactory() {
    }

    public BitmapDrawable createBitmapDrawable(Resources resources, String filePath) {
        return AndroidTargetUtils.getNewBitmapDrawable(resources, filePath);
    }

    public ImageButton createImageView(Context context, String contentDescription) {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setContentDescription(contentDescription);
        return imageButton;
    }
}
