package com.example.alimentadordeperritos.bd;

public enum NivelDeActividad
{
    BAJO(3),
    MEDIO(2),
    ALTO(1);

    private final int codigoNA;

    private NivelDeActividad(int codigo){

        codigoNA = codigo;
    }

    public int getCodigoNA(){
        return codigoNA;
    }

}
