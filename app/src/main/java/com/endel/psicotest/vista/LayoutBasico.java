package com.endel.psicotest.vista;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
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
    public int id_actual = 1, id_anterior = 1, siguiente;
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

        DataBaseHelper myDbHelper = new DataBaseHelper(contexto);
        Item item = myDbHelper.GetItemId(idPregunta);
        myDbHelper.close();



        relativeLayout = new RelativeLayout(contexto);
        relativeLayout.setPadding(10, 20, 10, 20);
        relativeLayout.setBackgroundColor(Color.LTGRAY);

        //id_actual = 1;

        //Pregunta
        pintarPregunta(item);



        //Separador
        pintarSeparador();

        pintarRespuestas(item);
        //pintarTablaPrueba();



        //pintarTablaVida();


        //Separador
        pintarSeparador();

        //Siguiente
        pintarBotonSiguiente();


        return relativeLayout;
    }

    private void pintarTablaPrueba() {
        TableLayout tableLayout = new TableLayout(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);


        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        id_actual = ++id_anterior;
        tableLayout.setId(id_actual);

        //Fila 1
        TableRow tableRow1 = new TableRow(contexto);
        tableRow1.setLayoutParams(tableRowParams);
        TextView texto1A = new TextView(contexto);
        texto1A.setText("-");
        texto1A.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1A);
        TextView texto1B = new TextView(contexto);
        texto1B.setText("PRESENCIAL");
        texto1B.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1B);
        tableLayout.addView(tableRow1);

        relativeLayout.addView(tableLayout, parametros);
    }

    private void pintarRespuestas(Item item) {

        //Sacamos las respuestas por el ID de la pregunta
        int idPregunta = item.getIdPregunta();

        int numeroRespuestas = item.getRespuestas().size();


        //Respuestas
        //1.texto | 2.checkbox | 3.radiobutton | 4.fecha
        int id_pregunta_tipo = 3;
        radioGroup = new RadioGroup(contexto);
        for(int numeroRespuesta=0; numeroRespuesta<numeroRespuestas; numeroRespuesta++)
        {
            switch (id_pregunta_tipo) {
                case 1: //Caja de texto
                    pintarCajaTexto();
                    break;
                case 2: //Checkbox
                    pintarCheckBox();
                    break;
                case 3: //Radiobutton
                    pintarRadioButton(numeroRespuesta, item);
                    break;
                case 4: //Fecha
                    pintarFecha();
                    break;

            }
            id_anterior = id_actual;
        }

        //Si son unos radio buttons al final hay que añadir el radioGroup
        if (id_pregunta_tipo == 3) {
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
        TableLayout tableLayout = new TableLayout(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams (TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(3, 3, 2, 10);
        //tableRow.LayoutParameters = tableRowParams;

        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        id_actual = ++id_anterior;
        tableLayout.setId(id_actual);

        //Fila 1
        TableRow tableRow1 = new TableRow(contexto);
        tableRow1.setLayoutParams(tableRowParams);
        TextView texto1A = new TextView(contexto);
        texto1A.setText("-");
        texto1A.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1A);
        TextView texto1B = new TextView(contexto);
        texto1B.setText("PRESENCIAL");
        texto1B.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1B);
        TextView texto1C = new TextView(contexto);
        texto1C.setText("-");
        texto1C.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1C);
        TextView texto1D = new TextView(contexto);
        texto1D.setText("ONLINE");
        texto1D.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1D);
        TextView texto1E = new TextView(contexto);
        texto1E.setText("-");
        texto1E.setTextColor(COLOR_RESPUESTA);
        tableRow1.addView(texto1E);
        tableLayout.addView(tableRow1);

        //Fila 2
        TableRow tableRow2 = new TableRow(contexto);
        TextView tableRow2A = new TextView(contexto);
        tableRow2A.setText("-");
        tableRow2A.setTextColor(COLOR_RESPUESTA);
        tableRow2.addView(tableRow2A);
        TextView tableRow2B = new TextView(contexto);
        tableRow2B.setText("  CON DINERO  ");
        tableRow2B.setTextColor(COLOR_RESPUESTA);
        tableRow2.addView(tableRow2B);
        TextView tableRow2C = new TextView(contexto);
        tableRow2C.setText("  SIN DINERO  ");
        tableRow2C.setTextColor(COLOR_RESPUESTA);
        tableRow2.addView(tableRow2C);
        TextView tableRow2D = new TextView(contexto);
        tableRow2D.setText("  CON DINERO  ");
        tableRow2D.setTextColor(COLOR_RESPUESTA);
        tableRow2.addView(tableRow2D);
        TextView tableRow2E = new TextView(contexto);
        tableRow2E.setText("  SIN DINERO  ");
        tableRow2E.setTextColor(COLOR_RESPUESTA);
        tableRow2.addView(tableRow2E);
        tableLayout.addView(tableRow2);

        //Fila 3
        TableRow tableRow3 = new TableRow(contexto);
        TextView tableRow3A = new TextView(contexto);
        tableRow3A.setText("Bingo");
        tableRow3A.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(tableRow3A);
        CheckBox tableRow3B = new CheckBox(contexto);
        tableRow3B.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(tableRow3B);
        CheckBox tableRow3C = new CheckBox(contexto);
        tableRow3C.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(tableRow3C);
        CheckBox tableRow3D = new CheckBox(contexto);
        tableRow3D.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(tableRow3D);
        CheckBox tableRow3E = new CheckBox(contexto);
        tableRow3E.setTextColor(COLOR_RESPUESTA);
        tableRow3.addView(tableRow3E);
        tableLayout.addView(tableRow3);

        //Fila 4
        TableRow tableRow4 = new TableRow(contexto);
        TextView tableRow4A = new TextView(contexto);
        tableRow4A.setText("Keno");
        tableRow4A.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(tableRow4A);
        CheckBox tableRow4B = new CheckBox(contexto);
        tableRow4B.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(tableRow4B);
        CheckBox tableRow4C = new CheckBox(contexto);
        tableRow4C.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(tableRow4C);
        CheckBox tableRow4D = new CheckBox(contexto);
        tableRow4D.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(tableRow4D);
        CheckBox tableRow4E = new CheckBox(contexto);
        tableRow4E.setTextColor(COLOR_RESPUESTA);
        tableRow4.addView(tableRow4E);
        tableLayout.addView(tableRow4);


        //Fila 5
        TableRow tableRow5 = new TableRow(contexto);
        TextView tableRow5A = new TextView(contexto);
        tableRow5A.setText("Póker");
        tableRow5A.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(tableRow5A);
        CheckBox tableRow5B = new CheckBox(contexto);
        tableRow5B.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(tableRow5B);
        CheckBox tableRow5C = new CheckBox(contexto);
        tableRow5C.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(tableRow5C);
        CheckBox tableRow5D = new CheckBox(contexto);
        tableRow5D.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(tableRow5D);
        CheckBox tableRow5E = new CheckBox(contexto);
        tableRow5E.setTextColor(COLOR_RESPUESTA);
        tableRow5.addView(tableRow5E);
        tableLayout.addView(tableRow5);

        //Fila 6
        TableRow tableRow6 = new TableRow(contexto);
        TextView tableRow6A = new TextView(contexto);
        tableRow6A.setText("Juegos casino (sin incluir Póker)\n(p. ej.: ruleta, blackjack)");
        tableRow6A.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(tableRow6A);
        CheckBox tableRow6B = new CheckBox(contexto);
        tableRow6B.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(tableRow6B);
        CheckBox tableRow6C = new CheckBox(contexto);
        tableRow6C.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(tableRow6C);
        CheckBox tableRow6D = new CheckBox(contexto);
        tableRow6D.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(tableRow6D);
        CheckBox tableRow6E = new CheckBox(contexto);
        tableRow6E.setTextColor(COLOR_RESPUESTA);
        tableRow6.addView(tableRow6E);
        tableLayout.addView(tableRow6);


        //Fila 7
        TableRow tableRow7 = new TableRow(contexto);
        TextView tableRow7A = new TextView(contexto);
        tableRow7A.setText("Apuestas deportivas\n(p. ej.: fútbol, caballos)");
        tableRow7A.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(tableRow7A);
        CheckBox tableRow7B = new CheckBox(contexto);
        tableRow7B.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(tableRow7B);
        CheckBox tableRow7C = new CheckBox(contexto);
        tableRow7C.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(tableRow7C);
        CheckBox tableRow7D = new CheckBox(contexto);
        tableRow7D.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(tableRow7D);
        CheckBox tableRow7E = new CheckBox(contexto);
        tableRow7E.setTextColor(COLOR_RESPUESTA);
        tableRow7.addView(tableRow7E);
        tableLayout.addView(tableRow7);

        //Fila 8
        TableRow tableRow8 = new TableRow(contexto);
        TextView tableRow8A = new TextView(contexto);
        tableRow8A.setText("Loterías");
        tableRow8A.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(tableRow8A);
        CheckBox tableRow8B = new CheckBox(contexto);
        tableRow8B.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(tableRow8B);
        CheckBox tableRow8C = new CheckBox(contexto);
        tableRow8C.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(tableRow8C);
        CheckBox tableRow8D = new CheckBox(contexto);
        tableRow8D.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(tableRow8D);
        CheckBox tableRow8E = new CheckBox(contexto);
        tableRow8E.setTextColor(COLOR_RESPUESTA);
        tableRow8.addView(tableRow8E);
        tableLayout.addView(tableRow8);

        //Fila 9
        TableRow tableRow9 = new TableRow(contexto);
        TextView tableRow9A = new TextView(contexto);
        tableRow9A.setText("Rascas");
        tableRow9A.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(tableRow9A);
        CheckBox tableRow9B = new CheckBox(contexto);
        tableRow9B.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(tableRow9B);
        CheckBox tableRow9C = new CheckBox(contexto);
        tableRow9C.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(tableRow9C);
        CheckBox tableRow9D = new CheckBox(contexto);
        tableRow9D.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(tableRow9D);
        CheckBox tableRow9E = new CheckBox(contexto);
        tableRow9E.setTextColor(COLOR_RESPUESTA);
        tableRow9.addView(tableRow9E);
        tableLayout.addView(tableRow9);

        //Fila 10
        TableRow tableRow10 = new TableRow(contexto);
        TextView tableRow10A = new TextView(contexto);
        tableRow10A.setText("Máquinas tragaperras");
        tableRow10A.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(tableRow10A);
        CheckBox tableRow10B = new CheckBox(contexto);
        tableRow10B.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(tableRow10B);
        CheckBox tableRow10C = new CheckBox(contexto);
        tableRow10C.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(tableRow10C);
        CheckBox tableRow10D = new CheckBox(contexto);
        tableRow10D.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(tableRow10D);
        CheckBox tableRow10E = new CheckBox(contexto);
        tableRow10E.setTextColor(COLOR_RESPUESTA);
        tableRow10.addView(tableRow10E);
        tableLayout.addView(tableRow10);




        relativeLayout.addView(tableLayout, parametros);
    }

    private void pintarFecha() {
        DatePicker datePicker = new DatePicker(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        id_actual = id_anterior + 1;
        datePicker.setId(id_actual);
        //datePicker.setText("cajita. ID:" + id_actual);
        //datePicker.setTextColor(COLOR_RESPUESTA);
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
        siguiente = item.getRespuestas().get(numeroRespuesta).getSiguiente();
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

    private void pintarBotonSiguiente() {
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
                if (validarRespuestas()) {
                    grabarRespuestas();
                    pintarNuevaPregunta();
                }
            }
        });

        relativeLayout.addView(botonSiguiente, parametros);
    }

    private void pintarNuevaPregunta() {
        LayoutBasico layoutBasico = new LayoutBasico(activity);
        relativeLayout = layoutBasico.pintarVista(contexto, siguiente);
        ScrollView scrollView = new ScrollView(contexto);
        scrollView.addView(relativeLayout);
        activity.setContentView(scrollView);
    }

    private void grabarRespuestas() {
        int opcionEscogida = radioGroup.getCheckedRadioButtonId();

        //Cogemos su valor
        boolean buscando = true;
        for (int i=0; buscando; i++) {
            RespuestaValor respuestaValor = listaValorRespuestas.get(i);
            if (respuestaValor.getId() == opcionEscogida) {
                buscando = false;
                Toast.makeText(contexto, String.valueOf("ID: " + respuestaValor.getId() + " | VALOR: " + respuestaValor.getValor() + " | SIGUIENTE: " + respuestaValor.getSiguiente()), Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validarRespuestas(){
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(contexto, "Debes escoger una de las opciones", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void pintarPregunta(Item item) {
        TextView pregunta = new TextView(contexto);
        pregunta.setId(id_actual);
        pregunta.setText(item.getTextoPregunta());
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
}
