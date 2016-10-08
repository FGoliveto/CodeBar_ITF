package com.example.flavio.codebar_itf;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MostrarActivity extends Activity {

    private static final int MI_PERMISO_GUARDAR = 1;
    private static final int MI_PERMISO_COMPARTIR = 2;
    private boolean generado=false;
    private Bitmap completo =null;
    private Context context=this;
    private File lugar;
    private String codigoEnNumero;
    private Uri uri;
    boolean permisoCompartir;
    boolean permisoGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar);
        Button salir = (Button)findViewById(R.id.salir);
        Button compartir = (Button)findViewById(R.id.compartir);
        Log.d("TAG","Permisos.. Guardar "+permisoGuardar+ " y compartir "+permisoCompartir);
        if (permisoCompartir && permisoGuardar){
            Log.d("TAG","Esta habilitado para compartir");
            Toast.makeText(context,"Como habilitó guardar y leer en la memoria, ya puede compartir el código generado",Toast.LENGTH_LONG).show();
        }else if (permisoGuardar){
            Log.d("TAG","Esta habilitado para guardar");
            Toast.makeText(context,"Como habilitó guardar en la memoria, ya puede guardar el código generado",Toast.LENGTH_LONG).show();
        }
        Log.d("TAG","bundle es "+savedInstanceState);
        if (savedInstanceState!=null){
            compartir.setVisibility(View.GONE);
            uri= Uri.parse(savedInstanceState.getString("direccion"));
            String direccion = ubicacionRealDeUri(context,uri);
            Log.d("TAG","la direcion es "+direccion);
            File eliminar = new File(direccion);
            if (eliminar.exists()) {
                if (eliminar.delete()) {
                    Log.d("TAG","se borro el archivo "+uri);
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(direccion))));
                } else {
                    Log.d("TAG","archivo no borrado "+uri);
                }
            }
        }else {
            salir.setVisibility(View.GONE);
        }
        ImageView imagen = (ImageView) findViewById(R.id.imagen);
        Intent intent = getIntent();
        codigoEnNumero = intent.getExtras().getString("numero");
        Log.d("TAG","el numero es "+ codigoEnNumero);
        Bitmap codigoDeBarras;
        Bitmap numeros;
        CodigoNumericoABitmap codigoNumericoABitmap = new CodigoNumericoABitmap();
        codigoNumericoABitmap.setAssets(getAssets());
        try {
            codigoDeBarras = new CodigoBarraABitmap().barrasABitmap(codigoEnNumero, BarcodeFormat.ITF, 400, 45);
            numeros = codigoNumericoABitmap.textoABitmap(codigoEnNumero);
            Log.d("TAG","bitmap:"+codigoDeBarras);
            Log.d("TAG","texto:"+numeros);
            completo = new CombinacionDeBitmaps().combinar(codigoDeBarras,numeros);
            if (imagen != null) {
                imagen.setImageBitmap(completo);
                generado=true;
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        findViewById(R.id.compartir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MostrarActivity.this, new String[]{Manifest.permission_group.STORAGE}, MI_PERMISO_COMPARTIR);
                } else  {
                    Log.d("TAG","El permiso esta activado");
                    ContentValues valores = new ContentValues();
                    valores.put(MediaStore.Images.Media.TITLE, codigoEnNumero);
                    valores.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            valores);
                    Log.d("TAG","lugar de guardado "+uri);
                    OutputStream out = null;
                    try {
                        if (uri != null) {
                            out = getContentResolver().openOutputStream(uri);
                            Log.d("TAG","outstream es "+out);
                        }
                        completo.compress(Bitmap.CompressFormat.PNG, 100, out);
                        if (out != null) {
                            out.close();
                        }
                    } catch (Exception ignored) {
                        Log.d("TAG","No se pudo guardar la imagen");
                    }
                    Intent compartirIntent = new Intent(android.content.Intent.ACTION_SEND);
                    compartirIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    compartirIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    compartirIntent.setType("image/png");
                    startActivity(Intent.createChooser(compartirIntent, "Compartir imagen"));
                }
            }
        });
        findViewById(R.id.guardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MostrarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MI_PERMISO_GUARDAR);
                } else  {
                    Log.d("TAG","El permiso esta activado");
                    final EditText edittext = new EditText(context);
                    new AlertDialog.Builder(context)
                            .setTitle("Ingrese el nombre")
                            .setView(edittext)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String titulo = edittext.getText().toString();
                                    if (titulo.length()>0 && titulo.length()<51 && generado){
                                        OutputStream out = null;
                                        Log.d("TAG","Entro al boton guardar");
                                        try {
                                            File camino = new File(Environment.getExternalStorageDirectory() + "/Pictures/");
                                            lugar = new File(camino, titulo+".jpg");
                                            Log.d("TAG","direccion"+camino);
                                            out = new FileOutputStream(lugar);
                                        } catch (Exception e) {
                                            Toast.makeText(context, "Ocurrio un error, intente más tarde",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        try {
                                            completo.compress(Bitmap.CompressFormat.PNG, 100, out);
                                            Log.d("TAG","Se guardo la imagen");
                                            if (out != null) {
                                                out.flush();
                                            }
                                            if (out != null) {
                                                out.close();
                                                MediaScannerConnection.scanFile(context, new String[] { lugar.getPath() }, new String[] { titulo+".jpg" }, null);
                                                Intent guardado = new Intent(context,InicioActivity.class);
                                                guardado.putExtra("guardado",true);
                                                MostrarActivity.this.startActivity(guardado);
                                                finish();
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }else{
                                        if (generado){
                                            Toast.makeText(context,"El titulo debe tener al menos un caracter y no más de 50.",Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context,"No se pudo crear el codigo de barras",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                }
                            }).create().show();
                }
            }
        });
        findViewById(R.id.salir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public static String ubicacionRealDeUri(Context context, Uri uri)
    {
        Uri ubicacion = uri;
        if (uri.getScheme().compareTo("content")==0)
        {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int indiceColumna = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                ubicacion = Uri.parse(cursor.getString(indiceColumna));
                Log.d("TAG",ubicacion.toString());
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return ubicacion.toString();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MI_PERMISO_GUARDAR: {
                permisoGuardar = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
            case MI_PERMISO_COMPARTIR: {
                permisoCompartir = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permisoGuardar = permisoCompartir;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("TAG","uri es "+uri);
        if (uri!=null){
            Log.d("TAG","entro a guardar informacion");
            outState.putString("direccion", uri.toString());
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),InicioActivity.class);
        MostrarActivity.this.startActivity(intent);
        finish();
    }
}
