package com.endel.psicotest.vista;

/**
 * Created by Cacharro_Portatil on 21/09/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.endel.psicotest.MainActivity;
import com.endel.psicotest.R;
import com.endel.psicotest.VariablesGlobales;

public class Admin extends Activity {

	EditText et1 = null, et2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);

		Button button = (Button)findViewById(R.id.button1);
		Button buttonCancel = (Button)findViewById(R.id.button2);

		et1 = (EditText)findViewById(R.id.editText1);
		et2 = (EditText)findViewById(R.id.editText2);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(et1.getText().toString().equals("admin") && et2.getText().toString().equals("uni2013")){
					Intent i = new Intent(Admin.this, Sincronizacion.class);
					startActivity(i);
					finish();
				} else{
					VariablesGlobales.PublicToast(getApplicationContext(), "WRONG USER OR PASSWORD");
				}
			}
		});

		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

}
