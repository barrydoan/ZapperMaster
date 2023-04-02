#include <IRremote.h>
int IR_IN_PIN = 7;
int IR_OUT_PIN = 8;
void setup()
{
  Serial.begin(9600);
  Serial.println("Enabling IRin");
  IrReceiver.begin(IR_IN_PIN, ENABLE_LED_FEEDBACK);
  IrSender.begin(IR_OUT_PIN);
  Serial.println("Enabled IRin");
}

void loop()
{
  char c;
  if (IrReceiver.decode())
  {
    Serial.print('[');
    Serial.print(IrReceiver.decodedIRData.protocol);
    Serial.print('|');
    Serial.print(IrReceiver.decodedIRData.address,HEX);
    Serial.print('|');
    Serial.print(IrReceiver.decodedIRData.command,HEX);
    Serial.print('|');
    Serial.print(IrReceiver.decodedIRData.numberOfBits);
    Serial.println(']');
    IrReceiver.resume();
  }

  if (Serial.available()) {
    char command[50];
    int length = 0;
	  while(Serial.available()) {
		  c = Serial.read();
      if (c == '<') {
        length = 0;
      }
      else if (c == '>') {
        Serial.println("Send command");
        sendCommand(command, length);
      }
      else if (c != '<' && c != '>') {
        command[length] = c;
        length++;
      }
	  }
  }

  
  
  //IrSender.sendSamsung(0x707, 0x3,3);


  
  delay(500);
}

void sendCommand(char command[], int length) {
  int index[3];
  int count = 0;
  for (int i = 0; i < length; i++) {
    if (command[i] == '|') {
      index[count] = i;
      count++;
    }
  }
  if (count == 3) {
    Serial.println(getValue(command, 0, index[0]));
    Serial.println(getValue(command, index[0] + 1, index[1]));
    Serial.println(getValue(command, index[1] + 1, index[2]));
    Serial.println(getValue(command, index[2] + 1, length));
    IRData data = {};
    data.protocol = getValue(command, 0, index[0]).toInt();
    data.address = x2i(getValue(command, index[0] + 1, index[1]));
    data.command = x2i(getValue(command, index[1] + 1, index[2]));
    data.numberOfBits = getValue(command, index[2] + 1, length).toInt();

  IrSender.write(&data, 3);
  IrReceiver.resume();

  }
}

String getValue(char command[], int startIndex, int endIndex) {
  String result = String();
  for (int i = startIndex; i < endIndex; i++) {
    result.concat(command[i]);
  }
  return result;
}

int x2i(String s) 
{
  int x = 0;
  for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);
    if (c >= '0' && c <= '9') {
      x *= 16;
      x += c - '0'; 
    }
    else if (c >= 'A' && c <= 'F') {
      x *= 16;
      x += (c - 'A') + 10; 
    }
    else break;
  }
  return x;
}