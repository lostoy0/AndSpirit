package com.dragon.android.spirit;

import android.content.Context;
import android.content.Intent;

import com.dragon.android.spirit.location.LocationManager;
import com.dragon.android.spirit.phone.PhoneManager;
import com.dragon.android.spirit.service.SpiritService;

import java.security.PublicKey;

/**
 * Created by raymondlee on 2018/1/24.
 */

public class SpriteManager {
    public static void init(Context context) {
        LocationManager.getInstance().init(context);
        PhoneManager.getInstance().start(context);
    }

    public static void startService(Context context) {
        context.startService(new Intent(context, SpiritService.class));
        PhoneManager.getInstance().start(context);
    }
}
