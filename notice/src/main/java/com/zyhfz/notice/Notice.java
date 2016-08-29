package com.zyhfz.notice;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

/**
 * Created by baic on 16/5/17.
 */
public class Notice implements Serializable {

    private static Notice instance;

    private static EventBus eventBus;

    private Context mContext;

    private NoticeListener noticeListener;

    public void setNoticeListener(NoticeListener listener) {
        noticeListener = listener;
    }

    public enum Key {
        DEFAULT_KEY
    }

    public static Notice getInstance() {
        if (instance != null) {
            return instance;
        }
        eventBus = EventBus.getDefault();
        instance = new Notice();
        return instance;
    }

    public Notice register(Context context) {
        mContext = context;
        if (eventBus != null) {
            eventBus.register(context);
        }
        if (noticeListener != null) {
            noticeListener.onNoticeRegister(context);
        }
        return this;
    }

    public void unregister(Context context) {
        if (eventBus != null) {
            eventBus.unregister(context);
        }
        if (noticeListener != null) {
            noticeListener.onNoticeUnRegister(context);
        }
    }

    public static void post(NoticeMessage msg) {
        if (eventBus != null && msg.getKey() != null) {
            eventBus.post(msg);
            NoticeListener listener = Notice.getInstance().noticeListener;
            if (listener != null) {
                listener.onNoticePost(msg);
            }
        }
    }

    public static void post(Object msg) {
        if (eventBus != null && msg != null) {
            NoticeMessage noticeMessage = null;
            if (msg instanceof String) {
                noticeMessage = new NoticeMessage(Key.DEFAULT_KEY, (String) msg);
            } else {
                noticeMessage = new NoticeMessage(Key.DEFAULT_KEY, msg);
            }
            post(noticeMessage);
        }
    }

    public static void clearCaches() {
        EventBus.clearCaches();
    }

    public interface NoticeListener {

        void onNoticePost(NoticeMessage msg);

        void onNoticeRegister(Context context);

        void onNoticeUnRegister(Context context);
    }
}
