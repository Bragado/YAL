package AnalisadorSemantico;

import java.time.format.TextStyle;
import java.util.ArrayList;

import Estruturas.NonTerminal;
import Estruturas.NonTerminalID;
import Estruturas.Symbol;
import Estruturas.TSymbol;
import Estruturas.Table;
import Estruturas.Token;
import Estruturas.TokenID;
import Estruturas.TreeNode;


public class SemanticAnalyses {
	
	private TreeNode<Symbol> AST;
	private Table SymbolTable; 
	
	
	public static  ArrayList<String> erros = new ArrayList<String>();
	
	
	
	public SemanticAnalyses(TreeNode<Symbol> AST) {
		this.AST = AST;
		this.SymbolTable = new Table();
		
	}
	
	
	
	public ArrayList<String> run() {
		
		semantic_check(AST, SymbolTable);
		
		
		return erros;
	}



	private void semantic_check(TreeNode<Symbol> AST, Table symbolTable) {
		// TODO Auto-generated method stub
			
		Symbol ID = AST.children.get(0).data;
			TreeNode<Symbol> DEC = AST.children.get(1);
			TreeNode<Symbol> FUNC = AST.children.get(2);
			
			TSymbol module_name = new TSymbol(ID, TSymbol.TYPE.ModuleName);
			
			this.SymbolTable.addEntrie(module_name);
			
			// Declarations
			Table declarations = this.SymbolTable.addChild(new Table(this.SymbolTable, Table.TYPE.DECLARATIONS));
			for(int i = 0; i < DEC.children.size(); i++) {
				dec_check(DEC.children.get(i), declarations);
			}
			
			// Functions
			Table Functions = this.SymbolTable.addChild(new Table(this.SymbolTable, Table.TYPE.FUNCS));
			
			for(int i = 0; i < FUNC.children.size(); i++) {
			
				Table Function = Functions.addChild(new Table(Functions, Table.TYPE.FUNCTION));
				function_check(FUNC.children.get(i), Function);
			}		
		
	}



	private void function_check(TreeNode<Symbol> Function, Table function) {
		
		TreeNode<Symbol> FunctionAssign = Function.children.get(0);
		TreeNode<Symbol> FunctionID = Function.children.get(1);
		TreeNode<Symbol> Varlist = Function.children.get(2);
		TreeNode<Symbol> Stmtlst = Function.children.get(3);
			
		// 1st: check if the function name already exists, if not continue
		if(function.function_exists(FunctionID.data)) {
			System.out.println("function already exists");
			return;
		}
		
		// 2nd: check if the return variable is different than the parameters
		if(!checkFunctionParamsAndReturn(FunctionAssign, Varlist)) {
			System.out.println("return variable name is the same as the name of one parameter");						
		}
		// 3rd: add the function name and return
		function.addEntrie(new TSymbol(FunctionID.data, TSymbol.TYPE.FunctionName));
		
		// 4th: add the return value
		if(FunctionAssign.children.size() > 0)
			this.addFunctionReturn(function, FunctionAssign);
		
		// 5th: add function parameters
		if(Varlist.children.size() > 0)
			this.addFunctionParameters(function, Varlist);
		
		
		// 6th: do the following for each stmt
		for(int i = 0; i< Stmtlst.children.size(); i++){
			stmt_check(function, Stmtlst.children.get(i));
		}
			
		
		
	}



	



	private void stmt_check(Table function, TreeNode<Symbol> treeNode) {
		// TODO Auto-generated method stub
		TreeNode<Symbol> type = treeNode.children.get(0);
		NonTerminal ttype = (NonTerminal)type.data;
		 
		
		switch((NonTerminalID)ttype.getID()){
		case WHILE:
			check_while(function, type);
			break;
		case IF:
			check_if(function, type);
			break;
		case Assign:
			check_assign(function, type);
			break;
		case Call:
			check_call(function, type);
			break;
		}
	}



