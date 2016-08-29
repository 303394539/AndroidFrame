package com.baic.androidframe.activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.baic.androidframe.R;
import com.baic.androidframe.application.Application;
import com.baic.androidframe.model.User;
import com.baic.cache.simplecache.Cache;
import com.baic.net.api.ApiConnect;
import com.baic.net.api.JniUtils;
import com.baic.net.api.intf.PagingCallback;
import com.baic.net.api.intf.SimpleCallback;
import com.baic.net.api.model.PagingRequestParam;
import com.baic.net.api.model.PagingResponseObj;
import com.baic.utils.DateUtil;
import com.baic.utils.IntentUtil;
import com.baic.utils.JsonUtil;
import com.baic.utils.LocationUtil;
import com.baic.utils.StringUtil;
import com.baic.utils.ToastUtil;
import com.baic.view.AlphaSafeWebView;
import com.baic.view.BannerView;
import com.baic.view.GlideImageView;
import com.baic.view.GlidePhotoView;
import com.baic.view.HeaderRecyclerView;
import com.baic.view.IOSAlertDialog;
import com.baic.view.IOSSelectDialog;
import com.baic.view.LoadingView;
import com.baic.view.PagingRecyclerView;
import com.baic.view.ToggleButtonView;
import com.zyhfz.notice.Notice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GlidePhotoView glidePhotoView = (GlidePhotoView) findViewById(R.id.gpv);
//        glidePhotoView.loadImageUrl("http://img15.3lian.com/2015/a1/16/d/203.jpg");
//        Application.getInstance().portService.queryAppCity();
//        IntentUtil.create(this, ScanQrCodeActivity.class).open();
//        finish();
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> map2 = new HashMap<>();
//        map.put("a", true);
//        map2.put("a", true);
//        map.put("b", "asdasd");
//        map2.put("b", "asdasd");
//        map.put("c", "???");
//        map2.put("c", "???");
//        map.put("d", map2);
//        Log.e("asd", StringUtil.formatUrl("http://www.baidu.com", map));
//        Log.e("getSign:", JniUtils.getSign(this, "1", "1"));
//        Log.e("getSign:", JniUtils.getSign(this, "1", "1"));
//        Log.e("getSign:", JniUtils.getSign(this, "1", "1"));
//        Log.e("getSign:", JniUtils.getSign(this, "1", "1"));
//        Log.e("getSign:", JniUtils.getSign(this, "1", "1"));
//        Log.e("getResult:", JniUtils.getResult(this, "1", "1"));
//        Log.e("getResult:", JniUtils.getResult(this, "1", "1"));
//        Log.e("getResult:", JniUtils.getResult(this, "1", "1"));
//        Log.e("getResult:", JniUtils.getResult(this, "1", "1"));
//        Log.e("getCertificateSHA1Fingerprint:", getCertificateSHA1Fingerprint(this));
//
//        final PagingRecyclerView pagingRecyclerView = (PagingRecyclerView) findViewById(R.id.hrv);
//        pagingRecyclerView.setSimpleListener(new PagingRecyclerView.SimpleListener() {
//            @Override
//            public void loadData(int offset, int size) {
////                PagingRequestParam requestParam = new PagingRequestParam();
////                Map<String, Object> obj = new HashMap();
////                obj.put("actcode", 201605111);
////                requestParam.setObj(obj);
////
////                ApiConnect conn = Application.getInstance().portService.postrank(requestParam, new PagingCallback() {
////                    @Override
////                    public void success(PagingResponseObj o) {
////                        pagingRecyclerView.next(o.getTotal(), o.getList());
////                    }
////
////                    @Override
////                    public void error(int code, String msg) {
////                    }
////
////                    @Override
////                    public void fail(String msg) {
////                    }
////                }, User.class);
////
////                conn.next(offset, size);
//                Application.getInstance().portService.queryAppCity(new SimpleCallback() {
//                    @Override
//                    public void success(Object o) {
//                        Log.e("o:", JsonUtil.objectToString(((List)o).get(0)));
//                    }
//
//                    @Override
//                    public void error(int code, String msg) {
//                    }
//
//                    @Override
//                    public void fail(String msg) {
//
//                    }
//                }).get();
//            }
//
//            @Override
//            public HeaderRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                return new ViewHolder(getLayoutInflater().inflate(R.layout.image, parent, false));
//            }
//
//            @Override
//            public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, Object o) {
//                final HeaderHolder holder = (HeaderHolder) viewHolder;
//
//                holder.bannerView.init(MainActivity.this, (List<Object>) o, new BannerView.DataListener() {
//
//                    @Override
//                    public View onCreatePageView() {
//                        return new GlideImageView(MainActivity.this);
//                    }
//
//                    @Override
//                    public void onBindPageView(View v, int position, Object data) {
//                        GlideImageView glideImageView = (GlideImageView) v;
//                        User user = (User) data;
//                        glideImageView.load("http://zhongshanhfz.cq.webhante.com" + user.getBigimgurl());
//                    }
//
//                    @Override
//                    public void onBindProgressLayout(LinearLayout layout) {
//                    }
//
//                    @Override
//                    public View onCreateProgressView() {
//                        View v = new View(MainActivity.this);
//                        v.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
//                        v.setBackgroundResource(R.color.colorAccent);
//                        return v;
//                    }
//
//                    @Override
//                    public void onBindRestoreProgress(View v) {
//                        v.setBackgroundResource(R.color.colorAccent);
//                    }
//
//                    @Override
//                    public void onBindSelectedProgress(View v) {
//                        v.setBackgroundResource(R.color.colorPrimaryDark);
//                    }
//                });
//            }
//
//            @Override
//            public void onBindFooterViewHolder(RecyclerView.ViewHolder viewHolder, Object o) {
//                final HeaderHolder holder = (HeaderHolder) viewHolder;
//
//                holder.bannerView.init(MainActivity.this, (List<Object>) o, new BannerView.DataListener() {
//
//                    @Override
//                    public View onCreatePageView() {
//                        return new GlideImageView(MainActivity.this);
//                    }
//
//                    @Override
//                    public void onBindPageView(View v, int position, Object data) {
//                        GlideImageView glideImageView = (GlideImageView) v;
//                        User user = (User) data;
//                        glideImageView.load("http://zhongshanhfz.cq.webhante.com" + user.getBigimgurl());
//                    }
//
//                    @Override
//                    public void onBindProgressLayout(LinearLayout layout) {
//                    }
//
//                    @Override
//                    public View onCreateProgressView() {
//                        View v = new View(MainActivity.this);
//                        v.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
//                        v.setBackgroundResource(R.color.colorAccent);
//                        return v;
//                    }
//
//                    @Override
//                    public void onBindRestoreProgress(View v) {
//                        v.setBackgroundResource(R.color.colorAccent);
//                    }
//
//                    @Override
//                    public void onBindSelectedProgress(View v) {
//                        v.setBackgroundResource(R.color.colorPrimaryDark);
//                    }
//                });
//            }
//
//            @Override
//            public HeaderRecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
//                return new HeaderHolder(getLayoutInflater().inflate(R.layout.header, parent, false));
//            }
//
//            @Override
//            public HeaderRecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent) {
//                return new HeaderHolder(getLayoutInflater().inflate(R.layout.header, parent, false));
//            }
//
//            @Override
//            public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, Object item, int position) {
//                ViewHolder holder = (ViewHolder) viewHolder;
//                User user = (User) item;
//                holder.imageView.load("http://zhongshanhfz.cq.webhante.com" + user.getBigimgurl());
//            }
//        }).start(this);
//
//
//        PagingRequestParam requestParam = new PagingRequestParam();
//        Map<String, Object> obj = new HashMap();
//        obj.put("actcode", 201605111);
//        requestParam.setObj(obj);
//        Application.getInstance().portService.postrank(requestParam, new PagingCallback() {
//            @Override
//            public void success(PagingResponseObj o) {
//                pagingRecyclerView.setHeader(o.getList());
//            }
//
//            @Override
//            public void error(int code, String msg) {
//            }
//
//            @Override
//            public void fail(String msg) {
//            }
//        }, User.class).next(0, 5);
//
//        Application.getInstance().portService.postrank(requestParam, new PagingCallback() {
//            @Override
//            public void success(PagingResponseObj o) {
//                pagingRecyclerView.setFooter(o.getList());
//            }
//
//            @Override
//            public void error(int code, String msg) {
//            }
//
//            @Override
//            public void fail(String msg) {
//            }
//        }, User.class).next(0, 5);
//
//        AlphaSafeWebView alphaWebView = (AlphaSafeWebView) findViewById(R.id.awv);
//
//        alphaWebView.setJsHelper(new AlphaSafeWebView.JsHelper(this) {
//        });
//        List<String> list = new ArrayList<>();
//        list.add("web.dev.goodhfz.com");
//        alphaWebView.setSafeDomainList(list);
//        alphaWebView.load("http://web.dev.goodhfz.com/test.html?j=%7B%22openid%22:%22oMg0RuH7wfP7ZWCr_kHfA6RYXAtQ%22,%22passport%22:%228efeba551a1c6df13af606666f57c413%22%7D");
//        ToastUtil.displayToastMsg(this, "成功");
//        Notice.post(DateUtil.getPeriodString(new Date().getTime()));
//
//        LocationUtil.getInstance().net(this, new LocationUtil.Manager.Handler() {
//            @Override
//            public void onCurrentLocation(Location location) {
//                Notice.post(location);
//            }
//
//            @Override
//            public void onCurrentAddress(Address address) {
//                Notice.post(address);
//            }
//        });

