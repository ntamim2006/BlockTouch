package com.nt.blocktouch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);


            Intent widgetIntent = new Intent(context, FloatingViewService.class);
            widgetIntent.putExtra("your_key_here", "dsa");
            widgetIntent.setAction("android.intent.action.SEND");
            PendingIntent pendingIntent = PendingIntent.getService(context,
                    0, widgetIntent, 0);

            remoteViews.setOnClickPendingIntent(R.id.blockTouchButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

   
}