import json

with open("resultat.json", "r") as f:
    lines = f.readlines()
with open("resultat.json", "w") as f:
    for line in lines:
        if line.strip("\n") != "db_resolver false folder=0 farmer=0":
            f.write(line)


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

