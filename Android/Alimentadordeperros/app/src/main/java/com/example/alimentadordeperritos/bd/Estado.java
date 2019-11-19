package com.example.alimentadordeperritos.bd;

public enum Estado
{
    GORDO(0.9),

    BIEN(1.0),
    FLACO(1.1);

    private final double codigoEstado;

    private Estado(double codigo)
    {
        codigoEstado = codigo;
    }

   public double getCodigoEstado(){
        return codigoEstado;
   }

}
