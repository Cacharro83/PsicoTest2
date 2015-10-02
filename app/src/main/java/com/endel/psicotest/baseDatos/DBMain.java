package com.endel.psicotest.baseDatos;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * http://www.c-sharpcorner.com/UploadFile/e14021/importing-database-in-android-studio/
 *
 * Created by vivek on 6/24/13.
 */
public class DBMain extends SQLiteOpenHelper {
    public static String DB_PATH; //= "data/data/com.endel.psicotest/databases/";
    public static String DB_NAME = "psico";
    public static int DB_VERSION = 177;
    private SQLiteDatabase dbObj;
    private final Context context;


    public DBMain(Context contexto) {
        super(contexto, DB_NAME, null, DB_VERSION);

        //Calculamos la ruta de la base de datos
        File file = new File(contexto.getDatabasePath(DB_NAME).getPath());
        DB_PATH = file.getParent() + File.separator;
        this.context  = contexto;
    }


    public void crearBD() throws IOException {
        boolean existeBaseDatos = checkDataBase();
        if(!existeBaseDatos) {
            this.getReadableDatabase();
            try {
                copiarBD();
            } catch (IOException e) {
                throw new Error("Error copiando la base de datos");
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
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }


    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    public void copiarBD() throws IOException{
        try {
            InputStream inputStream = context.getAssets().open(DB_NAME+".db");
            Log.i("Input Stream....", inputStream+"");
            String op =  DB_PATH + DB_NAME ;
            OutputStream output = new FileOutputStream( op);
            byte[] buffer = new byte[1024];
            int longitud;
            while ((longitud = inputStream.read(buffer))>0) {
                output.write(buffer, 0, longitud);
                Log.i("Content.... ", longitud+"");
            }
            output.flush();
            output.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.v("error", e.toString());
        }
    }


    public void abrirBD() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        dbObj = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.i("abriendo base datos...", dbObj.toString());
    }

    @Override
    public synchronized void close() {
        if(dbObj != null)
            dbObj.close();
        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void conectar(Context contexto) {
        DBMain db = new DBMain(contexto);

        try {
            db.crearBD();
        } catch (IOException ioExcepcion) {
            throw new Error("Base de datos no creada");
        }

        try {
            db.abrirBD();
        } catch(SQLException sqle) {
            throw sqle;
        }

    }
}

// Add your public helper methods to access and get content from the database.
// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
// to you to create adapters for your views.
