package com.example.intercapapp.GCMJAVA;

import java.io.IOException;

import com.example.mysqltest.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MsgActivity extends Activity implements OnClickListener {

    Button btnRegId;
    EditText etRegId;
	GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "872397807314";
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        
        btnRegId = (Button) findViewById(R.id.btnGetRegId);
        etRegId = (EditText) findViewById(R.id.etRegId);

        btnRegId.setOnClickListener(this);
    }
    public void getRegId(){
    	new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                   
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }
	@Override
	public void onClick(View v) {
		getRegId();
	}
    
}
