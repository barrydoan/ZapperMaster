#include <IRremote.h>
int IRPIN = 7;
void setup()
{
  Serial.begin(9600);
  Serial.println("Enabling IRin");
  IrReceiver.begin(IRPIN, ENABLE_LED_FEEDBACK);
  Serial.println("Enabled IRin");
}

void loop()
{
  char c;
  if (IrReceiver.decode())
  {
    Serial.println(IrReceiver.decodedIRData.decodedRawData, HEX);
    IrReceiver.resume();
  }

  if (Serial.available()) {
	while(Serial.available()) {
		c = Serial.read();
		Serial.print(c);
	}
  }
  delay(500);
}