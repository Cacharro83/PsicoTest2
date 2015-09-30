package com.endel.psicotest;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.endel.psicotest.baseDatos.DataBaseHelper;
import com.endel.psicotest.vista.LayoutBasico;
import com.endel.psicotest.vista.RespuestaValor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Javier on 07/09/2015.
 */
public class Logica {
    public static int ultimoVicio, vicioActual, idUsuario = -1, USUARIO_EDAD;
    public static boolean hayViciosConDinero = false;

    public static int averiguarSiguiente(Item item, int siguiente, boolean algunVicio, Context contexto, View viewById) {
        String respuestaDada = "";
        if (item.getIdTipo() == 1) {
            Spinner spinner = (Spinner) viewById;
            respuestaDada = spinner.getSelectedItem().toString();
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
                if (esUltimoVicio(item, ultimoVicio) && dataBaseHelper.finalmenteSinVicios(idUsuario)) {
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
             * Si llega al 'Gambling Compañía' (172-203) y es el último vicio hay que valorar que
             * previamente si hay algún vicio con dinero salte a los 'SOGS-RA' y 'DSM-IV-MR-J' y no a
             * 'Gambling Motives'
             */
            case 173:case 175:case 177:case 179:case 181:case 183:case 185:case 187:case 189:case 191:
            case 193:case 195:case 197:case 199:case 201:case 203:
                if (vicioActual != ultimoVicio) {
                    return buscarSiguienteVicio();
                }
                if (vicioActual==ultimoVicio && hayViciosConDinero) {
                    return 220;
                }
                break;
            /*
             * Si está en 'Gambling Dinero' (204-219) no debería ir al 'siguiente' natural (220)
              * si hay más vicios
             */
            case 204:case 205:case 206:case 207:case 208:case 209:case 210:case 211:case 212:case 213:
            case 214:case 215:case 216:case 217:case 218:case 219:
                if (vicioActual != ultimoVicio) {
                    return buscarSiguienteVicio();
                }
                break;
        }
        return siguiente;   //Si no hay ningún cambio 'siguiente' sigue como estaba
    }


    private static boolean esUltimoVicio(Item item, int ultimoVicio) {
        if (item.getIdPregunta() == ultimoVicio+64) {
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

        switch (item.getIdPregunta()) {
            case 0:     //idUsuario
                dataBaseHelper.insertarUsuarioNuevo(idUsuario);
                break;
            case 1:     //edad
                Spinner edad = (Spinner) activity.findViewById(3);
                USUARIO_EDAD = Integer.parseInt(edad.getSelectedItem().toString());
                break;
            case 11:    //tabla vida
                algunVicio = guardarRespuestasTablaVida(activity, algunVicio, LayoutBasico.mapaRespuestasTablaVida, contexto);
                return algunVicio;
        }

        switch (item.getIdTipo()) {
            case 1:     //Contador
                Spinner spinner = (Spinner) activity.findViewById(3);  //ojo en el caso de los 2 contadores
                if (Logica.esHoraYMinutos(item.getIdPregunta())) {
                    Spinner spinnerMinutos = (Spinner) activity.findViewById(55);
                    int horas = Integer.valueOf(spinner.getSelectedItem().toString());
                    int minutos = Integer.valueOf(spinnerMinutos.getSelectedItem().toString());
                    dataBaseHelper.insertarRespuestaUsuario(item.getIdPregunta(), idUsuario, Integer.valueOf(horas*60+minutos).toString());
                } else {
                    dataBaseHelper.insertarRespuestaUsuario(item.getIdPregunta(), idUsuario, spinner.getSelectedItem().toString());
                }
                break;
            case 2:     //RadioButton
                int opcionSeleccionada = radioGroup.getCheckedRadioButtonId();

                //Buscamos el radio button seleccionado
                boolean buscando = true;
                for (int i=0; listaRespuestasRadioButton.size()>0&&buscando; i++) {
                    RespuestaValor respuestaValor = listaRespuestasRadioButton.get(i);
                    if (respuestaValor.getId() == opcionSeleccionada) {
                        buscando = false;
                        //Toast.makeText(contexto, String.valueOf("ID: " + respuestaValor.getId() + " | VALOR: " + respuestaValor.getValor() + " | SIGUIENTE: " + respuestaValor.getSiguiente()), Toast.LENGTH_LONG).show();
                        dataBaseHelper.insertarRespuestaUsuario(item.getIdPregunta(), idUsuario, String.valueOf(respuestaValor.getValor()));
                    }
                }
                break;
            case 3:     //3-Fecha
                DatePicker datePicker = (DatePicker) activity.findViewById(3);
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String fecha = Integer.toString(day).toString() + "/" + Integer.toString(month).toString() + "/" + Integer.toString(year).toString();
                dataBaseHelper.insertarRespuestaUsuario(item.getIdPregunta(), idUsuario, fecha);
                break;
            case 4:     //TextView
                TextView textView = (TextView) activity.findViewById(3);
                dataBaseHelper.insertarRespuestaUsuario(item.getIdPregunta(), idUsuario, textView.getText().toString());
                break;
            case 5:     //CheckBox
                break;
        }

        dataBaseHelper.aumentarUltimaPregunta(idUsuario, item.getIdPregunta());
        return algunVicio;
    }

    public static boolean esHoraYMinutos(int idPregunta) {
        if (idPregunta>=140 && idPregunta<=171) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean esEdad(int idPregunta) {
        if (idPregunta==1 || (idPregunta>=43 && idPregunta<=74)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean esDinero(int idPregunta) {
        if (idPregunta>=204 && idPregunta<=219) {
            return true;
        } else {
            return false;
        }
    }


    private static boolean guardarRespuestasTablaVida(Activity activity, boolean algunVicio, HashMap<Integer, Integer> mapaRespuestasTablaVida, Context contexto) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);
        for (int i=11; i<43; i++) {
            CheckBox checkBox = (CheckBox) activity.findViewById(i);
            boolean escogido = checkBox.isChecked();
            int escogidoEntero = (escogido) ? 1 : 0;
            if (escogido) {
                algunVicio = true;
                ultimoVicio = i;
            }
            mapaRespuestasTablaVida.put(Integer.valueOf(i), Integer.valueOf(escogidoEntero));   //debería de desaparecer
            dataBaseHelper.insertarRespuestaUsuario(Integer.valueOf(i), idUsuario, Integer.valueOf(escogidoEntero).toString());
        }
        return algunVicio;
    }


    public static boolean validarRespuestas(Item item, Context contexto, Activity activity, RadioGroup radioGroup) {
        int id_pregunta_tipo = item.getIdTipo();
        int numeroRespuestas = item.getRespuestas().size();
        LayoutBasico layoutBasico = new LayoutBasico(activity);

        switch (item.getIdPregunta()) {
            case 0:
                EditText editText = (EditText) activity.findViewById(3);
                String idUsuario = editText.getText().toString();
                if (idUsuario.equals("")) {
                    VariablesGlobales.PublicToast(contexto, contexto.getResources().getText(R.string.error_CampoVacio).toString());
                    return false;
                }
                for (int i=0; i<idUsuario.length(); i++) {
                    if (!Character.isDigit(idUsuario.charAt(i))) {
                        VariablesGlobales.PublicToast(contexto, contexto.getResources().getText(R.string.error_Numero).toString());
                        return false;
                    }
                }
                Logica.idUsuario = Integer.parseInt(idUsuario);
                if (hayUnTestRepetido()) {
                    layoutBasico.crearMensajeUsuarioDuplicado();
                    return false;
                } else {
                    //Si no hay un test completo borramos los (posibles) registros anteriores
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);
                    dataBaseHelper.borrarPosiblesRespuestas();
                }
                break;

            case 11:    //Tabla vida. Que avise si no tiene vicios
                if (sinVicios()) {
                    layoutBasico.crearMensajeSinViciosEnTablaVida();
                    return false;
                }
                break;
            case 108:case 109:case 110:case 111:case 112:case 113:case 114:case 115:case 116:case 117:
            case 118:case 119:case 120:case 121:case 122:case 123:case 124:case 125:case 126:case 127:
            case 128:case 129:case 130:case 131:case 132:case 133:case 134:case 135:case 136:case 137:
            case 138:case 139:
                DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);
                Spinner spinner = (Spinner) activity.findViewById(3);
                String respuestaVeces = spinner.getSelectedItem().toString();
                //Casos especiales, para evitar un problema al comparar valores
                if (respuestaVeces.equals(contexto.getResources().getText(R.string.numero_51_99).toString())) {
                    respuestaVeces = "51";
                }
                if (respuestaVeces.equals(contexto.getResources().getText(R.string.numero_100_ó_más).toString())) {
                    respuestaVeces = "100";
                }
                int veces1Mes = Integer.parseInt(respuestaVeces);
                if (dataBaseHelper.masVeces1MesQueDurante12Meses(item.getIdPregunta(), veces1Mes)) {
                    layoutBasico.crearMensajeAlert12Veces(R.string.layoutBasico_mensajeAlerta12meses);
                    return false;
                } else {
                    return true;
                }
            //Hora y minutos: no puede ser '0'
            case 140:case 141:case 142:case 143:case 144:case 145:case 146:case 147:case 148:case 149:
            case 150:case 151:case 152:case 153:case 154:case 155:case 156:case 157:case 158:case 159:
            case 160:case 161:case 162:case 163:case 164:case 165:case 166:case 167:case 168:case 169:
            case 170:case 171:
                Spinner hora = (Spinner) activity.findViewById(3);
                Spinner minutos = (Spinner) activity.findViewById(55);
                if (hora.getSelectedItem().toString().equals("00") && minutos.getSelectedItem().toString().equals("00")) {
                    VariablesGlobales.PublicToast(contexto, contexto.getResources().getText(R.string.error_TiempoMayorQueCero).toString());
                    return false;
                }
                break;
            case 257:case 258:case 259:     //acontecimientos
                EditText etAcontecimiento = (EditText) activity.findViewById(3);
                if (etAcontecimiento.length() < 3) {
                    VariablesGlobales.PublicToast(contexto, contexto.getResources().getText(R.string.error_TextoMinimo3caracteres).toString());
                    return false;
                }
        }

        for (int numeroRespuesta=0; numeroRespuesta<numeroRespuestas; numeroRespuesta++) {
            //1-Contadores | 2-RadioButton | 3-Fecha | 4-TextView | 5-CheckBox
            switch (id_pregunta_tipo) {
                case 2: //RadioButton
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        VariablesGlobales.PublicToast(contexto, contexto.getResources().getText(R.string.error_RadioVacio).toString());
                        return false;
                    } else {
                        return true;
                    }
            }
        }
        return true;    //Si es de tipo fecha siempre coge un valor por defecto
    }

