package com.baic.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.io.Serializable;

/**
 * Created by baic on 16/5/18.
 */
public class LoadingView extends View {

    private Context mContext;

    public LoadingView(Context context) {
        super(context);
        mContext = context;
        readAttrs(null);
        render();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        readAttrs(attrs);
        render();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        readAttrs(attrs);
        render();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        readAttrs(attrs);
        render();
    }

    private enum Render implements Serializable {
        Level(0),
        Gear(1),
        Whorl(2),
        Material(3);
        int id;

        Render(int id) {
            this.id = id;
        }

        static Render get(int id) {
            for (Render render : values()) {
                if (render.id == id) return render;
            }
            throw new IllegalArgumentException();
        }
    }

    private LoadingRenderer loadingRenderer;

    private void readAttrs(AttributeSet attrs) {
        loadingRenderer = new LevelLoadingRenderer(mContext);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.LoadingView,
                    0, 0);
            if (typedArray != null) {
                if (typedArray.hasValue(R.styleable.LoadingView_render)) {
                    switch (Render.get(typedArray.getInt(R.styleable.LoadingView_render, 0))) {
                        case Material:
                            loadingRenderer = new MaterialLoadingRenderer(mContext);
                            break;
                        case Whorl:
                            loadingRenderer = new WhorlLoadingRenderer(mContext);
                            break;
                        case Gear:
                            loadingRenderer = new GearLoadingRenderer(mContext);
                            break;
                        case Level:
                        default:
                            loadingRenderer = new LevelLoadingRenderer(mContext);
                            break;
                    }
                }
                typedArray.recycle();
            }
        }
    }

    private void render() {
        LoadingDrawable loadingDrawable = new LoadingDrawable(loadingRenderer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(loadingDrawable);
        } else {
            this.setBackgroundDrawable(loadingDrawable);
        }
        loadingDrawable.start();
    }

    class LoadingDrawable extends Drawable implements Animatable {

        private LoadingRenderer mLoadingRender;

        private final Callback mCallback = new Callback() {
            @Override
            public void invalidateDrawable(Drawable d) {
                invalidateSelf();
            }

            @Override
            public void scheduleDrawable(Drawable d, Runnable what, long when) {
                scheduleSelf(what, when);
            }

            @Override
            public void unscheduleDrawable(Drawable d, Runnable what) {
                unscheduleSelf(what);
            }
        };

        public LoadingDrawable(LoadingRenderer loadingRender) {
            this.mLoadingRender = loadingRender;
            this.mLoadingRender.setCallback(mCallback);
        }

        @Override
        public void draw(Canvas canvas) {
            mLoadingRender.draw(canvas, getBounds());
        }

        @Override
        public void setAlpha(int alpha) {
            mLoadingRender.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mLoadingRender.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void start() {
            mLoadingRender.start();
        }

        @Override
        public void stop() {
            mLoadingRender.stop();
        }

        @Override
        public boolean isRunning() {
            return mLoadingRender.isRunning();
        }

        @Override
        public int getIntrinsicHeight() {
            return (int) (mLoadingRender.getHeight() + 1);
        }

        @Override
        public int getIntrinsicWidth() {
            return (int) (mLoadingRender.getWidth() + 1);
        }
    }

    public abstract static class LoadingRenderer {
        private static final long ANIMATION_DURATION = 1333;

        private static final float DEFAULT_SIZE = 56.0f;
        private static final float DEFAULT_CENTER_RADIUS = 12.5f;
        private static final float DEFAULT_STROKE_WIDTH = 2.5f;

        protected float mWidth;
        protected float mHeight;
        protected float mStrokeWidth;
        protected float mCenterRadius;

        private long mDuration;
        private Drawable.Callback mCallback;
        private ValueAnimator mRenderAnimator;

        public LoadingRenderer(Context context) {
            setupDefaultParams(context);
            setupAnimators();
        }

        public abstract void draw(Canvas canvas, Rect bounds);

        public abstract void computeRender(float renderProgress);

        public abstract void setAlpha(int alpha);

        public abstract void setColorFilter(ColorFilter cf);

        public abstract void reset();

        public void start() {
            reset();
            setDuration(mDuration);
            mRenderAnimator.start();
        }

        public void stop() {
            mRenderAnimator.cancel();
        }

        public boolean isRunning() {
            return mRenderAnimator.isRunning();
        }

        public void setCallback(Drawable.Callback callback) {
            this.mCallback = callback;
        }

        protected void invalidateSelf() {
            mCallback.invalidateDrawable(null);
        }

        private void setupDefaultParams(Context context) {
            final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            final float screenDensity = metrics.density;

            mWidth = DEFAULT_SIZE * screenDensity;
            mHeight = DEFAULT_SIZE * screenDensity;
            mStrokeWidth = DEFAULT_STROKE_WIDTH * screenDensity;
            mCenterRadius = DEFAULT_CENTER_RADIUS * screenDensity;

            mDuration = ANIMATION_DURATION;
        }

        private void setupAnimators() {
            mRenderAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            mRenderAnimator.setRepeatCount(Animation.INFINITE);
            mRenderAnimator.setRepeatMode(Animation.RESTART);
            //fuck you! the default interpolator is AccelerateDecelerateInterpolator
            mRenderAnimator.setInterpolator(new LinearInterpolator());
            mRenderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    computeRender((float) animation.getAnimatedValue());
                    invalidateSelf();
                }
            });
        }

        protected void addRenderListener(Animator.AnimatorListener animatorListener) {
            mRenderAnimator.addListener(animatorListener);
        }

        public void setCenterRadius(float centerRadius) {
            mCenterRadius = centerRadius;
        }

        public float getCenterRadius() {
            return mCenterRadius;
        }

        public void setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
        }

        public float getStrokeWidth() {
            return mStrokeWidth;
        }

        public float getWidth() {
            return mWidth;
        }

        public void setWidth(float width) {
            this.mWidth = width;
        }

        public float getHeight() {
            return mHeight;
        }

        public void setHeight(float height) {
            this.mHeight = height;
        }

        public long getDuration() {
            return mDuration;
        }

        public void setDuration(long duration) {
            this.mDuration = duration;
            mRenderAnimator.setDuration(mDuration);
        }
    }

    public static class MaterialLoadingRenderer extends LoadingRenderer {

        private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();

        private static final int DEGREE_360 = 360;
        private static final int NUM_POINTS = 5;

        private static final float MIN_SWIPE_DEGREE = 0.1f;
        private static final float MAX_SWIPE_DEGREES = 0.8f * DEGREE_360;
        private static final float FULL_GROUP_ROTATION = 3.0f * DEGREE_360;
        private static final float MAX_ROTATION_INCREMENT = 0.25f * DEGREE_360;

        private static final float COLOR_START_DELAY_OFFSET = 0.8f;
        private static final float END_TRIM_DURATION_OFFSET = 1.0f;
        private static final float START_TRIM_DURATION_OFFSET = 0.5f;

        private static final int[] DEFAULT_COLORS = new int[]{
                Color.RED, Color.GREEN, Color.BLUE
        };

        private final Paint mPaint = new Paint();
        private final RectF mTempBounds = new RectF();

        private final Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animator) {
                super.onAnimationRepeat(animator);
                storeOriginals();
                goToNextColor();

                mStartDegrees = mEndDegrees;
                mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRotationCount = 0;
            }
        };

        private int[] mColors;
        private int mColorIndex;
        private int mCurrentColor;

        private float mStrokeInset;

        private float mRotationCount;
        private float mGroupRotation;

        private float mEndDegrees;
        private float mStartDegrees;
        private float mSwipeDegrees;
        private float mRotationIncrement;
        private float mOriginEndDegrees;
        private float mOriginStartDegrees;
        private float mOriginRotationIncrement;

        public MaterialLoadingRenderer(Context context) {
            super(context);
            init();
            setupPaint();
            addRenderListener(mAnimatorListener);
        }

        private void init() {
            mColors = DEFAULT_COLORS;

            setColorIndex(0);
            setInsets((int) getWidth(), (int) getHeight());
        }

        private void setupPaint() {
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(getStrokeWidth());
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        @Override
        public void draw(Canvas canvas, Rect bounds) {
            int saveCount = canvas.save();

            canvas.rotate(mGroupRotation, bounds.exactCenterX(), bounds.exactCenterY());

            RectF arcBounds = mTempBounds;
            arcBounds.set(bounds);
            arcBounds.inset(mStrokeInset, mStrokeInset);

            mPaint.setColor(mCurrentColor);
            canvas.drawArc(arcBounds, mStartDegrees, mSwipeDegrees, false, mPaint);

            canvas.restoreToCount(saveCount);
        }

        @Override
        public void computeRender(float renderProgress) {
            updateRingColor(renderProgress);

            // Moving the start trim only occurs in the first 50% of a
            // single ring animation
            if (renderProgress <= START_TRIM_DURATION_OFFSET) {
                float startTrimProgress = renderProgress / START_TRIM_DURATION_OFFSET;
                mStartDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);
            }

            // Moving the end trim starts after 50% of a single ring
            // animation completes
            if (renderProgress > START_TRIM_DURATION_OFFSET) {
                float endTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
                mEndDegrees = mOriginEndDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);
            }

            if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                mSwipeDegrees = mEndDegrees - mStartDegrees;
            }

            mGroupRotation = ((FULL_GROUP_ROTATION / NUM_POINTS) * renderProgress) + (FULL_GROUP_ROTATION * (mRotationCount / NUM_POINTS));
            mRotationIncrement = mOriginRotationIncrement + (MAX_ROTATION_INCREMENT * renderProgress);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
            invalidateSelf();
        }

        @Override
        public void reset() {
            resetOriginals();
        }

        public void setColors(@NonNull int[] colors) {
            mColors = colors;
            setColorIndex(0);
        }

        public void setColorIndex(int index) {
            mColorIndex = index;
            mCurrentColor = mColors[mColorIndex];
        }

        private int getNextColor() {
            return mColors[getNextColorIndex()];
        }

        private int getNextColorIndex() {
            return (mColorIndex + 1) % (mColors.length);
        }

        private void goToNextColor() {
            setColorIndex(getNextColorIndex());
        }

        @Override
        public void setStrokeWidth(float strokeWidth) {
            super.setStrokeWidth(strokeWidth);
            mPaint.setStrokeWidth(strokeWidth);
            invalidateSelf();
        }

        private void setInsets(int width, int height) {
            final float minEdge = (float) Math.min(width, height);
            float insets;
            if (getCenterRadius() <= 0 || minEdge < 0) {
                insets = (float) Math.ceil(getStrokeWidth() / 2.0f);
            } else {
                insets = minEdge / 2.0f - getCenterRadius();
            }
            mStrokeInset = insets;
        }

        private void storeOriginals() {
            mOriginEndDegrees = mEndDegrees;
            mOriginStartDegrees = mStartDegrees;
            mOriginRotationIncrement = mRotationIncrement;
        }

        private void resetOriginals() {
            mOriginEndDegrees = 0;
            mOriginStartDegrees = 0;
            mOriginRotationIncrement = 0;

            mEndDegrees = 0;
            mStartDegrees = 0;
            mRotationIncrement = 0;

            mSwipeDegrees = MIN_SWIPE_DEGREE;
        }

        private int getStartingColor() {
            return mColors[mColorIndex];
        }

        private void updateRingColor(float interpolatedTime) {
            if (interpolatedTime > COLOR_START_DELAY_OFFSET) {
                mCurrentColor = evaluateColorChange((interpolatedTime - COLOR_START_DELAY_OFFSET)
                        / (1.0f - COLOR_START_DELAY_OFFSET), getStartingColor(), getNextColor());
            }
        }

        private int evaluateColorChange(float fraction, int startValue, int endValue) {
            int startA = (startValue >> 24) & 0xff;
            int startR = (startValue >> 16) & 0xff;
            int startG = (startValue >> 8) & 0xff;
            int startB = startValue & 0xff;

            int endA = (endValue >> 24) & 0xff;
            int endR = (endValue >> 16) & 0xff;
            int endG = (endValue >> 8) & 0xff;
            int endB = endValue & 0xff;

            return ((startA + (int) (fraction * (endA - startA))) << 24)
                    | ((startR + (int) (fraction * (endR - startR))) << 16)
                    | ((startG + (int) (fraction * (endG - startG))) << 8)
                    | ((startB + (int) (fraction * (endB - startB))));
        }
    }

    public static class WhorlLoadingRenderer extends LoadingRenderer {
        private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();

        private static final int DEGREE_180 = 180;
        private static final int DEGREE_360 = 360;
        private static final int NUM_POINTS = 5;

        private static final float MIN_SWIPE_DEGREE = 0.1f;
        private static final float MAX_SWIPE_DEGREES = 0.6f * DEGREE_360;
        private static final float FULL_GROUP_ROTATION = 3.0f * DEGREE_360;
        private static final float MAX_ROTATION_INCREMENT = 0.25f * DEGREE_360;

        private static final float START_TRIM_DURATION_OFFSET = 0.5f;
        private static final float END_TRIM_DURATION_OFFSET = 1.0f;

        private static final int[] DEFAULT_COLORS = new int[]{
                Color.RED, Color.GREEN, Color.BLUE
        };

        private final Paint mPaint = new Paint();
        private final RectF mTempBounds = new RectF();

        private final Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animator) {
                super.onAnimationRepeat(animator);
                storeOriginals();

                mStartDegrees = mEndDegrees;
                mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRotationCount = 0;
            }
        };

        private int[] mColors;

        private float mStrokeInset;

        private float mRotationCount;
        private float mGroupRotation;

        private float mEndDegrees;
        private float mStartDegrees;
        private float mSwipeDegrees;
        private float mRotationIncrement;
        private float mOriginEndDegrees;
        private float mOriginStartDegrees;
        private float mOriginRotationIncrement;

        public WhorlLoadingRenderer(Context context) {
            super(context);
            init();
            setupPaint();
            addRenderListener(mAnimatorListener);
        }

        private void init() {
            mColors = DEFAULT_COLORS;
            setInsets((int) getWidth(), (int) getHeight());
        }

        private void setupPaint() {
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(getStrokeWidth());
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        @Override
        public void draw(Canvas canvas, Rect bounds) {
            int saveCount = canvas.save();

            canvas.rotate(mGroupRotation, bounds.exactCenterX(), bounds.exactCenterY());
            RectF arcBounds = mTempBounds;
            arcBounds.set(bounds);
            arcBounds.inset(mStrokeInset, mStrokeInset);

            for (int i = 0; i < mColors.length; i++) {
                mPaint.setStrokeWidth(getStrokeWidth() / (i + 1));
                mPaint.setColor(mColors[i]);
                canvas.drawArc(createArcBounds(arcBounds, i), mStartDegrees + DEGREE_180 * (i % 2),
                        mSwipeDegrees, false, mPaint);
            }

            canvas.restoreToCount(saveCount);
        }

        private RectF createArcBounds(RectF sourceArcBounds, int index) {
            RectF arcBounds = new RectF();
            int intervalWidth = 0;

            for (int i = 0; i < index; i++) {
                intervalWidth += getStrokeWidth() / (i + 1.0f) * 1.5f;
            }

            int arcBoundsLeft = (int) (sourceArcBounds.left + intervalWidth);
            int arcBoundsTop = (int) (sourceArcBounds.top + intervalWidth);
            int arcBoundsRight = (int) (sourceArcBounds.right - intervalWidth);
            int arcBoundsBottom = (int) (sourceArcBounds.bottom - intervalWidth);
            arcBounds.set(arcBoundsLeft, arcBoundsTop, arcBoundsRight, arcBoundsBottom);

            return arcBounds;
        }

        @Override
        public void computeRender(float renderProgress) {
            // Moving the start trim only occurs in the first 50% of a
            // single ring animation
            if (renderProgress <= START_TRIM_DURATION_OFFSET) {
                float startTrimProgress = (renderProgress) / (1.0f - START_TRIM_DURATION_OFFSET);
                mStartDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);
            }

            // Moving the end trim starts after 50% of a single ring
            // animation completes
            if (renderProgress > START_TRIM_DURATION_OFFSET) {
                float endTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
                mEndDegrees = mOriginEndDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);
            }

            if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                mSwipeDegrees = mEndDegrees - mStartDegrees;
            }

            mGroupRotation = ((FULL_GROUP_ROTATION / NUM_POINTS) * renderProgress) + (FULL_GROUP_ROTATION * (mRotationCount / NUM_POINTS));
            mRotationIncrement = mOriginRotationIncrement + (MAX_ROTATION_INCREMENT * renderProgress);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
            invalidateSelf();
        }

        @Override
        public void reset() {
            resetOriginals();
        }

        public void setColors(@NonNull int[] colors) {
            mColors = colors;
        }

        @Override
        public void setStrokeWidth(float strokeWidth) {
            super.setStrokeWidth(strokeWidth);
            invalidateSelf();
        }

        public void setInsets(int width, int height) {
            final float minEdge = (float) Math.min(width, height);
            float insets;
            if (getCenterRadius() <= 0 || minEdge < 0) {
                insets = (float) Math.ceil(getStrokeWidth() / 2.0f);
            } else {
                insets = minEdge / 2.0f - getCenterRadius();
            }
            mStrokeInset = insets;
        }

        private void storeOriginals() {
            mOriginEndDegrees = mEndDegrees;
            mOriginStartDegrees = mStartDegrees;
            mOriginRotationIncrement = mRotationIncrement;
        }

        private void resetOriginals() {
            mOriginEndDegrees = 0;
            mOriginStartDegrees = 0;
            mOriginRotationIncrement = 0;

            mEndDegrees = 0;
            mStartDegrees = 0;
            mRotationIncrement = 0;

            mSwipeDegrees = MIN_SWIPE_DEGREE;
        }
    }

    public static class LevelLoadingRenderer extends LoadingRenderer {
        private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
        private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
        private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
        private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

        private static final int NUM_POINTS = 5;
        private static final int DEGREE_360 = 360;

        private static final float MIN_SWIPE_DEGREE = 0.1f;
        private static final float MAX_SWIPE_DEGREES = 0.8f * DEGREE_360;
        private static final float FULL_GROUP_ROTATION = 3.0f * DEGREE_360;
        private static final float MAX_ROTATION_INCREMENT = 0.25f * DEGREE_360;

        private static final float LEVEL2_SWEEP_ANGLE_OFFSET = 7.0f / 8.0f;
        private static final float LEVEL3_SWEEP_ANGLE_OFFSET = 5.0f / 8.0f;

        private static final float START_TRIM_DURATION_OFFSET = 0.5f;
        private static final float END_TRIM_DURATION_OFFSET = 1.0f;

        private static final int DEFAULT_COLOR = Color.WHITE;

        private final Paint mPaint = new Paint();
        private final RectF mTempBounds = new RectF();

        private final Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animator) {
                super.onAnimationRepeat(animator);
                storeOriginals();

                mStartDegrees = mEndDegrees;
                mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRotationCount = 0;
            }
        };

        private int mLevel1Color;
        private int mLevel2Color;
        private int mLevel3Color;

        private float mStrokeInset;

        private float mRotationCount;
        private float mGroupRotation;

        private float mEndDegrees;
        private float mStartDegrees;
        private float mLevel1SwipeDegrees;
        private float mLevel2SwipeDegrees;
        private float mLevel3SwipeDegrees;
        private float mRotationIncrement;
        private float mOriginEndDegrees;
        private float mOriginStartDegrees;
        private float mOriginRotationIncrement;

        public LevelLoadingRenderer(Context context) {
            super(context);
            setupPaint();
            addRenderListener(mAnimatorListener);
        }

        private void setupPaint() {
            mLevel1Color = oneThirdAlphaColor(DEFAULT_COLOR);
            mLevel2Color = twoThirdAlphaColor(DEFAULT_COLOR);
            mLevel3Color = DEFAULT_COLOR;

            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(getStrokeWidth());
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);

            setInsets((int) getWidth(), (int) getHeight());
        }

        @Override
        public void draw(Canvas canvas, Rect bounds) {
            int saveCount = canvas.save();

            canvas.rotate(mGroupRotation, bounds.exactCenterX(), bounds.exactCenterY());
            RectF arcBounds = mTempBounds;
            arcBounds.set(bounds);
            arcBounds.inset(mStrokeInset, mStrokeInset);

            mPaint.setColor(mLevel1Color);
            canvas.drawArc(arcBounds, mEndDegrees, mLevel1SwipeDegrees, false, mPaint);
            mPaint.setColor(mLevel2Color);
            canvas.drawArc(arcBounds, mEndDegrees, mLevel2SwipeDegrees, false, mPaint);
            mPaint.setColor(mLevel3Color);
            canvas.drawArc(arcBounds, mEndDegrees, mLevel3SwipeDegrees, false, mPaint);

            canvas.restoreToCount(saveCount);
        }

        @Override
        public void computeRender(float renderProgress) {
            // Moving the start trim only occurs in the first 50% of a
            // single ring animation
            if (renderProgress <= START_TRIM_DURATION_OFFSET) {
                float startTrimProgress = (renderProgress) / START_TRIM_DURATION_OFFSET;
                mStartDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);

                float mSwipeDegrees = MIN_SWIPE_DEGREE;
                if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                    mSwipeDegrees = mEndDegrees - mStartDegrees;
                }
                float levelSwipeDegreesProgress = Math.abs(mSwipeDegrees) / MAX_SWIPE_DEGREES;

                float level1Increment = DECELERATE_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress) - LINEAR_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress);
                float level3Increment = ACCELERATE_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress) - LINEAR_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress);

                mLevel1SwipeDegrees = -mSwipeDegrees * (1 + level1Increment);
                mLevel2SwipeDegrees = -mSwipeDegrees * LEVEL2_SWEEP_ANGLE_OFFSET;
                mLevel3SwipeDegrees = -mSwipeDegrees * LEVEL3_SWEEP_ANGLE_OFFSET * (1 + level3Increment);
            }

            // Moving the end trim starts after 50% of a single ring
            // animation completes
            if (renderProgress > START_TRIM_DURATION_OFFSET) {
                float endTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
                mEndDegrees = mOriginEndDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);

                float mSwipeDegrees = MIN_SWIPE_DEGREE;
                if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                    mSwipeDegrees = mEndDegrees - mStartDegrees;
                }
                float levelSwipeDegreesProgress = Math.abs(mSwipeDegrees) / MAX_SWIPE_DEGREES;

                if (levelSwipeDegreesProgress > LEVEL2_SWEEP_ANGLE_OFFSET) {
                    mLevel1SwipeDegrees = -mSwipeDegrees;
                    mLevel2SwipeDegrees = MAX_SWIPE_DEGREES * LEVEL2_SWEEP_ANGLE_OFFSET;
                    mLevel3SwipeDegrees = MAX_SWIPE_DEGREES * LEVEL3_SWEEP_ANGLE_OFFSET;
                } else if (levelSwipeDegreesProgress > LEVEL3_SWEEP_ANGLE_OFFSET) {
                    mLevel1SwipeDegrees = MIN_SWIPE_DEGREE;
                    mLevel2SwipeDegrees = -mSwipeDegrees;
                    mLevel3SwipeDegrees = MAX_SWIPE_DEGREES * LEVEL3_SWEEP_ANGLE_OFFSET;
                } else {
                    mLevel1SwipeDegrees = MIN_SWIPE_DEGREE;
                    mLevel2SwipeDegrees = MIN_SWIPE_DEGREE;
                    mLevel3SwipeDegrees = -mSwipeDegrees;
                }
            }

            mGroupRotation = ((FULL_GROUP_ROTATION / NUM_POINTS) * renderProgress) + (FULL_GROUP_ROTATION * (mRotationCount / NUM_POINTS));
            mRotationIncrement = mOriginRotationIncrement + (MAX_ROTATION_INCREMENT * renderProgress);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
            invalidateSelf();
        }

        @Override
        public void reset() {
            resetOriginals();
        }

        public void setColor(int color) {
            mLevel1Color = oneThirdAlphaColor(color);
            mLevel2Color = twoThirdAlphaColor(color);
            mLevel3Color = color;
        }

        @Override
        public void setStrokeWidth(float strokeWidth) {
            super.setStrokeWidth(strokeWidth);
            mPaint.setStrokeWidth(strokeWidth);
            invalidateSelf();
        }

        public void setInsets(int width, int height) {
            final float minEdge = (float) Math.min(width, height);
            float insets;
            if (getCenterRadius() <= 0 || minEdge < 0) {
                insets = (float) Math.ceil(getStrokeWidth() / 2.0f);
            } else {
                insets = minEdge / 2.0f - getCenterRadius();
            }
            mStrokeInset = insets;
        }

        private void storeOriginals() {
            mOriginEndDegrees = mEndDegrees;
            mOriginStartDegrees = mStartDegrees;
            mOriginRotationIncrement = mRotationIncrement;
        }

        private void resetOriginals() {
            mOriginEndDegrees = 0;
            mOriginStartDegrees = 0;
            mOriginRotationIncrement = 0;

            mEndDegrees = 0;
            mStartDegrees = 0;
            mRotationIncrement = 0;

            mLevel1SwipeDegrees = MIN_SWIPE_DEGREE;
            mLevel2SwipeDegrees = MIN_SWIPE_DEGREE;
            mLevel3SwipeDegrees = MIN_SWIPE_DEGREE;
        }

        private int oneThirdAlphaColor(int colorValue) {
            int startA = (colorValue >> 24) & 0xff;
            int startR = (colorValue >> 16) & 0xff;
            int startG = (colorValue >> 8) & 0xff;
            int startB = colorValue & 0xff;

            return (startA / 3 << 24)
                    | (startR << 16)
                    | (startG << 8)
                    | startB;
        }

        private int twoThirdAlphaColor(int colorValue) {
            int startA = (colorValue >> 24) & 0xff;
            int startR = (colorValue >> 16) & 0xff;
            int startG = (colorValue >> 8) & 0xff;
            int startB = colorValue & 0xff;

            return (startA * 2 / 3 << 24)
                    | (startR << 16)
                    | (startG << 8)
                    | startB;
        }
    }

    public static class GearLoadingRenderer extends LoadingRenderer {
        private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
        private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
        private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

        private static final int GEAR_COUNT = 4;
        private static final int NUM_POINTS = 3;
        private static final int MAX_ALPHA = 255;
        private static final int DEGREE_360 = 360;

        private static final float MIN_SWIPE_DEGREE = 0.1f;
        private static final float MAX_SWIPE_DEGREES = 0.17f * DEGREE_360;
        private static final float FULL_GROUP_ROTATION = 3.0f * DEGREE_360;
        private static final float MAX_ROTATION_INCREMENT = 0.25f * DEGREE_360;

        private static final float START_SCALE_DURATION_OFFSET = 0.3f;
        private static final float START_TRIM_DURATION_OFFSET = 0.5f;
        private static final float END_TRIM_DURATION_OFFSET = 0.7f;
        private static final float END_SCALE_DURATION_OFFSET = 1.0f;

        private static final int DEFAULT_COLOR = Color.WHITE;

        private final Paint mPaint = new Paint();
        private final RectF mTempBounds = new RectF();

        private boolean mIsScaling;

        private final Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animator) {
                super.onAnimationRepeat(animator);
                storeOriginals();

                mStartDegrees = mEndDegrees;
                mRotationCount = (mRotationCount + 1) % NUM_POINTS;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRotationCount = 0;
            }
        };

        private int mCurrentColor;

        private float mStrokeInset;

        private float mRotationCount;
        private float mGroupRotation;

        private float mScale;
        private float mEndDegrees;
        private float mStartDegrees;
        private float mSwipeDegrees;
        private float mRotationIncrement;
        private float mOriginEndDegrees;
        private float mOriginStartDegrees;
        private float mOriginRotationIncrement;

        public GearLoadingRenderer(Context context) {
            super(context);
            mCurrentColor = DEFAULT_COLOR;

            setupPaint();
            addRenderListener(mAnimatorListener);
        }

        private void setupPaint() {
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(getStrokeWidth());
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);

            setInsets((int) getWidth(), (int) getHeight());
        }

        @Override
        public void draw(Canvas canvas, Rect bounds) {
            int saveCount = canvas.save();

            canvas.rotate(mGroupRotation, bounds.exactCenterX(), bounds.exactCenterY());
            RectF arcBounds = mTempBounds;
            arcBounds.set(bounds);
            arcBounds.inset(mStrokeInset, mStrokeInset);

            arcBounds.inset(arcBounds.width() * (1.0f - mScale) / 2.0f, arcBounds.width() * (1.0f - mScale) / 2.0f);

            mPaint.setColor(mCurrentColor);
            mPaint.setAlpha((int) (MAX_ALPHA * mScale));
            mPaint.setStrokeWidth(getStrokeWidth() * mScale);
            for (int i = 0; i < GEAR_COUNT; i++) {
                canvas.drawArc(arcBounds, mStartDegrees + DEGREE_360 / GEAR_COUNT * i, mSwipeDegrees, false, mPaint);
            }

            canvas.restoreToCount(saveCount);
        }

        @Override
        public void computeRender(float renderProgress) {
            // Scaling up the start size only occurs in the first 20% of a
            // single ring animation
            if (renderProgress <= START_SCALE_DURATION_OFFSET) {
                float startScaleProgress = (renderProgress) / START_SCALE_DURATION_OFFSET;
                mScale = DECELERATE_INTERPOLATOR.getInterpolation(startScaleProgress);
            }

            // Moving the start trim only occurs between 20% to 50% of a
            // single ring animation
            if (renderProgress <= START_TRIM_DURATION_OFFSET && renderProgress > START_SCALE_DURATION_OFFSET) {
                float startTrimProgress = (renderProgress - START_SCALE_DURATION_OFFSET) / (START_TRIM_DURATION_OFFSET - START_SCALE_DURATION_OFFSET);
                mStartDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * LINEAR_INTERPOLATOR.getInterpolation(startTrimProgress);
            }

            // Moving the end trim starts between 50% to 80% of a single ring
            // animation completes
            if (renderProgress <= END_TRIM_DURATION_OFFSET && renderProgress > START_TRIM_DURATION_OFFSET) {
                float endTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
                mEndDegrees = mOriginEndDegrees + MAX_SWIPE_DEGREES * LINEAR_INTERPOLATOR.getInterpolation(endTrimProgress);
            }

            // Scaling down the end size starts after 80% of a single ring
            // animation completes
            if (renderProgress > END_TRIM_DURATION_OFFSET) {
                float endScaleProgress = (renderProgress - END_TRIM_DURATION_OFFSET) / (END_SCALE_DURATION_OFFSET - END_TRIM_DURATION_OFFSET);
                mScale = 1.0f - ACCELERATE_INTERPOLATOR.getInterpolation(endScaleProgress);
            }

            if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                mSwipeDegrees = mEndDegrees - mStartDegrees;
            }

            if (renderProgress <= END_TRIM_DURATION_OFFSET && renderProgress > START_SCALE_DURATION_OFFSET) {
                float rotateProgress = (renderProgress - START_SCALE_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_SCALE_DURATION_OFFSET);
                mGroupRotation = ((FULL_GROUP_ROTATION / NUM_POINTS) * rotateProgress) + (FULL_GROUP_ROTATION * (mRotationCount / NUM_POINTS));
                mRotationIncrement = mOriginRotationIncrement + (MAX_ROTATION_INCREMENT * rotateProgress);
            }
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            mPaint.setColorFilter(cf);
            invalidateSelf();
        }

        @Override
        public void reset() {
            resetOriginals();
        }

        public void setColor(int color) {
            mCurrentColor = color;
        }

        @Override
        public void setStrokeWidth(float strokeWidth) {
            super.setStrokeWidth(strokeWidth);
            mPaint.setStrokeWidth(strokeWidth);
            invalidateSelf();
        }

        private void setInsets(int width, int height) {
            final float minEdge = (float) Math.min(width, height);
            float insets;
            if (getCenterRadius() <= 0 || minEdge < 0) {
                insets = (float) Math.ceil(getStrokeWidth() / 2.0f);
            } else {
                insets = minEdge / 2.0f - getCenterRadius();
            }
            mStrokeInset = insets;
        }

        private void storeOriginals() {
            mOriginEndDegrees = mEndDegrees;
            mOriginStartDegrees = mStartDegrees;
            mOriginRotationIncrement = mRotationIncrement;
        }

        private void resetOriginals() {
            mOriginEndDegrees = 0;
            mOriginStartDegrees = 0;
            mOriginRotationIncrement = 0;

            mEndDegrees = 0;
            mStartDegrees = 0;
            mRotationIncrement = 0;

            mSwipeDegrees = MIN_SWIPE_DEGREE;
        }
    }
}
