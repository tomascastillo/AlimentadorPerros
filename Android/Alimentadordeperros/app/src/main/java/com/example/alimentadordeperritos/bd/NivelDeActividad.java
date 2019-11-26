package com.example.alimentadordeperritos.bd;

public enum NivelDeActividad
{
    BAJO(1.1),
    MEDIO(1.0),
    ALTO(0.9);

    private final double codigoNA;

    private NivelDeActividad(double codigo){

        codigoNA = codigo;
    }

    public double getCodigoNA(){
        return codigoNA;
    }

}
