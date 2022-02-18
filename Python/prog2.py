import json
import collections

with open("resultat.json") as jsonFile:
    jsonObject = json.load(jsonFile)
    jsonFile.close()

fight = jsonObject['fight']['actions'][1]
winner = jsonObject['winner']
logs = jsonObject['logs']['0']#['1']

sortedLogs = collections.OrderedDict(sorted(logs.items()))

for log in sortedLogs: 
    print(log)

