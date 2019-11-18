package com.example.alimentadordeperritos.bd;

public class Raza {

    private int ID;
    private Tamanio tamanio;
    private String descripcion;

public  Raza(String descripcion){

    this.descripcion = descripcion;

    switch (descripcion){

        case "San bernardo":
            this.ID =1;
            this.tamanio = Tamanio.GRANDE;
            break;

        case "Boxer":
            this.ID =2;
            this.tamanio = Tamanio.MEDIANO;
            break;

        case "Chihuahua":
            this.ID =3;
            this.tamanio = Tamanio.PEQUENIO;
            break;
    }
}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Tamanio getTamanio() {
        return tamanio;
    }

    public void setTamanio(Tamanio tamanio) {
        this.tamanio = tamanio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
