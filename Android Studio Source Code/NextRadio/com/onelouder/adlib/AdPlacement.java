package com.onelouder.adlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import com.admarvel.android.ads.Constants;
import com.google.ads.mediation.AbstractAdViewAdapter;
import com.onelouder.adlib.AdInterstitial.AdInterstitialListener;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.transport.discovery.multicast.MulticastDiscoveryAgent;
import org.apache.activemq.transport.stomp.Stomp;

public class AdPlacement {
    public static final String ACTION_1L_ADVIEW_CLICKED = "com.onelouder.adlib.adview.clicked";
    public static final String ACTION_1L_ADVIEW_CLKTOACT = "com.onelouder.adlib.adview.clktoact";
    public static final String ACTION_1L_ADVIEW_CLOSED = "com.onelouder.adlib.adview.closed";
    public static final String ACTION_1L_ADVIEW_DISPLAYED = "com.onelouder.adlib.adview.displayed";
    public static final String ACTION_1L_ADVIEW_RECEIVED = "com.onelouder.adlib.adview.received";
    public static final String ACTION_1L_ADVIEW_REQUESTED = "com.onelouder.adlib.adview.requested";
    public static final String ACTION_1L_ADVIEW_REQ_FAILED = "com.onelouder.adlib.adview.request.failed";
    public static final String ACTION_1L_INTERSTITIAL_CLICKED = "com.onelouder.adlib.interstitial.clicked";
    public static final String ACTION_1L_INTERSTITIAL_CLOSED = "com.onelouder.adlib.interstitial.closed";
    public static final String ACTION_1L_INTERSTITIAL_DISPLAYED = "com.onelouder.adlib.interstitial.displayed";
    public static final String ACTION_1L_INTERSTITIAL_RECEIVED = "com.onelouder.adlib.interstitial.received";
    public static final String ACTION_1L_INTERSTITIAL_REQUESTED = "com.onelouder.adlib.interstitial.requested";
    public static final String ACTION_1L_INTERSTITIAL_REQ_FAILED = "com.onelouder.adlib.interstitial.request.failed";
    public static final int ERRORCODE_ADS_NOT_ENABLED = 10016;
    public static final int ERRORCODE_AD_COUNT_EXCEEDED = 10012;
    public static final int ERRORCODE_AD_NETWORK_MISSING = 10013;
    public static final int ERRORCODE_AD_PLACEMENT_PAUSED = 10011;
    public static final int ERRORCODE_NO_CONNECTIVITY = 10014;
    public static final int ERRORCODE_NO_PLACEMENT = 10010;
    public static final int ERRORCODE_REQUEST_TIMEOUT = 10015;
    public static final String EXTRA_1L_ERROR_CODE = "1l-error-code";
    public static final String EXTRA_1L_ERROR_MESSAGE = "1l-error-message";
    public static final String EXTRA_1L_OPEN_DURATION = "1l-open-duration";
    public static final String EXTRA_1L_PACKAGENAME = "1l-package-name";
    public static final String EXTRA_1L_PLACEMENT_ID = "1l-placementid";
    public static final String EXTRA_1L_PLACEMENT_NETWORK = "1l-network";
    public static final String EXTRA_1L_PLACEMENT_PUBID = "1l-pubid";
    public static final String EXTRA_1L_PLACEMENT_SITEID = "1l-siteid";
    public static final String EXTRA_1L_URL_CLICKED = "1l-url-clicked";
    public static final String EXTRA_1L_VIDEO_LENGTH = "1l-video-length";
    private static final String TAG = "AdPlacement";
    private String ID;
    private boolean _ispaused;
    private I1LouderAdProxy adproxy;
    private boolean click_redirect;
    private boolean clone;
    private String closebutton;
    protected SoftReference<Context> contextRef;
    private String density;
    private String device;
    private int freq;
    private boolean impressionflag;
    private String int_type;
    private Handler mHandler;
    private AdPlacementListener mListener;
    private Runnable mReadyRunnable;
    private AdInterstitialListener mUserListener;
    private String network;
    private String onclosead;
    private boolean onetime;
    private String pubid;
    private boolean recycle;
    private int refresh_rate;
    private String reset;
    private boolean reset_on_cancel;
    private String rollover;
    private int runnableCount;
    private int sc;
    private String siteid;
    private String str_cancel;
    private String str_nojoy;
    private String str_ok;
    private String str_text;
    private String str_title;
    private long timestamp;
    private long timestamp_paused;
    private String type;

