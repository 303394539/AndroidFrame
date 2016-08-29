package com.baic.view;

import android.Manifest;
import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by baic on 16/5/26.
 */
public class ScanQrCodeView extends ZXingView implements QRCodeView.Delegate, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private Context mContext;

    public ScanQrCodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
        this.setResultHandler(this);
    }

    public ScanQrCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.setResultHandler(this);
    }

    private ScanQrCodeListener scanQrCodeListener;
    private ScanQrCodePermissionsListener scanQrCodePermissionsListener;

    public interface ScanQrCodeListener {
        void onResult(String result);

        void onError();
    }

    public interface ScanQrCodePermissionsListener {
        void onPermissionsGranted();

        void onPermissionsDenied();
    }

    public ScanQrCodeView setScanQrCodeListener(ScanQrCodeListener listener) {
        this.scanQrCodeListener = listener;
        return this;
    }

    public ScanQrCodeView setScanQrCodePermissionsListener(ScanQrCodePermissionsListener listener) {
        this.scanQrCodePermissionsListener = listener;
        return this;
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (scanQrCodeListener != null) {
            scanQrCodeListener.onResult(result);
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        if (scanQrCodeListener != null) {
            scanQrCodeListener.onError();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (scanQrCodePermissionsListener != null) {
            scanQrCodePermissionsListener.onPermissionsGranted();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (scanQrCodePermissionsListener != null) {
            scanQrCodePermissionsListener.onPermissionsDenied();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public ScanQrCodeView requestCodeQrCodePermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.FLASHLIGHT};
        requestCodeQrCodePermissions(permissions);
        return this;
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    public void requestCodeQrCodePermissions(String[] permissions) {
        if (!EasyPermissions.hasPermissions(mContext, permissions)) {
            EasyPermissions.requestPermissions(mContext, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, permissions);
        }
    }
}
