package com.suffix.fieldforce.networking;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.BillDetailsActivity;
import com.suffix.fieldforce.activity.home.LoginActivity;
import com.suffix.fieldforce.activity.home.MainDashboardActivity;
import com.suffix.fieldforce.activity.task.PreviewTaskActivity;
import com.suffix.fieldforce.preference.FieldForcePreferences;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //private Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/notification_sound.mp3");
    private String NOTIFICATION_CHANNEL_ID = "com.suffix.fieldforce";
    private Class cls;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String id = remoteMessage.getData().get("id");
        String message = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String activityName = remoteMessage.getData().get("activity_name");

        try {
            if(!TextUtils.isEmpty(activityName)) {
                showNotification(id, title, message, activityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String id, String title, String body, String activityName) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = null;

        if(new FieldForcePreferences(getApplicationContext()).getUser() != null) {
            switch (activityName) {
                case "task":
                    notificationIntent = new Intent(getApplicationContext(), PreviewTaskActivity.class);
                    notificationIntent.putExtra("ID",id);
                    break;
                case "bill":
                    notificationIntent = new Intent(getApplicationContext(), BillDetailsActivity.class);
                    notificationIntent.putExtra("ID",id);
                    break;
                default:
                    notificationIntent = new Intent(getApplicationContext(), MainDashboardActivity.class);
                    break;
            }
        }else {
            notificationIntent = new Intent(getApplicationContext(), LoginActivity.class);
        }

        notificationIntent.putExtra("BODY", body);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent resultIntent = PendingIntent.getActivity(getApplicationContext(),  (int) (Math.random() * 100), notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setSmallIcon(R.drawable.message);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setContentInfo("info");
        notificationBuilder.setContentIntent(resultIntent);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "notification",
                notificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(body);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});

            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify((int)System.currentTimeMillis(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NewToken", s);
    }
}
