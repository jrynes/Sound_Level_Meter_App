package com.amazon.device.ads;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Properties;
import javax.crypto.Cipher;

class DebugProperties {
    public static final String DEBUG_AAX_AD_HOSTNAME = "debug.aaxHostname";
    public static final String DEBUG_AAX_AD_PARAMS = "debug.aaxAdParams";
    public static final String DEBUG_AAX_CONFIG_HOSTNAME = "debug.aaxConfigHostname";
    public static final String DEBUG_AAX_CONFIG_PARAMS = "debug.aaxConfigParams";
    public static final String DEBUG_AAX_CONFIG_USE_SECURE = "debug.aaxConfigUseSecure";
    public static final String DEBUG_ADID = "debug.adid";
    public static final String DEBUG_AD_PREF_URL = "debug.adPrefURL";
    public static final String DEBUG_APPID = "debug.appid";
    public static final String DEBUG_CAN_TIMEOUT = "debug.canTimeout";
    public static final String DEBUG_CHANNEL = "debug.channel";
    public static final String DEBUG_CONFIG_FEATURE_USE_GPS_ADVERTISING_ID = "debug.fUseGPSAID";
    public static final String DEBUG_DINFO = "debug.dinfo";
    public static final String DEBUG_ECPM = "debug.ec";
    public static final String DEBUG_GEOLOC = "debug.geoloc";
    public static final String DEBUG_IDFA = "debug.idfa";
    public static final String DEBUG_LOGGING = "debug.logging";
    public static final String DEBUG_MADS_HOSTNAME = "debug.madsHostname";
    public static final String DEBUG_MADS_USE_SECURE = "debug.madsUseSecure";
    public static final String DEBUG_MD5UDID = "debug.md5udid";
    public static final String DEBUG_MXSZ = "debug.mxsz";
    public static final String DEBUG_NORETRYTTL = "debug.noRetryTTL";
    public static final String DEBUG_NORETRYTTL_MAX = "debug.noRetryTTLMax";
    public static final String DEBUG_ON = "debug.mode";
    public static final String DEBUG_OPT_OUT = "debug.optOut";
    public static final String DEBUG_PA = "debug.pa";
    public static final String DEBUG_PK = "debug.pk";
    public static final String DEBUG_PKG = "debug.pkg";
    public static final String DEBUG_PT = "debug.pt";
    public static final String DEBUG_SEND_GEO = "debug.sendGeo";
    public static final String DEBUG_SHA1UDID = "debug.sha1udid";
    public static final String DEBUG_SHOULD_FETCH_CONFIG = "debug.shouldFetchConfig";
    public static final String DEBUG_SHOULD_IDENTIFY_USER = "debug.shouldIdentifyUser";
    public static final String DEBUG_SHOULD_REGISTER_SIS = "debug.shouldRegisterSIS";
    public static final String DEBUG_SIS_DOMAIN = "debug.sisDomain";
    public static final String DEBUG_SIS_URL = "debug.sisURL";
    public static final String DEBUG_SIZE = "debug.size";
    public static final String DEBUG_SLOT = "debug.slot";
    public static final String DEBUG_SLOTS = "debug.slots";
    public static final String DEBUG_SLOT_ID = "debug.slotId";
    public static final String DEBUG_SP = "debug.sp";
    public static final String DEBUG_SUPPORTED_MEDIA_TYPES = "debug.supportedMediaTypes";
    public static final String DEBUG_TEST = "debug.test";
    public static final String DEBUG_TLS_ENABLED = "debug.tlsEnabled";
    public static final String DEBUG_TRUNCATE_LAT_LON = "debug.truncateLatLon";
    public static final String DEBUG_UA = "debug.ua";
    public static final String DEBUG_VER = "debug.ver";
    public static final String DEBUG_VIDEO_OPTIONS = "debug.videoOptions";
    public static final String DEBUG_WEBVIEWS = "debug.webViews";
    private static final String FILE_PREFIX = "/com.amazon.device.ads.debug";
    private static final String LOGTAG;
    private static final DebugProperties instance;
    private static final BigInteger privExponent;
    private static final BigInteger privModulus;
    private boolean debugModeOn;
    private final Properties debugProperties;
    private final FileHandlerFactory fileHandlerFactory;
    private final MobileAdsLogger logger;

    static {
        instance = new DebugProperties(new DefaultFileHandlerFactory());
        LOGTAG = DebugProperties.class.getSimpleName();
        privModulus = new BigInteger("22425945969293236512819607281747202268852113345956851069545419503178249900977203670147638322801582881051882957295768557918356441519366172126884608406316888515239296504501830280664879549133570276792155151832332847188532369002492210234019359186842709493620665119919750832332220777141369255943445578381285984064028865613478676828533273460580467686485184132743895959747097454385452868408957601246667523882372216446056029831689133478714597838700864119273209955182548633182248700235085802575904827859971001196599005060045450779595767759943984991630413046800554347791145167910883355627096118148593841261053098773337592734097");
        privExponent = new BigInteger("5599215006722084151841970702827860151139465197978118529242591197804380779249736540498127864809226859371835159226553869008622098243456195347852554241917744888762998133926842072150379542281041403163862165638226686887497980590930009552760406707269286898150890998325325890252103828011111664174475487114957696526157790937869377570600085450453371238028811033168218737171144699577236108423054506552958366535341910569552237227686862748056351625445281035713423043506793107235726047151346608576583081807969458368853010104969843563629579750936551771756389538574062221915919980316992216032119182896925094308799622409361028579777");
    }

    public DebugProperties(FileHandlerFactory fileHandlerFactory) {
        this.debugProperties = new Properties();
        this.debugModeOn = false;
        this.logger = new MobileAdsLoggerFactory().createMobileAdsLogger(LOGTAG);
        this.fileHandlerFactory = fileHandlerFactory;
    }

