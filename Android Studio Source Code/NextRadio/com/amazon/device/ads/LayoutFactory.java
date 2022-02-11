package com.amazon.device.ads;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.xbill.DNS.Zone;

class LayoutFactory {

    /* renamed from: com.amazon.device.ads.LayoutFactory.1 */
    static /* synthetic */ class C03161 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$LayoutFactory$LayoutType;

        static {
            $SwitchMap$com$amazon$device$ads$LayoutFactory$LayoutType = new int[LayoutType.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$LayoutFactory$LayoutType[LayoutType.RELATIVE_LAYOUT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$LayoutFactory$LayoutType[LayoutType.FRAME_LAYOUT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$LayoutFactory$LayoutType[LayoutType.LINEAR_LAYOUT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    enum LayoutType {
        RELATIVE_LAYOUT,
        LINEAR_LAYOUT,
        FRAME_LAYOUT
    }

    LayoutFactory() {
    }

    public ViewGroup createLayout(Context context, LayoutType layoutType, String contentDescription) {
        ViewGroup layout;
        switch (C03161.$SwitchMap$com$amazon$device$ads$LayoutFactory$LayoutType[layoutType.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                layout = new RelativeLayout(context);
                break;
            case Zone.SECONDARY /*2*/:
                layout = new FrameLayout(context);
                break;
            default:
                layout = new LinearLayout(context);
                break;
        }
        layout.setContentDescription(contentDescription);
        return layout;
    }
}
