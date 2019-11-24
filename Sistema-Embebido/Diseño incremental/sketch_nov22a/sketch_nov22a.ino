#include <Servo.h>
#include <HX711.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#define DOUT A1
#define CLK A0
HX711 balanza(DOUT, CLK);
Servo servoMotor;
SoftwareSerial mySerial(10, 9); //rx,tx

const int LEDPin = 12;
const int PIRPin = 7;
const int BUZZERpin = 8;
const int Trigger = 4; //Pin digital 2 para el Trigger del sensor
const int Echo = 2;    //Pin digital 4 para el Echo del sensor

int perro_comio_rapido_mandar_datos = 0;
int primer_chequeo = 0;
int segundo_chequeo = 0;
int tiempo_inicial = 0;
int tiempo_final = 0;

int banderaLecturaInicial = 0;
int lecturaInicial = 0;
int operacion_general = 0;
char cadena_recibida_bluetooth = 'z';
int cantidad_de_aperturas_servo = 0;
int vector_comio_rapido[20] = {0}; //={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
int ultima_posicion_insercion_vector = 0;
int bandera_servo_trabado = 0;
int lectura_restos_comida = 0;
int servo_trabado_mandar_datos = 0;
int perro_comio_fuera_de_timepo_mandar_datos = 0;
int lectura_restos_comida_mandar_datos = 0;
int distancia_ultrasonido_mandar_datos = 0;

int tiempo_inicial_mandar_datos = 0;
int tiempo_final_mandar_datos = 0;
int lectura_peso_inicial_comida_agregada = 0;
String cadena_a_enviar = "";

int estado = 0 ;
int actividad = 0;
int actividad1 = 0;
int actividad2 = 0;

void setup()
{
    Serial.begin(9600);
    mySerial.begin(9600);
    Serial.println("No ponga ningun objeto sobre la balanza.");
    Serial.println("Destarando...");
    balanza.set_scale(); //La escala por defecto es 1
    balanza.tare(20);    //El peso actual es considerado Tara.
    Serial.println("Midiendo el peso inicial del plato...");
    servoMotor.attach(6); // Iniciamos el servo para que empiece a trabajar con el pin 6
    servoMotor.write(0);  //Inicializamos el servo en angulo 0.
    pinMode(LEDPin, OUTPUT);
    pinMode(PIRPin, INPUT);
    pinMode(BUZZERpin, OUTPUT); //definir pin como salida
    pinMode(Trigger, OUTPUT);   //pin como salida
    pinMode(Echo, INPUT);       //pin como entrada
    digitalWrite(Trigger, LOW); //Inicializamos el pin con 0
    tiempo_inicial = millis();
    digitalWrite(BUZZERpin, LOW);
    Serial.println("Setup...");
}

void loop() {

    switch(estado){

      case 0: //LECTURA INICIAL SENSORES

        switch(actividad){

          case 0: //LECTURA BLUETOOTH
            actividad = leer_bluetooth();
            tiempo_inicial = millis();
            
            break;

          /*case 1: //CHEQUEO RESTOS DE COMIDA  *****ESTE NO VA ACA*****
            chequear_restos_comida();

            break;
          */
          case 1: //Chequeo ultimas comidas
            actividad = chequear_vector_ultimas_comidas();

            break;
          case 2: //CHEQUEO PESO INICIAL BALANZA
            actividad = chequear_peso_inicial_comida_agregada();
            tiempo_inicial = millis();
            tiempo_inicial_mandar_datos = millis();

            break ;

          default:break;
        
          }


      case 1: //ALIMENTANDO

        switch(actividad1){

          case 0: //abrir una servo 1 vez
            actividad1 = abrir_servo_1_vez();

            break;
          case 1: //cerrar servo 1 vez
            actividad1 =cerrar_servo_1_vez();

            break;
          case 2: //abrir servo 2 veces
            actividad1 =abrir_servo_2_veces();

            break;
          case 3: //cerrar servo 2 veces
            actividad1 =cerrar_servo_2_veces();

            break;
          case 4: //abrir servo 3 veces
            actividad1 =abrir_servo_3_veces();

            break;
          case 5: //cerrar servo 3 veces
            actividad1 =cerrar_servo_3_veces();

            break;

          case 6: //mide el peso de la comida largada por el servo
            actividad1 =medir_peso_inicial(); 

            break;

          case 7: //chequea que el servo no haya quedado trabado (son 2 funciones distintas para comparar valores)
            actividad1 =chequear_servo_primer_chequeo();

            break;

          case 8:
            actividad1 =chequear_servo_segundo_chequeo();

            break;

          case 9: //con el pir detecta si el perro esta, en ese caso se fija la balanza
                  //a ver si comio rapido, y en ese caso activa el buzzer
            actividad1 =detectar_presencia_y_alertar(); //UNIR PRENDER BUZZER Y APAGAR

            break ;

          case 10:
            actividad1 =prender_buzzer();

              break;

          case 11:
            actividad1 =apagar_buzzer();

              break;

          default:break;
          }
      

      case 2: //LECTURA FINAL SENSORES

        switch(actividad2){

          case 0://verifica si quedo comida despues de que el perro haya comida
            actividad2 = chequear_restos_comida(); 
            
            break;

          case 1://veriifca con el ultrasonido la cantidad de comida en el deposito
                 //si hay poca comida prende el led
            actividad2 = medir_cantidad_en_deposito_y_alertar(); //UNIR PRENDER Y APAGAR LED
            
            break;

          case 2:
            actividad2 = prender_led();

            break ;

          case 3: 
            actividad2 = apagar_led();

            break ;

          case 4: //
            actividad2 = detectar_perro_husmeando();

            break;

          default:break;
          }

      case 3: //ENVIANDO DATOS DESDE ARDUINO A ANDROID
        
        mandar_datos();

      break;

      default:
    break;
    }
}

