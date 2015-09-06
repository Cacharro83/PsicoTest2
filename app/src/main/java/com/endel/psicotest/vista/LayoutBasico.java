package com.endel.psicotest.vista;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.endel.psicotest.R;
import com.endel.psicotest.Item;
import com.endel.psicotest.baseDatos.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by JavierH on 25/08/2015.
 */
public class LayoutBasico {
    public final int COLOR_RESPUESTA = Color.BLUE;
    public int id_actual = 1, id_anterior = 1, siguiente, idPregunta;
    public RelativeLayout relativeLayout;
    public RadioGroup radioGroup;
    public Context contexto;
    public Activity activity;
    public List<RespuestaValor> listaValorRespuestas = new ArrayList();

    public LayoutBasico (Activity _activity) {
        this.activity = _activity;
    }


    public RelativeLayout pintarVista(Context contexto, int idPregunta) {
        this.contexto = contexto;
        relativeLayout = new RelativeLayout(contexto);
        relativeLayout.setPadding(10, 20, 10, 20);
        relativeLayout.setBackgroundColor(Color.LTGRAY);

        DataBaseHelper myDbHelper = new DataBaseHelper(contexto);
        Item item = myDbHelper.GetItemId(idPregunta);
        myDbHelper.close();

        //Pregunta
        pintarPregunta(item);

        //Separador
        pintarSeparador();

        //Casos especiales de redireccionamiento
        switch (item.getIdPregunta()) {
            case 11:    //Tabla vida
                pintarTablaVida();
                valorarRespuestasTablaVida();
                siguiente = 257;
                break;
            default:
                pintarRespuestas(item);
                break;
        }

        //Separador
        pintarSeparador();

        //Siguiente
        pintarBotonSiguiente(item);

        return relativeLayout;
    }

    private void valorarRespuestasTablaVida() {
    }


    private void pintarRespuestas(Item item) {
        //Sacamos las respuestas por el ID de la pregunta
        idPregunta = item.getIdPregunta();

        int numeroRespuestas = item.getRespuestas().size();

        //Respuestas
        //1-Contadores | 2-RadioButton | 3-Fecha | 4-TextView | 5-CheckBox
        // ANTES 1.texto | 2.checkbox | 3.radiobutton | 4.fecha
        int id_pregunta_tipo = item.getIdTipo();
        radioGroup = new RadioGroup(contexto);
        for(int numeroRespuesta=0; numeroRespuesta<numeroRespuestas; numeroRespuesta++) {
            siguiente = item.getRespuestas().get(numeroRespuesta).getSiguiente();

            switch (id_pregunta_tipo) {
                case 1: //Contador
                    pintarCajaTexto();
                    break;
                case 2: //Radiobutton
                    pintarRadioButton(numeroRespuesta, item);   //'item' necesario para obtener los valores
                    break;
                case 3: //Fecha
                    pintarFecha();
                    break;
                case 4: //Caja de texto
                    pintarCajaTexto();
                    break;
                case 5: //Checkbox
                    pintarCheckBox();
                    break;
            }
            id_anterior = id_actual;
        }

        //Si son unos radio buttons al final hay que añadir el radioGroup
        if (id_pregunta_tipo == 2) {
            final RelativeLayout.LayoutParams parametrosRadioButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            id_anterior = 2;
            parametrosRadioButton.addRule(RelativeLayout.BELOW, id_anterior);
            id_actual = id_anterior + 1;
            radioGroup.setId(id_actual);
            relativeLayout.addView(radioGroup, parametrosRadioButton);
            id_anterior = id_actual;
        }
    }

    private void pintarTablaVida() {
        //borde de una celda (ojo, en la tablet se rellenan de negro porque sí, sólo aplicado a 'presencial' y 'online'
        final GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);          //esquinas
        gd.setStroke(1, Color.WHITE);   //borde

