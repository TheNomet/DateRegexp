
parser = ./utils/parser.java

all : $(parser)

$(parser) : scanner.jflex parser.cup Makefile 
	jflex scanner.jflex 
	java java_cup.Main -expect 4 parser.cup  
	javac *.java 
	mv *.java  ./utils
	mv *.class ./utils
	mv ./utils/Main.java ./


draw : scanner.jflex parser.cup 
	jflex scanner.jflex 
	java java_cup.MainDrawTree -expect 4 parser.cup 
	javac *.java 


