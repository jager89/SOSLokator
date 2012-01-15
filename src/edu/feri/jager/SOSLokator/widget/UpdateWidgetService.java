package edu.feri.jager.SOSLokator.widget;

import java.util.List;

import edu.feri.jager.SOSLokator.MainApplication;
import edu.feri.jager.SOSLokator.R;
import edu.feri.jager.SOSLokator.SOSSearchActivity;
import edu.feri.jager.SOSLokator.structures.MyLocation;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	public static final String FORWARD = new String("FORWARD");
	public static final String BACKWARD = new String("BACKWARD");
	public static final String OPEN = new String("BACKWARD");

	MainApplication app;
	List<MyLocation> list = null;
	@Override
	public void onStart(Intent intent, int startId) {
		if(app == null) {
			app = (MainApplication) getApplicationContext();
		}
		app.fillLocationsFromDB();			
		list = app.getListLocations();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
				.getApplicationContext());

		int[] appWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.widget_layout);
		
		if (appWidgetIds.length > 0) {
			for (int widgetId : appWidgetIds) {				
				setRemoteViews(remoteViews, intent);
				appWidgetManager.updateAppWidget(widgetId, remoteViews);
			}
		}
		
		super.onStart(intent, startId);
	}

	private void setRemoteViews(RemoteViews remoteViews, Intent intent) {
		if(list.isEmpty()) {
			remoteViews.setTextViewText(R.id.widget_textview_phone_number, "NI PODATKOV...");		
		}
		else {
			int pos = app.getWidgetCounter();

			if(intent.getAction().equalsIgnoreCase(OPEN)) {
				
				Intent i = new Intent(this, SOSSearchActivity.class);  
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				i.putExtra("dbData", new String[] {list.get(pos).getLongitude(), list.get(pos).getLatitude()});
				startActivity(i); 
			} 
			else {
				if(intent.getAction().equalsIgnoreCase(FORWARD)) {
					if(pos < list.size() - 1) {
						if(pos < 0) {
							pos = 0;
						} else {
							pos++;
						}
					} 	
				} else if(intent.getAction().equalsIgnoreCase(BACKWARD)) {
					if(pos < 0) {
						pos = 0;
					}
					else if(pos > 0){
						pos--;
					}
				}
				remoteViews.setTextViewText(R.id.widget_textview_timestamp,  list.get(pos).getTimestamp());
				remoteViews.setTextViewText(R.id.widget_textview_dislplay_name,  list.get(pos).getDisplayName());
				remoteViews.setTextViewText(R.id.widget_textview_phone_number,  list.get(pos).getPhoneNumber());
				app.setWidgetCounter(pos);
			}
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
