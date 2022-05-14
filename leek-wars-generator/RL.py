import os
import sys
import json
import random
import collections
import numpy as np


print ("This is the name of the script: ", sys.argv[0])
print ("Number of arguments: ", len(sys.argv))
print ("The arguments are: " , str(sys.argv))

if(len(sys.argv) != 2):
    print("Utilisation : RL.py nombreDeCombats")
    exit(1)

nbFight = int(sys.argv[1]) 
tabAssociatif = {}
tabRL = []

freeIndex = 0


for i in range(nbFight) : 
    with open("test/scenario/scenarioTrain2.json") as jsonFile:
        scenario = json.load(jsonFile)
        jsonFile.close()

    scenario["random_seed"] = random.randint(0,10000000)

    with open('test/scenario/scenarioTrain2.json', 'w') as f:
        json.dump(scenario, f)
    
    fileName = 'resultat.json'
    
    os.system('java -jar generator.jar test/scenario/scenarioTrain2.json > ' + fileName)
    
    
    with open(fileName, "r") as f:
        lines = f.readlines()
    with open(fileName, "w") as f:
        for line in lines:
            if line.strip("\n") != "db_resolver false folder=0 farmer=0":
                f.write(line)
     
    with open(fileName) as jsonFile:
        resultat = json.load(jsonFile)
        jsonFile.close()
        
        
    logs = resultat['logs']['0']

    sortedLogs = collections.OrderedDict(sorted(logs.items()))

    winnerID = int(resultat['winner']) 
    winnerName = scenario["entities"][winnerID][0]["name"]
    duration = resultat["duration"]
    execution_time = resultat["execution_time"]

    print("Combat " + str(i + 1) + " : Winner -> " + winnerName + " // Durée : " + str(duration) + " tours // Temps : " +  str(execution_time / 10000000) + " sec")
    

    for log in sortedLogs: 
        rl = str(sortedLogs[log][0][2])
        rl = rl.split('/')
        print(rl)
        #tabRL[rl[0]] = int(rl[1])
        if rl[0] not in tabAssociatif :
            tabAssociatif[rl[0]] = freeIndex
            for i in range(16):
                tabRL.append(0)
            tabRL[(freeIndex*16)+int(rl[1])] = int(rl[2])
            freeIndex += 1
        else :
            tabRL[(tabAssociatif[rl[0]]*16)+int(rl[1])] += int(rl[2])


        #tabRL[int(rl[0])] += int(rl[1])
        #tabRL[int(rl[2])] += int(rl[3])
    

    with open('test/ai/Tab.leek', 'w') as f:
        f.write("var tabAssiocatif = []; \n")
        for elem in tabAssociatif:
            f.write("tabAssiocatif["+str(elem)+"] = "+str(tabAssociatif[elem])+"; \n")

        f.write("var tabRL = []; \n")
        for elem in tabRL :
            f.write("push(tabRl,"+str(elem)+"); \n")

        f.close()

   



print("--------------------------------------------------------------")
print("// Tableau de renforcement")
print("global tab = [];")
print(tabAssociatif)
print(tabRL)

for t in tabAssociatif:
    print(tabAssociatif[t])

