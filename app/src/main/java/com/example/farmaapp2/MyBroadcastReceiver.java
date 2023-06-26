package com.example.farmaapp2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Random;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String name = intent.getStringExtra("NAME");
        //Creamos notificación
        NotificationManager notificationManager;

        // crea canal de notificaciones
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "com.example.pharminder_2_0.notify");
        //pendingIntent para abrir la actividad cuando se pulse la notificación
        Intent ii = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, m, ii, PendingIntent.FLAG_IMMUTABLE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.pharminder_logo_circular);
        mBuilder.setContentTitle("Es la hora de tu medicación");
        mBuilder.setContentText( name);

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal de Pharminder",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        notificationManager.notify(m, mBuilder.build());

        // Guardar el identificador único de la notificación en SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("NotificationIDs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("notification_id", m);
        editor.apply();

    }

    public int cancelNotifications(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("NotificationIDs", Context.MODE_PRIVATE);
        int notificationId = sharedPreferences.getInt("notification_id", -1);

        if (notificationId != -1) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);

            // Eliminar el identificador almacenado
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("notification_id");
            editor.apply();

            return -1;
        }else{
            return 1;
        }

    }

}