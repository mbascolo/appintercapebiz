package com.example.intercapapp.GCMJAVA;

import java.io.IOException;

import com.example.intercapapp.GCM.GreetingActivity;
import com.example.intercapapp.GCM.Utility;
import com.example.mysqltest.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MsgActivity extends Activity implements OnClickListener {
    ProgressDialog prgDialog;
    Button btnRegId, RegisterUser;
    EditText etRegId, emailET;
    RequestParams params = new RequestParams();
	GoogleCloudMessaging gcm;
    String regId;
    String PROJECT_NUMBER = "872397807314";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Context applicationContext;
    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

        applicationContext = getApplicationContext();
        btnRegId = (Button) findViewById(R.id.btnGetRegId);
        RegisterUser = (Button) findViewById(R.id.RegisterUser);
        etRegId = (EditText) findViewById(R.id.etRegId);
        emailET = (EditText) findViewById(R.id.emailTxt);
        btnRegId.setOnClickListener(this);

        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Aguarde por favor...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");

        //if (!TextUtils.isEmpty(registrationId)) {
        //    Intent i = new Intent(applicationContext, GreetingActivity.class);
        //    i.putExtra("regId", registrationId);
        //    startActivity(i);
        //    finish();
        //}
    }

    //Click en botón btnGetRegId
    //@Override
    //public void onClick(View v) {
    //    getRegId();
    //}




    // CLIC EN REGISTRARME
    public void onClick(View v) {
        String emailID = emailET.getText().toString();

        if (!TextUtils.isEmpty(emailID) && ValidarCorreo.validate(emailID)) {
            // Check if Google Play Service is installed in Device
            // Play services is needed to handle GCM stuffs
            if (checkPlayServices()) {

                // Register Device in GCM Server
                getRegId(emailID);
            }
        }
        // When Email is invalid
        else {
            Toast.makeText(applicationContext, "Por favor inserte un email válido",
                    Toast.LENGTH_LONG).show();
        }
    }




    public void getRegId(final String emailID){
    	new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regId;
                    Log.i("GCM",  msg);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            //@Override
            //protected void onPostExecute(String msg) {
            //    etRegId.setText(msg + "\n");
            //}

            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    storeRegIdinSharedPref(applicationContext, regId, emailID);
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
                                        String emailID) {
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putString(EMAIL_ID, emailID);
        editor.commit();
        storeRegIdinServer(regId, emailID);

    }

    // Share RegID and Email ID with GCM Server Application (Php)
    private void storeRegIdinServer(String regId2, String emailID) {
        prgDialog.show();
        params.put("emailId", emailID);
        params.put("regId", regId);
        System.out.println("Email id = " + emailID + " Reg Id = " + regId);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constantes.APP_SERVER_URL, params,
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
                        //Intent i = new Intent(applicationContext,
                        //        GreetingActivity.class);
                        //i.putExtra("regId", regId);
                        //startActivity(i);
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

    //Check if Google Playservices is installed in Device or not
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




    //@Override
	//public void onClick(View v) {
		//getRegId();
	//}
    
}
