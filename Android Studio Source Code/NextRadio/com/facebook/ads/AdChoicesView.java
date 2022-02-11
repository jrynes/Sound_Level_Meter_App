package com.facebook.ads;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.ads.NativeAd.Image;

public class AdChoicesView extends RelativeLayout {
    private final Context f1405a;
    private final NativeAd f1406b;
    private final DisplayMetrics f1407c;
    private boolean f1408d;
    private TextView f1409e;

    /* renamed from: com.facebook.ads.AdChoicesView.1 */
    class C03971 implements OnTouchListener {
        final /* synthetic */ AdChoicesView f1396a;

        C03971(AdChoicesView adChoicesView) {
            this.f1396a = adChoicesView;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            if (this.f1396a.f1408d) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setData(Uri.parse(this.f1396a.f1406b.getAdChoicesLinkUrl()));
                this.f1396a.f1405a.startActivity(intent);
            } else {
                this.f1396a.m1037a();
            }
            return true;
        }
    }

    /* renamed from: com.facebook.ads.AdChoicesView.2 */
    class C03982 extends Animation {
        final /* synthetic */ int f1397a;
        final /* synthetic */ int f1398b;
        final /* synthetic */ AdChoicesView f1399c;

        C03982(AdChoicesView adChoicesView, int i, int i2) {
            this.f1399c = adChoicesView;
            this.f1397a = i;
            this.f1398b = i2;
        }

        protected void applyTransformation(float f, Transformation transformation) {
            int i = (int) (((float) this.f1397a) + (((float) (this.f1398b - this.f1397a)) * f));
            this.f1399c.getLayoutParams().width = i;
            this.f1399c.requestLayout();
            this.f1399c.f1409e.getLayoutParams().width = i - this.f1397a;
            this.f1399c.f1409e.requestLayout();
        }

        public boolean willChangeBounds() {
            return true;
        }
    }

    /* renamed from: com.facebook.ads.AdChoicesView.3 */
    class C04013 implements AnimationListener {
        final /* synthetic */ int f1402a;
        final /* synthetic */ int f1403b;
        final /* synthetic */ AdChoicesView f1404c;

        /* renamed from: com.facebook.ads.AdChoicesView.3.1 */
        class C04001 implements Runnable {
            final /* synthetic */ C04013 f1401a;

            /* renamed from: com.facebook.ads.AdChoicesView.3.1.1 */
            class C03991 extends Animation {
                final /* synthetic */ C04001 f1400a;

                C03991(C04001 c04001) {
                    this.f1400a = c04001;
                }

                protected void applyTransformation(float f, Transformation transformation) {
                    int i = (int) (((float) this.f1400a.f1401a.f1402a) + (((float) (this.f1400a.f1401a.f1403b - this.f1400a.f1401a.f1402a)) * f));
                    this.f1400a.f1401a.f1404c.getLayoutParams().width = i;
                    this.f1400a.f1401a.f1404c.requestLayout();
                    this.f1400a.f1401a.f1404c.f1409e.getLayoutParams().width = i - this.f1400a.f1401a.f1403b;
                    this.f1400a.f1401a.f1404c.f1409e.requestLayout();
                }

                public boolean willChangeBounds() {
                    return true;
                }
            }

            C04001(C04013 c04013) {
                this.f1401a = c04013;
            }

            public void run() {
                if (this.f1401a.f1404c.f1408d) {
                    this.f1401a.f1404c.f1408d = false;
                    Animation c03991 = new C03991(this);
                    c03991.setDuration(300);
                    c03991.setFillAfter(true);
                    this.f1401a.f1404c.startAnimation(c03991);
                }
            }
        }

        C04013(AdChoicesView adChoicesView, int i, int i2) {
            this.f1404c = adChoicesView;
            this.f1402a = i;
            this.f1403b = i2;
        }

        public void onAnimationEnd(Animation animation) {
            new Handler().postDelayed(new C04001(this), 3000);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    public AdChoicesView(Context context, NativeAd nativeAd) {
        this(context, nativeAd, false);
    }

    public AdChoicesView(Context context, NativeAd nativeAd, boolean z) {
        super(context);
        this.f1408d = false;
        this.f1405a = context;
        this.f1406b = nativeAd;
        this.f1407c = this.f1405a.getResources().getDisplayMetrics();
        Image adChoicesIcon = this.f1406b.getAdChoicesIcon();
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        setOnTouchListener(new C03971(this));
        this.f1409e = new TextView(this.f1405a);
        addView(this.f1409e);
        LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        if (z) {
            layoutParams2.addRule(11, m1036a(adChoicesIcon).getId());
            layoutParams2.width = 0;
            layoutParams.width = Math.round(((float) (adChoicesIcon.getWidth() + 4)) * this.f1407c.density);
            layoutParams.height = Math.round(((float) (adChoicesIcon.getHeight() + 2)) * this.f1407c.density);
            this.f1408d = false;
        } else {
            this.f1408d = true;
        }
        setLayoutParams(layoutParams);
        layoutParams2.addRule(15, -1);
        this.f1409e.setLayoutParams(layoutParams2);
        this.f1409e.setSingleLine();
        this.f1409e.setText("AdChoices");
        this.f1409e.setTextSize(10.0f);
        this.f1409e.setTextColor(-4341303);
    }

    private ImageView m1036a(Image image) {
        View imageView = new ImageView(this.f1405a);
        addView(imageView);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(Math.round(((float) image.getWidth()) * this.f1407c.density), Math.round(((float) image.getHeight()) * this.f1407c.density));
        layoutParams.addRule(9);
        layoutParams.addRule(15, -1);
        layoutParams.setMargins(Math.round(4.0f * this.f1407c.density), Math.round(this.f1407c.density * 2.0f), Math.round(this.f1407c.density * 2.0f), Math.round(this.f1407c.density * 2.0f));
        imageView.setLayoutParams(layoutParams);
        NativeAd.downloadAndDisplayImage(image, imageView);
        return imageView;
    }

    private void m1037a() {
        Paint paint = new Paint();
        paint.setTextSize(this.f1409e.getTextSize());
        int round = Math.round(paint.measureText("AdChoices") + (4.0f * this.f1407c.density));
        int width = getWidth();
        round += width;
        this.f1408d = true;
        Animation c03982 = new C03982(this, width, round);
        c03982.setAnimationListener(new C04013(this, round, width));
        c03982.setDuration(300);
        c03982.setFillAfter(true);
        startAnimation(c03982);
    }
}
