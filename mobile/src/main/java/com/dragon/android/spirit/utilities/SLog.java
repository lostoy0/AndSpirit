package com.dragon.android.spirit.utilities;

import com.dragon.android.spirit.SpiritConfig;
import com.orhanobut.logger.Logger;

/**
 * Created by raymondlee on 2018/1/31.
 */

public class SLog {

    public static void d(String message, Object... args) {
        if(SpiritConfig.DEBUG) {
            Logger.d(message, args);
        }
    }

    public static void d(Object object) {
        if(SpiritConfig.DEBUG) {
            Logger.d(object);
        }
    }

    public static void e(String message, Object... args) {
        if(SpiritConfig.DEBUG) {
            Logger.e(message, args);
        }
    }

    public static void e(Throwable throwable, String message, Object... args) {
        if(SpiritConfig.DEBUG) {
            Logger.e(throwable, message, args);
        }
    }

    public static void w(String message, Object... args) {
        if(SpiritConfig.DEBUG) {
            Logger.w(message, args);
        }
    }

    public static void i(String message, Object... args) {
        if(SpiritConfig.DEBUG) {
            Logger.i(message, args);
        }
    }

    public static void v(String message, Object... args) {
        if(SpiritConfig.DEBUG) {
            Logger.v(message, args);
        }
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(String json) {
        if(SpiritConfig.DEBUG) {
            Logger.json(json);
        }
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(String xml) {
        if(SpiritConfig.DEBUG) {
            Logger.xml(xml);
        }
    }
    
}
