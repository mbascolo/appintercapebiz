package com.example.intercapapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mysqltest.R;

public class Login extends AppCompatActivity {

	Activity context;
	ProgressDialog pd;

	TextView nombre_usuario,pass_usuario;
	private CheckBox check_recordar;
	//String ESTADO_LOGIN = "A" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		/*if (ESTADO_LOGIN == "A"){
			check_recordar = (CheckBox)findViewById(R.id.checkBoxRecordar);*/
			nombre_usuario = (TextView)findViewById(R.id.username);
			pass_usuario = (TextView)findViewById(R.id.password);
			context=this;
			//CargarPreferencias();

		//} else if (ESTADO_LOGIN == "B"){

			//Intent principal = new Intent(Login.this,HomeActivity.class);
			//finish();
			//startActivity(principal);
		//}


	}


	public void CargarPreferencias(){
		//Guarda las preferencias en un xml llamado PreferenciasUsuario
		SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
		check_recordar.setChecked(misPreferencias.getBoolean("checked", false));
		nombre_usuario.setText(misPreferencias.getString("nombre",""));
		pass_usuario.setText(misPreferencias.getString("pass", ""));


	}

	public void GuardarPreferencias(){

		SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = misPreferencias.edit();
		boolean valor = check_recordar.isChecked();
		String nombre = nombre_usuario.getText().toString();
		String pass = pass_usuario.getText().toString();
		editor.putBoolean("checked", valor);
		editor.putString("nombre", nombre);
		editor.putString("pass", pass);
		editor.commit();
	}


	//Botón Ingresar
	public void OnClick(View v){
		BackTask bt=new BackTask();
		bt.execute("http://www.intercapweb.com.ar/TiendaVirtualv3/rs/usuario/login/" + nombre_usuario.getText() + "/" + pass_usuario.getText());
	}

	public void OnCheck (View v){
		GuardarPreferencias();
	}


	//background process to download the file from internet
	private class BackTask extends AsyncTask<String,Integer,Void>{
		String text="";
		protected void onPreExecute(){
			super.onPreExecute();
			//display progress dialog
			pd = new ProgressDialog(context);
			pd.setTitle("Comprobando datos");
			pd.setMessage("Espere por favor.");
			pd.setCancelable(true);
			pd.setIndeterminate(false);
			pd.show();

		}



		protected Void doInBackground(String...params){
			URL url;
			try {
				//create url object to point to the file location on internet
				url = new URL(params[0]);
				//make a request to server
				HttpURLConnection con=(HttpURLConnection)url.openConnection();
				//get InputStream instance
				InputStream is=con.getInputStream();
				//create BufferedReader object
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				String line;
				//read content of the file line by line
				while((line=br.readLine())!=null){
					text+=line;

				}

				br.close();

			}catch (Exception e) {
				e.printStackTrace();
				//close dialog if error occurs
				if(pd!=null) pd.dismiss();
			}

			return null;

		}


		protected void onPostExecute(Void result){
			//close dialog
			if(pd!=null)
				pd.dismiss();


			//display read text in TextVeiw
			//resultado.setText(text);

			if(text.equals("Autorizado")){

				//GuardarPreferencias();
				Intent principal = new Intent(Login.this,HomeActivity.class);
				finish();
				startActivity(principal);


			} else if (text.equals("Password incorrecto")) {

				Toast.makeText(Login.this,"Datos incorrectos",Toast.LENGTH_SHORT).show();

			} else if (text.equals("")) {

				Toast.makeText(Login.this,"Ingrese su usuario y contraseNa por favor.",Toast.LENGTH_SHORT).show();
			}

		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_form_reserva, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
