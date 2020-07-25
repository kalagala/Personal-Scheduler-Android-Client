package com.kalagala.personalschechuler.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kalagala.personalschechuler.services.StartupService;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
            Intent startupService = new Intent(context, StartupService.class);
            context.startForegroundService(startupService);
    }
}
