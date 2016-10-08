package com.example.flavio.codebar_itf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class CrearActivity extends Activity {
    private static final String[] TIPO_FORMULARIOS= {
        "01 Factura A","06 Factura B","51 Factura M","91 Remito R","02 Notas de Debito A","03 Notas de Credito A",
            "07 Notas de Debito B","08 Notas de Credito B","19 Facturas de Exportación","52 Notas de Debito M",
            "53 Notas de Credito M","60 Liquido Producto A","61 Liquido Producto B",""
    };
    private static final int[] DIGITOS_DE_CADA_CAMPO={
        11,2,4,14,8,39
    };
    private String codigo="";
    private String formulario;
    private String cuit;
    private String cai;
    private String pv;
    private String fv;
    private EditText Ecuit;
    private EditText Epv;
    private EditText Ecai;
    private EditText Efv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        Typeface type = Typeface.createFromAsset(getAssets(),"calibrii.ttf");
        TextView uno = (TextView)findViewById(R.id.tcuit);
        TextView dos = (TextView)findViewById(R.id.tipoF);
        TextView tres = (TextView)findViewById(R.id.tpv);
        TextView cuatro = (TextView)findViewById(R.id.tcai);
        TextView cinco = (TextView)findViewById(R.id.tfv);
        uno.setTypeface(type);
        dos.setTypeface(type);
        tres.setTypeface(type);
        cuatro.setTypeface(type);
        cinco.setTypeface(type);
        Ecuit= (EditText) findViewById(R.id.cuit);
        Epv= (EditText) findViewById(R.id.pv);
        Ecai= (EditText) findViewById(R.id.cai);
        Efv= (EditText) findViewById(R.id.fv);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> formularios = new ArrayList<>();
        Collections.addAll(formularios, TIPO_FORMULARIOS);
        Collections.sort(formularios);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner, formularios);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                formulario = String.valueOf(parent.getItemAtPosition(position));
                formulario = formulario.split(" ")[0];
                Log.d("TAG",formulario);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                formulario = "";
            }
        });
        findViewById(R.id.generar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Ecuit.getText().toString().length()==DIGITOS_DE_CADA_CAMPO[0]){
                    cuit= Ecuit.getText().toString();
                }else{
                    Toast.makeText(getApplicationContext(),"Número de CUIT incorrecto",Toast.LENGTH_SHORT).show();
                }
                if (formulario.length()!=DIGITOS_DE_CADA_CAMPO[1]){
                    Toast.makeText(getApplicationContext(),"Seleccionar tipo de formulario",Toast.LENGTH_SHORT).show();
                }
                if (Epv.getText().toString().length()==DIGITOS_DE_CADA_CAMPO[2]){
                    pv= Epv.getText().toString();
                }else{
                    Toast.makeText(getApplicationContext(),"Punto de venta incorrecto",Toast.LENGTH_SHORT).show();
                }
                if (Ecai.getText().toString().length()==DIGITOS_DE_CADA_CAMPO[3]){
                    cai= Ecai.getText().toString();
                }else{
                    Toast.makeText(getApplicationContext(),"Número de CAI incorrecto",Toast.LENGTH_SHORT).show();
                }
                if (Efv.getText().toString().length()==DIGITOS_DE_CADA_CAMPO[4]){
                    fv= Efv.getText().toString();
                }else{
                    Toast.makeText(getApplicationContext(),"Fecha de vencimiento incorrecto",Toast.LENGTH_SHORT).show();
                }
                codigo = cuit+formulario+pv+cai+fv;
                Log.d("TAG","Codigo es "+codigo+ "y el largo es : "+codigo.length());
                if (codigo.length()==DIGITOS_DE_CADA_CAMPO[5]){
                    if (new Codigo().esNumerico(codigo)){
                        codigo = new Codigo().codigo(codigo);
                        Intent intent = new Intent(getApplicationContext(),MostrarActivity.class);
                        intent.putExtra("numero",codigo);
                        CrearActivity.this.startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Deben ser todos digitos",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.regresar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),InicioActivity.class);
        CrearActivity.this.startActivity(intent);
        finish();
    }
}
