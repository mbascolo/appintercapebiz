package com.example.intercapapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devazt.networking.HttpClient;
import com.devazt.networking.OnHttpRequestComplete;
import com.devazt.networking.Response;
import com.example.mysqltest.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PruebaLVE extends AppCompatActivity {

    ListView listaPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_lve);

        listaPrueba = (ListView)findViewById(R.id.ListaPrueba);

         /*HttpCliente crea un cliente que nos permite realizar peticiones web */
        /*Recibe como parámetro un OnHttpRequestComplete() */
        HttpClient client = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                /*Consultamos si se realizó una petición */
                if(status.isSuccess()){
                    Gson gson = new GsonBuilder().create();

                    try {
                        JSONObject jsono = new JSONObject(status.getResult());
                        JSONArray jsonarray = jsono.getJSONArray("pool"); /*Devuelve un JSONArray */
                        ArrayList<ListadoVEBean> ventasEspeciales = new ArrayList<ListadoVEBean>(); /*Nos permite seleccionar a través de un index */
                        for (int i = 0; i < jsonarray.length(); i++){
                            String ventaespecial = jsonarray.getString(i);
                            ListadoVEBean ve = gson.fromJson(ventaespecial,ListadoVEBean.class);
                            ventasEspeciales.add(ve);
                            TextView t = new TextView(getBaseContext());
                            TextView e = new TextView(getBaseContext());
                            TextView s = new TextView(getBaseContext());
                            t.setText(ve.getNroPool()); /*Obtenemos el nro del pool */
                            e.setText(ve.getDescripcion());
                            s.setText(ve.getPathImagenBannerMiniatura());

                            //ListAdapter adaptadorVE = new PruebaAdapter(this, ventaespecial);
                            //ListView veListView = (ListView)findViewById(R.id.ListaPrueba);
                            //veListView.setAdapter(adaptadorVE);

                            listaPrueba.addView(t);
                            listaPrueba.addView(e);
                            listaPrueba.addView(s);

                        }

                    } catch (Exception e){
                        e.printStackTrace();

                    }

                    Toast.makeText(PruebaLVE.this, status.getResult(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        /*Ejecuta la URL */
        client.excecute("http://intercapweb.com.ar/TiendaVirtualv3/rs/ve/lista/");




        /*String[] ventaespecial = {"001","002","003"};
        ListAdapter adaptadorVE = new PruebaAdapter(this, ventaespecial);
        ListView veListView = (ListView)findViewById(R.id.ListaPrueba);
        veListView.setAdapter(adaptadorVE);

        /*Al hacer clic en el item */
        /*veListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String venta = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(PruebaLVE.this,venta,Toast.LENGTH_SHORT).show();
                    }
                }
        ); */

    }
}
