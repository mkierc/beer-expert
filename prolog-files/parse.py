import sys


filename = sys.argv[1]

categoryPref = "\ncategory("
categorySuf = ") :- .\n"

def clean(type):
    type = type.replace(" [minlen=2]","")
    type = type.replace(" [minlen=3]","")
    type = type.replace("\\n"," ")
    type = type.replace(";","")
    type = type[2:]
    type = type.replace('"',"")
    type = type.replace(" ","_")
    type = type.replace("/","_")
    return type.lower()    

def format_type(type):
    return categoryPref+clean(type)+categorySuf

stylePref = "beerstyle("
styleMid = ") :- category("

def format_style(style,type):
    return stylePref+clean(style)+styleMid+clean(type)+").\n"

piwo = open(filename,"r")
piwopg = open(filename+".pg","w+")
type = ''
for line in piwo.readlines():
    if('"Piwa"' in line):
        type = line.split("->")[1][:-1]
        piwopg.write(format_type(type))
    elif('"' in line):
        style = line.split("->")[1][:-1]
        piwopg.write(format_style(style,type))

piwopg.flush()
piwopg.close()
