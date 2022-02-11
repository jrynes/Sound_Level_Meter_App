package com.amazon.device.ads;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.amazon.device.ads.AdEvent.AdEventType;
import com.amazon.device.ads.InAppBrowser.InAppBrowserBuilder;
import com.amazon.device.ads.JavascriptInteractor.Executor;
import com.amazon.device.ads.JavascriptInteractor.JavascriptMethodExecutor;
import com.amazon.device.ads.ThreadUtils.ExecutionStyle;
import com.amazon.device.ads.ThreadUtils.ExecutionThread;
import com.amazon.device.ads.ThreadUtils.ThreadRunner;
import com.amazon.device.ads.WebRequest.WebRequestException;
import com.amazon.device.ads.WebRequest.WebRequestFactory;
import com.amazon.device.ads.WebRequest.WebResponse;
import com.nextradioapp.androidSDK.actions.CalendarEventAction;
import com.nextradioapp.androidSDK.data.schema.Tables.activityEvents;
import com.nextradioapp.androidSDK.data.schema.Tables.stationReporting;
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;
import java.util.Locale;
import org.apache.activemq.transport.stomp.Stomp;
import org.json.JSONException;
import org.json.JSONObject;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xbill.DNS.Zone;

class MRAIDAdSDKBridge implements AdSDKBridge {
    private static final int CLOSE_BUTTON_SIZE = 50;
    private static final String CONTENT_DESCRIPTION_AD_CONTAINER_VIEW = "adContainerView";
    private static final String CONTENT_DESCRIPTION_DIMMING_VIEW = "dimmingView";
    private static final String CONTENT_DESCRIPTION_EXPANSION_VIEW = "expansionView";
    private static final String CONTENT_DESCRIPTION_RESIZED_VIEW = "resizedView";
    private static final String ERROR_EVENT_FORMAT = "mraidBridge.error('%s', '%s');";
    private static final String JAVASCRIPT;
    private static final String LOGTAG;
    private static final String MRAID_BRIDGE_NAME = "mraidObject";
    private static final String PLACEMENT_TYPE_INLINE = "inline";
    private static final String PLACEMENT_TYPE_INTERSTITIAL = "interstitial";
    private ViewGroup adContainerView;
    private final AdControlAccessor adControlAccessor;
    private final AdUtils2 adUtils;
    private final AlertDialogFactory alertDialogFactory;
    private final Position defaultPosition;
    private final ExpandProperties expandProperties;
    private boolean expandedWithUrl;
    private ViewGroup expansionView;
    private final GraphicsUtils graphicsUtils;
    private final JavascriptInteractor javascriptInteractor;
    private final MobileAdsLogger logger;
    private final OrientationProperties orientationProperties;
    private final PermissionChecker permissionChecker;
    private ResizeProperties resizeProperties;
    private ViewGroup resizedView;
    private FrameLayout rootView;
    private SDKEventListener sdkEventListener;
    private final ThreadRunner threadRunner;
    private final WebRequestFactory webRequestFactory;
    private final WebUtils2 webUtils;

    class 10 implements OnGlobalLayoutListener {
        private boolean triggered;

        10() {
            this.triggered = false;
        }

        public void onGlobalLayout() {
            if (!this.triggered) {
                this.triggered = true;
                MRAIDAdSDKBridge.this.logger.m637d("Expand ViewTreeObserver fired");
                MRAIDAdSDKBridge.this.adControlAccessor.fireAdEvent(new AdEvent(AdEventType.EXPANDED));
                MRAIDAdSDKBridge.this.adControlAccessor.injectJavascript("mraidBridge.stateChange('expanded');");
                MRAIDAdSDKBridge.this.reportSizeChangeEvent();
                MRAIDAdSDKBridge.this.orientationPropertyChange();
            }
        }
    }

    class 11 implements Runnable {
        final /* synthetic */ ResizeProperties val$resizeProperties;
        final /* synthetic */ Size val$resizeSize;

        11(ResizeProperties resizeProperties, Size size) {
            this.val$resizeProperties = resizeProperties;
            this.val$resizeSize = size;
        }

        public void run() {
            MRAIDAdSDKBridge.this.resizeAdOnThread(this.val$resizeProperties, this.val$resizeSize);
        }
    }

    class 12 implements OnGlobalLayoutListener {
        private boolean triggered;

        12() {
            this.triggered = false;
        }

        public void onGlobalLayout() {
            if (!this.triggered) {
                this.triggered = true;
                int[] onScreen = new int[2];
                MRAIDAdSDKBridge.this.resizedView.getLocationOnScreen(onScreen);
                Rect positionOnScreen = new Rect(onScreen[0], onScreen[1], onScreen[0] + MRAIDAdSDKBridge.this.resizedView.getWidth(), onScreen[1] + MRAIDAdSDKBridge.this.resizedView.getHeight());
                AdEvent adEvent = new AdEvent(AdEventType.RESIZED);
                adEvent.setParameter(AdEvent.POSITION_ON_SCREEN, positionOnScreen);
                MRAIDAdSDKBridge.this.adControlAccessor.fireAdEvent(adEvent);
                MRAIDAdSDKBridge.this.adControlAccessor.injectJavascript("mraidBridge.stateChange('resized');");
                MRAIDAdSDKBridge.this.reportSizeChangeEvent();
            }
        }
    }

