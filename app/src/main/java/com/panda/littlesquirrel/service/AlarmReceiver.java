package com.panda.littlesquirrel.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.panda.littlesquirrel.service.PollDevStatusService;

/**
 * Created by diaohaixiang on 2019/8/12.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PollDevStatusService.class);
        context.startService(i);
    }
}