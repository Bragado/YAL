package AnalisadorSintático;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

import Estruturas.*;

public class ParserTable {
	
	public EnumMap<NonTerminalID, Integer> NonTerminals = new EnumMap<NonTerminalID, Integer>(NonTerminalID.class);
	public EnumMap<TokenID, Integer> Tokens = new EnumMap<TokenID, Integer>(TokenID.class);
	
	public class SymbolID {
		
		private boolean token;
		private TokenID tokenID;
		private NonTerminalID nonTerminalID;
		
		public boolean isToken() {
			return token;
		}
		
		public Object getID() {
			return token == true ? tokenID : nonTerminalID;
		}
		
		public SymbolID(TokenID tokenID) {
			this.token = true;
			this.tokenID = tokenID;
		}
		
		public SymbolID(NonTerminalID nonTerminalID) {
			this.token = false;
			this.nonTerminalID = nonTerminalID;
		}
		
	}
	
	
	ArrayList[][] table = new ArrayList[49][28];
	
	
	public ParserTable() {
		inicializeEnumMaps();
		inicializeTable();
	
	}
	
	public ArrayList<SymbolID> getSymbols(NonTerminalID nonTerminal, TokenID token) {
		return table[NonTerminals.get(nonTerminal)][Tokens.get(token)];
	}
	
	


	private void inicializeTable() {
		// TODO Auto-generated method stub
		
		// Module(module) -> module id lchaveta Dec Func rchaveta
		ArrayList<SymbolID> aux = new ArrayList<SymbolID>();
		aux.add(new SymbolID(TokenID.MODULE));
		aux.add(new SymbolID(TokenID.ID));
		aux.add(new SymbolID(TokenID.LCHAVETA));
		aux.add(new SymbolID(NonTerminalID.DEC));
		aux.add(new SymbolID(NonTerminalID.FUNC));
		aux.add(new SymbolID(TokenID.RCHAVETA));
				
		table[NonTerminals.get(NonTerminalID.MODULE)][Tokens.get(TokenID.MODULE)] = aux;
		
		
		// Dec(id) -> Declaration Dec
		aux = new ArrayList<SymbolID>();
		aux.add(new SymbolID(NonTerminalID.DECLARATION));
		aux.add( new SymbolID(NonTerminalID.DEC));
		table[NonTerminals.get(NonTerminalID.DEC)][Tokens.get(TokenID.ID)] =  aux;
		
		// Dec(rchaveta) -> EPSILON
		aux = new ArrayList<SymbolID>();
		aux.add(new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.DEC)][Tokens.get(TokenID.RCHAVETA)] = aux;
		
