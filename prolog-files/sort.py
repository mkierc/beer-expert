import sys


filename = sys.argv[1]

piwo = open(filename,"r")
piwopg = open(filename+".sort","w+")
categories = []
styles = []
for line in piwo.readlines():
    if(line.startswith("cat")):
        categories.append(line)
    elif(line.startswith("beer")):
        styles.append(line)


for cat in categories:
    piwopg.write(cat)
   
for style in styles:
    piwopg.write(style)

piwopg.flush()
piwopg.close()
