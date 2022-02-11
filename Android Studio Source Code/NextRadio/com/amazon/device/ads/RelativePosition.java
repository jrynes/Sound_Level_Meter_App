package com.amazon.device.ads;

import java.util.HashMap;

enum RelativePosition {
    TOP_LEFT,
    TOP_RIGHT,
    CENTER,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_CENTER,
    BOTTOM_CENTER;
    
    private static final HashMap<String, RelativePosition> POSITIONS;

    static {
        POSITIONS = new HashMap();
        POSITIONS.put("top-left", TOP_LEFT);
        POSITIONS.put("top-right", TOP_RIGHT);
        POSITIONS.put("top-center", TOP_CENTER);
        POSITIONS.put("bottom-left", BOTTOM_LEFT);
        POSITIONS.put("bottom-right", BOTTOM_RIGHT);
        POSITIONS.put("bottom-center", BOTTOM_CENTER);
        POSITIONS.put("center", CENTER);
    }

    public static RelativePosition fromString(String value) {
        return (RelativePosition) POSITIONS.get(value);
    }
}
