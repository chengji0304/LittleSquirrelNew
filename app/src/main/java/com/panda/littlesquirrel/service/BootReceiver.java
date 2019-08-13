package com.panda.littlesquirrel.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.panda.littlesquirrel.activity.MainActivity;
import com.panda.littlesquirrel.activity.UserSelectActivity;

/**
 * Created by jinjing on 2019/3/22.
 */

public class BootReceiver extends BroadcastReceiver {
    static final String ACTION="android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION)){
           Intent mainActivivtyIntent=new Intent(context, UserSelectActivity.class);
            mainActivivtyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivivtyIntent);
        }
    }
}
