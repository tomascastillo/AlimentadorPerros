package com.example.alimentadordeperritos.interfaz;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alimentadordeperritos.R;
import com.example.alimentadordeperritos.bd.ConexionSqlLite;
import com.example.alimentadordeperritos.bd.Estado;
import com.example.alimentadordeperritos.bd.NivelDeActividad;
import com.example.alimentadordeperritos.bd.Utilidades;

import java.util.ArrayList;
import java.util.Calendar;

public class CrearPerfilActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etPeso;
    private Spinner spRaza;
    private Spinner spNivelActividad;
    private Spinner spEstado;

    //Variables para spinners
    private String NASeleccionado;
    private String estadoSeleccionado;
    private String razaSeleccionada;

    //Variables para fnac
    private static final String TAG = "CrearPerfilActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String dateFnac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_perfil);

       etNombre = (EditText) findViewById(R.id.editTextNombre);
        etPeso = (EditText) findViewById(R.id.editTextPeso);
        spRaza = (Spinner) findViewById(R.id.spinnerRaza);
        spNivelActividad = (Spinner) findViewById(R.id.spinnerNivelActividad);
        spEstado = (Spinner) findViewById(R.id.spinnerEstado);
        mDisplayDate = (TextView) findViewById(R.id.textViewIngreseFnac);

        configurarSpinnerRaza();
        configurarSpinnerNA();
        configurarSpinnerEstado();

        configurarFnac();




    }//onCreate

    public void configurarFnac(){

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CrearPerfilActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);

                dateFnac = date; //funciona?
            }
        };
    }//finMetodo

    public void configurarSpinnerRaza(){
        ArrayList<String> lista = new ArrayList<String>();

        lista.add("Seleccione");
        lista.add("San bernardo");
        lista.add("Boxer");
        lista.add("Chihuahua");

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,lista);

        spRaza.setAdapter(adapter);

        spRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"Seleccionado: "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                razaSeleccionada = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void configurarSpinnerNA(){
        ArrayList<String> listaNA = new ArrayList<String>();

        listaNA.add("Seleccione");
        listaNA.add(NivelDeActividad.ALTO.name());
        listaNA.add(NivelDeActividad.MEDIO.name());
        listaNA.add(NivelDeActividad.BAJO.name());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listaNA);

        spNivelActividad.setAdapter(adapter);

        spNivelActividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"Seleccionado: "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                NASeleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void configurarSpinnerEstado(){
        ArrayList<String> lista = new ArrayList<String>();

        lista.add("Seleccione");
        lista.add(Estado.BIEN.name());
        lista.add(Estado.FLACO.name());
        lista.add(Estado.GORDO.name());

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,lista);

        spEstado.setAdapter(adapter);

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(parent.getContext(),"Seleccionado: "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
                estadoSeleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void eventoButtonRegistarPerfil(View view){


           registrarPerfilEnBd(); //guarda el perfil del perro en la bd solo la primera vez

           Intent intent = new Intent(this, DispositivosBT.class);

            //Aca antes de iniciar la siguiente actividad se codifica la info que se le quiere enviar.
            Bundle myBundle = prepararInfoAEnviar(etNombre.getText().toString(), etPeso.getText().toString(), razaSeleccionada, NASeleccionado, estadoSeleccionado, dateFnac);
              //SE GUARDA INFO EN EL INTENT
            intent.putExtras(myBundle);

            startActivity(intent); //Una vez que inicia la sig activity, la recepcion de la info la podemos hacer dentro de su metodo onCreate.

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

    private void registrarPerfilEnBd()
    {
        ConexionSqlLite con = new ConexionSqlLite(this, "bd_perros", null, 1);

        SQLiteDatabase db=con.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_ID,1);
        values.put(Utilidades.CAMPO_NOMBRE,etNombre.getText().toString());
        values.put(Utilidades.CAMPO_PESO,etPeso.getText().toString());
        values.put(Utilidades.CAMPO_RAZA,razaSeleccionada);
        values.put(Utilidades.CAMPO_ACTIVIDAD,NASeleccionado);
        values.put(Utilidades.CAMPO_ESTADO,estadoSeleccionado);
        values.put(Utilidades.CAMPO_FECHA_NACIMIENTO,dateFnac);

        Long idResultante = db.insert(Utilidades.TABLA_PERFIL,Utilidades.CAMPO_ID,values); //Se inserta en la bd

        Toast.makeText(getApplicationContext(),"id Registrado; "+idResultante,Toast.LENGTH_SHORT).show();

        // String pathDatabase = getDatabasePath("bd_perros.db").getAbsolutePath();
        //  Toast.makeText(getApplicationContext(),pathDatabase,Toast.LENGTH_SHORT).show();
        db.close();

        //return idResultante;
    }
}
