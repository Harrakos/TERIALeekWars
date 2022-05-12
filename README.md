# TERIALeekWars


## Contact 

* Thomas Juliat, [Harrakos](https://github.com/Harrakos)
* Loic Schnuriger, [LoicSCHN](https://github.com/LoicSCHN)
* Alexandre Cauty, [alexandre-cauty08](https://github.com/alexandre-cauty08)
* Lucas Aissaoui, [AissAiss](https://github.com/AissAiss)

## TODO : 

* Fonction de vectorisation
* Système d'entreinement simultané (RLRLipynb et RLRL.py)
* Améliorer la sélection des actions (Stats/Pondéré bestActionTP/bestActionMP) 

## CMD 
python3 -m json.tool file.json > res.json


## Exemple vectorisation : 

vectorisation() -> "0100100111100100100011"

tabVec

["0100100111100100100011" : 0]   ->   tab[0 * 16];   ->   (Sélection des actions)  

["0100100111100100101011" : 1]   ->   tab[1 * 16];

["0100000111100100100011" : 2]   ->   tab[2 * 16];

[...]

["0111100111100100100011" : 56]  ->   tab[56 * 16];
