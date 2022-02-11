package com.amazon.device.ads;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import java.lang.reflect.InvocationTargetException;

public class AdActivity extends Activity {
    static final String ADAPTER_KEY = "adapter";
    private static final String LOGTAG;
    private ActivityAdapterFactory activityAdapterFactory;
    private IAdActivityAdapter adapter;
    private AdRegistrationExecutor amazonAdRegistration;
    private MobileAdsLogger logger;

    static class ActivityAdapterFactory {
        private final MobileAdsLogger logger;

        public ActivityAdapterFactory(MobileAdsLoggerFactory loggerFactory) {
            this.logger = loggerFactory.createMobileAdsLogger(AdActivity.LOGTAG);
        }

        IAdActivityAdapter createAdapter(Intent intent) {
            String adapterClassName = intent.getStringExtra(AdActivity.ADAPTER_KEY);
            if (adapterClassName == null) {
                this.logger.m639e("Unable to launch the AdActivity due to an internal error.");
                return null;
            }
            try {
                try {
                    try {
                        return (IAdActivityAdapter) Class.forName(adapterClassName).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    } catch (IllegalArgumentException e) {
                        this.logger.m639e("Illegal arguments given to the default constructor.");
                        return null;
                    } catch (InstantiationException e2) {
                        this.logger.m639e("Instantiation exception when instantiating the adapter.");
                        return null;
                    } catch (IllegalAccessException e3) {
                        this.logger.m639e("Illegal access exception when instantiating the adapter.");
                        return null;
                    } catch (InvocationTargetException e4) {
                        this.logger.m639e("Invocation target exception when instantiating the adapter.");
                        return null;
                    }
                } catch (SecurityException e5) {
                    this.logger.m639e("Security exception when trying to get the default constructor.");
                    return null;
                } catch (NoSuchMethodException e6) {
                    this.logger.m639e("No default constructor exists for the adapter.");
                    return null;
                }
            } catch (ClassNotFoundException e7) {
                this.logger.m639e("Unable to get the adapter class.");
                return null;
            }
        }
    }

    public interface IAdActivityAdapter {
        boolean onBackPressed();

        void onConfigurationChanged(Configuration configuration);

        void onCreate();

        void onPause();

        void onResume();

        void onStop();

        void preOnCreate();

        void setActivity(Activity activity);
    }

    static {
        LOGTAG = AdActivity.class.getSimpleName();
    }

    public AdActivity() {
        this(new MobileAdsLoggerFactory(), AdRegistration.getAmazonAdRegistrationExecutor(), new ActivityAdapterFactory(new MobileAdsLoggerFactory()));
    }

    AdActivity(MobileAdsLoggerFactory loggerFactory, AdRegistrationExecutor amazonAdRegistration, ActivityAdapterFactory activityAdapterFactory) {
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.amazonAdRegistration = amazonAdRegistration;
        this.activityAdapterFactory = activityAdapterFactory;
    }

    public void onCreate(Bundle savedInstanceState) {
        initializeSdk();
        this.adapter = this.activityAdapterFactory.createAdapter(getIntent());
        if (this.adapter == null) {
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        this.adapter.setActivity(this);
        this.adapter.preOnCreate();
        super.onCreate(savedInstanceState);
        this.adapter.onCreate();
    }

    private void initializeSdk() {
        if (this.logger == null) {
            setLoggerFactory(new MobileAdsLoggerFactory());
        }
        if (this.amazonAdRegistration == null) {
            setAmazonAdRegistrationExecutor(AdRegistration.getAmazonAdRegistrationExecutor());
        }
        if (this.activityAdapterFactory == null) {
            setActivityAdapterFactory(new ActivityAdapterFactory(new MobileAdsLoggerFactory()));
        }
        this.amazonAdRegistration.initializeAds(getApplicationContext());
    }

    void setLoggerFactory(MobileAdsLoggerFactory loggerFactory) {
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
    }

    void setAmazonAdRegistrationExecutor(AdRegistrationExecutor amazonAdRegistrationExecutor) {
        this.amazonAdRegistration = amazonAdRegistrationExecutor;
    }

    void setActivityAdapterFactory(ActivityAdapterFactory activityAdapterFactory) {
        this.activityAdapterFactory = activityAdapterFactory;
    }

    public void onPause() {
        super.onPause();
        this.adapter.onPause();
    }

    public void onResume() {
        super.onResume();
        this.adapter.onResume();
    }

    public void onStop() {
        this.adapter.onStop();
        super.onStop();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.adapter.onConfigurationChanged(newConfig);
    }

    public void onBackPressed() {
        if (!this.adapter.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
