package com.baic.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baic on 16/5/3.
 */
public class HeaderRecyclerView extends RecyclerView {

    public HeaderRecyclerView(Context context) {
        super(context);
    }

    public HeaderRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) getLayoutManager();
            if (gridLayoutManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup((Adapter) adapter, gridLayoutManager));
                setLayoutManager(gridLayoutManager);
            }
        }
        super.setAdapter(adapter);
    }

    public abstract static class Adapter<VH extends ViewHolder, Header, Item, Footer> extends RecyclerView.Adapter<VH> {

        protected static final int TYPE_HEADER = 1;
        protected static final int TYPE_ITEM = 0;
        protected static final int TYPE_FOOTER = -1;

        private Header header;
        private List<Item> itemList = new ArrayList<Item>();
        private Footer footer;
        private boolean showFooter = true;

        @Override
        public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH viewHolder;
            if (isHeader4Type(viewType)) {
                viewHolder = onCreateHeaderViewHolder(parent);
            } else if (isFooter4Type(viewType)) {
                viewHolder = onCreateFooterViewHolder(parent);
            } else {
                viewHolder = onCreateItemViewHolder(parent, viewType);
            }
            return viewHolder;

        }

        protected abstract VH onCreateHeaderViewHolder(ViewGroup parent);

        protected VH onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        protected abstract VH onCreateFooterViewHolder(ViewGroup parent);

        @Override
        public final void onBindViewHolder(VH holder, int position) {
            if (isHeader4Position(position)) {
                onBindHeaderViewHolder(holder);
            } else if (isFooter4Position(position)) {
                onBindFooterViewHolder(holder);
            } else {
                onBindItemViewHolder(holder, position);
            }
        }

        protected abstract void onBindHeaderViewHolder(VH holder);

        protected void onBindItemViewHolder(VH holder, int position) {
        }

        protected abstract void onBindFooterViewHolder(VH holder);

        @Override
        public final void onViewRecycled(VH holder) {
            int position = holder.getAdapterPosition();
            if (isHeader4Position(position)) {
                onHeaderViewRecycled(holder);
            } else if (isFooter4Position(position)) {
                onFooterViewRecycled(holder);
            } else {
                onItemViewRecycled(holder);
            }
        }

        protected void onHeaderViewRecycled(VH holder) {
        }

        protected void onItemViewRecycled(VH holder) {
        }

        protected void onFooterViewRecycled(VH holder) {
        }

        @Override
        public int getItemViewType(int position) {
            int viewType = TYPE_ITEM;
            if (isHeader4Position(position)) {
                viewType = TYPE_HEADER;
            } else if (isFooter4Position(position)) {
                viewType = TYPE_FOOTER;
            }
            return viewType;
        }

        @Override
        public int getItemCount() {
            int size = getItemList().size();
            if (hasHeader()) {
                size++;
            }
            if (hasFooter()) {
                size++;
            }
            return size;
        }

        public Header getHeader() {
            return header;
        }

        public Item getItem(int position) {
            if (hasHeader() && hasItemList()) {
                position--;
            }
            return getItemList().get(position);
        }

        public Footer getFooter() {
            return footer;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public void setItemList(List<Item> itemList) {
            if (itemList != null) {
                this.itemList = itemList;
            }
        }

        public void setFooter(Footer footer) {
            this.footer = footer;
        }

        public void showFooter() {
            this.setShowFooter(Boolean.TRUE);
            notifyDataSetChanged();
        }

        public void hideFooter() {
            this.setShowFooter(Boolean.FALSE);
            notifyDataSetChanged();
        }

        public boolean isHeader4Position(int position) {
            return hasHeader() && position == 0;
        }

        public boolean hasHeader() {
            return getHeader() != null;
        }

        public boolean isFooter4Position(int position) {
            return hasFooter() && position == getItemCount() - 1;
        }

        public boolean hasFooter() {
            return getFooter() != null;
        }

        protected boolean isHeader4Type(int viewType) {
            return viewType == TYPE_HEADER;
        }

        protected boolean isFooter4Type(int viewType) {
            return viewType == TYPE_FOOTER;
        }

        public boolean hasItemList() {
            return getItemList() == null || getItemList().size() > 0;
        }

        public List<Item> getItemList() {
            return itemList;
        }

        public boolean isShowFooter() {
            return showFooter;
        }

        private void setShowFooter(boolean showFooter) {
            this.showFooter = showFooter;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views = new SparseArray<View>();

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = itemView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }
    }

    private class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        private final Adapter adapter;
        private final GridLayoutManager gridLayoutManager;

        public SpanSizeLookup(Adapter adapter, GridLayoutManager layoutManager) {
            this.adapter = adapter;
            this.gridLayoutManager = layoutManager;
        }

        @Override
        public int getSpanSize(int position) {
            return adapter.isHeader4Position(position) || adapter.isFooter4Position(position) ? gridLayoutManager.getSpanCount() : 1;
        }
    }
}
