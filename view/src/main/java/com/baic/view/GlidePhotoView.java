package com.baic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.GestureDetector;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by baic on 16/7/15.
 */
public class GlidePhotoView extends PhotoView {

    private Context mContext;

    private PhotoViewAttacher photoViewAttacher;

    public GlidePhotoView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public GlidePhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        readAttrs(attrs);
        initView();
    }

    public GlidePhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        readAttrs(attrs);
        initView();
    }

    private void readAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.BannerView,
                    0, 0);
            if (typedArray != null) {

            }
        }
    }

    private void initView() {
        photoViewAttacher = new PhotoViewAttacher(this);
    }

    public GlidePhotoView setDrawable(@DrawableRes int index) {
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(index, null);
        } else {
            drawable = getResources().getDrawable(index);
        }
        this.setImageDrawable(drawable);
        return this;
    }

    public GlidePhotoView setDrawable(Drawable drawable) {
        this.setImageDrawable(drawable);
        return this;
    }

    public GlidePhotoView loadImageUrl(@StringRes int index) {
        return loadImageUrl(getResources().getString(index));
    }

    public GlidePhotoView loadImageUrl(String url) {
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this);
        return this;
    }

    public GlidePhotoView setOnAttachMatrixChangeListener(PhotoViewAttacher.OnMatrixChangedListener onMatrixChangedListener) {
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnMatrixChangeListener(onMatrixChangedListener);
        }
        return this;
    }

    public GlidePhotoView setOnAttachPhotoTapListener(PhotoViewAttacher.OnPhotoTapListener onPhotoTapListener) {
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnPhotoTapListener(onPhotoTapListener);
        }
        return this;
    }

    public GlidePhotoView setOnAttachDoubleTapListener(GestureDetector.OnDoubleTapListener OnDoubleTapListener) {
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnDoubleTapListener(OnDoubleTapListener);
        }
        return this;
    }

    public GlidePhotoView setOnAttachLongClickListener(OnLongClickListener onLongClickListener) {
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnLongClickListener(onLongClickListener);
        }
        return this;
    }

    public GlidePhotoView setOnAttachViewTapListener(PhotoViewAttacher.OnViewTapListener onViewTapListener) {
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnViewTapListener(onViewTapListener);
        }
        return this;
    }

    public GlidePhotoView setOnAttachScaleChangeListener(PhotoViewAttacher.OnScaleChangeListener onScaleChangeListener) {
        if (photoViewAttacher != null) {
            photoViewAttacher.setOnScaleChangeListener(onScaleChangeListener);
        }
        return this;
    }
}
