package com.mixpanel.android.mpmetrics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

@TargetApi(16)
public class UpdateDisplayState implements Parcelable {
    public static final Creator<UpdateDisplayState> CREATOR;
    private static final String DISPLAYSTATE_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.DISPLAYSTATE_BUNDLE_KEY";
    private static final String DISTINCT_ID_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.DISTINCT_ID_BUNDLE_KEY";
    private static final String LOGTAG = "MixpanelAPI.UpDisplSt";
    private static final long MAX_LOCK_TIME_MILLIS = 43200000;
    private static final String TOKEN_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.TOKEN_BUNDLE_KEY";
    private static int sNextIntentId;
    private static int sShowingIntentId;
    private static final ReentrantLock sUpdateDisplayLock;
    private static long sUpdateDisplayLockMillis;
    private static UpdateDisplayState sUpdateDisplayState;
    private final DisplayState mDisplayState;
    private final String mDistinctId;
    private final String mToken;

    /* renamed from: com.mixpanel.android.mpmetrics.UpdateDisplayState.1 */
    static class C11101 implements Creator<UpdateDisplayState> {
        C11101() {
        }

        public UpdateDisplayState createFromParcel(Parcel in) {
            Bundle read = new Bundle(UpdateDisplayState.class.getClassLoader());
            read.readFromParcel(in);
            return new UpdateDisplayState(null);
        }

        public UpdateDisplayState[] newArray(int size) {
            return new UpdateDisplayState[size];
        }
    }

    public static class AnswerMap implements Parcelable {
        public static final Creator<AnswerMap> CREATOR;
        private final HashMap<Integer, String> mMap;

        /* renamed from: com.mixpanel.android.mpmetrics.UpdateDisplayState.AnswerMap.1 */
        static class C11111 implements Creator<AnswerMap> {
            C11111() {
            }

            public AnswerMap createFromParcel(Parcel in) {
                Bundle read = new Bundle(AnswerMap.class.getClassLoader());
                AnswerMap ret = new AnswerMap();
                read.readFromParcel(in);
                for (String kString : read.keySet()) {
                    ret.put(Integer.valueOf(kString), read.getString(kString));
                }
                return ret;
            }

            public AnswerMap[] newArray(int size) {
                return new AnswerMap[size];
            }
        }

        @SuppressLint({"UseSparseArrays"})
        public AnswerMap() {
            this.mMap = new HashMap();
        }

        public void put(Integer i, String s) {
            this.mMap.put(i, s);
        }

        public String get(Integer i) {
            return (String) this.mMap.get(i);
        }

        public boolean contentEquals(AnswerMap other) {
            return this.mMap.equals(other.mMap);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            Bundle out = new Bundle();
            for (Entry<Integer, String> entry : this.mMap.entrySet()) {
                out.putString(Integer.toString(((Integer) entry.getKey()).intValue()), (String) entry.getValue());
            }
            dest.writeBundle(out);
        }

        static {
            CREATOR = new C11111();
        }
    }

    public static abstract class DisplayState implements Parcelable {
        public static final Creator<DisplayState> CREATOR;
        public static final String STATE_IMPL_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.DisplayState.STATE_IMPL_KEY";
        public static final String STATE_TYPE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.DisplayState.STATE_TYPE_KEY";

        /* renamed from: com.mixpanel.android.mpmetrics.UpdateDisplayState.DisplayState.1 */
        static class C11121 implements Creator<DisplayState> {
            C11121() {
            }

            public DisplayState createFromParcel(Parcel source) {
                Bundle read = new Bundle(DisplayState.class.getClassLoader());
                read.readFromParcel(source);
                String type = read.getString(DisplayState.STATE_TYPE_KEY);
                Bundle implementation = read.getBundle(DisplayState.STATE_IMPL_KEY);
                if (InAppNotificationState.TYPE.equals(type)) {
                    return new InAppNotificationState(null);
                }
                if (SurveyState.TYPE.equals(type)) {
                    return new SurveyState(null);
                }
                throw new RuntimeException("Unrecognized display state type " + type);
            }

            public DisplayState[] newArray(int size) {
                return new DisplayState[size];
            }
        }

        public static final class InAppNotificationState extends DisplayState {
            public static final Creator<InAppNotificationState> CREATOR;
            private static String HIGHLIGHT_KEY = null;
            private static String INAPP_KEY = null;
            public static final String TYPE = "InAppNotificationState";
            private final int mHighlightColor;
            private final InAppNotification mInAppNotification;

