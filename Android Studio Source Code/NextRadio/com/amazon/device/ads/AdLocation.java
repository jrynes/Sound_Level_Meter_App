package com.amazon.device.ads;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.widget.AutoScrollHelper;
import com.amazon.device.ads.Configuration.ConfigOption;

class AdLocation {
    private static final int ARCMINUTE_PRECISION = 6;
    private static final String LOGTAG;
    private static final float MAX_DISTANCE_IN_KILOMETERS = 3.0f;
    private final Configuration configuration;
    private final Context context;
    private final MobileAdsLogger logger;

    private enum LocationAwareness {
        LOCATION_AWARENESS_NORMAL,
        LOCATION_AWARENESS_TRUNCATED,
        LOCATION_AWARENESS_DISABLED
    }

    static {
        LOGTAG = AdLocation.class.getSimpleName();
    }

    public AdLocation(Context context) {
        this(context, Configuration.getInstance());
    }

    AdLocation(Context context, Configuration configuration) {
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.context = context;
        this.configuration = configuration;
    }

    private static double roundToArcminutes(double coordinate) {
        return ((double) Math.round(coordinate * 60.0d)) / 60.0d;
    }

    private LocationAwareness getLocationAwareness() {
        if (this.configuration.getBoolean(ConfigOption.TRUNCATE_LAT_LON)) {
            return LocationAwareness.LOCATION_AWARENESS_TRUNCATED;
        }
        return LocationAwareness.LOCATION_AWARENESS_NORMAL;
    }

    public Location getLocation() {
        LocationAwareness locationAwareness = getLocationAwareness();
        if (LocationAwareness.LOCATION_AWARENESS_DISABLED.equals(locationAwareness)) {
            return null;
        }
        LocationManager lm = (LocationManager) this.context.getSystemService("location");
        Location gpsLocation = null;
        try {
            gpsLocation = lm.getLastKnownLocation("gps");
        } catch (SecurityException e) {
            this.logger.m637d("Failed to retrieve GPS location: No permissions to access GPS");
        } catch (IllegalArgumentException e2) {
            this.logger.m637d("Failed to retrieve GPS location: No GPS found");
        }
        Location networkLocation = null;
        try {
            networkLocation = lm.getLastKnownLocation("network");
        } catch (SecurityException e3) {
            this.logger.m637d("Failed to retrieve network location: No permissions to access network location");
        } catch (IllegalArgumentException e4) {
            this.logger.m637d("Failed to retrieve network location: No network provider found");
        }
        if (gpsLocation == null && networkLocation == null) {
            return null;
        }
        Location result;
        if (gpsLocation == null || networkLocation == null) {
            if (gpsLocation != null) {
                this.logger.m637d("Setting lat/long using GPS, not network");
                result = gpsLocation;
            } else {
                this.logger.m637d("Setting lat/long using network location, not GPS");
                result = networkLocation;
            }
        } else if (gpsLocation.distanceTo(networkLocation) / 1000.0f <= MAX_DISTANCE_IN_KILOMETERS) {
            if ((gpsLocation.hasAccuracy() ? gpsLocation.getAccuracy() : AutoScrollHelper.NO_MAX) < (networkLocation.hasAccuracy() ? networkLocation.getAccuracy() : AutoScrollHelper.NO_MAX)) {
                this.logger.m637d("Setting lat/long using GPS determined by distance");
                result = gpsLocation;
            } else {
                this.logger.m637d("Setting lat/long using network determined by distance");
                result = networkLocation;
            }
        } else if (gpsLocation.getTime() > networkLocation.getTime()) {
            this.logger.m637d("Setting lat/long using GPS");
            result = gpsLocation;
        } else {
            this.logger.m637d("Setting lat/long using network");
            result = networkLocation;
        }
        if (LocationAwareness.LOCATION_AWARENESS_TRUNCATED.equals(locationAwareness)) {
            result.setLatitude(((double) Math.round(Math.pow(10.0d, 6.0d) * roundToArcminutes(result.getLatitude()))) / Math.pow(10.0d, 6.0d));
            result.setLongitude(((double) Math.round(Math.pow(10.0d, 6.0d) * roundToArcminutes(result.getLongitude()))) / Math.pow(10.0d, 6.0d));
        }
        return result;
    }
}
