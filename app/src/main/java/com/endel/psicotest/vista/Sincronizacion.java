package com.endel.psicotest.vista;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.test.UiThreadTest;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.endel.psicotest.baseDatos.DataBaseHelper;
import com.endel.psicotest.baseDatos.DBMain;
import com.endel.psicotest.VariablesGlobales;
import com.endel.psicotest.R;

public class Sincronizacion extends Activity {

    private static final int FORZAR_ACTUALIZACION = 1;
    Button bSalir = null;
    Button bActualizarIdioma = null;
    Button bTest = null;
    Button bSubirResultados = null;
    Button bClearTests = null;

    Button btCerrar = null;
    String textoCerrar =	"Finish test";

    EditText ipText = null;

    TextView tvIdiomaTableta = null;
    TextView tvResultados = null;

    ImageView imagenEstado = null;

    boolean updatedAll = false;

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    private long fileSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sincronizacion);

        updatedAll = false;

        bSalir = (Button) findViewById(R.id.button2);
        bActualizarIdioma = (Button) findViewById(R.id.button4);
        bTest = (Button) findViewById(R.id.button3);
        bSubirResultados = (Button) findViewById(R.id.button1);
        bClearTests = (Button)findViewById(R.id.button6);
        btCerrar = (Button) findViewById(R.id.button5);

        ipText = (EditText) findViewById(R.id.editText1);
        ipText.setText("192.168.0.100:8080");
//		ipText.setText("psicologia.seineko.com");

        tvIdiomaTableta = (TextView) findViewById(R.id.textView3);
        tvResultados = (TextView) findViewById(R.id.textView2);

        imagenEstado = (ImageView) findViewById(R.id.imageView1);
        imagenEstado.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_offline));

        DBMain myDbHelper = new DBMain(getApplicationContext());
        try {
            myDbHelper.crearBD();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.abrirBD();
        } catch (SQLException sqle) {
            throw sqle;
        }
        btCerrar.setText(textoCerrar);

        //tvIdiomaTableta.setText("Language of the test: " + myDbHelper.getNombreIdioma());
        //tvResultados.setText("Pending results: " + myDbHelper.getNumeroResultados());
        myDbHelper.close();
        /*
        bTest.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WebService wb = new WebService(ipText.getText().toString(), getApplicationContext());
                Boolean estado = wb.EstaDisponible(ipText.getText().toString());
                if(estado == true){
                    imagenEstado.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
                }
                else{
                    imagenEstado.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_offline));
                }
            }
        });
        */

        bActualizarIdioma
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // prepare for a progress bar dialog
                        progressBar = new ProgressDialog(v.getContext());
                        progressBar.setCancelable(true);
                        progressBar.setMessage("Syncing ...");
                        progressBar
                                .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressBar.setProgress(0);
                        progressBar.setMax(100);
                        progressBar.show();

                        // reset progress bar status
                        progressBarStatus = 0;

                        // reset filesize
                        fileSize = 0;

                        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
                        try {
                            myDbHelper.createDataBase();
                            myDbHelper.openDataBase();
                        } catch (IOException ioe) {
                            throw new Error("Unable to create database");
                        }
                       // WebService wb = new WebService(ipText.getText().toString(), getApplicationContext());

                        //Integer IdIdioma = myDbHelper.getIdIdioma();
                        //if(IdIdioma == -1){
                            //VariablesGlobales.PublicToast(getApplicationContext(), "No se ha encontrado idioma en la base de datos");
                        //}


                        //Par_ID_Nombre parIdioma = wb.idiomaActivo(ipText.getText().toString());

                        //Integer idiomaActivo = Integer.valueOf(parIdioma.getId());

                        //if(idiomaActivo == -1){
                            //VariablesGlobales.PublicToast(getApplicationContext(), "No se ha encontrado el idioma activo en el WB");
                        //}
                        myDbHelper.close();

                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressBarStatus);
                            }
                        });

                        // Si el idioma es el mismo
                       /* if(idiomaActivo == IdIdioma){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showDialog(FORZAR_ACTUALIZACION);
                                        }
                                    });

                                }
                            }).start();
                        }
                        else{
                            updateAll(myDbHelper, wb);
                            updatedAll = false;
                        }*/

