with open("resultat.json", "r") as f:
    lines = f.readlines()
with open("resultat.json", "w") as f:
    for line in lines:
        if line.strip("\n") != "db_resolver false folder=0 farmer=0":
            f.write(line)