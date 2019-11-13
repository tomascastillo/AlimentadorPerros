const int LEDPin= 12;
const int PIRPin= 2;
 
void setup()
{
  pinMode(LEDPin, OUTPUT);
  pinMode(PIRPin, INPUT);
}
 
void loop()
{
  int value= digitalRead(PIRPin);
 

    digitalWrite(LEDPin, HIGH);
    delay(500);
    digitalWrite(LEDPin, LOW);
    delay(500);
  

}
