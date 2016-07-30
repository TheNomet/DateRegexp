#!/bin/bash


if [ $# -eq 3 ]; then
	final="$(./do.sh $1 $2 num $3)"
elif [ $# -eq 2 ]; then
	final="$(./do.sh $1 $2 num)"
else 
	echo "Syntax: ./from.sh <from> <to> [<1 -from odd numbers> <0 -for even numbers>]? "
	exit
fi

cd ./fortest

touch test.txt 1> /dev/null 2> /dev/null
touch scanner.jflex 1> /dev/null 2> /dev/null
echo "
%%

%standalone
%unicode

nl = \r|\n|\r\n
space = \" \"|\t
" > scanner.jflex

IFS=' '

for word in $final 
	do echo $word >> scanner.jflex
done

echo "
%%

^{num}$ {System.out.println(\"NUM\"+yytext());}

//UTILS
{nl}  				{;}
{space} 			{;}
. 					{System.out.println(\"error:\"+yytext());}
//UTILS
" >> scanner.jflex


seq $1 $2 > test.txt

jflex scanner.jflex 1>/dev/null 
javac Yylex.java 1>/dev/null 2>/dev/null
java Yylex test.txt
errors="$(java Yylex test.txt | grep error | wc -l)"
if [ $errors -gt 0 ]; then
	echo "ERRORS found -> the regular expression wasn't correct"
else 
	echo "Everything is OK!"
fi
 cd ..
