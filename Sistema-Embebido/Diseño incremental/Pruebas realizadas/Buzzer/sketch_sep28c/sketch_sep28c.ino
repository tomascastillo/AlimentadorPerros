const int pin = 8;
 
void setup() {
  pinMode(pin, OUTPUT);  //definir pin como salida
}
 
void loop(){
  //digitalWrite(pin, LOW);   // poner el Pin en HIGH
  //tone(pin,30);
  //delay(1000);  
  //tone(pin,0);
    digitalWrite(pin, HIGH);
  //noTone(pin);
   delay(1000);  
digitalWrite(pin, LOW);
  // esperar 5 segundos
      // poner el Pin en LOW
  
  delay(1000);               // esperar 20 segundos
}
