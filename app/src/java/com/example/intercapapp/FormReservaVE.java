package com.example.intercapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mysqltest.R;


public class FormReservaVE extends AppCompatActivity {

    TextView titulo_ve, mensaje_ve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_reserva_ve);

        titulo_ve = (TextView)findViewById(R.id.textVtaNro);
        mensaje_ve = (TextView)findViewById(R.id.textVtaTitulo);

        //Recogiendo intent desde MainActivity

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            String datoNro = (String)extras.get("in_nrove");
            String datoTitulo = (String)extras.get("in_descrip");


            titulo_ve.setText(datoNro);
            mensaje_ve.setText(datoTitulo);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form_reserva_ve, menu);
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
