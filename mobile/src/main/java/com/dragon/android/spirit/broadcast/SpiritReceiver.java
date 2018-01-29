package com.dragon.android.spirit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.AlarmManagerCompat;

import com.dragon.android.spirit.phone.PhoneManager;

/**
 * Created by raymondlee on 2018/1/14.
 */
public class SpiritReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //start phone listener
        PhoneManager.getInstance().start(context);
    }

}
