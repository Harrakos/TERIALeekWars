import json

with open("scenarioTrain.json") as jsonFile:
    jsonObject = json.load(jsonFile)
    jsonFile.close()

print(jsonObject["random_seed"])

jsonObject["random_seed"] = 12

print(jsonObject["random_seed"])

with open('scenarioTrain.json', 'w') as f:
    json.dump(jsonObject, f)


