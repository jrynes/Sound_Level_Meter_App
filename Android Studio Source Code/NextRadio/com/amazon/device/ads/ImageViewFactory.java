package com.amazon.device.ads;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

interface ImageViewFactory {
    BitmapDrawable createBitmapDrawable(Resources resources, String str);

    ImageView createImageView(Context context, String str);
}
