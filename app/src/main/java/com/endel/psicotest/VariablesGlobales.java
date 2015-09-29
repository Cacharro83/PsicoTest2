package com.endel.psicotest;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class VariablesGlobales {
	
	public static String SIGUIENTE = "Siguiente";
	public static String ERROR_CONTESTAR = "Responde antes de continuar";

	public static String ERROR = "Error";
	
	public static String CONTINUAR_TEST = "Continuar test";
	
	public static String MODIFICAR_RESPUESTA = "Modificar respuesta";
	public static String VOLVER = "Volver";
	
	public final static int PRUEBA_STROOP = 10;
	public final static int TEST_STROOP = 30;
	
	public static final long TIME_DELAY = 2000;
	
//	public final static int PRUEBA_STROOP = 1;//10
//	public final static int TEST_STROOP = 1;//30
//	
//	public static final long TIME_DELAY = 2;
	
	public static int item_actual = 0;
	
	public static String[] tiempos = {"1 día", "1 semana", "1 mes", "6 meses", "1 año", "5 años", "25 años"};
	public static String etapa = "Etapa";
	public static String anuncio = "Anuncio";
	public static Integer Maximo = 1000;
	
	public static Integer ITEM_EDAD = 384;
	
	public Integer anterior = 0;
	
	public static String pressTwice = "Presiona de nuevo para volver a la pregunta anterior";
	
	public static final int DOUBLE_BACK_TIME = 2000;

	public static void PublicToast(Context context, String str){
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		v.setTextSize(30);
		toast.show();
	}
	

}

