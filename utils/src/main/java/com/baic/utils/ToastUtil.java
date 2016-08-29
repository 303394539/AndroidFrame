package com.baic.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by baic on 16/5/16.
 */
public class ToastUtil {

    private static Toast toast = null;

    private ToastUtil() {

    }

    public static void displayToastMsg(Context context, String toastMsg) {
        showMessage(context, toastMsg, Toast.LENGTH_SHORT);
    }

    public static void displayToastMsg(Context context, int stringId) {
        displayToastMsg(context, context.getResources().getString(stringId));
    }

    public static void showMessage(final Context act, final String msg,
                                   final int len) {
        try {
            if (toast == null) {
                toast = Toast.makeText(act, msg, len);
            } else {
                try {
                    toast.setText(msg);
                } catch (Exception e) {
                    Toast.makeText(act, msg, len).show();
                    return;
                }

            }
            toast.show();
        } catch (Exception ex) {
        }
    }

    public static void displayLongToastMsg(Context context, String toastMsgLong) {
        showMessage(context, toastMsgLong, Toast.LENGTH_LONG);
    }

    public static void displayLongToastMsg(Context context, int stringId) {
        displayLongToastMsg(context, context.getResources().getString(stringId));
    }
}
