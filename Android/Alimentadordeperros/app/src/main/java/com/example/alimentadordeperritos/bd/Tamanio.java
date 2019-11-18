package com.example.alimentadordeperritos.bd;

public enum Tamanio
{
    PEQUENIO(1),
    MEDIANO(2),
    GRANDE(3);

    private final int codigoTamanio;

     private Tamanio ( int value) {
        codigoTamanio = value;
    }

    public int getCodigoTamanio(){
         return codigoTamanio;
    }
}