int leer_bluetooth()
{
    //a=1 apertura, b= 2, c= 3
    Serial.println("Operacion: leer bluetooth.");
    if (mySerial.available() > 0)
    {
        Serial.println("Hay informacion disponible:");
        cantidad_de_aperturas_servo = 0;
        cadena_recibida_bluetooth = mySerial.read();
        Serial.println(cadena_recibida_bluetooth);
        
        if (cadena_recibida_bluetooth == 'a')
        {
            cantidad_de_aperturas_servo = 1;
            return 1;
        }

        if (cadena_recibida_bluetooth == 'b')
        {
            cantidad_de_aperturas_servo = 2;
            return 1;
        }

        if (cadena_recibida_bluetooth == 'c')
        {
            cantidad_de_aperturas_servo = 3;
            return 1;
        }
        if (cadena_recibida_bluetooth == 'd')
        {
            tiempo_inicial = millis();
            prender_buzzer();
            //return 13;
        }
        if (cadena_recibida_bluetooth == 'e')
        {
            tiempo_inicial = millis();
            apagar_buzzer();
            //return 17;
        }
        cadena_recibida_bluetooth = 'z';
    }
    else
        return 1;
}

int mandar_datos() 
{                  //bandera mandar datos
    char cadena_convertida[10] = " ";
    cadena_a_enviar = " ";

    itoa(servo_trabado_mandar_datos, cadena_convertida, 10);
    cadena_a_enviar = cadena_a_enviar + cadena_convertida + "|";//Primer campo: si el servo esta trabado

    itoa(perro_comio_fuera_de_timepo_mandar_datos, cadena_convertida, 10);
    cadena_a_enviar = cadena_a_enviar + cadena_convertida + "|";//Segundo campo: si el perro comio fuera de tieempo

    itoa(perro_comio_rapido_mandar_datos, cadena_convertida, 10);
    cadena_a_enviar = cadena_a_enviar + cadena_convertida + "|";//Tercer campo: si el perro comio rapido

    if (lectura_restos_comida_mandar_datos < 0)
        lectura_restos_comida_mandar_datos = 0;
    itoa(lectura_restos_comida_mandar_datos / 435, cadena_convertida, 10);
    cadena_a_enviar = cadena_a_enviar + cadena_convertida + "|";//Cuarto campo: restos de comida en balanza

    int calculo = 10 * (100 - ((distancia_ultrasonido_mandar_datos - 5) * 5));//Para calcular el peso que tiene el deposito
    itoa(calculo, cadena_convertida, 10);
    cadena_a_enviar = cadena_a_enviar + cadena_convertida + '#';//Quinto campo: peso del deposito

    Serial.println("Cadena a enviar por BT: ");
    Serial.println(cadena_a_enviar);
    mySerial.print(cadena_a_enviar);
    distancia_ultrasonido_mandar_datos = 0;//reinicio datos
    lectura_restos_comida_mandar_datos = 0;
    perro_comio_rapido_mandar_datos = 0;
    servo_trabado_mandar_datos = 0;
    perro_comio_fuera_de_timepo_mandar_datos = 0;

    return 0;
}



int abrir_servo_1_vez()
{
    Serial.println("abriendo el servo...1");
    servoMotor.write(135);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        Serial.println("abrir servo 1");
        tiempo_inicial = millis();
        return 1;
    }
    else
        return 0;
}

