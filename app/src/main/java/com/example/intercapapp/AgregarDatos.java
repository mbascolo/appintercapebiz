package com.example.intercapapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mysqltest.R;

public class AgregarDatos extends Activity implements OnClickListener{

    public static final int NOTIFICACION_ID=1;
	
	private EditText title, message;
	private Button  mSubmit;
	
	 // Progress Dialog
    private ProgressDialog pDialog;

    //Defino los íconos de la notificación en la barra de notificación
    int icono_v = R.drawable.carrito;
    int icono_r = R.drawable.carritopeq;


 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //php add a comment script
    
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String POST_COMMENT_URL = "http://xxx.xxx.x.x:1234/webservice/addcomment.php";
    
    //testing on Emulator:
    private static final String POST_COMMENT_URL = "http://www.beansoft.com.ar/webservusers/addcomment.php";
    
  //testing from a real server:
    //private static final String POST_COMMENT_URL = "http://www.mybringback.com/webservice/addcomment.php";
    
    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agregar_datos);
		
		title = (EditText)findViewById(R.id.nro_ve);
		message = (EditText)findViewById(R.id.descripcion);
		
		mSubmit = (Button)findViewById(R.id.submit);
		mSubmit.setOnClickListener(this);

	}

    @Override
	public void onClick(View v) {
				new PostComment().execute();

        String post_title = title.getText().toString();
        String post_message = message.getText().toString();

        //Construcción del intent implícito
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.intercap.com.ar/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        //Construcción de la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.nubeblanca);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.carritonotif));
        builder.setContentTitle(post_title);
        builder.setContentInfo(post_message);
        builder.setSubText("Clic para ingresar");

        ///... continuar con video https://www.youtube.com/watch?v=QFmv6Up0Izs min 5:30

        //Enviamos la notificación
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICACION_ID,builder.build());

	}


	class PostComment extends AsyncTask<String, String, String> {
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AgregarDatos.this);
            pDialog.setMessage("Posting Comment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String post_title = title.getText().toString();
            String post_message = message.getText().toString();
            
            //We need to change this:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AgregarDatos.this);
            String post_username = sp.getString("username", "anon");
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("title", post_title));
                params.add(new BasicNameValuePair("message", post_message));
 
                Log.d("request!", "starting");
                
                //Posteando usando los datos del script
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_COMMENT_URL, "POST", params);
 
                // full json response
                Log.d("Post Comment attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comentario agregado!", json.toString());
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Fallo en comentario!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(AgregarDatos.this, file_url, Toast.LENGTH_LONG).show();

            }
 
        }
		
	}
		 

}