    public interface AdPlacementListener {
        void onInterstitialReady(AdPlacement adPlacement);

        void onInterstitialRequestFailed(AdPlacement adPlacement, int i, String str);
    }

    /* renamed from: com.onelouder.adlib.AdPlacement.1 */
    class C12841 implements Runnable {
        final /* synthetic */ Context val$context;
        final /* synthetic */ String val$text;

        C12841(Context context, String str) {
            this.val$context = context;
            this.val$text = str;
        }

        public void run() {
            Toast.makeText(this.val$context, this.val$text, 1).show();
        }
    }

    /* renamed from: com.onelouder.adlib.AdPlacement.2 */
    class C12852 implements Runnable {
        C12852() {
        }

        public void run() {
            if (AdPlacement.this.adproxy == null) {
                return;
            }
            if (AdPlacement.this.adproxy.isInterstitialReady()) {
                if (AdPlacement.this.mListener != null) {
                    AdPlacement.this.mListener.onInterstitialReady(AdPlacement.this);
                }
                if (AdPlacement.this.mUserListener != null) {
                    AdPlacement.this.mUserListener.onInterstitialReceived();
                }
                AdPlacement.this.sendBroadcast((Context) AdPlacement.this.contextRef.get(), AdPlacement.ACTION_1L_INTERSTITIAL_RECEIVED, null);
            } else if (AdPlacement.this.runnableCount >= 60 || !AdPlacement.this.adproxy.isInterstitialRequested()) {
                if (AdPlacement.this.mListener != null) {
                    AdPlacement.this.mListener.onInterstitialRequestFailed(AdPlacement.this, AdPlacement.ERRORCODE_REQUEST_TIMEOUT, null);
                }
                if (!AdPlacement.this.network.equals("admarvel")) {
                    AdPlacement.this.sendBroadcast((Context) AdPlacement.this.contextRef.get(), AdPlacement.ACTION_1L_INTERSTITIAL_REQ_FAILED, null);
                }
                AdPlacement.this.adproxy = null;
            } else {
                AdPlacement.this.mHandler.postDelayed(AdPlacement.this.mReadyRunnable, 1000);
                AdPlacement.this.runnableCount = AdPlacement.this.runnableCount + 1;
            }
        }
    }

    private AdPlacement() {
        this._ispaused = true;
        this.impressionflag = false;
        this.onetime = false;
        this.recycle = false;
        this.clone = false;
        this.reset_on_cancel = false;
        this.click_redirect = true;
        this.refresh_rate = 0;
        this.sc = 0;
        this.freq = 0;
        this.timestamp = 0;
        this.timestamp_paused = 0;
        this.runnableCount = 1;
        this.mReadyRunnable = new C12852();
    }

    AdPlacement(String id, String definition, Handler handler) {
        this._ispaused = true;
        this.impressionflag = false;
        this.onetime = false;
        this.recycle = false;
        this.clone = false;
        this.reset_on_cancel = false;
        this.click_redirect = true;
        this.refresh_rate = 0;
        this.sc = 0;
        this.freq = 0;
        this.timestamp = 0;
        this.timestamp_paused = 0;
        this.runnableCount = 1;
        this.mReadyRunnable = new C12852();
        this.mHandler = handler;
        this.ID = id;
        applyDefinition(definition);
    }

    public void refresh(Context context) {
        applyDefinition(Preferences.getSimplePref(context, this.ID + "-" + this.type, null));
    }

