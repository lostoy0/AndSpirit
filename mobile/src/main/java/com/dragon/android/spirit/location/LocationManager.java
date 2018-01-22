package com.dragon.android.spirit.location;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by raymondlee on 2018/1/14.
 */
public class LocationManager {

    private static LocationManager sInstance = new LocationManager();

    public static LocationManager getInstance() {
        return sInstance;
    }

    private LocationClient mLocationClient = null;
    private SpiritLocationListener mLocationListener = null;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    public void init(Context context) {
        //声明LocationClient类
        mLocationClient = new LocationClient(context.getApplicationContext());

        LocationClientOption option = new LocationClientOption();

        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);

        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationListener = new SpiritLocationListener();
        mLocationClient.setLocOption(option);

        //注册监听函数
        mLocationClient.registerLocationListener(mLocationListener);
        mLocationClient.start();
    }

    public class SpiritLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();

            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();

            Log.i("raymond", "location info:" + location.toString());
        }
    }
}
