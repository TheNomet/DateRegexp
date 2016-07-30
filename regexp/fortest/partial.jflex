
%%

%standalone
%unicode

nl = \r|\n|\r\n
space = " "|\t

|--|

%%

|--|

//UTILS
{nl}  				{;}
{space} 			{;}
. 					{System.out.println("error:"+yytext()); System.out.print(" ");}
//UTILS
