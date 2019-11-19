package com.example.alimentadordeperritos.interfaz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alimentadordeperritos.R;
import com.example.alimentadordeperritos.bd.ConexionSqlLite;
import com.example.alimentadordeperritos.bd.Utilidades;

public class ActivityInicio extends AppCompatActivity {

    public static int cargar_pantalla_inicial=0;
    public static final int TODO_OK=1;
    public static final int TODO_MAL=0;
    public static final int CODIGO_MI_PERFIL=1;
    public static final int CODIGO_PERFILxDEFECTO=2;


    public Bundle myBundleCargadoDeBd; //Contenedor de info del perfil del perro, el cual se usa para enviar a otra activity

    public boolean soyConfigManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //EditText idPerro,nombrePerro;

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inicio);
            //idPerro= (EditText) findViewById();

        ConexionSqlLite con = new ConexionSqlLite(this, "bd_perros", null, 1);

        soyConfigManual=false;
    }

    public void cargarPerfilPorDefecto(View view)// evento on click
    {
        if(cargarPerfil(CODIGO_PERFILxDEFECTO)==TODO_OK){
        Toast.makeText(getApplicationContext(),"Perfil por defecto cargado!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(), "Aún no existe un perfil por defecto cargado en bd.", Toast.LENGTH_LONG).show();
            //INTENT TEMPORAL PARA PRUEBAS
            Intent intentTemp = new Intent(this, DispositivosBT.class);
            myBundleCargadoDeBd = new Bundle();

            soyConfigManual = true;

             myBundleCargadoDeBd.putBoolean("boolean",soyConfigManual);


            intentTemp.putExtras(myBundleCargadoDeBd);
            startActivity(intentTemp);
        }
    }


    public void crearPerfilPersonalizado(View view)
    {
        if(cargarPerfil(CODIGO_MI_PERFIL)==TODO_OK){
            Toast.makeText(getApplicationContext(),"Ya existe un perfil creado.",Toast.LENGTH_LONG).show();
        } else { //Se abre el formulario
            Intent intent = new Intent(this, CrearPerfilActivity.class);
            startActivity(intent);
        }
        //registrarPerfil();

        //Toast.makeText(getApplicationContext(),"Perfil personalizado creado!",Toast.LENGTH_LONG).show();

    }
    public void cargarPerfilConfigurado(View view)
    {
        //Toast.makeText(getApplicationContext(),"Perfil configurado cargado!",Toast.LENGTH_LONG).show();
try {
    if (cargarPerfil(CODIGO_MI_PERFIL) == TODO_OK) {
        Intent intent = new Intent(this, DispositivosBT.class);
        intent.putExtras(myBundleCargadoDeBd);
        startActivity(intent);
    } else
        Toast.makeText(getApplicationContext(), "Aun no existe un perfil creado.", Toast.LENGTH_LONG).show();
}catch(Exception e)
    {
        Log.e("ErrorBundle...",e.getMessage().toString());
    }

}

    private int cargarPerfil(Integer codigoPerfil) {
        ConexionSqlLite con = new ConexionSqlLite(this, "bd_perros", null, 1);
        SQLiteDatabase db = con.getReadableDatabase();
        String[] parametros = {codigoPerfil.toString()}; //Variable para guardar el campo por el que se busca. Se busca por el campo id
        String[] campos = {Utilidades.CAMPO_NOMBRE,Utilidades.CAMPO_PESO,Utilidades.CAMPO_RAZA,Utilidades.CAMPO_ACTIVIDAD,Utilidades.CAMPO_ESTADO,Utilidades.CAMPO_FECHA_NACIMIENTO}; //campos que quiero cargar

         try {

             Cursor cursor = db.query(Utilidades.TABLA_PERFIL,campos,Utilidades.CAMPO_ID+"=?",parametros,null,null,null); //se realiza la consulta por id
             /*En cursor se mapean los campos que quiero traer de la bd*/
             cursor.moveToFirst();
             Toast.makeText(getApplicationContext(), "Consulta: "+cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4)+" "+cursor.getString(5),Toast.LENGTH_LONG).show();

             myBundleCargadoDeBd = prepararInfoAEnviar(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
             cursor.close();

         }catch (Exception e){
             return TODO_MAL;
         }

        db.close();

         return TODO_OK;
    }

    public Bundle prepararInfoAEnviar(String name,String peso,String raza,String na,String estado,String fnac){

        Bundle myBundle = new Bundle();
        myBundle.putString("nombre",name);
        myBundle.putString("peso",peso);
        myBundle.putString("raza",raza);
        myBundle.putString("na",na);
        myBundle.putString("estado",estado);
        myBundle.putString("fnac",fnac);

        return myBundle;
    }

    @Override
    public void onBackPressed (){
       // Toast.makeText(getApplicationContext(),"No se puede ",Toast.LENGTH_LONG).show();
        //System.exit(0);

        finishAndRemoveTask();

    }

}
