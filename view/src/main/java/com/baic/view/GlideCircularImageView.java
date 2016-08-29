package com.baic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by baic on 16/4/22.
 */
public class GlideCircularImageView extends CircleImageView {

    private Context mContext;
    private Drawable placeholder;

    public GlideCircularImageView(Context context) {
        super(context);
        mContext = context;
        readAttrs(null);
    }

    public GlideCircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        readAttrs(attrs);
    }

    public GlideCircularImageView(Context context, AttributeSet attrs, int defStyle) {
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
            if (typedArray != null) {
                placeholder = typedArray.getDrawable(R.styleable.GlideImageView_placeholder);
                typedArray.recycle();
            }
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
        placeholder = drawable;
    }

    public void load(String url) {
        load(url, null);
    }

    public void load(String url, Transformation<Bitmap> transformation) {
        BitmapRequestBuilder builder = Glide.with(mContext)
                .load(url)
                .asBitmap()
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop();
        if (transformation != null)
            builder = builder.transform(transformation);
        builder.into(this);
    }
}
