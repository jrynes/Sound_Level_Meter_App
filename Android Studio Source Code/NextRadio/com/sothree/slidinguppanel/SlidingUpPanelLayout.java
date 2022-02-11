package com.sothree.slidinguppanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.sothree.slidinguppanel.library.R;
import org.xbill.DNS.Type;
import org.xbill.DNS.Zone;

public class SlidingUpPanelLayout extends ViewGroup {
    private static final int[] DEFAULT_ATTRS;
    private static final int DEFAULT_FADE_COLOR = -1728053248;
    private static final int DEFAULT_MIN_FLING_VELOCITY = 400;
    private static final boolean DEFAULT_OVERLAY_FLAG = false;
    private static final int DEFAULT_PANEL_HEIGHT = 68;
    private static final int DEFAULT_PARALAX_OFFSET = 0;
    private static final int DEFAULT_SHADOW_HEIGHT = 4;
    private static final String TAG;
    private float mAnchorPoint;
    private boolean mCanSlide;
    private int mCoveredFadeColor;
    private final Paint mCoveredFadePaint;
    private final ViewDragHelper mDragHelper;
    private View mDragView;
    private int mDragViewResId;
    private boolean mFirstLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsSlidingEnabled;
    private boolean mIsSlidingUp;
    private boolean mIsUnableToDrag;
    private boolean mIsUsingDragViewTouchEvents;
    private View mMainView;
    private int mMinFlingVelocity;
    private boolean mOverlayContent;
    private int mPanelHeight;
    private PanelSlideListener mPanelSlideListener;
    private int mParalaxOffset;
    private final int mScrollTouchSlop;
    private final Drawable mShadowDrawable;
    private int mShadowHeight;
    private float mSlideOffset;
    private int mSlideRange;
    private SlideState mSlideState;
    private View mSlideableView;
    private final Rect mTmpRect;

    public interface PanelSlideListener {
        void onPanelAnchored(View view);

        void onPanelCollapsed(View view);

        void onPanelExpanded(View view);

        void onPanelSlide(View view, float f);
    }

    static {
        TAG = SlidingUpPanelLayout.class.getSimpleName();
        DEFAULT_ATTRS = new int[]{16842927};
    }

