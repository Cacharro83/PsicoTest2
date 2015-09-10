package com.endel.psicotest;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.endel.psicotest.baseDatos.DataBaseHelper;
import com.endel.psicotest.vista.LayoutBasico;
import com.endel.psicotest.vista.RespuestaValor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Javier on 07/09/2015.
 */
public class Logica {
    public static int ultimoVicio, vicioActual, idUsuario = 1;
    public static boolean hayViciosConDinero = false;

    public static int averiguarSiguiente(Item item, int siguiente, boolean algunVicio, Context contexto, View viewById) {
        String respuestaDada = "";
        if (item.getIdTipo() == 1) {
            TextView campo = (TextView) viewById;
            respuestaDada = campo.getText().toString();
        }


        Integer respuesta;

        switch (item.getIdPregunta()) {
            case 11:    //Tabla de vida - Inicio
                //Valoramos primero si no tiene vicios
                if (!algunVicio) {
                    return 107;
                } else {
                    //En función del vicio te redirige al primer 'Gambling edad' adecuado
                    for (int i=11; i<43; i++) {
                        respuesta = LayoutBasico.mapaRespuestasTablaVida.get(Integer.valueOf(i));
                        if (respuesta.intValue() > 0) {
                            vicioActual = i;

                            //Si hay vicio impar hay que hacer preguntas 220-240
                            if (vicioActual%2!=0) {
                                hayViciosConDinero = true;
                            }

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
                DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);
                if (esUltimoVicio(item, ultimoVicio) && dataBaseHelper.finalmenteSinVicios(idUsuario, contexto)) {
                    return 107;
                }

                //Si responde '0' no tiene sentido llevar a la siguiente asignada (108-139)
                //Se le lleva al próximo vicio
                if (respuestaDada.equals("0")) {
                    if (esUltimoVicio(item, ultimoVicio)) {
                        //Si hay vicios con dinero hay que ir a los SOGS-RA y DSM-IV-MR-J
                        if (hayViciosConDinero) {
                            return 220; //SOGS-RA y DSM-IV-MR-J
                        } else {
                            return 241; //Gambling motives
                        }

                    } else {
                        return buscarSiguienteVicio();
                    }
                }

                break;

            /*
             * Si llega al 'Gambling Compañía' (172-203) no debe saltar a la siguiente sección
             * 'Gambling Motives' a no ser que sea el último vicio, debería recorrer el siguiente
             * vicio
             */
            case 172:case 173:case 174:case 175:case 176:case 177:case 178:case 179:case 180:
            case 181:case 182:case 183:case 184:case 185:case 186:case 187:case 188:case 189:
            case 190:case 191:case 192:case 193:case 194:case 195:case 196:case 197:case 198:
            case 199:case 200:case 201:case 202:case 203:
                if (item.getIdPregunta() != ultimoVicio+161) {
                    return buscarSiguienteVicio();
                }
        }
        return siguiente;   //Si no hay ningún cambio 'siguiente' sigue como estaba
    }

    private static boolean esUltimoVicio(Item item, int ultimoVicio) {
        if (item.getIdPregunta()==ultimoVicio+64) {
            return true;
        } else {
            return false;
        }
    }

    private static int buscarSiguienteVicio() {
        int respuesta;
        for (int i=vicioActual+1; i<43; i++) {
            respuesta = LayoutBasico.mapaRespuestasTablaVida.get(Integer.valueOf(i));
            if (respuesta > 0) {
                vicioActual = i;
                return i + 32;  //Gambling edad
            }
        }
        return 0;   //Caso improbable
    }


    public static boolean grabarRespuestas(Item item, RadioGroup radioGroup, List<RespuestaValor> listaRespuestasRadioButton, Context contexto, Activity activity, boolean algunVicio) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);

        //Si viene de la tabla vida
        if (item.getIdPregunta() == 11) {
            algunVicio = guardarRespuestasTablaVida(activity, algunVicio, LayoutBasico.mapaRespuestasTablaVida);
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
                ultimoVicio = i;
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

