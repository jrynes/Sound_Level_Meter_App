package com.facebook.ads.internal.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View.MeasureSpec;
import android.widget.TextView;

/* renamed from: com.facebook.ads.internal.view.j */
public class C0563j extends TextView {
    private int f2009a;
    private float f2010b;
    private float f2011c;

    public C0563j(Context context, int i) {
        super(context);
        this.f2011c = 8.0f;
        setMaxLines(i);
        setEllipsize(TruncateAt.END);
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.setMaxLines(this.f2009a + 1);
        super.setTextSize(this.f2010b);
        int i5 = i3 - i;
        int i6 = i4 - i2;
        measure(MeasureSpec.makeMeasureSpec(i5, 1073741824), MeasureSpec.makeMeasureSpec(i6, 0));
        if (getMeasuredHeight() > i6) {
            float f = this.f2010b;
            while (f > this.f2011c) {
                f -= 0.5f;
                super.setTextSize(f);
                measure(MeasureSpec.makeMeasureSpec(i5, 1073741824), 0);
                if (getMeasuredHeight() <= i6 && getLineCount() <= this.f2009a) {
                    break;
                }
            }
        }
        super.setMaxLines(this.f2009a);
        setMeasuredDimension(i5, i6);
        super.onLayout(z, i, i2, i3, i4);
    }

    public void setMaxLines(int i) {
        this.f2009a = i;
        super.setMaxLines(i);
    }

    public void setMinTextSize(float f) {
        this.f2011c = f;
    }

    public void setTextSize(float f) {
        this.f2010b = f;
        super.setTextSize(f);
    }
}