    public SlidingUpPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, DEFAULT_PARALAX_OFFSET);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;
        this.mCoveredFadeColor = DEFAULT_FADE_COLOR;
        this.mCoveredFadePaint = new Paint();
        this.mPanelHeight = -1;
        this.mShadowHeight = -1;
        this.mParalaxOffset = -1;
        this.mOverlayContent = DEFAULT_OVERLAY_FLAG;
        this.mDragViewResId = -1;
        this.mSlideState = SlideState.COLLAPSED;
        this.mAnchorPoint = 0.0f;
        this.mFirstLayout = true;
        this.mTmpRect = new Rect();
        if (isInEditMode()) {
            this.mShadowDrawable = null;
            this.mScrollTouchSlop = DEFAULT_PARALAX_OFFSET;
            this.mDragHelper = null;
            return;
        }
        if (attrs != null) {
            TypedArray defAttrs = context.obtainStyledAttributes(attrs, DEFAULT_ATTRS);
            if (defAttrs != null) {
                int gravity = defAttrs.getInt(DEFAULT_PARALAX_OFFSET, DEFAULT_PARALAX_OFFSET);
                if (gravity == 48 || gravity == 80) {
                    boolean z;
                    if (gravity == 80) {
                        z = true;
                    } else {
                        z = DEFAULT_OVERLAY_FLAG;
                    }
                    this.mIsSlidingUp = z;
                } else {
                    throw new IllegalArgumentException("gravity must be set to either top or bottom");
                }
            }
            defAttrs.recycle();
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout);
            if (ta != null) {
                this.mPanelHeight = ta.getDimensionPixelSize(DEFAULT_PARALAX_OFFSET, -1);
                this.mShadowHeight = ta.getDimensionPixelSize(1, -1);
                this.mParalaxOffset = ta.getDimensionPixelSize(2, -1);
                this.mMinFlingVelocity = ta.getInt(DEFAULT_SHADOW_HEIGHT, DEFAULT_MIN_FLING_VELOCITY);
                this.mCoveredFadeColor = ta.getColor(3, DEFAULT_FADE_COLOR);
                this.mDragViewResId = ta.getResourceId(5, -1);
                this.mOverlayContent = ta.getBoolean(6, DEFAULT_OVERLAY_FLAG);
            }
            ta.recycle();
        }
        float density = context.getResources().getDisplayMetrics().density;
        if (this.mPanelHeight == -1) {
            this.mPanelHeight = (int) ((68.0f * density) + 0.5f);
        }
        if (this.mShadowHeight == -1) {
            this.mShadowHeight = (int) ((4.0f * density) + 0.5f);
        }
        if (this.mParalaxOffset == -1) {
            this.mParalaxOffset = (int) (0.0f * density);
        }
        if (this.mShadowHeight <= 0) {
            this.mShadowDrawable = null;
        } else if (this.mIsSlidingUp) {
            this.mShadowDrawable = getResources().getDrawable(R.drawable.above_shadow);
        } else {
            this.mShadowDrawable = getResources().getDrawable(R.drawable.below_shadow);
        }
        setWillNotDraw(DEFAULT_OVERLAY_FLAG);
        this.mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback(this, null));
        this.mDragHelper.setMinVelocity(((float) this.mMinFlingVelocity) * density);
        this.mCanSlide = true;
        this.mIsSlidingEnabled = true;
        this.mScrollTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        if (this.mDragViewResId != -1) {
            this.mDragView = findViewById(this.mDragViewResId);
        }
    }

    public void setCoveredFadeColor(int color) {
        this.mCoveredFadeColor = color;
        invalidate();
    }

    public int getCoveredFadeColor() {
        return this.mCoveredFadeColor;
    }

    public void setSlidingEnabled(boolean enabled) {
        this.mIsSlidingEnabled = enabled;
    }

    public void setPanelHeight(int val) {
        this.mPanelHeight = val;
        requestLayout();
    }

    public int getPanelHeight() {
        return this.mPanelHeight;
    }

    public int getCurrentParalaxOffset() {
        int offset = (int) (((float) this.mParalaxOffset) * (1.0f - this.mSlideOffset));
        return this.mIsSlidingUp ? -offset : offset;
    }

    public void setPanelSlideListener(PanelSlideListener listener) {
        this.mPanelSlideListener = listener;
    }

    public void setDragView(View dragView) {
        this.mDragView = dragView;
    }

    public void setAnchorPoint(float anchorPoint) {
        if (anchorPoint > 0.0f && anchorPoint < 1.0f) {
            this.mAnchorPoint = anchorPoint;
        }
    }

    public void setOverlayed(boolean overlayed) {
        this.mOverlayContent = overlayed;
    }

    public boolean isOverlayed() {
        return this.mOverlayContent;
    }

    void dispatchOnPanelSlide(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelSlide(panel, this.mSlideOffset);
        }
    }

    void dispatchOnPanelExpanded(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelExpanded(panel);
        }
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelCollapsed(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelCollapsed(panel);
        }
        sendAccessibilityEvent(32);
    }

    void dispatchOnPanelAnchored(View panel) {
        if (this.mPanelSlideListener != null) {
            this.mPanelSlideListener.onPanelAnchored(panel);
        }
        sendAccessibilityEvent(32);
    }

    void updateObscuredViewVisibility() {
        if (getChildCount() != 0) {
            int left;
            int vis;
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - getPaddingRight();
            int topBound = getPaddingTop();
            int bottomBound = getHeight() - getPaddingBottom();
            int bottom;
            int top;
            int right;
            if (this.mSlideableView == null || !hasOpaqueBackground(this.mSlideableView)) {
                bottom = DEFAULT_PARALAX_OFFSET;
                top = DEFAULT_PARALAX_OFFSET;
                right = DEFAULT_PARALAX_OFFSET;
                left = DEFAULT_PARALAX_OFFSET;
            } else {
                left = this.mSlideableView.getLeft();
                right = this.mSlideableView.getRight();
                top = this.mSlideableView.getTop();
                bottom = this.mSlideableView.getBottom();
            }
            View child = getChildAt(DEFAULT_PARALAX_OFFSET);
            int clampedChildLeft = Math.max(leftBound, child.getLeft());
            int clampedChildTop = Math.max(topBound, child.getTop());
            int clampedChildRight = Math.min(rightBound, child.getRight());
            int clampedChildBottom = Math.min(bottomBound, child.getBottom());
            if (clampedChildLeft < left || clampedChildTop < top || clampedChildRight > right || clampedChildBottom > bottom) {
                vis = DEFAULT_PARALAX_OFFSET;
            } else {
                vis = DEFAULT_SHADOW_HEIGHT;
            }
            child.setVisibility(vis);
        }
    }

    void setAllChildrenVisible() {
        int childCount = getChildCount();
        for (int i = DEFAULT_PARALAX_OFFSET; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == DEFAULT_SHADOW_HEIGHT) {
                child.setVisibility(DEFAULT_PARALAX_OFFSET);
            }
        }
    }

    private static boolean hasOpaqueBackground(View v) {
        Drawable bg = v.getBackground();
        return (bg == null || bg.getOpacity() != -1) ? DEFAULT_OVERLAY_FLAG : true;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != 1073741824) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
        } else if (heightMode != 1073741824) {
            throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
        } else {
            int layoutHeight = (heightSize - getPaddingTop()) - getPaddingBottom();
            int panelHeight = this.mPanelHeight;
            int childCount = getChildCount();
            if (childCount > 2) {
                Log.e(TAG, "onMeasure: More than two child views are not supported.");
            } else if (getChildAt(1).getVisibility() == 8) {
                panelHeight = DEFAULT_PARALAX_OFFSET;
            }
            this.mSlideableView = null;
            this.mCanSlide = DEFAULT_OVERLAY_FLAG;
            for (int i = DEFAULT_PARALAX_OFFSET; i < childCount; i++) {
                View child = getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int height = layoutHeight;
                if (child.getVisibility() == 8) {
                    lp.dimWhenOffset = DEFAULT_OVERLAY_FLAG;
                } else {
                    int childWidthSpec;
                    int childHeightSpec;
                    if (i == 1) {
                        lp.slideable = true;
                        lp.dimWhenOffset = true;
                        this.mSlideableView = child;
                        this.mCanSlide = true;
                    } else {
                        if (!this.mOverlayContent) {
                            height -= panelHeight;
                        }
                        this.mMainView = child;
                    }
                    if (lp.width == -2) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, ExploreByTouchHelper.INVALID_ID);
                    } else if (lp.width == -1) {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, 1073741824);
                    } else {
                        childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width, 1073741824);
                    }
                    if (lp.height == -2) {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(height, ExploreByTouchHelper.INVALID_ID);
                    } else if (lp.height == -1) {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(height, 1073741824);
                    } else {
                        childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height, 1073741824);
                    }
                    child.measure(childWidthSpec, childHeightSpec);
                }
            }
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int slidingTop = getSlidingTop();
        int childCount = getChildCount();
        if (this.mFirstLayout) {
            switch (1.$SwitchMap$com$sothree$slidinguppanel$SlidingUpPanelLayout$SlideState[this.mSlideState.ordinal()]) {
                case Zone.PRIMARY /*1*/:
                    this.mSlideOffset = this.mCanSlide ? 0.0f : 1.0f;
                    break;
                case Zone.SECONDARY /*2*/:
                    this.mSlideOffset = this.mCanSlide ? this.mAnchorPoint : 1.0f;
                    break;
                default:
                    this.mSlideOffset = 1.0f;
                    break;
            }
        }
        for (int i = DEFAULT_PARALAX_OFFSET; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                int childTop;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childHeight = child.getMeasuredHeight();
                if (lp.slideable) {
                    this.mSlideRange = childHeight - this.mPanelHeight;
                }
                if (!this.mIsSlidingUp) {
                    if (lp.slideable) {
                        childTop = slidingTop - ((int) (((float) this.mSlideRange) * this.mSlideOffset));
                    } else {
                        childTop = paddingTop;
                    }
                    if (!(lp.slideable || this.mOverlayContent)) {
                        childTop += this.mPanelHeight;
                    }
                } else if (lp.slideable) {
                    childTop = slidingTop + ((int) (((float) this.mSlideRange) * this.mSlideOffset));
                } else {
                    childTop = paddingTop;
                }
                int childLeft = paddingLeft;
                child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + childHeight);
            }
        }
        if (this.mFirstLayout) {
            updateObscuredViewVisibility();
        }
        this.mFirstLayout = DEFAULT_OVERLAY_FLAG;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h != oldh) {
            this.mFirstLayout = true;
        }
    }

    public void setEnableDragViewTouchEvents(boolean enabled) {
        this.mIsUsingDragViewTouchEvents = enabled;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (!this.mCanSlide || !this.mIsSlidingEnabled || (this.mIsUnableToDrag && action != 0)) {
            this.mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        } else if (action == 3 || action == 1) {
            this.mDragHelper.cancel();
            return DEFAULT_OVERLAY_FLAG;
        } else {
            float x = ev.getX();
            float y = ev.getY();
            boolean interceptTap = DEFAULT_OVERLAY_FLAG;
            switch (action) {
                case DEFAULT_PARALAX_OFFSET /*0*/:
                    this.mIsUnableToDrag = DEFAULT_OVERLAY_FLAG;
                    this.mInitialMotionX = x;
                    this.mInitialMotionY = y;
                    if (isDragViewUnder((int) x, (int) y) && !this.mIsUsingDragViewTouchEvents) {
                        interceptTap = true;
                        break;
                    }
                case Zone.SECONDARY /*2*/:
                    float adx = Math.abs(x - this.mInitialMotionX);
                    float ady = Math.abs(y - this.mInitialMotionY);
                    int dragSlop = this.mDragHelper.getTouchSlop();
                    if (this.mIsUsingDragViewTouchEvents) {
                        if (adx > ((float) this.mScrollTouchSlop) && ady < ((float) this.mScrollTouchSlop)) {
                            return super.onInterceptTouchEvent(ev);
                        }
                        if (ady > ((float) this.mScrollTouchSlop)) {
                            interceptTap = isDragViewUnder((int) x, (int) y);
                        }
                    }
                    if ((ady > ((float) dragSlop) && adx > ady) || !isDragViewUnder((int) x, (int) y)) {
                        this.mDragHelper.cancel();
                        this.mIsUnableToDrag = true;
                        return DEFAULT_OVERLAY_FLAG;
                    }
                    break;
            }
            if (this.mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap) {
                return true;
            }
            return DEFAULT_OVERLAY_FLAG;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mCanSlide || !this.mIsSlidingEnabled) {
            return super.onTouchEvent(ev);
        }
        this.mDragHelper.processTouchEvent(ev);
        float x;
        float y;
        switch (ev.getAction() & Type.ANY) {
            case DEFAULT_PARALAX_OFFSET /*0*/:
                x = ev.getX();
                y = ev.getY();
                this.mInitialMotionX = x;
                this.mInitialMotionY = y;
                return true;
            case Zone.PRIMARY /*1*/:
                x = ev.getX();
                y = ev.getY();
                float dx = x - this.mInitialMotionX;
                float dy = y - this.mInitialMotionY;
                int slop = this.mDragHelper.getTouchSlop();
                View dragView = this.mDragView != null ? this.mDragView : this.mSlideableView;
                if ((dx * dx) + (dy * dy) >= ((float) (slop * slop)) || !isDragViewUnder((int) x, (int) y)) {
                    return true;
                }
                dragView.playSoundEffect(DEFAULT_PARALAX_OFFSET);
                if (isExpanded() || isAnchored()) {
                    collapsePane();
                    return true;
                }
                expandPane(this.mAnchorPoint);
                return true;
            default:
                return true;
        }
    }

    private boolean isDragViewUnder(int x, int y) {
        boolean z = true;
        View dragView = this.mDragView != null ? this.mDragView : this.mSlideableView;
        if (dragView == null) {
            return DEFAULT_OVERLAY_FLAG;
        }
        int[] viewLocation = new int[2];
        dragView.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        getLocationOnScreen(parentLocation);
        int screenX = parentLocation[DEFAULT_PARALAX_OFFSET] + x;
        int screenY = parentLocation[1] + y;
        if (screenX < viewLocation[DEFAULT_PARALAX_OFFSET] || screenX >= viewLocation[DEFAULT_PARALAX_OFFSET] + dragView.getWidth() || screenY < viewLocation[1] || screenY >= viewLocation[1] + dragView.getHeight()) {
            z = DEFAULT_PARALAX_OFFSET;
        }
        return z;
    }

    private boolean expandPane(View pane, int initialVelocity, float mSlideOffset) {
        return (this.mFirstLayout || smoothSlideTo(mSlideOffset, initialVelocity)) ? true : DEFAULT_OVERLAY_FLAG;
    }

    private boolean collapsePane(View pane, int initialVelocity) {
        return (this.mFirstLayout || smoothSlideTo(1.0f, initialVelocity)) ? true : DEFAULT_OVERLAY_FLAG;
    }

    private int getSlidingTop() {
        if (this.mSlideableView != null) {
            return this.mIsSlidingUp ? (getMeasuredHeight() - getPaddingBottom()) - this.mSlideableView.getMeasuredHeight() : getPaddingTop();
        } else {
            return getMeasuredHeight() - getPaddingBottom();
        }
    }

    public boolean collapsePane() {
        return collapsePane(this.mSlideableView, DEFAULT_PARALAX_OFFSET);
    }

    public boolean expandPane() {
        return expandPane(0.0f);
    }

    public boolean expandPane(float mSlideOffset) {
        if (!isPaneVisible()) {
            showPane();
        }
        return expandPane(this.mSlideableView, DEFAULT_PARALAX_OFFSET, mSlideOffset);
    }

    public boolean isExpanded() {
        return this.mSlideState == SlideState.EXPANDED ? true : DEFAULT_OVERLAY_FLAG;
    }

    public boolean isAnchored() {
        return this.mSlideState == SlideState.ANCHORED ? true : DEFAULT_OVERLAY_FLAG;
    }

    public boolean isSlideable() {
        return this.mCanSlide;
    }

    public boolean isPaneVisible() {
        boolean z = true;
        if (getChildCount() < 2) {
            return DEFAULT_OVERLAY_FLAG;
        }
        if (getChildAt(1).getVisibility() != 0) {
            z = DEFAULT_PARALAX_OFFSET;
        }
        return z;
    }

    public void showPane() {
        if (getChildCount() >= 2) {
            getChildAt(1).setVisibility(DEFAULT_PARALAX_OFFSET);
            requestLayout();
        }
    }

    public void hidePane() {
        if (this.mSlideableView != null) {
            this.mSlideableView.setVisibility(8);
            requestLayout();
        }
    }

    private void onPanelDragged(int newTop) {
        int topBound = getSlidingTop();
        this.mSlideOffset = this.mIsSlidingUp ? ((float) (newTop - topBound)) / ((float) this.mSlideRange) : ((float) (topBound - newTop)) / ((float) this.mSlideRange);
        dispatchOnPanelSlide(this.mSlideableView);
        if (this.mParalaxOffset > 0) {
            int mainViewOffset = getCurrentParalaxOffset();
            if (VERSION.SDK_INT >= 11) {
                this.mMainView.setTranslationY((float) mainViewOffset);
            } else {
                AnimatorProxy.wrap(this.mMainView).setTranslationY((float) mainViewOffset);
            }
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int save = canvas.save(2);
        boolean drawScrim = DEFAULT_OVERLAY_FLAG;
        if (!(!this.mCanSlide || lp.slideable || this.mSlideableView == null)) {
            if (!this.mOverlayContent) {
                canvas.getClipBounds(this.mTmpRect);
                if (this.mIsSlidingUp) {
                    this.mTmpRect.bottom = Math.min(this.mTmpRect.bottom, this.mSlideableView.getTop());
                } else {
                    this.mTmpRect.top = Math.max(this.mTmpRect.top, this.mSlideableView.getBottom());
                }
                canvas.clipRect(this.mTmpRect);
            }
            if (this.mSlideOffset < 1.0f) {
                drawScrim = true;
            }
        }
        boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(save);
        if (drawScrim) {
            this.mCoveredFadePaint.setColor((((int) (((float) ((this.mCoveredFadeColor & ViewCompat.MEASURED_STATE_MASK) >>> 24)) * (1.0f - this.mSlideOffset))) << 24) | (this.mCoveredFadeColor & ViewCompat.MEASURED_SIZE_MASK));
            canvas.drawRect(this.mTmpRect, this.mCoveredFadePaint);
        }
        return result;
    }

    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!this.mCanSlide) {
            return DEFAULT_OVERLAY_FLAG;
        }
        int topBound = getSlidingTop();
        if (!this.mDragHelper.smoothSlideViewTo(this.mSlideableView, this.mSlideableView.getLeft(), this.mIsSlidingUp ? (int) (((float) topBound) + (((float) this.mSlideRange) * slideOffset)) : (int) (((float) topBound) - (((float) this.mSlideRange) * slideOffset)))) {
            return DEFAULT_OVERLAY_FLAG;
        }
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    public void computeScroll() {
        if (!this.mDragHelper.continueSettling(true)) {
            return;
        }
        if (this.mCanSlide) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            this.mDragHelper.abort();
        }
    }

    public void draw(Canvas c) {
        super.draw(c);
        if (this.mSlideableView != null) {
            int top;
            int bottom;
            int right = this.mSlideableView.getRight();
            if (this.mIsSlidingUp) {
                top = this.mSlideableView.getTop() - this.mShadowHeight;
                bottom = this.mSlideableView.getTop();
            } else {
                top = this.mSlideableView.getBottom();
                bottom = this.mSlideableView.getBottom() + this.mShadowHeight;
            }
            int left = this.mSlideableView.getLeft();
            if (this.mShadowDrawable != null) {
                this.mShadowDrawable.setBounds(left, top, right, bottom);
                this.mShadowDrawable.draw(c);
            }
        }
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int scrollX = v.getScrollX();
            int scrollY = v.getScrollY();
            for (int i = group.getChildCount() - 1; i >= 0; i--) {
                View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()) {
                    if (canScroll(child, true, dx, (x + scrollX) - child.getLeft(), (y + scrollY) - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        return (checkV && ViewCompat.canScrollHorizontally(v, -dx)) ? true : DEFAULT_OVERLAY_FLAG;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams ? new LayoutParams((MarginLayoutParams) p) : new LayoutParams(p);
    }

    protected boolean checkLayoutParams(LayoutParams p) {
        return ((p instanceof LayoutParams) && super.checkLayoutParams(p)) ? true : DEFAULT_OVERLAY_FLAG;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.mSlideState = this.mSlideState;
        return ss;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mSlideState = ss.mSlideState;
    }
}
