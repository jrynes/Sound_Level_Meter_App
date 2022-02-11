package com.facebook.ads.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/* renamed from: com.facebook.ads.internal.g */
public class C0474g extends View {
    private Paint f1772a;
    private Paint f1773b;
    private Paint f1774c;
    private int f1775d;
    private boolean f1776e;

    public C0474g(Context context) {
        this(context, 60, true);
    }

    public C0474g(Context context, int i, boolean z) {
        super(context);
        this.f1775d = i;
        this.f1776e = z;
        if (z) {
            this.f1772a = new Paint();
            this.f1772a.setColor(-3355444);
            this.f1772a.setStyle(Style.STROKE);
            this.f1772a.setStrokeWidth(PDF417.PREFERRED_RATIO);
            this.f1772a.setAntiAlias(true);
            this.f1773b = new Paint();
            this.f1773b.setColor(-1287371708);
            this.f1773b.setStyle(Style.FILL);
            this.f1773b.setAntiAlias(true);
            this.f1774c = new Paint();
            this.f1774c.setColor(-1);
            this.f1774c.setStyle(Style.STROKE);
            this.f1774c.setStrokeWidth(6.0f);
            this.f1774c.setAntiAlias(true);
        }
        m1395a();
    }

    private void m1395a() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (((float) this.f1775d) * displayMetrics.density), (int) (displayMetrics.density * ((float) this.f1775d)));
        layoutParams.addRule(10);
        layoutParams.addRule(11);
        setLayoutParams(layoutParams);
    }

    protected void onDraw(Canvas canvas) {
        if (this.f1776e) {
            if (canvas.isHardwareAccelerated() && VERSION.SDK_INT < 17) {
                setLayerType(1, null);
            }
            int min = Math.min(canvas.getWidth(), canvas.getHeight());
            int i = min / 2;
            int i2 = min / 2;
            int i3 = (i * 2) / 3;
            canvas.drawCircle((float) i, (float) i2, (float) i3, this.f1772a);
            canvas.drawCircle((float) i, (float) i2, (float) (i3 - 2), this.f1773b);
            int i4 = min / 3;
            int i5 = min / 3;
            canvas.drawLine((float) i4, (float) i5, (float) (i4 * 2), (float) (i5 * 2), this.f1774c);
            canvas.drawLine((float) (i4 * 2), (float) i5, (float) i4, (float) (i5 * 2), this.f1774c);
        }
        super.onDraw(canvas);
    }
}
