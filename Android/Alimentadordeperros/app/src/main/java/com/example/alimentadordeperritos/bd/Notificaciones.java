package com.example.alimentadordeperritos.bd;

import java.util.Date;

public class Notificaciones {
    private int codigo;
    private Date fechaHora;
    private String desc;
    private char tipoNotificacion;// I informativo - A Alerta

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public char getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(char tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }
}
