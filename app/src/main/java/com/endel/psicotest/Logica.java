package com.endel.psicotest;

import android.app.Activity;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.endel.psicotest.vista.RespuestaValor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Javier on 07/09/2015.
 */
public class Logica {
    public static int averiguarSiguiente(Item item, HashMap<Integer, Integer> mapaRespuestasTablaVida, int siguiente, boolean algunVicio) {
        switch (item.getIdPregunta()) {
            case 11:    //Tabla de vida
                //Valoramos primero si no tiene vicios
                if (!algunVicio) {
                    siguiente = 107;
                } else {
                    //Lógica de la pregunta 11
                    Integer respuesta = mapaRespuestasTablaVida.get(Integer.valueOf(11));
                    if (respuesta.intValue() > 0) {
                        siguiente = 43; //Gambling edad
                    }
                }
                break;
        }
        return siguiente;
    }

    public static boolean grabarRespuestas(Item item, RadioGroup radioGroup, List<RespuestaValor> listaRespuestasRadioButton, Context contexto, Activity activity, boolean algunVicio, HashMap<Integer, Integer> mapaRespuestasTablaVida) {
        //Si viene de la tabla vida
        if (item.getIdPregunta() == 11) {
            algunVicio = guardarRespuestasTablaVida(activity, algunVicio, mapaRespuestasTablaVida);
            return algunVicio;
        }


        switch (item.getIdTipo()) {
            case 1: //Contador
                break;
            case 2: //RadioButton
                int opcionSeleccionada = radioGroup.getCheckedRadioButtonId();

                //Buscamos el radio button seleccionado
                boolean buscando = true;
                for (int i = 0; listaRespuestasRadioButton.size() > 0 && buscando; i++) {
                    RespuestaValor respuestaValor = listaRespuestasRadioButton.get(i);
                    if (respuestaValor.getId() == opcionSeleccionada) {
                        buscando = false;
                        Toast.makeText(contexto, String.valueOf("ID: " + respuestaValor.getId() + " | VALOR: " + respuestaValor.getValor() + " | SIGUIENTE: " + respuestaValor.getSiguiente()), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        return algunVicio;
    }


    private static boolean guardarRespuestasTablaVida(Activity activity, boolean algunVicio, HashMap<Integer, Integer> mapaRespuestasTablaVida) {
        for (int i = 11; i < 43; i++) {
            CheckBox checkBox = (CheckBox) activity.findViewById(i);
            boolean escogido = checkBox.isChecked();
            int escogidoEntero = (escogido) ? 1 : 0;
            if (escogido) {
                algunVicio = true;
            }
            mapaRespuestasTablaVida.put(Integer.valueOf(i), Integer.valueOf(escogidoEntero));
        }
        return algunVicio;
    }


}

