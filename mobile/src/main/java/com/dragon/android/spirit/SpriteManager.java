package com.dragon.android.spirit;

import android.content.Context;

import com.dragon.android.spirit.location.LocationManager;

/**
 * Created by raymondlee on 2018/1/24.
 */

public class SpriteManager {
    public static void init(Context context) {
        LocationManager.getInstance().init(context);
    }
}
