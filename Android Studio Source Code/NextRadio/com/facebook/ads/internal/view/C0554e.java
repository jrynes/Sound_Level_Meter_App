package com.facebook.ads.internal.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.gms.maps.model.GroundOverlayOptions;

/* renamed from: com.facebook.ads.internal.view.e */
public class C0554e extends LinearLayout {
    private Bitmap f1983a;
    private Bitmap f1984b;
    private ImageView f1985c;
    private ImageView f1986d;
    private ImageView f1987e;
    private Bitmap f1988f;
    private int f1989g;
    private int f1990h;

    public C0554e(Context context) {
        super(context);
        m1598b();
    }

    private void m1597a() {
        if (getHeight() > 0) {
            this.f1990h = m1599c();
            this.f1989g = (int) Math.ceil((double) (((float) (getHeight() - this.f1990h)) / 2.0f));
            Matrix matrix = new Matrix();
            matrix.preScale(1.0f, GroundOverlayOptions.NO_DIMENSION);
            int floor = (int) Math.floor((double) (((float) (getHeight() - this.f1990h)) / 2.0f));
            float height = ((float) this.f1983a.getHeight()) / ((float) this.f1990h);
            int round = Math.round(((float) this.f1989g) * height);
            if (round > 0) {
                this.f1988f = Bitmap.createBitmap(this.f1984b, 0, 0, this.f1984b.getWidth(), round, matrix, true);
                this.f1985c.setImageBitmap(this.f1988f);
            }
            round = Math.round(((float) floor) * height);
            if (round > 0) {
                this.f1987e.setImageBitmap(Bitmap.createBitmap(this.f1984b, 0, this.f1984b.getHeight() - round, this.f1984b.getWidth(), round, matrix, true));
            }
        }
    }

    private void m1598b() {
        getContext().getResources().getDisplayMetrics();
        setOrientation(1);
        this.f1985c = new ImageView(getContext());
        this.f1985c.setScaleType(ScaleType.FIT_XY);
        addView(this.f1985c);
        this.f1986d = new ImageView(getContext());
        this.f1986d.setLayoutParams(new LayoutParams(-1, -1));
        this.f1986d.setScaleType(ScaleType.FIT_CENTER);
        addView(this.f1986d);
        this.f1987e = new ImageView(getContext());
        this.f1987e.setScaleType(ScaleType.FIT_XY);
        addView(this.f1987e);
    }

    private int m1599c() {
        return (int) Math.round(((double) getWidth()) / 1.91d);
    }

    public void m1600a(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            this.f1986d.setImageDrawable(null);
            return;
        }
        this.f1986d.setImageBitmap(Bitmap.createBitmap(bitmap));
        this.f1983a = bitmap;
        this.f1984b = bitmap2;
        float height = ((float) bitmap.getHeight()) / ((float) bitmap.getWidth());
        m1597a();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.f1983a == null || this.f1984b == null) {
            super.onLayout(z, i, i2, i3, i4);
            return;
        }
        int c = m1599c();
        if (this.f1988f == null || this.f1990h != c) {
            m1597a();
        }
        this.f1985c.layout(i, i2, i3, this.f1989g);
        this.f1986d.layout(i, this.f1989g + i2, i3, this.f1989g + this.f1990h);
        this.f1987e.layout(i, (this.f1989g + i2) + this.f1990h, i3, i4);
    }
}
