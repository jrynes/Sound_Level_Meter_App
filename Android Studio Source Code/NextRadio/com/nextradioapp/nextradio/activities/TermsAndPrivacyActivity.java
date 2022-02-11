package com.nextradioapp.nextradio.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.nextradioapp.androidSDK.ext.PreferenceStorage;
import com.nextradioapp.nextradio.NextRadioApplication;
import com.nextradioapp.utils.CountryEULAManager;

public class TermsAndPrivacyActivity extends Activity {
    private static final String MEXICO_COUNTRY_CODE = "mx";
    Button buttonAccept;
    TextView textAgreeTo;
    TextView textSuffix;
    TextView textUsage;

    /* renamed from: com.nextradioapp.nextradio.activities.TermsAndPrivacyActivity.1 */
    class C12231 implements OnClickListener {
        C12231() {
        }

        public void onClick(View arg0) {
            PreferenceManager.getDefaultSharedPreferences(TermsAndPrivacyActivity.this).edit().putBoolean(PreferenceStorage.EULA_AGREEMENT, true).commit();
            Intent intent = new Intent(TermsAndPrivacyActivity.this, SplashScreen.class);
            intent.putExtra("fromTerms", true);
            TermsAndPrivacyActivity.this.startActivity(intent);
            TermsAndPrivacyActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903119);
        if (NextRadioApplication.isTablet) {
            setRequestedOrientation(0);
        }
        this.textUsage = (TextView) findViewById(2131689804);
        this.textUsage.setText(Html.fromHtml(generateUsageAndPrivacyText(getString(2131165424))));
        this.textUsage.setMovementMethod(LinkMovementMethod.getInstance());
        this.textUsage.setClickable(true);
        this.textSuffix = (TextView) findViewById(2131689805);
        if (!this.textSuffix.getText().toString().equals("BLANK")) {
            this.textSuffix.setVisibility(0);
        }
        this.textAgreeTo = (TextView) findViewById(2131689803);
        if (!this.textAgreeTo.getText().toString().equals("BLANK")) {
            this.textAgreeTo.setVisibility(0);
        }
        this.buttonAccept = (Button) findViewById(2131689806);
        this.buttonAccept.setOnClickListener(new C12231());
    }

    private String generateUsageAndPrivacyText(String usageString) {
        String[] linkStrings = getUsageandPrivacyLinks();
        String usageLink = linkStrings[0];
        return usageString.replace("^1", "<a href='").replace("^2", "'><font color=#33b5e5>").replace("^3", usageLink).replace("^5", linkStrings[1]).replace("^4", "</font></a>");
    }

    private String[] getUsageandPrivacyLinks() {
        CountryEULAManager eulaManager = new CountryEULAManager(this);
        return new String[]{eulaManager.getEULA(), eulaManager.getPrivacy()};
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623940, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 2131689837) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
