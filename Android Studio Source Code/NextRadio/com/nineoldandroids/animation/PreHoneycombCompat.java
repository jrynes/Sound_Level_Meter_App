package com.nineoldandroids.animation;

import android.view.View;
import com.nineoldandroids.util.FloatProperty;
import com.nineoldandroids.util.IntProperty;
import com.nineoldandroids.util.Property;
import com.nineoldandroids.view.animation.AnimatorProxy;

final class PreHoneycombCompat {
    static Property<View, Float> ALPHA;
    static Property<View, Float> PIVOT_X;
    static Property<View, Float> PIVOT_Y;
    static Property<View, Float> ROTATION;
    static Property<View, Float> ROTATION_X;
    static Property<View, Float> ROTATION_Y;
    static Property<View, Float> SCALE_X;
    static Property<View, Float> SCALE_Y;
    static Property<View, Integer> SCROLL_X;
    static Property<View, Integer> SCROLL_Y;
    static Property<View, Float> TRANSLATION_X;
    static Property<View, Float> TRANSLATION_Y;
    static Property<View, Float> f2278X;
    static Property<View, Float> f2279Y;

    static class 10 extends FloatProperty<View> {
        10(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setScaleY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getScaleY());
        }
    }

    static class 11 extends IntProperty<View> {
        11(String x0) {
            super(x0);
        }

        public void setValue(View object, int value) {
            AnimatorProxy.wrap(object).setScrollX(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(AnimatorProxy.wrap(object).getScrollX());
        }
    }

    static class 12 extends IntProperty<View> {
        12(String x0) {
            super(x0);
        }

        public void setValue(View object, int value) {
            AnimatorProxy.wrap(object).setScrollY(value);
        }

        public Integer get(View object) {
            return Integer.valueOf(AnimatorProxy.wrap(object).getScrollY());
        }
    }

    static class 13 extends FloatProperty<View> {
        13(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getX());
        }
    }

    static class 14 extends FloatProperty<View> {
        14(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getY());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.1 */
    static class C12391 extends FloatProperty<View> {
        C12391(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setAlpha(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getAlpha());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.2 */
    static class C12402 extends FloatProperty<View> {
        C12402(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setPivotX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getPivotX());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.3 */
    static class C12413 extends FloatProperty<View> {
        C12413(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setPivotY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getPivotY());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.4 */
    static class C12424 extends FloatProperty<View> {
        C12424(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setTranslationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getTranslationX());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.5 */
    static class C12435 extends FloatProperty<View> {
        C12435(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setTranslationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getTranslationY());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.6 */
    static class C12446 extends FloatProperty<View> {
        C12446(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setRotation(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getRotation());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.7 */
    static class C12457 extends FloatProperty<View> {
        C12457(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setRotationX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getRotationX());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.8 */
    static class C12468 extends FloatProperty<View> {
        C12468(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setRotationY(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getRotationY());
        }
    }

    /* renamed from: com.nineoldandroids.animation.PreHoneycombCompat.9 */
    static class C12479 extends FloatProperty<View> {
        C12479(String x0) {
            super(x0);
        }

        public void setValue(View object, float value) {
            AnimatorProxy.wrap(object).setScaleX(value);
        }

        public Float get(View object) {
            return Float.valueOf(AnimatorProxy.wrap(object).getScaleX());
        }
    }

    static {
        ALPHA = new C12391("alpha");
        PIVOT_X = new C12402("pivotX");
        PIVOT_Y = new C12413("pivotY");
        TRANSLATION_X = new C12424("translationX");
        TRANSLATION_Y = new C12435("translationY");
        ROTATION = new C12446("rotation");
        ROTATION_X = new C12457("rotationX");
        ROTATION_Y = new C12468("rotationY");
        SCALE_X = new C12479("scaleX");
        SCALE_Y = new 10("scaleY");
        SCROLL_X = new 11("scrollX");
        SCROLL_Y = new 12("scrollY");
        f2278X = new 13("x");
        f2279Y = new 14("y");
    }

    private PreHoneycombCompat() {
    }
}