//						progressBarStatus = progressBarStatus + 15;
//						progressBarHandler.post(new Runnable() {
//							public void run() {
//								progressBar
//										.setProgress(progressBarStatus);
//							}
//						});

                        myDbHelper.close();
                        //tvIdiomaTableta.setText("Language of the test: " + myDbHelper.getNombreIdioma());

                    }

                });

        bSubirResultados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
                try {
                    myDbHelper.createDataBase();
                } catch (IOException ioe) {
                    throw new Error("Unable to create database");
                }

                try {
                    myDbHelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }
                /*
                Boolean correcto = myDbHelper.sincronizarRespuestas(ipText.getText().toString());
                if(!correcto){
                    VariablesGlobales.PublicToast(getApplicationContext(), "Threre was a problem sending the results, try again later");
                }
                */
                //tvResultados.setText("Pending results: " + myDbHelper.getNumeroResultados());
                myDbHelper.close();
            }
        });

        bClearTests.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
                try {
                    myDbHelper.createDataBase();
                } catch (IOException ioe) {
                    throw new Error("Unable to create database");
                }

                try {
                    myDbHelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }

                //myDbHelper.dropAllResults();
                //tvResultados.setText("Pending results: " + myDbHelper.getNumeroResultados());
                myDbHelper.close();

            }
        });


        bSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent i = new Intent(Sincronizacion.this, MainActivity.class);
                //startActivity(i);
                finish();

            }
        });


    }
