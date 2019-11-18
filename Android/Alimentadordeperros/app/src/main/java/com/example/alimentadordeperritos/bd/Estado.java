package com.example.alimentadordeperritos.bd;

public enum Estado
{
    GORDO(1),
    EXCEDIDO(2),
    BIEN(3),
    FLACO(4);

    private final int codigoEstado;

    private Estado(int codigo)
    {
        codigoEstado = codigo;
    }

   public int getCodigoEstado(){
        return codigoEstado;
   }

}
