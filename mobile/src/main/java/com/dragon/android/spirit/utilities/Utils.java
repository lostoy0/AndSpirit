package com.dragon.android.spirit.utilities;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by raymondlee on 2018/1/31.
 */

public class Utils {

    public static boolean isRunningService(Context context, String name) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : runningServices) {
            if (TextUtils.equals(info.service.getClassName(), name)) {
                return true;
            }
        }
        return false;
    }

    public static Notification createAnonymousNotification(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return new Notification();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, createChannel(context));
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String createChannel(Context context) {
        String channelId = "com.dragon.android.spirit.service";
        String channelName = "SpiritService";
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.enableLights(false);
        chan.setShowBadge(false);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(chan);
        return channelId;
    }

}
