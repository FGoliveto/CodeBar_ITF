package com.example.flavio.codebar_itf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class InicioActivity extends Activity{
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_inicio);
        Intent guardado = getIntent();
        boolean mensaje = guardado.getBooleanExtra("guardado",false);
        if (mensaje){
            Toast.makeText(getApplicationContext(),"Guardado exitoso",Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"El codigo se encuentra guardado en la carpeta Imagenes",Toast.LENGTH_LONG).show();
        }
        Typeface type = Typeface.createFromAsset(getAssets(),"calibriz.ttf");
        TextView portada = (TextView)findViewById(R.id.portada);
        portada.setTypeface(type);
        findViewById(R.id.comenzar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CrearActivity.class);
                InicioActivity.this.startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.salirDefinitivo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
