package com.dragon.android.spirit.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lifanlong on 2018/1/24.
 */

public class PhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service=new Intent(context,PhoneDogService.class);//显示、隐式
        context.startService(service);
    }
}
