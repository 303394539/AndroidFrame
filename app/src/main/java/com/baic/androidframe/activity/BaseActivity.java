package com.baic.androidframe.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zyhfz.notice.Notice;
import com.zyhfz.notice.NoticeMessage;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by baic on 16/4/21.
 */
public class BaseActivity extends AppCompatActivity implements Notice.NoticeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notice.getInstance().register(this).setNoticeListener(this);
    }

    @Subscribe
    public void onNotice(Object msg) {

    }

    @Override
    public void onNoticePost(NoticeMessage msg) {

    }

    @Override
    public void onNoticeRegister(Context context) {

    }

    @Override
    public void onNoticeUnRegister(Context context) {

    }
}
