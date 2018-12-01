import sys
import json
import time
entrance = sys.argv
entrance.remove(entrance[0])
entrance = json.loads(entrance[0])
if entrance:
    print(1)
else:
    print(0)
