package com.example.seongmin.terminal;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by seongmin on 10/24/16.
 */

public class ShellIntentService extends IntentService {

    public ShellIntentService() {
        super("ShellIntentService");
    }

    public static void launchService(Context context) {
        if (context == null) return;
        context.startService(new Intent(context, ShellIntentService.class));
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
