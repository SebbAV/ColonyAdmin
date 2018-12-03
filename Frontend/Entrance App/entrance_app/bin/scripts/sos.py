import sys
import json
import time
sos = sys.argv
sos.remove(sos[0])
sos = json.loads(sos[0])
print(sos)