	private void check_if(Table function, TreeNode<Symbol> type) {
		// TODO Auto-generated method stub
		TreeNode<Symbol> Exprtest = type.children.get(0);
		TreeNode<Symbol> Stmtlst = type.children.get(1);
		TreeNode<Symbol> ELSE = type.children.get(2);
		
		TreeNode<Symbol> LHS = Exprtest.children.get(0);
		TreeNode<Symbol> RHS = Exprtest.children.get(2);
		

		check_if_becomes_integer(function, LHS.children.get(0));
		check_if_becomes_integer(function, RHS.children.get(0));
		
		if(RHS.children.size() == 3)
			check_if_becomes_integer(function, RHS.children.get(2));
		
		
		Table ifTable = new Table(function, Table.TYPE.WHILE);
		
		for(int i = 0; i < Stmtlst.children.size(); i++){
			stmt_check(ifTable, Stmtlst.children.get(i));
		}
		
		Stmtlst = ELSE.children.get(0);
		Table elseTable = new Table(function, Table.TYPE.WHILE);
		for(int i = 0; i < Stmtlst.children.size(); i++){
			stmt_check(elseTable, Stmtlst.children.get(i));
		}
		
	}



	private void check_while(Table function, TreeNode<Symbol> type) {
		// TODO Auto-generated method stub
		TreeNode<Symbol> Exprtest = type.children.get(0);
		TreeNode<Symbol> Stmtlst = type.children.get(1);
		
		TreeNode<Symbol> LHS = Exprtest.children.get(0);
		TreeNode<Symbol> RHS = Exprtest.children.get(2);
		
		check_if_becomes_integer(function, LHS.children.get(0));
		check_if_becomes_integer(function, RHS.children.get(0));
		
		if(RHS.children.size() == 3)
			check_if_becomes_integer(function, RHS.children.get(2));
		Table whileTable = new Table(function, Table.TYPE.WHILE);
		
		for(int i = 0; i < Stmtlst.children.size(); i++){
			stmt_check(whileTable, Stmtlst.children.get(i));
		}
		
	}



	private boolean check_call(Table function, TreeNode<Symbol> call) {
		// TODO Auto-generated method stub
		boolean ret = true;
		
		Symbol ID = call.children.get(0).data;
		ArrayList<Symbol> params = new ArrayList<Symbol> ();
		
		TreeNode<Symbol> ArgumentList = null;
		
		
		switch(call.children.size()) {
		case 2:					// Call -> id ArgumentList
			ArgumentList = call.children.get(1);
			break;
		case 4:					// Call -> id ponto id Argumentlist
			ArgumentList = call.children.get(3);
			break;
		}
		
		
		if(ArgumentList != null && ArgumentList.children.size() > 0) {
			
			for(int i = 0; i < ArgumentList.children.size(); i++) {
				params.add(ArgumentList.children.get(i).children.get(0).data);
				
			
			}
		
		}
		
		switch(call.children.size()) {
		case 2:					// Call -> id ArgumentList, the function must exist
			if(!function.function_exists(ID, params)) {
				if(function.function_exists(ID)) {		// checks if the parameters are wrong
					System.out.println("Line: " + ((Token)ID).line() + " | Column: " + ((Token)ID).getColumn() +  " ; The function '" + ((Token)ID).getDate() + "' called does not have the parameters passed" );
					ret = false;
				}else{									// checks if the function does not exists at all
					System.out.println("Line: " + ((Token)ID).line() + " | Column: " + ((Token)ID).getColumn() + " ; The function " + ((Token)ID).getDate() + " does not exists");
					ret = false;
				}
			}
				
			break;
		case 4:					// Call -> id ponto id Argumentlist, 
			
			if(((Token)ID).getDate() == "io") {
				/*
				String ponto = ((Token)call.children.get(1).data).getDate();
				String print = ((Token)call.children.get(2).data).getDate();	
				
				
				switch(params.size()) {
				case 1:
					Symbol string = (Token)params.get(0);
					if(string.getID() != TokenID.STRING)
						System.out.println("first parameter of io methods must be a string");
					
					
				case 2:
					Symbol val = (Token)params.get(0);
					if(val.getID() != TokenID.INTEGER)
						System.out.println("second parameter of io methods must be a integer");
					
					
					
					break;
				}
				*/
				
			}
			for(int i = 0; i < params.size(); i++) {
				Token param = (Token)params.get(i);
				if(param.getID() == TokenID.ID) {
					TSymbol var = function.search_variable(param);
					if(var == null) {
						System.out.println("Line: " + param.line() + " | Column: " + param.getColumn() +  " ; Variable '" + param.getDate() + "' was not declared");
						ret = false;
					}else if(!var.inicialized){
						System.out.println("Line: " + param.line() + " | Column: " + param.getColumn() + " ; Variable '" + param.getDate() + "' was not inicialized");
						ret = false;
					}
				}
				
			}
			
			
			
			break;
		}
		
		
		return ret;
		
	}
	