    public static DebugProperties getInstance() {
        return instance;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readDebugProperties() {
        /*
        r14 = this;
        r13 = 1;
        r7 = android.os.Environment.getExternalStorageState();
        r8 = "mounted";
        r8 = r8.equals(r7);
        if (r8 == 0) goto L_0x003b;
    L_0x000d:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = android.os.Environment.getExternalStorageDirectory();
        r8 = r8.append(r9);
        r9 = "/com.amazon.device.ads.debug";
        r8 = r8.append(r9);
        r6 = r8.toString();
        r8 = r14.fileHandlerFactory;
        r5 = r8.createFileInputHandler(r6);
        r8 = r5.doesFileExist();
        if (r8 == 0) goto L_0x003b;
    L_0x0030:
        r8 = r5.getFileLength();
        r10 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r8 <= 0) goto L_0x003c;
    L_0x003b:
        return;
    L_0x003c:
        r1 = r5.readAllBytesFromFileAndClose();
        if (r1 == 0) goto L_0x003b;
    L_0x0042:
        r3 = r14.decrypt(r1);
        if (r3 == 0) goto L_0x003b;
    L_0x0048:
        r0 = new java.io.ByteArrayInputStream;
        r0.<init>(r3);
        r8 = r14.debugProperties;
        r8.clear();
        r8 = r14.debugProperties;	 Catch:{ IOException -> 0x007a }
        r8.load(r0);	 Catch:{ IOException -> 0x007a }
        r0.close();	 Catch:{ IOException -> 0x0071 }
    L_0x005a:
        r8 = r14.debugProperties;
        r9 = "debug.mode";
        r10 = "false";
        r2 = r8.getProperty(r9, r10);
        r8 = java.lang.Boolean.valueOf(r2);
        r8 = r8.booleanValue();
        if (r8 == 0) goto L_0x003b;
    L_0x006e:
        r14.debugModeOn = r13;
        goto L_0x003b;
    L_0x0071:
        r4 = move-exception;
        r8 = r14.logger;
        r9 = "Exception closing input stream.";
        r8.m637d(r9);
        goto L_0x005a;
    L_0x007a:
        r4 = move-exception;
        r8 = r14.logger;	 Catch:{ all -> 0x0099 }
        r9 = "Exception loading debug properties. %s";
        r10 = 1;
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x0099 }
        r11 = 0;
        r12 = r4.getMessage();	 Catch:{ all -> 0x0099 }
        r10[r11] = r12;	 Catch:{ all -> 0x0099 }
        r8.m638d(r9, r10);	 Catch:{ all -> 0x0099 }
        r0.close();	 Catch:{ IOException -> 0x0090 }
        goto L_0x005a;
    L_0x0090:
        r4 = move-exception;
        r8 = r14.logger;
        r9 = "Exception closing input stream.";
        r8.m637d(r9);
        goto L_0x005a;
    L_0x0099:
        r8 = move-exception;
        r0.close();	 Catch:{ IOException -> 0x009e }
    L_0x009d:
        throw r8;
    L_0x009e:
        r4 = move-exception;
        r9 = r14.logger;
        r10 = "Exception closing input stream.";
        r9.m637d(r10);
        goto L_0x009d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amazon.device.ads.DebugProperties.readDebugProperties():void");
    }

    public String getDebugPropertyAsString(String property, String defaultValue) {
        return !this.debugModeOn ? defaultValue : this.debugProperties.getProperty(property, defaultValue);
    }

    public Integer getDebugPropertyAsInteger(String property, Integer defaultValue) {
        if (this.debugModeOn) {
            String propertyValue = this.debugProperties.getProperty(property);
            if (propertyValue != null) {
                try {
                    defaultValue = Integer.valueOf(Integer.parseInt(propertyValue));
                } catch (NumberFormatException e) {
                    this.logger.m640e("Unable to parse integer debug property - property: %s, value: %s", property, propertyValue);
                }
            }
        }
        return defaultValue;
    }

    public Boolean getDebugPropertyAsBoolean(String property, Boolean defaultValue) {
        if (this.debugModeOn) {
            String propertyValue = this.debugProperties.getProperty(property);
            if (propertyValue != null) {
                try {
                    defaultValue = Boolean.valueOf(Boolean.parseBoolean(propertyValue));
                } catch (NumberFormatException e) {
                    this.logger.m640e("Unable to parse boolean debug property - property: %s, value: %s", property, propertyValue);
                }
            }
        }
        return defaultValue;
    }

    public Long getDebugPropertyAsLong(String property, Long defaultValue) {
        if (this.debugModeOn) {
            String propertyValue = this.debugProperties.getProperty(property);
            if (propertyValue != null) {
                try {
                    defaultValue = Long.valueOf(Long.parseLong(propertyValue));
                } catch (NumberFormatException e) {
                    this.logger.m640e("Unable to parse long debug property - property: %s, value: %s", property, propertyValue);
                }
            }
        }
        return defaultValue;
    }

    public boolean containsDebugProperty(String property) {
        if (this.debugModeOn) {
            return this.debugProperties.containsKey(property);
        }
        return false;
    }

    public boolean isDebugModeOn() {
        return this.debugModeOn;
    }

    protected byte[] decrypt(byte[] data) {
        byte[] decryptedData = null;
        try {
            PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec(privModulus, privExponent));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(2, key);
            decryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            this.logger.m637d("Exception " + e + " trying to decrypt debug file");
        }
        return decryptedData;
    }

    void setDebugProperty(String property, String value) {
        this.debugProperties.put(property, value);
    }

    void enableDebugging() {
        this.debugModeOn = true;
    }

    void disableDebugging() {
        this.debugModeOn = false;
    }
}
