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
SoftwareSerial mySerial(10, 9);
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
char cadena_recibida_bluetooth = '9';
int cantidad_de_aperturas_servo = 0;
int bandera_servo = 0;
int bandera_bluetooth = 0;
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
  servoMotor.write(3);
  pinMode(LEDPin, OUTPUT);
  pinMode(PIRPin, INPUT);
  pinMode(BUZZERpin, OUTPUT); //definir pin como salida
  pinMode(Trigger, OUTPUT);   //pin como salida
  pinMode(Echo, INPUT);       //pin como entrada
  digitalWrite(Trigger, LOW); //Inicializamos el pin con 0
  tiempo_inicial = millis();
  digitalWrite(BUZZERpin, LOW);
}

void loop()
{
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
    break;

  case 1:
    operacion_general = abrir_servo_1_vez();
    break;

  case 2:
    operacion_general = cerrar_servo_1_vez();
    break;

  case 3:
    operacion_general = abrir_servo_2_veces();
    break;

  case 4:
    operacion_general = cerrar_servo_2_veces();
    break;

  case 5:
    operacion_general = abrir_servo_3_veces();
    break;

  case 6:
    operacion_general = cerrar_servo_3_veces();
    break;

  case 7:
    operacion_general = medir_peso_inicial();
    break;

  case 8:
    operacion_general = detectar_presencia_y_alertar();
    break;

  case 9:
    operacion_general = prender_buzzer();
    break;

  case 10:
    operacion_general = apagar_buzzer();
    break;

  case 11:
    operacion_general = medir_cantidad_en_deposito_y_alertar();
    break;

  case 12:
    operacion_general = prender_led();
    break;

  case 13:
    operacion_general = apagar_led();
    break;

  default:
    operacion_general = 0;
    break;
  }
}
int abrir_servo_1_vez()
{
  servoMotor.write(135);
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    Serial.println("abrir servo 1");
    tiempo_inicial = millis();
    return 2;
  }
  else
    return 1;
}
int cerrar_servo_1_vez()
{
  servoMotor.write(0);
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    Serial.println("cerrar servo 1");
    tiempo_inicial = millis();
    if (cantidad_de_aperturas_servo == 1)
    {
      return 7;
    }
    else
      return 3;
  }
  else
    return 2;
}
int abrir_servo_2_veces()
{
  servoMotor.write(135);
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    Serial.println("abrir servo 2");
    tiempo_inicial = millis();
    return 4;
  }
  else
    return 3;
}
int cerrar_servo_2_veces()
{
  servoMotor.write(0);
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    Serial.println("cerrar servo2");
    tiempo_inicial = millis();
    if (cantidad_de_aperturas_servo == 2)
      return 7;
    else
      return 5;
  }
  else
    return 4;
}
int abrir_servo_3_veces()
{
  servoMotor.write(135);
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    Serial.println("abrir servo3");
    tiempo_inicial = millis();
    return 6;
  }
  else
    return 5;
}
int cerrar_servo_3_veces()
{
  servoMotor.write(0);
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 2000)
  {
    Serial.println("cerrar servo3");
    tiempo_inicial = millis();
    return 7;
  }
  else
    return 6;
}
int leer_bluetooth()
{
  Serial.println("leer bluetooth");
  if (mySerial.available() > 0) 
  {
    Serial.println("info disponible");
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
    else
      return 0;
  }
  else
    return 0; 
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
  
  if (d > 17)
  {
    return 12;
    Serial.println("entrando al if dist>17");
  }
  else
  {
    return 0;
    Serial.println("entrando al else dist>17");
    digitalWrite(LEDPin, LOW);
  }
  return 0;
}

int medir_peso_inicial()
{
  Serial.print("Valor de lectura inicial:  ");
  lecturaInicial = balanza.get_value(10);
  Serial.println(lecturaInicial);
  banderaLecturaInicial = 1;
  Serial.println("delay para sacar la comida, 5 segundos...(5 segundos para que coma el perro)");
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 5000) //5 segundos para sacar la comida
  {
    tiempo_inicial = millis();
    return 8;
  }
  else
    return 7;
}
int detectar_presencia_y_alertar()
{
  int value = digitalRead(PIRPin);
  Serial.println("lectura del PIR:");
  Serial.println(value);
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
      return 9;
    }
    else
      return 11;
  }
  else
  //en este punto, el perro se fue, podemos ver el peso final.
  {
    int lecturaFinal = balanza.get_value(10);
  }
  return 11;
}
int prender_led()
{
  digitalWrite(LEDPin, HIGH); // poner el Pin en HIGH
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 50)
  {
    tiempo_inicial = millis();
    return 13;
  }
}
int apagar_led()
{
  digitalWrite(LEDPin, LOW); 
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 50)
  {
    tiempo_inicial = millis();
    return 0;
  }
}
int prender_buzzer()
{
  digitalWrite(BUZZERpin, HIGH); // poner el Pin en HIGH
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 1000)
  {
    tiempo_inicial = millis();
    return 10;
  }
  else
    return 9;
}
int apagar_buzzer()
{
  digitalWrite(BUZZERpin, LOW); // poner el Pin en HIGH
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 1000)
  {
    tiempo_inicial = millis();
    return 11;
  }
  else
    return 9;
}
