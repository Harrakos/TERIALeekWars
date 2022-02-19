import json
import collections

with open("resultat2.json") as jsonFile:
    jsonObject = json.load(jsonFile)
    jsonFile.close()

logs = jsonObject['logs']['0']

sortedLogs = collections.OrderedDict(sorted(logs.items()))

for log in sortedLogs: 
    rl = str(sortedLogs[log][0][2])
    print(rl)

