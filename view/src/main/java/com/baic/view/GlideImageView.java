package com.baic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by baic on 16/4/27.
 */
public class GlideImageView extends ImageView {

    private Context mContext;
    private Drawable placeholder;

    private int radiusPx;
    private int radiusBottomLeftPx;
    private int radiusBottomRightPx;
    private int radiusTopLeftPx;
    private int radiusTopRightPx;

    public GlideImageView(Context context) {
        super(context);
        mContext = context;
        readAttrs(null);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        readAttrs(attrs);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        readAttrs(attrs);
    }

    private void readAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.GlideImageView,
                    0, 0);
            placeholder = typedArray.getDrawable(R.styleable.GlideImageView_placeholder);
            radiusPx = typedArray.getDimensionPixelOffset(R.styleable.GlideImageView_radius, 0);
            radiusBottomLeftPx = typedArray.getDimensionPixelOffset(R.styleable.GlideImageView_radiusBottomLeft, 0);
            radiusBottomRightPx = typedArray.getDimensionPixelOffset(R.styleable.GlideImageView_radiusBottomRight, 0);
            radiusTopLeftPx = typedArray.getDimensionPixelOffset(R.styleable.GlideImageView_radiusTopLeft, 0);
            radiusTopRightPx = typedArray.getDimensionPixelOffset(R.styleable.GlideImageView_radiusTopRight, 0);
            typedArray.recycle();
        }
    }

    public void setDefaultImage(@DrawableRes int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            placeholder = getResources().getDrawable(index, null);
        } else {
            placeholder = getResources().getDrawable(index);
        }
    }

    public void setDefaultImage(Drawable drawable) {
        this.placeholder = drawable;
    }

    public void load(String url) {
        Transformation[] transformations = {};
        if (radiusPx > 0 || radiusBottomLeftPx > 0 || radiusBottomRightPx > 0 || radiusTopLeftPx > 0 || radiusTopRightPx > 0) {
            if (radiusPx > 0) {
                load4Radius(url, radiusPx);
            } else {
                ArrayList<Transformation> transformationList = new ArrayList<Transformation>();
                if (radiusBottomLeftPx > 0) {
                    transformationList.add(new RoundedCornersTransformation(mContext, radiusBottomLeftPx, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT));
                }
                if (radiusBottomRightPx > 0) {
                    transformationList.add(new RoundedCornersTransformation(mContext, radiusBottomRightPx, 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT));
                }
                if (radiusTopLeftPx > 0) {
                    transformationList.add(new RoundedCornersTransformation(mContext, radiusTopLeftPx, 0, RoundedCornersTransformation.CornerType.TOP_LEFT));
                }
                if (radiusTopRightPx > 0) {
                    transformationList.add(new RoundedCornersTransformation(mContext, radiusTopRightPx, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT));
                }
                int size = transformationList.size();
                if (size > 0) {
                    transformations = new Transformation[size];
                    for (int i = 0; i < size; i++) {
                        transformations[i] = transformationList.get(i);
                    }
                }
                load(url, transformations);
            }
        } else {
            load(url, transformations);
        }
    }

    public void load(String url, Transformation<Bitmap> transformation) {
        DrawableRequestBuilder builder = getBuilder(url);
        if (transformation != null) {
            builder.bitmapTransform(transformation);
        }
        builder.into(this);
    }

    public void load(String url, Transformation<Bitmap>[] transformations) {
        DrawableRequestBuilder builder = getBuilder(url);
        if (transformations != null && transformations.length > 0) {
            builder = builder.bitmapTransform(transformations);
        }
        builder.into(this);
    }

    private DrawableRequestBuilder getBuilder(String url) {
        return Glide.with(mContext)
                .load(url)
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .centerCrop()
                .crossFade();
    }

    public void load4Circular(String url) {
        load(url, new CropCircleTransformation(mContext));
    }

    public void load4Radius(String url, int radius, RoundedCornersTransformation.CornerType cornerType) {
        load(url, new RoundedCornersTransformation(mContext, radius, 0, cornerType));
    }

    public void load4Radius(String url, int radius) {
        load4Radius(url, radius, RoundedCornersTransformation.CornerType.ALL);
    }
}
