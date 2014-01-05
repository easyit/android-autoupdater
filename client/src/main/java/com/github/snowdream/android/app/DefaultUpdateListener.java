package com.github.snowdream.android.app;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.github.snowdream.android.util.Log;

/**
 * Created by snowdream on 1/2/14.
 */
public class DefaultUpdateListener extends AbstractUpdateListener {
    private NotificationManager notificationManager = null;
    private NotificationCompat.Builder notificationBuilder = null;

    @Override
    public void onShowUpdateUI(final UpdateInfo info) {
        if (info == null) {
            return;
        }

        Context context = getContext();
        if (context != null) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("更新提示")
                    .setMessage(getUpdateTips(info))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            informUpdate(info);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            informCancel(info);
                        }
                    })
                    .setNeutralButton("忽略", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            informSkip(info);
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
        //informUpdate(info);
    }

    @Override
    public void onShowNoUpdateUI() {
        Context context = getContext();
        if (context != null) {
            Toast.makeText(context, "没有更新", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShowUpdateProgressUI(DownloadTask task, int progress) {
        Context context = getContext();
        if (context != null && task != null) {
            try {
                PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                if (notificationManager == null) {
                    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                }

                if (notificationBuilder == null) {
                    notificationBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(task.getName())
                            .setContentText(task.getName())
                            .setAutoCancel(true);
                }
                notificationBuilder.setProgress(100, progress, false);
                notificationManager.notify(0, notificationBuilder.build());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log.e("can not get the package info", e);
            }
        }
    }

    @Override
    public void ExitApp() {
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
