package com.example.flavio.codebar_itf;


import android.graphics.Bitmap;
import android.graphics.Canvas;

class CombinaciondeBitmaps {

    Bitmap combinar(Bitmap b1, Bitmap b2) {
        Bitmap bitmapFinal;
        int ancho, alto;
        alto = b1.getHeight() + b2.getHeight();
        if (b1.getWidth() > b2.getWidth()) {
            ancho = b1.getWidth();
        } else {
            ancho = b2.getWidth();
        }
        int inicioDelTexto= ((b1.getWidth()-b2.getWidth())/2);
        bitmapFinal = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapFinal);
        canvas.drawBitmap(b1, 0f, 0f, null);
        canvas.drawBitmap(b2, inicioDelTexto, b1.getHeight(), null);
        return bitmapFinal;
    }
}
