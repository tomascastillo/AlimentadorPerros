// Incluímos la librería para poder controlar el servo
#include <Servo.h>
#include <HX711.h>
#include <Wire.h>
#include <SoftwareSerial.h>

#define DOUT A1
#define CLK A0
/*
Si uso el millis() para el servo no funciona, es tan rapido que nunca llega a la posicion final del servo, nunca llega a escribir el 135 y de nuevo el 0,
necesitamos un delay porque ese paso es secuencial. ademas adentro del if tenemos una bandera
lo mismo con el buzzer, activo el buzzer en HIGH y tengo que esperar un segundo si o si para ponerlo en low
*/
HX711 balanza(DOUT, CLK);
// Declaramos la variable para controlar el servo
Servo servoMotor;
SoftwareSerial mySerial(10, 9);//rx,tx
int perro_comio_rapido_mandar_datos=0;
int primer_chequeo=0;
int segundo_chequeo=0;
int bandera = 0;
int contador = 0;
int tiempo_inicial = 0;
int tiempo_final = 0;
int tiempo_inicial2 = 0;
int tiempo_final2 = 0;
const int LEDPin = 12;
const int PIRPin = 7;
int banderaBluetooth = 0;
const int BUZZERpin = 8;
const int Trigger = 4; //Pin digital 2 para el Trigger del sensor
const int Echo = 2;    //Pin digital 3 para el Echo del sensor
int banderaLecturaInicial = 0;
int lecturaInicial = 0;
int operacion_alimentar = 0;
int operacion_buzzer = 99;
int operacion_ultrasound = 99;
int operacion_general = 0;
int banderaYaSonoBuzzer = 0;
char cadena_recibida_bluetooth = 'z';
int cantidad_de_aperturas_servo = 0;
int bandera_servo = 0;
int bandera_bluetooth = 0;
int vector_comio_rapido[20]={0};//={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
int iterador=0;
int ultima_posicion_insercion_vector=0;
int bandera_servo_trabado=0;
int lectura_restos_comida=0;
int servo_trabado_mandar_datos=0;
int perro_comio_fuera_de_timepo_mandar_datos=0;
int lectura_restos_comida_mandar_datos=0;
int distancia_ultrasonido_mandar_datos=0;
String cadena_a_enviar="";
int tiempo_inicial_mandar_datos=0;
int tiempo_final_mandar_datos=0;
int lectura_peso_inicial_comida_agregada=0;
/*
1. servo
2. balanza
3. pir
4. buzzer
5. todo el tiempo US+LED
*/

void setup()
{
  Serial.begin(9600);
  mySerial.begin(9600);
  //Serial.print("Lectura del valor del ADC:  ");
  //Serial.println(balanza.read());
  Serial.println("No ponga ningun objeto sobre la balanza");
  Serial.println("Destarando...");
  balanza.set_scale(); //La escala por defecto es 1
  balanza.tare(20);    //El peso actual es considerado Tara.
  Serial.println("Coloque un peso conocido, empezamos midiendo solo el peso del plato.");
  // Iniciamos el servo para que empiece a trabajar con el pin 9
  servoMotor.attach(6);
  servoMotor.write(0);
  pinMode(LEDPin, OUTPUT);
  pinMode(PIRPin, INPUT);
  pinMode(BUZZERpin, OUTPUT); //definir pin como salida
  pinMode(Trigger, OUTPUT);   //pin como salida
  pinMode(Echo, INPUT);       //pin como entrada
  digitalWrite(Trigger, LOW); //Inicializamos el pin con 0
  tiempo_inicial = millis();
  digitalWrite(BUZZERpin, LOW);
  Serial.println("setup");

}

