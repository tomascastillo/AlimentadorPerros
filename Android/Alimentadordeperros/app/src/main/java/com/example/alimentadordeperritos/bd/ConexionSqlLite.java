package com.example.alimentadordeperritos.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSqlLite extends SQLiteOpenHelper {

    //final String CREATE_TABLE_PERFIL="CREATE TABLE perfil_perro (id INTEGER,Peso INTEGER,Raza TEXT,Nivel_Actividad TEXT,Estado TEXT,Fecha_Nacimiento DATE,Nombre TEXT)";
   /* final String CREATE_TABLE_RAZA="CREATE TABLE raza (id_Raza INTEGER,Tamanio INTEGER,Descripcion TEXT)";
    final String CREATE_TABLE_COMIDA_PROGRAMADA="CREATE TABLE comida_programada (Cantidad INTEGER,Horario DATE)";
    final String CREATE_TABLE_ALERTA="CREATE TABLE alertas (id_alerta INTEGER,descripcion TEXT)";
    final String CREATE_TABLE_NOTIFICACION="CREATE TABLE notificacion (id INTEGER,Fecha_Hora DATE,Alerta TEXT)";
    final String CREATE_TABLE_ESTADISTICAS="CREATE TABLE estadisticas (id INTEGER,Cant_Consumida INTEGER,Fecha_Hora DATE)";
*/

    public ConexionSqlLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    } //Este metodo, automaticamente llama al metodo onCreate y crea la base de datos.

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREATE_TABLE_PERFIL);
        db.execSQL(Utilidades.CREATE_TABLE_ESTADSTICAS);
        /*db.execSQL(Utilidades.CREATE_TABLE_RAZA);
        db.execSQL(Utilidades.CREATE_TABLE_COMIDA_PROGRAMADA);
        db.execSQL(Utilidades.CREATE_TABLE_ALERTA);
        db.execSQL(Utilidades.CREATE_TABLE_NOTIFICACION);
        db.execSQL(Utilidades.CREATE_TABLE_ESTADISTICAS);*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS perfil_perro");
      /*  db.execSQL("DROP TABLE IF EXISTS raza");
        db.execSQL("DROP TABLE IF EXISTS comida_programada");
        db.execSQL("DROP TABLE IF EXISTS alertas");
        db.execSQL("DROP TABLE IF EXISTS notificacion");
        db.execSQL("DROP TABLE IF EXISTS estadisticas");*/
        db.execSQL("DROP TABLE IF EXISTS estadisticas");

      onCreate(db);
    } //Este metodo se ejecuta cada vez que reinstalamos la App, verifica si existe una version vieja de bd la borra, y crea una nueva!


    // En el main activity agregar ConexionSqlLite con=new ConexionSqlLite(this,"bd_perros",null,1);
    // Eso es para conectar la base de datos
}