int cerrar_servo_1_vez()
{
    Serial.println("cerrando el servo...1");
    servoMotor.write(0);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        Serial.println("cerrar servo 1");
        tiempo_inicial = millis();
        if (cantidad_de_aperturas_servo == 1)
        {
            return 6;
        }
        else
            return 2;
    }
    else
        return 1;
}

int abrir_servo_2_veces()
{
    Serial.println("abrir servo2");

    servoMotor.write(135);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        Serial.println("abrir servo 2");
        tiempo_inicial = millis();
        return 3;
    }
    else
        return 2;
}

int cerrar_servo_2_veces()
{
    Serial.println("cerrar servo2");

    servoMotor.write(0);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        Serial.println("cerrar servo2");
        tiempo_inicial = millis();
        if (cantidad_de_aperturas_servo == 2)
            return 6;
        else
            return 4;
    }
    else
        return 3;
}

int abrir_servo_3_veces()
{
    Serial.println("abrir servo3");

    servoMotor.write(135);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        Serial.println("abrir servo3");
        tiempo_inicial = millis();
        return 5;
    }
    else
        return 4;
}

int cerrar_servo_3_veces()
{
    Serial.println("cerrar servo3");

    servoMotor.write(0);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        Serial.println("cerrar servo3 ");
        tiempo_inicial = millis();
        return 6;
    }
    else
        return 5;
}

int chequear_restos_comida()
{
    Serial.println("Peso de los restos de comida:  ");
    lectura_restos_comida = balanza.get_value(10);
    Serial.println(lectura_restos_comida);
    lectura_restos_comida_mandar_datos = lectura_restos_comida;
    return 1;
}


int chequear_vector_ultimas_comidas()
{
    Serial.println("Operacion: chequeo de ultimas comidas.");
    if (ultima_posicion_insercion_vector > 3) //aca ya hubo 3 comidas del perro
    {
        Serial.println("Hubo mas de 3 comidas.");

        if (vector_comio_rapido[ultima_posicion_insercion_vector - 1] == 1 && vector_comio_rapido[ultima_posicion_insercion_vector - 2] == 1 && vector_comio_rapido[ultima_posicion_insercion_vector - 3] == 1)
        {
            Serial.println("De las ultimas 3 comidas, todas fueron rapidas...se enviara menos comida la proxima como castigo!");
            cantidad_de_aperturas_servo = 1;
        }
    }
    return 3;
}

int chequear_peso_inicial_comida_agregada() //si hay comida agregada, abro una sola vez el servo
{
    Serial.print("Valor de balanza para ver si hay comida agregada de antes de usar el alimentador:  ");
    lectura_peso_inicial_comida_agregada = balanza.get_value(10);
    Serial.println(lectura_peso_inicial_comida_agregada);
    if (lectura_peso_inicial_comida_agregada > 20000) //quiere decir que alguien le agrego comida, entonces abro una sola vez el servo
    {
        Serial.println("Alguien agrego comida! Ahora la cantidad de aperturas sera menor.");
        cantidad_de_aperturas_servo = 1;
    }
    //return 3;
    estado = 1;
}

int chequear_servo_primer_chequeo() //si esta abierto, en 2 lecturas tiene que haber caido mucha comida, lo puedo saber con el Ultra sonido
{
    digitalWrite(Trigger, HIGH);
    delayMicroseconds(10); //Enviamos un pulso de 10us
    digitalWrite(Trigger, LOW);
    int t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
    primer_chequeo = t / 59;
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 2000)
    {
        tiempo_inicial = millis();
        return 8;
    }
    else
        return 7;
}

int chequear_servo_segundo_chequeo() //si esta abierto, en 2 lecturas tiene que haber caido mucha comida, lo puedo saber con el US
{
    digitalWrite(Trigger, HIGH);
    delayMicroseconds(10); //Enviamos un pulso de 10us
    digitalWrite(Trigger, LOW);
    int t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
    segundo_chequeo = t / 59;
    if (segundo_chequeo < primer_chequeo - 4)
    {
        bandera_servo_trabado = 1;
        return 9;
    }
}

int medir_peso_inicial()
{
    if (banderaLecturaInicial == 0)
    {
        Serial.print("Peso inicial de la balanza:");
        lecturaInicial = balanza.get_value(10);
        Serial.println(lecturaInicial);
        banderaLecturaInicial = 1;
        Serial.println("Contando 5 segundos para que coma el perro...");
    }
    tiempo_final = millis() - tiempo_inicial;

    if (tiempo_final > 5000) //5 segundos para sacar la comida
    {
        banderaLecturaInicial = 0;
        tiempo_inicial = millis();
        return 7;
    }
    else
        return 6;
}

