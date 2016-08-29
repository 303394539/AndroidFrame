package com.baic.androidframe.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.baic.androidframe.BuildConfig;
import com.baic.androidframe.net.PortService;
import com.baic.cache.database.DBManager;
import com.baic.net.api.Api;
import com.baic.net.api.ApiConfig;
import com.baic.utils.LocationUtil;
import com.zyhfz.repair.Repair;

/**
 * Created by baic on 16/4/21.
 */
public class Application extends MultiDexApplication {

    private static Application instance;

    public PortService portService;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {
            instance = this;
            ApiConfig apiConfig = new ApiConfig();
            apiConfig.setBaseUrl(BuildConfig.BASE_URL);
            apiConfig.setPathname(BuildConfig.PATHNAME);
            apiConfig.setIsLoggable(BuildConfig.ApiIsLoggable);
            portService = Api.init(apiConfig, PortService.class);
            DBManager.init(getApplicationContext());
//            try {
//                Repair.create(getApplicationContext(), getPackageManager().getPackageInfo(getPackageName(), 0).versionName)
//                        .builderDownLoadPatch("http://htzs.dev.wx.webhante.com/patch.apatch");
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
        }
    }

    public String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
