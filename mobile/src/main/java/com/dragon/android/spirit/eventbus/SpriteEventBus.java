package com.dragon.android.spirit.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lifanlong on 2018/1/24.
 */

public class SpriteEventBus {

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

}
