package com.nextradioapp.nextradio.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.nextradio.fragments.PrefFragment;

public class PrefActivity extends PreferenceActivity {

    /* renamed from: com.nextradioapp.nextradio.activities.PrefActivity.1 */
    class C11771 extends Thread {
        C11771() {
        }

        public void run() {
            ((NextRadioApplication) PrefActivity.this.getApplicationContext()).initSDK();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSDKInitInBackground();
        getFragmentManager().beginTransaction().replace(16908290, new PrefFragment()).commit();
    }

    protected void doSDKInitInBackground() {
        new C11771().start();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