		// Dec(function) -> EPSILON
		aux = new ArrayList<SymbolID>();
		aux.add(new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.DEC)][Tokens.get(TokenID.FUNCTION)] = aux;
		
		
		
		// Func(rchaveta) -> EPSILON
		aux = new ArrayList<SymbolID>();
		aux.add(new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.FUNC)][Tokens.get(TokenID.RCHAVETA)] = aux;
		
		
		// Func(function) -> Function Func
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Function), new SymbolID(NonTerminalID.FUNC));
		table[NonTerminals.get(NonTerminalID.FUNC)][Tokens.get(TokenID.FUNCTION)] = aux;
		
			
		
		//Declaration(id) -> ArrayORScalarElement DeclarationEnd pvirg
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.ArrayOrScalarElement), new SymbolID(NonTerminalID.DeclarationEnd), new SymbolID(TokenID.PVIRG));
		table[NonTerminals.get(NonTerminalID.DECLARATION)][Tokens.get(TokenID.ID)] = aux;
		
		//Function(function) -> FunctionBegin lchav Stmtlst rchav
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.FunctionBegin), new SymbolID(TokenID.LCHAVETA), new SymbolID(NonTerminalID.Stmtlst), new SymbolID(TokenID.RCHAVETA));
		table[NonTerminals.get(NonTerminalID.Function)][Tokens.get(TokenID.FUNCTION)] = aux;
				
		
		//FunctionBegin(function) -> function FunctionID lpar VarFunc rpar		
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.FUNCTION), new SymbolID(NonTerminalID.FunctionID), new SymbolID(TokenID.LPAR), new SymbolID(NonTerminalID.VarFunc), new SymbolID(TokenID.RPAR));
		table[NonTerminals.get(NonTerminalID.FunctionBegin)][Tokens.get(TokenID.FUNCTION)] = aux;
		
		
		//ArrORScal(assign) -> EPSILON
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArrOrScal)][Tokens.get(TokenID.ASSIGN)] = aux;
		
		
		//ArrORScal(rpar) -> EPSILON
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArrOrScal)][Tokens.get(TokenID.RPAR)] = aux;
		
		//ArrORScal(virg) -> EPSILON
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArrOrScal)][Tokens.get(TokenID.VIRG)] = aux;
		
		//ArrORScal(pvirg) -> EPSILON
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArrOrScal)][Tokens.get(TokenID.PVIRG)] = aux;
		
		//ArrORScal(id) -> EPSILON
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArrOrScal)][Tokens.get(TokenID.ID)] = aux;
		
		//ArrORScal(lparR) -> lparR rparR
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(TokenID.RPARR));
		table[NonTerminals.get(NonTerminalID.ArrOrScal)][Tokens.get(TokenID.LPARR)] = aux;
		
		
		//ArrayORScalarElement(id) -> id ArrORScal
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.ArrOrScal));
		table[NonTerminals.get(NonTerminalID.ArrayOrScalarElement)][Tokens.get(TokenID.ID)] = aux;
		
		
		//DeclarationEnd(pvirg) -> EPSILON
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.DeclarationEnd)][Tokens.get(TokenID.PVIRG)] = aux;
		
		
		//DeclarationEnd(assign) -> assign DecAssign
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ASSIGN), new SymbolID(NonTerminalID.DecAssign));
		table[NonTerminals.get(NonTerminalID.DeclarationEnd)][Tokens.get(TokenID.ASSIGN)] = aux;
		
		
		//DecAssign(addsub_op) -> ADD_SUBBOP integer
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.AddSubbOP),new SymbolID(TokenID.INTEGER));
		table[NonTerminals.get(NonTerminalID.DecAssign)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//DecAssign(integer) -> ADD_SUBBOP integer
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.AddSubbOP),new SymbolID(TokenID.INTEGER));
		table[NonTerminals.get(NonTerminalID.DecAssign)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//DecAssign(lparR) -> lparR ArraySize rparR
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(NonTerminalID.ArraySize),new SymbolID(TokenID.RPARR));
		table[NonTerminals.get(NonTerminalID.DecAssign)][Tokens.get(TokenID.LPARR)] = aux;
		
		//ADD_SUBBOP(addsub_op) -> addsub_op
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ADDSUB_OP));
		table[NonTerminals.get(NonTerminalID.AddSubbOP)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//ADD_SUBBOP(integer) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.AddSubbOP)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//FunctionID(id) -> id FunctionAssign
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.FunctionAssign));
		table[NonTerminals.get(NonTerminalID.FunctionID)][Tokens.get(TokenID.ID)] = aux;
		

		//FunctionAssign(assign) -> assign id
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ASSIGN), new SymbolID(TokenID.ID));
		table[NonTerminals.get(NonTerminalID.FunctionAssign)][Tokens.get(TokenID.ASSIGN)] = aux;
		
		//FunctionAssign(lpar) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.FunctionAssign)][Tokens.get(TokenID.LPAR)] = aux;
		
		
		//FunctionAssign(lparR) -> lparR rparR assign id
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(TokenID.RPARR), new SymbolID(TokenID.ASSIGN), new SymbolID(TokenID.ID));
		table[NonTerminals.get(NonTerminalID.FunctionAssign)][Tokens.get(TokenID.LPARR)] = aux;
				
		//VarFunc(rpar) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.VarFunc)][Tokens.get(TokenID.RPAR)] = aux;
		
		
		//VarFunc(id) -> VarList VarFunc
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.VarList), new SymbolID( NonTerminalID.VarFunc));
		table[NonTerminals.get(NonTerminalID.VarFunc)][Tokens.get(TokenID.ID)] = aux;
		
		//VarList(id) -> ArrayORScalarElement VarListEND
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.ArrayOrScalarElement), new SymbolID( NonTerminalID.VarListEnd));
		table[NonTerminals.get(NonTerminalID.VarList)][Tokens.get(TokenID.ID)] = aux;
		
		
		//VarListEnd(rpar) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.VarListEnd)][Tokens.get(TokenID.RPAR)] = aux;
		
		//VarListEND(virg) -> virg ArrayORScalarElement VarListEND
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.VIRG), new SymbolID(NonTerminalID.ArrayOrScalarElement), new SymbolID(NonTerminalID.VarListEnd));
		table[NonTerminals.get(NonTerminalID.VarListEnd)][Tokens.get(TokenID.VIRG)] = aux;
		
		//VarListEnd(id) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.VarListEnd)][Tokens.get(TokenID.ID)] = aux;
		
		//Stmtlst(while) -> Stmt Stmtlst
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Stmt), new SymbolID(NonTerminalID.Stmtlst));
		table[NonTerminals.get(NonTerminalID.Stmtlst)][Tokens.get(TokenID.WHILE)] = aux;
		
		//Stmtlst(if) -> Stmt Stmtlst
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Stmt), new SymbolID(NonTerminalID.Stmtlst));
		table[NonTerminals.get(NonTerminalID.Stmtlst)][Tokens.get(TokenID.IF)] = aux;
		//Stmtlst(rchav) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Stmtlst)][Tokens.get(TokenID.RCHAVETA)] = aux;
		
		//Stmtlst(id) -> Stmt Stmtlst
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Stmt), new SymbolID(NonTerminalID.Stmtlst));
		table[NonTerminals.get(NonTerminalID.Stmtlst)][Tokens.get(TokenID.ID)] = aux;
		
		//Stmt(while) -> WHILE
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.WHILE));
		table[NonTerminals.get(NonTerminalID.Stmt)][Tokens.get(TokenID.WHILE)] = aux;
		
		//Stmt(if) -> IF
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.IF));
		table[NonTerminals.get(NonTerminalID.Stmt)][Tokens.get(TokenID.IF)] = aux;
		
		//Stmt(id) -> CallAssign
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.CallAssign));
		table[NonTerminals.get(NonTerminalID.Stmt)][Tokens.get(TokenID.ID)] = aux;
		
		//WHILE(while) -> while EXPRtest lchav Stmtlst rchav
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.WHILE), new SymbolID(NonTerminalID.EXPRtest), new SymbolID(TokenID.LCHAVETA), new SymbolID(NonTerminalID.Stmtlst), new SymbolID(TokenID.RCHAVETA));
		table[NonTerminals.get(NonTerminalID.WHILE)][Tokens.get(TokenID.WHILE)] = aux;
		
		
		
		//IF(if) -> if EXPRtest lchav Stmtlst rchav ELSE
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.IF), new SymbolID(NonTerminalID.EXPRtest), new SymbolID(TokenID.LCHAVETA), new SymbolID(NonTerminalID.Stmtlst), new SymbolID(TokenID.RCHAVETA), new SymbolID(NonTerminalID.ELSE));
		table[NonTerminals.get(NonTerminalID.IF)][Tokens.get(TokenID.IF)] = aux;
		
		//ELSE(while) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ELSE)][Tokens.get(TokenID.WHILE)] = aux;
		
		//ELSE(if) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ELSE)][Tokens.get(TokenID.ID)] = aux;
		
		//ELSE(else) -> else lchav Stmtlst rchav
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ELSE), new SymbolID(TokenID.LCHAVETA), new SymbolID(NonTerminalID.Stmtlst), new SymbolID(TokenID.RCHAVETA));
		table[NonTerminals.get(NonTerminalID.ELSE)][Tokens.get(TokenID.ELSE)] = aux;
		
		//ELSE(rchav) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ELSE)][Tokens.get(TokenID.RCHAVETA)] = aux;
		
		//ELSE(id) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ELSE)][Tokens.get(TokenID.ID)] = aux;
		
		//CallAssign(id) -> id CallAssignAux pvirg 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.CallAssignAux), new SymbolID(TokenID.PVIRG));
		table[NonTerminals.get(NonTerminalID.CallAssign)][Tokens.get(TokenID.ID)] = aux;
		
		//EXPRtest(lpar) -> lpar LHS rela_op RHS rpar
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPAR),  new SymbolID(NonTerminalID.LHS), new SymbolID(TokenID.RELA_OP),  new SymbolID(NonTerminalID.RHS), new SymbolID(TokenID.RPAR));
		table[NonTerminals.get(NonTerminalID.EXPRtest)][Tokens.get(TokenID.LPAR)] = aux;
		
		//LHS(id) -> id Access
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.Access));
		table[NonTerminals.get(NonTerminalID.LHS)][Tokens.get(TokenID.ID)] = aux;
		
		//Access(rela_op) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Access)][Tokens.get(TokenID.RELA_OP)] = aux;
		
		//Access(ponto) -> ponto size
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.PONTO), new SymbolID(TokenID.SIZE));
		table[NonTerminals.get(NonTerminalID.Access)][Tokens.get(TokenID.PONTO)] = aux;
		
		//Access(lparR) -> lparR Index rparR
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(NonTerminalID.Index), new SymbolID(TokenID.RPARR));
		table[NonTerminals.get(NonTerminalID.Access)][Tokens.get(TokenID.LPARR)] = aux;
		
		//Index(id) -> id
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID));
		table[NonTerminals.get(NonTerminalID.Index)][Tokens.get(TokenID.ID)] = aux;
		
		//Index(integer) -> integer
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.INTEGER));
		table[NonTerminals.get(NonTerminalID.Index)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//RHS(addsub_op) -> RHS1
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.RHS1));
		table[NonTerminals.get(NonTerminalID.RHS)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//RHS(id) -> RHS1
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.RHS1));
		table[NonTerminals.get(NonTerminalID.RHS)][Tokens.get(TokenID.ID)] = aux;
		
		//RHS(integer) -> RHS1
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.RHS1));
		table[NonTerminals.get(NonTerminalID.RHS)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//RHS(lparR) -> lparR ArraySize rparR
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(NonTerminalID.ArraySize), new SymbolID(TokenID.RPARR));
		table[NonTerminals.get(NonTerminalID.RHS)][Tokens.get(TokenID.LPARR)] = aux;
			
		//RHS1(addsub_op) -> Term ArithTerm
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Term), new SymbolID(NonTerminalID.ArithTerm));
		table[NonTerminals.get(NonTerminalID.RHS1)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//RHS1(id) -> Term ArithTerm
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Term), new SymbolID(NonTerminalID.ArithTerm));
		table[NonTerminals.get(NonTerminalID.RHS1)][Tokens.get(TokenID.ID)] = aux;
		
		//RHS1(integer) -> Term ArithTerm
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Term), new SymbolID(NonTerminalID.ArithTerm));
		table[NonTerminals.get(NonTerminalID.RHS1)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//Term(addsub_op) -> Term1 Term2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Term1), new SymbolID(NonTerminalID.Term2));
		table[NonTerminals.get(NonTerminalID.Term)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//Term(id) -> Term1 Term2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Term1), new SymbolID(NonTerminalID.Term2));
		table[NonTerminals.get(NonTerminalID.Term)][Tokens.get(TokenID.ID)] = aux;
		
		//Term(integer) -> Term1 Term2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Term1), new SymbolID(NonTerminalID.Term2));
		table[NonTerminals.get(NonTerminalID.Term)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//Term1(addsub_op) -> addsub_op
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ADDSUB_OP));
		table[NonTerminals.get(NonTerminalID.Term1)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//Term1(id) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term1)][Tokens.get(TokenID.ID)] = aux;
		
		//Term1(integer) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term1)][Tokens.get(TokenID.INTEGER)] = aux;
		

		//Term2(id) -> id Term3
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.Term3));
		table[NonTerminals.get(NonTerminalID.Term2)][Tokens.get(TokenID.ID)] = aux;
		
		//Term2(integer) -> integer 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.INTEGER));
		table[NonTerminals.get(NonTerminalID.Term2)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//ArithTerm(addsub_op) -> OP Term
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.OP), new SymbolID(NonTerminalID.Term));
		table[NonTerminals.get(NonTerminalID.ArithTerm)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//ArithTerm(arith_op) -> OP Term
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.OP), new SymbolID(NonTerminalID.Term));
		table[NonTerminals.get(NonTerminalID.ArithTerm)][Tokens.get(TokenID.ARITH_OP)] = aux;
		
		//ArithTerm(bitwise_op) -> OP Term
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.OP), new SymbolID(NonTerminalID.Term));
		table[NonTerminals.get(NonTerminalID.ArithTerm)][Tokens.get(TokenID.BITWISE_OP)] = aux;
		
		//ArithTerm(rpar) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArithTerm)][Tokens.get(TokenID.RPAR)] = aux;
		
		//ArithTerm(pvirg) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArithTerm)][Tokens.get(TokenID.PVIRG)] = aux;
		

		//Term3(addsub_op) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//Term3(arith_op) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.ARITH_OP)] = aux;
		
		//Term3(bitwise_op) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.BITWISE_OP)] = aux;
		
		//Term3(lpar) -> Call 
		//Term3(rpar) -> '' 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.RPAR)] = aux;
		
		//Term3(pvirg) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.PVIRG)] = aux;
		
		//Term3(ponto) -> ponto CallOrScalarAccess 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.PONTO), new SymbolID(NonTerminalID.CallOrScalarAccess));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.PONTO)] = aux;
		
		//Term3(lparR) -> lparR Index rparR 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(NonTerminalID.Index), new SymbolID(TokenID.RPARR));
		table[NonTerminals.get(NonTerminalID.Term3)][Tokens.get(TokenID.LPARR)] = aux;
		
		//OP(addsub_op) -> addsub_op
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ADDSUB_OP));
		table[NonTerminals.get(NonTerminalID.OP)][Tokens.get(TokenID.ADDSUB_OP)] = aux;
		
		//OP(arith_op) -> arith_op
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ARITH_OP));
		table[NonTerminals.get(NonTerminalID.OP)][Tokens.get(TokenID.ARITH_OP)] = aux;
		
		//OP(bitwise_op) -> bitwise_op
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.BITWISE_OP));
		table[NonTerminals.get(NonTerminalID.OP)][Tokens.get(TokenID.BITWISE_OP)] = aux;
		

		//CallOrScalarAccess(size) -> size 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.SIZE));
		table[NonTerminals.get(NonTerminalID.CallOrScalarAccess)][Tokens.get(TokenID.SIZE)] = aux;
		
		//CallOrScalarAccess(id) -> id Call 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.Call));
		table[NonTerminals.get(NonTerminalID.CallOrScalarAccess)][Tokens.get(TokenID.ID)] = aux;
		
		//ArraySize(id) -> ScalarAccess
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.ScalarAccess));
		table[NonTerminals.get(NonTerminalID.ArraySize)][Tokens.get(TokenID.ID)] = aux;
		
		//ArraySize(integer) -> integer
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.INTEGER));
		table[NonTerminals.get(NonTerminalID.ArraySize)][Tokens.get(TokenID.INTEGER)] = aux;
		
		//ScalarAccess(id) -> id ScalarAccess1
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.ScalarAccess1));
		table[NonTerminals.get(NonTerminalID.ScalarAccess)][Tokens.get(TokenID.ID)] = aux;
		
		//ScalarAccess1(ponto) -> ponto size
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.PONTO), new SymbolID(TokenID.SIZE));
		table[NonTerminals.get(NonTerminalID.ScalarAccess1)][Tokens.get(TokenID.PONTO)] = aux;
		//ScalarAccess1(rparR) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ScalarAccess1)][Tokens.get(TokenID.RPARR)] = aux;

		//ArgumentList(rpar) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.ArgumentList)][Tokens.get(TokenID.RPAR)] = aux;
		//ArgumentList(id) -> Argument Argument2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Argument), new SymbolID(NonTerminalID.Argument2));
		table[NonTerminals.get(NonTerminalID.ArgumentList)][Tokens.get(TokenID.ID)] = aux;
		//ArgumentList(string) -> Argument Argument2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Argument), new SymbolID(NonTerminalID.Argument2));
		table[NonTerminals.get(NonTerminalID.ArgumentList)][Tokens.get(TokenID.STRING)] = aux;
		//ArgumentList(integer) -> Argument Argument2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Argument), new SymbolID(NonTerminalID.Argument2));
		table[NonTerminals.get(NonTerminalID.ArgumentList)][Tokens.get(TokenID.INTEGER)] = aux;

		//Argument2(rpar) -> ''
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.EPSILON));
		table[NonTerminals.get(NonTerminalID.Argument2)][Tokens.get(TokenID.RPAR)] = aux;
		//Argument2(virg) -> virg Argument Argument2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.VIRG), new SymbolID(NonTerminalID.Argument), new SymbolID(NonTerminalID.Argument2));
		table[NonTerminals.get(NonTerminalID.Argument2)][Tokens.get(TokenID.VIRG)] = aux;

		//Argument(id) -> id  
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID));
		table[NonTerminals.get(NonTerminalID.Argument)][Tokens.get(TokenID.ID)] = aux;
		//Argument(string) -> string  
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.STRING));
		table[NonTerminals.get(NonTerminalID.Argument)][Tokens.get(TokenID.STRING)] = aux;
		//Argument(integer) -> integer 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.INTEGER));
		table[NonTerminals.get(NonTerminalID.Argument)][Tokens.get(TokenID.INTEGER)] = aux;

		//CallAssignAux(assign) -> Assign2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Assign2));
		table[NonTerminals.get(NonTerminalID.CallAssignAux)][Tokens.get(TokenID.ASSIGN)] = aux;
		//CallAssignAux(lpar) -> Call
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Call));
		table[NonTerminals.get(NonTerminalID.CallAssignAux)][Tokens.get(TokenID.LPAR)] = aux;
		//CallAssignAux(ponto) -> CallAssign1
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.CallAssign1));
		table[NonTerminals.get(NonTerminalID.CallAssignAux)][Tokens.get(TokenID.PONTO)] = aux;
		//CallAssignAux(lparR) -> Assign
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Assign));
		table[NonTerminals.get(NonTerminalID.CallAssignAux)][Tokens.get(TokenID.LPARR)] = aux;

		//CallAssign2(size) -> size Assign2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.SIZE), new SymbolID(NonTerminalID.Assign2));
		table[NonTerminals.get(NonTerminalID.CallAssign2)][Tokens.get(TokenID.SIZE)] = aux;
		//CallAssign2(id) -> id Call
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ID), new SymbolID(NonTerminalID.Call));
		table[NonTerminals.get(NonTerminalID.CallAssign2)][Tokens.get(TokenID.ID)] = aux;
		 
		//CallAssign1(ponto) -> ponto CallAssign2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.PONTO), new SymbolID(NonTerminalID.CallAssign2));
		table[NonTerminals.get(NonTerminalID.CallAssign1)][Tokens.get(TokenID.PONTO)] = aux;
		 
		//Call(lpar) -> lpar ArgumentList rpar
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPAR), new SymbolID(NonTerminalID.ArgumentList), new SymbolID(TokenID.RPAR));
		table[NonTerminals.get(NonTerminalID.Call)][Tokens.get(TokenID.LPAR)] = aux;
		 
		//Assign(lparR) ->Assign1 Assign2
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(NonTerminalID.Assign1), new SymbolID(NonTerminalID.Assign2));
		table[NonTerminals.get(NonTerminalID.Assign)][Tokens.get(TokenID.LPARR)] = aux;

		//Assign1(lparR) -> lparR Index rparR
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.LPARR), new SymbolID(NonTerminalID.Index), new SymbolID(TokenID.RPARR));
		table[NonTerminals.get(NonTerminalID.Assign1)][Tokens.get(TokenID.LPARR)] = aux;
		 
		//Assign2(assign) -> assign RHS 
		aux = new ArrayList<SymbolID>();
		Collections.addAll(aux, new SymbolID(TokenID.ASSIGN), new SymbolID(NonTerminalID.RHS));
		table[NonTerminals.get(NonTerminalID.Assign2)][Tokens.get(TokenID.ASSIGN)] = aux;
		
		
	}


	private void inicializeEnumMaps() {
		// TODO Auto-generated method stub
		NonTerminals.put(NonTerminalID.MODULE, 0);
		NonTerminals.put(NonTerminalID.DEC, 1);
		NonTerminals.put(NonTerminalID.FUNC, 2);
		NonTerminals.put(NonTerminalID.DECLARATION, 3);
		NonTerminals.put(NonTerminalID.ArrayOrScalarElement, 4);
		NonTerminals.put(NonTerminalID.ArrOrScal, 5);
		NonTerminals.put(NonTerminalID.DeclarationEnd, 6);
		NonTerminals.put(NonTerminalID.DecAssign, 7);
		NonTerminals.put(NonTerminalID.AddSubbOP, 8);
		NonTerminals.put(NonTerminalID.Function, 9);
		NonTerminals.put(NonTerminalID.Argument, 10);
		NonTerminals.put(NonTerminalID.FunctionBegin, 12);
		NonTerminals.put(NonTerminalID.FunctionID, 13);
		NonTerminals.put(NonTerminalID.FunctionAssign, 14);
		NonTerminals.put(NonTerminalID.VarFunc, 15);
		NonTerminals.put(NonTerminalID.VarList, 16);
		NonTerminals.put(NonTerminalID.VarListEnd, 17);
		NonTerminals.put(NonTerminalID.Stmtlst, 18);
		NonTerminals.put(NonTerminalID.Stmt, 19);
		NonTerminals.put(NonTerminalID.WHILE, 20);
		NonTerminals.put(NonTerminalID.IF, 21);
		NonTerminals.put(NonTerminalID.ELSE, 22);
		NonTerminals.put(NonTerminalID.EXPRtest, 23);
		NonTerminals.put(NonTerminalID.LHS, 24);
		NonTerminals.put(NonTerminalID.Access, 25);
		NonTerminals.put(NonTerminalID.Index, 26);
		NonTerminals.put(NonTerminalID.RHS, 27);
		NonTerminals.put(NonTerminalID.RHS1, 28);
		NonTerminals.put(NonTerminalID.ArithTerm, 29);
		NonTerminals.put(NonTerminalID.OP, 30);
		NonTerminals.put(NonTerminalID.Term, 31);
		NonTerminals.put(NonTerminalID.Term1, 32);
		NonTerminals.put(NonTerminalID.Term2, 33);
		NonTerminals.put(NonTerminalID.Term3, 34);
		NonTerminals.put(NonTerminalID.CallOrScalarAccess, 35);
		NonTerminals.put(NonTerminalID.ArraySize, 36);
		NonTerminals.put(NonTerminalID.CallAssign, 37);
		NonTerminals.put(NonTerminalID.CallAssignAux, 38);
		NonTerminals.put(NonTerminalID.CallAssign1, 39);
		NonTerminals.put(NonTerminalID.CallAssign2, 40);
		NonTerminals.put(NonTerminalID.Call, 41);
		NonTerminals.put(NonTerminalID.Assign, 42);
		NonTerminals.put(NonTerminalID.Assign1, 43);
		NonTerminals.put(NonTerminalID.Assign2, 44);
		NonTerminals.put(NonTerminalID.ArgumentList, 45);
		NonTerminals.put(NonTerminalID.Argument2, 46);
		NonTerminals.put(NonTerminalID.ScalarAccess, 47);
		NonTerminals.put(NonTerminalID.ScalarAccess1, 48);
		
		Tokens.put(TokenID.END, 0);
		Tokens.put(TokenID.ADDSUB_OP, 1);
		Tokens.put(TokenID.RELA_OP, 2);
		Tokens.put(TokenID.ARITH_OP, 3);
		Tokens.put(TokenID.BITWISE_OP, 4);
		Tokens.put(TokenID.NOT_OP, 5);
		Tokens.put(TokenID.WHILE, 6);
		Tokens.put(TokenID.IF, 7);
		Tokens.put(TokenID.ELSE, 8);
		Tokens.put(TokenID.ASSIGN, 9);
		Tokens.put(TokenID.ASPA, 10);
		Tokens.put(TokenID.LPAR, 11);
		Tokens.put(TokenID.RPAR, 12);
		Tokens.put(TokenID.VIRG, 13);
		Tokens.put(TokenID.PVIRG, 14);
		Tokens.put(TokenID.LCHAVETA, 15);
		Tokens.put(TokenID.RCHAVETA, 16);
		Tokens.put(TokenID.FUNCTION, 17);
		Tokens.put(TokenID.MODULE, 18);
		Tokens.put(TokenID.SIZE, 19);
		Tokens.put(TokenID.ID, 20);
		Tokens.put(TokenID.STRING, 21);
		Tokens.put(TokenID.INTEGER, 22);
		Tokens.put(TokenID.PONTO, 23);
		Tokens.put(TokenID.LPARR, 24);
		Tokens.put(TokenID.RPARR, 25);
		Tokens.put(TokenID.ERROR, 26);
		Tokens.put(TokenID.EPSILON, 27);
		
	}
	
	
	
	
	
	
	
	
	
}
