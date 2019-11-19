package com.example.alimentadordeperritos.bd;

public enum NivelDeActividad
{
    BAJO(0.9),
    MEDIO(1.0),
    ALTO(1.1);

    private final double codigoNA;

    private NivelDeActividad(double codigo){

        codigoNA = codigo;
    }

    public double getCodigoNA(){
        return codigoNA;
    }

}
