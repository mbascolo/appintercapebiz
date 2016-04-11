package com.example.intercapapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysqltest.R;

/**
 * Created by Matias on 11/04/2016.
 */
class PruebaAdapter extends ArrayAdapter<String> {

    public PruebaAdapter(Context context, String[] ventaespecial) {
        super(context, R.layout.activity_prueba_lve, ventaespecial);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater listadoInflater = LayoutInflater.from(getContext());
        View customView = listadoInflater.inflate(R.layout.item_ve, parent, false);

        String singleVeItem = getItem(position);
        TextView nroVE = (TextView)customView.findViewById(R.id.nro_ve);
        //TextView descVE = (TextView)customView.findViewById(R.id.descripcion);
        ImageView imgVE = (ImageView)customView.findViewById(R.id.boxImagen);

        nroVE.setText(singleVeItem);
        //descVE.setText(singleVeItem);
        imgVE.setImageResource(R.drawable.carrito);

        return customView;

    }
}
