package com.dragon.android.spirit.service;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.dragon.android.spirit.utilities.Utils;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

@SuppressLint("NewApi")
public class SpiritJobService extends JobService {

    public static void StartJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context
                .JOB_SCHEDULER_SERVICE);
//        setPersisted 在设备重启依然执行
        JobInfo.Builder builder = new JobInfo.Builder(10, new ComponentName(context
                .getPackageName(), SpiritJobService.class
                .getName())).setPersisted(true);
        //小于7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // 每隔1s 执行一次 job
            builder.setPeriodic(1000);
        } else {
            //延迟执行任务
            builder.setMinimumLatency(1000);
        }

        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.e("开启job");
        //如果7.0以上 轮训
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StartJob(this);
        }
        boolean isLocalRun = Utils.isRunningService(this, SpiritLocalService.class.getName());
        boolean isRemoteRun = Utils.isRunningService(this, SpiritRemoteService.class.getName());
        if (!isLocalRun || !isRemoteRun) {
            startService(new Intent(this, SpiritLocalService.class));
            startService(new Intent(this, SpiritRemoteService.class));
        }

        executeTasks();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void executeTasks() {
        //获取位置
    }
}
