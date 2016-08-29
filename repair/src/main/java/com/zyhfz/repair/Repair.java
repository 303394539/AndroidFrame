package com.zyhfz.repair;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by baic on 16/7/4.
 */
public class Repair {

    private Context mContext;

    private PatchManager patchManager;

    private static Repair repair;

    private static String baseFileUrl;

    public static final String FIX = "Repair_";
    public static final String SUFFIX = ".apatch";

    public Repair(Context context, String appVersion) {
        this.mContext = context;
        this.patchManager = new PatchManager(context);
        this.patchManager.init(appVersion);
        this.baseFileUrl = FIX + String.valueOf(System.currentTimeMillis()) + SUFFIX;
    }

    public static Repair create(Context context, String appVersion) {
        repair = new Repair(context, appVersion);
        return repair;
    }

    public Repair builderDownLoadPatch(String downLoadUrl) {
        mContext.registerReceiver(new DownloadCompleteReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downLoadUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setAllowedOverRoaming(false);
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, baseFileUrl);
        ((DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        return this;
    }

    private class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                addPatch(Uri.withAppendedPath(Uri.fromFile(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)), baseFileUrl).getPath())
                        .deleteFile();
                mContext.unregisterReceiver(this);
            }
        }
    }

    public Repair addPatch(String fileUrl) {
        try {
            patchManager.addPatch(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Repair deleteFile(){
        for (File file: mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).listFiles()) {
            if(file.exists() && file.getPath().indexOf(FIX) > 0 && file.getPath().endsWith(SUFFIX)){
                if(!file.delete()){
                    Log.e("Repair:", file.getPath() + "，删除失败");
                }
            }
        }
        return this;
    }

    public Repair builder() {
        patchManager.loadPatch();
        return this;
    }
}
