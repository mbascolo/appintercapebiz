package com.example.intercapapp.GCMJAVA;

import com.example.intercapapp.ListadoVE;
import com.example.mysqltest.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class GcmMessageHandler extends IntentService {
	Context mContext;
     String mes, tit;
     private Handler handler;
	public GcmMessageHandler() {
		super("GcmMessageHandler");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new Handler();
	}
	@Override
	protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

		tit = extras.getString("title");
       	mes = extras.getString("message");
		EnvioNotificacion();
       	showToast();
       	Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));



        GcmBroadcastReceiver.completeWakefulIntent(intent);

	}
	
	public void showToast(){
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
			}
		});

	}

	public void EnvioNotificacion(){

		Intent resultIntent = new Intent(this, ListadoVE.class);
		resultIntent.setAction(Intent.ACTION_MAIN);
		resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent =
				PendingIntent.getActivity(
						this,
						0,
						resultIntent,
						PendingIntent.FLAG_UPDATE_CURRENT
				);


		int numMessages;
		NotificationCompat.Builder mNotifyBuilder;
		NotificationManager mNotificationManager;

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Sets an ID for the notification, so it can be updated
		int notifyID = 1;
		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle(tit)
				.setContentText("You've received new messages.")
				.setSmallIcon(R.drawable.carritonotif);
		numMessages = 0;
		// Start of a loop that processes data and then notifies the user

		mNotifyBuilder.setContentText(mes)
				.setNumber(++numMessages);
		// Set pending intent
		mNotifyBuilder.setContentIntent(resultPendingIntent);
		// Because the ID remains unchanged, the existing notification is
		// updated.
		mNotificationManager.notify(
				notifyID,
				mNotifyBuilder.build());


	}

	
	

}
