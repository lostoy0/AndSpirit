package com.dragon.android.spirit;

import android.app.Application;

import com.dragon.android.spirit.location.LocationManager;

/**
 * Created by raymondlee on 2018/1/14.
 */
public class SpiritApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initBaiduMapSDK();
    }

    private void initBaiduMapSDK() {
        LocationManager.getInstance().init(this);
    }

}
