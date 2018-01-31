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
import com.dragon.android.spirit.SpiritManager;
import com.dragon.android.spirit.utilities.Utils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public class SpiritLocalService extends Service {

    private SpiritBinder mBinder;

    class SpiritBinder extends ISpiritAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double
                aDouble, String aString) throws RemoteException {

        }
    }

    ServiceConnection serviceConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new SpiritBinder();
        serviceConnection = new ServiceConnection();
        //使Service变成前台服务
//        startForeground(20, Utils.createAnonymousNotification(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startService(new Intent(this, InnnerService.class));
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, SpiritRemoteService.class), serviceConnection,
                BIND_AUTO_CREATE);

        executeTasks();

        return super.onStartCommand(intent, flags, startId);
    }

    class ServiceConnection implements android.content.ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务连接后回调
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.e("子进程可能被干掉了，拉活");
            //连接中断后回调
            startService(new Intent(SpiritLocalService.this, SpiritRemoteService.class));
            bindService(new Intent(SpiritLocalService.this, SpiritRemoteService.class), serviceConnection,
                    BIND_AUTO_CREATE);
            if(!Utils.isRunningService(SpiritLocalService.this, SpiritJobService.class.getName())) {
                SpiritJobService.StartJob(SpiritLocalService.this);
            }
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

    private void executeTasks() {
        SpiritManager.init(this);
    }
}
