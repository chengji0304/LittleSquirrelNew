package com.panda.littlesquirrel.utils;

/**
 * Created by jinjing on 2019/7/5.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.panda.littlesquirrel.activity.UserLoginActivity;
import com.panda.littlesquirrel.activity.UserSelectActivity;
import com.panda.littlesquirrel.config.MyApplication;

public class DefaultExceptionHandler  implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultUEH;
    Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        try {

            Intent intent = new Intent(activity, UserSelectActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    MyApplication.getInstance().getBaseContext(), 0, intent, intent.getFlags());

            //Following code will restart your application after 2 seconds
            AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext()
                    .getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    pendingIntent);

            //This will finish your activity manually
            activity.finish();

            //This will stop your application and take out from it.
            System.exit(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
