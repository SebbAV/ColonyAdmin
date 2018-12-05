import sys
import json
import time
import RPi.GPIO as GPIO
GPIO.setwarnings(False)
sos = sys.argv
sos.remove(sos[0])
sos = json.loads(sos[0])
print(sos)
GPIO.setmode(GPIO.BOARD)
GPIO.setup(3, GPIO.OUT)
GPIO.output(3,sos)