package com.example.alimentadordeperritos.bd;
import java.util.Date;
import java.util.List;

public class RutinaProgramada {
    private int cantidadxRacion;//en gr
    private int cantidadXDia;
    private List<Date> horario;


    public RutinaProgramada(int cantidadxRacion, int cantidadXDia, List<Date> horario) {
        this.cantidadxRacion = cantidadxRacion;
        this.cantidadXDia = cantidadXDia;
        this.horario = horario;
    }

    public int getCantidadxRacion() {
        return cantidadxRacion;
    }

    public void setCantidadxRacion(int cantidadxRacion) {
        this.cantidadxRacion = cantidadxRacion;
    }

    public int getCantidadXDia() {
        return cantidadXDia;
    }

    public void setCantidadXDia(int cantidadXDia) {
        this.cantidadXDia = cantidadXDia;
    }

    public List<Date> getHorario() {
        return horario;
    }

    public void setHorario(List<Date> horario) {
        this.horario = horario;
    }
}
