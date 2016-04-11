package com.example.intercapapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mysqltest.R;

public class PruebaLVE extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_lve);

        String[] ventaespecial = {"001","002","003"};
        ListAdapter adaptadorVE = new PruebaAdapter(this, ventaespecial);
        ListView veListView = (ListView)findViewById(R.id.ListaPrueba);
        veListView.setAdapter(adaptadorVE);

        /*Al hacer clic en el item */
        veListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String venta = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(PruebaLVE.this,venta,Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}
