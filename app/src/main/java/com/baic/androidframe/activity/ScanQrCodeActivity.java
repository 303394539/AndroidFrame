package com.baic.androidframe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.baic.androidframe.R;
import com.baic.utils.StringUtil;
import com.baic.view.ScanQrCodeView;
import com.zyhfz.notice.Notice;

import java.util.HashMap;
import java.util.Map;

public class ScanQrCodeActivity extends BaseActivity {

    private ScanQrCodeView scanQrCodeView;

    public final static int REQUEST_CODE = 404;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        Map<String, Object> query = new HashMap<>();
        query.put("a", "121");
//        query.put("b", "1211");

        Notice.post(StringUtil.formatUrl("1234", query));

        scanQrCodeView = (ScanQrCodeView) findViewById(R.id.sqcv);

        scanQrCodeView.setScanQrCodeListener(new ScanQrCodeView.ScanQrCodeListener() {
            @Override
            public void onResult(String result) {
                Notice.post(result);
                Intent intent = new Intent();
                intent.putExtra("qc_result", result);
                setResult(REQUEST_CODE, intent);
                finish();
            }

            @Override
            public void onError() {

            }
        }).requestCodeQrCodePermissions().startSpotDelay(1000);
    }

    @Override
    protected void onDestroy() {
        scanQrCodeView.stopSpot();
        scanQrCodeView.stopCamera();
        super.onDestroy();
    }
}
