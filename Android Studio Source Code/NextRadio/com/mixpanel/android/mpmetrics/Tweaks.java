package com.mixpanel.android.mpmetrics;

import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tweaks {
    public static final int BOOLEAN_TYPE = 1;
    public static final int DOUBLE_TYPE = 2;
    private static final String LOGTAG = "MixpanelAPI.Tweaks";
    public static final int LONG_TYPE = 3;
    public static final int STRING_TYPE = 4;
    private final List<OnTweakDeclaredListener> mTweakDeclaredListeners;
    private final Map<String, TweakValue> mTweakValues;

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.1 */
    class C11021 implements Tweak<String> {
        final /* synthetic */ String val$tweakName;

        C11021(String str) {
            this.val$tweakName = str;
        }

        public String get() {
            return Tweaks.this.getValue(this.val$tweakName).getStringValue();
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.2 */
    class C11032 implements Tweak<Double> {
        final /* synthetic */ String val$tweakName;

        C11032(String str) {
            this.val$tweakName = str;
        }

        public Double get() {
            return Double.valueOf(Tweaks.this.getValue(this.val$tweakName).getNumberValue().doubleValue());
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.3 */
    class C11043 implements Tweak<Float> {
        final /* synthetic */ String val$tweakName;

        C11043(String str) {
            this.val$tweakName = str;
        }

        public Float get() {
            return Float.valueOf(Tweaks.this.getValue(this.val$tweakName).getNumberValue().floatValue());
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.4 */
    class C11054 implements Tweak<Long> {
        final /* synthetic */ String val$tweakName;

        C11054(String str) {
            this.val$tweakName = str;
        }

        public Long get() {
            return Long.valueOf(Tweaks.this.getValue(this.val$tweakName).getNumberValue().longValue());
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.5 */
    class C11065 implements Tweak<Integer> {
        final /* synthetic */ String val$tweakName;

        C11065(String str) {
            this.val$tweakName = str;
        }

        public Integer get() {
            return Integer.valueOf(Tweaks.this.getValue(this.val$tweakName).getNumberValue().intValue());
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.6 */
    class C11076 implements Tweak<Byte> {
        final /* synthetic */ String val$tweakName;

        C11076(String str) {
            this.val$tweakName = str;
        }

        public Byte get() {
            return Byte.valueOf(Tweaks.this.getValue(this.val$tweakName).getNumberValue().byteValue());
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.7 */
    class C11087 implements Tweak<Short> {
        final /* synthetic */ String val$tweakName;

        C11087(String str) {
            this.val$tweakName = str;
        }

        public Short get() {
            return Short.valueOf(Tweaks.this.getValue(this.val$tweakName).getNumberValue().shortValue());
        }
    }

    /* renamed from: com.mixpanel.android.mpmetrics.Tweaks.8 */
    class C11098 implements Tweak<Boolean> {
        final /* synthetic */ String val$tweakName;

        C11098(String str) {
            this.val$tweakName = str;
        }

        public Boolean get() {
            return Tweaks.this.getValue(this.val$tweakName).getBooleanValue();
        }
    }

    public interface OnTweakDeclaredListener {
        void onTweakDeclared();
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface TweakType {
    }

    public static class TweakValue {
        private final Object defaultValue;
        private final Number maximum;
        private final Number minimum;
        public final int type;
        private final Object value;

        private TweakValue(int aType, Object aDefaultValue, Number aMin, Number aMax, Object value) {
            this.type = aType;
            this.defaultValue = aDefaultValue;
            this.minimum = aMin;
            this.maximum = aMax;
            this.value = value;
        }

        public TweakValue updateValue(Object newValue) {
            return new TweakValue(this.type, this.defaultValue, this.minimum, this.maximum, newValue);
        }

        public String getStringValue() {
            String ret = null;
            try {
                ret = (String) this.defaultValue;
            } catch (ClassCastException e) {
            }
            try {
                return (String) this.value;
            } catch (ClassCastException e2) {
                return ret;
            }
        }

        public Number getNumberValue() {
            Number ret = Integer.valueOf(0);
            if (this.defaultValue != null) {
                try {
                    ret = (Number) this.defaultValue;
                } catch (ClassCastException e) {
                }
            }
            if (this.value == null) {
                return ret;
            }
            try {
                return (Number) this.value;
            } catch (ClassCastException e2) {
                return ret;
            }
        }

        public Boolean getBooleanValue() {
            Boolean ret = Boolean.valueOf(false);
            if (this.defaultValue != null) {
                try {
                    ret = (Boolean) this.defaultValue;
                } catch (ClassCastException e) {
                }
            }
            if (this.value == null) {
                return ret;
            }
            try {
                return (Boolean) this.value;
            } catch (ClassCastException e2) {
                return ret;
            }
        }
    }

    public synchronized void addOnTweakDeclaredListener(OnTweakDeclaredListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener cannot be null");
        }
        this.mTweakDeclaredListeners.add(listener);
    }

    public synchronized void set(String tweakName, Object value) {
        if (this.mTweakValues.containsKey(tweakName)) {
            this.mTweakValues.put(tweakName, ((TweakValue) this.mTweakValues.get(tweakName)).updateValue(value));
        } else {
            Log.w(LOGTAG, "Attempt to set a tweak \"" + tweakName + "\" which has never been defined.");
        }
    }

    public synchronized Map<String, TweakValue> getAllValues() {
        return new HashMap(this.mTweakValues);
    }

    Tweaks() {
        this.mTweakValues = new HashMap();
        this.mTweakDeclaredListeners = new ArrayList();
    }

    Tweak<String> stringTweak(String tweakName, String defaultValue) {
        declareTweak(tweakName, defaultValue, STRING_TYPE);
        return new C11021(tweakName);
    }

    Tweak<Double> doubleTweak(String tweakName, double defaultValue) {
        declareTweak(tweakName, Double.valueOf(defaultValue), DOUBLE_TYPE);
        return new C11032(tweakName);
    }

    Tweak<Float> floatTweak(String tweakName, float defaultValue) {
        declareTweak(tweakName, Float.valueOf(defaultValue), DOUBLE_TYPE);
        return new C11043(tweakName);
    }

    Tweak<Long> longTweak(String tweakName, long defaultValue) {
        declareTweak(tweakName, Long.valueOf(defaultValue), LONG_TYPE);
        return new C11054(tweakName);
    }

    Tweak<Integer> intTweak(String tweakName, int defaultValue) {
        declareTweak(tweakName, Integer.valueOf(defaultValue), LONG_TYPE);
        return new C11065(tweakName);
    }

    Tweak<Byte> byteTweak(String tweakName, byte defaultValue) {
        declareTweak(tweakName, Byte.valueOf(defaultValue), LONG_TYPE);
        return new C11076(tweakName);
    }

    Tweak<Short> shortTweak(String tweakName, short defaultValue) {
        declareTweak(tweakName, Short.valueOf(defaultValue), LONG_TYPE);
        return new C11087(tweakName);
    }

    Tweak<Boolean> booleanTweak(String tweakName, boolean defaultValue) {
        declareTweak(tweakName, Boolean.valueOf(defaultValue), BOOLEAN_TYPE);
        return new C11098(tweakName);
    }

    private synchronized TweakValue getValue(String tweakName) {
        return (TweakValue) this.mTweakValues.get(tweakName);
    }

    private void declareTweak(String tweakName, Object defaultValue, int tweakType) {
        if (this.mTweakValues.containsKey(tweakName)) {
            Log.w(LOGTAG, "Attempt to define a tweak \"" + tweakName + "\" twice with the same name");
            return;
        }
        this.mTweakValues.put(tweakName, new TweakValue(defaultValue, null, null, defaultValue, null));
        int listenerSize = this.mTweakDeclaredListeners.size();
        for (int i = 0; i < listenerSize; i += BOOLEAN_TYPE) {
            ((OnTweakDeclaredListener) this.mTweakDeclaredListeners.get(i)).onTweakDeclared();
        }
    }
}