void loop()
{
  //agregar chequeo de peso inicial, chequeo de que no este trabado el servo, 
  //chequeo de sensores constante, ver si quedo comida cuando el perro se fue
  //diagrama de estados
  //mandarle datos a la app
  //si no hay info disponible, chequeo que el perro no este con el pir, si esta lo mando, y tambien mido la balanza, si hay variacion de la balanza es que el perro vino a comer
  //a 1 vez, b 2 veces, c 3 veces
  switch (operacion_general)
  {
  case 0:
    Serial.println("case 0");
    //tiene que ver con el tema de que escribo 135 muchas veces, pero con delay funciona porq escribe 1 sola vez, espera los 3 seg
    operacion_general = leer_bluetooth(); //si hay info disponible hace lo que te pidan, caso contrario volve a esperar info
    //si hay info disponible hace lo que te piden, caso contrario volve a esperar info a menos que hayas llegado a 135. si no llego a 135, no volver a pedir info.
    //eso lo podemos saber una vez que pasaron los 3 segundos, blanquear la variable de cadena recibida con una letra z. si la variable es igual a z, pasaron los
    //3 segundos y puedo volver a pedir info.
    tiempo_inicial=millis();

    break;
  case 1:
    operacion_general=chequear_vector_ultimas_comidas();
    break;
  case 2:
    operacion_general=chequear_peso_inicial_comida_agregada();
    tiempo_inicial=millis();
    tiempo_inicial_mandar_datos=millis();

    break;
  case 3:
    operacion_general = abrir_servo_1_vez();
    break;

  case 4:
    operacion_general = cerrar_servo_1_vez();
    break;

  case 5:
    operacion_general = abrir_servo_2_veces();
    break;

  case 6:
    operacion_general = cerrar_servo_2_veces();
    break;

  case 7:
    operacion_general = abrir_servo_3_veces();
    break;

  case 8:
    operacion_general = cerrar_servo_3_veces();
    break;

  case 9:
    operacion_general = medir_peso_inicial();
    break;

  case 10:
    operacion_general = chequear_servo_primer_chequeo();

  case 11:
    operacion_general = chequear_servo_segundo_chequeo();

  case 12:
    operacion_general = detectar_presencia_y_alertar();
    break;
    
  case 13:
    operacion_general = prender_buzzer();
    break;

  case 14:
    operacion_general = apagar_buzzer();
    break;

  case 15:
    operacion_general = chequear_restos_comida();
    break;

  case 16:
    operacion_general = medir_cantidad_en_deposito_y_alertar();
    break;

  case 17:
    operacion_general = prender_led();
    break;

  case 18:
    operacion_general = apagar_led();
    break;
    
  case 19:
    operacion_general = detectar_perro_husmeando();
    break;
    
  case 20:
    operacion_general = mandar_datos();
    break;
    
  default:
    operacion_general = 0;
    break;//chequear_peso_inicial_comida_agregada
    //chequear_vector_ultimas_comidas
    //chequear_servo_primer_chequeo
    //chequear_servo_segundo_chequeo
    //chequear_restos_comida
    //mandar_datos()
  }
}
int mandar_datos()//peso de la balanza,
{//bandera mandar datos

    char cadena_convertida[30]=" ";
    cadena_a_enviar=" ";
 // if(servo_trabado_mandar_datos!=0)
  
    itoa(servo_trabado_mandar_datos,cadena_convertida,10);
    cadena_a_enviar=cadena_a_enviar+cadena_convertida+"|";
  
  //if(perro_comio_fuera_de_timepo_mandar_datos!=0)
  
    itoa(perro_comio_fuera_de_timepo_mandar_datos,cadena_convertida,10);
    cadena_a_enviar=cadena_a_enviar+cadena_convertida+"|";
  
  //if(perro_comio_rapido_mandar_datos!=0)
  
    itoa(perro_comio_rapido_mandar_datos,cadena_convertida,10);
    cadena_a_enviar=cadena_a_enviar+cadena_convertida+"|";
  
  //if(lectura_restos_comida_mandar_datos!=0)
    if(lectura_restos_comida_mandar_datos<0)lectura_restos_comida_mandar_datos=0;
    itoa(lectura_restos_comida_mandar_datos/435,cadena_convertida,10);
    cadena_a_enviar=cadena_a_enviar+cadena_convertida+"|";
  
  //if(distancia_ultrasonido_mandar_datos!=0)
  
    int calculo=10*(100-((distancia_ultrasonido_mandar_datos-5)*5));
    itoa(calculo,cadena_convertida,10);
    cadena_a_enviar=cadena_a_enviar+cadena_convertida+'#';
  Serial.print(cadena_a_enviar);
  mySerial.print(cadena_a_enviar);
  distancia_ultrasonido_mandar_datos=0;
  lectura_restos_comida_mandar_datos=0;  
  perro_comio_rapido_mandar_datos=0;
  servo_trabado_mandar_datos=0;
  perro_comio_fuera_de_timepo_mandar_datos=0;
  return 0;
 
}

