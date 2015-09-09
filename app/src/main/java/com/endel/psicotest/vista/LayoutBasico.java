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

import com.endel.psicotest.Logica;
import com.endel.psicotest.R;
import com.endel.psicotest.Item;
import com.endel.psicotest.baseDatos.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by JavierH on 25/08/2015.
 */
public class LayoutBasico {
    public final int COLOR_RESPUESTA = Color.BLUE;
    public int id_actual = 1, id_anterior = 1, siguiente, idPregunta, contadorIDsTablaVida = 11, idUsuario;
    public boolean algunVicio = false;
    public RelativeLayout relativeLayout;
    public RadioGroup radioGroup;
    public Context contexto;
    public Activity activity;
    public List<RespuestaValor> listaRespuestasRadioButton = new ArrayList();
    public HashMap<Integer, Integer> mapaRespuestasTablaVida = new HashMap<>();

    RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    final TableRow.LayoutParams parametrosCelda = new TableRow.LayoutParams(0, AbsListView.LayoutParams.WRAP_CONTENT, 1); //ancho normal
    final TableRow.LayoutParams parametrosCeldaDoble = new TableRow.LayoutParams(0, AbsListView.LayoutParams.WRAP_CONTENT, 2); //ancho doble

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


    private void pintarRespuestas(Item item) {
        //Sacamos las respuestas por el ID de la pregunta
        idPregunta = item.getIdPregunta();

        int numeroRespuestas = item.getRespuestas().size();

        //Respuestas
        //1-Contadores | 2-RadioButton | 3-Fecha | 4-TextView | 5-CheckBox
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

        //Pintamos las celdas
        String[] listaVicios = {"Bingo", "Keno", "Póker", "Juegos casino (sin incluir Póker) (p. ej.: ruleta, blackjack)", "Apuestas deportivas (p. ej.: fútbol, caballos)", "Loterías", "Rascas", "Máquinas tragaperras"};
        for (int i=0; i<listaVicios.length; i++) {
            tableLayout.addView(pintarFila(listaVicios[i]));
        }

        relativeLayout.addView(tableLayout, parametros);
    }

    private TableRow pintarFila(String titulo) {
        TableRow tableRow3 = new TableRow(contexto);
        tableRow3.addView(pintarCabeceraTablaVida(titulo, false), parametrosCeldaDoble);

        //Pintar celdas
        for (int i=0; i<4; i++) {
            tableRow3.addView(pintarCelda(contexto), parametrosCelda);
        }
        return tableRow3;
    }

    private CheckBox pintarCelda(Context contexto) {
        CheckBox celda = new CheckBox(contexto);
        celda.setId(contadorIDsTablaVida++);
        celda.setTextColor(COLOR_RESPUESTA);
        return celda;
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
        parametros.addRule(RelativeLayout.BELOW, 1);
        final RadioButton radioButton = new RadioButton(contexto);
        radioButton.setTextColor(COLOR_RESPUESTA);
        id_actual = id_anterior + 1;
        radioButton.setId(id_actual + 100); //para que no solape con el 'Siguiente' y se trabe el radioGroup
        float valor = item.getRespuestas().get(numeroRespuesta).getValor();
        radioButton.setText("radioButton ID:" + id_actual + " VALOR: " + valor + " | " + item.getRespuestas().get(numeroRespuesta).getTextoRespuesta());

        RespuestaValor respuestaValor = new RespuestaValor(radioButton.getId(), valor, siguiente);

        listaRespuestasRadioButton.add(respuestaValor);

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
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //Aunque se repita no quitar pues se desmaqueta completamente
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
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT); //no quitar, casca
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
                if (Logica.validarRespuestas(item, activity, contexto, radioGroup)) {
                    algunVicio = Logica.grabarRespuestas(item, radioGroup, listaRespuestasRadioButton, contexto, activity, algunVicio, mapaRespuestasTablaVida);
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

        //Casos especiales
        siguiente = Logica.averiguarSiguiente(item, mapaRespuestasTablaVida, siguiente, algunVicio);

        relativeLayout = layoutBasico.pintarVista(contexto, siguiente);
        ScrollView scrollView = new ScrollView(contexto);
        scrollView.addView(relativeLayout);
        activity.setContentView(scrollView);
    }

    private void pintarPregunta(Item item) {
        TextView pregunta = new TextView(contexto);
        pregunta.setId(id_actual);
        pregunta.setText(item.getIdPregunta() + ") " + item.getTextoPregunta() + " | tipoPregunta: " + item.getIdTipo());
        pregunta.setTextColor(Color.BLACK);
        pregunta.setTextSize(30);
        pregunta.setTypeface(null, Typeface.BOLD);
        relativeLayout.addView(pregunta);
    }

    private void pintarCajaTexto() {
        RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);   //no quitar, casca
        final EditText editText = new EditText(contexto);
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
