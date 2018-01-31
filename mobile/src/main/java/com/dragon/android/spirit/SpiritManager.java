package com.dragon.android.spirit;

import android.content.Context;
import android.content.Intent;

import com.dragon.android.spirit.location.LocationManager;
import com.dragon.android.spirit.phone.PhoneManager;
import com.dragon.android.spirit.service.SpiritJobService;
import com.dragon.android.spirit.service.SpiritLocalService;
import com.dragon.android.spirit.service.SpiritRemoteService;

/**
 * Created by raymondlee on 2018/1/24.
 */

public class SpiritManager {

    public static void init(Context context) {
        PhoneManager.getInstance().init(context);
        LocationManager.getInstance().init(context);
    }

    public static void startCoreService(Context context) {
        context.startService(new Intent(context, SpiritLocalService.class));
        context.startService(new Intent(context, SpiritRemoteService.class));
        SpiritJobService.StartJob(context);
    }

}
