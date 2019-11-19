const int LEDPin= 13;
const int PIRPin= 7;
 
void setup()
{ Serial.begin(9600);

}
 
void loop()
{
  int value= digitalRead(PIRPin);
  
  Serial.println(value);
  delay(1000);
}
