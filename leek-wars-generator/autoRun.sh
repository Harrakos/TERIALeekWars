#!/bin/bash
nbRun=10 
i=0
while ((i<nbRun))
do 
	java -jar generator.jar test/scenario/scenario1.json > ../resultat.json
	i=$((i+1))
	echo Combat $i 
done
exit 0
