package com.dragon.android.spirit.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.dragon.android.spirit.ISpiritAidlInterface;
import com.dragon.android.spirit.utilities.Utils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/1/29 0029.
 */
public class SpiritRemoteService extends Service {

    class SpiritBinder extends ISpiritAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double
                aDouble, String aString) throws RemoteException {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    SpiritBinder mBinder;
    ServiceConnection serviceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new SpiritBinder();
        serviceConnection = new ServiceConnection();
        //使Service变成前台服务
//        startForeground(20, Utils.createAnonymousNotification(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(new Intent(this, SpiritLocalService.InnnerService.class));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, SpiritLocalService.class), serviceConnection,
                BIND_AUTO_CREATE);
        return super.onStartCommand(intent, flags, startId);
    }

    class ServiceConnection implements android.content.ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接后回调
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.e("主进程可能被干掉了，拉活");
            //连接中断后回调
            startService(new Intent(SpiritRemoteService.this, SpiritLocalService.class));
            bindService(new Intent(SpiritRemoteService.this, SpiritLocalService.class), serviceConnection,
                    BIND_AUTO_CREATE);

        }
    }

    public static class InnnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
//            startForeground(20, Utils.createAnonymousNotification(this));
            stopSelf();
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
