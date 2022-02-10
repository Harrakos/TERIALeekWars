import json

# JSON
with open("resultat.json") as jsonFile:
    jsonObject = json.load(jsonFile)
    jsonFile.close()

fight = jsonObject['fight']['actions'][1]
winner = jsonObject['winner']

print(fight)
print(winner)

# LEEKSCRIPT
file = open('IA.leekscript')
  
content = file.readlines()
  
print(content[0:2])

