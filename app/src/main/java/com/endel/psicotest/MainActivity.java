/*
ejemplo de
http://www.c-sharpcorner.com/uploadfile/e14021/importing-database-in-android-studio/
 */

package com.endel.psicotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.endel.psicotest.baseDatos.DBMain;
import com.endel.psicotest.baseDatos.DataBaseHelper;
import com.endel.psicotest.vista.Admin;
import com.endel.psicotest.vista.LayoutBasico;

public class MainActivity extends Activity {
    private long lastBackPress = 0;

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            manageCancel();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void manageCancel() {
        //Sólo permitimos dar atrás antes de la tabla vida
        if (LayoutBasico.idPreguntaAnterior >= 11) {
            return;
        }
        if ((getLastBackPress() + VariablesGlobales.DOUBLE_BACK_TIME > System.currentTimeMillis()) && LayoutBasico.idPreguntaAnterior != LayoutBasico.idPregunta) {
            LayoutBasico layoutBasico = new LayoutBasico(LayoutBasico.activity);
            LayoutBasico.idPregunta = LayoutBasico.idPreguntaAnterior;
            LayoutBasico.relativeLayout = layoutBasico.pintarVista(LayoutBasico.contexto, LayoutBasico.idPregunta);
            ScrollView scrollView = new ScrollView(LayoutBasico.contexto);
            scrollView.addView(LayoutBasico.relativeLayout);
            LayoutBasico.activity.setContentView(scrollView);

            //Al dar para atrás puede ser que tenga valores guardados antiguos
            DataBaseHelper dataBaseHelper = new DataBaseHelper(LayoutBasico.contexto);
            if (LayoutBasico.idPregunta == 0) {                 //Si está en la pregunta de 'IdUsuario'
                dataBaseHelper.borrarPosiblesRespuestas();      //borrado de hasta de 'usuarios'
            } else {                                            //si no
                dataBaseHelper.borrarSiYaExisteValorAnterior(); //borrado sólo del valor anterior
            }
        } else {
            if (LayoutBasico.idPreguntaAnterior != LayoutBasico.idPregunta) {
                Toast tstBackPress = Toast.makeText(this, com.endel.psicotest.VariablesGlobales.pressTwice, Toast.LENGTH_LONG);
                TextView v = (TextView) tstBackPress.getView().findViewById(android.R.id.message);
                v.setTextSize(25);
                tstBackPress.show();
                setLastBackPress();
            }
        }
    }


    private synchronized long getLastBackPress() {
        return lastBackPress;
    }

    private synchronized void setLastBackPress() {
        lastBackPress = System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = null;
        switch(item.getItemId()){
            case R.id.action_settings:
                intent = new Intent(getApplicationContext(), Admin.class);
                startActivity(intent);
                finish();
                return true;
        }
        return false;
    }
}