        TableLayout tableLayout = new TableLayout(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final TableRow.LayoutParams parametrosCelda = new TableRow.LayoutParams(0, AbsListView.LayoutParams.WRAP_CONTENT, 1); //ancho normal
        final TableRow.LayoutParams parametrosCeldaDoble = new TableRow.LayoutParams(0, AbsListView.LayoutParams.WRAP_CONTENT, 2); //ancho doble

        //no los pilla
        parametrosCeldaDoble.gravity = Gravity.CENTER_HORIZONTAL;
        parametrosCeldaDoble.gravity = Gravity.CENTER_VERTICAL;

        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        id_actual = ++id_anterior;
        tableLayout.setId(id_actual);

        //Fila 1
        TableRow tableRow1 = new TableRow(contexto);
        tableRow1.setLayoutParams(parametros);


        TextView celda1A = new TextView(contexto);
        tableRow1.addView(celda1A, parametrosCeldaDoble);
        tableRow1.addView(pintarCabeceraPrincipalTablaVida("PRESENCIAL"), parametrosCeldaDoble);
        tableRow1.addView(pintarCabeceraPrincipalTablaVida("ONLINE"), parametrosCeldaDoble);
        tableLayout.addView(tableRow1);

        //Fila 2
        TableRow tableRow2 = new TableRow(contexto);
        tableRow2.addView(pintarCabeceraTablaVida("", false), parametrosCeldaDoble);
        tableRow2.addView(pintarCabeceraTablaVida("CON DINERO", true), parametrosCelda);
        tableRow2.addView(pintarCabeceraTablaVida("SIN DINERO", true), parametrosCelda);
        tableRow2.addView(pintarCabeceraTablaVida("CON DINERO", true), parametrosCelda);
        tableRow2.addView(pintarCabeceraTablaVida("SIN DINERO", true), parametrosCelda);
        tableLayout.addView(tableRow2);

        //Fila 3
        TableRow tableRow3 = new TableRow(contexto);
        tableRow3.addView(pintarCabeceraTablaVida("Bingo", false), parametrosCeldaDoble);
        CheckBox celda3B = new CheckBox(contexto);
        celda3B.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(celda3B, parametrosCelda);
        CheckBox celda3C = new CheckBox(contexto);
        celda3C.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(celda3C, parametrosCelda);
        CheckBox celda3D = new CheckBox(contexto);
        celda3D.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(celda3D, parametrosCelda);
        CheckBox celda3E = new CheckBox(contexto);
        celda3E.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(celda3E, parametrosCelda);
        tableLayout.addView(tableRow3);

        //Fila 4
        TableRow tableRow4 = new TableRow(contexto);
        tableRow4.addView(pintarCabeceraTablaVida("Keno", false), parametrosCeldaDoble);
        CheckBox celda4B = new CheckBox(contexto);
        celda4B.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(celda4B, parametrosCelda);
        CheckBox celda4C = new CheckBox(contexto);
        celda4C.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(celda4C, parametrosCelda);
        CheckBox celda4D = new CheckBox(contexto);
        celda4D.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(celda4D, parametrosCelda);
        CheckBox celda4E = new CheckBox(contexto);
        celda4E.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(celda4E, parametrosCelda);
        tableLayout.addView(tableRow4);

        //Fila 5
        TableRow tableRow5 = new TableRow(contexto);
        tableRow5.addView(pintarCabeceraTablaVida("Póker", false), parametrosCeldaDoble);
        CheckBox celda5B = new CheckBox(contexto);
        celda5B.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(celda5B, parametrosCelda);
        CheckBox celda5C = new CheckBox(contexto);
        celda5C.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(celda5C, parametrosCelda);
        CheckBox celda5D = new CheckBox(contexto);
        celda5D.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(celda5D, parametrosCelda);
        CheckBox celda5E = new CheckBox(contexto);
        celda5E.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(celda5E, parametrosCelda);
        tableLayout.addView(tableRow5);

        //Fila 6
        TableRow tableRow6 = new TableRow(contexto);
        tableRow6.addView(pintarCabeceraTablaVida("Juegos casino (sin incluir Póker) (p. ej.: ruleta, blackjack)", false), parametrosCeldaDoble);

        CheckBox celda6B = new CheckBox(contexto);
        celda6B.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(celda6B, parametrosCelda);
        CheckBox celda6C = new CheckBox(contexto);
        celda6C.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(celda6C, parametrosCelda);
        CheckBox celda6D = new CheckBox(contexto);
        celda6D.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(celda6D, parametrosCelda);
        CheckBox celda6E = new CheckBox(contexto);
        celda6E.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(celda6E, parametrosCelda);
        tableLayout.addView(tableRow6);

        //Fila 7
        TableRow tableRow7 = new TableRow(contexto);
        tableRow7.addView(pintarCabeceraTablaVida("Apuestas deportivas (p. ej.: fútbol, caballos)", false), parametrosCeldaDoble);
        CheckBox celda7B = new CheckBox(contexto);
        celda7B.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(celda7B, parametrosCelda);
        CheckBox celda7C = new CheckBox(contexto);
        celda7C.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(celda7C, parametrosCelda);
        CheckBox celda7D = new CheckBox(contexto);
        celda7D.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(celda7D, parametrosCelda);
        CheckBox celda7E = new CheckBox(contexto);
        celda7E.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(celda7E, parametrosCelda);
        tableLayout.addView(tableRow7);

        //Fila 8
        TableRow tableRow8 = new TableRow(contexto);
        tableRow8.addView(pintarCabeceraTablaVida("Loterías", false), parametrosCeldaDoble);
        CheckBox celda8B = new CheckBox(contexto);
        celda8B.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(celda8B, parametrosCelda);
        CheckBox celda8C = new CheckBox(contexto);
        celda8C.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(celda8C, parametrosCelda);
        CheckBox celda8D = new CheckBox(contexto);
        celda8D.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(celda8D, parametrosCelda);
        CheckBox celda8E = new CheckBox(contexto);
        celda8E.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(celda8E, parametrosCelda);
        tableLayout.addView(tableRow8);

        //Fila 9
        TableRow tableRow9 = new TableRow(contexto);
        tableRow9.addView(pintarCabeceraTablaVida("Rascas", false), parametrosCeldaDoble);
        CheckBox celda9B = new CheckBox(contexto);
        celda9B.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(celda9B, parametrosCelda);
        CheckBox celda9C = new CheckBox(contexto);
        celda9C.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(celda9C, parametrosCelda);
        CheckBox celda9D = new CheckBox(contexto);
        celda9D.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(celda9D, parametrosCelda);
        CheckBox celda9E = new CheckBox(contexto);
        celda9E.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(celda9E, parametrosCelda);
        tableLayout.addView(tableRow9);

        //Fila 10
        TableRow tableRow10 = new TableRow(contexto);
        tableRow10.addView(pintarCabeceraTablaVida("Máquinas tragaperras", false), parametrosCeldaDoble);
        CheckBox celda10B = new CheckBox(contexto);
        celda10B.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(celda10B, parametrosCelda);
        CheckBox celda10C = new CheckBox(contexto);
        celda10C.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(celda10C, parametrosCelda);
        CheckBox celda10D = new CheckBox(contexto);
        celda10D.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(celda10D, parametrosCelda);
        CheckBox celda10E = new CheckBox(contexto);
        celda10E.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(celda10E, parametrosCelda);
        tableLayout.addView(tableRow10);

        relativeLayout.addView(tableLayout, parametros);
    }


