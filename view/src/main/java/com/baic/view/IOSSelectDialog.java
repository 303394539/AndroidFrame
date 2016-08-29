package com.baic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by baic on 16/5/30.
 */
public class IOSSelectDialog extends Dialog {

    private static IOSSelectDialog iosSelectDialog;
    private Context mContext;
    private ViewHolder viewHolder;
    private int btnCount = 0;

    private IOSSelectDialog(Context context) {
        super(context, R.style.IOSSelectDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ios_select_dialog);

        viewHolder = new ViewHolder();
    }

    public static IOSSelectDialog builder(Context context) {
        iosSelectDialog = new IOSSelectDialog(context);
        iosSelectDialog.setCanceledOnTouchOutside(false);
        iosSelectDialog.getWindow().setGravity(Gravity.BOTTOM);
        iosSelectDialog.show();
        return iosSelectDialog;
    }

    public IOSSelectDialog setContextText(String text) {
        viewHolder.contentView.setText(text);
        viewHolder.contentView.setVisibility(View.VISIBLE);
        return iosSelectDialog;
    }

    public IOSSelectDialog setContextText(@StringRes int id) {
        viewHolder.contentView.setText(id);
        viewHolder.contentView.setVisibility(View.VISIBLE);
        return iosSelectDialog;
    }

    public IOSSelectDialog setContextTextColor(@ColorInt int color) {
        viewHolder.contentView.setTextColor(color);
        return iosSelectDialog;
    }

    public IOSSelectDialog setAloneBtnText(String text) {
        viewHolder.aloneTextView.setText(text);
        viewHolder.aloneView.setVisibility(View.VISIBLE);
        return iosSelectDialog;
    }

    public IOSSelectDialog setAloneBtnTextColor(@ColorInt int color) {
        viewHolder.aloneTextView.setTextColor(color);
        return iosSelectDialog;
    }

    public IOSSelectDialog setAloneBtnOnClickListener(View.OnClickListener listener) {
        viewHolder.aloneView.setOnClickListener(listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog setAloneBtnOnTouchListener(View.OnTouchListener listener) {
        viewHolder.aloneView.setOnTouchListener(listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(String text, @ColorInt int color, View.OnClickListener listener) {
        View v = createBtn(text, color);
        v.setOnClickListener(listener);
        viewHolder.btnListView.addView(v);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(String text, @ColorInt int color, View.OnTouchListener listener){
        View v = createBtn(text, color);
        v.setOnTouchListener(listener);
        viewHolder.btnListView.addView(v);
        return iosSelectDialog;
    }

    private View createBtn(String text, @ColorInt int color){
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        if(btnCount > 0){
            View lineView = new View(mContext);
            lineView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            lineView.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
            linearLayout.addView(lineView);
        }
        TextView v = new TextView(mContext);
        v.setPadding(0, 30, 0, 30);
        v.setGravity(Gravity.CENTER);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setText(text);
        v.setTextColor(color);
        linearLayout.addView(v);
        btnCount++;
        return linearLayout;
    }

    public IOSSelectDialog addBtn(@StringRes int id, @ColorInt int color, View.OnClickListener listener){
        addBtn(mContext.getResources().getString(id), color, listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(@StringRes int id, @ColorInt int color, View.OnTouchListener listener){
        addBtn(mContext.getResources().getString(id), color, listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(String text, View.OnClickListener listener){
        addBtn(text, mContext.getResources().getColor(R.color.ios_text_blue), listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(String text, View.OnTouchListener listener){
        addBtn(text, mContext.getResources().getColor(R.color.ios_text_blue), listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(@StringRes int id, View.OnClickListener listener){
        addBtn(id, mContext.getResources().getColor(R.color.ios_text_blue), listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtn(@StringRes int id, View.OnTouchListener listener){
        addBtn(id, mContext.getResources().getColor(R.color.ios_text_blue), listener);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtnLayout(@LayoutRes int layoutId, View.OnClickListener listener) {
        View v = getLayoutInflater().inflate(layoutId, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(listener);
        viewHolder.btnListView.addView(v);
        return iosSelectDialog;
    }

    public IOSSelectDialog addBtnLayout(@LayoutRes int layoutId, View.OnTouchListener listener) {
        View v = getLayoutInflater().inflate(layoutId, null);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setOnTouchListener(listener);
        viewHolder.btnListView.addView(v);
        return iosSelectDialog;
    }

    public static void close() {
        iosSelectDialog.dismiss();
    }

    class ViewHolder {
        TextView contentView;
        View aloneView;
        TextView aloneTextView;
        LinearLayout btnListView;

        public ViewHolder() {
            contentView = (TextView) findViewById(R.id.ios_select_dialog_content);
            contentView.setVisibility(View.GONE);
            aloneView = findViewById(R.id.ios_select_dialog_alone_btn);
            aloneView.setVisibility(View.GONE);
            aloneTextView = (TextView) findViewById(R.id.ios_select_dialog_alone_btn_text);
            btnListView = (LinearLayout) findViewById(R.id.ios_select_dialog_btn_list);
        }
    }
}