package com.baic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

/**
 * Created by baic on 16/5/30.
 */
public class IOSAlertDialog extends Dialog {

    private Context mContext;
    private static IOSAlertDialog iosAlertDialog;
    private ViewHolder viewHolder;

    private IOSAlertDialog(Context context) {
        super(context, R.style.IOSAlertDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ios_alert_dialog);

        viewHolder = new ViewHolder();
    }

    public static IOSAlertDialog builder(Context context) {
        iosAlertDialog = new IOSAlertDialog(context);
        iosAlertDialog.setCanceledOnTouchOutside(false);
        iosAlertDialog.show();
        return iosAlertDialog;
    }

    public IOSAlertDialog setLeftBtnText(String text) {
        viewHolder.leftBtnTextView.setText(text);
        viewHolder.btnBoxView.setVisibility(View.VISIBLE);
        viewHolder.leftBtnView.setVisibility(View.VISIBLE);
        return iosAlertDialog;
    }

    public IOSAlertDialog setLeftBtnText(@StringRes int id) {
        viewHolder.leftBtnTextView.setText(id);
        viewHolder.btnBoxView.setVisibility(View.VISIBLE);
        viewHolder.leftBtnView.setVisibility(View.VISIBLE);
        return iosAlertDialog;
    }

    public IOSAlertDialog setLeftBtnTextColor(@ColorInt int color){
        viewHolder.leftBtnTextView.setTextColor(color);
        return iosAlertDialog;
    }

    public IOSAlertDialog setLeftBtnOnClickListener(View.OnClickListener listener){
        viewHolder.leftBtnView.setOnClickListener(listener);
        return iosAlertDialog;
    }

    public IOSAlertDialog setRightBtnOnClickListener(View.OnClickListener listener){
        viewHolder.rightBtnView.setOnClickListener(listener);
        return iosAlertDialog;
    }

    public IOSAlertDialog setLeftBtnOnTouchListener(View.OnTouchListener listener){
        viewHolder.leftBtnView.setOnTouchListener(listener);
        return iosAlertDialog;
    }

    public IOSAlertDialog setRightBtnOnTouchListener(View.OnTouchListener listener){
        viewHolder.rightBtnView.setOnTouchListener(listener);
        return iosAlertDialog;
    }

    public IOSAlertDialog setRightBtnText(String text) {
        viewHolder.rightBtnTextView.setText(text);
        viewHolder.btnBoxView.setVisibility(View.VISIBLE);
        viewHolder.rightBtnView.setVisibility(View.VISIBLE);
        if(viewHolder.leftBtnView.getVisibility() == View.VISIBLE && viewHolder.rightBtnView.getVisibility() == View.VISIBLE){
            viewHolder.spaceView.setVisibility(View.VISIBLE);
        }
        return iosAlertDialog;
    }

    public IOSAlertDialog setRightBtnText(@StringRes int id) {
        viewHolder.rightBtnTextView.setText(id);
        viewHolder.btnBoxView.setVisibility(View.VISIBLE);
        viewHolder.rightBtnView.setVisibility(View.VISIBLE);
        if(viewHolder.leftBtnView.getVisibility() == View.VISIBLE && viewHolder.rightBtnView.getVisibility() == View.VISIBLE){
            viewHolder.spaceView.setVisibility(View.VISIBLE);
        }
        return iosAlertDialog;
    }

    public IOSAlertDialog setRightBtnTextColor(@ColorInt int color){
        viewHolder.rightBtnTextView.setTextColor(color);
        return iosAlertDialog;
    }

    public IOSAlertDialog setContextText(String text){
        viewHolder.contentView.setText(text);
        viewHolder.contentView.setVisibility(View.VISIBLE);
        return iosAlertDialog;
    }

    public IOSAlertDialog setContextText(@StringRes int id){
        viewHolder.contentView.setText(id);
        viewHolder.contentView.setVisibility(View.VISIBLE);
        return iosAlertDialog;
    }

    public IOSAlertDialog setContentTextColor(@ColorInt int color){
        viewHolder.contentView.setTextColor(color);
        return iosAlertDialog;
    }

    public IOSAlertDialog setTitleText(String text){
        viewHolder.titleView.setText(text);
        viewHolder.titleView.setVisibility(View.VISIBLE);
        return iosAlertDialog;
    }

    public IOSAlertDialog setTitleText(@StringRes int id){
        viewHolder.titleView.setText(id);
        viewHolder.titleView.setVisibility(View.VISIBLE);
        return iosAlertDialog;
    }

    public IOSAlertDialog setTitleTextColor(@ColorInt int color){
        viewHolder.titleView.setTextColor(color);
        return iosAlertDialog;
    }

    public static void close(){
        iosAlertDialog.dismiss();
    }

    class ViewHolder {
        TextView titleView;
        TextView contentView;
        View btnBoxView;
        View leftBtnView;
        TextView leftBtnTextView;
        View spaceView;
        View rightBtnView;
        TextView rightBtnTextView;

        public ViewHolder() {
            titleView = (TextView) findViewById(R.id.ios_alert_dialog_title);
            titleView.setVisibility(View.GONE);
            contentView = (TextView) findViewById(R.id.ios_alert_dialog_content);
            contentView.setVisibility(View.GONE);
            btnBoxView = findViewById(R.id.ios_alert_dialog_btn_box);
            btnBoxView.setVisibility(View.GONE);
            leftBtnView = findViewById(R.id.ios_alert_dialog_left_btn);
            leftBtnView.setVisibility(View.GONE);
            leftBtnTextView = (TextView) findViewById(R.id.ios_alert_dialog_left_btn_text);
            spaceView = findViewById(R.id.ios_alert_dialog_btn_space);
            spaceView.setVisibility(View.GONE);
            rightBtnView = findViewById(R.id.ios_alert_dialog_right_btn);
            rightBtnView.setVisibility(View.GONE);
            rightBtnTextView = (TextView) findViewById(R.id.ios_alert_dialog_right_btn_text);
        }
    }
}
