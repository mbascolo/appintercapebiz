package com.example.mysqltest;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ReadComments extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// php read comments script

	// localhost :
	// testing on your device
	// put your local ip instead, on windows, run CMD > ipconfig
	// or in mac's terminal type ifconfig and look for the ip under en0 or en1
	// private static final String READ_COMMENTS_URL =
	// "http://xxx.xxx.x.x:1234/webservice/comments.php";

	// testing on Emulator:
	private static final String READ_COMMENTS_URL = "http://www.beansoft.com.ar/webservusers/comments.php";

	// testing from a real server:
	// private static final String READ_COMMENTS_URL =
	// "http://www.mybringback.com/webservice/comments.php";

	// JSON IDS:
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_TITLE = "title";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_POST_ID = "post_id";
	private static final String TAG_USERNAME = "username";
	private static final String TAG_MESSAGE = "message";
	// Que es importante tener en cuenta que el mensaje es tanto en la rama principal de nuestro árbol JSON que muestra
	// un "Post Disponible" o un mensaje "No Publicar disponible", y también hay un mensaje para cada entrada individual,
	// que se enumeran en la sección "categoría de mensajes ", que muestra lo que el usuario escribió como su mensaje.

	// An array of all of our comments
	private JSONArray mComments = null;
	// manages all of our comments in a list.
	private ArrayList<HashMap<String, String>> mCommentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// cuenta que el uso read_comments.xml lugar de nuestra single_post.xml
		setContentView(R.layout.read_comments);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// carga comentarios via AsyncTask
		new LoadComments().execute();
	}

	public void addComment(View v) {
		Intent i = new Intent(ReadComments.this, AddComment.class);
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
		JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

		// when parsing JSON stuff, we should probably
		// try to catch any exceptions:
		try {

			// I know I said we would check if "Posts were Avail." (success==1)
			// before we tried to read the individual posts, but I lied...
			// mComments will tell us how many "posts" or comments are
			// available
			mComments = json.getJSONArray(TAG_POSTS);

			// bucle a través de todos los mensajes de acuerdo con el objeto JSON devuelto
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// gets the content of each tag
				String title = c.getString(TAG_TITLE);
				String content = c.getString(TAG_MESSAGE);
				String username = c.getString(TAG_USERNAME);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_TITLE, title);
				map.put(TAG_MESSAGE, content);
				map.put(TAG_USERNAME, username);

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
		// utilice nuestra plantilla xml single_post para cada elemento de la lista, y coloque la información apropiada
		// de la lista para la Identificación del GUI correcta. El orden es importante aquí.
		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.single_post, new String[] { TAG_TITLE, TAG_MESSAGE,
						TAG_USERNAME }, new int[] { R.id.title, R.id.message,
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

				// Este método se activa si un artículo es hacer clic dentro de nuestra lista.
				// Para nuestro ejemplo no vamos a usar esto, pero es útil saber en aplicaciones de la vida real.

			}
		});
	}

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReadComments.this);
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
