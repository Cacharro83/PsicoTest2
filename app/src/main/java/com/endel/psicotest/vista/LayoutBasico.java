package com.endel.psicotest.vista;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.InputType;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.endel.psicotest.Logica;
import com.endel.psicotest.R;
import com.endel.psicotest.vo.Item;
import com.endel.psicotest.VariablesGlobales;
import com.endel.psicotest.baseDatos.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;


/**
 * Created by JavierH on 25/08/2015.
 */
public class LayoutBasico {
    public static final int COLOR_RESPUESTA = Color.BLUE, TAMANO_RESPUESTA = 20, TAMANO_COMBO = 114;
    public int id_actual = 1, id_anterior = 1, siguiente, contadorIDsTablaVida = 11, idUsuario;
    public static int idPregunta, idPreguntaAnterior;
    public boolean algunVicio = false;
    public static RelativeLayout relativeLayout;
    public RadioGroup radioGroup;
    public static Context contexto;
    public static Activity activity;
    public List<RespuestaValor> listaRespuestasRadioButton = new ArrayList();
    public static HashMap<Integer, Integer> mapaRespuestasTablaVida = new HashMap<>();

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

        //1-Contadores | 2-RadioButton | 3-Fecha | 4-TextView | 5-CheckBox
        int id_pregunta_tipo = item.getIdTipo();
        radioGroup = new RadioGroup(contexto);
        for(int numeroRespuesta=0; numeroRespuesta<numeroRespuestas; numeroRespuesta++) {
            siguiente = item.getRespuestas().get(numeroRespuesta).getSiguiente();

            switch (id_pregunta_tipo) {
                case 1: //Contador
                    pintarCombo(idPregunta);
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

    private void pintarCombo(int idPregunta) {
        RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);   //no quitar, casca
        Spinner spinner = new Spinner(contexto);
        int valorMinimo, valorMaximo;

        if (Logica.esHoraYMinutos(idPregunta)) {
            //Si vamos a introducir horas y minutos el primer spinner es para las horas
            valorMinimo = 0;
            valorMaximo = 23;
        } else if (Logica.esEdad(idPregunta)){
            //Edades
            valorMinimo = 14;
            valorMaximo = 18;

            //Si son preguntas "del pasado"
            if (idPregunta>=43 && idPregunta<=74) {
                valorMinimo = 11;
                valorMaximo = Logica.USUARIO_EDAD;
            }
        } else {
            if (Logica.esDinero(idPregunta)) {
                valorMinimo = 1;
            } else {
                valorMinimo = 0;    //Opciones para veces que has hecho algo, casos generales
            }
            valorMaximo = 50;
        }

        String[] valores = rellenarSpinner(valorMinimo, valorMaximo, idPregunta);
        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        id_actual = id_anterior + 1;

        AdaptadorPersonalizado<CharSequence> adapter = new AdaptadorPersonalizado<CharSequence>(contexto, simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setMinimumWidth(TAMANO_COMBO);
        spinner.setId(id_actual);
        spinner.setAdapter(adapter);
        relativeLayout.addView(spinner, parametros);

        if (Logica.esHoraYMinutos(idPregunta)) {
            relativeLayout = insertarRestoDeHora(relativeLayout);
        }

        if (Logica.esDinero(idPregunta)) {
            relativeLayout = insertarEuros(relativeLayout);
        }
    }

    private RelativeLayout insertarRestoDeHora(RelativeLayout relativeLayout) {
        //Dos puntos
        id_actual++;id_anterior++;
        final RelativeLayout.LayoutParams parametrosDosPuntos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView tvDosPuntos = new TextView(contexto);
        tvDosPuntos.setId(44);
        tvDosPuntos.setText(":");
        tvDosPuntos.setTextColor(COLOR_RESPUESTA);
        tvDosPuntos.setTextSize(33);
        parametrosDosPuntos.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER, 3);
        parametrosDosPuntos.addRule(RelativeLayout.RIGHT_OF, 3);
        id_actual++;id_anterior++;
        relativeLayout.addView(tvDosPuntos, parametrosDosPuntos);

        //Spinner de los minutos
        RelativeLayout.LayoutParams parametrosMinutos = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);   //no quitar, casca
        Spinner spinnerMinutos = new Spinner(contexto);
        spinnerMinutos.setId(55);
        int valorMinimo=0, valorMaximo=59;
        String[] valores = rellenarSpinner(valorMinimo, valorMaximo, idPregunta);
        parametrosMinutos.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER, 44);
        parametrosMinutos.addRule(RelativeLayout.RIGHT_OF, 44);
        AdaptadorPersonalizado<CharSequence> adapter = new AdaptadorPersonalizado<CharSequence>(contexto, simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMinutos.setMinimumWidth(TAMANO_COMBO);
        spinnerMinutos.setAdapter(adapter);
        id_actual++;id_anterior++;
        relativeLayout.addView(spinnerMinutos, parametrosMinutos);

        //Leyenda
        final RelativeLayout.LayoutParams parametrosLeyenda = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        TextView tvLeyenda = new TextView(contexto);
        tvLeyenda.setId(66);
        tvLeyenda.setText("(horas y minutos)");
        tvLeyenda.setTextColor(COLOR_RESPUESTA);
        tvLeyenda.setTextSize(TAMANO_RESPUESTA);
        parametrosLeyenda.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER, 55);
        parametrosLeyenda.addRule(RelativeLayout.RIGHT_OF, 55);
        id_anterior=3;id_actual=3;  //hace referencia al campo de las horas
        relativeLayout.addView(tvLeyenda, parametrosLeyenda);

