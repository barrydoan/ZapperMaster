# ZapperMaster
![screenshot](https://github.com/barrydoan/ZapperMaster/blob/master/remote%20on%20phone.png?raw=true)
![screenshot](https://github.com/barrydoan/ZapperMaster/blob/master/hardware.jpg?raw=true)
## Setup the arduino
Install the arduino IDE
Install the IR library inside the folder hardware/libs:
Sketch > Include Library > Add .Zip Library
## Setup hardware
The hardware:
- Arduino
- IrTransmitter
- IrReceiver
The IrReceiver data pin connects to pin 7 of Arduino.
The IrTransmitter data pin connects to pin 8 of Arduino.

## Decode the IR signal
The code to decode the IR signal is inside forder ZapperMaster\hardware\arduino\IRCommunication

## Android
The Android application is located in ZapperMaster\phone\ZapperMaster
Step to run the api
Go to folder ZapperMaster\webapi\zappermaster
- Create python environment: python -m venv env
- Active python environment: .\env\Scripts\Activate.ps1
- Install dependencies: pip install -r requirements.txt
- Go to folder ZapperMaster\webapi\zappermaster\zappermasstersite
- Run the server: python manage.py runserver 0.0.0.0:8000
- admin login admin/password123

## Web api
The web api is located in ZapperMaster\webapi\zappermaster

## Resources
###### decode IR code
https://www.circuitbasics.com/arduino-ir-remote-receiver-tutorial/
https://www.instructables.com/IR-REMOTE-DECODER-USING-ARDUINO/
###### android and arduino communication
https://www.allaboutcircuits.com/projects/communicate-with-your-arduino-through-android/
