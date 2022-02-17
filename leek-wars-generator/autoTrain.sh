#!/bin/bash
nbRun=1
i=0

while ((i<nbRun))
do 
	java -jar generator.jar test/scenario/scenarioTrain.json > resultat.json
	i=$((i+1))
    
	output=$((python RL.py))

	echo Combat $i $output
done
exit 0
