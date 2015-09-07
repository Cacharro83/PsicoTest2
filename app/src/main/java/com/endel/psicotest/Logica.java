package com.endel.psicotest;

import android.app.Activity;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.endel.psicotest.baseDatos.DataBaseHelper;
import com.endel.psicotest.vista.RespuestaValor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Javier on 07/09/2015.
 */
public class Logica {
    public static int ultimoVicioParaGambling12Meses, idUsuario = 1;

    public static int averiguarSiguiente(Item item, HashMap<Integer, Integer> mapaRespuestasTablaVida, int siguiente, boolean algunVicio) {
        Integer respuesta;

        switch (item.getIdPregunta()) {
            case 11:    //Tabla de vida - Inicio
                //Valoramos primero si no tiene vicios
                if (!algunVicio) {
                    return 107;
                } else {
                    //En función del vicio te redirige al primer 'Gambling edad' adecuado
                    for (int i=11; i<43; i++) {
                        respuesta = mapaRespuestasTablaVida.get(Integer.valueOf(i));
                        if (respuesta.intValue() > 0) {
                            return i + 32;  //Gambling edad
                        }
                    }
                }
                break;

            /*
             *  Si contesta 0 para todos los items del 75 al 106 inclusive, pasa al item 107.
             *  Si contesta mayor que 0, pasa al item 108, 140, 172, 204, 220-231, 232-240, 241-256
             */
            case 75:case 76:case 77:case 78:case 79:case 80:case 81:case 82:case 83:case 84:case 85:
            case 86:case 87:case 88:case 89:case 90:case 91:case 92:case 93:case 94:case 95:case 96:
            case 97:case 98:case 99:case 100:case 101:case 102:case 103:case 104:case 105:case 106:
                //Si es el último vicio y al final se "arrepiente" de tener vicios
                if (item.getIdPregunta() == ultimoVicioParaGambling12Meses && Logica.finalmenteSinVicios()) {
                    return 107;
                }
                break;
        }
        return siguiente;   //Si no hay ningún cambio 'siguiente' sigue como estaba
    }

    /**
     * Cuando finaliza el 'Gambling 12 meses' puede darse el caso de que al final se arrepienta de
     * "no tener vicios". Es decir, que primero diga en 'Tabla vida' que tiene algún vicio pero que
     * a la hora de valorarlo por tiempos en 'Gambling 12 meses' diga que no haya jugado nunca
     *
     * @return booleano  Si tiene algún vicio o ninguno
     */
    private static boolean finalmenteSinVicios() {
        return false;
    }


    public static boolean grabarRespuestas(Item item, RadioGroup radioGroup, List<RespuestaValor> listaRespuestasRadioButton, Context contexto, Activity activity, boolean algunVicio, HashMap<Integer, Integer> mapaRespuestasTablaVida) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);

        //Si viene de la tabla vida
        if (item.getIdPregunta() == 11) {
            algunVicio = guardarRespuestasTablaVida(activity, algunVicio, mapaRespuestasTablaVida);
            return algunVicio;
        }


        switch (item.getIdTipo()) {
            case 1: //Contador
                TextView textView = (TextView) activity.findViewById(3);  //ojo en el caso de los 2 contadores
                dataBaseHelper.insertarRespuestaUsuario(item, idUsuario, textView.getText().toString());
                break;
            case 2: //RadioButton
                int opcionSeleccionada = radioGroup.getCheckedRadioButtonId();

                //Buscamos el radio button seleccionado
                boolean buscando = true;
                for (int i=0; listaRespuestasRadioButton.size()>0&&buscando; i++) {
                    RespuestaValor respuestaValor = listaRespuestasRadioButton.get(i);
                    if (respuestaValor.getId() == opcionSeleccionada) {
                        buscando = false;
                        Toast.makeText(contexto, String.valueOf("ID: " + respuestaValor.getId() + " | VALOR: " + respuestaValor.getValor() + " | SIGUIENTE: " + respuestaValor.getSiguiente()), Toast.LENGTH_LONG).show();
                        dataBaseHelper.insertarRespuestaUsuario(item, idUsuario, String.valueOf(respuestaValor.getValor()));
                    }
                }
                break;
        }

        dataBaseHelper.aumentarUltimaPregunta(idUsuario, item.getIdPregunta());
        return algunVicio;
    }


    private static boolean guardarRespuestasTablaVida(Activity activity, boolean algunVicio, HashMap<Integer, Integer> mapaRespuestasTablaVida) {
        for (int i=11; i<43; i++) {
            CheckBox checkBox = (CheckBox) activity.findViewById(i);
            boolean escogido = checkBox.isChecked();
            int escogidoEntero = (escogido) ? 1 : 0;
            if (escogido) {
                algunVicio = true;
                ultimoVicioParaGambling12Meses = i + 64;
            }
            mapaRespuestasTablaVida.put(Integer.valueOf(i), Integer.valueOf(escogidoEntero));
        }
        return algunVicio;
    }


    public static boolean validarRespuestas(Item item, Activity activity, Context contexto, RadioGroup radioGroup) {
        int id_pregunta_tipo = item.getIdTipo();
        int numeroRespuestas = item.getRespuestas().size();

        for (int numeroRespuesta=0; numeroRespuesta<numeroRespuestas; numeroRespuesta++) {
            //1-Contadores | 2-RadioButton | 3-Fecha | 4-TextView | 5-CheckBox
            switch (id_pregunta_tipo) {
                case 1: //Contador
                    TextView textView = (TextView) activity.findViewById(numeroRespuesta + 3);  //1 es pregunta y 2 es el separador
                    String valor = textView.getText().toString();

                    if (valor.equals("")) {
                        VariablesGlobales.PublicToast(contexto, "Debes completar todos los campos");
                        return false;
                    } else {
                        return true;
                    }
                case 2: //RadioButton
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        VariablesGlobales.PublicToast(contexto, "Debes escoger una de las opciones");
                        return false;
                    } else {
                        return true;
                    }
            }
        }
        return true;    //Si es de tipo fecha siempre coge un valor por defecto
    }
}

