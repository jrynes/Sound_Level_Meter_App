package com.amazon.device.ads;

import java.util.ArrayList;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

class MobileAdsLogger implements Logger {
    private static final String DEFAULT_LOGTAG_PREFIX = "AmazonMobileAds";
    private static final int DEFAULT_MAX_LENGTH = 1000;
    static final String LOGGING_ENABLED = "loggingEnabled";
    private final DebugProperties debugProperties;
    private final Logger logger;
    private int maxLength;
    private final Settings settings;

    /* renamed from: com.amazon.device.ads.MobileAdsLogger.1 */
    static /* synthetic */ class C03291 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level;

        static {
            $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level = new int[Level.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level[Level.DEBUG.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level[Level.ERROR.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level[Level.INFO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level[Level.VERBOSE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level[Level.WARN.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public enum Level {
        DEBUG,
        ERROR,
        INFO,
        VERBOSE,
        WARN
    }

    public MobileAdsLogger(Logger logger) {
        this(logger, DebugProperties.getInstance(), Settings.getInstance());
    }

    MobileAdsLogger(Logger logger, DebugProperties debugProperties, Settings settings) {
        this.maxLength = DEFAULT_MAX_LENGTH;
        this.logger = logger.withLogTag(DEFAULT_LOGTAG_PREFIX);
        this.debugProperties = debugProperties;
        this.settings = settings;
    }

    public MobileAdsLogger withMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public MobileAdsLogger withLogTag(String logTag) {
        this.logger.withLogTag("AmazonMobileAds " + logTag);
        return this;
    }

    public void enableLogging(boolean enabled) {
        this.settings.putTransientBoolean(LOGGING_ENABLED, enabled);
    }

    public boolean canLog() {
        if (this.logger == null) {
            return false;
        }
        return this.debugProperties.getDebugPropertyAsBoolean(DebugProperties.DEBUG_LOGGING, Boolean.valueOf(this.settings.getBoolean(LOGGING_ENABLED, false))).booleanValue();
    }

    public final void enableLoggingWithSetterNotification(boolean enable) {
        if (!enable) {
            logSetterNotification("Debug logging", Boolean.valueOf(enable));
        }
        enableLogging(enable);
        if (enable) {
            logSetterNotification("Debug logging", Boolean.valueOf(enable));
            m638d("Amazon Mobile Ads API Version: %s", Version.getRawSDKVersion());
        }
    }

    public void logSetterNotification(String capitalizedSingularPropertyName, Object value) {
        if (!canLog()) {
            return;
        }
        if (value instanceof Boolean) {
            String str = "%s has been %s.";
            Object[] objArr = new Object[2];
            objArr[0] = capitalizedSingularPropertyName;
            objArr[1] = ((Boolean) value).booleanValue() ? "enabled" : "disabled";
            m638d(str, objArr);
            return;
        }
        m638d("%s has been set: %s", capitalizedSingularPropertyName, String.valueOf(value));
    }

    public void m642i(String message, Object... params) {
        log(Level.INFO, message, params);
    }

    public void m644v(String message, Object... params) {
        log(Level.VERBOSE, message, params);
    }

    public void m638d(String message, Object... params) {
        log(Level.DEBUG, message, params);
    }

    public void m646w(String message, Object... params) {
        log(Level.WARN, message, params);
    }

    public void m640e(String message, Object... params) {
        log(Level.ERROR, message, params);
    }

    public void m641i(String message) {
        m642i(message, (Object[]) null);
    }

    public void m643v(String message) {
        m644v(message, (Object[]) null);
    }

    public void m637d(String message) {
        m638d(message, (Object[]) null);
    }

    public void m645w(String message) {
        m646w(message, (Object[]) null);
    }

    public void m639e(String message) {
        m640e(message, (Object[]) null);
    }

    public void log(Level level, String message, Object... params) {
        doLog(false, level, message, params);
    }

    public void forceLog(Level level, String message, Object... params) {
        doLog(true, level, message, params);
    }

    private void doLog(boolean force, Level level, String message, Object... params) {
        if (canLog() || force) {
            for (String data : formatAndSplit(message, params)) {
                switch (C03291.$SwitchMap$com$amazon$device$ads$MobileAdsLogger$Level[level.ordinal()]) {
                    case Zone.PRIMARY /*1*/:
                        this.logger.m627d(data);
                        break;
                    case Zone.SECONDARY /*2*/:
                        this.logger.m628e(data);
                        break;
                    case Protocol.GGP /*3*/:
                        this.logger.m629i(data);
                        break;
                    case Type.MF /*4*/:
                        this.logger.m630v(data);
                        break;
                    case Service.RJE /*5*/:
                        this.logger.m631w(data);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private Iterable<String> formatAndSplit(String msg, Object... params) {
        if (params != null && params.length > 0) {
            msg = String.format(msg, params);
        }
        return split(msg, this.maxLength);
    }

    private Iterable<String> split(String input, int maxLength) {
        ArrayList<String> output = new ArrayList();
        if (!(input == null || input.length() == 0)) {
            int i = 0;
            while (i < input.length()) {
                output.add(input.substring(i, Math.min(input.length(), i + maxLength)));
                i += maxLength;
            }
        }
        return output;
    }
}
