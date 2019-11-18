package com.example.alimentadordeperritos.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class Estadisticas {
    private int Id_Estadistica;
    private int servoTrabado;// 1 - notificar y guardar en el historial
    private int perroComiofueraTiempo;// 1- notificar perro come fuera de tiempo y Guardar en el historial.
    private int Cantidad_Consumida;//cantidadXRacion -comida en el plato (arduino) //control de desborde de plato

    private int perroComioRapido; //recalcular rutina agregando un horario(al otro dia) con tope  de horarios hasta 6 veces al dia, si come rapido >= a 3 veces // notificar modificacion de rutina.//
    private int comidaDepo;// notificar 1000gr lleno . 0gr vacio y menor a 30 no alcanza para otra racion,
    private String Fecha_Hora;


    public Estadisticas(int id_Estadistica, int servoTrabado, int perroComiofueraTiempo, int cantidad_Consumida, int perroComioRapido, int comidaDepo, String fecha_Hora) {
        Id_Estadistica = id_Estadistica;
        this.servoTrabado = servoTrabado;
        this.perroComiofueraTiempo = perroComiofueraTiempo;
        Cantidad_Consumida = cantidad_Consumida;
        this.perroComioRapido = perroComioRapido;
        this.comidaDepo = comidaDepo;
        Fecha_Hora = fecha_Hora;
    }



    public Estadisticas ()
    {

    }


    public List<Integer> leer_estadistica(String fecha,Context context){

        ConexionSqlLite con = new ConexionSqlLite(context, "bd_perros", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();


        String[] campos = {Utilidades.PERRO_COMIO_RAPIDO}; //campos que quiero cargar
        String[] parametros = {fecha}; //Variable para guardar el campo por el que se busca. Se busca por el campo id
        List<Integer> lista=new ArrayList<Integer>();

        try {

            Cursor cursor = db.query(Utilidades.TABLA_ESTADISTICAS,campos,Utilidades.FECHA_ESTADISTICA+"=?",parametros,null,null,null); //se realiza la consulta por id
            //Utilidades.FECHA_ESTADISTICA+"=?",parametros
            /*En cursor se mapean los campos que quiero traer de la bd*/

          //  Toast.makeText(context, ,Toast.LENGTH_LONG).show();


          //  myBundleCargadoDeBd = prepararInfoAEnviar(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    lista.add(cursor.getInt(0));

                } while (cursor.moveToNext());
            }

            cursor.close();

        }catch (Exception e){
            return null;
        }

        db.close();

        return lista;
       /*

        Cursor c = db.rawQuery("SELECT "+ Utilidades.PERRO_COMIO_FUERA_TIEMPO + " from estadisticas WHERE Fecha_Estadistica =" +d ,null);

        if (c != null){
            c.moveToFirst();
            do{
                vector.add(c.getColumnIndex("perro_comio_fuera_tiempo"));

            } while(c.moveToNext());
            }
            c.close();
            db.close();

            return vector;

        */
        }

      public Long insertar_estadistica(Context context){
            ConexionSqlLite con = new ConexionSqlLite(context, "bd_perros", null, 1);
    SQLiteDatabase db = con.getWritableDatabase();
         //    String[] parametros;
    /* String[] campos = (Utilidades.ID_ESTADISTICA,Utilidades.SERVO_TRABADO,Utilidades.PERRO_COMIO_FUERA_TIEMPO);*/

    //db.execSQL("INSERT INTO estadisticas (id_estadistica,servo_trabado,perro_comio_fuera_tiempo,cantidad_consumida,comida_depo,fecha_estadistica,perro_comio_rapido) VALUES (" + this.getId_Estadistica() + "," + this.getServoTrabado() + "," + this.getPerroComiofueraTiempo() + "," + this.getCantidad_Consumida() + "," + this.getComidaDepo() + "," + this.getFecha_Hora() + "," + this.getPerroComioRapido());
          ContentValues cv= new ContentValues();
          cv.put(Utilidades.ID_ESTADISTICA,this.Id_Estadistica);
          cv.put(Utilidades.SERVO_TRABADO,this.servoTrabado);
          cv.put(Utilidades.PERRO_COMIO_FUERA_TIEMPO,this.perroComiofueraTiempo);
          cv.put(Utilidades.CANTIDAD_CONSUMIDA,this.Cantidad_Consumida);
          cv.put(Utilidades.COMIDA_DEPO,this.comidaDepo);
          cv.put(Utilidades.FECHA_ESTADISTICA,this.Fecha_Hora);
          cv.put(Utilidades.PERRO_COMIO_RAPIDO,this.perroComioRapido);

          Long idResultante = db.insert(Utilidades.TABLA_ESTADISTICAS,Utilidades.ID_ESTADISTICA,cv);

          //Toast.makeText(context,"id Registrado; "+idResultante,Toast.LENGTH_SHORT).show();

          db.close();
          return idResultante;
        }


    public int getId_Estadistica() {
        return Id_Estadistica;
    }

    public void setId_Estadistica(int id_Estadistica) {
        Id_Estadistica = id_Estadistica;
    }

    public int getServoTrabado() {
        return servoTrabado;
    }

    public void setServoTrabado(int servoTrabado) {
        this.servoTrabado = servoTrabado;
    }

    public int getPerroComiofueraTiempo() {
        return perroComiofueraTiempo;
    }

    public void setPerroComiofueraTiempo(int perroComiofueraTiempo) {
        this.perroComiofueraTiempo = perroComiofueraTiempo;
    }

    public int getCantidad_Consumida() {
        return Cantidad_Consumida;
    }

    public void setCantidad_Consumida(int cantidad_Consumida) {
        Cantidad_Consumida = cantidad_Consumida;
    }

    public int getPerroComioRapido() {
        return perroComioRapido;
    }

    public void setPerroComioRapido(int perroComioRapido) {
        this.perroComioRapido = perroComioRapido;
    }

    public int getComidaDepo() {
        return comidaDepo;
    }

    public void setComidaDepo(int comidaDepo) {
        this.comidaDepo = comidaDepo;
    }

    public String getFecha_Hora() {
        return Fecha_Hora;
    }

    public void setFecha_Hora(String fecha_Hora) {
        Fecha_Hora = fecha_Hora;
    }
}
