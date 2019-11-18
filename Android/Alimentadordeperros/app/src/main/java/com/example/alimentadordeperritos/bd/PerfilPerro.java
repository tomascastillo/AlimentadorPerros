package com.example.alimentadordeperritos.bd;

import java.util.Date;


public class PerfilPerro {

   private int id;
   private Integer peso;
   private Raza raza;
   private String na;
   private String estado;
   private Date fechaNac;
   private String nombre;

    public PerfilPerro(int id, Integer peso, Raza raza, String na, String estado, Date fechaNac, String nombre) {
        this.id = id;
        this.peso = peso;
        this.raza = raza;
        this.na = na;
        this.estado = estado;
        this.fechaNac = fechaNac;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Raza getRaza() {
        return raza;
    }

    public void setRaza(Raza raza) {
        this.raza = raza;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaNac() {


    return fechaNac;
    }

    public void setFechaNac(Date fechaNac)
    {

        this.fechaNac = fechaNac;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
