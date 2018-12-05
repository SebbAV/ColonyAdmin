import sys
import json
import time
import RPi.GPIO as GPIO
GPIO.setwarnings(False)
outs = [5,7]
GPIO.setmode(GPIO.BOARD)
GPIO.setup(outs,GPIO.OUT)
entrance = sys.argv
entrance.remove(entrance[0])
entrance = json.loads(entrance[0])
print(entrance)
if entrance:
    GPIO.output(7,1)
    time.sleep(10)
    GPIO.output(7,0)
    print("ok")
else:
    GPIO.output(5,1)
    time.sleep(10)
    GPIO.output(5,0)
    print("not allowed")