//        IOSAlertDialog.builder(this)
//                .setLeftBtnText("取消")
//                .setLeftBtnOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        IOSAlertDialog.close();
//                    }
//                })
//                .setRightBtnText("退出")
//                .setRightBtnOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        IOSAlertDialog.close();
//                    }
//                })
//                .setTitleText("退出当前账号")
//                .setContextText("确认退出当前账号？");

//        IOSSelectDialog.builder(this)
//                .setContextText("清空消息列表后，聊天纪录依然保留，确定要清空消息列表吗？")
//                .setAloneBtnText("取消")
//                .setAloneBtnOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        IOSSelectDialog.close();
//                    }
//                })
//                .addBtn("清空消息列表", Color.parseColor("#EC5430"), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });

//        final ToggleButtonView toggleBtn = (ToggleButtonView) findViewById(R.id.tbv);
//
//
//        //切换无动画
//        toggleBtn.toggle(false);
//        //开关切换事件
//        toggleBtn.setOnToggleChanged(new ToggleButtonView.OnToggleChanged(){
//            @Override
//            public void onToggle(boolean on) {
//            }
//        });
//
//        toggleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //切换开关
//                toggleBtn.toggle();
//            }
//        });

//        toggleBtn.setToggleOn();
//        toggleBtn.setToggleOff();
//        //无动画切换
//        toggleBtn.setToggleOn(false);
//        toggleBtn.setToggleOff(false);
//
//        //禁用动画
//        toggleBtn.setAnimate(false);
    }

    @Override
    public void onNotice(Object msg) {
        Log.e("onNotice1", JsonUtil.objectToString(msg));
    }

    class ViewHolder extends PagingRecyclerView.ViewHolder {

        private GlideImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = getView(R.id.image);
        }
    }

    class HeaderHolder extends PagingRecyclerView.ViewHolder {

        private BannerView bannerView;

        public HeaderHolder(View itemView) {
            super(itemView);
            bannerView = getView(R.id.bv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtil.getInstance().destroy();
    }

    public static String getCertificateSHA1Fingerprint(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
        String packageName = context.getPackageName();
        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        //X509证书，X.509是一种非常通用的证书格式
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            //加密算法的类，这里的参数可以使MD4,MD5等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA256");
            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());
            //字节到十六进制的格式转换
            hexString = StringUtil.bytes2HexString(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }
}
