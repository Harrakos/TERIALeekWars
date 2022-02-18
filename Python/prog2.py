import json
import collections

with open("resultat2.json") as jsonFile:
    jsonObject = json.load(jsonFile)
    jsonFile.close()

fight = jsonObject['fight']['actions'][1]
winner = jsonObject['winner']
logs = jsonObject['logs']['0']#['1']
logTest = jsonObject['logs']['0']["11"]

#print(logTest)

sortedLogs = collections.OrderedDict(sorted(logs.items()))

for log in sortedLogs: 
    rl = str(sortedLogs[log][0][2])
    print(rl)

