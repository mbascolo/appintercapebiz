package com.example.intercapapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.intercapapp.Email.SendMailActivity;
import com.example.mysqltest.R;

public class HomeActivity extends AppCompatActivity {

    TelephonyManager manager;
    TextView mensajeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mensajeManager = (TextView)findViewById(R.id.textViewManager);
        manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder builder = new StringBuilder();
        builder.append("Imei:").append(manager.getDeviceId()).append("\n");
        builder.append("Operador:").append(manager.getNetworkOperatorName());
        mensajeManager.setText(builder.toString());


        //Instancio los botones
        Button btn_ventas = (Button)findViewById(R.id.btn_ventas);
        Button btn_gcm = (Button)findViewById(R.id.btn_gcm);
        Button btn_testgson = (Button)findViewById(R.id.btn_testgson);
        FloatingActionButton fab_send = (FloatingActionButton)findViewById(R.id.fab_sendemail);
        //Button btn_catalogo = (Button)findViewById(R.id.btn_catalogo);
        //Button btn_ctacte = (Button)findViewById(R.id.btn_ctacte);
        //Button btn_enviar_correo = (Button)findViewById(R.id.btn_enviar_correo);
        //Button btn_ir_leer_url = (Button)findViewById(R.id.btn_ir_leer);

        btn_ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ListadoVE.class);
                startActivity(intent);
            }
        });

        btn_gcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,GCMActivity.class);
                startActivity(intent);
            }
        });

        btn_testgson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,ListadoVEGson.class);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sendemail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SendMailActivity.class);
                startActivity(intent);
            }
        });






        // btn_catalogo.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         Intent intent = new Intent(HomeActivity.this,CatalogoActivity.class);
        //         startActivity(intent);
        //     }
        //});

        // btn_ctacte.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent(HomeActivity.this,CtacteActivity.class);
        //        startActivity(intent);
        //    }
        // });

        // btn_enviar_correo.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         Intent intent = new Intent(HomeActivity.this,FormReservaActivity.class);
        //         startActivity(intent);
        //     }
        // });

        // btn_ir_leer_url.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         Intent intent = new Intent(HomeActivity.this,LeerURL.class);
        //         startActivity(intent);
        //     }
        // });



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

