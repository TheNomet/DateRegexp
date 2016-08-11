#!/bin/bash

# a and b are used to test the number of errors

# the test.txt file contains 20 numbers out of range, so at the end there must be 20 errors

# this is done in order to test also a small range out of the one specified by the user, to make sure the 
# regular expression is correct

a=$1
let "a -= 10"
b=$2
let "b += 10"

#echo $a $b

if [ $# -eq 3 ]; then
	final="$(./do.sh $1 $2 num $3)"
elif [ $# -eq 2 ]; then
	final="$(./do.sh $1 $2 num)"
else 
	echo "Syntax: ./from.sh <from> <to> [<1 -from odd numbers> <0 -for even numbers>]? "
	exit
fi

#echo $final
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
.+ 					{System.out.println(\"error:\"+yytext());}
//UTILS
" >> scanner.jflex


seq $a $b > test.txt

jflex scanner.jflex 1>/dev/null 
javac Yylex.java 1>/dev/null 2>/dev/null
#java Yylex test.txt #remove the comment to show each number recognized
errors="$(java Yylex test.txt | grep error | wc -l)"
if [ $errors -eq 20 ]; then
	echo "Everything is OK!"
else 
	echo "ERRORS found -> the regular expression wasn't correct"

fi
 cd ..