    private void pintarFecha() {
        DatePicker datePicker = new DatePicker(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        id_actual = id_anterior + 1;
        datePicker.setId(id_actual);
        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        relativeLayout.addView(datePicker, parametros);
    }

    private void pintarRadioButton(int numeroRespuesta, Item item) {
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parametros.addRule(RelativeLayout.BELOW, 1);
        final RadioButton radioButton = new RadioButton(contexto);
        radioButton.setTextColor(COLOR_RESPUESTA);
        id_actual = id_anterior + 1;
        radioButton.setId(id_actual + 100); //para que no solape con el 'Siguiente' y se trabe el radioGroup
        float valor = item.getRespuestas().get(numeroRespuesta).getValor();
        radioButton.setText("radioButton ID:" + id_actual + " VALOR: " + valor + " | " + item.getRespuestas().get(numeroRespuesta).getTextoRespuesta());

        RespuestaValor respuestaValor = new RespuestaValor(radioButton.getId(), valor, siguiente);

        listaValorRespuestas.add(respuestaValor);

        radioGroup.addView(radioButton, parametros);
    }

    private void pintarCheckBox() {
        final CheckBox checkBox = new CheckBox(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        id_actual = id_anterior + 1;
        checkBox.setId(id_actual);
        checkBox.setText("cajita. ID:" + id_actual);
        checkBox.setTextColor(COLOR_RESPUESTA);
        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        relativeLayout.addView(checkBox, parametros);
    }

    private void pintarSeparador() {
        View separador = new View(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        id_actual = ++id_anterior;
        separador.setId(id_actual);
        parametros.addRule(RelativeLayout.CENTER_HORIZONTAL);
        parametros.height = 1;
        parametros.setMargins(10, 20, 10, 20);
        separador.setLayoutParams(parametros);
        separador.setBackgroundColor(Color.DKGRAY);
        relativeLayout.addView(separador);
    }

    private void pintarBotonSiguiente(final Item item) {
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        final Button botonSiguiente = new Button(contexto);
        id_actual = ++id_anterior;
        botonSiguiente.setId(id_actual);
        botonSiguiente.setText(contexto.getResources().getText(R.string.layoutBasico_botonSiguiente) + Integer.toString(id_anterior));
        botonSiguiente.setTextColor(COLOR_RESPUESTA);
        botonSiguiente.setTextSize(20);
        botonSiguiente.setTypeface(null, Typeface.ITALIC);
        botonSiguiente.setLayoutParams(parametros);


        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarRespuestas(item)) {
                    grabarRespuestas(item);
                    pintarNuevaPregunta(item);
                }
            }
        });

