import os
import sys
import json
import random
import collections
import numpy as np
from alive_progress import alive_bar
from time import sleep


print ("This is the name of the script: ", sys.argv[0])
print ("Number of arguments: ", len(sys.argv))
print ("The arguments are: " , str(sys.argv))

if(len(sys.argv) != 2):
    print("Utilisation : RL.py nombreDeCombats")
    exit(1)

nbFight = int(sys.argv[1]) 
tabAssociatif = {}
tabRL = []
file_scenario = "test/scenario/scenario-lvl301.json"

freeIndex = 0

    
with open('test/ai/IA-train.leek', 'w') as f:
    f.write("")
    f.close()

with alive_bar(nbFight) as bar:
    for i in range(nbFight) : 
        with open(file_scenario) as jsonFile:
            scenario = json.load(jsonFile)
            jsonFile.close()

        scenario["random_seed"] = random.randint(0,10000000)

        with open(file_scenario, 'w') as f:
            json.dump(scenario, f)
        
        fileName = 'resultat.json'
        
        os.system('java -jar generator.jar '+ file_scenario +' > ' + fileName)
        
        
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

        print("Combat " + str(i + 1) + " : Winner -> " + winnerName + " // Duree : " + str(duration) + " tours // Temps : " +  str(execution_time / 10000000) + " sec")
        

        for log in sortedLogs: 
            rl = str(sortedLogs[log][0][2])
            rl = rl.split('/')
            print(rl)
            #tabRL[rl[0]] = int(rl[1])

            if rl[0] not in tabAssociatif :
                if freeIndex == 100 :


                    # TODO faire le truc









                else :
                    tabAssociatif[rl[0]] = freeIndex
                    for i in range(44):
                        tabRL.append(1)
                    if int(rl[2]) == 0 :
                        tabRL[(freeIndex*44)+int(rl[1])] = 1
                        freeIndex += 1
                    else:
                        tabRL[(freeIndex*44)+int(rl[1])] = int(rl[2])
                        freeIndex += 1
            else :
                tabRL[(tabAssociatif[rl[0]]*44)+int(rl[1])] += int(rl[2])

        with open('test/ai/IA-train.leek', 'w') as f:
            f.write("var tabAssiocatif = []; \n")
            for elem in tabAssociatif:
                f.write("tabAssiocatif['"+str(elem)+"'] = "+str(tabAssociatif[elem])+"; \n")
            f.write("var tabRL = [ ")
            for elem in tabRL :
                f.write(str(elem)+",")
            f.write("];\n")
            f.write("\n")
            f.write("\n")
            with open('test/ai/IA-train_Script.leek',"r") as script:
                for line in script.readlines():
                    f.write(line)
                script.close()
            f.close()



        


        print("--------------------------------------------------------------")
        print("// Tableau de renforcement")
        print("global tab = [];")
        #print(tabAssociatif)
        #print(tabRL)
        bar()



    




