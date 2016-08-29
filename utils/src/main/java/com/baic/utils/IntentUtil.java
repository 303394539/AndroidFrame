package com.baic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by baic on 16/5/19.
 */
public class IntentUtil {

    private static BaseIntent baseIntent;

    private static BaseIntentList baseIntentList;

    private IntentUtil() {

    }

    public static class BaseIntent implements Serializable {

        private Context mContent;

        private Intent intent;

        public BaseIntent(Context context, Class<?> activityClass) {
            mContent = context;
            intent = new Intent(context, activityClass);
        }

        public Intent getIntent(){
            return intent;
        }

        public void open() {
            mContent.startActivity(intent);
        }

        public void openService(){
            mContent.startService(intent);
        }

        public void open(int requestCode) {
            ((Activity) mContent).startActivityForResult(intent, requestCode);
        }

        public BaseIntent putExtra(String name, boolean value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, byte value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, char value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, short value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, int value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, long value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, float value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, double value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, String value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, CharSequence value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, Parcelable value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, Parcelable[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, Serializable value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, boolean[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, byte[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, short[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, char[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, int[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, long[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, float[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, double[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, String[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, CharSequence[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtra(String name, Bundle value) {
            intent.putExtra(name, value);
            return this;
        }

        public BaseIntent putExtras(Bundle extras) {
            intent.putExtras(extras);
            return this;
        }

        public BaseIntent putExtras(Intent src) {
            intent.putExtras(src);
            return this;
        }

        public BaseIntent putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
            intent.putCharSequenceArrayListExtra(name, value);
            return this;
        }

        public BaseIntent putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
            intent.putIntegerArrayListExtra(name, value);
            return this;
        }

        public BaseIntent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
            intent.putParcelableArrayListExtra(name, value);
            return this;
        }

        public BaseIntent putStringArrayListExtra(String name, ArrayList<String> value) {
            intent.putStringArrayListExtra(name, value);
            return this;
        }
    }

    public static class BaseIntentList implements Serializable {

        private Context mContent;

        private ArrayList<BaseIntent> baseIntentList;

        private ArrayList<Intent> intentList;

        public BaseIntentList(Context context) {
            mContent = context;
        }

        public void openList(){
            mContent.startActivities((Intent[]) intentList.toArray());
        }

        public BaseIntentList add(BaseIntent baseIntent){
            baseIntentList.add(baseIntent);
            intentList.add(baseIntent.getIntent());
            return this;
        }

        public ArrayList<BaseIntent> getBaseIntentList() {
            return baseIntentList;
        }

        public ArrayList<Intent> getIntentList() {
            return intentList;
        }
    }

    public static BaseIntent create(Context context, Class<?> activityClass) {
        baseIntent = new BaseIntent(context, activityClass);
        return baseIntent;
    }

    public static BaseIntentList createList(Context context, Class<?>... activityClasses){
        baseIntentList = new BaseIntentList(context);
        for (Class c: activityClasses) {
            baseIntentList.add(new BaseIntent(context, c));
        }
        return baseIntentList;
    }
}