    static /* synthetic */ class 13 {
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$ForceOrientation;
        static final /* synthetic */ int[] $SwitchMap$com$amazon$device$ads$RelativePosition;

        static {
            $SwitchMap$com$amazon$device$ads$ForceOrientation = new int[ForceOrientation.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$ForceOrientation[ForceOrientation.PORTRAIT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$ForceOrientation[ForceOrientation.LANDSCAPE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$ForceOrientation[ForceOrientation.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$amazon$device$ads$RelativePosition = new int[RelativePosition.values().length];
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.TOP_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.TOP_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.TOP_CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.BOTTOM_LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.BOTTOM_RIGHT.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.BOTTOM_CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$com$amazon$device$ads$RelativePosition[RelativePosition.CENTER.ordinal()] = 7;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.1 */
    class C03171 implements PreloadCallback {
        final /* synthetic */ ExpandProperties val$expandProperties;

        C03171(ExpandProperties expandProperties) {
            this.val$expandProperties = expandProperties;
        }

        public void onPreloadComplete(String url) {
            MRAIDAdSDKBridge.this.adControlAccessor.injectJavascriptPreload("mraidBridge.stateChange('expanded');");
            MRAIDAdSDKBridge.this.adControlAccessor.injectJavascriptPreload("mraidBridge.ready();");
            MRAIDAdSDKBridge.this.expandAd(this.val$expandProperties, url);
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.2 */
    class C03182 implements Runnable {
        final /* synthetic */ String val$url;

        C03182(String str) {
            this.val$url = str;
        }

        public void run() {
            MRAIDAdSDKBridge.this.fetchPicture(this.val$url);
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.3 */
    class C03193 implements Runnable {
        final /* synthetic */ Bitmap val$bitmap;

        C03193(Bitmap bitmap) {
            this.val$bitmap = bitmap;
        }

        public void run() {
            MRAIDAdSDKBridge.this.savePicture(this.val$bitmap);
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.4 */
    class C03204 implements OnClickListener {
        final /* synthetic */ Bitmap val$bitmap;

        C03204(Bitmap bitmap) {
            this.val$bitmap = bitmap;
        }

        public void onClick(DialogInterface dialog, int which) {
            if (StringUtils.isNullOrEmpty(MRAIDAdSDKBridge.this.graphicsUtils.insertImageInMediaStore(MRAIDAdSDKBridge.this.getContext(), this.val$bitmap, "AdImage", "Image created by rich media ad."))) {
                MRAIDAdSDKBridge.this.fireErrorEvent("Picture could not be stored to device.", "storePicture");
                return;
            }
            MediaScannerConnection.scanFile(MRAIDAdSDKBridge.this.getContext(), new String[]{insertUrl}, null, null);
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.5 */
    class C03215 implements OnClickListener {
        C03215() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.6 */
    class C03226 implements Runnable {
        final /* synthetic */ AdControlAccessor val$adControlAccessor;

        C03226(AdControlAccessor adControlAccessor) {
            this.val$adControlAccessor = adControlAccessor;
        }

        public void run() {
            MRAIDAdSDKBridge.this.collapseExpandedAdOnThread(this.val$adControlAccessor);
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.7 */
    class C03237 implements OnGlobalLayoutListener {
        private boolean triggered;
        final /* synthetic */ AdControlAccessor val$adControlAccessor;

        C03237(AdControlAccessor adControlAccessor) {
            this.val$adControlAccessor = adControlAccessor;
            this.triggered = false;
        }

        public void onGlobalLayout() {
            if (!this.triggered) {
                this.triggered = true;
                this.val$adControlAccessor.fireAdEvent(new AdEvent(AdEventType.CLOSED));
                this.val$adControlAccessor.injectJavascript("mraidBridge.stateChange('default');");
                MRAIDAdSDKBridge.this.reportSizeChangeEvent();
            }
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.8 */
    class C03248 implements OnTouchListener {
        C03248() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }

    /* renamed from: com.amazon.device.ads.MRAIDAdSDKBridge.9 */
    class C03259 implements Runnable {
        final /* synthetic */ ExpandProperties val$expandProperties;
        final /* synthetic */ String val$url;

        C03259(ExpandProperties expandProperties, String str) {
            this.val$expandProperties = expandProperties;
            this.val$url = str;
        }

        public void run() {
            MRAIDAdSDKBridge.this.expandAdOnThread(this.val$expandProperties, this.val$url);
        }
    }

    static class AlertDialogFactory {
        AlertDialogFactory() {
        }

        public Builder createBuilder(Context context) {
            return new Builder(context);
        }
    }

    private static class CloseJSIF extends JavascriptMethodExecutor {
        private static final String name = "Close";
        private final MRAIDAdSDKBridge bridge;

        public CloseJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.close();
            return null;
        }
    }

    private static class CreateCalendarEventJSIF extends JavascriptMethodExecutor {
        private static final String name = "CreateCalendarEvent";
        private final MRAIDAdSDKBridge bridge;

        public CreateCalendarEventJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.createCalendarEvent(JSONUtils.getStringFromJSON(parameters, activityEvents.description, null), JSONUtils.getStringFromJSON(parameters, "location", null), JSONUtils.getStringFromJSON(parameters, "summary", null), JSONUtils.getStringFromJSON(parameters, "start", null), JSONUtils.getStringFromJSON(parameters, Stomp.END, null));
            return null;
        }
    }

    private static class ExpandJSIF extends JavascriptMethodExecutor {
        private static final String name = "Expand";
        private final MRAIDAdSDKBridge bridge;

        public ExpandJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.expand(JSONUtils.getStringFromJSON(parameters, SettingsJsonConstants.APP_URL_KEY, null));
            return null;
        }
    }

    private static class GetCurrentPositionJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetCurrentPosition";
        private final MRAIDAdSDKBridge bridge;

        public GetCurrentPositionJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getCurrentPosition();
        }
    }

    private static class GetDefaultPositionJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetDefaultPosition";
        private final MRAIDAdSDKBridge bridge;

        public GetDefaultPositionJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getDefaultPosition();
        }
    }

    private static class GetExpandPropertiesJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetExpandProperties";
        private final MRAIDAdSDKBridge bridge;

        public GetExpandPropertiesJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getExpandProperties();
        }
    }

    private static class GetMaxSizeJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetMaxSize";
        private final MRAIDAdSDKBridge bridge;

        public GetMaxSizeJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getMaxSize();
        }
    }

    private static class GetPlacementTypeJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetPlacementType";
        private final MRAIDAdSDKBridge bridge;

        public GetPlacementTypeJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            JSONObject json = new JSONObject();
            JSONUtils.put(json, "placementType", this.bridge.getPlacementType());
            return json;
        }
    }

    private static class GetResizePropertiesJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetResizeProperties";
        private final MRAIDAdSDKBridge bridge;

        public GetResizePropertiesJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getResizeProperties();
        }
    }

    private static class GetScreenSizeJSIF extends JavascriptMethodExecutor {
        private static final String name = "GetScreenSize";
        private final MRAIDAdSDKBridge bridge;

        public GetScreenSizeJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getScreenSize();
        }
    }

    private static class OpenJSIF extends JavascriptMethodExecutor {
        private static final String name = "Open";
        private final MRAIDAdSDKBridge bridge;

        public OpenJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.open(JSONUtils.getStringFromJSON(parameters, SettingsJsonConstants.APP_URL_KEY, null));
            return null;
        }
    }

    private static class PlayVideoJSIF extends JavascriptMethodExecutor {
        private static final String name = "PlayVideo";
        private final MRAIDAdSDKBridge bridge;

        public PlayVideoJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.playVideo(JSONUtils.getStringFromJSON(parameters, SettingsJsonConstants.APP_URL_KEY, null));
            return null;
        }
    }

    private static class ResizeJSIF extends JavascriptMethodExecutor {
        private static final String name = "Resize";
        private final MRAIDAdSDKBridge bridge;

        public ResizeJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.resize();
            return null;
        }
    }

