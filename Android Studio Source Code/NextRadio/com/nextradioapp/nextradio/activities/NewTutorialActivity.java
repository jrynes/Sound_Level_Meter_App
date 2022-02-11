package com.nextradioapp.nextradio.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.adapters.TutorialSlidePagerAdapter;
import com.nextradioapp.nextradio.views.CrossFadePageTransformer;

public class NewTutorialActivity extends FragmentActivity {
    LinearLayout circles;
    Button done;
    boolean isOpaque;
    ImageButton next;
    ViewPager pager;
    PagerAdapter pagerAdapter;
    Button skip;
    private RelativeLayout tutorial_bottom_background_layout;

    /* renamed from: com.nextradioapp.nextradio.activities.NewTutorialActivity.1 */
    class C11731 implements OnClickListener {
        C11731() {
        }

        public void onClick(View v) {
            NewTutorialActivity.this.endTutorial();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.NewTutorialActivity.2 */
    class C11742 implements OnClickListener {
        C11742() {
        }

        public void onClick(View v) {
            NewTutorialActivity.this.pager.setCurrentItem(NewTutorialActivity.this.pager.getCurrentItem() + 1, true);
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.NewTutorialActivity.3 */
    class C11753 implements OnClickListener {
        C11753() {
        }

        public void onClick(View v) {
            NewTutorialActivity.this.endTutorial();
        }
    }

    /* renamed from: com.nextradioapp.nextradio.activities.NewTutorialActivity.4 */
    class C11764 implements OnPageChangeListener {
        C11764() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position != SplashScreen.NUM_PAGES - 2 || positionOffset <= 0.0f) {
                if (!NewTutorialActivity.this.isOpaque) {
                    NewTutorialActivity.this.pager.setBackgroundColor(NewTutorialActivity.this.getResources().getColor(2131558447));
                    NewTutorialActivity.this.isOpaque = true;
                }
            } else if (NewTutorialActivity.this.isOpaque) {
                NewTutorialActivity.this.pager.setBackgroundColor(0);
                NewTutorialActivity.this.isOpaque = false;
            }
        }

        public void onPageSelected(int position) {
            NewTutorialActivity.this.setIndicator(position);
            NewTutorialActivity.this.changeBottomPageColor(position);
            if (position == SplashScreen.NUM_PAGES - 2) {
                NewTutorialActivity.this.skip.setVisibility(8);
                NewTutorialActivity.this.next.setVisibility(8);
                NewTutorialActivity.this.done.setVisibility(0);
            } else if (position < SplashScreen.NUM_PAGES - 2) {
                NewTutorialActivity.this.skip.setVisibility(0);
                NewTutorialActivity.this.next.setVisibility(0);
                NewTutorialActivity.this.done.setVisibility(8);
            } else if (position == SplashScreen.NUM_PAGES - 1) {
                NewTutorialActivity.this.endTutorial();
            }
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public NewTutorialActivity() {
        this.isOpaque = true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(67108864, 67108864);
        if (NextRadioApplication.isTablet) {
            setRequestedOrientation(0);
        }
        setContentView(2130903045);
        this.tutorial_bottom_background_layout = (RelativeLayout) findViewById(2131689526);
        this.tutorial_bottom_background_layout.setBackgroundColor(getResources().getColor(2131558401));
        this.skip = (Button) Button.class.cast(findViewById(2131689527));
        this.skip.setOnClickListener(new C11731());
        this.next = (ImageButton) ImageButton.class.cast(findViewById(2131689530));
        this.next.setOnClickListener(new C11742());
        this.done = (Button) Button.class.cast(findViewById(2131689529));
        this.done.setOnClickListener(new C11753());
        this.pager = (ViewPager) findViewById(2131689522);
        this.pagerAdapter = new TutorialSlidePagerAdapter(getSupportFragmentManager());
        this.pager.setAdapter(this.pagerAdapter);
        this.pager.setPageTransformer(true, new CrossFadePageTransformer());
        this.pager.addOnPageChangeListener(new C11764());
        buildCircles();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.pager != null) {
            this.pager.clearOnPageChangeListeners();
        }
    }

    private void buildCircles() {
        this.circles = (LinearLayout) LinearLayout.class.cast(findViewById(2131689528));
        int padding = (int) ((5.0f * getResources().getDisplayMetrics().density) + 0.5f);
        for (int i = 0; i < SplashScreen.NUM_PAGES - 1; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(2130837671);
            circle.setLayoutParams(new LayoutParams(-2, -2));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            this.circles.addView(circle);
        }
        setIndicator(0);
    }

    private void setIndicator(int index) {
        if (index < SplashScreen.NUM_PAGES) {
            for (int i = 0; i < SplashScreen.NUM_PAGES - 1; i++) {
                ImageView circle = (ImageView) this.circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(2131558438));
                } else {
                    circle.setColorFilter(getResources().getColor(17170445));
                }
            }
        }
    }

    private void changeBottomPageColor(int index) {
        int colorCode = 2131558405;
        if (index == 0) {
            colorCode = 2131558401;
        } else if (index == 1) {
            colorCode = 2131558402;
        } else if (index == 2) {
            colorCode = 2131558403;
        } else if (index == 3) {
            colorCode = 2131558405;
        } else if (index == 4) {
            colorCode = 2131558405;
        }
        this.tutorial_bottom_background_layout.setBackgroundColor(getResources().getColor(colorCode));
    }

    private void endTutorial() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("didTutorial", true).apply();
        finish();
        overridePendingTransition(17432576, 17432577);
    }

    public void onBackPressed() {
        if (this.pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);
        }
    }
}