int detectar_perro_husmeando()
{

  int value = digitalRead(PIRPin);
  Serial.println("lectura del PIR:");
  Serial.println(value);
  if (value == HIGH)
  {
    int chequeo_balanza_perro_husmeando = balanza.get_value(10);
    Serial.println("lectura balanza lectura_restos_comida: ");
    Serial.println(lectura_restos_comida);
    Serial.println("lectura balanza chequeo_balanza_perro_husmeando: ");
    Serial.println(balanza.get_value(10), 0);                                                                                                                              //no esta comiendo nada
    if (chequeo_balanza_perro_husmeando < lectura_restos_comida - 4000 /*&& lecturaComiendo != 0 && banderaLecturaInicial == 1 && banderaYaSonoBuzzer == 0*/) //esta comiendo muy rapido
    {
      perro_comio_fuera_de_timepo_mandar_datos=1;
      //variable para mandar datos                                                                                                                                  
    }
  }
  tiempo_final_mandar_datos = millis() - tiempo_inicial_mandar_datos;
  if (tiempo_final_mandar_datos > 2000/*600000*/)//10 min
  {
    tiempo_inicial_mandar_datos = millis();
    return 20;
  }
  return 0;
}
int chequear_restos_comida()
{

    Serial.print("Valor de lectura de restos:  ");
    lectura_restos_comida = balanza.get_value(10);
    Serial.println(lectura_restos_comida);
    lectura_restos_comida_mandar_datos=lectura_restos_comida;
    return 16;
}
int chequear_vector_ultimas_comidas()
{
  Serial.println("chequear vector");
  if(ultima_posicion_insercion_vector>3)//aca ya hubo 3 comidas del perro
  {
     Serial.println("PRIMER IF");
 
    if(vector_comio_rapido[ultima_posicion_insercion_vector-1]==1&&vector_comio_rapido[ultima_posicion_insercion_vector-2]==1&&vector_comio_rapido[ultima_posicion_insercion_vector-3]==1)
    {
        Serial.println("Segundo if");

      cantidad_de_aperturas_servo=1;
    }
  }
  return 2;
}
int chequear_peso_inicial_comida_agregada()//si hay comida agregada, abro una sola vez el servo
{
    Serial.print("Valor de lectura para ver si hay comida agregada de antes:  ");
    lectura_peso_inicial_comida_agregada = balanza.get_value(10);
    Serial.println(lectura_peso_inicial_comida_agregada);
    if(lectura_peso_inicial_comida_agregada>20000)//quiere decir que alguien le agrego comida, entonces abro una sola vez el servo
    {
      cantidad_de_aperturas_servo=1;
    }
    return 3;
}
int chequear_servo_primer_chequeo()//si esta abierto, en 2 lecturas tiene que haber caido mucha comida, lo puedo saber con el US
{
  digitalWrite(Trigger, HIGH);
  int tiempo_micros_inicial;
  int tiempo_micros_final;
  delayMicroseconds(10); //Enviamos un pulso de 10us
  digitalWrite(Trigger, LOW);
  int t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
  primer_chequeo= t / 59; 
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    tiempo_inicial = millis();
    return 11;
  }
  else
    return 10;
}
int chequear_servo_segundo_chequeo()//si esta abierto, en 2 lecturas tiene que haber caido mucha comida, lo puedo saber con el US
{
  digitalWrite(Trigger, HIGH);
  int tiempo_micros_inicial;
  int tiempo_micros_final;
  delayMicroseconds(10); //Enviamos un pulso de 10us
  digitalWrite(Trigger, LOW);
  int t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
  segundo_chequeo= t / 59; 
  if(segundo_chequeo<primer_chequeo-4)
  {
    bandera_servo_trabado=1;
    return 12;
  }

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
    return 4;
  }
  else
    return 3;
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
      return 9;
    }
    else
      return 5;
  }
  else
    return 4;
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
    return 6;
  }
  else
    return 5;
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
      return 9;
    else
      return 7;
  }
  else
    return 6;
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
    return 8;
  }
  else
    return 7;
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
    return 9;
  }
  else
    return 8;
}
int leer_bluetooth()
{
  Serial.println("leer bluetooth");
  if (mySerial.available() > 0) 
  {
    Serial.println("info disponible");
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
            tiempo_inicial=millis();

      return 13;
    }
        if (cadena_recibida_bluetooth == 'e')
    {
            tiempo_inicial=millis();

      return 17;
    }  
    cadena_recibida_bluetooth='z';

  }
  else
    return 9; 
}
int medir_cantidad_en_deposito_y_alertar()
{
  Serial.println("entrando a operacion general 3");
  operacion_ultrasound = 0;
  //US
  long t; //timepo que demora en llegar el eco
  long d; //distancia en centimetros

  digitalWrite(Trigger, HIGH);
  int tiempo_micros_inicial;
  int tiempo_micros_final;
  delayMicroseconds(10); //Enviamos un pulso de 10us
  digitalWrite(Trigger, LOW);
  t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
  d = t / 59;              //escalamos el tiempo a una distancia en cm
  Serial.print("Distancia ultrasonido: ");
  Serial.print(d); //Enviamos serialmente el valor de la distancia
  Serial.print("cm");
  Serial.println();
  distancia_ultrasonido_mandar_datos=d;
  if (d > 17)
  {
    tiempo_inicial=millis();
    return 17;
    Serial.println("entrando al if dist>17");
  }
  else
  {
    return 19;
    Serial.println("entrando al else dist>17");
    digitalWrite(LEDPin, LOW);
  }
  return 19;
}

