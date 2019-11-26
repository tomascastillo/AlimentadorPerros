package com.example.alimentadordeperritos.interfaz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

//IMPORT PARA SENSORES

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final int TODO_OK = 1;
    public static final int TODO_MAL = 0;

    private AppBarConfiguration mAppBarConfiguration;

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


    private static final int SENSIBILIDAD_SHAKE=25;

    //codigo de temporal activity
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ReceiveFromArduinoThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address = null;

    private SendToArduinoThread MyConexionBT_send;

    //VARIABLES PARA SENSORES
    private static final String TAG = "MainActivity";

    private SensorManager sensorManager ;
    private Sensor acelerometro, proximidad, luz  ;
    private float   acelVal ;   //CURRENTE ACCELERATION VALUE AND GRAVITY
    private float   acelLast ;  //LAST ACCELERATION VALUE AND GRAVITY
    private float   shake ;     //ACCELERATION VALUE differ FROM GRAVITY


    private int contadorAux=0;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        if (recibirPerfil() == TODO_MAL) {//ACA RECIBO EL PERFIL CREADO EN ActivtyInicio
            //Toast.makeText(getApplicationContext(), "No recibi perfil, te falta programar esta parte.", Toast.LENGTH_LONG).show();
            rutinaProg = rutinaHarcodeada(30, 4);
        }
        else {
            //SE CALCULA LA RUTINA DEL PERRO
            rutinaProg = calcularRutinaProgramada(perfilActual);

        }//else

        prepararCadenayEnviarAFragment(); //ENVIA INFO DE ACTIVITY ---> FRAGMENT

        // set Fragmentclass Arguments
        RutinaFragment fragobj = new RutinaFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, fragobj).commit();

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
         iniciarSensores(); //ACA INICIO LOS SENSORES

        //CODIGO PARA AGREGAR ESTADISTICAS A LA BD
        /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date diaActual = new Date();
        Calendar fecha_estadistica= Calendar.getInstance();
        fecha_estadistica.setTime(diaActual);
        fecha_estadistica.add(Calendar.DAY_OF_YEAR, -1);

        Estadisticas e = new Estadisticas(23,1,0,35,1,500,dateFormat.format(fecha_estadistica.getTime()));

         e.insertar_estadistica(getApplicationContext());
        e.insertar_estadistica(getApplicationContext());
        e.insertar_estadistica(getApplicationContext());*/
    }//onCreate

    private RutinaProgramada rutinaHarcodeada(int cantXRacion,int cantRaciones)
    {

        List<Date> horario= new ArrayList<Date>() ;


        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date horaBase = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaBase); //recibe un Date;


        for (int i = 0; i < cantRaciones; i++) {
            calendar.add(Calendar.MINUTE, 1); //Suma 2 minutos por cada rutina

            horario.add(calendar.getTime());
        }



        return new RutinaProgramada(cantXRacion,cantXRacion*cantRaciones,horario);
    }

    public void iniciarSensores(){
       Log.d(TAG,"onCreate: iniciando sensor services");

       // Creamos el objeto para acceder al servicio de sensores
       sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

       // Iniciando sensores
       acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       if(acelerometro != null){

           sensorManager.registerListener(MainActivity.this,acelerometro,SensorManager.SENSOR_DELAY_NORMAL);
           Log.d(TAG,"onCreate: registered acelerometro listener") ;

       }
       else{

           Toast.makeText(this, "Sensor acelerometro no soportado", Toast.LENGTH_SHORT).show();
       }

       proximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
       if(proximidad != null){

           sensorManager.registerListener(MainActivity.this,proximidad,SensorManager.SENSOR_DELAY_NORMAL);
           Log.d(TAG,"onCreate: registered proximidad listener") ;

       }
       else{
           Toast.makeText(this, "Sensor de proximidad no soportado", Toast.LENGTH_SHORT).show();

       }

       luz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
       if(luz != null){

           sensorManager.registerListener(MainActivity.this,luz,SensorManager.SENSOR_DELAY_NORMAL);
           Log.d(TAG,"onCreate; registered luz listener");
       }
       else{
           Toast.makeText(this, "Sensor de luz no soportado", Toast.LENGTH_SHORT).show();
       }

       //MAGIA
       acelVal = SensorManager.GRAVITY_EARTH;
       acelLast = SensorManager.GRAVITY_EARTH;
       shake = 0.00f ;
   }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Metodo que escucha el cambio de los sensores
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;

        // Cada sensor puede lanzar un thread que pase por aqui
        // Para asegurarnos ante los accesos simultaneos sincronizamos esto

        synchronized (this) {

            switch (sensor.getType()) {

                case Sensor.TYPE_ACCELEROMETER:

                    float x = sensorEvent.values[0] ;
                    float y = sensorEvent.values[1] ;
                    float z = sensorEvent.values[2] ;

                    acelLast = acelVal ;
                    acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z) );
                    float delta = acelVal - acelLast ;
                    shake = shake * 0.9f + delta;

                    if(shake > SENSIBILIDAD_SHAKE){

                        //DO WHAT YOU WANT ****aca se mandaria una "a" por bluetooth****
                        MyConexionBT_send.write("a");
                        Toast toast = Toast.makeText(getApplicationContext(),"DO NOT SHAKE ME "+contadorAux, Toast.LENGTH_LONG);
                        toast.show();
                        contadorAux++;
                    }

                    break;

                case Sensor.TYPE_PROXIMITY:

                    if(sensorEvent.values[0]<sensor.getMaximumRange()){
                        //DO WHAT YOU WANT ****aca se mandaria una "d" por bluetooth****
                        MyConexionBT_send.write("d");
                        Toast.makeText(this, "Proximidad activado", Toast.LENGTH_SHORT).show();
                    }

                    break;

                case Sensor.TYPE_LIGHT:
         //Toast.makeText(getApplicationContext(),"--> "+sensorEvent.values[0],Toast.LENGTH_LONG).show();
                    /*luzValue.setText("Luz: "+sensorEvent.values[0]);
                     */ // si sensorEvent.values[0] == 0 -> no hay luz?
                    if(sensorEvent.values[0]<2.0/*<sensor.getMaximumRange()*/) {
                        //DO WHAT YOU WANT ****aca se mandaria una "e" por bluetooth****
                        MyConexionBT_send.write("e");
                        Toast.makeText(this, "LUZ", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }

        }

    }

    private void prepararCadenayEnviarAFragment() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date diaActual = new Date();
        Calendar fecha_estadistica= Calendar.getInstance();
        String array2 = new String();
        int cont=1;

        for(Date elem : rutinaProg.getHorario()){
            fecha_estadistica.setTime(elem);

            array2 = array2.concat("\nHorario "+cont+": "+ dateFormat.format(fecha_estadistica.getTime()));
            cont++;
        }
        //ENVIAR INFO DE ACTIVITY A UN FRAGMENT

        rutinaVM = ViewModelProviders.of(this).get(RutinaViewModel.class);
        rutinaVM.init();
        rutinaVM.sendMessage("ComidaTotalxDia: " + rutinaProg.getCantidadXDia()+ " gr.\n" + "ComidaxRacion: " + rutinaProg.getCantidadxRacion()+" gr.\n" + array2  );
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
        if(cantidadPlato!=-1)
         cantidadConsumida = rutinaProg.getCantidadxRacion() - cantidadPlato;


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date diaActual = new Date();
        Calendar fecha_estadistica = Calendar.getInstance();
        fecha_estadistica.setTime(diaActual);


        Estadisticas e = new Estadisticas(1, servoTrabado, perroComioFueraTiempo,(int) cantidadConsumida, perroComioRapido, comidaDepo, dateFormat.format(fecha_estadistica.getTime()));

        e.insertar_estadistica(getApplicationContext());

    }

    private RutinaProgramada calcularRutinaProgramada(PerfilPerro perfilActual) {

        //FORMULA
        /* ComidaTotalxDia = (360 * Raza * estado) / (na * edad) */


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

        double resxDia = (360 * codigoTamanioRaza * codigoEstado) / (codigoNA * edadD); //FORMULA, CALCULA LA CANTIDAD DE COMIDA EN GRAMOS X DIA


        //GET CantRaciones
        int cantRaciones = obtenerCantRaciones(codigoTamanioRaza,edadD);

        //CALCULO ComidaxRacion
        double resxRacion = resxDia / cantRaciones; // CALCULA LA CANTIDAD DE COMIDA EN GRAMOS X RACION


        //CALCULO Horarios
        List<Date> horarios = new ArrayList<Date>();

        int intervaloTiempo = 24 / cantRaciones;

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date horaBase = new Date();
        horaBase.setHours(0);
        horaBase.setMinutes(0);
        horaBase.setSeconds(0);
        //System.out.println("Hora actual: " + dateFormat.format(horaActual));

        //CREO UN OBJETO CALENDAR PARA SUMAR HORAS!
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaBase); //recibe un Date;


        for (int i = 0; i < cantRaciones; i++) {
            calendar.add(Calendar.HOUR, intervaloTiempo); //horasASumar es int.

            if(i == cantRaciones-1)
                calendar.add(Calendar.MINUTE,-5);

            horarios.add(calendar.getTime());
        }


        return new RutinaProgramada(resxRacion, resxDia, horarios);
    }

    private int obtenerCantRaciones(int codigoTamanioRaza, int edadD) {

        if (codigoTamanioRaza == 1 && edadD == 4)
            return 3;
        if (codigoTamanioRaza == 1 && edadD == 3)
            return 2;
        if (codigoTamanioRaza == 1 && edadD == 2)
            return 3;

        if (codigoTamanioRaza == 2 && edadD == 4)
            return 2;
        if (codigoTamanioRaza == 2 && edadD == 3)
            return 4;
        if (codigoTamanioRaza == 2 && edadD == 2)
            return 4;

        if (codigoTamanioRaza == 3 && edadD == 4)
            return 3;
        if (codigoTamanioRaza == 3 && edadD == 3)
            return 6;


            return 6;  //Ese return 6 es para ---> if (codigoTamanioRaza == 3 && edadD == 2)

    }


    public Date obtenerHorarioActual(){

      //DateFormat dateFormat = new SimpleDateFormat(formato);
      Date horaActual = new Date();

       return horaActual;
  }

    private int calcularReferenteEdad(int edad) {

        if (edad <= 1)
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

        if (myBundle.getBoolean("boolean")==false) {
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


        MyConexionBT = new ReceiveFromArduinoThread(btSocket);
        MyConexionBT.start(); //Ejecuta el hilo para recibir info de arduino

        MyConexionBT_send = new SendToArduinoThread((btSocket));
        MyConexionBT_send.start(); //Ejecuta el hilo para enviar info a arduino
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

       //HILO QUE RECIBE INFO DE ARDUINO
    private class ReceiveFromArduinoThread extends Thread {

        private final InputStream mmInStream;
        //private final OutputStream mmOutStream;

        public ReceiveFromArduinoThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            //OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                //tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            //mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    bytes = mmInStream.read(buffer); //Recibe una respuesta del socket ( con read se recibe la informacion que nos envia Arduino
                    String readMessage = new String(buffer, 0, bytes);
                    // Envia los datos obtenidos hacia el evento via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /*
        public void write(String input) {
            byte msgBytes[] = input.getBytes();
            try {
                mmOutStream.write(msgBytes);
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        } */

    }
      //HILO QUE ENVIA INFO A ARDUINO
    public class SendToArduinoThread extends Thread {

        //private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public SendToArduinoThread(BluetoothSocket socket) {
            //InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                //tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
           // mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

     public void run(){
         int i=0;

         //NO OLVIDAR RECORRER ARRAY DE HORARIOS Y SETEAR EL I



         //if (date1.compareTo(date2) > 0)
             for(int j=0 ; j<rutinaProg.getHorario().size() ; j++){

             if(rutinaProg.getHorario().get(j).compareTo(obtenerHorarioActual())>0)
             {   i = j;
                 break;
              }
             }//for
         DateFormat dateFormat = new SimpleDateFormat("HH:mm");
         Date horaActual;

         String strHoraActual, iHorarioRutina;

         boolean banderaHoraCero=true;
         while(true){
         try {
             horaActual = new Date();
              strHoraActual = ConvertirHorarioDateToString(horaActual);
              iHorarioRutina = ConvertirHorarioDateToString(rutinaProg.getHorario().get(i));

             if (strHoraActual.equals(iHorarioRutina)) //ACA COMPARO STRINGS
             {

                 String cantAperturas = retornarClaveArduino(rutinaProg.getCantidadxRacion() / 30);

                 MyConexionBT_send.write(cantAperturas);

                 i++;
                 banderaHoraCero=true;
             }


             if (strHoraActual.equals("00:00") && banderaHoraCero) {
                 i = 0;
                 //En el metodo de abajo, a la fecha actual le resto 1 dia(se pasa como parametro), porque necesito obtener las estadisticas del dia anterior para recalcular la rutina programada para el siguiente dia.
                 List<Integer> arrayComioRapido = Estadisticas.leer_estadistica(ConvertirFechaDateToString(new Date(),"dd/MM/yyyy",-1), getApplicationContext());

                 if(arrayComioRapido.size()!=0) {
                     rutinaProg.recalcularRutina(arrayComioRapido);
                     /*prepararCadenayEnviarAFragment();
                     RutinaFragment fragobj = new RutinaFragment();
                     getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragobj).commit();*/
                     updateCadenaySendtoFragment();
                 }
                 banderaHoraCero=false;
             }
             }catch (Exception e){
                  break;
                 }//catch
         }//while
    }//run
         //ESTE METODO SE USA CUANDO QUEREMOS ENVIAR ALGO A ARDUINO
        public void write(String input) {
            byte msgBytes[] = input.getBytes();
            try {
                //escribe una peticion en el socket ( con write se le envia un flujo de bytes a arduino )
                mmOutStream.write(msgBytes);
            } catch (IOException e) {
                //si no es posible enviar datos se cierra la conexión
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
  }//FIN CLASE HILO_SEND

    private void updateCadenaySendtoFragment() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date diaActual = new Date();
        Calendar fecha_estadistica= Calendar.getInstance();
        String array2 = new String();
        int cont=1;

        for(Date elem : rutinaProg.getHorario()){
            fecha_estadistica.setTime(elem);

            array2 = array2.concat("\nHorario "+cont+": "+ dateFormat.format(fecha_estadistica.getTime()));
            cont++;
        }
        //ENVIAR INFO DE ACTIVITY A UN FRAGMENT

        String cad = new String("ComidaTotalxDia: " + rutinaProg.getCantidadXDia()+ " gr.\n" + "ComidaxRacion: " + rutinaProg.getCantidadxRacion()+" gr.\n" + array2);

        RutinaFragment rutina = new RutinaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("rutina",cad);

        rutina.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,rutina,null);
        fragmentTransaction.detach(rutina);
        fragmentTransaction.attach(rutina);// CON DETACH Y ATTACH SE REFRESCAN LAS VISTAS DEL FRAGMENT
        fragmentTransaction.commit();
        //rutina.onResume();
    }

    public String ConvertirHorarioDateToString(Date date)
    {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar hora = Calendar.getInstance();
        hora.setTime(date);

        return dateFormat.format(hora.getTime());
    }

    public String ConvertirFechaDateToString(Date date,String formato,int cantDiasASumar)
    {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(cantDiasASumar!=0)
        calendar.add(Calendar.DAY_OF_YEAR,cantDiasASumar);

        return dateFormat.format(calendar.getTime());
    }

    public String retornarClaveArduino(double valor){
        int auxiliar = (int) Math.round(valor); //metodo para redondear un double a un int
        String pan="a";
        switch(auxiliar){

            case 1: pan = "a"; break;
            case 2: pan = "b"; break;
            case 3: pan = "c"; break;
        }
       return pan;
    }

}//activity

