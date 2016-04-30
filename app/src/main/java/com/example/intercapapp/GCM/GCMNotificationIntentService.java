package com.example.intercapapp.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.example.intercapapp.JSONParser;
import com.example.mysqltest.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class GCMNotificationIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {

				sendNotification(""	+ extras.get(ApplicationConstants.MSG_KEY)); //When Message is received normally from GCM Cloud Server
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
		// AQUI DEBO COLOCAR EL registerInBackground del Main Activity
		refrescoRegistro();


	}

	public void refrescoRegistro(){
		//sss
	}



	private void sendNotification(String greetMsg) {
	        Intent resultIntent = new Intent(this, GreetingActivity.class);
	        resultIntent.putExtra("greetjson", greetMsg);
	        resultIntent.setAction(Intent.ACTION_MAIN);
	        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
					resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("Alerta E-Biz")
	                .setContentText("Nuevas promociones on-line.")
	                .setSmallIcon(R.drawable.carritonotif);
	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;

	        
	        mNotifyBuilder.setDefaults(defaults);
	        // Set the content for Notification}
			JSONParser jsp = new JSONParser();


		try {
			JSONObject obj = new JSONObject(greetMsg);

			mNotifyBuilder.setContentText(obj.getString("greetMsg"));
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
