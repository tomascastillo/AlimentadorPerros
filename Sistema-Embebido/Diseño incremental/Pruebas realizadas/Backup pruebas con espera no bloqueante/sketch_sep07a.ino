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
const int LEDPin = 12;
const int PIRPin = 7;
int bandera = 0;
const int BUZZERpin = 8;
const int Trigger = 4; //Pin digital 2 para el Trigger del sensor
const int Echo = 2;    //Pin digital 3 para el Echo del sensor
int banderaLecturaInicial = 0;
int lecturaInicial = 0;
int operacion = 0;
int operacion_buzzer = 0;
int operacion_ultrasound = 0;
int operacion_general = 0;
int banderaYaSonoBuzzer = 0;
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
    pinMode(BUZZERpin, OUTPUT); //definir pin como salida
    pinMode(Trigger, OUTPUT);   //pin como salida
    pinMode(Echo, INPUT);       //pin como entrada
    digitalWrite(Trigger, LOW); //Inicializamos el pin con 0
    tiempo_inicial = millis();
    digitalWrite(BUZZERpin, LOW);
}

void loop()
{
    switch (operacion_general)
    {
    case 0:
        abrir_servo_y_medir_peso_incial();
        break;

    case 1:
        medir_pir_y_alertar_perro();
        break;

    case 2:
        medir_cantidad_en_deposito_y_alertar();
        break;

    default:
        break;
    }
}
void medir_cantidad_en_deposito_y_alertar()
{

    //US
    long t; //timepo que demora en llegar el eco
    long d; //distancia en centimetros

    digitalWrite(Trigger, HIGH);
    int tiempo_micros_inicial;
    int tiempo_micros_final;
    tiempo_micros_inicial = micros();
    tiempo_micros_final = micros() - tiempo_micros_inicial;
    if (tiempo_micros_final > 10)
    {
        tiempo_micros_inicial = micros();
        digitalWrite(Trigger, LOW);
        t = pulseIn(Echo, HIGH); //obtenemos el ancho del pulso
        d = t / 59;              //escalamos el tiempo a una distancia en cm

        Serial.print("Distancia ultrasonido: ");
        Serial.print(d); //Enviamos serialmente el valor de la distancia
        Serial.print("cm");
        Serial.println();
    }
    //delayMicroseconds(10); //Enviamos un pulso de 10us

    if (d > 17)
    {
        switch (operacion_ultrasound)
        {
        case 0:
            prender_led();
            break;

        case 1:
            apagar_led();
            break;

        default:
            break;
        }
    }
    else
    {
        digitalWrite(LEDPin, LOW);
    }

    // delay(100); //Hacemos una pausa de 100ms
    //dist>17 -> prendo LED

    operacion_general = 1;
}
void abrir_servo_y_medir_peso_incial()
{
    if (bandera == 0)
    {
        switch (operacion)
        {
        case 0:
            cerrar_servo();

            break;

        case 1:
            abrir_servo();

            break;

        case 2:
            cerrar_servo2();
            bandera = 1;
            break;

        case 3:
            medir_peso_inicial();
            break;

        default:
            break;
        }
    }
    else
    {
    }
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 5000) //5 segundos para sacar la comida
    {
        tiempo_inicial = millis();
        operacion_general = 1;
    }
}
void medir_peso_inicial()
{
    bandera = 1;
    Serial.print("Valor de lectura inicial:  ");
    lecturaInicial = balanza.get_value(10);
    Serial.println(lecturaInicial);
    banderaLecturaInicial = 1;
    Serial.println("delay para sacar la comida, 5 segundos...");
}
void medir_pir_y_alertar_perro()
{
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
        }                                                                                                                                  //no esta comiendo nada
        if (lecturaComiendo < lecturaInicial - 4000 /*&& lecturaComiendo != 0 && banderaLecturaInicial == 1 && banderaYaSonoBuzzer == 0*/) //esta comiendo muy rapido
        {
            //activar buzzer
            switch (operacion_buzzer)
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
    operacion_general = 2;
}
void prender_led()
{
    digitalWrite(LEDPin, HIGH); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 50)
    {
        tiempo_inicial = millis();
        operacion_ultrasound = 1;
    }
}
void apagar_led()
{
    digitalWrite(LEDPin, LOW); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 50)
    {
        tiempo_inicial = millis();
    }
}
void prender_buzzer()
{
    digitalWrite(BUZZERpin, HIGH); // poner el Pin en HIGH
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        tiempo_inicial = millis();
        operacion_buzzer = 1;
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
        operacion = 2;
    }
}
void cerrar_servo()
{
    servoMotor.write(0);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 1000)
    {
        tiempo_inicial = millis();
        operacion = 1;
    }
}
void cerrar_servo2()
{
    servoMotor.write(0);
    tiempo_final = millis() - tiempo_inicial;
    if (tiempo_final > 3000)
    {
        tiempo_inicial = millis();
        operacion = 3;
    }
}