	private void check_assign(Table function, TreeNode<Symbol> assign) {
		TreeNode<Symbol> LHS = assign.children.get(0);
		TreeNode<Symbol> RHS = assign.children.get(2);
		
		TSymbol.TYPE varType;
		TSymbol var = null;
		Symbol varAux = null;
		
		switch((NonTerminalID)LHS.children.get(0).data.getID()){
		case ArrayAccess: // a[i] = ... a[N] = ... 
			// 1st: verify if variable name exists and if is type array otherwise create a new variable 
			TreeNode<Symbol> arrayAccess = LHS.children.get(0);
			TSymbol index;
			var = function.search_variable(arrayAccess.children.get(0).data);
			Token data = (Token)arrayAccess.children.get(0).data;
			if(var == null) {
				
				System.out.println("Line: " + data.line() + " | Column: " + data.getColumn() + " ; You cannot access an array that was not declared");
				return;
			}
			
			if(var != null) {
				
				if(var.type != TSymbol.TYPE.ARRAY || var.inicialized != true) {
					System.out.println("Line: " + data.line() + " | Column: " + data.getColumn() + " : You are trying to access an array that was no inicialized");
					return;
				}
			}
			
			// 2nd: if the array index is an ID verify if that variable exists, if it's inciliazed and if it's an integer
			if(arrayAccess.children.get(2).data.getID() == TokenID.ID) {
					index = function.search_variable(arrayAccess.children.get(2).data);
					
					if(index == null || index.inicialized == false || index.type != TSymbol.TYPE.INT) {
						Token err = (Token)index.data;
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : You are trying to use a variable as index that was not declared");
						return;
					}
			}
			break;
		case ScalarAccess: // a = ... 	a.size = ...
			// 1st: verify if the variable exists and if is type integer otherwise create a new variable
			TreeNode<Symbol> ScalarAccess = LHS.children.get(0);
			var = function.search_variable(ScalarAccess.children.get(0).data);
			switch(ScalarAccess.children.size()) {
				case 1:
					varAux = ScalarAccess.children.get(0).data;
					break;
				case 3:
					Token err = (Token)ScalarAccess.children.get(0).data;
					System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : What could you possible do by assigning something to an array size?");
					return;
					
			}
			
			
			// 2nd: what's the meaning of assign something to array.size ??
			
			break;
		}
		
		
		switch(RHS.children.size()){
		case 1:			// Term					// ... = 1 ; ... = - 1 ;  ... = B ; ... = -B ;  ... = b.size ; ... = b[2] ; ... = f(N, Z) ;  
			TreeNode<Symbol> Term = RHS.children.get(0);
			TreeNode<Symbol> assignType = null;
			
			switch(Term.children.size()){
			case 1:		// integer | Call | ArrayAccess | ScalarAccess
				assignType = Term.children.get(0);
				break;
			case 2:		// (AddSub_op) (integer | Call | ArrayAccess | ScalarAccess)
				assignType = Term.children.get(1);
				break;
			}
			
			if(assignType.data.getID() == TokenID.INTEGER){			//  check if the LHS is an integer			// Check
				
						if(var != null && varAux == null) {			// a[n] = 1
							
						}else if(var != null && varAux != null) {	// b = 2 and b does exist
							// 1st verify if b is type integer
							if(var.type != TSymbol.TYPE.INT) {
								Token err = (Token)var.data;
								System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + "You are trying to assign an integer to a variable that is not an integer");
								return;
							}
							
							// 2nd inicialize variable
							if(function.type != Table.TYPE.WHILE)
								var.inicialized = true;
							else
								function.addLocal(new TSymbol(var.data, var.type, true));
						}else if(var == null && varAux != null) {	// c = 2 and c does not exist
							// create a new entry inicialized 
							function.addLocal(new TSymbol(varAux, TSymbol.TYPE.INT, true));
						}else {
							System.out.println("An error occured, please report the bug to the owners of this project :)");
						}
				
				
			}else if(assignType.data.getID() == NonTerminalID.Call){		// check if the function is valid and if the return value matches LHS type
				
				// 1st check if the function is correct		
				boolean func = check_call(function, assignType);
				if(!func && assignType.children.size() == 2) {
					Token err = (Token)assignType.children.get(0).data;
					System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : Function called is not correct");
					return;
				}
				
				
				TSymbol.TYPE functionReturnType = function.getFunctionRetType(assignType.children.get(0).data);
								
				if(var != null && varAux == null) {			 
					if(assignType.children.size() == 4) {				// a[b] = library.f(2);
						return;					// true
					}
					
					if(functionReturnType != null &&  functionReturnType != TSymbol.TYPE.INT) {		// a[n] = f(2);   <-- must return an integer
						Token err = (Token)assignType.children.get(0).data;
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : The function must return an integer");
					}
					
					
				}else if(var != null && varAux != null) {	// b = f(2) <-- f must return the same type as b
					if(assignType.children.size() == 4) {				// a = library.f(2);
						// 2nd inicialize variable
						if(function.type != Table.TYPE.WHILE)
							var.inicialized = true;
						else
							function.addLocal(new TSymbol(var.data, var.type, true));
						return;					// true
					}					
					// 1st verify if b is type integer
					
					if(functionReturnType != null &&  functionReturnType != var.type){
						Token err = (Token)assignType.children.get(0).data;
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : The function must return an integer");
					}
					
					
					// 2nd inicialize variable
					if(function.type != Table.TYPE.WHILE)
						var.inicialized = true;
					else
						function.addLocal(new TSymbol(var.data, var.type, true));
				}else if(var == null && varAux != null) {	// c = f(2) and c does not exist <-- c is inicialized
					// create a new entry inicialized 
					function.addLocal(new TSymbol(varAux, functionReturnType, true));
				}else {
					System.out.println("An error occured, please report the bug to the owners of this project :)");
				}
				
				
				
				
			}else if(assignType.data.getID() == NonTerminalID.ArrayAccess){
				
				Symbol id = assignType.children.get(0).data;
				TreeNode<Symbol> Index = assignType.children.get(2);
				Symbol  index = Index.children.get(0).data;
				Token err = (Token)id;
				// verifies if the array b in ( a = b[N]) is inicialized and exists
				TSymbol v = function.search_variable(id);
				if(v == null || v.type != TSymbol.TYPE.ARRAY || v.inicialized == false) {
					
					
					System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  " : The assigning variable must exist, and must be an array inicialized");
					return;
				}
				
				// verifies if the variable ii in ( a = b[ii]) is inicialized and exists
				if(index.getID() == TokenID.ID) {
					v = function.search_variable(id);
					if(v == null || v.type != TSymbol.TYPE.INT || v.inicialized == false) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  " : Index must exist and must be an integer inicialized");
						return;
					}
				}
				
				
				
				if(var != null && varAux == null) {			// a[n] = b[N]
					
				}else if(var != null && varAux != null) {	// b = a[N] and b does exist
					// 1st verify if b is type integer
					if(var.type != TSymbol.TYPE.INT) {
						
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +   " : You are trying to assign an integer to a variable that is not an integer");
						return;
					}
					
					// 2nd inicialize variable
					if(function.type != Table.TYPE.WHILE)
						var.inicialized = true;
					else
						function.addLocal(new TSymbol(var.data, var.type, true));
				}else if(var == null && varAux != null) {	// c = a[N] and c does not exist
					// create a new entry inicialized 
					function.addLocal(new TSymbol(varAux, TSymbol.TYPE.INT, true));
				}else {
					System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +   " : An error occured, please report the bug to the owners of this project :)");
				}
				
				
				
				
			}else if(assignType.data.getID() == NonTerminalID.ScalarAccess) {			// a = b ; a = b.size ; a[N] = b ; a[N] = b.size 
				
				Symbol id = assignType.children.get(0).data;
				TSymbol v = function.search_variable(id);
				Token err = (Token)id;
				if(v == null || v.inicialized == false) {
					System.out.print("Line: " + err.line() + " | Column: " + err.getColumn() +   "Variable to be assign must exist and must be inicialized");
				}
				
				if(var != null && varAux == null) {			// a[n] = b ; a[n] = b.size 
					
					if(assignType.children.size() == 1 && v.type != TSymbol.TYPE.INT) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  "Variable to be assigned mus be an integer");
						return;
					}else if(assignType.children.size() == 3 && v.type != TSymbol.TYPE.ARRAY) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  "Variable to be assigned mus be an array");
						return;
					}
					
					
				}else if(var != null && varAux != null) {	// b = a ; b = a.size   and b does exist
					// 1st verify if b is type integer
					if(assignType.children.size() == 1 && (var.type != TSymbol.TYPE.INT || v.type != TSymbol.TYPE.INT)) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  " : You are trying to assign an integer to a variable that is not an integer");
						return;
					}
					if(assignType.children.size() == 3 && (var.type != TSymbol.TYPE.INT || v.type != TSymbol.TYPE.ARRAY)) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +   " : You are trying to assign a size of an integer to a variable that may not be an integer");
						return;
					}
					
					// 2nd inicialize variable
					if(function.type != Table.TYPE.WHILE)
						var.inicialized = true;
					else
						function.addLocal(new TSymbol(var.data, var.type, true));
				}else if(var == null && varAux != null) {	// c = a ; c = b.size and c does not exist
					if(assignType.children.size() == 1 && v.type != TSymbol.TYPE.INT) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  " : Variable to be assigned mus be an integer");
						return;
					}else if(assignType.children.size() == 3 && v.type != TSymbol.TYPE.ARRAY) {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  " : Variable to be assigned mus be an array");
						return;
					}					
					
					// create a new entry inicialized 
					function.addLocal(new TSymbol(varAux, TSymbol.TYPE.INT, true));
				}else {
					System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  " : An error occured, please report the bug to the owners of this project :)");
				}
				
				
				
			}else {
				System.out.println("Unknown error");
			}
			
			
			
			
			break;
		case 3:				// Term OP Term ; LPARR ArraySize RPARR
			if(RHS.children.get(0).data.getID() == TokenID.LPARR){		// a = [100] ; a = [N]
				TreeNode<Symbol> ArraySize = RHS.children.get(1);
				Token err = (Token)ArraySize.children.get(0).data;
				if(ArraySize.children.get(0).data.getID() == TokenID.INTEGER){
					function.addLocal(new TSymbol(varAux, TSymbol.TYPE.ARRAY, true));
					return;
				}else{
					TSymbol v = function.search_variable(ArraySize.children.get(0).data);
					
					if(v != null && v.type == TSymbol.TYPE.INT && v.inicialized == true)
						function.addLocal(new TSymbol(varAux, TSymbol.TYPE.ARRAY, true));
					else {
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() +  "You cannot inicialize an array with a variable that does not exist or it's properly inicialized");
					}
				}
				
				
			}else {
				TreeNode<Symbol> Term1 = RHS.children.get(0);
				TreeNode<Symbol> Term2 = RHS.children.get(2);
				
				boolean term1 = check_if_becomes_integer(function, Term1);
				boolean term2 = check_if_becomes_integer(function, Term2);
				
				if(!term1 || !term2)
					return;

				if(var != null && varAux == null) {			// a[n] = 1
					
				}else if(var != null && varAux != null) {	// b = 2 and b does exist
					// 1st verify if b is type integer
					if(var.type != TSymbol.TYPE.INT) {
						Token err = (Token)var.data;
						System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : You are trying to assign an integer to a variable that is not an integer");
						return;
					}
					
					// 2nd inicialize variable
					if(function.type != Table.TYPE.WHILE)
						var.inicialized = true;
					else
						function.addLocal(new TSymbol(var.data, var.type, true));
				}else if(var == null && varAux != null) {	// c = 2 and c does not exist
					// create a new entry inicialized 
					function.addLocal(new TSymbol(varAux, TSymbol.TYPE.INT, true));
				}else {
					System.out.println("An error occured, please report the bug to the owners of this project :)");
				}
				
				
				
				
			}
			
			
			
			
			
			break;
			
		}
		
		
		
		
	}
	



	private void addFunctionParameters(Table function, TreeNode<Symbol> varlist) {
		// TODO Auto-generated method stub
		for(int i = 0; i < varlist.children.size(); i++) {
			TreeNode<Symbol> ArrayOrScalarElement = varlist.children.get(i);
			Symbol ID = ArrayOrScalarElement.children.get(0).data;
			switch(ArrayOrScalarElement.children.size()) {
			case 1:
				function.addParam(new TSymbol(ID, TSymbol.TYPE.INT, true));
				break;
			case 3:
				function.addParam(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
				break;
			}
			
		}
	}



	private boolean checkFunctionParamsAndReturn(TreeNode<Symbol> functionAssign, TreeNode<Symbol> varlist) {
		// TODO Auto-generated method stub
		if(functionAssign.children.size() == 0)
			return true;
		if(varlist.children.size() == 0)
			return true;		
		
		for(int i = 0; i < varlist.children.size(); i++) {
			TreeNode<Symbol> ArrayOrScalarElement = varlist.children.get(i);
			
			Token ID = (Token)ArrayOrScalarElement.children.get(0).data;
			Token aux = (Token)functionAssign.children.get(0).data;
			
			if(ID.getDate().equals(aux.getDate()))
				return false;	
			
		}
		
		
		
		return true;
	}



	private void addFunctionReturn(Table function, TreeNode<Symbol> functionAssign) {
		// TODO Auto-generated method stub
		Symbol ID = functionAssign.children.get(0).data;
		
		switch(functionAssign.children.size()){
		case 1:			// FunctionAssign -> ID
			function.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, false));
			break;
		case 3:			// FunctionAssign -> ID LPARR RPARR
			function.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, false));
			break;
		}
		
		
	}
	
	
	private boolean check_if_becomes_integer(Table function, TreeNode<Symbol> Term) {
		TreeNode<Symbol> TermChild = null;
		switch(Term.children.size()){
		case 1:
			TermChild = Term.children.get(0);
			break;
		case 2:
			TermChild = Term.children.get(1);
			break;
		}
		
		if(TermChild.data.getID() == TokenID.INTEGER){
			return true;
		}
		
		if(TermChild.data.getID() == NonTerminalID.ArrayAccess) {
			Symbol Id = TermChild.children.get(0).data;
			TreeNode<Symbol> Index = TermChild.children.get(2);
			
			if(Index.children.get(0).data.getID() == TokenID.ID) {
				TSymbol index = function.search_variable(Index.children.get(0).data);
				
				if(index == null || !index.inicialized || index.type != TSymbol.TYPE.INT){
					System.out.println("Error at array Index");
					return false;
				}
				
			}
			
			TSymbol vv = function.search_variable(Id);
			
			if(vv != null && vv.type == TSymbol.TYPE.ARRAY && vv.inicialized)
				return true;
			
			System.out.println("Expected an array");
			return false;
			
		}
		
		if(TermChild.data.getID() == NonTerminalID.ScalarAccess) {
			switch(TermChild.children.size()){
			case 1:							// ... = id;
				TSymbol v = function.search_variable(TermChild.children.get(0).data);
				if(v != null && v.inicialized && v.type == TSymbol.TYPE.INT)
					return true;
				
				System.out.println("expected an integer inicialized");
				return false;
				
			case 3:							// ... = id.size
				TSymbol vv = function.search_variable(TermChild.children.get(0).data);
				if(vv != null && vv.inicialized && vv.type == TSymbol.TYPE.ARRAY)
					return true;
				System.out.println("expected an array inicialized");
				return false;
				
			
			}
			
			
		}
		
		if(TermChild.data.getID() == NonTerminalID.Call){
			if(TermChild.children.size() == 4)					// ... = library.max(...);
				return true;
			
			boolean func_exists = check_call(function, TermChild);
			TSymbol.TYPE functionReturnType = function.getFunctionRetType(TermChild.children.get(0).data);
			
			if(func_exists && functionReturnType != null &&  functionReturnType == TSymbol.TYPE.INT)
				return true;
			
			Token id =	(Token) TermChild.children.get(0).data;
			
			if(!func_exists) {
				System.out.println("line: " + id.line() + " | column: " + id.getColumn() + " ; Function does not exists");
			}else{
				System.out.println("line: " + id.line() + " | column: " + id.getColumn() + " ; Function call was expecting integer as return");
			}
			
			return false;
		
		}
		
		
		
		
		return true;
	}



	private void dec_check(TreeNode<Symbol> Declaration, Table declarations) {
		// a[] = [10] <==> a = [10] 	-> a is array, a is inicialized
		// a[] = 1    <==> a = 1    	-> a is integer, a is inicialized
		// N = 100 & b[] = [N] 			-> N is integer, N is inicialized; b is integer and is inicialized if N exists
		// b[] = 1						-> if b exists, b is now inicialized			
		// b[0] = 100					-> check if b exists
		// ms; || mx[] 					-> check if the variable already exists! 
		if(Declaration.children.size() == 2) {		// ex: a = (..)
			
			Symbol ID = Declaration.children.get(0).data;
			TreeNode<Symbol> DeclarationEnd = Declaration.children.get(1);
			
			if(DeclarationEnd.children.size() == 0) {	// ex: mx;	
				if(declarations.variable_exists(ID)) {
					System.out.println("error5");
					return;
				}
				
				declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, false));
			
			}		
			else {									// ex: mx = (..)
				
				int size = DeclarationEnd.children.size();
				
				
				switch(size){						  
					case 2:			// ex: mx = 1
					case 3:			// ex: mx = -1
						if(declarations.variable_exists(ID, TSymbol.TYPE.ARRAY))								
							System.out.println("error6");
						else
							declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, true));
						break;
					case 4:			// ex: a = [100] 
						TreeNode<Symbol> ArraySize = DeclarationEnd.children.get(2);
						
						switch(ArraySize.children.size()){
							case 1:						
								if(ArraySize.children.get(0).data.getID() == TokenID.INTEGER) {				// ex: a = [100]
									if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
										System.out.println("error6");
									else
										declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));		
									return;
								}
								
								Symbol ArraySize_ID = ArraySize.children.get(0).data;
								if(declarations.variable_exists(ArraySize_ID, TSymbol.TYPE.INT, true)) {										// ex: a = [N]	-> N must be declare and must be an integer	
									if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
										System.out.println("error6");
									else
										declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
									
								}else{
									System.out.println("error1");
								}
								
									
								break;
							case 3:
								Symbol ArraySize_IDD = ArraySize.children.get(0).data;
								if(declarations.variable_exists(ArraySize_IDD, TSymbol.TYPE.ARRAY, true)) {					// ex: a = [b.size] -> b must be declare and must be an array
									if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
										System.out.println("error6");
									else
										declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));	
								}else{
									System.out.println("error2");
								}
								
								break;
						}
						
						
						break;
				}
				
			}
			
				
			
			
		}else if(Declaration.children.size() == 4){  // ex: a[] = (..)
			
			TreeNode<Symbol> DeclarationEnd = Declaration.children.get(3);
			Symbol ID = Declaration.children.get(0).data;
			
			switch(DeclarationEnd.children.size()){
				case 0:			// ex: a[]
					if(declarations.variable_exists(ID)) {
						System.out.println("error4");
						return;
					}
						
					
					declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, false));
					break;
				case 2:			// ex: a[] = 1  <=> a = 1
					if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
						System.out.println("error6");
					declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, true));
					break;
				case 3:			// ex: a[] = -1  <=> a = -1
					if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
						System.out.println("error6");
					else
						declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, true));
					break;
				case 4:			// ex: a[] = [ArraySize]
					TreeNode<Symbol> ArraySize = DeclarationEnd.children.get(2);
					
					switch(ArraySize.children.size()) {
						case 1:			// integer | id
							if(ArraySize.children.get(0).data.getID() == TokenID.INTEGER) {				// ex: a[] = [100]
								if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
									System.out.println("error6");
								else
									declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
								return;
							}
							Symbol ArraySize_ID = ArraySize.children.get(0).data;
							if(declarations.variable_exists(ArraySize_ID, TSymbol.TYPE.INT, true)) {					// ex: a[] = [N]	-> N must be declare and must be an integer	
								if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
									System.out.println("error5");
								else
									declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
								
									
							}else{
								System.out.println("error3");
							}
							
							
							break;
						case 3:			// id.size
							Symbol ArraySize_IDD = ArraySize.children.get(0).data;
							if(declarations.variable_exists(ArraySize_IDD, TSymbol.TYPE.ARRAY, true)) {					// ex: a[] = [b.size] -> b must be declare and must be an array
								if(declarations.variable_exists(ID, TSymbol.TYPE.INT))								
									System.out.println("error5");
								else
									declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));	
							}else{
								System.out.println("error2");
							}
							
							
							break;
						
					
					}
					
					
					
					break;
			
			}
			
			
			
		}
		
		
		
	}
	
	
	
}
