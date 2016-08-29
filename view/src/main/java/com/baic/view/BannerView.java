package com.baic.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baic on 16/5/24.
 */
public class BannerView extends RelativeLayout {

    private Context mContext;
    private List<Object> sourceList;
    private List<Object> dataList;
    private DataListener dataListener;
    private ViewPager mViewPager;
    private List<View> progressList;
    private Handler handler;
    private Runnable runnable;

    private boolean isRunnable = false;
    private boolean isAuto = true;
    private boolean isLoop = false;

    private long expires = 5000;
    private int progressLayoutMarginBottom = 20;
    private int progressSpaceWidth = 15;

    public BannerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        readAttrs(attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        readAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        readAttrs(attrs);
    }

    private void readAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.BannerView,
                    0, 0);
            if (typedArray != null) {
                isAuto = typedArray.getBoolean(R.styleable.BannerView_isAuto, true);
                isLoop = typedArray.getBoolean(R.styleable.BannerView_isLoop, false);
                expires = typedArray.getInteger(R.styleable.BannerView_expires, 5000);
                progressLayoutMarginBottom = typedArray.getInteger(R.styleable.BannerView_progressLayoutMarginBottom, 20);
                progressSpaceWidth = typedArray.getInteger(R.styleable.BannerView_progressSpaceWidth, 15);
                typedArray.recycle();
            }
        }
    }

    public interface DataListener {

        View onCreatePageView();

        void onBindPageView(View v, int position, Object data);

        void onBindProgressLayout(LinearLayout layout);

        View onCreateProgressView();

        void onBindRestoreProgress(View v);

        void onBindSelectedProgress(View v);
    }

    public BannerView setting(int expires, boolean isAuto, int progressLayoutMarginBottom, int progressSpaceWidth) {
        this.expires = expires;
        this.isAuto = isAuto;
        this.progressLayoutMarginBottom = progressLayoutMarginBottom;
        this.progressSpaceWidth = progressSpaceWidth;
        return this;
    }

    public BannerView init(Context context, List<Object> list, DataListener listener) {
        this.sourceList = list;
        this.dataList = new ArrayList<>(list);
        this.dataListener = listener;
        this.mContext = context;
        this.removeAllViews();
        this.mViewPager = new ViewPager(this.mContext);
        this.mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (dataListener != null && container.getChildCount() <= dataList.size()) {
                    View view = dataListener.onCreatePageView();
                    dataListener.onBindPageView(view, position, dataList.get(position));
                    ViewParent vp = view.getParent();
                    if (vp != null) {
                        ((ViewGroup) vp).removeView(view);
                    }
                    container.addView(view);
                    return view;
                }
                return null;
            }
        });

        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int size = progressList.size();
                position = (position % size + size) % size;

                if (position == size - 1 && !isRunnable && isLoop) {
                    dataList.addAll(sourceList);
                    mViewPager.getAdapter().notifyDataSetChanged();
                }

                if (dataListener != null) {
                    for (int i = 0; i < size; i++) {
                        if (size != position) {
                            dataListener.onBindRestoreProgress(progressList.get(i));
                        }
                    }
                    dataListener.onBindSelectedProgress(progressList.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        if (isAuto) {
                            isRunnable = false;
                            handler.removeCallbacks(runnable);
                        }
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (isAuto) {
                            isRunnable = true;
                            handler.postDelayed(runnable, expires);
                        }
                        break;
                }
            }
        });

        this.addView(this.mViewPager);

        if (dataListener != null) {
            LinearLayout progressLayout = new LinearLayout(this.mContext);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            layoutParams.setMargins(0, 0, 0, progressLayoutMarginBottom);
            progressLayout.setGravity(Gravity.CENTER);
            progressLayout.setLayoutParams(layoutParams);
            dataListener.onBindProgressLayout(progressLayout);
            progressList = new ArrayList<>();
            int len = sourceList.size();
            for (int i = 0; i < len; i++) {
                View progressView = dataListener.onCreateProgressView();
                progressLayout.addView(progressView);
                if (i < len - 1) {
                    View space = new View(this.mContext);
                    space.setLayoutParams(new ViewGroup.LayoutParams(progressSpaceWidth, 0));
                    progressLayout.addView(space);
                }
                progressList.add(progressView);
            }
            if (len > 0) {
                dataListener.onBindSelectedProgress(progressList.get(0));
            }
            this.addView(progressLayout);
        }

        if (isAuto) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.removeCallbacks(this);
                    int index = mViewPager.getCurrentItem();
                    if (index == dataList.size() - 1) {
                        mViewPager.setCurrentItem(0);
                    } else {
                        mViewPager.setCurrentItem(++index);
                    }
                    handler.postDelayed(this, expires);
                }
            };

            handler = new Handler();
            isRunnable = true;
            handler.postDelayed(runnable, expires);
        }
        return this;
    }

    public void destroy() {
        if(runnable != null && handler != null){
            handler.removeCallbacks(runnable);
        }
    }
}