            /* renamed from: com.mixpanel.android.mpmetrics.UpdateDisplayState.DisplayState.InAppNotificationState.1 */
            static class C11131 implements Creator<InAppNotificationState> {
                C11131() {
                }

                public InAppNotificationState createFromParcel(Parcel source) {
                    Bundle read = new Bundle(InAppNotificationState.class.getClassLoader());
                    read.readFromParcel(source);
                    return new InAppNotificationState(null);
                }

                public InAppNotificationState[] newArray(int size) {
                    return new InAppNotificationState[size];
                }
            }

            static {
                CREATOR = new C11131();
                INAPP_KEY = "com.com.mixpanel.android.mpmetrics.UpdateDisplayState.InAppNotificationState.INAPP_KEY";
                HIGHLIGHT_KEY = "com.com.mixpanel.android.mpmetrics.UpdateDisplayState.InAppNotificationState.HIGHLIGHT_KEY";
            }

            public InAppNotificationState(InAppNotification notification, int highlightColor) {
                super();
                this.mInAppNotification = notification;
                this.mHighlightColor = highlightColor;
            }

            public int getHighlightColor() {
                return this.mHighlightColor;
            }

            public InAppNotification getInAppNotification() {
                return this.mInAppNotification;
            }

            public String getType() {
                return TYPE;
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel dest, int flags) {
                Bundle write = new Bundle();
                write.putParcelable(INAPP_KEY, this.mInAppNotification);
                write.putInt(HIGHLIGHT_KEY, this.mHighlightColor);
                dest.writeBundle(write);
            }

            private InAppNotificationState(Bundle in) {
                super();
                this.mInAppNotification = (InAppNotification) in.getParcelable(INAPP_KEY);
                this.mHighlightColor = in.getInt(HIGHLIGHT_KEY);
            }
        }

        public static final class SurveyState extends DisplayState {
            private static final String ANSWERS_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.ANSWERS_BUNDLE_KEY";
            private static final String BACKGROUND_COMPRESSED_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.BACKGROUND_COMPRESSED_BUNDLE_KEY";
            public static final Creator<SurveyState> CREATOR;
            private static final String HIGHLIGHT_COLOR_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.HIGHLIGHT_COLOR_BUNDLE_KEY";
            private static final String SURVEY_BUNDLE_KEY = "com.mixpanel.android.mpmetrics.UpdateDisplayState.SURVEY_BUNDLE_KEY";
            public static final String TYPE = "SurveyState";
            private final AnswerMap mAnswers;
            private Bitmap mBackground;
            private int mHighlightColor;
            private final Survey mSurvey;

            /* renamed from: com.mixpanel.android.mpmetrics.UpdateDisplayState.DisplayState.SurveyState.1 */
            static class C11141 implements Creator<SurveyState> {
                C11141() {
                }

                public SurveyState createFromParcel(Parcel source) {
                    Bundle read = new Bundle(SurveyState.class.getClassLoader());
                    read.readFromParcel(source);
                    return new SurveyState(null);
                }

                public SurveyState[] newArray(int size) {
                    return new SurveyState[size];
                }
            }

            static {
                CREATOR = new C11141();
            }

            public SurveyState(Survey survey) {
                super();
                this.mSurvey = survey;
                this.mAnswers = new AnswerMap();
                this.mHighlightColor = ViewCompat.MEASURED_STATE_MASK;
                this.mBackground = null;
            }

            public void setBackground(Bitmap background) {
                this.mBackground = background;
            }

            public void setHighlightColor(int highlightColor) {
                this.mHighlightColor = highlightColor;
            }

            public Bitmap getBackground() {
                return this.mBackground;
            }

            public AnswerMap getAnswers() {
                return this.mAnswers;
            }

            public int getHighlightColor() {
                return this.mHighlightColor;
            }

            public Survey getSurvey() {
                return this.mSurvey;
            }

            public String getType() {
                return TYPE;
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel dest, int flags) {
                Bundle out = new Bundle();
                out.putInt(HIGHLIGHT_COLOR_BUNDLE_KEY, this.mHighlightColor);
                out.putParcelable(ANSWERS_BUNDLE_KEY, this.mAnswers);
                byte[] backgroundCompressed = null;
                if (this.mBackground != null) {
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    this.mBackground.compress(CompressFormat.PNG, 20, bs);
                    backgroundCompressed = bs.toByteArray();
                }
                out.putByteArray(BACKGROUND_COMPRESSED_BUNDLE_KEY, backgroundCompressed);
                out.putParcelable(SURVEY_BUNDLE_KEY, this.mSurvey);
                dest.writeBundle(out);
            }

