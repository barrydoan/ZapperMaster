#include <IRremote.h>
int IRPIN = 7;
int IROUTPIN = 8;
void setup()
{
  Serial.begin(9600);
  Serial.println("Enabling IRin");
  IrReceiver.begin(IRPIN, ENABLE_LED_FEEDBACK);
  IrSender.begin(IROUTPIN);
  Serial.println("Enabled IRin");
}

void loop()
{
  char c;
  if (IrReceiver.decode())
  {
    Serial.print(IrReceiver.decodedIRData.protocol);
    Serial.print('|');
    Serial.print(IrReceiver.decodedIRData.address,HEX);
    Serial.print('|');
    Serial.print(IrReceiver.decodedIRData.command,HEX);
    Serial.print('|');
    Serial.println(IrReceiver.decodedIRData.numberOfBits);
    IrReceiver.resume();
  }
  IRData data = {};
  data.protocol = 23;
  data.address = 0x44;
  data.command = 0x12;
  data.numberOfBits = 15;

  IrSender.write(&data, 3);
  IrReceiver.resume();
  
  //IrSender.sendSamsung(0x707, 0x3,3);


  
  delay(500);
}