int detectar_presencia_y_alertar()
{
    int value = digitalRead(PIRPin);
    Serial.println("Lectura del PIR:");
    Serial.println(value);
    //value = HIGH;
    if (value == HIGH)
    {
        int lecturaComiendo = balanza.get_value(10);
        Serial.println("Peso inicial de la balanza:");
        Serial.println(lecturaInicial);
        Serial.println("Peso actual de la balanza:");
        Serial.println(balanza.get_value(10), 0);                                                                                                                                  //no esta comiendo nada
        if (lecturaComiendo < lecturaInicial - 4000 ) //esta comiendo muy rapido
        {
            vector_comio_rapido[ultima_posicion_insercion_vector] = 1;//Lo guardo en el vector para luego obtener las ultimas 3 comidas del perro
            //y en caso de que haya comido rapido las ultimas 3 veces, se modifica el comportamiento del sistema
            ultima_posicion_insercion_vector++;
            tiempo_inicial = millis();
            
            return 10;
        }
        else
            return 15; //ACA TENGO QUE SALIR DEL SWITCH E IR AL OTRO
    }
    else     //en este punto, el perro se fue, podemos ver el peso final.
    {
        int lecturaFinal = balanza.get_value(10);
    }
    //return 15; //ACA TENGO QUE SALIR DEL SWITCH E IR AL OTRO
}

int detectar_perro_husmeando()
{
    int value = digitalRead(PIRPin);
    Serial.println("Lectura del PIR para ver si el perro esta husmeando:");
    Serial.println(value);
    if (value == HIGH)
    {
        int chequeo_balanza_perro_husmeando = balanza.get_value(10);
        Serial.println("Peso de los restos de comida:");
        Serial.println(lectura_restos_comida);
        Serial.println("Peso de la balanza final cuando el perro esta husmeando:");
        Serial.println(balanza.get_value(10), 0);                                                                                                                 
        if (chequeo_balanza_perro_husmeando < lectura_restos_comida - 4000) //esta comiendo muy rapido
        {
            perro_comio_fuera_de_timepo_mandar_datos = 1; //variable para mandar datos
        }
    }
    tiempo_final_mandar_datos = millis() - tiempo_inicial_mandar_datos;
    if (tiempo_final_mandar_datos > /*2000)*/ 600000) //10 min
    {
        tiempo_inicial_mandar_datos = millis();
        // ACA TENGO QUE SALIR DEL SWITCH E IR AL OTRO
        //return 20;
        estado = 2; 
    }
    estado = 2;
    //return 0;
}

int medir_cantidad_en_deposito_y_alertar()//Ultrasonido
{
    Serial.println("Operacion: medir el deposito.");
    long t; //timepo que demora en llegar el eco
    long d; //distancia en centimetros
    digitalWrite(Trigger, HIGH);
    delayMicroseconds(10); //Enviamos un pulso de 10us
    digitalWrite(Trigger, LOW);
    t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
    d = t / 59;              //escalamos el tiempo a una distancia en cm
    Serial.print("Distancia obtenida del ultrasonido: ");
    Serial.print(d); //Enviamos serialmente el valor de la distancia
    Serial.print("cm.");
    Serial.println();
    distancia_ultrasonido_mandar_datos = d;
    if (d > 17)
    {
        tiempo_inicial = millis();
       
        return 2;
    }
    else
    {   
        
        return 4;
    }
    return 4;
}

int prender_buzzer()
{
    Serial.println("Prendiendo buzzer...");
    digitalWrite(BUZZERpin, HIGH); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        perro_comio_rapido_mandar_datos = 1;
        tiempo_inicial = millis();
        return 11;
    }
    else
        return 10;
}

int apagar_buzzer()
{
    digitalWrite(BUZZERpin, LOW); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        tiempo_inicial = millis();
        //return 15; ACA TENGO QUE SALIR DEL SWITCH
    }
    else
        //return 10;
        estado = 2 ;
}

int prender_led()
{
    Serial.println("Prendiendo led...");
    digitalWrite(LEDPin, HIGH); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 5000)
    {
        tiempo_inicial = millis();
        return 3;
    }
    else
        return 2;
}

int apagar_led()
{
    Serial.println("Apagando led...");
    digitalWrite(LEDPin, LOW);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 500)
    {
        tiempo_inicial = millis();
        return 4;
    }
    else
        return 3;
}
