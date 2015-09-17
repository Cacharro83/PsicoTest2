package com.endel.psicotest.baseDatos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.endel.psicotest.Colegio;
import com.endel.psicotest.Item;
import com.endel.psicotest.Logica;
import com.endel.psicotest.RespuestaRellenada;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	// Context.getFilesDir().getPath()
	private static String DB_PATH = "/data/data/com.example.psicotestv1/databases/";
	//private static String DB_NAME = "psico";

	private SQLiteDatabase myDataBase;
	private final Context myContext;

	public final String TABLA_CENTROS = "centros";
	public final String ID_CENTRO = "IdCentro";
	public final String NOMBRE_CENTRO = "Nombre";

	public final String TABLA_RESPUESTAUSUARIO = "respuestasusuario";
	public final String RESPUESTAUSUARIODELAY = "respuestausuariodelay";
	public final String PAISES = "paisestexto";

	public final String TABLA_PREGUNTAS = "itemtexto";
	public final String ID_ITEM = "IdItem";
	public final String TEXTO = "Texto";

	public final String TABLA_RESPUESTAUSUARIODELAY = "respuestausuariodelay";
	public final String ID = "id";
	public final String INDEXLOTE = "IndexLote";
	public final String INDEXGLOBAL = "IndexGlobal";
	public final String MIN = "Min";
	public final String MAX = "Max";
	public final String ELECCION = "Eleccion";

	public final String TABLA_RESPUESTATEXTO = "respuestatexto";
	public final String ID_RESPUESTA = "IdRespuesta";
	public final String FOTO = "Foto";

	public final String TABLA_ERRORES = "errortexto";
	public final String ID_ERROR = "IdError";

	public final String TABLA_AVISOS = "avisostexto";
	public final String ID_AVISO = "IdAviso";

	public final String TABLA_PAISES = "paisestexto";
	public final String ID_PAIS = "IdPais";

	public final String TABLA_STROOPTEXTO = "strooptexto";
	public final String ID_STROOP = "IdStroop";

	public final String TABLA_DELAYTEXTO = "delaytexto";
	public final String ID_DELAY = "IdDelay";

	public final String TABLA_IDIOMA = "idioma";
	public final String ID_IDIOMA = "IdIdiomaActual";
	public final String NOMBRE = "Nombre";

	public DataBaseHelper(Context context) {
		super(context, DBMain.DB_NAME, null, DBMain.DB_VERSION);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 *
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DBMain.DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DBMain.DB_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DBMain.DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DBMain.DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();

		try{
			super.close();
		}
		catch(SQLiteException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public String queryItem(int id){

		String selectQuery = "SELECT IdItem FROM items WHERE IdItem = " + String.valueOf(id);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery(selectQuery, null);

		cursor.moveToFirst();

		String aux = cursor.getString(0);
		return aux;
	}

	public Item GetItemId(Integer Id) {
		Log.i("ENTRO", "GetItemId");
		Cursor cursor = null;
		Item item_relleno = new Item();

		String error = new String();

		item_relleno.setIdPregunta(Id);

		String[] camposAPedirItemTexto = new String[2];
		camposAPedirItemTexto[0] = "Texto";
		camposAPedirItemTexto[1] = "IdTipo";

		String[] parametros = new String[1];
		parametros[0] = String.valueOf(Id);

		ArrayList<RespuestaRellenada> respuestas = new ArrayList<RespuestaRellenada>();

		SQLiteDatabase db = this.getReadableDatabase();

		try {
			cursor = db.query("itemtexto", camposAPedirItemTexto, "IdItem=?", parametros, null, null, null);
			while(cursor.moveToNext()) {
				item_relleno.setTextoPregunta(cursor.getString(0));
				item_relleno.setIdTipo(cursor.getInt(1));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			String[] camposAPedirItemRespuestas = new String[3];
			camposAPedirItemRespuestas[0] = "IdRespuesta";
			camposAPedirItemRespuestas[1] = "IdItemSiguiente";
			camposAPedirItemRespuestas[2] = "Valor";

			cursor = db.query("itemrespuestas", camposAPedirItemRespuestas, "IdItem=?", parametros, null, null, null);

			cursor.moveToFirst();
			for (int i=0; i<cursor.getCount(); i++) {
				RespuestaRellenada respu = new RespuestaRellenada();

				int id_respuesta;
				if(cursor.getString(0) == null) {
					id_respuesta = -1;
				} else {
					id_respuesta = Integer.parseInt(cursor.getString(0));
				}

				respu.setSiguiente(Integer.parseInt(cursor.getString(1)));
				if (cursor.getString(2) == null) {
					respu.setValor(0f);
				} else {
					respu.setValor(Float.valueOf(cursor.getString(2)));
				}

				String[] camposRespuestaTexto = new String[1];
				camposRespuestaTexto[0] = "Texto";

				try {
					camposRespuestaTexto[0] = "Texto";
					String[] args_b = new String[1];
					args_b[0] = String.valueOf(id_respuesta);

					Cursor cursorRespuesta = db.query("respuestatexto", camposRespuestaTexto, "IdRespuesta=?",
							args_b, null, null, null);

					if (cursorRespuesta.moveToNext()) {
						switch (id_respuesta) {
							case 46:	//Entre hoy y [acontecimiento 1 mes]
								respu.setTextoRespuesta("Entre hoy y " + devolverAcontecimiento(257));
								break;
							case 47:	//Entre [acontecimiento 1 mes] y [acontecimiento 3 meses]
								respu.setTextoRespuesta("Entre " + devolverAcontecimiento(257) + " y " + devolverAcontecimiento(258));
								break;
							case 48:	//Entre [acontecimiento 3 meses] y [acontecimiento 1 año]
								respu.setTextoRespuesta("Entre " + devolverAcontecimiento(258) + " y " + devolverAcontecimiento(259));
								break;
							case 49:	//Antes de [acontecimiento 1 año]
								respu.setTextoRespuesta("Antes de " + devolverAcontecimiento(259));
								break;
							default:
								respu.setTextoRespuesta(cursorRespuesta.getString(0));
								break;
						}
					}
					cursorRespuesta.close();
				}

				catch (Exception e) {
					e.printStackTrace();
				}

				try {
					camposAPedirItemTexto[0] = "IdError";
					String[] args_b = new String[2];
					args_b[0] = String.valueOf(id_respuesta);
					args_b[1] = parametros[0];

					Cursor c3 = db.query("error", camposAPedirItemTexto,
							"IdRespuesta=? AND IdItem=?", args_b, null, null,
							null);

					while(c3.moveToNext()){
						error = c3.getString(0);
						try {
							camposAPedirItemTexto[0] = "Texto";
							String[] args_c = new String[1];
							args_c[0] = error;

							Cursor c4 = db.query("errortexto", camposAPedirItemTexto, "IdError=?", args_c,
									null, null, null);

							while(c4.moveToNext())
								respu.setTextoError(c4.getString(0));
							c4.close();

						}

						catch (Exception e) {
							e.printStackTrace();
						}
					}
					c3.close();
				}

				catch (Exception e) {
					e.printStackTrace();
				}
				respuestas.add(respu);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		item_relleno.setRespuestas(respuestas);
		Log.i("SALGO", "GetItemId");
		return item_relleno;
	}

	private String devolverAcontecimiento(int acontecimiento) {
		Log.i("ENTRO", "devolverAcontecimiento");
		String[] parametrosValores = new String[2];
		parametrosValores[0] = String.valueOf(acontecimiento);
		parametrosValores[1] = String.valueOf(Logica.idUsuario);

		String respuesta = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("respuestasUsuarioNM", new String[]{"valor"}, "IdRespuesta=? AND IdUsuario=?",
				parametrosValores, null, null, null, null);
		if (cursor!=null && cursor.moveToFirst()) {
			respuesta = cursor.getString(0);
		}
		cursor.close();

		return respuesta;
	}

	public int getUltimaPreguntaSegunUsuario(int idUsuario) {
		Log.i("ENTRO", "getUltimaPreguntaSegunUsuario");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("usuarios", new String[]{"ultimaPregunta"}, "IdUsuario" + "=?",
				new String[]{String.valueOf(idUsuario)}, null, null, null, null);
		int ultimaPregunta;
		if (cursor!=null && cursor.moveToFirst()) {
			ultimaPregunta = cursor.getInt(0);
		} else {
			insertarUsuarioNuevo(idUsuario);
			ultimaPregunta = 0;
		}
		db.close();
		cursor.close();

		//ultimaPregunta = 284; //desarrollo
		return ultimaPregunta;
	}

	/*
	public String getAviso(int i) {
		Log.i("ENTRO", "getAviso");
		SQLiteDatabase db = this.getReadableDatabase();

		String[] parametros = new String[1];
		parametros[0] = String.valueOf(Id);

		Cursor cursor = db.query(TABLA_AVISOS, new String[]{TEXTO}, "IdAviso" + "=?",
				new String[]{String.valueOf(i)}, null, null, null, null);

		String aux = new String();

		if (cursor != null){
			cursor.moveToFirst();
			aux = cursor.getString(0);
			cursor.close();
			Log.i("SALGO", "getAviso");
			return aux;
		}
		else{
			db.close();
			Log.i("SALGO", "getAviso");
			return "";
		}
	}
	*/

	public ArrayList <Colegio> getListaColegios(){
		Log.i("ENTRO", "getListaColegios");

		ArrayList<Colegio> listaColegios = new ArrayList<Colegio>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLA_CENTROS, new String[] {"IdCentro", "Nombre"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			listaColegios.add(new Colegio(cursor.getInt(0), cursor.getString(1)));
		}
//    db.close();
		cursor.close();
		Log.i("SALGO", "getListaColegios");
		return listaColegios;

	}


	public void insertarUsuarioNuevo(int idUsuario) {
		Log.i("ENTRO", "insertarUsuarioNuevo");

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues campos = new ContentValues();
		campos.put("IdUsuario", idUsuario);
		campos.put("ultimaPregunta", 1);
		db.insert("usuarios", null, campos);

		db.close();
		Log.i("SALGO", "insertarUsuarioNuevo");
	}

	public void insertarRespuestaUsuario(int idPregunta, int idUsuario, String valor) {
		Log.i("ENTRO", "insertarRespuestaUsuario");
		//if (comprobarSiYaExisteValorAnterior
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("IdRespuesta", idPregunta);
		values.put("IdUsuario", idUsuario);
		values.put("valor", valor);
		db.insert("respuestasUsuarioNM", null, values);

		db.close();
		Log.i("SALGO", "insertarRespuestaUsuario");
	}

	public void aumentarUltimaPregunta(int idUsuario, int idPregunta) {
		Log.i("ENTRO", "aumentarUltimaPregunta");

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("ultimaPregunta", idPregunta);

		String[] parametros = new String[1];
		parametros[0] = Integer.toString(idUsuario);

		db.update("usuarios", values, "IdUsuario=?", parametros);

		db.close();
		Log.i("SALGO", "aumentarUltimaPregunta");
	}


	/**
	 * Cuando finaliza el 'Gambling 12 meses' puede darse el caso de que al final se arrepienta de
	 * "no tener vicios". Es decir, que primero diga en 'Tabla vida' que tiene algún vicio pero que
	 * a la hora de valorarlo por tiempos en 'Gambling 12 meses' diga que no haya jugado nunca
	 *
	 * cuando contesta 0 en todos los respuestas del 75 al 106.
	 *
	 * @return booleano  Si tiene algún vicio o ninguno
	 */
	public boolean finalmenteSinVicios(int idUsuario) {
		Log.i("ENTRO", "finalmenteSinVicios");
		boolean finalmenteSinVicios;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("respuestasUsuarioNM", new String[]{"valor"}, "IdUsuario" + "=? AND IdRespuesta BETWEEN 75 AND 106 AND valor!=0",
				new String[]{String.valueOf(idUsuario)}, null, null, null, null);
		if (cursor!=null && cursor.moveToFirst()) {
			finalmenteSinVicios = false;
		} else {
			finalmenteSinVicios = true;
		}
		db.close();
		cursor.close();

      /*
            SELECT * FROM respuestasUsuarioNM
            WHERE IdUsuario = 1
               AND IdRespuesta BETWEEN 75 AND 106
               AND valor != 0
       */

		Log.i("SALGO", "finalmenteSinVicios");
		return finalmenteSinVicios;
	}

	public boolean masVeces1MesQueDurante12Meses(int idPregunta, int veces1Mes) {
		Log.i("ENTRO", "masVeces1MesQueDurante12Meses");
		boolean finalmenteMasVeces1MesQueDurante12Meses = false;

		String[] parametros = new String[2];
		parametros[0] = String.valueOf(idPregunta-33);	//-33 es la distancia entre G12meses y G1mes
		parametros[1] = String.valueOf(Logica.idUsuario);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("respuestasUsuarioNM", new String[]{"valor"}, "IdRespuesta=? AND IdUsuario=?",
				parametros, null, null, null, null);

		if(cursor.moveToNext()){
			int veces12Meses = cursor.getInt(0);
			if (veces1Mes > veces12Meses) {
				finalmenteMasVeces1MesQueDurante12Meses = true;
			}
		}

		db.close();
		cursor.close();


		Log.i("SALGO", "masVeces1MesQueDurante12Meses");
		return finalmenteMasVeces1MesQueDurante12Meses;
	}
}

   /*
      Cursor c = db.query(
            "Quotes",  //Nombre de la tabla
            null,  //Lista de Columnas a consultar
            null,  //Columnas para la clausula WHERE
            null,  //Valores a comparar con las columnas del WHERE
            null,  //Agrupar con GROUP BY
            null,  //Condición HAVING para GROUP BY
            null  //Clausula ORDER BY
      );
   */