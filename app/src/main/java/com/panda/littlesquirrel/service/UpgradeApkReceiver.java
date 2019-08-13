package com.panda.littlesquirrel.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.panda.littlesquirrel.activity.UserSelectActivity;

/**
 * Created by jinjing on 2019/7/7.
 */

public class UpgradeApkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
            //Toast.makeText(context,"已升级到新版本",Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(context, UserSelectActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);

        }


    }
}
