#include <IRremote.h>
int IROUTPIN = 8;

void setup() {
  // put your setup code here, to run once:
  IrSender.begin(IROUTPIN);

}

void loop() {
  // put your main code here, to run repeatedly:
  IrSender.sendSony(0x44,0x13,2,15);
  delay(5000);
  IrSender.sendSony(0x44,0x12,2,15);
 
  IR

}
