const int LEDPin= 13;
const int PIRPin= 7;
 
void setup()
{ Serial.begin(9600);
  pinMode(LEDPin, OUTPUT);
  pinMode(PIRPin, INPUT);
}
 
void loop()
{
  int value= digitalRead(PIRPin);
 Serial.println(value);
 
}
