import java_cup.runtime.*;
import java.util.*;
%%

//%standalone
%cup
%unicode
%line
%column
%xstate people

%{
	private void print(String str){
		System.out.print(str+" ");
	}	
	private void println(String str){
		System.out.println(str+" ");
	}
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
	
  }
  
%}

nl = \r|\n|\r\n
space = " "|\t

comment = \/\/.*

id = n(X)*[0-9]+

integer = 0|[1-9][0-9]*

sep = "//-----------//"

%%
"ALL"			{return symbol(sym.ALL, new Integer(-1));}			
"ODD"			{return symbol(sym.ODD, new Integer(1));}
"EVEN"			{return symbol(sym.EVEN, new Integer(0));}
"END"			{return symbol(sym.END);}
"="				{return symbol(sym.EQ);}
"$"				{return symbol(sym.DOL);}
";"				{return symbol(sym.S);}
","				{return symbol(sym.C);}
"("				{return symbol(sym.RO);}
")"				{return symbol(sym.RC);}
"["				{return symbol(sym.SO);}
"]"				{return symbol(sym.SC);}
"{"				{return symbol(sym.GO);}
"}"				{return symbol(sym.GC);}
"+"				{return symbol(sym.PLUS);}
"-"				{return symbol(sym.MINUS);}
"|"				{return symbol(sym.PIPE);}
"*"				{return symbol(sym.MUL);}
{id}			{return symbol(sym.ID, new String(yytext()));}
{integer}		{return symbol(sym.INT, new String(yytext()));}

{sep}			{return symbol(sym.SEP);}

//UTILS
{comment}			{;}
{nl}  				{;}
{space} 			{;}
. 					{print("error: "+yytext());}
//UTILS

