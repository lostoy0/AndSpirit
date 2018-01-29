package com.dragon.android.spirit.phone;

import android.content.Context;
import android.content.Intent;

/**
 * Created by raymondlee on 2018/1/14.
 */
public class PhoneManager {

    private static PhoneManager sInstance = new PhoneManager();

    private PhoneManager() {}

    public static PhoneManager getInstance() {
        return sInstance;
    }

    public void start(Context context) {
        Intent service=new Intent(context,PhoneDogService.class);//显示、隐式
        context.startService(service);
    }
}
