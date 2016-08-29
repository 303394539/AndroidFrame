package com.baic.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by baic on 16/5/4.
 */
public class PagingRecyclerView extends HeaderRecyclerView {

    private Context mContext;

    public PagingRecyclerView(Context context) {
        super(context);
        mContext = context;
    }

    public PagingRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PagingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    private SimpleListenerInterface simpleListenerInterface;
    private SimpleListener simpleListener;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int offset = 0;
    private int size = 20;
    private int total = 0;

    private Adapter adapter;

    private Object headerObj;
    private Object footerObj;

    public PagingRecyclerView setSimpleListener(SimpleListenerInterface simpleListener) {
        this.simpleListenerInterface = simpleListener;
        setSwipeRefreshLayout();
        return this;
    }

    public PagingRecyclerView setSimpleListener(SimpleListener simpleListener) {
        this.simpleListener = simpleListener;
        setSwipeRefreshLayout();
        return this;
    }

    private interface SimpleListenerInterface<VH> {

        void loadData(int offset, int size);

        ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        ViewHolder onCreateHeaderViewHolder(ViewGroup parent);

        ViewHolder onCreateFooterViewHolder(ViewGroup parent);

        void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, Object item, int position);

        void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, Object o);

        void onBindFooterViewHolder(RecyclerView.ViewHolder viewHolder, Object o);

        void onStart();

        void onScrolling(int scrollType);

        void onScrolled();

        void onStop();

        void onNoneData();

        void onStartNoneData();

    }

    public abstract static class SimpleListener implements SimpleListenerInterface {

        @Override
        public ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
            return null;
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, Object o) {

        }

        @Override
        public void onBindFooterViewHolder(RecyclerView.ViewHolder viewHolder, Object o) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onScrolling(int type) {

        }

        @Override
        public void onScrolled() {

        }

        @Override
        public void onStartNoneData() {

        }

        @Override
        public void onNoneData() {

        }
    }

    public void setSwipeRefreshLayout() {
        setSwipeRefreshLayout(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (simpleListenerInterface != null) {
                    init();
                    simpleListenerInterface.loadData(offset, size);
                }
                if (simpleListener != null) {
                    init();
                    simpleListener.loadData(offset, size);
                }
            }
        });
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        if (swipeRefreshLayout == null && getParent() instanceof SwipeRefreshLayout) {
            swipeRefreshLayout = (SwipeRefreshLayout) getParent();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        }
    }

    public void start(Context context, int size) {
        this.size = size;
        start(context);
    }

    public void start(final Context context) {

        if (getLayoutManager() == null) {
            setLayoutManager(new LinearLayoutManager(context));
        }

        adapter = new Adapter();

        this.setAdapter(adapter);

        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (simpleListenerInterface != null) {
                    simpleListenerInterface.onScrolling(newState);
                }
                if (simpleListener != null) {
                    simpleListener.onScrolling(newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到RecyclerView的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    if (simpleListenerInterface != null) {
                        simpleListenerInterface.onScrolled();
                    }
                    if (simpleListener != null) {
                        simpleListener.onScrolled();
                    }
                    if (total > adapter.getItemList().size()) {
                        if (simpleListenerInterface != null) {
                            simpleListenerInterface.loadData(offset, size);
                        }
                        if (simpleListener != null) {
                            simpleListener.loadData(offset, size);
                        }
                    } else {
                        if (simpleListenerInterface != null) {
                            simpleListenerInterface.onStop();
                            simpleListenerInterface.onNoneData();
                        }
                        if (simpleListener != null) {
                            simpleListener.onStop();
                            simpleListener.onNoneData();
                        }
                    }
                }
            }
        });

        if (simpleListenerInterface != null) {
            simpleListenerInterface.onStart();
            simpleListenerInterface.loadData(offset, size);
        }
        if (simpleListener != null) {
            simpleListener.onStart();
            simpleListener.loadData(offset, size);
        }
    }

    public void next(int total, List list) {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (list != null && list.size() > 0) {
            this.total = total;
            this.offset++;
            this.adapter.addItemList(list);
        } else {
            if (offset == 0) {
                if (simpleListenerInterface != null) {
                    simpleListenerInterface.onStartNoneData();
                }
                if (simpleListener != null) {
                    simpleListener.onStartNoneData();
                }
            }

            if (simpleListenerInterface != null) {
                simpleListenerInterface.onNoneData();
            }
            if (simpleListener != null) {
                simpleListener.onNoneData();
            }
        }
    }

    public PagingRecyclerView init() {
        return init(20);
    }

    public PagingRecyclerView init(int size) {
        this.offset = 0;
        this.size = size;
        this.total = 0;
        this.adapter = new Adapter();
        if(this.headerObj != null){
            this.adapter.setHeader(this.headerObj);
        }
        if(this.headerObj != null){
            this.adapter.setHeader(this.headerObj);
        }
        this.setAdapter(adapter);
        return this;
    }

    class Adapter extends HeaderRecyclerView.Adapter {

        public void addItemList(List list) {
            getItemList().addAll(list);
            notifyDataSetChanged();
        }

        @Override
        protected ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = null;
            if (simpleListenerInterface != null) {
                viewHolder = simpleListenerInterface.onCreateViewHolder(parent, viewType);
            }
            if (simpleListener != null) {
                viewHolder = simpleListener.onCreateViewHolder(parent, viewType);
            }
            return viewHolder;
        }

        @Override
        protected ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (simpleListenerInterface != null) {
                viewHolder = simpleListenerInterface.onCreateHeaderViewHolder(parent);
            }
            if (simpleListener != null) {
                viewHolder = simpleListener.onCreateHeaderViewHolder(parent);
            }
            return viewHolder;
        }

        @Override
        protected ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (simpleListenerInterface != null) {
                viewHolder = simpleListenerInterface.onCreateFooterViewHolder(parent);
            }
            if (simpleListener != null) {
                viewHolder = simpleListener.onCreateFooterViewHolder(parent);
            }
            return viewHolder;
        }

        @Override
        protected void onBindHeaderViewHolder(ViewHolder holder) {
            if (simpleListenerInterface != null) {
                simpleListenerInterface.onBindHeaderViewHolder(holder, getHeader());
            }
            if (simpleListener != null) {
                simpleListener.onBindHeaderViewHolder(holder, getHeader());
            }
        }

        @Override
        protected void onBindFooterViewHolder(ViewHolder holder) {
            if (simpleListenerInterface != null) {
                simpleListenerInterface.onBindFooterViewHolder(holder, getFooter());
            }
            if (simpleListener != null) {
                simpleListener.onBindFooterViewHolder(holder, getFooter());
            }
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder holder, int position) {
            if (simpleListenerInterface != null) {
                simpleListenerInterface.onBindItemViewHolder(holder, getItem(position), position);
            }
            if (simpleListener != null) {
                simpleListener.onBindItemViewHolder(holder, getItem(position), position);
            }
        }
    }

    public void setHeader(Object o) {
        if (o != null) {
            headerObj = o;
            adapter.setHeader(o);
            adapter.notifyDataSetChanged();
        }
    }

    public void setFooter(Object o) {
        if (o != null) {
            footerObj = o;
            adapter.setFooter(o);
            adapter.notifyDataSetChanged();
        }
    }
}