    private static boolean sinVicios() {
        for (int i=11; i<43; i++) {
            CheckBox checkBox = (CheckBox) LayoutBasico.activity.findViewById(i);
            if (checkBox.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private static boolean hayUnTestRepetido() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(LayoutBasico.contexto);
        return dataBaseHelper.hayTestAnterior();
    }

    public static DatePicker calendarioSegunIdPregunta(DatePicker datePicker) {
        Calendar cal = Calendar.getInstance();  //fecha de hoy
        switch (LayoutBasico.idPregunta) {
            case 3:     //fecha de nacimiento en relación a la edad que metió en la pregunta anterior
                cal.set(datePicker.getYear() - Logica.USUARIO_EDAD, datePicker.getMonth(), datePicker.getDayOfMonth());
                long fechaMinima = cal.getTimeInMillis();
                long fechaMaxima = fechaMinima + 31449600000L;  //364 días
                datePicker.setMinDate(fechaMinima);
                datePicker.setMaxDate(fechaMaxima);
                return datePicker;
            //Calendarios "de apoyo" a los acontecimientos
            case 257:   //un mes atrás
                cal.set(datePicker.getYear(), datePicker.getMonth()-1, datePicker.getDayOfMonth());
                break;
            case 258:   //tres meses atrás
                cal.set(datePicker.getYear(), datePicker.getMonth()-3, datePicker.getDayOfMonth());
                break;
            case 259:   //un año atrás
                cal.set(datePicker.getYear()-1, datePicker.getMonth(), datePicker.getDayOfMonth());
                break;
        }
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        return datePicker;
    }
}
