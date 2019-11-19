package com.example.alimentadordeperritos.interfaz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.alimentadordeperritos.R;
import com.example.alimentadordeperritos.bd.Estadisticas;
import com.example.alimentadordeperritos.bd.Estado;
import com.example.alimentadordeperritos.bd.NivelDeActividad;
import com.example.alimentadordeperritos.bd.PerfilPerro;
import com.example.alimentadordeperritos.bd.Raza;
import com.example.alimentadordeperritos.bd.RutinaProgramada;
import com.example.alimentadordeperritos.ui.rutina.RutinaFragment;
import com.example.alimentadordeperritos.ui.rutina.RutinaViewModel;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    public static final int TODO_OK = 1;
    public static final int TODO_MAL = 0;

    private AppBarConfiguration mAppBarConfiguration;


    //bluetooth comentado por codigo de temporal activity
    //  BluetoothAdapter myBluetoothAdapter;
    //  Intent btEnablingIntent;
    //  int REQUEST_ENABLE_BLUETOOTH=1;
    //  boolean ACTIVAR_MENU;

    private PerfilPerro perfilActual; //Aca se guarda el perfil

    private RutinaProgramada rutinaProg; //Aca guardo la rutina calculada en t de ejecucion


    //PARA ENVIAR  INFO DEL MainActivity A UN FRAGMENT
    RutinaViewModel rutinaVM;

    //home-perfil
    //gallery-comida
    //slideshow-notificaciones
    //tools-recargar

    /*
     * pasos para agregar un fragment al menu
     * 1-agregar un fragment con viewModel
     * 2- agregar fragment nuevo en mobile-navigator.xml
     * 3- agregar nuevo archivo en la carpeta drawable
     * 4- agregar item en activity_main_drawer
     * 5- agregar un id text en la carpeta layout
     * 6- modificar las clases del fragment y el viewmodel del mismo fragment
     *
     * */


    //codigo de temporal activity
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //boton mail desactivado

        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_gallery, R.id.nav_home, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_rutina)//, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //creando objeto conectivitdad para bluetooth
     /*
        myBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        ACTIVAR_MENU=bluetoothONMethod();
*/


