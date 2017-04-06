import static analisador.Token.*;
import static analisador.TokenIDS.*
%%
%class Lexer
%type Token
%line
%column

L = [a-zA-Z_]
D = [0-9]
WHITE=[ \t\r\n]
%{
public newToken(TokenID tokenID, String value) {
	return new Token(tokenID, value, column, line);
} ;
%}
%%
{WHITE} {/*Ignore*/}

/* Operadores Relacionais */
( ">" | "<" | "<=" | ">=" | "==" | "!=" ) { return newToken(RELA_OP, yytext());}

/* Operadores soma&subtração  */
( "+" | "-" ) { return newToken(ADDSUB_OP, yytext());}

/* Operadores de mult&div */
( "*" | "/" | "<<" | ">>" | ">>>" ) { return newToken(ARITH_OP, yytext());}

/* Operadores BITWISE */
("&" | "|" | "^" )    {return newToken(BITWISE_OP, yytext());}

/*Operador not */
("!")   {return newToken(NOT_OP, yytext());}

/* WHILE */
("while")      {return newToken(WHILE, yytext());}

/* IF */
("if")      {return newToken(IF, yytext());}

/* ELSE */
("else")      {return newToken(ELSE, yytext());}

/* ASSIGN */
("=")      {return newToken(ASSIGN, yytext());}

/* ASPA */
("\")      {return newToken(ASPA, yytext());}

/* LPAR */
("(")      {return newToken(LPAR, yytext());}

/* RPAR */
(")")      {return newToken(RPAR, yytext());}

/* VIRG */
(",")      {return newToken(VIRG, yytext());}

/* PVIRG */
(";")      {return newToken(PVIRG, yytext());}

/* LCHAVETA */
("{")      {return newToken(LCHAVETA, yytext());}

/* RCHAVETA */
("}")      {return newToken(RCHAVETA, yytext());}

/* FUNCTION */
("function")      {return newToken(FUNCTION, yytext());}

/* MODULE */
("module")      {return newToken(MODULE, yytext());}

/* SIZE */
("size")      {return newToken(SIZE, yytext());}

/* Comentarios */
( "//" | "/*" | "*/")     { }



{L}({L}|{D})* {return newToken(ID, yytext());}
({D})+ {return newToken(INTEGER, yytext());}
("\"" {L}+ "\"") {return newToken(String, yytex());}



. {return ERROR;}