        relativeLayout.addView(botonSiguiente, parametros);
    }

    private void pintarNuevaPregunta(Item item) {
        LayoutBasico layoutBasico = new LayoutBasico(activity);

        //Opción para desarrollo, por si no hay respuestas
        if (siguiente == 0) {
            siguiente = idPregunta + 1;
        }

        relativeLayout = layoutBasico.pintarVista(contexto, siguiente);
        ScrollView scrollView = new ScrollView(contexto);
        scrollView.addView(relativeLayout);
        activity.setContentView(scrollView);
    }

    private void grabarRespuestas(Item item) {
        switch (item.getIdTipo()) {
            case 1: //Contador
                break;
            case 2: //RadioButton
                int opcionEscogida = radioGroup.getCheckedRadioButtonId();

                //Cogemos su valor
                boolean buscando = true;
                for (int i=0; listaValorRespuestas.size()>0 && buscando; i++) {
                    RespuestaValor respuestaValor = listaValorRespuestas.get(i);
                    if (respuestaValor.getId() == opcionEscogida) {
                        buscando = false;
                        Toast.makeText(contexto, String.valueOf("ID: " + respuestaValor.getId() + " | VALOR: " + respuestaValor.getValor() + " | SIGUIENTE: " + respuestaValor.getSiguiente()), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }






    }

    private boolean validarRespuestas(Item item) {
        int id_pregunta_tipo = item.getIdTipo();
        int numeroRespuestas = item.getRespuestas().size();
        for(int numeroRespuesta=0; numeroRespuesta<numeroRespuestas; numeroRespuesta++) {
            //1-Contadores | 2-RadioButton | 3-Fecha | 4-TextView | 5-CheckBox
            switch (id_pregunta_tipo) {
                case 1: //Contador
                    TextView textView = (TextView) activity.findViewById(numeroRespuesta + 3);  //1 es pregunta y 2 es el separador
                    String valor = textView.getText().toString();

                    if (valor.equals("")) {
                        Toast.makeText(contexto, "Debes completar todos los campos", Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        return true;
                    }
                case 2: //RadioButton
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(contexto, "Debes escoger una de las opciones", Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        return true;
                    }
            }
        }
        return true;    //Si es de tipo fecha siempre coge un valor por defecto
    }

    private void pintarPregunta(Item item) {
        TextView pregunta = new TextView(contexto);
        pregunta.setId(id_actual);
        pregunta.setText(item.getIdPregunta() + ") | " + item.getTextoPregunta() + " | tipoPregunta: " + item.getIdTipo());
        pregunta.setTextColor(Color.BLACK);
        pregunta.setTextSize(30);
        pregunta.setTypeface(null, Typeface.BOLD);
        relativeLayout.addView(pregunta);
    }

    private void pintarCajaTexto() {
        final EditText editText = new EditText(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        parametros.addRule(RelativeLayout.BELOW, id_anterior);

        id_actual = id_anterior + 1;
        editText.setId(id_actual);
        editText.setText("Texto. ID: " + id_actual);

        editText.setTextColor(COLOR_RESPUESTA);
        editText.setTextSize(23);
        editText.setTypeface(null, Typeface.ITALIC);
        editText.setLayoutParams(parametros);
        relativeLayout.addView(editText, parametros);
    }

    private TextView pintarCabeceraPrincipalTablaVida(String texto) {
        final GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(1, 0xFF000000);

        texto = "\t\t\t\t\t\t\t" + texto;
        TextView celda = new TextView(contexto);
        celda.setTypeface(null, Typeface.BOLD_ITALIC);
        celda.setTextColor(Color.WHITE);
        celda.setBackgroundDrawable(gd);
        celda.setTextSize(28);
        celda.setText(texto);
        //celda.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); //para la tablet no lo pilla por su API vieja
        return celda;
    }

    private TextView pintarCabeceraTablaVida(String texto, boolean esCabecera) {
        TextView celda = new TextView(contexto);
        celda.setTypeface(null, Typeface.BOLD_ITALIC);
        celda.setTextColor(COLOR_RESPUESTA);

        //con/sin dinero
        if (esCabecera) {
            celda.setTextSize(23);
        } else {
            //ludopatías
            celda.setTextSize(16);
        }
        celda.setText(texto);
        return celda;
    }
}
