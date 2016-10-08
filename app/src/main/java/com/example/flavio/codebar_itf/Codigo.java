package com.example.flavio.codebar_itf;

import android.util.Log;


class Codigo {
    private static  final int CODIGO=39;
    String codigo (String codigo){
        int[] prueba = new int[CODIGO];
        for (int i=0;i<codigo.length();i++){
            prueba[i]= Integer.valueOf(String.valueOf(codigo.charAt(i)));
        }
        int suma=0;
        for (int j=0;j<prueba.length;j++) {
            if (j%2==0){
                suma = suma + prueba[j]*3;
            }else{
                suma = suma + prueba[j];
            }
        }
        int resto = suma%10;
        if (resto!=0){
            resto=10-resto;
        }
        codigo=codigo+resto;
        Log.d("TAG","suma: "+suma+ ", y el codigocompleto es "+codigo);
        return codigo;
    }

    boolean esNumerico(String codigo) {
        for (int i = 0; i < codigo.length(); i++) {
            if (!Character.isDigit(codigo.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