//metodo temporal activity
        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    DataStringIN.append(readMessage);

                    int endOfLineIndex = DataStringIN.indexOf("#");

                    if (endOfLineIndex > 0) {
                        String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                        //etTemp.setText("Dato: " + dataInPrint);//<-<- PARTE A MODIFICAR >->->
                        //Toast.makeText(getBaseContext(),"Dato: "  + dataInPrint, Toast.LENGTH_LONG).show();

                        //ESTE METODO PARSEA Y INSERTA ESTADISTICA EN BD
                        parsearCadenaDeArduino(dataInPrint);// ES LO QUE RECIBE DEL ARDUINO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        //ACA AGREGAR ENVIO DE ESTADISTICAS A FRAGMENT "HISTORIAL DE COMIDAS"
                        //ACA AGREGAR ENVIO DE ESTADISTICAS A FRAGMENT "HISTORIAL DE NOTIFICACIONES"
                        DataStringIN.delete(0, DataStringIN.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter(); // get Bluetooth adapter
        VerificarEstadoBT();

        //recibir info
        if (recibirPerfil() == TODO_MAL) //ACA RECIBO EL PERFIL CREADO EN ActivtyInicio
            Toast.makeText(getApplicationContext(), "No recibi perfil, te falta programar esta parte.", Toast.LENGTH_LONG).show();
        else {
            //SE CALCULA LA RUTINA DEL PERRO
            rutinaProg = calcularRutinaProgramada(perfilActual);


            prepararCadenayEnviar();


            // set Fragmentclass Arguments
            RutinaFragment fragobj = new RutinaFragment();
            /****************/


            Bundle bundle = new Bundle();
            bundle.putString("valuesArray", "pepeeee");
            fragobj.setArguments(bundle);
            /**********************/
            getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, fragobj).commit();

        }//else

/*
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date diaActual = new Date();
        Calendar fecha_estadistica= Calendar.getInstance();
        fecha_estadistica.setTime(diaActual);

        Log.d("fecha antes de restar",dateFormat.format(fecha_estadistica.getTime()));
        fecha_estadistica.add(Calendar.DAY_OF_YEAR, -1); //horasASumar es int.
        Log.d("fecha Despues de restar",dateFormat.format(fecha_estadistica.getTime()));

        dateFormat.format(fecha_estadistica.getTime());


        Date currentTime = Calendar.getInstance().getTime();



        Estadisticas e = new Estadisticas(23,1,0,35,1,500,dateFormat.format(fecha_estadistica.getTime()));

       Long l= e.insertar_estadistica(getApplicationContext());
       // Toast.makeText(getApplicationContext(),l.toString()+"",Toast.LENGTH_LONG).show();
/*
        List<Integer> vec= new ArrayList<Integer>();

        vec =e.leer_estadistica(dateFormat.format(fecha_estadistica.getTime()),getApplicationContext());

        for ( Integer v:vec) {
            //Toast.makeText(getApplicationContext(),v.toString(),Toast.LENGTH_LONG).show();
            Log.d("MUESTRA CAMPOS",v.toString() );
        } */

        //------------------>PRUEBA PARSEAR CADENA DE ARDUINO
        //  parsearCadenaDeArduino("0|1|0|31|400");
    /*for(Date element: rutinaProg.getHorario()){
     Log.e("------------_>",element.toString());
    }*/

    }//onCreate

    private void prepararCadenayEnviar() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date diaActual = new Date();
        Calendar fecha_estadistica= Calendar.getInstance();
        String array2 = new String();

        for(Date elem : rutinaProg.getHorario()){
            fecha_estadistica.setTime(elem);

            array2 = array2.concat("\n"+ dateFormat.format(fecha_estadistica.getTime()));
        }
        //ENVIAR INFO DE ACTIVITY A UN FRAGMENT

        rutinaVM = ViewModelProviders.of(this).get(RutinaViewModel.class);
        rutinaVM.init();
        rutinaVM.sendMessage("ComidaTotalxDia: " + rutinaProg.getCantidadXDia()+ " gramos." + array2  );
    }

    private void parsearCadenaDeArduino(String dataInPrint) {

        String aux = dataInPrint;
        int servoTrabado = -1, perroComioFueraTiempo = -1, perroComioRapido = -1, cantidadPlato = -1, comidaDepo = -1;
        int inicioCadena = 0;
        int numeroDeCampoEstadistica = 0;
        for (int i = 0; i < aux.length(); i++) {
            if (aux.charAt(i) == '|') {
                switch (numeroDeCampoEstadistica) {
                    case 0:
                        servoTrabado = Integer.parseInt(aux.substring(inicioCadena, i));
                        break;
                    case 1:
                        perroComioFueraTiempo = Integer.parseInt(aux.substring(inicioCadena, i));
                        break;
                    case 2:
                        perroComioRapido = Integer.parseInt(aux.substring(inicioCadena, i));
                        break;
                    case 3:
                        cantidadPlato = Integer.parseInt(aux.substring(inicioCadena, i)); //cantidadConsumida = comidaxRacion - comidaPlato no es asi?

                        break;
                    case 4:
                        comidaDepo = Integer.parseInt(aux.substring(inicioCadena, i));
                        break;
                    default:
                        break;

                }

                inicioCadena = i + 1;
                numeroDeCampoEstadistica++;
            }

        }

        comidaDepo = Integer.parseInt(aux.substring(inicioCadena, aux.length()));

        double cantidadConsumida = 0;
        //if(cantidadPlato!=-1)
        // cantidadConsumida = rutinaProg.getCantidadxRacion() - cantidadPlato;


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date diaActual = new Date();
        Calendar fecha_estadistica = Calendar.getInstance();
        fecha_estadistica.setTime(diaActual);


        Estadisticas e = new Estadisticas(1, servoTrabado, perroComioFueraTiempo, cantidadPlato/*cantidadConsumida*/, perroComioRapido, comidaDepo, dateFormat.format(fecha_estadistica.getTime()));

        e.insertar_estadistica(getApplicationContext());

    }

    private RutinaProgramada calcularRutinaProgramada(PerfilPerro perfilActual) {

        //FORMULAS
        /* ComidaTotalxDia = (300 * Raza * estado) / na * edad */

        /* ComidaxRacion = ComidaTotalxDia / edad */

        /* CantidadAperturas = ComidaxRacion / 30 gr */

        //DECLARACIONES
        int codigoTamanioRaza = -1, year = -1, month = -1, day = -1, edad = -1;
        double codigoEstado = -1, codigoNA = -1;

        //CONVERSIONES
        codigoEstado = Estado.valueOf(perfilActual.getEstado()).getCodigoEstado();

        codigoNA = NivelDeActividad.valueOf(perfilActual.getNa()).getCodigoNA();

        codigoTamanioRaza = perfilActual.getRaza().getTamanio().getCodigoTamanio();

        year = perfilActual.getFechaNac().getYear() + 1900; //se le suma 1900 porque getYear funciona para el or..
        month = perfilActual.getFechaNac().getMonth() + 1;
        day = perfilActual.getFechaNac().getDate();

        edad = Integer.parseInt(getAge(year, month, day));

        //CONTROL CONVERSIONES
        if (codigoEstado == -1 || codigoNA == -1 || codigoTamanioRaza == -1 || year == -1 || month == -1 || day == -1 || edad == -1)
            return new RutinaProgramada(-1, -1, null); //Error en las conversiones

        //CONTROL DIVISION POR CERO
        if ((codigoNA * edad) == 0)
            return new RutinaProgramada(-2, -2, null); //Error division por cero!

        //CALCULO ComidaTotalxDia
        int edadD = calcularReferenteEdad(edad); //CALCULA LA EDAD DE LA DOSIS EN BASE A LA EDAD DEL PERRO

        double resxDia = (360 * codigoTamanioRaza * codigoEstado) / (codigoNA * edadD); //FORMULA

        //CALCULO ComidaxRacion
        double resxRacion = resxDia / edad;

        //CALCULO Horarios
        List<Date> horarios = new ArrayList<Date>();
        int cantRaciones = 3;//TEMPORAL, COMO SE CALCULA?

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
        return new RutinaProgramada(resxRacion, resxDia, horarios);
    }

  public Date obtenerHorarioActual(String formato){

      DateFormat dateFormat = new SimpleDateFormat(formato);
      Date horaActual = new Date();
       return horaActual;
  }

    private int calcularReferenteEdad(int edad) {

        if (edad == 1)
            return 4;

        if (edad >= 2 && edad <= 10)
            return 3;

        return 2;
    }

    //CALCULA EDAD
    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    //RECIBE EL PERFIL DE PERRO DE ActivityInicio
    private int recibirPerfil() {

        Bundle myBundle = this.getIntent().getExtras();
        String nombre, raza, na, estado, fnac;
        Integer peso;
        Date date;

        if (myBundle != null) {
            nombre = myBundle.getString("nombre");
            peso = Integer.parseInt(myBundle.getString("peso"));
            raza = myBundle.getString("raza");
            na = myBundle.getString("na");
            estado = myBundle.getString("estado");
            fnac = myBundle.getString("fnac");

            Toast.makeText(getApplicationContext(), "Recibi info: " + nombre + " " + peso + " " + raza + " " + na + " " + estado + " " + fnac, Toast.LENGTH_LONG).show();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy"); //Le indico el formato que tiene el string "fnac"
            try {
                date = format.parse(fnac); //Convierte el string "fnac" al tipo Date
                perfilActual = new PerfilPerro(1, peso, new Raza(raza), na, estado, date, nombre); //perfilPerro tiene un atributo tipo Date
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return TODO_OK;
        }

        return TODO_MAL;
    }//recibirPerfil



     /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BLUETOOTH)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(getApplicationContext(),"Bluetooth Activado",Toast.LENGTH_LONG).show();

            }else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(),"Fue cancelada la activacion del Bluetooth.La app no funcionara",Toast.LENGTH_LONG).show();
                ACTIVAR_MENU=false;

            }
        }
    }


    private boolean bluetoothONMethod() {
        if (myBluetoothAdapter == null)
        {
            Toast.makeText(getApplicationContext(),"Bluetooth no es soportado por este dispositivo. La app dejo se ejecutarse",Toast.LENGTH_LONG).show();
            finishAffinity();

        }
        else
        {
            if(!myBluetoothAdapter.isEnabled())
            {
                btEnablingIntent=new Intent (BluetoothAdapter.ACTION_REQUEST_ENABLE);

                startActivityForResult(btEnablingIntent,REQUEST_ENABLE_BLUETOOTH); //Este metodo invoca a onActivityResult?

            }

        }
        return true;
    }
