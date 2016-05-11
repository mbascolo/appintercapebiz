package com.example.intercapapp.GCM;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysqltest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;

public class MainActivity extends Activity {
	ProgressDialog prgDialog;
	RequestParams params = new RequestParams();
	GoogleCloudMessaging gcmObj;
	Context applicationContext;
	String regId = "",imeiId;
	TelephonyManager manager;

	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	AsyncTask<Void, Void, String> createRegIdTask;

	public static final String REG_ID = "regId";
	public static final String EMAIL_ID = "eMailId";
	public static final String SITIO_ID = "sitioId";
	EditText emailET, sitioCli;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Obtengo el IMEI
		manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		imeiId = manager.getDeviceId();

		//Guardamos el Imei en las preferencias de usuario
		SharedPreferences pref = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("imei", imeiId);

		applicationContext = getApplicationContext();
		emailET = (EditText) findViewById(R.id.email);
		sitioCli = (EditText) findViewById(R.id.editSitio);


		prgDialog = new ProgressDialog(this);
		// Set Progress Dialog Text
		prgDialog.setMessage("Aguarde por favor...");
		// Set Cancelable as False
		prgDialog.setCancelable(false);

		SharedPreferences prefs = getSharedPreferences("UserDetails",
				Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");

		if (!TextUtils.isEmpty(registrationId)) {
			Intent i = new Intent(applicationContext, GreetingActivity.class);
			i.putExtra("regId", registrationId);
			startActivity(i);
			finish();
		}

	}

	// When Register Me button is clicked
	public void RegisterUser(View view) {
		String emailID = emailET.getText().toString();
		String sitioID = sitioCli.getText().toString();

		if (!emailID.isEmpty() && Utility.validate(emailID)) {
			// Check if Google Play Service is installed in Device
			// Play services is needed to handle GCM stuffs
			if (checkPlayServices()) {

				// Register Device in GCM Server
				registerInBackground(emailID,sitioID);
			}
		}
		// When Email is invalid
		else {
			Toast.makeText(applicationContext, "Por favor inserte un email válido",
					Toast.LENGTH_LONG).show();
		}
	}

	// AsyncTask to register Device in GCM Server
	public void registerInBackground(final String emailID, final String sitioID) {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging.getInstance(applicationContext);
					}
					regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
					msg = "Registration ID :" + regId;

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				if (!regId.isEmpty()) {
					storeRegIdinSharedPref(applicationContext, regId, emailID, imeiId, sitioID);
					Toast.makeText(
							applicationContext,
							"Registrado con Exito servidor de GCM.\n\n"
									+ msg, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							applicationContext,
							"Reg ID Creación Fallo.\n\nO bien no se ha habilitado Internet o servidor de GCM es ocupado ahora. Asegúrese de Internet habilitado y registrate de nuevo después de un tiempo."
									+ msg, Toast.LENGTH_LONG).show();
				}
			}
		}.execute(null, null, null);
	}

	// Store RegId and Email entered by User in SharedPref
	private void storeRegIdinSharedPref(Context context, String regId,
										String emailID, String imeiId, String sitioID) {
		SharedPreferences prefs = getSharedPreferences("UserDetails",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putString(EMAIL_ID, emailID);
		editor.putString(SITIO_ID, sitioID);
		editor.commit();
		storeRegIdinServer(regId, emailID, imeiId, sitioID);

	}

	// Share RegID and Email ID with GCM Server Application (Php)
	private void storeRegIdinServer(String regId2, String emailID, final String imeiId, final String sitioId) {
		prgDialog.show();
		params.put("emailId", emailID);
		params.put("regId", regId);
		params.put("imeiId",imeiId);
		params.put("sitioId",sitioId);
		System.out.println("Email id = " + emailID + " Reg Id = " + regId + "Imei Usuario:" + imeiId + " Sitio Cliente:" + sitioId);
		// Make RESTful webservice call using AsyncHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(ApplicationConstants.APP_SERVER_URL, params,
				new AsyncHttpResponseHandler() {
					// When the response returned by REST has Http
					// response code '200'
					@Override
					public void onSuccess(String response) {
						// Hide Progress Dialog
						prgDialog.hide();
						if (prgDialog != null) {
							prgDialog.dismiss();
						}
						Toast.makeText(applicationContext,
								"Reg Id compartido correctamente con la aplicación web ",
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(applicationContext,
								GreetingActivity.class);
						i.putExtra("regId", regId);
						i.putExtra("imeiId", imeiId);
						i.putExtra("sitioId",sitioId);
						startActivity(i);
						finish();
					}

					// When the response returned by REST has Http
					// response code other than '200' such as '404',
					// '500' or '403' etc
					@Override
					public void onFailure(int statusCode, Throwable error,
							String content) {
						// Hide Progress Dialog
						prgDialog.hide();
						if (prgDialog != null) {
							prgDialog.dismiss();
						}
						// When Http response code is '404'
						if (statusCode == 404) {
							Toast.makeText(applicationContext,
									"Requested resource not found",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code is '500'
						else if (statusCode == 500) {
							Toast.makeText(applicationContext,
									"Something went wrong at server end",
									Toast.LENGTH_LONG).show();
						}
						// When Http response code other than 404, 500
						else {
							Toast.makeText(
									applicationContext,
									"Unexpected Error occcured! [Most common Error: Device might "
											+ "not be connected to Internet or remote server is not up and running], check for other errors as well",
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	// Check if Google Playservices is installed in Device or not
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// When Play services not found in device
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				// Show Error dialog to install Play services
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Toast.makeText(
						applicationContext,
						"This device doesn't support Play services, App will not work normally",
						Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		} else {
			Toast.makeText(
					applicationContext,
					"This device supports Play services, App will work normally",
					Toast.LENGTH_LONG).show();
		}
		return true;
	}

	// When Application is resumed, check for Play services support to make sure
	// app will be running normally
	@Override
	protected void onResume() {
		super.onResume();
		checkPlayServices();
	}
}
