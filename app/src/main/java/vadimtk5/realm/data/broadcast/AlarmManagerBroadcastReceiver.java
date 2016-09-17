package vadimtk5.realm.data.broadcast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
//Осуществляем блокировку
        wl.acquire();

//Здесь можно делать обработку.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null & extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
//проверяем параметр ONE_TIME, если это одиночный будильник,
//выводим соответствующее сообщение.
            msgStr.append("Одноразовый будильник: ");
        }
        Format formatter = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        msgStr.append(formatter.format(new Date()));

        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

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
