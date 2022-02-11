package com.amazon.device.ads;

import android.content.Context;
import android.view.WindowManager;

class DisplayUtils {
    private static int[][] rotationArray;

    DisplayUtils() {
    }

    static {
        rotationArray = new int[][]{new int[]{1, 0, 9, 8}, new int[]{0, 9, 8, 1}};
    }

    public static int determineCanonicalScreenOrientation(Context context) {
        boolean naturalOrientationIsPortrait;
        int rotationSelector;
        int rotation = AndroidTargetUtils.getOrientation(((WindowManager) context.getSystemService("window")).getDefaultDisplay());
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == 1) {
            if (rotation == 0 || rotation == 2) {
                naturalOrientationIsPortrait = true;
            } else {
                naturalOrientationIsPortrait = false;
            }
        } else if (orientation == 2) {
            naturalOrientationIsPortrait = rotation == 1 || rotation == 3;
        } else {
            naturalOrientationIsPortrait = true;
        }
        if (naturalOrientationIsPortrait) {
            rotationSelector = 0;
        } else {
            rotationSelector = 1;
        }
        return rotationArray[rotationSelector][rotation];
    }
}