/*
    protected void updateCentros(DataBaseHelper myDbHelper, WebService wb) {

        progressBarStatus = 0;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo la lista de centros
        ArrayList<Par_ID_Nombre> centros = wb.obtenerCentros(ipText.getText().toString());
        if(centros != null){
            myDbHelper.dropTablaCentros();
            for(int i = 0; i < centros.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.NOMBRE_CENTRO, centros.get(i).getNombre());
                values.put(myDbHelper.ID_CENTRO, centros.get(i).getId());
                myDbHelper.insertData(myDbHelper.TABLA_CENTROS, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener los centros del servicio web");
        }

        progressBarStatus = 100;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // sleep 2 seconds, so that you can see the 100%
        try {
            Thread.sleep(1000);
            progressBar.dismiss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
*/
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Dialog dialogo = null;

        switch (id) {
            case FORZAR_ACTUALIZACION:
                dialogo = crearDialogoActualizarIdioma();
                break;
        }

        return dialogo;
    }

    private Dialog crearDialogoActualizarIdioma() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Updating tests...");
        builder.setMessage("If you want to update all the texts it might take some minutes depending on the Internet connection");

        builder.setPositiveButton("Update all", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
                        try {
                            myDbHelper.createDataBase();
                            myDbHelper.openDataBase();
                        } catch (IOException ioe) {
                            throw new Error("Unable to create database");
                        }
                       // WebService wb = new WebService(ipText.getText().toString(), getApplicationContext());
                        //updateAll(myDbHelper, wb);
                        updatedAll = false;
                        myDbHelper.close();

                    }
                }).start();
                dialog.cancel();

            }
        });


        builder.setNegativeButton("Update only centers", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
                        try {
                            myDbHelper.createDataBase();
                            myDbHelper.openDataBase();
                        } catch (IOException ioe) {
                            throw new Error("Unable to create database");
                        }
                       // WebService wb = new WebService(ipText.getText().toString(), getApplicationContext());
                        //updateCentros(myDbHelper, wb);
                        myDbHelper.close();


                    }
                }).start();
                dialog.cancel();

            }

        });

        return builder.create();
    }
    /*
    protected void updateAll(DataBaseHelper myDbHelper, WebService wb) {


        progressBarStatus = 0;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        Par_ID_Nombre parIdioma = wb.idiomaActivo(ipText.getText().toString());

        Integer idiomaActivo = Integer.valueOf(parIdioma.getId());
        // Actualizo la lista de centros
        ArrayList<Par_ID_Nombre> centros = wb.obtenerCentros(ipText.getText().toString());
        if(centros != null){
            myDbHelper.dropTablaCentros();
            for(int i = 0; i < centros.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.NOMBRE_CENTRO, centros.get(i).getNombre());
                values.put(myDbHelper.ID_CENTRO, centros.get(i).getId());
                myDbHelper.insertData(myDbHelper.TABLA_CENTROS, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener los centros del servicio web");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo la lista de preguntas
        ArrayList<Par_ID_Nombre> preguntas = wb.obtenerPreguntas(ipText.getText().toString(), idiomaActivo);
        if(preguntas != null){
            myDbHelper.dropTablaPreguntas();
            for(int i = 0; i < preguntas.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_ITEM, preguntas.get(i).getId());
                values.put(myDbHelper.TEXTO, preguntas.get(i).getNombre());
                myDbHelper.insertData(myDbHelper.TABLA_PREGUNTAS, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener las preguntas del servicio web");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo la lista de respuestas
        ArrayList<Respuesta> respuestasPosibles = wb.obtenerRespuestas(ipText.getText().toString(), idiomaActivo);
        if(respuestasPosibles != null){
            myDbHelper.dropTablaRespuestas();
            for(int i = 0; i < respuestasPosibles.size() ; i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_RESPUESTA, respuestasPosibles.get(i).getId());
                values.put(myDbHelper.TEXTO, respuestasPosibles.get(i).getNombre());
                values.put(myDbHelper.FOTO, respuestasPosibles.get(i).getFoto());
                myDbHelper.insertData(myDbHelper.TABLA_RESPUESTATEXTO, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener las respuestas posibles del servicio web");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo los mensajes de error
        ArrayList<Par_ID_Nombre> errores = wb.obtenerMensajesError(ipText.getText().toString(), idiomaActivo);
        if(errores != null){
            myDbHelper.dropTablaError();
            for(int i = 0; i < errores.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_ERROR, errores.get(i).getId());
                values.put(myDbHelper.TEXTO, errores.get(i).getNombre());
                myDbHelper.insertData(myDbHelper.TABLA_ERRORES, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener los mensajes de error del servicio web");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo los mensajes de avisos
        ArrayList<Par_ID_Nombre> avisos = wb.obtenerMensajesAviso(ipText.getText().toString(), idiomaActivo);
        if(avisos != null){
            myDbHelper.dropTablaAvisos();
            for(int i = 0; i < avisos.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_AVISO, avisos.get(i).getId());
                values.put(myDbHelper.TEXTO, avisos.get(i).getNombre());
                myDbHelper.insertData(myDbHelper.TABLA_AVISOS, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener los mensajes de aviso del servicio web");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo los mensajes de paises
        ArrayList<Par_ID_Nombre> paises = wb.obtenerPaises(ipText.getText().toString(), idiomaActivo);
        if(paises != null){
            myDbHelper.dropTablaPaises();
            for(int i = 0; i < paises.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_PAIS, paises.get(i).getId());
                values.put(myDbHelper.TEXTO, paises.get(i).getNombre());
                myDbHelper.insertData(myDbHelper.TABLA_PAISES, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener la lista de paises del servicio web");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo los mensajes de stroop
        ArrayList<Par_ID_Nombre> strooptexto = wb.obtenerStroop(ipText.getText().toString(), idiomaActivo);
        if(strooptexto != null){
            myDbHelper.dropTablaStroop();
            for(int i = 0; i < strooptexto.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_STROOP, strooptexto.get(i).getId());
                values.put(myDbHelper.TEXTO, strooptexto.get(i).getNombre());
                myDbHelper.insertData(myDbHelper.TABLA_STROOPTEXTO, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener los mensajes de stroop");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo los mensajes de stroop
        ArrayList<Par_ID_Nombre> delaytexto = wb.obtenerDelay(ipText.getText().toString(), idiomaActivo);
        if(delaytexto != null){
            myDbHelper.dropTablaDelay();
            for(int i = 0; i < delaytexto.size(); i++){
                ContentValues values = new ContentValues();
                values.put(myDbHelper.ID_DELAY, delaytexto.get(i).getId());
                values.put(myDbHelper.TEXTO, delaytexto.get(i).getNombre());
                myDbHelper.insertData(myDbHelper.TABLA_DELAYTEXTO, values);
            }
        }
        else{
            VariablesGlobales.PublicToast(getApplicationContext(), "Ha habido un error al obtener los mensajes de Delay Discounting");
        }

        progressBarStatus = progressBarStatus + 15;
        progressBarHandler.post(new Runnable() {
            public void run() {
                progressBar
                        .setProgress(progressBarStatus);
            }
        });

        // Actualizo el idioma de la tabla en la base
        myDbHelper.actualizarIdioma(parIdioma.getNombre(), idiomaActivo);

        // sleep 2 seconds, so that you can see the 100%
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // close the progress bar dialog
        progressBar.dismiss();

    }

    public void onClick2 (View view){
        finish();
        VariablesGlobales.item_actual = 0;
        RespuestasTest.getInstance().resetear();
        Intent i = new Intent(Sincronizacion.this, MainActivity.class);
        startActivity(i);
        finish();
    }
*/
}