        return relativeLayout;
    }

    private RelativeLayout insertarEuros(RelativeLayout relativeLayout) {
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView tvEuros = new TextView(contexto);
        tvEuros.setId(44);
        tvEuros.setText("€");
        tvEuros.setTextColor(COLOR_RESPUESTA);
        tvEuros.setTextSize(33);
        parametros.addRule(RelativeLayout.TEXT_ALIGNMENT_CENTER, 3);
        parametros.addRule(RelativeLayout.RIGHT_OF, 3);
        relativeLayout.addView(tvEuros, parametros);
        id_anterior=3;id_actual=3;  //hace referencia al campo de las '€'
        return relativeLayout;
    }


    private String[] rellenarSpinner(int valorMinimo, int valorMaximo, int idPregunta) {
        int numeroCampos = (valorMaximo+1)-valorMinimo;
        if (valorMaximo == 50) {
            numeroCampos = numeroCampos + 2;
        }
        String[] camposSpinner = new String[numeroCampos];
        int indice = 0;
        for (int i=valorMinimo; i<=valorMaximo; i++) {
            if (Logica.esHoraYMinutos(idPregunta) && i<10) {
                camposSpinner[indice++] = "0" + String.valueOf(i);
            } else {
                if (Logica.esEdad(idPregunta) && i==11) {
                    camposSpinner[indice++] = String.valueOf(i) + " o menos";
                } else {
                    camposSpinner[indice++] = String.valueOf(i);
                }
            }
        }
        //Si puede llegar a 50 hay que añadir los casos especiales
        if (valorMaximo == 50) {
            camposSpinner[indice++] = contexto.getResources().getText(R.string.numero_51_99).toString();
            camposSpinner[indice++] = contexto.getResources().getText(R.string.numero_100_ó_más).toString();
        }

        return camposSpinner;
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
        TableRow fila = new TableRow(contexto);
        fila.setMinimumHeight(55);
        fila.addView(pintarCabeceraTablaVida(titulo, false), parametrosCeldaDoble);

        //Pintar celdas
        for (int i=0; i<4; i++) {
            fila.addView(pintarCelda(contexto), parametrosCelda);
        }
        return fila;
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
        datePicker = Logica.calendarioSegunIdPregunta(datePicker);
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
        //radioButton.setText("radioButton ID:" + id_actual + " VALOR: " + valor + " | " + item.getRespuestas().get(numeroRespuesta).getTextoRespuesta());
        radioButton.setText(item.getRespuestas().get(numeroRespuesta).getTextoRespuesta());
        radioButton.setTextSize(TAMANO_RESPUESTA);
        RespuestaValor respuestaValor = new RespuestaValor(radioButton.getId(), valor, siguiente);
        listaRespuestasRadioButton.add(respuestaValor);
        radioGroup.addView(radioButton, parametros);
    }

    private void pintarCheckBox() {
        final CheckBox checkBox = new CheckBox(contexto);
        final RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        id_actual = id_anterior + 1;
        checkBox.setId(id_actual);
        //checkBox.setText("cajita. ID:" + id_actual);
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
        if (item.getIdPregunta() == 999) {
            botonSiguiente.setText(contexto.getResources().getText(R.string.layoutBasico_botonSiguienteFIN));
        } else {
            botonSiguiente.setText(contexto.getResources().getText(R.string.layoutBasico_botonSiguiente));
        }
        botonSiguiente.setTextColor(Color.WHITE);
        botonSiguiente.setTextSize(TAMANO_RESPUESTA);
        botonSiguiente.setTypeface(null, Typeface.ITALIC);
        botonSiguiente.setLayoutParams(parametros);

        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Logica.validarRespuestas(item, contexto, activity, radioGroup)) {
                    algunVicio = Logica.grabarRespuestas(item, radioGroup, listaRespuestasRadioButton, contexto, activity, algunVicio);
                    pintarNuevaPregunta(item);
                }
                //Caso 'fin de test'
                if (item.getIdPregunta() == 999) {
                    System.exit(0);
                }
            }
        });

        relativeLayout.addView(botonSiguiente, parametros);
    }

    private void crearMensajeAlert(int layoutBasico_mensajeAlerta) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(layoutBasico_mensajeAlerta);
        alertDialogBuilder.setPositiveButton("Continuar", null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    public void crearMensajeAlert12Veces(int layoutBasico_mensajeAlerta) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(layoutBasico_mensajeAlerta);
        alertDialogBuilder.setPositiveButton("Cambiar respuesta de los 12 meses", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                relativeLayout = pintarVista(contexto, idPregunta - 33);
                ScrollView scrollView = new ScrollView(contexto);
                scrollView.addView(relativeLayout);
                activity.setContentView(scrollView);
            }
        });
        alertDialogBuilder.setNegativeButton("Cambiar respuesta del último mes", null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    public void crearMensajeUsuarioDuplicado() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(R.string.layoutBasico_mensajeAlertaUsuarioDuplicado);
        alertDialogBuilder.setPositiveButton("Borrar test anterior", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);
                dataBaseHelper.borrarPosiblesRespuestas();
                VariablesGlobales.PublicToast(contexto, "Test anterior borrado");
            }
        });
        alertDialogBuilder.setNegativeButton("Introducir otro código", null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    public void crearMensajeSinViciosEnTablaVida() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(R.string.layoutBasico_mensajeAlertaSinVicios);
        alertDialogBuilder.setPositiveButton("Sí, estoy seguro", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                LayoutBasico layoutBasico = new LayoutBasico(activity);
                relativeLayout = layoutBasico.pintarVista(contexto, 107);
                ScrollView scrollView = new ScrollView(contexto);
                scrollView.addView(relativeLayout);
                activity.setContentView(scrollView);
            }
        });
        alertDialogBuilder.setNegativeButton("No, quiero modificar la tabla", null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }


    private void pintarNuevaPregunta(Item item) {
        LayoutBasico layoutBasico = new LayoutBasico(activity);

        //Casos especiales
        siguiente = Logica.averiguarSiguiente(item, siguiente, algunVicio, contexto, activity.findViewById(3));
        if(siguiente == 220) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta);
        }
        if(siguiente == 231) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta);
        }
        if(siguiente == 241) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta2);
        }
        if(siguiente == 257) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta3);
        }
        if(siguiente == 260) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta5);
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta4);
        }
        if(siguiente == 266) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta6);
        }
        if(siguiente == 273) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta7);
        }
        if(siguiente == 278) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta7);
        }
        if(siguiente == 999) {
            crearMensajeAlert(R.string.layoutBasico_mensajeAlerta8);
        }

        relativeLayout = layoutBasico.pintarVista(contexto, siguiente);
        ScrollView scrollView = new ScrollView(contexto);
        scrollView.addView(relativeLayout);
        activity.setContentView(scrollView);
    }

    private void pintarPregunta(Item item) {
        TextView pregunta = new TextView(contexto);
        idPreguntaAnterior = idPregunta;
        idPregunta = item.getIdPregunta();
        pregunta.setId(id_actual);
        //pregunta.setText(idPregunta + ") " + item.getTextoPregunta());
        pregunta.setText(item.getTextoPregunta());
        pregunta.setTextColor(Color.BLACK);
        pregunta.setTextSize(30);
        pregunta.setTypeface(null, Typeface.BOLD);
        relativeLayout.addView(pregunta);
    }

    private void pintarCajaTexto() {
        RelativeLayout.LayoutParams parametros = new RelativeLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);   //no quitar, casca
        final EditText editText = new EditText(contexto);
        if (idPregunta == 0) {
            editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);  //sólo números
        }
        parametros.addRule(RelativeLayout.BELOW, id_anterior);
        id_actual = id_anterior + 1;
        editText.setId(id_actual);
        editText.setTextColor(COLOR_RESPUESTA);
        editText.setTextSize(TAMANO_RESPUESTA);
        editText.setBackgroundColor(Color.WHITE);
        editText.setTypeface(null, Typeface.ITALIC);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});    //límite de caracteres
        editText.setLayoutParams(parametros);
        relativeLayout.addView(editText, parametros);
        //En el caso de ser 257-259 se pinta un calendario debajo como "guía", sin funcionalidad
        if (idPregunta>=257 && idPregunta<=259) {
            id_actual++;id_anterior++;
            pintarFecha();
        }
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
            //vicios
            celda.setTextSize(23);
        }
        celda.setText(texto);
        return celda;
    }
}
