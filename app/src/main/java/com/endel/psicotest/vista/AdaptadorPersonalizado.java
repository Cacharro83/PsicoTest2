package com.endel.psicotest.vista;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by JavierH on 11/09/2015.
 *
 * Clase para poner estilos a los 'spinner'
 */
public class AdaptadorPersonalizado<T> extends ArrayAdapter<T> {
    public AdaptadorPersonalizado(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(30);
            ((TextView) view).setTextColor(LayoutBasico.COLOR_RESPUESTA);
        }
        return view;
    }
}