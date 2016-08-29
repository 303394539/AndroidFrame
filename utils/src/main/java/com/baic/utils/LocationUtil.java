package com.baic.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by baic on 16/5/26.
 */
public class LocationUtil {

    private static Manager instance;

    public static Manager getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new Manager();
        return instance;
    }

    public static class Manager {
        private Context mContext;
        private LocationManager gpsLocationManager;
        private LocationManager netLocationManager;
        private LocationProvider gpsLocationProvider;
        private LocationProvider netLocationProvider;
        private Geocoder geocoder;

        private static final long MIN_TIME = 1000 * 60;
        private static final float MIN_DISTANCE = 100;

        public Manager() {
        }

        private void init(Context context) {
            mContext = context;
            gpsLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            netLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gpsLocationProvider = gpsLocationManager.getProvider(LocationManager.GPS_PROVIDER);
            netLocationProvider = netLocationManager.getProvider(LocationManager.NETWORK_PROVIDER);
            geocoder = new Geocoder(mContext, Locale.getDefault());
        }

        private static Handler handler;

        private interface HandlerListener {
            void onCurrentLocation(Location location);

            void onCurrentAddress(Address address);
        }

        public abstract static class Handler implements HandlerListener {
            protected void onNonePermissionAction(Context context) {
                Toast.makeText(context, "无法定位，请打开定位服务", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }

            ;
        }

        private final LocationListener locationListener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onLocationChanged(Location location) {
                handleLocation(location);
            }
        };

        private void handleLocation(Location location) {
            if (handler != null && location != null) {
                handler.onCurrentLocation(location);
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addressList.size() > 0) {
                    handler.onCurrentAddress(addressList.get(0));
                }
            }
        }

        private boolean validate() {
            if (gpsLocationProvider != null || netLocationProvider != null) {
                return true;
            }
            return false;
        }

        public void gps(Context context, Handler handler) {
            init(context);
            if (validate()) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    handler.onNonePermissionAction(mContext);
                    return;
                }
                gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, locationListener);
                this.handler = handler;
                handleLocation(gpsLocationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER));
            } else {
                handler.onNonePermissionAction(mContext);
            }
        }

        public void net(Context context, Handler handler) {
            init(context);
            if (validate()) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    handler.onNonePermissionAction(mContext);
                    return;
                }
                netLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME, MIN_DISTANCE, locationListener);
                this.handler = handler;
                handleLocation(gpsLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            } else {
                handler.onNonePermissionAction(mContext);
            }
        }

        public void destroy() {
            if(mContext != null){
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                gpsLocationManager.removeUpdates(locationListener);
                netLocationManager.removeUpdates(locationListener);
            }
        }
    }
}
