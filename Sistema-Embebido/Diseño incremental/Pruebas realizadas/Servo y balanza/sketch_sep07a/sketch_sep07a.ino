// Incluímos la librería para poder controlar el servo
#include <Servo.h>

#include <HX711.h>

#define DOUT  A1
#define CLK  A0

HX711 balanza(DOUT, CLK); 
// Declaramos la variable para controlar el servo
Servo servoMotor;

int bandera=0;


 
void setup() {
  Serial.begin(9600);
  Serial.print("Lectura del valor del ADC:  ");
  Serial.println(balanza.read());
  Serial.println("No ponga ningun  objeto sobre la balanza");
  Serial.println("Destarando...");
  balanza.set_scale(); //La escala por defecto es 1
  balanza.tare(20);  //El peso actual es considerado Tara.
  Serial.println("Coloque un peso conocido:");
  
  // Iniciamos el monitor serie para mostrar el resultado
 
  // Iniciamos el servo para que empiece a trabajar con el pin 9
  servoMotor.attach(3);
}
 
void loop() {
  if(bandera==0)
  {
  // Desplazamos a la posición 0º
  servoMotor.write(0);
  // Esperamos 1 segundo
  delay(1000);
  
  // Desplazamos a la posición 90º
  servoMotor.write(180);
  // Esperamos 1 segundo
  delay(1000);
  servoMotor.write(0);
  // Esperamos 1 segundo
  bandera=1;
  }

  Serial.print("Valor de lectura:  ");
  Serial.println(balanza.get_value(10),0);
  delay(1000);
  // Desplazamos a la posición 180º
  // servoMotor.write(180);
  // Esperamos 1 segundo
  // delay(1000);
}
