package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.activemq.transport.stomp.Stomp.Headers;
import org.xbill.DNS.KEYRecord.Flags;

/* compiled from: FragmentManager */
final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory {
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    ArrayList<Fragment> mActive;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<Integer> mAvailIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    FragmentController mController;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<Runnable> mPendingActions;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    Runnable[] mTmpActions;

    /* renamed from: android.support.v4.app.FragmentManagerImpl.1 */
    class FragmentManager implements Runnable {
        FragmentManager() {
        }

        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl.2 */
    class FragmentManager implements Runnable {
        FragmentManager() {
        }

        public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, -1, 0);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl.3 */
    class FragmentManager implements Runnable {
        final /* synthetic */ int val$flags;
        final /* synthetic */ String val$name;

        FragmentManager(String str, int i) {
            this.val$name = str;
            this.val$flags = i;
        }

        public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), this.val$name, -1, this.val$flags);
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl.4 */
    class FragmentManager implements Runnable {
        final /* synthetic */ int val$flags;
        final /* synthetic */ int val$id;

        FragmentManager(int i, int i2) {
            this.val$id = i;
            this.val$flags = i2;
        }

        public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, this.val$id, this.val$flags);
        }
    }

    /* compiled from: FragmentManager */
    static class AnimateOnHWLayerIfNeededListener implements AnimationListener {
        private AnimationListener mOrignalListener;
        private boolean mShouldRunOnHWLayer;
        private View mView;

        /* renamed from: android.support.v4.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.1 */
        class FragmentManager implements Runnable {
            FragmentManager() {
            }

            public void run() {
                ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, FragmentManagerImpl.ANIM_STYLE_OPEN_EXIT, null);
            }
        }

        /* renamed from: android.support.v4.app.FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.2 */
        class FragmentManager implements Runnable {
            FragmentManager() {
            }

            public void run() {
                ViewCompat.setLayerType(AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
            }
        }

        public AnimateOnHWLayerIfNeededListener(View v, Animation anim) {
            this.mOrignalListener = null;
            this.mShouldRunOnHWLayer = FragmentManagerImpl.HONEYCOMB;
            this.mView = null;
            if (v != null && anim != null) {
                this.mView = v;
            }
        }

        public AnimateOnHWLayerIfNeededListener(View v, Animation anim, AnimationListener listener) {
            this.mOrignalListener = null;
            this.mShouldRunOnHWLayer = FragmentManagerImpl.HONEYCOMB;
            this.mView = null;
            if (v != null && anim != null) {
                this.mOrignalListener = listener;
                this.mView = v;
            }
        }

        @CallSuper
        public void onAnimationStart(Animation animation) {
            if (this.mView != null) {
                this.mShouldRunOnHWLayer = FragmentManagerImpl.shouldRunOnHWLayer(this.mView, animation);
                if (this.mShouldRunOnHWLayer) {
                    this.mView.post(new FragmentManager());
                }
            }
            if (this.mOrignalListener != null) {
                this.mOrignalListener.onAnimationStart(animation);
            }
        }

        @CallSuper
        public void onAnimationEnd(Animation animation) {
            if (this.mView != null && this.mShouldRunOnHWLayer) {
                this.mView.post(new FragmentManager());
            }
            if (this.mOrignalListener != null) {
                this.mOrignalListener.onAnimationEnd(animation);
            }
        }

        public void onAnimationRepeat(Animation animation) {
            if (this.mOrignalListener != null) {
                this.mOrignalListener.onAnimationRepeat(animation);
            }
        }
    }

    /* renamed from: android.support.v4.app.FragmentManagerImpl.5 */
    class FragmentManager extends AnimateOnHWLayerIfNeededListener {
        final /* synthetic */ ViewGroup val$container;
        final /* synthetic */ Fragment val$fragment;

        FragmentManager(View v, Animation anim, Fragment fragment, ViewGroup viewGroup) {
            this.val$fragment = fragment;
            this.val$container = viewGroup;
            super(v, anim);
        }

        public void onAnimationEnd(Animation animation) {
            super.onAnimationEnd(animation);
            if (this.val$fragment.mAnimatingAway != null) {
                this.val$container.removeView(this.val$fragment.mAnimatingAway);
                this.val$fragment.mAnimatingAway = null;
                FragmentManagerImpl.this.moveToState(this.val$fragment, this.val$fragment.mStateAfterAnimating, 0, 0, FragmentManagerImpl.HONEYCOMB);
            }
        }
    }

    /* compiled from: FragmentManager */
    static class FragmentTag {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;

        FragmentTag() {
        }

        static {
            Fragment = new int[]{16842755, 16842960, 16842961};
        }
    }

    FragmentManagerImpl() {
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new FragmentManager();
    }

    static {
        boolean z = HONEYCOMB;
        DEBUG = HONEYCOMB;
        if (VERSION.SDK_INT >= 11) {
            z = true;
        }
        HONEYCOMB = z;
        sAnimationListenerField = null;
        DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    }

    static boolean modifiesAlpha(Animation anim) {
        if (anim instanceof AlphaAnimation) {
            return true;
        }
        if (anim instanceof AnimationSet) {
            List<Animation> anims = ((AnimationSet) anim).getAnimations();
            for (int i = 0; i < anims.size(); i += ANIM_STYLE_OPEN_ENTER) {
                if (anims.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    static boolean shouldRunOnHWLayer(View v, Animation anim) {
        return (VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(v) == 0 && ViewCompat.hasOverlappingRendering(v) && modifiesAlpha(anim)) ? true : HONEYCOMB;
    }

    private void throwException(RuntimeException ex) {
        Log.e(TAG, ex.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter pw = new PrintWriter(new LogWriter(TAG));
        if (this.mHost != null) {
            try {
                this.mHost.onDump("  ", null, pw, new String[0]);
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
            }
        } else {
            try {
                dump("  ", null, pw, new String[0]);
            } catch (Exception e2) {
                Log.e(TAG, "Failed dumping state", e2);
            }
        }
        throw ex;
    }

    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public boolean executePendingTransactions() {
        return execPendingActions();
    }

    public void popBackStack() {
        enqueueAction(new FragmentManager(), HONEYCOMB);
    }

    public boolean popBackStackImmediate() {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mHost.getHandler(), null, -1, 0);
    }

    public void popBackStack(String name, int flags) {
        enqueueAction(new FragmentManager(name, flags), HONEYCOMB);
    }

    public boolean popBackStackImmediate(String name, int flags) {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mHost.getHandler(), name, -1, flags);
    }

    public void popBackStack(int id, int flags) {
        if (id < 0) {
            throw new IllegalArgumentException("Bad id: " + id);
        }
        enqueueAction(new FragmentManager(id, flags), HONEYCOMB);
    }

    public boolean popBackStackImmediate(int id, int flags) {
        checkStateLoss();
        executePendingTransactions();
        if (id >= 0) {
            return popBackStackState(this.mHost.getHandler(), null, id, flags);
        }
        throw new IllegalArgumentException("Bad id: " + id);
    }

    public int getBackStackEntryCount() {
        return this.mBackStack != null ? this.mBackStack.size() : 0;
    }

    public BackStackEntry getBackStackEntryAt(int index) {
        return (BackStackEntry) this.mBackStack.get(index);
    }

    public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(listener);
    }

    public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(listener);
        }
    }

    public void putFragment(Bundle bundle, String key, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(key, fragment.mIndex);
    }

    public Fragment getFragment(Bundle bundle, String key) {
        int index = bundle.getInt(key, -1);
        if (index == -1) {
            return null;
        }
        if (index >= this.mActive.size()) {
            throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
        }
        Fragment f = (Fragment) this.mActive.get(index);
        if (f != null) {
            return f;
        }
        throwException(new IllegalStateException("Fragment no longer exists for key " + key + ": index " + index));
        return f;
    }

    public List<Fragment> getFragments() {
        return this.mActive;
    }

    public SavedState saveFragmentInstanceState(Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        if (fragment.mState <= 0) {
            return null;
        }
        Bundle result = saveFragmentBasicState(fragment);
        if (result != null) {
            return new SavedState(result);
        }
        return null;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(Flags.FLAG8);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        int N;
        int i;
        Fragment f;
        String innerPrefix = prefix + "    ";
        if (this.mActive != null) {
            N = this.mActive.size();
            if (N > 0) {
                writer.print(prefix);
                writer.print("Active Fragments in ");
                writer.print(Integer.toHexString(System.identityHashCode(this)));
                writer.println(Headers.SEPERATOR);
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mActive.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f);
                    if (f != null) {
                        f.dump(innerPrefix, fd, writer, args);
                    }
                }
            }
        }
        if (this.mAdded != null) {
            N = this.mAdded.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Added Fragments:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mAdded.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (this.mCreatedMenus != null) {
            N = this.mCreatedMenus.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Fragments Created Menus:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    f = (Fragment) this.mCreatedMenus.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(f.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            N = this.mBackStack.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Back Stack:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    BackStackRecord bs = (BackStackRecord) this.mBackStack.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(bs.toString());
                    bs.dump(innerPrefix, fd, writer, args);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                N = this.mBackStackIndices.size();
                if (N > 0) {
                    writer.print(prefix);
                    writer.println("Back Stack Indices:");
                    for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                        bs = (BackStackRecord) this.mBackStackIndices.get(i);
                        writer.print(prefix);
                        writer.print("  #");
                        writer.print(i);
                        writer.print(": ");
                        writer.println(bs);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                writer.print(prefix);
                writer.print("mAvailBackStackIndices: ");
                writer.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null) {
            N = this.mPendingActions.size();
            if (N > 0) {
                writer.print(prefix);
                writer.println("Pending Actions:");
                for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                    Runnable r = (Runnable) this.mPendingActions.get(i);
                    writer.print(prefix);
                    writer.print("  #");
                    writer.print(i);
                    writer.print(": ");
                    writer.println(r);
                }
            }
        }
        writer.print(prefix);
        writer.println("FragmentManager misc state:");
        writer.print(prefix);
        writer.print("  mHost=");
        writer.println(this.mHost);
        writer.print(prefix);
        writer.print("  mContainer=");
        writer.println(this.mContainer);
        if (this.mParent != null) {
            writer.print(prefix);
            writer.print("  mParent=");
            writer.println(this.mParent);
        }
        writer.print(prefix);
        writer.print("  mCurState=");
        writer.print(this.mCurState);
        writer.print(" mStateSaved=");
        writer.print(this.mStateSaved);
        writer.print(" mDestroyed=");
        writer.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            writer.print(prefix);
            writer.print("  mNeedMenuInvalidate=");
            writer.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            writer.print(prefix);
            writer.print("  mNoTransactionsBecause=");
            writer.println(this.mNoTransactionsBecause);
        }
        if (this.mAvailIndices != null && this.mAvailIndices.size() > 0) {
            writer.print(prefix);
            writer.print("  mAvailIndices: ");
            writer.println(Arrays.toString(this.mAvailIndices.toArray()));
        }
    }

    static Animation makeOpenCloseAnimation(Context context, float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(HONEYCOMB);
        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale, ANIM_STYLE_OPEN_ENTER, 0.5f, ANIM_STYLE_OPEN_ENTER, 0.5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(220);
        set.addAnimation(scale);
        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(220);
        set.addAnimation(alpha);
        return set;
    }

    static Animation makeFadeAnimation(Context context, float start, float end) {
        AlphaAnimation anim = new AlphaAnimation(start, end);
        anim.setInterpolator(DECELERATE_CUBIC);
        anim.setDuration(220);
        return anim;
    }

    Animation loadAnimation(Fragment fragment, int transit, boolean enter, int transitionStyle) {
        Animation animObj = fragment.onCreateAnimation(transit, enter, fragment.mNextAnim);
        if (animObj != null) {
            return animObj;
        }
        if (fragment.mNextAnim != 0) {
            Animation anim = AnimationUtils.loadAnimation(this.mHost.getContext(), fragment.mNextAnim);
            if (anim != null) {
                return anim;
            }
        }
        if (transit == 0) {
            return null;
        }
        int styleIndex = transitToStyleIndex(transit, enter);
        if (styleIndex < 0) {
            return null;
        }
        switch (styleIndex) {
            case ANIM_STYLE_OPEN_ENTER /*1*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            case ANIM_STYLE_OPEN_EXIT /*2*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            case ANIM_STYLE_CLOSE_ENTER /*3*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            case ANIM_STYLE_CLOSE_EXIT /*4*/:
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            case ANIM_STYLE_FADE_ENTER /*5*/:
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            case ANIM_STYLE_FADE_EXIT /*6*/:
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            default:
                if (transitionStyle == 0 && this.mHost.onHasWindowAnimations()) {
                    transitionStyle = this.mHost.onGetWindowAnimations();
                }
                if (transitionStyle == 0) {
                    return null;
                }
                return null;
        }
    }

    public void performPendingDeferredStart(Fragment f) {
        if (!f.mDeferStart) {
            return;
        }
        if (this.mExecutingActions) {
            this.mHavePendingDeferredStart = true;
            return;
        }
        f.mDeferStart = HONEYCOMB;
        moveToState(f, this.mCurState, 0, 0, HONEYCOMB);
    }

    private void setHWLayerAnimListenerIfAlpha(View v, Animation anim) {
        if (v != null && anim != null && shouldRunOnHWLayer(v, anim)) {
            AnimationListener originalListener = null;
            try {
                if (sAnimationListenerField == null) {
                    sAnimationListenerField = Animation.class.getDeclaredField("mListener");
                    sAnimationListenerField.setAccessible(true);
                }
                originalListener = (AnimationListener) sAnimationListenerField.get(anim);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "No field with the name mListener is found in Animation class", e);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Cannot access Animation's mListener field", e2);
            }
            anim.setAnimationListener(new AnimateOnHWLayerIfNeededListener(v, anim, originalListener));
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void moveToState(android.support.v4.app.Fragment r11, int r12, int r13, int r14, boolean r15) {
        /*
        r10 = this;
        r0 = r11.mAdded;
        if (r0 == 0) goto L_0x0008;
    L_0x0004:
        r0 = r11.mDetached;
        if (r0 == 0) goto L_0x000c;
    L_0x0008:
        r0 = 1;
        if (r12 <= r0) goto L_0x000c;
    L_0x000b:
        r12 = 1;
    L_0x000c:
        r0 = r11.mRemoving;
        if (r0 == 0) goto L_0x0016;
    L_0x0010:
        r0 = r11.mState;
        if (r12 <= r0) goto L_0x0016;
    L_0x0014:
        r12 = r11.mState;
    L_0x0016:
        r0 = r11.mDeferStart;
        if (r0 == 0) goto L_0x0023;
    L_0x001a:
        r0 = r11.mState;
        r1 = 4;
        if (r0 >= r1) goto L_0x0023;
    L_0x001f:
        r0 = 3;
        if (r12 <= r0) goto L_0x0023;
    L_0x0022:
        r12 = 3;
    L_0x0023:
        r0 = r11.mState;
        if (r0 >= r12) goto L_0x02e9;
    L_0x0027:
        r0 = r11.mFromLayout;
        if (r0 == 0) goto L_0x0030;
    L_0x002b:
        r0 = r11.mInLayout;
        if (r0 != 0) goto L_0x0030;
    L_0x002f:
        return;
    L_0x0030:
        r0 = r11.mAnimatingAway;
        if (r0 == 0) goto L_0x0041;
    L_0x0034:
        r0 = 0;
        r11.mAnimatingAway = r0;
        r2 = r11.mStateAfterAnimating;
        r3 = 0;
        r4 = 0;
        r5 = 1;
        r0 = r10;
        r1 = r11;
        r0.moveToState(r1, r2, r3, r4, r5);
    L_0x0041:
        r0 = r11.mState;
        switch(r0) {
            case 0: goto L_0x0081;
            case 1: goto L_0x017f;
            case 2: goto L_0x027b;
            case 3: goto L_0x027b;
            case 4: goto L_0x029d;
            default: goto L_0x0046;
        };
    L_0x0046:
        r0 = r11.mState;
        if (r0 == r12) goto L_0x002f;
    L_0x004a:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "moveToState: Fragment state for ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r9 = " not updated inline; ";
        r1 = r1.append(r9);
        r9 = "expected state ";
        r1 = r1.append(r9);
        r1 = r1.append(r12);
        r9 = " found ";
        r1 = r1.append(r9);
        r9 = r11.mState;
        r1 = r1.append(r9);
        r1 = r1.toString();
        android.util.Log.w(r0, r1);
        r11.mState = r12;
        goto L_0x002f;
    L_0x0081:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x009d;
    L_0x0085:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "moveto CREATED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x009d:
        r0 = r11.mSavedFragmentState;
        if (r0 == 0) goto L_0x00e9;
    L_0x00a1:
        r0 = r11.mSavedFragmentState;
        r1 = r10.mHost;
        r1 = r1.getContext();
        r1 = r1.getClassLoader();
        r0.setClassLoader(r1);
        r0 = r11.mSavedFragmentState;
        r1 = "android:view_state";
        r0 = r0.getSparseParcelableArray(r1);
        r11.mSavedViewState = r0;
        r0 = r11.mSavedFragmentState;
        r1 = "android:target_state";
        r0 = r10.getFragment(r0, r1);
        r11.mTarget = r0;
        r0 = r11.mTarget;
        if (r0 == 0) goto L_0x00d3;
    L_0x00c8:
        r0 = r11.mSavedFragmentState;
        r1 = "android:target_req_state";
        r9 = 0;
        r0 = r0.getInt(r1, r9);
        r11.mTargetRequestCode = r0;
    L_0x00d3:
        r0 = r11.mSavedFragmentState;
        r1 = "android:user_visible_hint";
        r9 = 1;
        r0 = r0.getBoolean(r1, r9);
        r11.mUserVisibleHint = r0;
        r0 = r11.mUserVisibleHint;
        if (r0 != 0) goto L_0x00e9;
    L_0x00e2:
        r0 = 1;
        r11.mDeferStart = r0;
        r0 = 3;
        if (r12 <= r0) goto L_0x00e9;
    L_0x00e8:
        r12 = 3;
    L_0x00e9:
        r0 = r10.mHost;
        r11.mHost = r0;
        r0 = r10.mParent;
        r11.mParentFragment = r0;
        r0 = r10.mParent;
        if (r0 == 0) goto L_0x012a;
    L_0x00f5:
        r0 = r10.mParent;
        r0 = r0.mChildFragmentManager;
    L_0x00f9:
        r11.mFragmentManager = r0;
        r0 = 0;
        r11.mCalled = r0;
        r0 = r10.mHost;
        r0 = r0.getContext();
        r11.onAttach(r0);
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x0131;
    L_0x010b:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "Fragment ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r9 = " did not call through to super.onAttach()";
        r1 = r1.append(r9);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x012a:
        r0 = r10.mHost;
        r0 = r0.getFragmentManagerImpl();
        goto L_0x00f9;
    L_0x0131:
        r0 = r11.mParentFragment;
        if (r0 != 0) goto L_0x013a;
    L_0x0135:
        r0 = r10.mHost;
        r0.onAttachFragment(r11);
    L_0x013a:
        r0 = r11.mRetaining;
        if (r0 != 0) goto L_0x0143;
    L_0x013e:
        r0 = r11.mSavedFragmentState;
        r11.performCreate(r0);
    L_0x0143:
        r0 = 0;
        r11.mRetaining = r0;
        r0 = r11.mFromLayout;
        if (r0 == 0) goto L_0x017f;
    L_0x014a:
        r0 = r11.mSavedFragmentState;
        r0 = r11.getLayoutInflater(r0);
        r1 = 0;
        r9 = r11.mSavedFragmentState;
        r0 = r11.performCreateView(r0, r1, r9);
        r11.mView = r0;
        r0 = r11.mView;
        if (r0 == 0) goto L_0x02d1;
    L_0x015d:
        r0 = r11.mView;
        r11.mInnerView = r0;
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 11;
        if (r0 < r1) goto L_0x02c7;
    L_0x0167:
        r0 = r11.mView;
        r1 = 0;
        android.support.v4.view.ViewCompat.setSaveFromParentEnabled(r0, r1);
    L_0x016d:
        r0 = r11.mHidden;
        if (r0 == 0) goto L_0x0178;
    L_0x0171:
        r0 = r11.mView;
        r1 = 8;
        r0.setVisibility(r1);
    L_0x0178:
        r0 = r11.mView;
        r1 = r11.mSavedFragmentState;
        r11.onViewCreated(r0, r1);
    L_0x017f:
        r0 = 1;
        if (r12 <= r0) goto L_0x027b;
    L_0x0182:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x019e;
    L_0x0186:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "moveto ACTIVITY_CREATED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x019e:
        r0 = r11.mFromLayout;
        if (r0 != 0) goto L_0x026a;
    L_0x01a2:
        r5 = 0;
        r0 = r11.mContainerId;
        if (r0 == 0) goto L_0x021c;
    L_0x01a7:
        r0 = r11.mContainerId;
        r1 = -1;
        if (r0 != r1) goto L_0x01cd;
    L_0x01ac:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "Cannot create fragment ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r9 = " for a container view with no id";
        r1 = r1.append(r9);
        r1 = r1.toString();
        r0.<init>(r1);
        r10.throwException(r0);
    L_0x01cd:
        r0 = r10.mContainer;
        r1 = r11.mContainerId;
        r5 = r0.onFindViewById(r1);
        r5 = (android.view.ViewGroup) r5;
        if (r5 != 0) goto L_0x021c;
    L_0x01d9:
        r0 = r11.mRestored;
        if (r0 != 0) goto L_0x021c;
    L_0x01dd:
        r0 = r11.getResources();	 Catch:{ NotFoundException -> 0x02d6 }
        r1 = r11.mContainerId;	 Catch:{ NotFoundException -> 0x02d6 }
        r7 = r0.getResourceName(r1);	 Catch:{ NotFoundException -> 0x02d6 }
    L_0x01e7:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "No view found for id 0x";
        r1 = r1.append(r9);
        r9 = r11.mContainerId;
        r9 = java.lang.Integer.toHexString(r9);
        r1 = r1.append(r9);
        r9 = " (";
        r1 = r1.append(r9);
        r1 = r1.append(r7);
        r9 = ") for fragment ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        r0.<init>(r1);
        r10.throwException(r0);
    L_0x021c:
        r11.mContainer = r5;
        r0 = r11.mSavedFragmentState;
        r0 = r11.getLayoutInflater(r0);
        r1 = r11.mSavedFragmentState;
        r0 = r11.performCreateView(r0, r5, r1);
        r11.mView = r0;
        r0 = r11.mView;
        if (r0 == 0) goto L_0x02e5;
    L_0x0230:
        r0 = r11.mView;
        r11.mInnerView = r0;
        r0 = android.os.Build.VERSION.SDK_INT;
        r1 = 11;
        if (r0 < r1) goto L_0x02db;
    L_0x023a:
        r0 = r11.mView;
        r1 = 0;
        android.support.v4.view.ViewCompat.setSaveFromParentEnabled(r0, r1);
    L_0x0240:
        if (r5 == 0) goto L_0x0258;
    L_0x0242:
        r0 = 1;
        r3 = r10.loadAnimation(r11, r13, r0, r14);
        if (r3 == 0) goto L_0x0253;
    L_0x0249:
        r0 = r11.mView;
        r10.setHWLayerAnimListenerIfAlpha(r0, r3);
        r0 = r11.mView;
        r0.startAnimation(r3);
    L_0x0253:
        r0 = r11.mView;
        r5.addView(r0);
    L_0x0258:
        r0 = r11.mHidden;
        if (r0 == 0) goto L_0x0263;
    L_0x025c:
        r0 = r11.mView;
        r1 = 8;
        r0.setVisibility(r1);
    L_0x0263:
        r0 = r11.mView;
        r1 = r11.mSavedFragmentState;
        r11.onViewCreated(r0, r1);
    L_0x026a:
        r0 = r11.mSavedFragmentState;
        r11.performActivityCreated(r0);
        r0 = r11.mView;
        if (r0 == 0) goto L_0x0278;
    L_0x0273:
        r0 = r11.mSavedFragmentState;
        r11.restoreViewState(r0);
    L_0x0278:
        r0 = 0;
        r11.mSavedFragmentState = r0;
    L_0x027b:
        r0 = 3;
        if (r12 <= r0) goto L_0x029d;
    L_0x027e:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x029a;
    L_0x0282:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "moveto STARTED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x029a:
        r11.performStart();
    L_0x029d:
        r0 = 4;
        if (r12 <= r0) goto L_0x0046;
    L_0x02a0:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x02bc;
    L_0x02a4:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "moveto RESUMED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x02bc:
        r11.performResume();
        r0 = 0;
        r11.mSavedFragmentState = r0;
        r0 = 0;
        r11.mSavedViewState = r0;
        goto L_0x0046;
    L_0x02c7:
        r0 = r11.mView;
        r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0);
        r11.mView = r0;
        goto L_0x016d;
    L_0x02d1:
        r0 = 0;
        r11.mInnerView = r0;
        goto L_0x017f;
    L_0x02d6:
        r6 = move-exception;
        r7 = "unknown";
        goto L_0x01e7;
    L_0x02db:
        r0 = r11.mView;
        r0 = android.support.v4.app.NoSaveStateFrameLayout.wrap(r0);
        r11.mView = r0;
        goto L_0x0240;
    L_0x02e5:
        r0 = 0;
        r11.mInnerView = r0;
        goto L_0x026a;
    L_0x02e9:
        r0 = r11.mState;
        if (r0 <= r12) goto L_0x0046;
    L_0x02ed:
        r0 = r11.mState;
        switch(r0) {
            case 1: goto L_0x02f4;
            case 2: goto L_0x0376;
            case 3: goto L_0x0354;
            case 4: goto L_0x0332;
            case 5: goto L_0x0310;
            default: goto L_0x02f2;
        };
    L_0x02f2:
        goto L_0x0046;
    L_0x02f4:
        r0 = 1;
        if (r12 >= r0) goto L_0x0046;
    L_0x02f7:
        r0 = r10.mDestroyed;
        if (r0 == 0) goto L_0x0307;
    L_0x02fb:
        r0 = r11.mAnimatingAway;
        if (r0 == 0) goto L_0x0307;
    L_0x02ff:
        r8 = r11.mAnimatingAway;
        r0 = 0;
        r11.mAnimatingAway = r0;
        r8.clearAnimation();
    L_0x0307:
        r0 = r11.mAnimatingAway;
        if (r0 == 0) goto L_0x03ef;
    L_0x030b:
        r11.mStateAfterAnimating = r12;
        r12 = 1;
        goto L_0x0046;
    L_0x0310:
        r0 = 5;
        if (r12 >= r0) goto L_0x0332;
    L_0x0313:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x032f;
    L_0x0317:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "movefrom RESUMED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x032f:
        r11.performPause();
    L_0x0332:
        r0 = 4;
        if (r12 >= r0) goto L_0x0354;
    L_0x0335:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0351;
    L_0x0339:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "movefrom STARTED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0351:
        r11.performStop();
    L_0x0354:
        r0 = 3;
        if (r12 >= r0) goto L_0x0376;
    L_0x0357:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0373;
    L_0x035b:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "movefrom STOPPED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0373:
        r11.performReallyStop();
    L_0x0376:
        r0 = 2;
        if (r12 >= r0) goto L_0x02f4;
    L_0x0379:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x0395;
    L_0x037d:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "movefrom ACTIVITY_CREATED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x0395:
        r0 = r11.mView;
        if (r0 == 0) goto L_0x03a8;
    L_0x0399:
        r0 = r10.mHost;
        r0 = r0.onShouldSaveFragmentState(r11);
        if (r0 == 0) goto L_0x03a8;
    L_0x03a1:
        r0 = r11.mSavedViewState;
        if (r0 != 0) goto L_0x03a8;
    L_0x03a5:
        r10.saveFragmentViewState(r11);
    L_0x03a8:
        r11.performDestroyView();
        r0 = r11.mView;
        if (r0 == 0) goto L_0x03dc;
    L_0x03af:
        r0 = r11.mContainer;
        if (r0 == 0) goto L_0x03dc;
    L_0x03b3:
        r3 = 0;
        r0 = r10.mCurState;
        if (r0 <= 0) goto L_0x03c1;
    L_0x03b8:
        r0 = r10.mDestroyed;
        if (r0 != 0) goto L_0x03c1;
    L_0x03bc:
        r0 = 0;
        r3 = r10.loadAnimation(r11, r13, r0, r14);
    L_0x03c1:
        if (r3 == 0) goto L_0x03e7;
    L_0x03c3:
        r4 = r11;
        r5 = r11.mContainer;
        r0 = r11.mView;
        r11.mAnimatingAway = r0;
        r11.mStateAfterAnimating = r12;
        r2 = r11.mView;
        r0 = new android.support.v4.app.FragmentManagerImpl$5;
        r1 = r10;
        r0.<init>(r2, r3, r4, r5);
        r3.setAnimationListener(r0);
        r0 = r11.mView;
        r0.startAnimation(r3);
    L_0x03dc:
        r0 = 0;
        r11.mContainer = r0;
        r0 = 0;
        r11.mView = r0;
        r0 = 0;
        r11.mInnerView = r0;
        goto L_0x02f4;
    L_0x03e7:
        r0 = r11.mContainer;
        r1 = r11.mView;
        r0.removeView(r1);
        goto L_0x03dc;
    L_0x03ef:
        r0 = DEBUG;
        if (r0 == 0) goto L_0x040b;
    L_0x03f3:
        r0 = "FragmentManager";
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "movefrom CREATED: ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r1 = r1.toString();
        android.util.Log.v(r0, r1);
    L_0x040b:
        r0 = r11.mRetaining;
        if (r0 != 0) goto L_0x043b;
    L_0x040f:
        r11.performDestroy();
    L_0x0412:
        r0 = 0;
        r11.mCalled = r0;
        r11.onDetach();
        r0 = r11.mCalled;
        if (r0 != 0) goto L_0x043f;
    L_0x041c:
        r0 = new android.support.v4.app.SuperNotCalledException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r9 = "Fragment ";
        r1 = r1.append(r9);
        r1 = r1.append(r11);
        r9 = " did not call through to super.onDetach()";
        r1 = r1.append(r9);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x043b:
        r0 = 0;
        r11.mState = r0;
        goto L_0x0412;
    L_0x043f:
        if (r15 != 0) goto L_0x0046;
    L_0x0441:
        r0 = r11.mRetaining;
        if (r0 != 0) goto L_0x044a;
    L_0x0445:
        r10.makeInactive(r11);
        goto L_0x0046;
    L_0x044a:
        r0 = 0;
        r11.mHost = r0;
        r0 = 0;
        r11.mParentFragment = r0;
        r0 = 0;
        r11.mFragmentManager = r0;
        r0 = 0;
        r11.mChildFragmentManager = r0;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    void moveToState(Fragment f) {
        moveToState(f, this.mCurState, 0, 0, HONEYCOMB);
    }

    void moveToState(int newState, boolean always) {
        moveToState(newState, 0, 0, always);
    }

    void moveToState(int newState, int transit, int transitStyle, boolean always) {
        if (this.mHost == null && newState != 0) {
            throw new IllegalStateException("No host");
        } else if (always || this.mCurState != newState) {
            this.mCurState = newState;
            if (this.mActive != null) {
                boolean loadersRunning = HONEYCOMB;
                for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                    Fragment f = (Fragment) this.mActive.get(i);
                    if (f != null) {
                        moveToState(f, newState, transit, transitStyle, HONEYCOMB);
                        if (f.mLoaderManager != null) {
                            loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                        }
                    }
                }
                if (!loadersRunning) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == ANIM_STYLE_FADE_ENTER) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = HONEYCOMB;
                }
            }
        }
    }

    void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    performPendingDeferredStart(f);
                }
            }
        }
    }

    void makeActive(Fragment f) {
        if (f.mIndex < 0) {
            if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
                if (this.mActive == null) {
                    this.mActive = new ArrayList();
                }
                f.setIndex(this.mActive.size(), this.mParent);
                this.mActive.add(f);
            } else {
                f.setIndex(((Integer) this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
                this.mActive.set(f.mIndex, f);
            }
            if (DEBUG) {
                Log.v(TAG, "Allocated fragment index " + f);
            }
        }
    }

    void makeInactive(Fragment f) {
        if (f.mIndex >= 0) {
            if (DEBUG) {
                Log.v(TAG, "Freeing fragment index " + f);
            }
            this.mActive.set(f.mIndex, null);
            if (this.mAvailIndices == null) {
                this.mAvailIndices = new ArrayList();
            }
            this.mAvailIndices.add(Integer.valueOf(f.mIndex));
            this.mHost.inactivateFragment(f.mWho);
            f.initState();
        }
    }

    public void addFragment(Fragment fragment, boolean moveToStateNow) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList();
        }
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (!fragment.mDetached) {
            if (this.mAdded.contains(fragment)) {
                throw new IllegalStateException("Fragment already added: " + fragment);
            }
            this.mAdded.add(fragment);
            fragment.mAdded = true;
            fragment.mRemoving = HONEYCOMB;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (moveToStateNow) {
                moveToState(fragment);
            }
        }
    }

    public void removeFragment(Fragment fragment, int transition, int transitionStyle) {
        boolean inactive;
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        if (fragment.isInBackStack()) {
            inactive = HONEYCOMB;
        } else {
            inactive = true;
        }
        if (!fragment.mDetached || inactive) {
            int i;
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = HONEYCOMB;
            fragment.mRemoving = true;
            if (inactive) {
                i = 0;
            } else {
                i = ANIM_STYLE_OPEN_ENTER;
            }
            moveToState(fragment, i, transition, transitionStyle, HONEYCOMB);
        }
    }

    public void hideFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mView != null) {
                Animation anim = loadAnimation(fragment, transition, HONEYCOMB, transitionStyle);
                if (anim != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    fragment.mView.startAnimation(anim);
                }
                fragment.mView.setVisibility(8);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(true);
        }
    }

    public void showFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = HONEYCOMB;
            if (fragment.mView != null) {
                Animation anim = loadAnimation(fragment, transition, true, transitionStyle);
                if (anim != null) {
                    setHWLayerAnimListenerIfAlpha(fragment.mView, anim);
                    fragment.mView.startAnimation(anim);
                }
                fragment.mView.setVisibility(0);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(HONEYCOMB);
        }
    }

    public void detachFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (!fragment.mDetached) {
            fragment.mDetached = true;
            if (fragment.mAdded) {
                if (this.mAdded != null) {
                    if (DEBUG) {
                        Log.v(TAG, "remove from detach: " + fragment);
                    }
                    this.mAdded.remove(fragment);
                }
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = HONEYCOMB;
                moveToState(fragment, ANIM_STYLE_OPEN_ENTER, transition, transitionStyle, HONEYCOMB);
            }
        }
    }

    public void attachFragment(Fragment fragment, int transition, int transitionStyle) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = HONEYCOMB;
            if (!fragment.mAdded) {
                if (this.mAdded == null) {
                    this.mAdded = new ArrayList();
                }
                if (this.mAdded.contains(fragment)) {
                    throw new IllegalStateException("Fragment already added: " + fragment);
                }
                if (DEBUG) {
                    Log.v(TAG, "add from attach: " + fragment);
                }
                this.mAdded.add(fragment);
                fragment.mAdded = true;
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                moveToState(fragment, this.mCurState, transition, transitionStyle, HONEYCOMB);
            }
        }
    }

    public Fragment findFragmentById(int id) {
        int i;
        Fragment f;
        if (this.mAdded != null) {
            for (i = this.mAdded.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
            }
        }
        if (this.mActive != null) {
            for (i = this.mActive.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mActive.get(i);
                if (f != null && f.mFragmentId == id) {
                    return f;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByTag(String tag) {
        int i;
        Fragment f;
        if (!(this.mAdded == null || tag == null)) {
            for (i = this.mAdded.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        if (!(this.mActive == null || tag == null)) {
            for (i = this.mActive.size() - 1; i >= 0; i--) {
                f = (Fragment) this.mActive.get(i);
                if (f != null && tag.equals(f.mTag)) {
                    return f;
                }
            }
        }
        return null;
    }

    public Fragment findFragmentByWho(String who) {
        if (!(this.mActive == null || who == null)) {
            for (int i = this.mActive.size() - 1; i >= 0; i--) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    f = f.findFragmentByWho(who);
                    if (f != null) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        } else if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }

    public void enqueueAction(Runnable action, boolean allowStateLoss) {
        if (!allowStateLoss) {
            checkStateLoss();
        }
        synchronized (this) {
            if (this.mDestroyed || this.mHost == null) {
                throw new IllegalStateException("Activity has been destroyed");
            }
            if (this.mPendingActions == null) {
                this.mPendingActions = new ArrayList();
            }
            this.mPendingActions.add(action);
            if (this.mPendingActions.size() == ANIM_STYLE_OPEN_ENTER) {
                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                this.mHost.getHandler().post(this.mExecCommit);
            }
        }
    }

    public int allocBackStackIndex(BackStackRecord bse) {
        synchronized (this) {
            int index;
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList();
                }
                index = this.mBackStackIndices.size();
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.add(bse);
                return index;
            }
            index = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
            if (DEBUG) {
                Log.v(TAG, "Adding back stack index " + index + " with " + bse);
            }
            this.mBackStackIndices.set(index, bse);
            return index;
        }
    }

    public void setBackStackIndex(int index, BackStackRecord bse) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int N = this.mBackStackIndices.size();
            if (index < N) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + index + " to " + bse);
                }
                this.mBackStackIndices.set(index, bse);
            } else {
                while (N < index) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding available back stack index " + N);
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(N));
                    N += ANIM_STYLE_OPEN_ENTER;
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + index + " with " + bse);
                }
                this.mBackStackIndices.add(bse);
            }
        }
    }

    public void freeBackStackIndex(int index) {
        synchronized (this) {
            this.mBackStackIndices.set(index, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + index);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(index));
        }
    }

    public void execSingleAction(Runnable action, boolean allowStateLoss) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        } else if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else {
            if (!allowStateLoss) {
                checkStateLoss();
            }
            this.mExecutingActions = true;
            action.run();
            this.mExecutingActions = HONEYCOMB;
            doPendingDeferredStart();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean execPendingActions() {
        /*
        r5 = this;
        r3 = r5.mExecutingActions;
        if (r3 == 0) goto L_0x000c;
    L_0x0004:
        r3 = new java.lang.IllegalStateException;
        r4 = "FragmentManager is already executing transactions";
        r3.<init>(r4);
        throw r3;
    L_0x000c:
        r3 = android.os.Looper.myLooper();
        r4 = r5.mHost;
        r4 = r4.getHandler();
        r4 = r4.getLooper();
        if (r3 == r4) goto L_0x0024;
    L_0x001c:
        r3 = new java.lang.IllegalStateException;
        r4 = "Must be called from main thread of fragment host";
        r3.<init>(r4);
        throw r3;
    L_0x0024:
        r0 = 0;
    L_0x0025:
        monitor-enter(r5);
        r3 = r5.mPendingActions;	 Catch:{ all -> 0x0077 }
        if (r3 == 0) goto L_0x0032;
    L_0x002a:
        r3 = r5.mPendingActions;	 Catch:{ all -> 0x0077 }
        r3 = r3.size();	 Catch:{ all -> 0x0077 }
        if (r3 != 0) goto L_0x0037;
    L_0x0032:
        monitor-exit(r5);	 Catch:{ all -> 0x0077 }
        r5.doPendingDeferredStart();
        return r0;
    L_0x0037:
        r3 = r5.mPendingActions;	 Catch:{ all -> 0x0077 }
        r2 = r3.size();	 Catch:{ all -> 0x0077 }
        r3 = r5.mTmpActions;	 Catch:{ all -> 0x0077 }
        if (r3 == 0) goto L_0x0046;
    L_0x0041:
        r3 = r5.mTmpActions;	 Catch:{ all -> 0x0077 }
        r3 = r3.length;	 Catch:{ all -> 0x0077 }
        if (r3 >= r2) goto L_0x004a;
    L_0x0046:
        r3 = new java.lang.Runnable[r2];	 Catch:{ all -> 0x0077 }
        r5.mTmpActions = r3;	 Catch:{ all -> 0x0077 }
    L_0x004a:
        r3 = r5.mPendingActions;	 Catch:{ all -> 0x0077 }
        r4 = r5.mTmpActions;	 Catch:{ all -> 0x0077 }
        r3.toArray(r4);	 Catch:{ all -> 0x0077 }
        r3 = r5.mPendingActions;	 Catch:{ all -> 0x0077 }
        r3.clear();	 Catch:{ all -> 0x0077 }
        r3 = r5.mHost;	 Catch:{ all -> 0x0077 }
        r3 = r3.getHandler();	 Catch:{ all -> 0x0077 }
        r4 = r5.mExecCommit;	 Catch:{ all -> 0x0077 }
        r3.removeCallbacks(r4);	 Catch:{ all -> 0x0077 }
        monitor-exit(r5);	 Catch:{ all -> 0x0077 }
        r3 = 1;
        r5.mExecutingActions = r3;
        r1 = 0;
    L_0x0066:
        if (r1 >= r2) goto L_0x007a;
    L_0x0068:
        r3 = r5.mTmpActions;
        r3 = r3[r1];
        r3.run();
        r3 = r5.mTmpActions;
        r4 = 0;
        r3[r1] = r4;
        r1 = r1 + 1;
        goto L_0x0066;
    L_0x0077:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0077 }
        throw r3;
    L_0x007a:
        r3 = 0;
        r5.mExecutingActions = r3;
        r0 = 1;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.execPendingActions():boolean");
    }

    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean loadersRunning = HONEYCOMB;
            for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (!(f == null || f.mLoaderManager == null)) {
                    loadersRunning |= f.mLoaderManager.hasRunningLoaders();
                }
            }
            if (!loadersRunning) {
                this.mHavePendingDeferredStart = HONEYCOMB;
                startPendingDeferredFragments();
            }
        }
    }

    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); i += ANIM_STYLE_OPEN_ENTER) {
                ((OnBackStackChangedListener) this.mBackStackChangeListeners.get(i)).onBackStackChanged();
            }
        }
    }

    void addBackStackState(BackStackRecord state) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(state);
        reportBackStackChanged();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    boolean popBackStackState(android.os.Handler r13, java.lang.String r14, int r15, int r16) {
        /*
        r12 = this;
        r9 = r12.mBackStack;
        if (r9 != 0) goto L_0x0006;
    L_0x0004:
        r9 = 0;
    L_0x0005:
        return r9;
    L_0x0006:
        if (r14 != 0) goto L_0x0039;
    L_0x0008:
        if (r15 >= 0) goto L_0x0039;
    L_0x000a:
        r9 = r16 & 1;
        if (r9 != 0) goto L_0x0039;
    L_0x000e:
        r9 = r12.mBackStack;
        r9 = r9.size();
        r5 = r9 + -1;
        if (r5 >= 0) goto L_0x001a;
    L_0x0018:
        r9 = 0;
        goto L_0x0005;
    L_0x001a:
        r9 = r12.mBackStack;
        r1 = r9.remove(r5);
        r1 = (android.support.v4.app.BackStackRecord) r1;
        r2 = new android.util.SparseArray;
        r2.<init>();
        r6 = new android.util.SparseArray;
        r6.<init>();
        r1.calculateBackFragments(r2, r6);
        r9 = 1;
        r10 = 0;
        r1.popFromBackStack(r9, r10, r2, r6);
        r12.reportBackStackChanged();
    L_0x0037:
        r9 = 1;
        goto L_0x0005;
    L_0x0039:
        r4 = -1;
        if (r14 != 0) goto L_0x003e;
    L_0x003c:
        if (r15 < 0) goto L_0x008e;
    L_0x003e:
        r9 = r12.mBackStack;
        r9 = r9.size();
        r4 = r9 + -1;
    L_0x0046:
        if (r4 < 0) goto L_0x005c;
    L_0x0048:
        r9 = r12.mBackStack;
        r1 = r9.get(r4);
        r1 = (android.support.v4.app.BackStackRecord) r1;
        if (r14 == 0) goto L_0x0060;
    L_0x0052:
        r9 = r1.getName();
        r9 = r14.equals(r9);
        if (r9 == 0) goto L_0x0060;
    L_0x005c:
        if (r4 >= 0) goto L_0x0069;
    L_0x005e:
        r9 = 0;
        goto L_0x0005;
    L_0x0060:
        if (r15 < 0) goto L_0x0066;
    L_0x0062:
        r9 = r1.mIndex;
        if (r15 == r9) goto L_0x005c;
    L_0x0066:
        r4 = r4 + -1;
        goto L_0x0046;
    L_0x0069:
        r9 = r16 & 1;
        if (r9 == 0) goto L_0x008e;
    L_0x006d:
        r4 = r4 + -1;
    L_0x006f:
        if (r4 < 0) goto L_0x008e;
    L_0x0071:
        r9 = r12.mBackStack;
        r1 = r9.get(r4);
        r1 = (android.support.v4.app.BackStackRecord) r1;
        if (r14 == 0) goto L_0x0085;
    L_0x007b:
        r9 = r1.getName();
        r9 = r14.equals(r9);
        if (r9 != 0) goto L_0x008b;
    L_0x0085:
        if (r15 < 0) goto L_0x008e;
    L_0x0087:
        r9 = r1.mIndex;
        if (r15 != r9) goto L_0x008e;
    L_0x008b:
        r4 = r4 + -1;
        goto L_0x006f;
    L_0x008e:
        r9 = r12.mBackStack;
        r9 = r9.size();
        r9 = r9 + -1;
        if (r4 != r9) goto L_0x009b;
    L_0x0098:
        r9 = 0;
        goto L_0x0005;
    L_0x009b:
        r8 = new java.util.ArrayList;
        r8.<init>();
        r9 = r12.mBackStack;
        r9 = r9.size();
        r3 = r9 + -1;
    L_0x00a8:
        if (r3 <= r4) goto L_0x00b6;
    L_0x00aa:
        r9 = r12.mBackStack;
        r9 = r9.remove(r3);
        r8.add(r9);
        r3 = r3 + -1;
        goto L_0x00a8;
    L_0x00b6:
        r9 = r8.size();
        r0 = r9 + -1;
        r2 = new android.util.SparseArray;
        r2.<init>();
        r6 = new android.util.SparseArray;
        r6.<init>();
        r3 = 0;
    L_0x00c7:
        if (r3 > r0) goto L_0x00d5;
    L_0x00c9:
        r9 = r8.get(r3);
        r9 = (android.support.v4.app.BackStackRecord) r9;
        r9.calculateBackFragments(r2, r6);
        r3 = r3 + 1;
        goto L_0x00c7;
    L_0x00d5:
        r7 = 0;
        r3 = 0;
    L_0x00d7:
        if (r3 > r0) goto L_0x010b;
    L_0x00d9:
        r9 = DEBUG;
        if (r9 == 0) goto L_0x00f9;
    L_0x00dd:
        r9 = "FragmentManager";
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Popping back stack state: ";
        r10 = r10.append(r11);
        r11 = r8.get(r3);
        r10 = r10.append(r11);
        r10 = r10.toString();
        android.util.Log.v(r9, r10);
    L_0x00f9:
        r9 = r8.get(r3);
        r9 = (android.support.v4.app.BackStackRecord) r9;
        if (r3 != r0) goto L_0x0109;
    L_0x0101:
        r10 = 1;
    L_0x0102:
        r7 = r9.popFromBackStack(r10, r7, r2, r6);
        r3 = r3 + 1;
        goto L_0x00d7;
    L_0x0109:
        r10 = 0;
        goto L_0x0102;
    L_0x010b:
        r12.reportBackStackChanged();
        goto L_0x0037;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.popBackStackState(android.os.Handler, java.lang.String, int, int):boolean");
    }

    FragmentManagerNonConfig retainNonConfig() {
        ArrayList<Fragment> fragments = null;
        ArrayList<FragmentManagerNonConfig> childFragments = null;
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mActive.get(i);
                if (f != null) {
                    if (f.mRetainInstance) {
                        if (fragments == null) {
                            fragments = new ArrayList();
                        }
                        fragments.add(f);
                        f.mRetaining = true;
                        f.mTargetIndex = f.mTarget != null ? f.mTarget.mIndex : -1;
                        if (DEBUG) {
                            Log.v(TAG, "retainNonConfig: keeping retained " + f);
                        }
                    }
                    boolean addedChild = HONEYCOMB;
                    if (f.mChildFragmentManager != null) {
                        FragmentManagerNonConfig child = f.mChildFragmentManager.retainNonConfig();
                        if (child != null) {
                            if (childFragments == null) {
                                childFragments = new ArrayList();
                                for (int j = 0; j < i; j += ANIM_STYLE_OPEN_ENTER) {
                                    childFragments.add(null);
                                }
                            }
                            childFragments.add(child);
                            addedChild = true;
                        }
                    }
                    if (!(childFragments == null || addedChild)) {
                        childFragments.add(null);
                    }
                }
            }
        }
        if (fragments == null && childFragments == null) {
            return null;
        }
        return new FragmentManagerNonConfig(fragments, childFragments);
    }

    void saveFragmentViewState(Fragment f) {
        if (f.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = new SparseArray();
            } else {
                this.mStateArray.clear();
            }
            f.mInnerView.saveHierarchyState(this.mStateArray);
            if (this.mStateArray.size() > 0) {
                f.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }

    Bundle saveFragmentBasicState(Fragment f) {
        Bundle result = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        f.performSaveInstanceState(this.mStateBundle);
        if (!this.mStateBundle.isEmpty()) {
            result = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (f.mView != null) {
            saveFragmentViewState(f);
        }
        if (f.mSavedViewState != null) {
            if (result == null) {
                result = new Bundle();
            }
            result.putSparseParcelableArray(VIEW_STATE_TAG, f.mSavedViewState);
        }
        if (!f.mUserVisibleHint) {
            if (result == null) {
                result = new Bundle();
            }
            result.putBoolean(USER_VISIBLE_HINT_TAG, f.mUserVisibleHint);
        }
        return result;
    }

    Parcelable saveAllState() {
        execPendingActions();
        if (HONEYCOMB) {
            this.mStateSaved = true;
        }
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int i;
        int N = this.mActive.size();
        FragmentState[] active = new FragmentState[N];
        boolean haveFragments = HONEYCOMB;
        for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
            Fragment f = (Fragment) this.mActive.get(i);
            if (f != null) {
                if (f.mIndex < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + f + " has cleared index: " + f.mIndex));
                }
                haveFragments = true;
                FragmentState fs = new FragmentState(f);
                active[i] = fs;
                if (f.mState <= 0 || fs.mSavedFragmentState != null) {
                    fs.mSavedFragmentState = f.mSavedFragmentState;
                } else {
                    fs.mSavedFragmentState = saveFragmentBasicState(f);
                    if (f.mTarget != null) {
                        if (f.mTarget.mIndex < 0) {
                            throwException(new IllegalStateException("Failure saving state: " + f + " has target not in fragment manager: " + f.mTarget));
                        }
                        if (fs.mSavedFragmentState == null) {
                            fs.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fs.mSavedFragmentState, TARGET_STATE_TAG, f.mTarget);
                        if (f.mTargetRequestCode != 0) {
                            fs.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, f.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + f + ": " + fs.mSavedFragmentState);
                }
            }
        }
        if (haveFragments) {
            int[] added = null;
            BackStackState[] backStack = null;
            if (this.mAdded != null) {
                N = this.mAdded.size();
                if (N > 0) {
                    added = new int[N];
                    for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                        added[i] = ((Fragment) this.mAdded.get(i)).mIndex;
                        if (added[i] < 0) {
                            throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i) + " has cleared index: " + added[i]));
                        }
                        if (DEBUG) {
                            Log.v(TAG, "saveAllState: adding fragment #" + i + ": " + this.mAdded.get(i));
                        }
                    }
                }
            }
            if (this.mBackStack != null) {
                N = this.mBackStack.size();
                if (N > 0) {
                    backStack = new BackStackState[N];
                    for (i = 0; i < N; i += ANIM_STYLE_OPEN_ENTER) {
                        backStack[i] = new BackStackState((BackStackRecord) this.mBackStack.get(i));
                        if (DEBUG) {
                            Log.v(TAG, "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i));
                        }
                    }
                }
            }
            Parcelable fms = new FragmentManagerState();
            fms.mActive = active;
            fms.mAdded = added;
            fms.mBackStack = backStack;
            return fms;
        } else if (!DEBUG) {
            return null;
        } else {
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
    }

    void restoreAllState(Parcelable state, FragmentManagerNonConfig nonConfig) {
        if (state != null) {
            FragmentManagerState fms = (FragmentManagerState) state;
            if (fms.mActive != null) {
                List<Fragment> nonConfigFragments;
                int count;
                int i;
                Fragment f;
                FragmentState fs;
                List<FragmentManagerNonConfig> childNonConfigs = null;
                if (nonConfig != null) {
                    nonConfigFragments = nonConfig.getFragments();
                    childNonConfigs = nonConfig.getChildNonConfigs();
                    count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (i = 0; i < count; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) nonConfigFragments.get(i);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: re-attaching retained " + f);
                        }
                        fs = fms.mActive[f.mIndex];
                        fs.mInstance = f;
                        f.mSavedViewState = null;
                        f.mBackStackNesting = 0;
                        f.mInLayout = HONEYCOMB;
                        f.mAdded = HONEYCOMB;
                        f.mTarget = null;
                        if (fs.mSavedFragmentState != null) {
                            fs.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            f.mSavedViewState = fs.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                            f.mSavedFragmentState = fs.mSavedFragmentState;
                        }
                    }
                }
                this.mActive = new ArrayList(fms.mActive.length);
                if (this.mAvailIndices != null) {
                    this.mAvailIndices.clear();
                }
                i = 0;
                while (i < fms.mActive.length) {
                    fs = fms.mActive[i];
                    if (fs != null) {
                        FragmentManagerNonConfig childNonConfig = null;
                        if (childNonConfigs != null && i < childNonConfigs.size()) {
                            childNonConfig = (FragmentManagerNonConfig) childNonConfigs.get(i);
                        }
                        f = fs.instantiate(this.mHost, this.mParent, childNonConfig);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: active #" + i + ": " + f);
                        }
                        this.mActive.add(f);
                        fs.mInstance = null;
                    } else {
                        this.mActive.add(null);
                        if (this.mAvailIndices == null) {
                            this.mAvailIndices = new ArrayList();
                        }
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: avail #" + i);
                        }
                        this.mAvailIndices.add(Integer.valueOf(i));
                    }
                    i += ANIM_STYLE_OPEN_ENTER;
                }
                if (nonConfig != null) {
                    nonConfigFragments = nonConfig.getFragments();
                    count = nonConfigFragments != null ? nonConfigFragments.size() : 0;
                    for (i = 0; i < count; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) nonConfigFragments.get(i);
                        if (f.mTargetIndex >= 0) {
                            if (f.mTargetIndex < this.mActive.size()) {
                                f.mTarget = (Fragment) this.mActive.get(f.mTargetIndex);
                            } else {
                                Log.w(TAG, "Re-attaching retained fragment " + f + " target no longer exists: " + f.mTargetIndex);
                                f.mTarget = null;
                            }
                        }
                    }
                }
                if (fms.mAdded != null) {
                    this.mAdded = new ArrayList(fms.mAdded.length);
                    for (i = 0; i < fms.mAdded.length; i += ANIM_STYLE_OPEN_ENTER) {
                        f = (Fragment) this.mActive.get(fms.mAdded[i]);
                        if (f == null) {
                            throwException(new IllegalStateException("No instantiated fragment for index #" + fms.mAdded[i]));
                        }
                        f.mAdded = true;
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: added #" + i + ": " + f);
                        }
                        if (this.mAdded.contains(f)) {
                            throw new IllegalStateException("Already added!");
                        }
                        this.mAdded.add(f);
                    }
                } else {
                    this.mAdded = null;
                }
                if (fms.mBackStack != null) {
                    this.mBackStack = new ArrayList(fms.mBackStack.length);
                    for (i = 0; i < fms.mBackStack.length; i += ANIM_STYLE_OPEN_ENTER) {
                        BackStackRecord bse = fms.mBackStack[i].instantiate(this);
                        if (DEBUG) {
                            Log.v(TAG, "restoreAllState: back stack #" + i + " (index " + bse.mIndex + "): " + bse);
                            bse.dump("  ", new PrintWriter(new LogWriter(TAG)), HONEYCOMB);
                        }
                        this.mBackStack.add(bse);
                        if (bse.mIndex >= 0) {
                            setBackStackIndex(bse.mIndex, bse);
                        }
                    }
                    return;
                }
                this.mBackStack = null;
            }
        }
    }

    public void attachController(FragmentHostCallback host, FragmentContainer container, Fragment parent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = host;
        this.mContainer = container;
        this.mParent = parent;
    }

    public void noteStateNotSaved() {
        this.mStateSaved = HONEYCOMB;
    }

    public void dispatchCreate() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
    }

    public void dispatchActivityCreated() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
    }

    public void dispatchStart() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
    }

    public void dispatchResume() {
        this.mStateSaved = HONEYCOMB;
        moveToState(ANIM_STYLE_FADE_ENTER, HONEYCOMB);
    }

    public void dispatchPause() {
        moveToState(ANIM_STYLE_CLOSE_EXIT, HONEYCOMB);
    }

    public void dispatchStop() {
        this.mStateSaved = true;
        moveToState(ANIM_STYLE_CLOSE_ENTER, HONEYCOMB);
    }

    public void dispatchReallyStop() {
        moveToState(ANIM_STYLE_OPEN_EXIT, HONEYCOMB);
    }

    public void dispatchDestroyView() {
        moveToState(ANIM_STYLE_OPEN_ENTER, HONEYCOMB);
    }

    public void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        moveToState(0, HONEYCOMB);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public void dispatchConfigurationChanged(Configuration newConfig) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performConfigurationChanged(newConfig);
                }
            }
        }
    }

    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performLowMemory();
                }
            }
        }
    }

    public boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int i;
        Fragment f;
        boolean show = HONEYCOMB;
        ArrayList<Fragment> newMenus = null;
        if (this.mAdded != null) {
            for (i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performCreateOptionsMenu(menu, inflater)) {
                    show = true;
                    if (newMenus == null) {
                        newMenus = new ArrayList();
                    }
                    newMenus.add(f);
                }
            }
        }
        if (this.mCreatedMenus != null) {
            for (i = 0; i < this.mCreatedMenus.size(); i += ANIM_STYLE_OPEN_ENTER) {
                f = (Fragment) this.mCreatedMenus.get(i);
                if (newMenus == null || !newMenus.contains(f)) {
                    f.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = newMenus;
        return show;
    }

    public boolean dispatchPrepareOptionsMenu(Menu menu) {
        boolean show = HONEYCOMB;
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performPrepareOptionsMenu(menu)) {
                    show = true;
                }
            }
        }
        return show;
    }

    public boolean dispatchOptionsItemSelected(MenuItem item) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performOptionsItemSelected(item)) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    public boolean dispatchContextItemSelected(MenuItem item) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null && f.performContextItemSelected(item)) {
                    return true;
                }
            }
        }
        return HONEYCOMB;
    }

    public void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); i += ANIM_STYLE_OPEN_ENTER) {
                Fragment f = (Fragment) this.mAdded.get(i);
                if (f != null) {
                    f.performOptionsMenuClosed(menu);
                }
            }
        }
    }

    public static int reverseTransit(int transit) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                return InputDeviceCompat.SOURCE_MOUSE;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case InputDeviceCompat.SOURCE_MOUSE /*8194*/:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    public static int transitToStyleIndex(int transit, boolean enter) {
        int animAttr = -1;
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /*4097*/:
                animAttr = enter ? ANIM_STYLE_OPEN_ENTER : ANIM_STYLE_OPEN_EXIT;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /*4099*/:
                animAttr = enter ? ANIM_STYLE_FADE_ENTER : ANIM_STYLE_FADE_EXIT;
                break;
            case InputDeviceCompat.SOURCE_MOUSE /*8194*/:
                animAttr = enter ? ANIM_STYLE_CLOSE_ENTER : ANIM_STYLE_CLOSE_EXIT;
                break;
        }
        return animAttr;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (!"fragment".equals(name)) {
            return null;
        }
        String fname = attrs.getAttributeValue(null, Name.LABEL);
        TypedArray a = context.obtainStyledAttributes(attrs, FragmentTag.Fragment);
        if (fname == null) {
            fname = a.getString(0);
        }
        int id = a.getResourceId(ANIM_STYLE_OPEN_ENTER, -1);
        String tag = a.getString(ANIM_STYLE_OPEN_EXIT);
        a.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), fname)) {
            return null;
        }
        int containerId;
        if (parent != null) {
            containerId = parent.getId();
        } else {
            containerId = 0;
        }
        if (containerId == -1 && id == -1 && tag == null) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + fname);
        }
        Fragment fragment;
        if (id != -1) {
            fragment = findFragmentById(id);
        } else {
            fragment = null;
        }
        if (fragment == null && tag != null) {
            fragment = findFragmentByTag(tag);
        }
        if (fragment == null && containerId != -1) {
            fragment = findFragmentById(containerId);
        }
        if (DEBUG) {
            Log.v(TAG, "onCreateView: id=0x" + Integer.toHexString(id) + " fname=" + fname + " existing=" + fragment);
        }
        if (fragment == null) {
            int i;
            fragment = Fragment.instantiate(context, fname);
            fragment.mFromLayout = true;
            if (id != 0) {
                i = id;
            } else {
                i = containerId;
            }
            fragment.mFragmentId = i;
            fragment.mContainerId = containerId;
            fragment.mTag = tag;
            fragment.mInLayout = true;
            fragment.mFragmentManager = this;
            fragment.mHost = this.mHost;
            fragment.onInflate(this.mHost.getContext(), attrs, fragment.mSavedFragmentState);
            addFragment(fragment, true);
        } else if (fragment.mInLayout) {
            throw new IllegalArgumentException(attrs.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(id) + ", tag " + tag + ", or parent id 0x" + Integer.toHexString(containerId) + " with another fragment for " + fname);
        } else {
            fragment.mInLayout = true;
            fragment.mHost = this.mHost;
            if (!fragment.mRetaining) {
                fragment.onInflate(this.mHost.getContext(), attrs, fragment.mSavedFragmentState);
            }
        }
        if (this.mCurState >= ANIM_STYLE_OPEN_ENTER || !fragment.mFromLayout) {
            moveToState(fragment);
        } else {
            moveToState(fragment, ANIM_STYLE_OPEN_ENTER, 0, 0, HONEYCOMB);
        }
        if (fragment.mView == null) {
            throw new IllegalStateException("Fragment " + fname + " did not create a view.");
        }
        if (id != 0) {
            fragment.mView.setId(id);
        }
        if (fragment.mView.getTag() == null) {
            fragment.mView.setTag(tag);
        }
        return fragment.mView;
    }

    LayoutInflaterFactory getLayoutInflaterFactory() {
        return this;
    }
}
