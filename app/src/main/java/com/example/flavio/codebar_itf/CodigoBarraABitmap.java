package com.example.flavio.codebar_itf;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;


class CodigoBarraABitmap {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap barrasABitmap(String contenido, BarcodeFormat formato, int ancho, int alto) throws WriterException {
        if (contenido == null) {
            return null;
        }
        Map<EncodeHintType, Object> indicio = null;
        String numeros = guessAppropriateEncoding(contenido);
        if (numeros != null) {
            indicio = new EnumMap<>(EncodeHintType.class);
            indicio.put(EncodeHintType.CHARACTER_SET, numeros);
        }
        MultiFormatWriter escritura = new MultiFormatWriter();
        BitMatrix resultado;
        try {
            resultado = escritura.encode(contenido, formato, ancho, alto, indicio);
        } catch (IllegalArgumentException iae) {
            return null;
        }
        int anchoBitmap = resultado.getWidth();
        int altoBitmap = resultado.getHeight();
        int[] pixeles = new int[anchoBitmap * altoBitmap];
        for (int y = 0; y < altoBitmap; y++) {
            int pos = y * anchoBitmap;
            for (int x = 0; x < anchoBitmap; x++) {
                pixeles[pos + x] = resultado.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap imagen = Bitmap.createBitmap(anchoBitmap, altoBitmap,
                Bitmap.Config.ARGB_8888);
        imagen.setPixels(pixeles, 0, anchoBitmap, 0, 0, anchoBitmap, altoBitmap);
        return imagen;
    }
    private String guessAppropriateEncoding(CharSequence contenido) {
        for (int i = 0; i < contenido.length(); i++) {
            if (contenido.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
