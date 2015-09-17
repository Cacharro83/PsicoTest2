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
        LayoutBasico.contexto = getApplicationContext();

        DBMain.conectar(LayoutBasico.contexto);
        ScrollView scrollView = new ScrollView(LayoutBasico.contexto);
        RelativeLayout relativeLayout;
        LayoutBasico layoutBasico = new LayoutBasico(this);

        relativeLayout = layoutBasico.pintarVista(LayoutBasico.contexto, 0);
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
