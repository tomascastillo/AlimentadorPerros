package com.example.alimentadordeperritos.bd;

public class Utilidades {

    //Constantes campos tabla perfil
 /*
    public static final String TABLA_PERFIL="perfil_perro";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_RAZA="Raza";
    public static final String CAMPO_PESO="Peso";
    public static final String CAMPO_ACTIVIDAD="Nivel_Actividad";
    public static final String CAMPO_ESTADO="Estado";
    public static final String CAMPO_FECHA_NACIMIENTO="Fecha_Nacimiento";
    public static final String CAMPO_NOMBRE="Nombre";

    public static final String CREATE_TABLE_PERFIL="CREATE TABLE" +TABLA_PERFIL+ "("+CAMPO_ID+ "INTEGER,"+CAMPO_RAZA+"TEXT, "+CAMPO_PESO+" INTEGER,"+CAMPO_ACTIVIDAD +"TEXT,"+CAMPO_ESTADO+ "TEXT," +CAMPO_FECHA_NACIMIENTO+"DATE,"+CAMPO_NOMBRE+ "TEXT)";
    public static final String CREATE_TABLE_RAZA="CREATE TABLE raza (id_Raza INTEGER,Tamanio INTEGER,Descripcion TEXT)";
    public static final String CREATE_TABLE_COMIDA_PROGRAMADA="CREATE TABLE comida_programada (Cantidad INTEGER,Horario DATE)";
    public static final String CREATE_TABLE_ALERTA="CREATE TABLE alertas (id_alerta INTEGER,descripcion TEXT)";
    public static final String CREATE_TABLE_NOTIFICACION="CREATE TABLE notificacion (id INTEGER,Fecha_Hora DATE,Alerta TEXT)";
    public static final String CREATE_TABLE_ESTADISTICAS="CREATE TABLE Estadisticas (id INTEGER,Cant_Consumida INTEGER,Fecha_Hora DATE)";*/

    public static final String TABLA_PERFIL="perfil_perro";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_PESO="Peso";
    public static final String CAMPO_RAZA="Raza";
    public static final String CAMPO_ACTIVIDAD="Nivel_Actividad";
    public static final String CAMPO_ESTADO="Estado";
    public static final String CAMPO_FECHA_NACIMIENTO="Fecha_Nacimiento";
    public static final String CAMPO_NOMBRE="Nombre";

    public static final String CREATE_TABLE_PERFIL="CREATE TABLE "+TABLA_PERFIL+" ("+CAMPO_ID+" INTEGER,"+CAMPO_PESO+" INTEGER,"+CAMPO_RAZA+" TEXT,"+CAMPO_ACTIVIDAD+" TEXT,"+CAMPO_ESTADO+" TEXT,"+CAMPO_FECHA_NACIMIENTO+" DATE,"+CAMPO_NOMBRE+" TEXT)";
//estadisticas, Notificaciones

    public static final String TABLA_ESTADISTICAS="estadisticas";
    public static final String ID_ESTADISTICA="id_estadistica";
    public static final String SERVO_TRABADO="servo_trabado";
    public static final String PERRO_COMIO_FUERA_TIEMPO="perro_comio_fuera_tiempo";
    public static final String CANTIDAD_CONSUMIDA="cantidad_consumida";
    public static final String COMIDA_DEPO="comida_depo";
    public static final String FECHA_ESTADISTICA="fecha_estadistica";

    public static final String PERRO_COMIO_RAPIDO="perro_comio_rapido";

    public static final String CREATE_TABLE_ESTADSTICAS="CREATE TABLE "+TABLA_ESTADISTICAS+" ("+ID_ESTADISTICA+" INTEGER, "+SERVO_TRABADO+" INTEGER,"+PERRO_COMIO_FUERA_TIEMPO+" INTEGER,"+CANTIDAD_CONSUMIDA+" INTEGER,"+COMIDA_DEPO+" INTEGER,"+FECHA_ESTADISTICA+" TEXT,"+PERRO_COMIO_RAPIDO+" INTEGER)";

}
