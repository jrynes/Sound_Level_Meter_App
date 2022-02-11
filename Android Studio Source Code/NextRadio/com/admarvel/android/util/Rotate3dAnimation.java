package com.admarvel.android.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* renamed from: com.admarvel.android.util.o */
public class Rotate3dAnimation extends Animation {
    private final float f1056a;
    private final float f1057b;
    private final float f1058c;
    private final float f1059d;
    private final float f1060e;
    private final boolean f1061f;
    private Camera f1062g;

    public Rotate3dAnimation(float f, float f2, float f3, float f4, float f5, boolean z) {
        this.f1056a = f;
        this.f1057b = f2;
        this.f1058c = f3;
        this.f1059d = f4;
        this.f1060e = f5;
        this.f1061f = z;
    }

    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float f = this.f1056a;
        f += (this.f1057b - f) * interpolatedTime;
        float f2 = this.f1058c;
        float f3 = this.f1059d;
        Camera camera = this.f1062g;
        Matrix matrix = t.getMatrix();
        camera.save();
        if (this.f1061f) {
            camera.translate(0.0f, 0.0f, this.f1060e * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, this.f1060e * (1.0f - interpolatedTime));
        }
        camera.rotateY(f);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-f2, -f3);
        matrix.postTranslate(f2, f3);
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.f1062g = new Camera();
    }
}
