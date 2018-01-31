package com.dragon.android.spirit.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dragon.android.spirit.SpiritManager;

/**
 * Created by raymondlee on 2018/1/31.
 */

public class SpiritReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SpiritManager.startCoreService(context);
    }
}
