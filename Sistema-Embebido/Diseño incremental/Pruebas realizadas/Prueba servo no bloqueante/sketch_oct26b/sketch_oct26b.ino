// Incluímos la librería para poder controlar el servo
#include <Servo.h>
#include <HX711.h>
#include <Wire.h>

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

int tiempo_inicial = 0;
int tiempo_final = 0;
int tiempo_inicial2 = 0;
int tiempo_final2 = 0;
const int LEDPin = 13;
const int PIRPin = 7;
int bandera = 0;
const int BUZZERpin = 8;
const int Trigger = 4; //Pin digital 2 para el Trigger del sensor
const int Echo = 2;    //Pin digital 3 para el Echo del sensor
int banderaLecturaInicial = 0;
int lecturaInicial = 0;
int operacion = 0;
int operacion_buzzer=0;
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
  Serial.print("Lectura del valor del ADC:  ");
  Serial.println(balanza.read());
  Serial.println("No ponga ningun objeto sobre la balanza");
  Serial.println("Destarando...");
  balanza.set_scale(); //La escala por defecto es 1
  balanza.tare(20);    //El peso actual es considerado Tara.
  Serial.println("Coloque un peso conocido:");
  // Iniciamos el servo para que empiece a trabajar con el pin 9
  servoMotor.attach(3);
  pinMode(LEDPin, OUTPUT);
  pinMode(PIRPin, INPUT);
  pinMode(BUZZERpin, OUTPUT);  //definir pin como salida
  pinMode(Trigger, OUTPUT);   //pin como salida
  pinMode(Echo, INPUT);       //pin como entrada
  digitalWrite(Trigger, LOW); //Inicializamos el pin con 0
  tiempo_inicial = millis();
  digitalWrite(BUZZERpin, LOW); 
}

void loop()
{
  int banderaYaSonoBuzzer = 0;

  if (bandera == 0)
  {
    switch(operacion)
    {
      case 0:
      cerrar_servo();
    
      break;
      
      case 1:
      abrir_servo();
      
      break;

      case 2:
      cerrar_servo2();
      bandera=1;
       Serial.print("Valor de lectura inicial:  ");
       lecturaInicial = balanza.get_value(10);
       Serial.println(lecturaInicial);
       banderaLecturaInicial = 1;
      Serial.println("delay para sacar la comida, 5 segundos...");
      break;

      default:
      break;
    }
    
    }
    else
    {
    }

  
  tiempo_final = millis() - tiempo_inicial;
  if (tiempo_final > 5000)
  {
    tiempo_inicial = millis();
    int value = digitalRead(PIRPin);
    Serial.println("lectura del PIR:");
    Serial.println(value);
    if (value == HIGH)
    {
      //leo de la balanza
      int lecturaComiendo = balanza.get_value(10);
      Serial.println("lectura balanza INICIAL: ");
      Serial.println(lecturaInicial);
      Serial.println("lectura balanza actual: ");
      Serial.println(balanza.get_value(10), 0);
      if (lecturaComiendo == lecturaInicial)
      {
      }                                                                                                                              //no esta comiendo nada
      if (lecturaComiendo < lecturaInicial - 4000 /*&& lecturaComiendo != 0 && banderaLecturaInicial == 1 && banderaYaSonoBuzzer == 0*/) //esta comiendo muy rapido
      {
        //activar buzzer
        switch(operacion_buzzer)
        {
          case 0:
          Serial.print("case 0");
          prender_buzzer();
          break;

          case 1:
          Serial.print("case 1");
          apagar_buzzer();
          banderaYaSonoBuzzer = 1;
          break;

          default:
          break;
        }        
      }
    }
    else
    //en este punto, el perro se fue, podemos ver el peso final.
    {
      int lecturaFinal = balanza.get_value(10);
    }
  }
  
   
}
void prender_buzzer()
{
    digitalWrite(BUZZERpin, HIGH); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
    tiempo_inicial = millis();
    operacion_buzzer=1;
    }
}
void apagar_buzzer()
{
    digitalWrite(BUZZERpin, LOW); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
    tiempo_inicial = millis();
    }    
}
  void abrir_servo()
{
   servoMotor.write(135);
   tiempo_final = millis() - tiempo_inicial;
   if (tiempo_final > 1000)
   {
     tiempo_inicial = millis();
     operacion=2;
   }
}
void cerrar_servo()
{
   servoMotor.write(0);
   tiempo_final = millis() - tiempo_inicial;
   if (tiempo_final > 1000)
   {
     tiempo_inicial = millis();
     operacion=1;
   }

}
void cerrar_servo2()
{
   servoMotor.write(0);
   tiempo_final = millis() - tiempo_inicial;
   if (tiempo_final > 1000)
   {
     tiempo_inicial = millis();

   }
}