*/ // reemplazado por el codigo de temporal activity

    //ESTOS 2 METODOS SE CREAN POR DEFECTO AL ELEGIR LA ACTIVITY CON NAVIGATION DRAWER
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        //  if (ACTIVAR_MENU)
        // {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
        //}
        // return false;
    }
    /*
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
      return true;
    }*/

    /*
        public void salir(View view)
        {
            //finish();
            Toast.makeText(getApplicationContext(),"saliendo...",Toast.LENGTH_LONG).show();

        }

     */
    public void onBackPressed() {
        // Toast.makeText(getApplicationContext(),"No se puede ",Toast.LENGTH_LONG).show();
        //System.exit(0);

        finishAffinity(); //SIRVE PARA FINALIZAR LA APP DESDE CUALQUIER ACTIVITY

    }

//codigo de temporal activity


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        //crea un conexion de salida segura para el dispositivo
        //usando el servicio UUID
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Consigue la direccion MAC desde DeviceListActivity via intent

        try {
            Intent intent = getIntent();
            //Consigue la direccion MAC desde DeviceListActivity via EXTRA
            address = intent.getStringExtra(DispositivosBT.EXTRA_DEVICE_ADDRESS);//<-<- PARTE A MODIFICAR >->->
            //Setea la direccion MAC
            BluetoothDevice device = btAdapter.getRemoteDevice(address);


            btSocket = createBluetoothSocket(device);
        } catch (Exception e) {
            Log.e("intentttttt", e.getMessage());

            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try {
            btSocket.connect();
        } catch (IOException e) {
            Log.e("<<SK>>", e.getMessage());
            Toast.makeText(getBaseContext(), "La conexion fallo", Toast.LENGTH_LONG).show();
            try {
                btSocket.close();
            } catch (IOException e2) {

            }
        }


        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        try { // Cuando se sale de la aplicación esta parte permite
            // que no se deje abierto el socket
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    //Comprueba que el dispositivo Bluetooth Bluetooth está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexion
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //Envio de trama
        public void write(String input) {
            byte msgBytes[] = input.getBytes();
            try {
                mmOutStream.write(msgBytes);
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }

    } //Clase ConnectedThread extends

    public class sendToArduinoThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public sendToArduinoThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

     public void run(){
         int i=0;

         //NO OLVIDAR RECORRER ARRAY DE HORARIOS Y SETEAR EL I



         //if (date1.compareTo(date2) > 0)
             for(int j=0 ; j<rutinaProg.getHorario().size() ; j++){

             if(rutinaProg.getHorario().get(j).compareTo(obtenerHorarioActual("HH:mm"))>0)
             {   i = j;
                 break;
              }
             }//for
         DateFormat dateFormat = new SimpleDateFormat("HH:mm");
         Date horaActual;

         while(true){

             horaActual =  new Date();

        if(ConvertirHorario(horaActual).equals(ConvertirHorario(rutinaProg.getHorario().get(i)))){

            String cantAperturas = retornarClaveArduino(rutinaProg.getCantidadxRacion()/30);

            MyConexionBT.write(cantAperturas);

            i++;
        }
             SimpleDateFormat sdfa = new SimpleDateFormat("HH:mm");

             Date horaTurbiaFeliz = new Date(Date.parse("00:00"));


             if(obtenerHorarioActual("HH:mm").compareTo(horaTurbiaFeliz) == 0 ){
                 i=0;
                 Calendar fecha_estadistica= Calendar.getInstance();
                 fecha_estadistica.setTime(obtenerHorarioActual("dd/MM/yyyy"));
                 fecha_estadistica.add(Calendar.DAY_OF_YEAR, -1); //horasASumar es int.
                 List<Integer> arrayComioRapido = Estadisticas.leer_estadistica(dateFormat.format(fecha_estadistica.getTime()),getApplicationContext());
                 rutinaProg.recalcularRutina(arrayComioRapido);
                 prepararCadenayEnviar();
             }

      }//while
    }

        public void write(String input) {
            byte msgBytes[] = input.getBytes();
            try {
                mmOutStream.write(msgBytes);
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
  }

    public String ConvertirHorario(Date date)
    {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date diaActual = date;
        Calendar hora= Calendar.getInstance();
        hora.setTime(diaActual);

        return dateFormat.format(hora.getTime());
    }

    public String retornarClaveArduino(double valor){
        int auxiliar = (int) valor;
        String pan="puto";
        switch(auxiliar){

            case 1: pan = "a";
            case 2: pan = "b";
            case 3: pan = "c";
        }
       return pan;
    }

}//activity

