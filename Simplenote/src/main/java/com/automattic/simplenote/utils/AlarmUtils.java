package com.automattic.simplenote.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.automattic.simplenote.ReminderReceiver;

import java.util.Calendar;

/**
 * Created by jegumi on 25/08/16.
 */
public class AlarmUtils {

    private static final String TAG = AlarmUtils.class.getName();

    public static final String REMINDER_EXTRA_ID = "reminderId";
    public static final String REMINDER_EXTRA_TITLE = "reminderTitle";
    public static final String REMINDER_EXTRA_CONTENT = "reminderContent";

    public static void createAlarm(Context context, String id, String title, String content, Calendar calendar) {
        Log.i(TAG, "create Alarm with id: " + id + "at : " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE)
                + ":" + calendar.get(Calendar.SECOND) + ":" + calendar.get(Calendar.MILLISECOND) + ":" + calendar.getTimeInMillis());
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(REMINDER_EXTRA_ID, id);
        intent.putExtra(REMINDER_EXTRA_TITLE, title);
        intent.putExtra(REMINDER_EXTRA_CONTENT, content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void removeAlarm(Context context, String id, String title, String content) {
        Log.i(TAG, "remove Alarm with : " + id);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(REMINDER_EXTRA_ID, id);
        intent.putExtra(REMINDER_EXTRA_TITLE, title);
        intent.putExtra(REMINDER_EXTRA_CONTENT, content);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }

    public static void removeAlarm(Context context, String id, String title, String content, boolean isRepeated) {
        NoteUtils.removeNoteReminder(id);
        if (isRepeated) {
            removeAlarm(context, id, title, content);
        }
    }
}