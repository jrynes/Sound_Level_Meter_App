package com.nextradioapp.nextradio.test;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.test.expectations.Expectation;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.activemq.transport.stomp.Stomp;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Zone;

public class AutomatedTestActivity extends FragmentActivity implements TabListener {
    ArrayList<Expectation> Expectations;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    /* renamed from: com.nextradioapp.nextradio.test.AutomatedTestActivity.1 */
    class C12371 extends SimpleOnPageChangeListener {
        final /* synthetic */ ActionBar val$actionBar;

        C12371(ActionBar actionBar) {
            this.val$actionBar = actionBar;
        }

        public void onPageSelected(int position) {
            this.val$actionBar.setSelectedNavigationItem(position);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (position == 0) {
                return new DeviceInfoFragment();
            }
            if (position == 2) {
                return new ManualControlFragment();
            }
            return new AutomatedTestFragment();
        }

        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case Tokenizer.EOF /*0*/:
                    return AutomatedTestActivity.this.getString(2131165255).toUpperCase(l);
                case Zone.PRIMARY /*1*/:
                    return AutomatedTestActivity.this.getString(2131165256).toUpperCase(l);
                case Zone.SECONDARY /*2*/:
                    return AutomatedTestActivity.this.getString(2131165257).toUpperCase(l);
                default:
                    return null;
            }
        }
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() != 2131689840) {
            return super.onMenuItemSelected(featureId, item);
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(Stomp.TEXT_PLAIN);
        intent.putExtra("android.intent.extra.SUBJECT", "NR-Test:" + Build.MODEL.toLowerCase());
        intent.putExtra("android.intent.extra.TEXT", AutomatedTestLogger.logText);
        startActivity(intent);
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NextRadioApplication.isTablet) {
            setRequestedOrientation(0);
        }
        setContentView(2130903043);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(2);
        new TextView(this).setText("NextRadio FM Tester");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(Stomp.EMPTY);
        actionBar.setCustomView(2130903040);
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager = (ViewPager) findViewById(2131689522);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C12371(actionBar));
        for (int i = 0; i < this.mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(this.mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623939, menu);
        return true;
    }

    public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
        this.mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
