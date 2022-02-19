import os
import sys
import json

if(len(sys.argv) != 2):
    print("Utilisation : RL.py nombreDeCombat ") 
    sys.exit(1); 

nbFight = int(sys.argv[1])

for i in range(nbFight) : 
    os.system('java -jar generator.jar test/scenario/scenarioTrain.json > resultat.json')

    
    with open("resultat.json", "r") as f:
        lines = f.readlines()
    with open("resultat.json", "w") as f:
        for line in lines:
            if line.strip("\n") != "db_resolver false folder=0 farmer=0":
                f.write(line)

    
    with open("test/scenario/scenarioTrain.json") as jsonFile:
        scenario = json.load(jsonFile)
        jsonFile.close()

     
    with open("resultat.json") as jsonFile:
        resultat = json.load(jsonFile)
        jsonFile.close()

    winnerID = int(resultat['winner']) 
    winnerName = scenario["entities"][winnerID][0]["name"]

    print("Combat " + str(i) + " : " + str(winnerID) + " -> " + winnerName)

    nbFight = nbFight - 1