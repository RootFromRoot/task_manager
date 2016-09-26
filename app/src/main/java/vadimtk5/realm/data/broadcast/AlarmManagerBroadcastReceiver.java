package vadimtk5.realm.data.broadcast;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vadimtk5.realm.R;
import vadimtk5.realm.ui.activity.TaskCreateActivity;
import vadimtk5.realm.utils.RequestCodes;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();

    final public static String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()-> new broadcast received");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
//Осуществляем блокировку
        wl.acquire();

//Здесь можно делать обработку.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null) {
            //проверяем параметр ONE_TIME, если это одиночный будильник,
            //выводим соответствующее сообщение.
            msgStr.append("Одноразовый будильник: ");
            Intent notificationIntent = new Intent(context, TaskCreateActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentIntent(contentIntent)
                    .setTicker("DEADLINE!")
                    .setSmallIcon(R.drawable.ic_alarm_white_24dp)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("DEADLINE")
                    .setContentText("Time ended")
                    .addAction(R.drawable.ic_restore_white_24dp, "Restore",contentIntent)
                    .addAction(R.drawable.ic_check_circle_white_24dp,"Ok",contentIntent)
                    .build();
            notification.flags = notification.flags | Notification.FLAG_INSISTENT|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(RequestCodes.NOTIFY_ID, notification);
        }

        Format formatter = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        msgStr.append(formatter.format(new Date()));
//alarm is started
        //Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

//Разблокируем поток.
        wl.release();
    }

    public static void setAlarm(Context context, long alarmTime) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);//Задаем параметр интента
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
//Устанавливаем интервал срабатывания в 5 секунд.
        am.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 1000 * 5, pi);

    }

    public static void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);//Отменяем будильник, связанный с интентом данного класса
    }

    public static void setOnetimeTimer(Context context, long alarmTime) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);//Задаем параметр интента
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, alarmTime, pi);
    }
}
