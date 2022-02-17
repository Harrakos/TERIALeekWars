import os
import sys
import json

##print('Number of arguments:', len(sys.argv), 'arguments.')
##print('Argument List:', str(sys.argv))

if(len(sys.argv) != 2):
    print("Utilisation : RL.py nombreDeCombat ") 
    sys.exit(1); 

nbFight = int(sys.argv[1])

while nbFight > 0 : 
    os.system('java -jar generator.jar test/scenario/scenarioTrain.json > resultat.json')

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

    #print(fight)
    print(winner)

    nbFight = nbFight - 1



# LEEKSCRIPT
#file = open('IA.leekscript')
##content = file.readlines()
##print(content[0:2])