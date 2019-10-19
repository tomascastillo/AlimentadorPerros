// Incluímos la librería para poder controlar el servo
#include <Servo.h>
#include <HX711.h>
#include <Wire.h> 

#define DOUT  A1
#define CLK  A0

HX711 balanza(DOUT, CLK); 
// Declaramos la variable para controlar el servo
Servo servoMotor;

const int LEDPin= 13;
const int PIRPin= 7;
int bandera=0;
const int BUZZERpin = 8;
const int Trigger = 4;   //Pin digital 2 para el Trigger del sensor
const int Echo = 2;   //Pin digital 3 para el Echo del sensor
int banderaLecturaInicial=0;
    int lecturaInicial=0;
/*
1. servo
2. balanza
3. pir
4. buzzer
5. todo el tiempo US+LED
*/

void setup() {
  Serial.begin(9600);
  Serial.print("Lectura del valor del ADC:  ");
  Serial.println(balanza.read());
  Serial.println("No ponga ningun objeto sobre la balanza");
  Serial.println("Destarando...");
  balanza.set_scale(); //La escala por defecto es 1
  balanza.tare(20);  //El peso actual es considerado Tara.
  Serial.println("Coloque un peso conocido:");  
  // Iniciamos el servo para que empiece a trabajar con el pin 9
  servoMotor.attach(3);
  pinMode(LEDPin, OUTPUT);
  pinMode(PIRPin, INPUT);
  //pinMode(BUZZERpin, OUTPUT);  //definir pin como salida
  pinMode(Trigger, OUTPUT); //pin como salida
  pinMode(Echo, INPUT);  //pin como entrada
  digitalWrite(Trigger, LOW);//Inicializamos el pin con 0
 }
 
void loop() {
  int banderaYaSonoBuzzer=0;

  
  if(bandera==0)
  {
      
  // Desplazamos a la posición 0º
  servoMotor.write(0);
  // Esperamos 1 segundo
  delay(1000);
  
  // Desplazamos a la posición 90º
  servoMotor.write(135);
  // Esperamos 1 segundo
  delay(1000);
  servoMotor.write(0);
  // Esperamos 1 segundo
  bandera=1;
  banderaLecturaInicial=1;
  Serial.print("Valor de lectura inicial:  ");
  lecturaInicial=balanza.get_value(10);
  Serial.println(balanza.get_value(10),0);
  delay(1000);
  }
  //verificar que el perro siga comiendo
  //si come muy rapido, activar el buzzer

  //***********************************FALTA:*****************************************************************
  //esta lectura, hacerla cada 1 segundo.
  Serial.println("delay para sacar la comida...");
  delay(10000);
  int value= digitalRead(PIRPin);
   Serial.println("lectura del PIR:");
    Serial.println(value);
  if (value == HIGH)
  {
    //leo de la balanza
    int lecturaComiendo=balanza.get_value(10);
    Serial.println("lectura balanza INICIAL: ");
    Serial.println(lecturaInicial);
     Serial.println("lectura balanza actual: ");
    Serial.println(balanza.get_value(10),0);
    if(lecturaComiendo==lecturaInicial){}//no esta comiendo nada
    if(lecturaComiendo<lecturaInicial-4000&&lecturaComiendo!=0&&banderaLecturaInicial==1&&banderaYaSonoBuzzer==0)//esta comiendo muy rapido
    {
      //activar buzzer
      digitalWrite(BUZZERpin, HIGH);   // poner el Pin en HIGH
      delay(1000);// esperar 5 segundos
      digitalWrite(BUZZERpin, LOW);    // poner el Pin en LOW
      delay(200);// esperar 20 segundos    
      //tone(BUZZERpin,30);
      banderaYaSonoBuzzer=1;
      delay(1000);
    }
  }
  else
  //en este punto, el perro se fue, podemos ver el peso final.
  {
    int lecturaFinal=balanza.get_value(10);
  }

  //US
  long t; //timepo que demora en llegar el eco
  long d; //distancia en centimetros

  digitalWrite(Trigger, HIGH);
  delayMicroseconds(10);          //Enviamos un pulso de 10us
  digitalWrite(Trigger, LOW);
  
  t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
  d = t/59;             //escalamos el tiempo a una distancia en cm
  
  Serial.print("Distancia ultrasonido: ");
  Serial.print(d);      //Enviamos serialmente el valor de la distancia
  Serial.print("cm");
  Serial.println();
  delay(100);          //Hacemos una pausa de 100ms
  //dist>17 -> prendo LED
  if(d>17){
    digitalWrite(LEDPin, HIGH);
    delay(50);
    digitalWrite(LEDPin, LOW);
    delay(50);
  }else{
    digitalWrite(LEDPin, LOW);
  }

}
