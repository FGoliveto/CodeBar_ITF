package com.example.flavio.codebar_itf;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.FILTER_BITMAP_FLAG;
import static android.graphics.Paint.LINEAR_TEXT_FLAG;
import static android.graphics.Paint.SUBPIXEL_TEXT_FLAG;


class CodigoNumericoABitmap {
    private AssetManager assets;

    void setAssets(AssetManager assets) {
        this.assets = assets;
    }

    Bitmap textoABitmap(String texto) {
        Typeface type = Typeface.createFromAsset(assets, "GOTHIC.TTF");
        Paint dibujo = new Paint(ANTI_ALIAS_FLAG);
        dibujo.setFlags(LINEAR_TEXT_FLAG);
        dibujo.setFlags(SUBPIXEL_TEXT_FLAG);
        dibujo.setFlags(FILTER_BITMAP_FLAG);
        dibujo.setTypeface(type);
        dibujo.setTextAlign(Paint.Align.LEFT);
        dibujo.clearShadowLayer();
        float baseDeLinea = -dibujo.ascent();
        int ancho = (int) (dibujo.measureText(texto) + 0.5f);
        int alto = (int) (baseDeLinea + dibujo.descent() + 0.5f);
        Bitmap imagen = Bitmap.createBitmap(400, alto, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(imagen);
        canvas.drawColor(Color.WHITE);
        canvas.drawText(texto, (400-ancho)/2, baseDeLinea, dibujo);
        return imagen;
    }
}
