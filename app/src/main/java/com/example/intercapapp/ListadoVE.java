package com.example.intercapapp;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.mysqltest.R;

public class ListadoVE extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;
	private ImageView imageView;


	private static final String URL_LISTADO_VE = "http://intercapweb.com.ar/TiendaVirtualv3/rs/ve/lista/";

	// JSON IDS:

	private static final String TAG_POOL = "pool";
	private static final String TAG_NRO = "nroPool";
	private static final String TAG_DESCRIPCION = "descripcion";
	private static final String TAG_PATHIMAGEN = "pathImagenBannerMiniatura";

	// An array of all of our comments
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cuenta que el uso listado_ve.xmlgar de nuestra item_ve
		setContentView(R.layout.listado_ve);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// carga comentarios via AsyncTask
		new LoadComments().execute();
	}

	public void addComment(View v) {
		Intent i = new Intent(ListadoVE.this, AgregarDatos.class);
		startActivity(i);
	}

	/**
	 * Recupera los datos post reciente del servidor.
	 */
	public void updateJSONdata() {

		// Crear una instancia ArrayList para contener todos los datos JSON. vamos a usar un montón de pares clave-valor,
		// en referencia al nombre del elemento json, y el contenido, por ejemplo, el mensaje de que la etiqueta,
		// y "soy impresionante" como el contenido ..

		mCommentList = new ArrayList<HashMap<String, String>>();

		// Encendemos J Parser
		JSONParser jParser = new JSONParser();
		// Feed the beast our comments url, and it spits us
		// back a JSON object. Boo-yeah Jerome.
		JSONObject json = jParser.getJSONFromUrl(URL_LISTADO_VE);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POOL);

			// bucle a través de todos los mensajes de acuerdo con el objeto JSON devuelto
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// gets the content of each tag
				String nroPool = c.getString(TAG_NRO);
				String descripcion = c.getString(TAG_DESCRIPCION);
				String pathImagen = c.getString(TAG_PATHIMAGEN);


				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_NRO, nroPool);
				map.put(TAG_DESCRIPCION, descripcion);
				map.put(TAG_PATHIMAGEN, pathImagen);

				// adding HashList to ArrayList
				mCommentList.add(map);

				// annndddd, our JSON data is up to date same with our array
				// list
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserts the parsed data into the listview.
	 */
	private void updateList() {
		// // Para un ListActivity necesitamos configurar el adaptador de la lista, y con el fin de hacer eso,
		// tenemos que crear un ListAdapter. Este SimpleAdapter, utilizará nuestra actualizada Hashmapped ArrayList,
		// utilice nuestra plantilla xml item_ve para cada elemento de la lista, y coloque la información apropiada
		// de la lista para la Identificación del GUI correcta. El orden es importante aquí.
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.item_ve, new String[] { TAG_NRO, TAG_DESCRIPCION,
						TAG_PATHIMAGEN }, new int[] { R.id.nro_ve, R.id.descripcion,
						R.id.username });

		// I shouldn't have to comment on this one:
		setListAdapter(adapter);

		// Opcional: cuando el usuario hace clic en un elemento
		// de la lista que podríamos hacer algo. Sin embargo, vamos a optar por no hacer nada ...
		ListView lv = getListView();	
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView nrove = (TextView)findViewById(R.id.nro_ve);
				TextView descrip = (TextView)findViewById(R.id.descripcion);
				Intent ReservaProducto = new Intent(getApplicationContext(),FormReservaVE.class);

				//Toast.makeText(ListadoVE.this, titulo.getText().toString() , Toast.LENGTH_SHORT).show();
				ReservaProducto.putExtra("in_nrove",nrove.getText().toString());
				ReservaProducto.putExtra("in_descrip", descrip.getText().toString());
				startActivity(ReservaProducto);



			}
		});
	}

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListadoVE.this);
			pDialog.setMessage("Cargando comentarios...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			updateList();
		}
	}


}