    protected void applyDefinition(String definition) {
        if (definition != null && definition.length() > 0) {
            for (String value : definition.split(Stomp.COMMA)) {
                int idx = value.indexOf(61);
                if (idx != -1) {
                    String name = value.substring(0, idx);
                    if (name.equals("rollover")) {
                        this.rollover = value.substring(idx + 1);
                    } else if (name.equals(AbstractAdViewAdapter.AD_UNIT_ID_PARAMETER)) {
                        this.pubid = value.substring(idx + 1);
                    } else if (name.equals("siteid")) {
                        this.siteid = value.substring(idx + 1);
                    } else if (name.equals("network")) {
                        this.network = value.substring(idx + 1);
                    } else if (name.equals("device")) {
                        this.device = value.substring(idx + 1);
                    } else if (name.equals("density")) {
                        this.density = value.substring(idx + 1);
                    } else if (name.equals("reset")) {
                        this.reset = value.substring(idx + 1);
                    } else if (name.equals("close_button")) {
                        this.closebutton = value.substring(idx + 1);
                    } else if (name.equals("onclosead")) {
                        this.onclosead = value.substring(idx + 1);
                    } else if (name.equals("str_title")) {
                        this.str_title = value.substring(idx + 1);
                    } else if (name.equals("str_text")) {
                        this.str_text = value.substring(idx + 1);
                    } else if (name.equals("str_ok")) {
                        this.str_ok = value.substring(idx + 1);
                    } else if (name.equals("str_cancel")) {
                        this.str_cancel = value.substring(idx + 1);
                    } else if (name.equals("str_nojoy")) {
                        this.str_nojoy = value.substring(idx + 1);
                    } else if (name.equals("int_type")) {
                        this.int_type = value.substring(idx + 1);
                    } else if (name.equals("refresh_rate")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.refresh_rate = Utils.ParseInteger(temp);
                        }
                    } else if (name.equals("sc")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.sc = Utils.ParseInteger(temp);
                        }
                    } else if (name.equals("freq")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.freq = Utils.ParseInteger(temp);
                        }
                    } else if (name.equals("onetime")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.onetime = temp.equals(Stomp.TRUE);
                        }
                    } else if (name.equals("recycle")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.recycle = temp.equals(Stomp.TRUE);
                        }
                    } else if (name.equals("clone")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.clone = temp.equals(Stomp.TRUE);
                        }
                    } else if (name.equals("reset_on_cancel")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.reset_on_cancel = temp.equals(Stomp.TRUE);
                        }
                    } else if (name.equals("click_redirect")) {
                        temp = value.substring(idx + 1);
                        if (temp.length() > 0) {
                            this.click_redirect = temp.equals(Stomp.TRUE);
                        }
                    }
                }
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdPlacement ID=" + this.ID);
        return sb.toString();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPubid() {
        return this.pubid;
    }

    public String getSiteid() {
        return this.siteid;
    }

    public String getNetwork() {
        return this.network;
    }

    public String getReset() {
        return this.reset;
    }

    public String getRolloverId() {
        return this.rollover;
    }

    public String geIntType() {
        if (this.int_type != null) {
            return this.int_type;
        }
        if (this.ID.equals("launch")) {
            return "AppOpen";
        }
        return "ScreenChange";
    }

    public long getTimestamp() {
        if (this.timestamp_paused != 0) {
            return this.timestamp_paused;
        }
        return this.timestamp;
    }

    public void setTimestamp(long ts) {
        this.timestamp = ts;
        this.timestamp_paused = 0;
    }

    public long getRemaining(Context context) {
        int interval = getRefreshRate(context);
        long now = System.currentTimeMillis();
        long remaining = ((long) interval) - (now - this.timestamp);
        if (this.timestamp_paused > 0) {
            remaining += now - this.timestamp_paused;
        }
        if (remaining < 0) {
            remaining = 0;
        }
        Diagnostics.m1951d(TAG(), "getRemaining()=" + remaining);
        return remaining;
    }

    public int getPauseDuration() {
        return this.freq;
    }

    public int getRefreshRate(Context context) {
        if (this.refresh_rate == 0) {
            this.refresh_rate = Preferences.getSimplePref(context, "ads_refresh_rate", 30);
        }
        return this.refresh_rate * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
    }

    public boolean showCloseButton(Context context) {
        if (this.closebutton == null) {
            this.closebutton = Preferences.getSimplePref(context, "ads_close_button", null);
        }
        if (this.closebutton != null) {
            return this.closebutton.equals(Stomp.TRUE);
        }
        return false;
    }

    public boolean getImpressionFlag() {
        if (this.impressionflag) {
            setTimestamp(System.currentTimeMillis());
        }
        return this.impressionflag;
    }

    public void clearImpressionFlag() {
        this.impressionflag = false;
    }

    public boolean doClickRedirect() {
        return this.click_redirect;
    }

    public boolean isRecycleable() {
        return this.recycle;
    }

    public boolean isCloneable() {
        return this.clone;
    }

    public boolean isResetOnCancel() {
        return this.reset_on_cancel;
    }

    public boolean isOnetime() {
        return this.onetime;
    }

    public boolean isDefined() {
        if (this.pubid == null) {
            Diagnostics.m1952e(TAG, "isDefined, pubid == null");
            return false;
        } else if (this.siteid == null) {
            Diagnostics.m1952e(TAG, "isDefined, siteid == null");
            return false;
        } else if (this.network == null) {
            Diagnostics.m1952e(TAG, "isDefined, network == null");
            return false;
        } else if (this.network.equals("admarvel") || this.network.equals("smaato") || this.network.equals("googleplay") || this.network.equals("psm") || this.network.equals("psmplus") || this.network.equals("aol") || this.network.equals("adcolony") || this.network.equals("vungle") || this.network.equals("virool") || this.network.equals("vast")) {
            return true;
        } else {
            Diagnostics.m1952e(TAG, "isDefined, network is invalid, " + this.network);
            return false;
        }
    }

    String getString(Context context, String name) {
        String key = null;
        if (name.equals(SettingsJsonConstants.PROMPT_TITLE_KEY)) {
            key = this.str_title;
        } else if (name.equals("text")) {
            key = this.str_text;
        } else if (name.equals("ok")) {
            key = this.str_ok;
        } else if (name.equals("cancel")) {
            key = this.str_cancel;
        } else if (name.equals("nojoy")) {
            key = this.str_nojoy;
        }
        return Preferences.getSimplePref(context, "1Louder-string-" + key, Stomp.EMPTY);
    }

    void sendBroadcast(Context context, String action, HashMap<String, Object> params) {
        if (context != null) {
            Intent intent = new Intent(action);
            intent.putExtra(EXTRA_1L_PLACEMENT_ID, this.ID);
            intent.putExtra(EXTRA_1L_PLACEMENT_PUBID, this.pubid);
            intent.putExtra(EXTRA_1L_PLACEMENT_SITEID, this.siteid);
            intent.putExtra(EXTRA_1L_PLACEMENT_NETWORK, this.network);
            intent.putExtra(EXTRA_1L_PACKAGENAME, context.getPackageName());
            if (params != null) {
                for (String key : params.keySet()) {
                    Object value = params.get(key);
                    if (value instanceof String) {
                        intent.putExtra(key, (String) value);
                    } else if (value instanceof Integer) {
                        intent.putExtra(key, ((Integer) value).intValue());
                    } else if (value instanceof Long) {
                        intent.putExtra(key, ((Long) value).longValue());
                    } else if (value instanceof Boolean) {
                        intent.putExtra(key, ((Boolean) value).booleanValue());
                    } else if (value != null) {
                        Diagnostics.m1952e(TAG, "sendBroadcast, Unexpected type " + value);
                    }
                }
            }
            context.sendBroadcast(intent);
        }
    }

    private String TAG() {
        return getID();
    }

    String getID() {
        return this.ID;
    }

    boolean isDefaultDensity() {
        if (this.density != null) {
            return this.density.equals(MulticastDiscoveryAgent.DEFAULT_HOST_STR);
        }
        return true;
    }

    boolean isLowDensity() {
        if (this.density != null) {
            return this.density.equals("low");
        }
        return false;
    }

    boolean isMediumDensity() {
        if (this.density != null) {
            return this.density.equals("medium");
        }
        return false;
    }

    boolean isTvDensity() {
        if (this.density != null && this.density.equals("tv") && Utils.isPortrait()) {
            return true;
        }
        return false;
    }

    boolean isHighDensity() {
        if (this.density != null) {
            return this.density.equals("high");
        }
        return false;
    }

    boolean isLarge() {
        if (this.device == null || !this.device.equals("large")) {
            return false;
        }
        return true;
    }

    boolean isXLarge() {
        if (this.device == null || !this.device.equals("x-large")) {
            return false;
        }
        return true;
    }

    boolean areStringsDefined() {
        return (this.str_title == null && this.str_text == null && this.str_ok == null && this.str_cancel == null) ? false : true;
    }

    boolean ispaused() {
        return this._ispaused;
    }

    boolean ispaused_until(Context context) {
        long pauseuntil = Preferences.getSimplePref(context, "1Louder-pauseuntil-" + this.ID, 0);
        if (pauseuntil > System.currentTimeMillis()) {
            Diagnostics.m1955i(TAG, "paused until " + new Date(pauseuntil).toString());
            return true;
        }
        if (pauseuntil > 0 && areStringsDefined()) {
            reset(context, 0);
        }
        Preferences.setSimplePref(context, "1Louder-pauseuntil-" + this.ID, 0);
        return false;
    }

    void pauseFromClose(Context context, int duration) {
        Diagnostics.m1951d(TAG(), "pauseFromClose()");
        long pauseuntil = System.currentTimeMillis();
        if (Diagnostics.getInstance().isEnabled(4)) {
            pauseuntil += (long) ((duration * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) * 60);
        } else {
            pauseuntil += (long) (((duration * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) * 60) * 60);
        }
        Preferences.setSimplePref(context, "1Louder-pauseuntil-" + this.ID, pauseuntil);
    }

    void pause(Context context) {
        Diagnostics.m1951d(TAG(), "pause()");
        this.timestamp_paused = System.currentTimeMillis();
        if (isRecycleable()) {
            PlacementManager.getInstance().verifyAdViews();
        }
        this._ispaused = true;
        if (this.onclosead != null) {
            AdPlacement closeplacement = PlacementManager.getInstance().getAdPlacement(context, this.onclosead, "interstitial");
            if (closeplacement != null) {
                I1LouderAdProxy closeproxy = closeplacement.getAdProxy();
                if (closeproxy != null) {
                    closeproxy.pause();
                }
            }
        }
    }

    void resume(Context context) {
        Diagnostics.m1951d(TAG(), "resume()");
        this._ispaused = false;
        if (this.timestamp > 0) {
            if (this.onetime) {
                Diagnostics.m1951d(TAG(), "onetime==true");
                return;
            }
            int interval = this.refresh_rate * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
            if (interval == 0) {
                interval = Preferences.getSimplePref(context, "ads_refresh_rate", 30) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
            }
            if (this.timestamp_paused == 0) {
                this.timestamp_paused = System.currentTimeMillis();
            }
            long remaining = ((long) interval) - (this.timestamp_paused - this.timestamp);
            if (remaining > ((long) interval)) {
                Diagnostics.m1952e(TAG(), "remaining > interval, timestamp=0");
                this.timestamp = 0;
            } else if (remaining < 0) {
                Diagnostics.m1952e(TAG(), "remaining < 0, timestamp=0");
                this.timestamp = 0;
            } else {
                Diagnostics.m1951d(TAG(), "placement.resume, remaining=" + remaining);
                this.timestamp = System.currentTimeMillis() - (((long) interval) - remaining);
            }
        }
        if (this.onclosead != null) {
            AdPlacement closeplacement = PlacementManager.getInstance().getAdPlacement(context, this.onclosead, "interstitial");
            if (closeplacement != null) {
                try {
                    closeplacement.ispaused_until(context);
                    I1LouderAdProxy closeproxy = closeplacement.getAdProxy();
                    if (closeproxy == null) {
                        if (closeplacement.getNetwork().equals("admarvel")) {
                            closeproxy = new ProxyAdMarvelView((Activity) context, closeplacement);
                        } else if (closeplacement.getNetwork().equals("googleplay")) {
                            closeproxy = new ProxyGooglePlay((Activity) context, closeplacement);
                        }
                        if (closeproxy != null) {
                            closeplacement.setAdProxy(closeproxy);
                            if (closeplacement.isTimeForNextAd(context, false)) {
                                closeplacement.requestInterstitial((Activity) context, null);
                            }
                        }
                    }
                    if (closeproxy != null) {
                        closeproxy.resume();
                    }
                } catch (Throwable e) {
                    Diagnostics.m1953e(TAG(), e);
                }
            }
        }
        this.timestamp_paused = 0;
    }

    void setAdProxy(I1LouderAdProxy proxy) {
        if (proxy == null) {
            if (this.impressionflag) {
                Diagnostics.m1951d(TAG(), "setAdProxy(null), impressionFlag still set: waiting on initial impression");
                return;
            }
            if (this.adproxy != null) {
                this.adproxy.destroy();
            }
            this.timestamp = 0;
            this.timestamp_paused = 0;
        }
        this.adproxy = proxy;
    }

    void onCloseAd(Context context) {
        if (this.onclosead != null) {
            Diagnostics.m1951d(TAG(), "onclosead placement=" + this.onclosead);
            AdPlacement closeplacement = PlacementManager.getInstance().getAdPlacement(context, this.onclosead, "interstitial");
            if (closeplacement != null && closeplacement.isTimeForNextAd(context, true)) {
                if (closeplacement.isInterstitialReady()) {
                    Intent intent = new Intent(context, AdDialog.class);
                    intent.setFlags(268435456);
                    intent.putExtra(EXTRA_1L_PLACEMENT_ID, this.onclosead);
                    context.startActivity(intent);
                    return;
                } else if (closeplacement.isInterstitialRequested()) {
                    Diagnostics.m1957w(TAG, "onclosead placement not ready");
                    return;
                } else {
                    closeplacement.requestInterstitial((Activity) context, null);
                    return;
                }
            }
            return;
        }
        Diagnostics.m1951d(TAG(), "onclosead==null");
    }

    I1LouderAdProxy getAdProxy() {
        return this.adproxy;
    }

    void setListener(AdPlacementListener listener) {
        this.mListener = listener;
    }

    void reset(Context context, long lFreq) {
        Diagnostics.m1951d(this.ID, "reset");
        if (this.sc > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("cur_");
            sb.append(this.ID);
            sb.append("_screencount");
            Preferences.setSimplePref(context, sb.toString(), 0);
            Diagnostics.m1951d(this.ID, "reset " + sb.toString());
        }
        if (this.freq > 0) {
            sb = new StringBuilder();
            sb.append("last_");
            sb.append(this.ID);
            sb.append("_timestamp");
            Preferences.setSimplePref(context, sb.toString(), lFreq);
            Diagnostics.m1951d(this.ID, "reset " + sb.toString());
        }
    }

    boolean isStillValid(Context context) {
        if (this.timestamp != 0 && this.onetime) {
            return true;
        }
        int interval = this.refresh_rate * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        if (interval == 0) {
            interval = Preferences.getSimplePref(context, "ads_refresh_rate", 30) * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH;
        }
        long remaining = ((long) interval) - (System.currentTimeMillis() - this.timestamp);
        if (remaining <= 0) {
            remaining = 0;
        }
        if (remaining <= 0) {
            return false;
        }
        return true;
    }

    boolean isTimeForNextAd(Context context, boolean bIncrement) {
        StringBuilder sb;
        Diagnostics.m1951d(TAG(), "isTimeForNextAd()");
        if (this.sc > 0) {
            sb = new StringBuilder();
            sb.append("cur_");
            sb.append(this.ID);
            sb.append("_screencount");
            int screencount = Preferences.getSimplePref(context, sb.toString(), 0) + 1;
            if (screencount == 1 && areStringsDefined()) {
                Preferences.setSimplePref(context, "1Louder-session-" + this.ID, System.currentTimeMillis());
            }
            if (screencount < this.sc) {
                Diagnostics.m1955i(TAG(), "isTimeForNextAd, " + sb + "=" + screencount + ", sc=" + this.sc);
                if (bIncrement) {
                    Preferences.setSimplePref(context, sb.toString(), screencount);
                }
                return false;
            }
        }
        if (this.freq > 0) {
            long interval;
            sb = new StringBuilder();
            if (areStringsDefined()) {
                sb.append("1Louder-session-");
                sb.append(this.ID);
            } else {
                sb.append("last_");
                sb.append(this.ID);
                sb.append("_timestamp");
            }
            if (Diagnostics.getInstance().isEnabled(4)) {
                interval = (long) ((this.freq * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) * 60);
            } else {
                interval = (long) (((this.freq * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) * 60) * 60);
            }
            long remaining = interval - (System.currentTimeMillis() - Preferences.getSimplePref(context, sb.toString(), 0));
            if (remaining <= 0) {
                remaining = 0;
            }
            if (areStringsDefined()) {
                if (remaining != 0) {
                    Diagnostics.m1955i(TAG(), "isTimeForNextAd, remaining for this session " + remaining);
                } else {
                    Diagnostics.m1955i(TAG(), "isTimeForNextAd, session expired, resetting click count");
                    reset(context, 0);
                    return false;
                }
            } else if (remaining != 0) {
                Diagnostics.m1955i(TAG(), "isTimeForNextAd, remaining=" + remaining);
                return false;
            }
        }
        if (ispaused_until(context)) {
            return false;
        }
        return true;
    }

    boolean isAlmostTimeForNextAd(Context context) {
        StringBuilder sb;
        Diagnostics.m1951d(TAG(), "isAlmostTimeForNextAd()");
        if (this.sc > 0) {
            sb = new StringBuilder();
            sb.append("cur_");
            sb.append(this.ID);
            sb.append("_screencount");
            int screencount = Preferences.getSimplePref(context, sb.toString(), 0) + 1;
            if (screencount < this.sc) {
                Diagnostics.m1955i(TAG(), "isAlmostTimeForNextAd, " + sb + "=" + screencount + ", sc=" + this.sc);
                return false;
            }
        }
        if (this.freq > 0) {
            sb = new StringBuilder();
            sb.append("last_");
            sb.append(this.ID);
            sb.append("_timestamp");
            long remaining = (((long) (((this.freq * ActiveMQPrefetchPolicy.DEFAULT_QUEUE_PREFETCH) * 60) * 60)) - (System.currentTimeMillis() - Preferences.getSimplePref(context, sb.toString(), 0))) - 600000;
            if (remaining <= 0) {
                remaining = 0;
            }
            if (remaining != 0) {
                Diagnostics.m1955i(TAG(), "isAlmostTimeForNextAd, remaining=" + remaining);
                return false;
            }
        }
        if (ispaused_until(context)) {
            return false;
        }
        return true;
    }

    boolean isInterstitialReady() {
        Diagnostics.m1951d(TAG(), "isInterstitialReady");
        if (this.adproxy != null) {
            return this.adproxy.isInterstitialReady();
        }
        Diagnostics.m1957w(TAG(), "isInterstitialReady, proxy==null");
        return false;
    }

    boolean isInterstitialRequested() {
        Diagnostics.m1951d(TAG(), "isInterstitialRequested");
        if (this.adproxy != null) {
            return this.adproxy.isInterstitialRequested();
        }
        Diagnostics.m1957w(TAG(), "isInterstitialRequested, proxy==null");
        return false;
    }

    void requestInterstitial(Activity activity, AdInterstitialListener listener) {
        Diagnostics.m1951d(TAG(), "requestInterstitial");
        this.mUserListener = listener;
        if (Utils.isNetworkConnected(activity)) {
            try {
                if (this.adproxy == null) {
                    if (this.network.equals("admarvel")) {
                        this.adproxy = new ProxyAdMarvelView(activity, this);
                    } else if (this.network.equals("googleplay")) {
                        this.adproxy = new ProxyGooglePlay(activity, this);
                    } else if (this.network.equals("psm")) {
                        this.adproxy = new ProxyPsmAdView(activity, this);
                    }
                }
                if (this.adproxy != null) {
                    String value;
                    HashMap<String, Object> targetParams = new HashMap();
                    if (listener != null) {
                        HashMap<String, String> userParams = new HashMap();
                        listener.onSetTargetParams(userParams);
                        for (String key : userParams.keySet()) {
                            value = (String) userParams.get(key);
                            if (value != null && value.length() > 0) {
                                targetParams.put(key, value);
                            }
                        }
                    }
                    Map<String, String> mediatorParams = Preferences.getMediatorArguments(activity, this.network);
                    for (String key2 : mediatorParams.keySet()) {
                        value = (String) mediatorParams.get(key2);
                        if (value != null && value.length() > 0) {
                            targetParams.put(key2, value);
                        }
                    }
                    if (!targetParams.containsKey("INT_TYPE")) {
                        targetParams.put("INT_TYPE", geIntType());
                    }
                    if (activity != null) {
                        int cardinal = Preferences.getSimplePref((Context) activity, "ads-cardinal", 0) + 1;
                        Preferences.setSimplePref((Context) activity, "ads-cardinal", cardinal);
                        targetParams.put("CARDINAL", Integer.toString(cardinal));
                    }
                    this.adproxy.requestInterstitial(activity, targetParams);
                    sendBroadcast(activity, ACTION_1L_INTERSTITIAL_REQUESTED, null);
                    SendAdUsage.trackEvent(activity, this, Constants.AD_REQUEST, targetParams, null);
                    if (this.adproxy.isInterstitialReady()) {
                        if (this.mUserListener != null) {
                            this.mUserListener.onInterstitialReceived();
                        }
                        sendBroadcast(activity, ACTION_1L_INTERSTITIAL_RECEIVED, null);
                        return;
                    }
                    this.contextRef = new SoftReference(activity);
                    this.mHandler.postDelayed(this.mReadyRunnable, 1000);
                    this.runnableCount = 1;
                    return;
                }
                return;
            } catch (Throwable e) {
                Diagnostics.m1953e(TAG(), e);
                return;
            }
        }
        Diagnostics.m1957w(TAG(), "no connectivity");
        if (this.mUserListener != null) {
            this.mUserListener.onInterstitialRequestFailed(ERRORCODE_NO_CONNECTIVITY, null);
        }
        sendBroadcast(activity, ACTION_1L_INTERSTITIAL_REQ_FAILED, null);
    }

    void displayInterstitial(Activity activity) {
        Diagnostics.m1951d(TAG(), "displayInterstitial");
        try {
            if (this.adproxy != null && this.adproxy.isInterstitialReady()) {
                this.adproxy.displayInterstitial(activity);
                if (this.mUserListener != null) {
                    this.mUserListener.onInterstitialDisplayed();
                }
                sendBroadcast(activity, ACTION_1L_INTERSTITIAL_DISPLAYED, null);
                SendAdUsage.trackEvent(activity, this, "impression", null, null);
            }
        } catch (Throwable e) {
            Diagnostics.m1953e(TAG(), e);
        }
    }

    void showNoJoyToast(Context context) {
        Diagnostics.m1957w(TAG(), "showNoJoyToast");
        if (context != null) {
            String text = getString(context, "nojoy");
            if (text == null || text.length() <= 0) {
                Diagnostics.m1957w(TAG(), "showNoJoyToast, string not defined");
                return;
            } else {
                this.mHandler.post(new C12841(context, text));
                return;
            }
        }
        Diagnostics.m1957w(TAG(), "showNoJoyToast, context==null");
    }

    protected void onInterstitialClosed(Context context, HashMap<String, Object> params) {
        if (this.mUserListener != null) {
            this.mUserListener.onInterstitialClosed(params);
        }
        sendBroadcast(context, ACTION_1L_INTERSTITIAL_CLOSED, params);
    }

    void prefetch(Context context, HashMap<String, Object> hashMap) {
    }
}
