package com.example.alimentadordeperritos.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSqlLite extends SQLiteOpenHelper {



    public ConexionSqlLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    } //Este metodo, automaticamente llama al metodo onCreate y crea la base de datos.

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREATE_TABLE_PERFIL);
        db.execSQL(Utilidades.CREATE_TABLE_ESTADSTICAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS perfil_perro");
        db.execSQL("DROP TABLE IF EXISTS estadisticas");

      onCreate(db);
    } //Este metodo se ejecuta cada vez que reinstalamos la App, verifica si existe una version vieja de bd la borra, y crea una nueva!


    // En el main activity agregar ConexionSqlLite con=new ConexionSqlLite(this,"bd_perros",null,1);
    // Eso es para conectar la base de datos
}