            private SurveyState(Bundle in) {
                super();
                this.mHighlightColor = in.getInt(HIGHLIGHT_COLOR_BUNDLE_KEY);
                this.mAnswers = (AnswerMap) in.getParcelable(ANSWERS_BUNDLE_KEY);
                byte[] backgroundCompressed = in.getByteArray(BACKGROUND_COMPRESSED_BUNDLE_KEY);
                if (backgroundCompressed != null) {
                    this.mBackground = BitmapFactory.decodeByteArray(backgroundCompressed, 0, backgroundCompressed.length);
                } else {
                    this.mBackground = null;
                }
                this.mSurvey = (Survey) in.getParcelable(SURVEY_BUNDLE_KEY);
            }
        }

        public abstract String getType();

        private DisplayState() {
        }

        static {
            CREATOR = new C11121();
        }
    }

    static ReentrantLock getLockObject() {
        return sUpdateDisplayLock;
    }

    static boolean hasCurrentProposal() {
        if (sUpdateDisplayLock.isHeldByCurrentThread()) {
            long deltaTime = System.currentTimeMillis() - sUpdateDisplayLockMillis;
            if (sNextIntentId > 0 && deltaTime > MAX_LOCK_TIME_MILLIS) {
                Log.i(LOGTAG, "UpdateDisplayState set long, long ago, without showing. Update state will be cleared.");
                sUpdateDisplayState = null;
            }
            return sUpdateDisplayState != null;
        } else {
            throw new AssertionError();
        }
    }

    static int proposeDisplay(DisplayState state, String distinctId, String token) {
        if (!sUpdateDisplayLock.isHeldByCurrentThread()) {
            throw new AssertionError();
        } else if (!hasCurrentProposal()) {
            sUpdateDisplayLockMillis = System.currentTimeMillis();
            sUpdateDisplayState = new UpdateDisplayState(state, distinctId, token);
            sNextIntentId++;
            return sNextIntentId;
        } else if (!MPConfig.DEBUG) {
            return -1;
        } else {
            Log.v(LOGTAG, "Already showing (or cooking) a Mixpanel update, declining to show another.");
            return -1;
        }
    }

    public static void releaseDisplayState(int intentId) {
        sUpdateDisplayLock.lock();
        try {
            if (intentId == sShowingIntentId) {
                sShowingIntentId = -1;
                sUpdateDisplayState = null;
            }
            sUpdateDisplayLock.unlock();
        } catch (Throwable th) {
            sUpdateDisplayLock.unlock();
        }
    }

    public static UpdateDisplayState claimDisplayState(int intentId) {
        UpdateDisplayState updateDisplayState = null;
        sUpdateDisplayLock.lock();
        try {
            if (sShowingIntentId > 0 && sShowingIntentId != intentId) {
                return updateDisplayState;
            }
            if (sUpdateDisplayState == null) {
                sUpdateDisplayLock.unlock();
            } else {
                sUpdateDisplayLockMillis = System.currentTimeMillis();
                sShowingIntentId = intentId;
                updateDisplayState = sUpdateDisplayState;
                sUpdateDisplayLock.unlock();
            }
            return updateDisplayState;
        } finally {
            sUpdateDisplayLock.unlock();
        }
    }

    static {
        CREATOR = new C11101();
        sUpdateDisplayLock = new ReentrantLock();
        sUpdateDisplayLockMillis = -1;
        sUpdateDisplayState = null;
        sNextIntentId = 0;
        sShowingIntentId = -1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(DISTINCT_ID_BUNDLE_KEY, this.mDistinctId);
        bundle.putString(TOKEN_BUNDLE_KEY, this.mToken);
        bundle.putParcelable(DISPLAYSTATE_BUNDLE_KEY, this.mDisplayState);
        dest.writeBundle(bundle);
    }

    public DisplayState getDisplayState() {
        return this.mDisplayState;
    }

    public String getDistinctId() {
        return this.mDistinctId;
    }

    public String getToken() {
        return this.mToken;
    }

    UpdateDisplayState(DisplayState displayState, String distinctId, String token) {
        this.mDistinctId = distinctId;
        this.mToken = token;
        this.mDisplayState = displayState;
    }

    private UpdateDisplayState(Bundle read) {
        this.mDistinctId = read.getString(DISTINCT_ID_BUNDLE_KEY);
        this.mToken = read.getString(TOKEN_BUNDLE_KEY);
        this.mDisplayState = (DisplayState) read.getParcelable(DISPLAYSTATE_BUNDLE_KEY);
    }
}
