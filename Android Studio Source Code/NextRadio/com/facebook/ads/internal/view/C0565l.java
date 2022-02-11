package com.facebook.ads.internal.view;

import android.content.Context;
import android.widget.RelativeLayout;

/* renamed from: com.facebook.ads.internal.view.l */
public class C0565l extends RelativeLayout {
    private int f2014a;
    private int f2015b;

    public C0565l(Context context) {
        super(context);
        this.f2014a = 0;
        this.f2015b = 0;
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.f2015b > 0 && getMeasuredWidth() > this.f2015b) {
            setMeasuredDimension(this.f2015b, getMeasuredHeight());
        } else if (getMeasuredWidth() < this.f2014a) {
            setMeasuredDimension(this.f2014a, getMeasuredHeight());
        }
    }

    protected void setMaxWidth(int i) {
        this.f2015b = i;
    }

    public void setMinWidth(int i) {
        this.f2014a = i;
    }
}
