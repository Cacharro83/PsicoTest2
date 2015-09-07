/*
ejemplo de
http://www.c-sharpcorner.com/uploadfile/e14021/importing-database-in-android-studio/
 */

package com.endel.psicotest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.endel.psicotest.baseDatos.DBMain;
import com.endel.psicotest.baseDatos.DataBaseHelper;
import com.endel.psicotest.vista.LayoutBasico;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context contexto = getApplicationContext();

        DBMain.conectar(contexto);

        ScrollView scrollView = new ScrollView(contexto);
        RelativeLayout relativeLayout = new RelativeLayout(contexto);
        LayoutBasico layoutBasico = new LayoutBasico(this);

        int idPregunta = 1;
        int idUsuario = 1;

        DataBaseHelper dataBaseHelper = new DataBaseHelper(contexto);
        relativeLayout = layoutBasico.pintarVista(contexto, dataBaseHelper.getUltimaPreguntaSegunUsuario(idUsuario));
        scrollView.addView(relativeLayout);

        super.onCreate(savedInstanceState);
        setContentView(scrollView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
