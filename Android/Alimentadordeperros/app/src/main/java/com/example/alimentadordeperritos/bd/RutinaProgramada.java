package com.example.alimentadordeperritos.bd;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RutinaProgramada {
    private double cantidadxRacion;//en gr
    private double cantidadXDia;
    private List<Date> horario;

    public RutinaProgramada(double cantidadxRacion, double cantidadXDia, List<Date> horario) {
        this.cantidadxRacion = cantidadxRacion;
        this.cantidadXDia = cantidadXDia;
        this.horario = horario;
    }

    public double getCantidadxRacion() {
        return cantidadxRacion;
    }

    public void setCantidadxRacion(double cantidadxRacion) {
        this.cantidadxRacion = cantidadxRacion;
    }

    public double getCantidadXDia() {
        return cantidadXDia;
    }

    public void setCantidadXDia(double cantidadXDia) {
        this.cantidadXDia = cantidadXDia;
    }

    public List<Date> getHorario() {
        return horario;
    }

    public void setHorario(List<Date> horario) {
        this.horario = horario;
    }

    public  void recalcularRutina(List<Integer> arrayComioRapido){

        int contador=0;

        for(Integer element: arrayComioRapido){
            if(element == 1)
                contador++;
        }

        double res = contador/arrayComioRapido.size();

        if(res > 0.5 && this.getHorario().size()<6){
            insertarHorarios(this.getHorario().size()+1);
        }


    }
    public void insertarHorarios(int cantRaciones){
    //CALCULO Horarios
    List<Date> horarios = new ArrayList<Date>();
    //int cantRaciones = 3;//TEMPORAL, COMO SE CALCULA?

    int intervaloTiempo = 24 / cantRaciones;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date horaActual = new Date();
        System.out.println("Hora actual: " + dateFormat.format(horaActual));

    //CREO UN OBJETO CALENDAR PARA SUMAR HORAS!
    Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaActual); //recibe un Date;


        for (int i = 0; i < cantRaciones; i++) {
            calendar.add(Calendar.HOUR, intervaloTiempo); //horasASumar es int.
            horarios.add(calendar.getTime());
        }

        this.setHorario(horarios);
    }
}