int medir_peso_inicial()
{
  if(banderaLecturaInicial==0)
  {
    Serial.print("Valor de lectura inicial:  ");
    lecturaInicial = balanza.get_value(10);
    Serial.println(lecturaInicial);
    banderaLecturaInicial = 1;
    Serial.println("delay para sacar la comida, 5 segundos...(5 segundos para que coma el perro)");
  }
    tiempo_final = millis() - tiempo_inicial;    

  if (tiempo_final > 5000) //5 segundos para sacar la comida
  {
    banderaLecturaInicial=0;
    tiempo_inicial = millis();
    return 10;
  }
  else
    return 9;
}
int detectar_presencia_y_alertar()
{
  int value = digitalRead(PIRPin);
  Serial.println("lectura del PIR:");
  Serial.println(value);
  value=HIGH;
  if (value == HIGH)
  {
    int lecturaComiendo = balanza.get_value(10);
    Serial.println("lectura balanza INICIAL: ");
    Serial.println(lecturaInicial);
    Serial.println("lectura balanza actual: ");
    Serial.println(balanza.get_value(10), 0);
    if (lecturaComiendo == lecturaInicial)
    {
    }                                                                                                                                  //no esta comiendo nada
    if (lecturaComiendo < lecturaInicial - 4000 /*&& lecturaComiendo != 0 && banderaLecturaInicial == 1 && banderaYaSonoBuzzer == 0*/) //esta comiendo muy rapido
    {
      vector_comio_rapido[ultima_posicion_insercion_vector]=1;
      ultima_posicion_insercion_vector++;
      tiempo_inicial=millis();                                                                                                                                  
      return 13;
    }
    else
      return 15;
  }
  else
  //en este punto, el perro se fue, podemos ver el peso final.
  {
    int lecturaFinal = balanza.get_value(10);
  }
  return 15;
}
int prender_led()
{
  Serial.println("prendiendo led");
  digitalWrite(LEDPin, HIGH); // poner el Pin en HIGH
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 5000)
  {
  Serial.println("entrando en el tiempo del led");
    
    tiempo_inicial = millis();
    return 18;
  }else return 17;
}
int apagar_led()
{
    Serial.println("entrando en apagar led");

  digitalWrite(LEDPin, LOW); 
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 500)
  {
      Serial.println("entrando en el tiempo de apagar led");

    tiempo_inicial = millis();
    return 19;
  }else return 18;
}
int prender_buzzer()
{
  Serial.println("prendiendo buzzer");
  digitalWrite(BUZZERpin, HIGH); // poner el Pin en HIGH
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 1000)
  {
    
    perro_comio_rapido_mandar_datos=1;
    tiempo_inicial = millis();
    return 14;
  }
  else
    return 13;
}
int apagar_buzzer()
{
  digitalWrite(BUZZERpin, LOW); // poner el Pin en HIGH
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 1000)
  {
    tiempo_inicial = millis();
    return 15;
  }
  else
    return 14;
}