    private static class SetExpandPropertiesJSIF extends JavascriptMethodExecutor {
        private static final String name = "SetExpandProperties";
        private final MRAIDAdSDKBridge bridge;

        public SetExpandPropertiesJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.setExpandProperties(JSONUtils.getIntegerFromJSON(parameters, SettingsJsonConstants.ICON_WIDTH_KEY, 0), JSONUtils.getIntegerFromJSON(parameters, SettingsJsonConstants.ICON_HEIGHT_KEY, 0), JSONUtils.getBooleanFromJSON(parameters, "useCustomClose", false));
            return null;
        }
    }

    private static class SetOrientationPropertiesJSIF extends JavascriptMethodExecutor {
        private static final String name = "SetOrientationProperties";
        private final MRAIDAdSDKBridge bridge;

        public SetOrientationPropertiesJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.setOrientationProperties(JSONUtils.getBooleanFromJSON(parameters, "allowOrientationChange", false), JSONUtils.getStringFromJSON(parameters, "forceOrientation", null));
            return null;
        }
    }

    private static class SetResizePropertiesJSIF extends JavascriptMethodExecutor {
        private static final String name = "SetResizeProperties";
        private final MRAIDAdSDKBridge bridge;

        public SetResizePropertiesJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.setResizeProperties(JSONUtils.getIntegerFromJSON(parameters, SettingsJsonConstants.ICON_WIDTH_KEY, 0), JSONUtils.getIntegerFromJSON(parameters, SettingsJsonConstants.ICON_HEIGHT_KEY, 0), JSONUtils.getIntegerFromJSON(parameters, "offsetX", 0), JSONUtils.getIntegerFromJSON(parameters, "offsetY", 0), JSONUtils.getStringFromJSON(parameters, "customClosePosition", null), JSONUtils.getBooleanFromJSON(parameters, "allowOffscreen", false));
            return null;
        }
    }

    private static class StorePictureJSIF extends JavascriptMethodExecutor {
        private static final String name = "StorePicture";
        private final MRAIDAdSDKBridge bridge;

        public StorePictureJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.storePicture(JSONUtils.getStringFromJSON(parameters, SettingsJsonConstants.APP_URL_KEY, null));
            return null;
        }
    }

    private static class SupportsJSIF extends JavascriptMethodExecutor {
        private static final String name = "Supports";
        private final MRAIDAdSDKBridge bridge;

        public SupportsJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            return this.bridge.getSupportedFeatures();
        }
    }

    private static class UseCustomCloseJSIF extends JavascriptMethodExecutor {
        private static final String name = "UseCustomClose";
        private final MRAIDAdSDKBridge bridge;

        public UseCustomCloseJSIF(MRAIDAdSDKBridge bridge) {
            super(name);
            this.bridge = bridge;
        }

        public JSONObject execute(JSONObject parameters) {
            this.bridge.setUseCustomClose(JSONUtils.getBooleanFromJSON(parameters, "useCustomClose", false));
            return null;
        }
    }

    static {
        LOGTAG = MRAIDAdSDKBridge.class.getSimpleName();
        JAVASCRIPT = "(function (window, console) {\n    var is_array = function (obj) {\n        return Object.prototype.toString.call(obj) === '[object Array]';\n    },\n    forEach = function (array, fn) {\n        var i;\n        for (i = 0; i < array.length; i++) {\n            if (i in array) {\n                fn.call(null, array[i], i);\n            }\n        }\n    },\n    events = {\n            error: 'error',\n            ready: 'ready',\n            sizeChange: 'sizeChange',\n            stateChange: 'stateChange',\n            viewableChange: 'viewableChange'\n    },\n    states = [\"loading\",\"default\",\"expanded\",\"resized\",\"hidden\"],\n    placementTypes = [\"inline\", \"interstitial\"],\n    listeners = [],\n    version = '2.0',\n    currentState = \"loading\",\n    supportedFeatures = null,\n    orientationProperties = {\"allowOrientationChange\":true,\"forceOrientation\":\"none\"},\n    // Error Event fires listeners\n    invokeListeners = function(event, args) {\n        var eventListeners = listeners[event] || [];\n        // fire all the listeners\n        forEach(eventListeners, function(listener){\n            try {\n                listener.apply(null, args);\n            }catch(e){\n                debug(\"Error executing \" + event + \" listener\");\n                debug(e);\n            }\n        });\n    },\n    debug = function(msg) {\n        console.log(\"MRAID log: \" + msg);\n    },\n    readyEvent = function() {\n        debug(\"MRAID ready\");\n        invokeListeners(\"ready\");\n    },\n    errorEvent = function(message, action) {\n        debug(\"error: \" + message + \" action: \" + action);\n        var args = [message, action];\n        invokeListeners(\"error\", args);\n    },\n    stateChangeEvent = function(state) {\n        debug(\"stateChange: \" + state);\n        var args = [state];\n        currentState = state;\n        invokeListeners(\"stateChange\", args);\n    },\n    viewableChangeEvent = function(viewable) {\n        debug(\"viewableChange: \" + viewable);\n        var args = [viewable];\n        invokeListeners(\"viewableChange\", args);\n    }, \n    sizeChangeEvent = function(width, height) {\n        debug(\"sizeChange: \" + width + \"x\" + height);\n        var args = [width, height];\n        invokeListeners(\"sizeChange\", args);\n    };\n    window.mraidBridge = {\n            error : errorEvent,\n            ready : readyEvent,\n            stateChange : stateChangeEvent,\n            sizeChange : sizeChangeEvent,\n            viewableChange : viewableChangeEvent\n    };\n    // Define the mraid object\n    window.mraid = {\n            // Command Flow\n            addEventListener : function(event, listener){\n                var eventListeners = listeners[event] || [],\n                alreadyRegistered = false;\n                \n                //verify the event is one that will actually occur\n                if (!events.hasOwnProperty(event)){\n                    return;\n                }\n                \n                //register first set of listeners for this event\n                if (!is_array(listeners[event])) {\n                    listeners[event] = eventListeners;\n                }\n                \n                forEach(eventListeners, function(l){ \n                    // Listener already registered, so no need to add it.\n                        if (listener === l){\n                            alreadyRegistered = true;\n                        }\n                    }\n                );\n                if (!alreadyRegistered){\n                    listeners[event].push(listener);\n                }\n            },\n            removeEventListener : function(event, listener){\n                if (listeners.hasOwnProperty(event)) {\n                    var eventListeners = listeners[event];\n                    if (eventListeners) {\n                        var idx = eventListeners.indexOf(listener);\n                        if (idx !== -1) {\n                            eventListeners.splice(idx, 1);\n                        }\n                    }\n                }\n            },\n            useCustomClose: function(bool){\n                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"UseCustomClose\", JSON.stringify({useCustomClose: bool}));\n" + "            },\n" + "            // Support\n" + "            supports: function(feature){\n" + "                if (!supportedFeatures)\n" + "                {\n" + "                    supportedFeatures = JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"Supports\", null));\n" + "                }\n" + "                return supportedFeatures[feature];\n" + "            },\n" + "            // Properties\n" + "            getVersion: function(){\n" + "                return version;\n" + "            },\n" + "            getState: function(){\n" + "                return currentState;\n" + "            },\n" + "            getPlacementType: function(){\n" + "                var json = JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetPlacementType\", null));\n" + "                return json.placementType;\n" + "            },\n" + "            isViewable: function(){\n" + "                // TODO - should we ask the OS if the widget is visible.\n" + "                return currentState !== \"hidden\" && currentState !== \"loading\";\n" + "            },\n" + "            getExpandProperties: function(){\n" + "                return JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetExpandProperties\", null));\n" + "            },\n" + "            setExpandProperties: function(properties){\n" + "                var currentProperties = mraid.getExpandProperties(),\n" + "                //properties aren't all required, map to existing value if not set.\n" + "                width = properties.hasOwnProperty('width') ? properties.width : currentProperties.width,\n" + "                height = properties.hasOwnProperty('height') ? properties.height : currentProperties.height,\n" + "                useClose = properties.hasOwnProperty('useCustomClose') ? properties.useCustomClose : currentProperties.useCustomClose;\n" + "                //Backwards compatibility with MRAID 1.0 creatives\n" + "                if (!!properties.lockOrientation){\n" + "                    mraid.setOrientationProperties({\"allowOrientationChange\":false});\n" + "                }\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"SetExpandProperties\", JSON.stringify({\n" + "                        width: width, \n" + "                        height: height, \n" + "                        useClose: useClose}));\n" + "            },\n" + "            getOrientationProperties: function(){\n" + "                return orientationProperties;\n" + "            },\n" + "            setOrientationProperties: function(properties){\n" + "                if (properties.hasOwnProperty(\"allowOrientationChange\")) {\n" + "                    orientationProperties.allowOrientationChange = properties.allowOrientationChange;\n" + "                }\n" + "                if (properties.hasOwnProperty(\"forceOrientation\")) {\n" + "                    orientationProperties.forceOrientation = properties.forceOrientation;\n" + "                }\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"SetOrientationProperties\", JSON.stringify({\n" + "                        allowOrientationChange: orientationProperties.allowOrientationChange, \n" + "                        forceOrientation: orientationProperties.forceOrientation}));\n" + "            },\n" + "            getResizeProperties: function(){\n" + "                return JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetResizeProperties\", null));\n" + "            },\n" + "            setResizeProperties: function(properties){\n" + "                if (!properties.customClosePosition){\n" + "                   properties.customClosePosition = null;\n" + "                }\n" + "                if (!properties.hasOwnProperty('allowOffscreen')){\n" + "                   properties.allowOffscreen = true;\n" + "                }\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"SetResizeProperties\", JSON.stringify({\n" + "                        width: properties.width, \n" + "                        height: properties.height, \n" + "                        offsetX: properties.offsetX, \n" + "                        offsetY: properties.offsetY, \n" + "                        customClosePosition: properties.customClosePosition, \n" + "                        allowOffscreen: properties.allowOffscreen}));\n" + "            },\n" + "            getCurrentPosition: function(){\n" + "                return JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetCurrentPosition\", null));\n" + "            },\n" + "            getMaxSize: function(){\n" + "                return JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetMaxSize\", null));\n" + "            },\n" + "            getDefaultPosition: function(){\n" + "                return JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetDefaultPosition\", null));\n" + "            },\n" + "            getScreenSize: function(){\n" + "                return JSON.parse(mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"GetScreenSize\", null));\n" + "            },\n" + "            // Operations\n" + "            open: function(url) {\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"Open\", JSON.stringify({url: url}));\n" + "            },\n" + "            close: function() {\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"Close\", null);\n" + "            },\n" + "            expand: function(url) {\n" + "                if (url !== undefined) {\n" + "                    mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"Expand\", JSON.stringify({url: url}));\n" + "                } else {\n" + "                    mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"Expand\", JSON.stringify({url: \"\"}));\n" + "                }\n" + "            },\n" + "            resize: function() {\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"Resize\", null);\n" + "            },\n" + "            createCalendarEvent: function(eventObject) {\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"CreateCalendarEvent\", JSON.stringify({\n" + "                        description: eventObject.description || null, \n" + "                        location: eventObject.customClosePosition || null, \n" + "                        summary: eventObject.summary || null, \n" + "                        start: eventObject.start || null, \n" + "                        end: eventObject.end || null}));\n" + "            },\n" + "            playVideo: function(url){\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"PlayVideo\", JSON.stringify({url: url}));\n" + "            },\n" + "            storePicture: function(url){\n" + "                mraidObject." + JavascriptInteractor.getExecutorMethodName() + "(\"StorePicture\", JSON.stringify({url: url}));\n" + "            }\n" + "    };\n" + "})(window, console);\n" + Stomp.EMPTY;
    }

    MRAIDAdSDKBridge(AdControlAccessor adControlAccessor, JavascriptInteractor javascriptInteractor) {
        this(adControlAccessor, javascriptInteractor, new PermissionChecker(), new MobileAdsLoggerFactory(), new WebRequestFactory(), ThreadUtils.getThreadRunner(), new GraphicsUtils(), new AlertDialogFactory(), new WebUtils2(), new AdUtils2());
    }

    MRAIDAdSDKBridge(AdControlAccessor adControlAccessor, JavascriptInteractor javascriptInteractor, PermissionChecker permissionChecker, MobileAdsLoggerFactory loggerFactory, WebRequestFactory webRequestFactory, ThreadRunner threadRunner, GraphicsUtils graphicsUtils, AlertDialogFactory alertDialogFactory, WebUtils2 webUtils, AdUtils2 adUtils) {
        this.orientationProperties = new OrientationProperties();
        this.expandProperties = new ExpandProperties();
        this.defaultPosition = new Position();
        this.expandedWithUrl = true;
        this.adControlAccessor = adControlAccessor;
        this.javascriptInteractor = javascriptInteractor;
        this.logger = loggerFactory.createMobileAdsLogger(LOGTAG);
        this.permissionChecker = permissionChecker;
        this.webRequestFactory = webRequestFactory;
        this.threadRunner = threadRunner;
        this.graphicsUtils = graphicsUtils;
        this.alertDialogFactory = alertDialogFactory;
        this.webUtils = webUtils;
        this.adUtils = adUtils;
        populateJavascriptExecutorsInInteractor();
    }

    private void populateJavascriptExecutorsInInteractor() {
        this.javascriptInteractor.addMethodExecutor(new CloseJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new CreateCalendarEventJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new ExpandJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetCurrentPositionJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetDefaultPositionJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetExpandPropertiesJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetMaxSizeJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetPlacementTypeJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetResizePropertiesJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new GetScreenSizeJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new OpenJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new PlayVideoJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new ResizeJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new SetExpandPropertiesJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new SetOrientationPropertiesJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new SetResizePropertiesJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new StorePictureJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new SupportsJSIF(this));
        this.javascriptInteractor.addMethodExecutor(new UseCustomCloseJSIF(this));
    }

    public boolean hasNativeExecution() {
        return true;
    }

    public Executor getJavascriptInteractorExecutor() {
        return this.javascriptInteractor.getExecutor();
    }

    public String getName() {
        return MRAID_BRIDGE_NAME;
    }

    public String getJavascript() {
        return JAVASCRIPT;
    }

    public SDKEventListener getSDKEventListener() {
        if (this.sdkEventListener == null) {
            this.sdkEventListener = new MRAIDAdSDKEventListener(this);
        }
        return this.sdkEventListener;
    }

    private Context getContext() {
        return this.adControlAccessor.getContext();
    }

    void updateExpandProperties(int width, int height) {
        this.expandProperties.setWidth(width);
        this.expandProperties.setHeight(height);
    }

    void updateDefaultPosition(int width, int height, int offsetX, int offsetY) {
        this.defaultPosition.setSize(new Size(width, height));
        this.defaultPosition.setX(offsetX);
        this.defaultPosition.setY(offsetY);
    }

    public JSONObject getCurrentPosition() {
        if (this.adControlAccessor.getCurrentPosition() != null) {
            return this.adControlAccessor.getCurrentPosition().toJSONObject();
        }
        fireErrorEvent("Current position is unavailable because the ad has not yet been displayed.", "getCurrentPosition");
        return new Position(new Size(0, 0), 0, 0).toJSONObject();
    }

    public JSONObject getDefaultPosition() {
        return this.defaultPosition.toJSONObject();
    }

    public JSONObject getMaxSize() {
        Size maxSize = this.adControlAccessor.getMaxSize();
        if (maxSize == null) {
            return new Size(0, 0).toJSONObject();
        }
        return maxSize.toJSONObject();
    }

    public JSONObject getScreenSize() {
        Size screenSize = this.adControlAccessor.getScreenSize();
        if (screenSize == null) {
            return new Size(0, 0).toJSONObject();
        }
        return screenSize.toJSONObject();
    }

    public String getPlacementType() {
        if (this.adControlAccessor.isInterstitial()) {
            return PLACEMENT_TYPE_INTERSTITIAL;
        }
        return PLACEMENT_TYPE_INLINE;
    }

    public String getOrientationProperties() {
        return this.orientationProperties.toString();
    }

    public void setOrientationProperties(boolean allowOrientationChange, String forceOrientation) {
        if (this.adControlAccessor.isInterstitial() && !this.adControlAccessor.isModal()) {
            this.adControlAccessor.orientationChangeAttemptedWhenNotAllowed();
        }
        this.orientationProperties.setAllowOrientationChange(Boolean.valueOf(allowOrientationChange));
        if (!StringUtils.isNullOrEmpty(forceOrientation)) {
            try {
                this.orientationProperties.setForceOrientation(ForceOrientation.valueOf(forceOrientation.toUpperCase(Locale.US)));
            } catch (IllegalArgumentException e) {
                this.logger.m645w("Not a valid orientation to force:" + forceOrientation);
            }
        }
        orientationPropertyChange();
    }

    public JSONObject getExpandProperties() {
        return this.expandProperties.toJSONObject();
    }

    public void setExpandProperties(int width, int height, boolean useCustomClose) {
        this.expandProperties.setWidth(width);
        this.expandProperties.setHeight(height);
        setUseCustomClose(useCustomClose);
    }

    public JSONObject getResizeProperties() {
        return this.resizeProperties.toJSONObject();
    }

    public void setResizeProperties(int width, int height, int offsetX, int offsetY, String customClosePosition, boolean allowOffscreen) {
        if (this.resizeProperties == null) {
            this.resizeProperties = new ResizeProperties();
        }
        this.resizeProperties.setAllowOffscreen(Boolean.valueOf(allowOffscreen));
        this.resizeProperties.setCustomClosePosition(customClosePosition);
        this.resizeProperties.setWidth(width);
        this.resizeProperties.setHeight(height);
        this.resizeProperties.setOffsetX(offsetX);
        this.resizeProperties.setOffsetY(offsetY);
    }

    public void setUseCustomClose(boolean useCustomClose) {
        this.expandProperties.setUseCustomClose(Boolean.valueOf(useCustomClose));
        this.adControlAccessor.showNativeCloseButtonImage(!useCustomClose);
    }

    public void close() {
        if (!this.adControlAccessor.closeAd()) {
            fireErrorEvent("Unable to close ad in its current state.", "close");
        }
    }

    public void expand(String url) {
        if (this.adControlAccessor.isInterstitial()) {
            fireErrorEvent("Unable to expand an interstitial ad placement", "expand");
        } else if (this.adControlAccessor.isModal()) {
            fireErrorEvent("Unable to expand while expanded.", "expand");
        } else if (!this.adControlAccessor.isVisible()) {
            fireErrorEvent("Unable to expand ad while it is not visible.", "expand");
        } else if (this.expandProperties.getWidth() < CLOSE_BUTTON_SIZE || this.expandProperties.getHeight() < CLOSE_BUTTON_SIZE) {
            fireErrorEvent("Expand size is too small, must leave room for close.", "expand");
        } else {
            ExpandProperties expandProperties = this.expandProperties;
            if (StringUtils.isNullOrWhiteSpace(url)) {
                expandAd(expandProperties, null);
            } else if (this.webUtils.isUrlValid(url)) {
                this.adControlAccessor.preloadUrl(url, new C03171(expandProperties));
            } else {
                fireErrorEvent("Unable to expand with invalid URL.", "expand");
            }
        }
    }

    public void resize() {
        if (this.adControlAccessor.isInterstitial()) {
            fireErrorEvent("Unable to resize an interstitial ad placement.", "resize");
        } else if (this.adControlAccessor.isModal()) {
            fireErrorEvent("Unable to resize while expanded.", "resize");
        } else if (!this.adControlAccessor.isVisible()) {
            fireErrorEvent("Unable to resize ad while it is not visible.", "resize");
        } else if (this.resizeProperties == null) {
            fireErrorEvent("Resize properties must be set before calling resize.", "resize");
        } else if (this.resizeProperties.getWidth() < CLOSE_BUTTON_SIZE || this.resizeProperties.getHeight() < CLOSE_BUTTON_SIZE) {
            fireErrorEvent("Resize width and height must be at least 50 dp in order to fit the close button.", "resize");
        } else {
            resizeAd(this.resizeProperties);
        }
    }

    public void playVideo(String url) {
        if (!this.adControlAccessor.isVisible()) {
            fireErrorEvent("Unable to play a video while the ad is not visible", "playVideo");
        } else if (StringUtils.isNullOrEmpty(url)) {
            fireErrorEvent("Unable to play a video without a URL", "playVideo");
        } else {
            try {
                Bundle data = new Bundle();
                data.putString(SettingsJsonConstants.APP_URL_KEY, url);
                Intent intent = new Intent(getContext(), AdActivity.class);
                intent.putExtra("adapter", VideoActionHandler.class.getName());
                intent.putExtras(data);
                getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                this.logger.m637d("Failed to open VideoAction activity");
                fireErrorEvent("Internal SDK Failure. Unable to launch VideoActionHandler", "playVideo");
            }
        }
    }

    public void open(String url) {
        if (this.adControlAccessor.isVisible()) {
            this.logger.m637d("Opening URL " + url);
            if (this.webUtils.isUrlValid(url)) {
                String scheme = WebUtils.getScheme(url);
                if ("http".equals(scheme) || "https".equals(scheme)) {
                    new InAppBrowserBuilder().withContext(getContext()).withExternalBrowserButton().withUrl(url).show();
                    return;
                } else {
                    this.adControlAccessor.loadUrl(url);
                    return;
                }
            }
            String message = "URL " + url + " is not a valid URL";
            this.logger.m637d(message);
            fireErrorEvent(message, "open");
            return;
        }
        fireErrorEvent("Unable to open a URL while the ad is not visible", "open");
    }

    public JSONObject getSupportedFeatures() {
        JSONObject json = new JSONObject();
        try {
            json.put(AdWebViewClient.SMS, getContext().getPackageManager().hasSystemFeature("android.hardware.telephony"));
            json.put(AdWebViewClient.TELEPHONE, getContext().getPackageManager().hasSystemFeature("android.hardware.telephony"));
            json.put(CalendarEventAction.ACTION_CALENDAR, AndroidTargetUtils.isAtLeastAndroidAPI(14));
            json.put("storePicture", this.permissionChecker.hasWriteExternalStoragePermission(getContext()));
            json.put("inlineVideo", AndroidTargetUtils.isAtLeastAndroidAPI(11));
        } catch (JSONException e) {
        }
        return json;
    }

    public void createCalendarEvent(String description, String location, String summary, String start, String end) {
        if (AndroidTargetUtils.isAtLeastAndroidAPI(14)) {
            try {
                createCalendarIntent(new CalendarEventParameters(description, location, summary, start, end));
                return;
            } catch (IllegalArgumentException e) {
                this.logger.m637d(e.getMessage());
                fireErrorEvent(e.getMessage(), "createCalendarEvent");
                return;
            }
        }
        String message = "API version does not support calendar operations.";
        this.logger.m637d("API version does not support calendar operations.");
        fireErrorEvent("API version does not support calendar operations.", "createCalendarEvent");
    }

    @TargetApi(14)
    private void createCalendarIntent(CalendarEventParameters parameters) {
        Intent calendarIntent = new Intent("android.intent.action.INSERT").setType("vnd.android.cursor.item/event");
        calendarIntent.putExtra(SettingsJsonConstants.PROMPT_TITLE_KEY, parameters.getDescription());
        if (!StringUtils.isNullOrEmpty(parameters.getLocation())) {
            calendarIntent.putExtra("eventLocation", parameters.getLocation());
        }
        if (!StringUtils.isNullOrEmpty(parameters.getSummary())) {
            calendarIntent.putExtra(activityEvents.description, parameters.getSummary());
        }
        calendarIntent.putExtra("beginTime", parameters.getStart().getTime());
        if (parameters.getEnd() != null) {
            calendarIntent.putExtra(stationReporting.endTime, parameters.getEnd().getTime());
        }
        getContext().startActivity(calendarIntent);
    }

    public void storePicture(String url) {
        if (this.permissionChecker.hasWriteExternalStoragePermission(getContext())) {
            this.threadRunner.execute(new C03182(url), ExecutionStyle.RUN_ASAP, ExecutionThread.BACKGROUND_THREAD);
        } else {
            fireErrorEvent("Picture could not be stored because permission was denied.", "storePicture");
        }
    }

    private void fetchPicture(String url) {
        WebRequest webRequest = this.webRequestFactory.createWebRequest();
        webRequest.enableLog(true);
        webRequest.setUrlString(url);
        try {
            WebResponse response = webRequest.makeCall();
            if (response == null) {
                fireErrorEvent("Server could not be contacted to download picture.", "storePicture");
                return;
            }
            Bitmap bitmap = new ImageResponseReader(response.getResponseReader(), this.graphicsUtils).readAsBitmap();
            if (bitmap == null) {
                fireErrorEvent("Picture could not be retrieved from server.", "storePicture");
            } else {
                this.threadRunner.execute(new C03193(bitmap), ExecutionStyle.SCHEDULE, ExecutionThread.MAIN_THREAD);
            }
        } catch (WebRequestException e) {
            fireErrorEvent("Server could not be contacted to download picture.", "storePicture");
        }
    }

    private void savePicture(Bitmap bitmap) {
        Builder alert = this.alertDialogFactory.createBuilder(getContext());
        alert.setTitle("Would you like to save the image to your gallery?");
        alert.setPositiveButton("Yes", new C03204(bitmap));
        alert.setNegativeButton("No", new C03215());
        alert.show();
    }

    void collapseExpandedAd(AdControlAccessor adControlAccessor) {
        this.logger.m637d("Collapsing expanded ad " + this);
        this.threadRunner.execute(new C03226(adControlAccessor), ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
    }

    @SuppressLint({"InlinedApi"})
    private void collapseExpandedAdOnThread(AdControlAccessor adControlAccessor) {
        this.rootView = (FrameLayout) ((Activity) getContext()).findViewById(16908290);
        if (this.expandedWithUrl) {
            this.logger.m637d("Expanded With URL");
            adControlAccessor.popView();
        } else {
            this.logger.m637d("Not Expanded with URL");
        }
        lookupExpansionView();
        adControlAccessor.moveViewBackToParent(new LayoutParams(-1, -1, 17));
        adControlAccessor.removeCloseButton();
        if (this.expansionView != null) {
            this.rootView.removeView(this.expansionView);
        }
        if (this.resizedView != null) {
            this.rootView.removeView(this.resizedView);
        }
        this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new C03237(adControlAccessor));
    }

    private void lookupExpansionView() {
        if (this.expansionView == null) {
            ViewGroup adContainerView = this.adControlAccessor.getViewParentIfExpanded();
            if (adContainerView != null && CONTENT_DESCRIPTION_AD_CONTAINER_VIEW.equals(adContainerView.getContentDescription())) {
                ViewGroup expansionView = (ViewGroup) adContainerView.getParent();
                if (expansionView != null && CONTENT_DESCRIPTION_EXPANSION_VIEW.equals(expansionView.getContentDescription())) {
                    this.expansionView = expansionView;
                }
            }
        }
    }

    private Size computeExpandedSizeInPixels(ExpandProperties expandProperties, String url) {
        Size maxSize = this.adControlAccessor.getMaxSize();
        int widthToCompute = (url == null || maxSize == null) ? expandProperties.getWidth() : maxSize.getWidth();
        int heightToCompute = (url == null || maxSize == null) ? expandProperties.getHeight() : maxSize.getHeight();
        this.logger.m637d("Expanding Ad to " + widthToCompute + "x" + heightToCompute);
        return new Size(this.adUtils.deviceIndependentPixelToPixel(widthToCompute), this.adUtils.deviceIndependentPixelToPixel(heightToCompute));
    }

    private void createExpandedView() {
        this.rootView = (FrameLayout) ((Activity) getContext()).findViewById(16908290);
        this.expansionView = new RelativeLayout(getContext());
        this.expansionView.setContentDescription(CONTENT_DESCRIPTION_EXPANSION_VIEW);
        View dimmingView = new View(getContext());
        dimmingView.setBackgroundColor(0);
        dimmingView.setContentDescription(CONTENT_DESCRIPTION_DIMMING_VIEW);
        dimmingView.setOnTouchListener(new C03248());
        this.expansionView.addView(dimmingView, new RelativeLayout.LayoutParams(-1, -1));
        this.adContainerView = new FrameLayout(getContext());
        this.adContainerView.setContentDescription(CONTENT_DESCRIPTION_AD_CONTAINER_VIEW);
    }

    void expandAd(ExpandProperties expandProperties, String url) {
        this.threadRunner.execute(new C03259(expandProperties, url), ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
    }

    @SuppressLint({"InlinedApi"})
    private void expandAdOnThread(ExpandProperties expandProperties, String url) {
        boolean z = true;
        if (url != null) {
            this.adControlAccessor.stashView();
            this.expandedWithUrl = true;
        } else {
            this.expandedWithUrl = false;
        }
        Size expandSize = computeExpandedSizeInPixels(expandProperties, url);
        createExpandedView();
        this.adControlAccessor.moveViewToViewGroup(this.adContainerView, new RelativeLayout.LayoutParams(-1, -1), true);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(expandSize.getWidth(), expandSize.getHeight());
        lp.addRule(13);
        this.expansionView.addView(this.adContainerView, lp);
        this.rootView.addView(this.expansionView, new RelativeLayout.LayoutParams(-1, -1));
        AdControlAccessor adControlAccessor = this.adControlAccessor;
        if (expandProperties.getUseCustomClose().booleanValue()) {
            z = false;
        }
        adControlAccessor.enableCloseButton(z);
        this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new 10());
    }

    void resizeAd(ResizeProperties resizeProperties) {
        this.threadRunner.execute(new 11(resizeProperties, computeResizeSizeInPixels(resizeProperties)), ExecutionStyle.RUN_ASAP, ExecutionThread.MAIN_THREAD);
    }

    private void resizeAdOnThread(ResizeProperties resizeProperties, Size resizeSize) {
        createResizedView();
        int leftMargin = this.adUtils.deviceIndependentPixelToPixel(this.defaultPosition.getX() + resizeProperties.getOffsetX());
        int topMargin = this.adUtils.deviceIndependentPixelToPixel(this.defaultPosition.getY() + resizeProperties.getOffsetY());
        RelativePosition closePosition = RelativePosition.fromString(resizeProperties.getCustomClosePosition());
        Size maxSize = this.adControlAccessor.getMaxSize();
        int maxWidth = this.adUtils.deviceIndependentPixelToPixel(maxSize.getWidth());
        int maxHeight = this.adUtils.deviceIndependentPixelToPixel(maxSize.getHeight());
        if (!resizeProperties.getAllowOffscreen().booleanValue()) {
            if (resizeSize.getWidth() > maxWidth) {
                resizeSize.setWidth(maxWidth);
            }
            if (resizeSize.getHeight() > maxHeight) {
                resizeSize.setHeight(maxHeight);
            }
            if (leftMargin < 0) {
                leftMargin = 0;
            } else if (resizeSize.getWidth() + leftMargin > maxWidth) {
                leftMargin = maxWidth - resizeSize.getWidth();
            }
            if (topMargin < 0) {
                topMargin = 0;
            } else if (resizeSize.getHeight() + topMargin > maxHeight) {
                topMargin = maxHeight - resizeSize.getHeight();
            }
        } else if (!isValidClosePosition(closePosition, topMargin, leftMargin, resizeSize, maxWidth, maxHeight)) {
            fireErrorEvent("Resize failed because close event area must be entirely on screen.", "resize");
            return;
        }
        this.adControlAccessor.moveViewToViewGroup(this.resizedView, new RelativeLayout.LayoutParams(resizeSize.getWidth(), resizeSize.getHeight()), false);
        LayoutParams params = new LayoutParams(resizeSize.getWidth(), resizeSize.getHeight());
        params.gravity = 48;
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        if (this.rootView.equals(this.resizedView.getParent())) {
            this.resizedView.setLayoutParams(params);
        } else {
            this.rootView.addView(this.resizedView, params);
        }
        this.adControlAccessor.enableCloseButton(false, closePosition);
        this.rootView.getViewTreeObserver().addOnGlobalLayoutListener(new 12());
    }

    private boolean isValidClosePosition(RelativePosition closePosition, int topMargin, int leftMargin, Size resizeSize, int maxWidth, int maxHeight) {
        int closeButtonSize = this.adUtils.deviceIndependentPixelToPixel(CLOSE_BUTTON_SIZE);
        int top = 0;
        int bottom = 0;
        int left = 0;
        int right = 0;
        switch (13.$SwitchMap$com$amazon$device$ads$RelativePosition[closePosition.ordinal()]) {
            case Zone.PRIMARY /*1*/:
                top = topMargin;
                left = leftMargin;
                bottom = top + closeButtonSize;
                right = left + closeButtonSize;
                break;
            case Zone.SECONDARY /*2*/:
                top = topMargin;
                right = leftMargin + resizeSize.getWidth();
                bottom = top + closeButtonSize;
                left = right - closeButtonSize;
                break;
            case Protocol.GGP /*3*/:
                top = topMargin;
                left = ((resizeSize.getWidth() / 2) + leftMargin) - (closeButtonSize / 2);
                bottom = top + closeButtonSize;
                right = left + closeButtonSize;
                break;
            case Type.MF /*4*/:
                bottom = topMargin + resizeSize.getHeight();
                left = leftMargin;
                top = bottom - closeButtonSize;
                right = left + closeButtonSize;
                break;
            case Service.RJE /*5*/:
                bottom = topMargin + resizeSize.getHeight();
                right = leftMargin + resizeSize.getWidth();
                top = bottom - closeButtonSize;
                left = right - closeButtonSize;
                break;
            case Protocol.TCP /*6*/:
                bottom = topMargin + resizeSize.getHeight();
                left = ((resizeSize.getWidth() / 2) + leftMargin) - (closeButtonSize / 2);
                top = bottom - closeButtonSize;
                right = left + closeButtonSize;
                break;
            case Service.ECHO /*7*/:
                top = ((resizeSize.getHeight() / 2) + topMargin) - (closeButtonSize / 2);
                left = ((resizeSize.getWidth() / 2) + leftMargin) - (closeButtonSize / 2);
                bottom = top + closeButtonSize;
                right = left + closeButtonSize;
                break;
        }
        if (top < 0 || left < 0 || bottom > maxHeight || right > maxWidth) {
            return false;
        }
        return true;
    }

    private Size computeResizeSizeInPixels(ResizeProperties resizeProperties) {
        return new Size(this.adUtils.deviceIndependentPixelToPixel(resizeProperties.getWidth()), this.adUtils.deviceIndependentPixelToPixel(resizeProperties.getHeight()));
    }

    private void createResizedView() {
        if (this.resizedView == null) {
            if (this.rootView == null) {
                this.rootView = (FrameLayout) ((Activity) getContext()).findViewById(16908290);
            }
            this.resizedView = new RelativeLayout(getContext());
            this.resizedView.setContentDescription(CONTENT_DESCRIPTION_RESIZED_VIEW);
        }
    }

    void reportSizeChangeEvent() {
        Position currentPosition = this.adControlAccessor.getCurrentPosition();
        if (currentPosition != null) {
            this.adControlAccessor.injectJavascript("mraidBridge.sizeChange(" + currentPosition.getSize().getWidth() + Stomp.COMMA + currentPosition.getSize().getHeight() + ");");
        }
    }

    void orientationPropertyChange() {
        if (this.adControlAccessor.isVisible() && this.adControlAccessor.isModal()) {
            Activity activity = (Activity) getContext();
            int originalOrientation = activity.getRequestedOrientation();
            Position currentPosition = this.adControlAccessor.getCurrentPosition();
            this.logger.m637d("Current Orientation: " + originalOrientation);
            if (!AdState.EXPANDED.equals(this.adControlAccessor.getAdState())) {
                switch (13.$SwitchMap$com$amazon$device$ads$ForceOrientation[this.orientationProperties.getForceOrientation().ordinal()]) {
                    case Zone.PRIMARY /*1*/:
                        activity.setRequestedOrientation(1);
                        break;
                    case Zone.SECONDARY /*2*/:
                        activity.setRequestedOrientation(0);
                        break;
                }
            }
            if (AdState.EXPANDED.equals(this.adControlAccessor.getAdState()) || ForceOrientation.NONE.equals(this.orientationProperties.getForceOrientation())) {
                if (this.orientationProperties.isAllowOrientationChange().booleanValue()) {
                    if (((Activity) this.adControlAccessor.getContext()).getRequestedOrientation() != -1) {
                        ((Activity) this.adControlAccessor.getContext()).setRequestedOrientation(-1);
                    }
                } else if (this.adControlAccessor.isModal()) {
                    activity.setRequestedOrientation(DisplayUtils.determineCanonicalScreenOrientation(activity));
                }
            }
            int newOrientation = activity.getRequestedOrientation();
            this.logger.m637d("New Orientation: " + newOrientation);
            if (newOrientation != originalOrientation && currentPosition != null) {
                if (currentPosition.getSize().getWidth() != this.adControlAccessor.getCurrentPosition().getSize().getWidth()) {
                    reportSizeChangeEvent();
                }
            }
        }
    }

    private void fireErrorEvent(String message, String action) {
        this.adControlAccessor.injectJavascript(String.format(Locale.US, ERROR_EVENT_FORMAT, new Object[]{message, action}));
    }
}
