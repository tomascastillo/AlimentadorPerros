package com.example.alimentadordeperritos.interfaz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alimentadordeperritos.R;
import com.example.alimentadordeperritos.bd.PerfilPerro;

import java.util.Set;

public class DispositivosBT extends AppCompatActivity {


    public static final int TODO_OK=1;
    public static final int TODO_MAL=0;

    private PerfilPerro perfilActual; //Aca se guarda el perfil


    //1)
    // Depuración de LOGCAT
    private static final String TAG = "DispositivosBT"; //<-<- PARTE A MODIFICAR >->->
    // Declaracion de ListView
    ListView IdLista;
    // String que se enviara a la actividad principal, mainactivity
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Declaracion de campos
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter mPairedDevicesArrayAdapter;

    Bundle myBundle ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bt);

        myBundle = this.getIntent().getExtras();


       // if(recibirPerfil()==TODO_MAL)
         //   Toast.makeText(getApplicationContext(),"No recibi perfil, te falta programar esta parte.",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        //---------------------------------
        VerificarEstadoBT();

        // Inicializa la array que contendra la lista de los dispositivos bluetooth vinculados
        mPairedDevicesArrayAdapter = new ArrayAdapter(this, R.layout.nombre_dispositivos);//<-<- PARTE A MODIFICAR >->->
        // Presenta los disposisitivos vinculados en el ListView
        IdLista = (ListView) findViewById(R.id.IdLista);
        IdLista.setAdapter(mPairedDevicesArrayAdapter);
        IdLista.setOnItemClickListener(mDeviceClickListener);
        // Obtiene el adaptador local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //------------------- EN CASO DE ERROR -------------------------------------
        //SI OBTIENES UN ERROR EN LA LINEA (BluetoothDevice device : pairedDevices)
        //CAMBIA LA SIGUIENTE LINEA POR
        //Set <BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        //------------------------------------------------------------------------------

        // Obtiene un conjunto de dispositivos actualmente emparejados y agregua a 'pairedDevices'
        Set <BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Adiciona un dispositivos previo emparejado al array
        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices) { //EN CASO DE ERROR LEER LA ANTERIOR EXPLICACION
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    }

    // Configura un (on-click) para la lista
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {

            // Obtener la dirección MAC del dispositivo, que son los últimos 17 caracteres en la vista
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

                // Realiza un intent para iniciar la siguiente actividad
            // mientras toma un EXTRA_DEVICE_ADDRESS que es la dirección MAC.
            //Intent i = new Intent(DispositivosBT.this, TemporalActivity.class);//<-<- PARTE A MODIFICAR >->->



            try {


              Intent i = new Intent(DispositivosBT.this, MainActivity.class);//<-<- PARTE A MODIFICAR >->->
            // i.putExtras(prepararInfoAEnviar(perfilActual.getNombre(), perfilActual.getPeso().toString(),
           //          perfilActual.getRaza().toString(), perfilActual.getNa(),perfilActual.getEstado(),perfilActual.getFechaNac().toString()));

                i.putExtras(myBundle);

            i.putExtra(EXTRA_DEVICE_ADDRESS, address);

             startActivity(i);
         }catch(RuntimeException e)
         {
             Log.e("<<Error intent >>", e.getMessage());
         }
        }
    };


/*
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

 */
    private void VerificarEstadoBT() {
        // Comprueba que el dispositivo tiene Bluetooth y que está encendido.
        mBtAdapter= BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (mBtAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth Activado...");
            } else {
                //Solicita al usuario que active Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }

    //RECIBE EL PERFIL DE PERRO DE ActivityInicio
   /*
    private int recibirPerfil() {

        Bundle myBundle = this.getIntent().getExtras();
        String nombre,raza,na,estado,fnac;
        Integer peso;
        Date date;

        if(myBundle!=null){
            nombre = myBundle.getString("nombre");
            peso = Integer.parseInt(myBundle.getString("peso"));
            raza = myBundle.getString("raza");
            na = myBundle.getString("na");
            estado = myBundle.getString("estado");
            fnac = myBundle.getString("fnac");

            Toast.makeText(getApplicationContext(),"Recibi info: "+nombre+" "+peso+" "+raza+" "+na+" "+estado+" "+fnac ,Toast.LENGTH_LONG).show();

            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            try {
                date = format.parse(fnac);
                perfilActual = new PerfilPerro(1,peso,new Raza(raza),na,estado,date,nombre);
            } catch (ParseException e) {
                e.printStackTrace();
            }




            return TODO_OK;
        }

        return TODO_MAL;
    }//recibirPerfil
*/

}


