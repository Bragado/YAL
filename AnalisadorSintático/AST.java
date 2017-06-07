package AnalisadorSintático;

import java.util.ArrayList;

import Estruturas.NonTerminal;
import Estruturas.NonTerminalID;
import Estruturas.Symbol;
import Estruturas.TokenID;
import Estruturas.TreeNode;

public class AST {
	
	private TreeNode<Symbol> CST;
	public TreeNode<Symbol> AST;
	
	
	public AST(TreeNode<Symbol> CST) {
		
		this.CST = CST;
		
		
		
	}
	
	// Module -> id Dec Func
	public TreeNode<Symbol> run() {				// WRONG
		TreeNode<Symbol> AST = new TreeNode<Symbol>(CST.data);
		AST.addChild(CST.children.get(1).data);						// id
		for(int i = 0; i < CST.children.size(); i++) {				// Dec
			if(CST.children.get(i).data.getID() == NonTerminalID.DEC) {
				Dec(CST.children.get(i), AST.addChild(CST.children.get(i).data)); 
			}else if(CST.children.get(i).data.getID() == NonTerminalID.FUNC) 
				Func(CST.children.get(i), AST.addChild(CST.children.get(i).data));
		}
		
		
		
		
		return AST;
	}
	
	private void Func(TreeNode<Symbol> CSTFunc, TreeNode<Symbol> ASTFunc) {
		// TODO Auto-generated method stub
		for(int i = 0; i < CSTFunc.children.size(); i++) {
			if(CSTFunc.children.get(i).data.getID() == NonTerminalID.Function)
				FUNCTION(CSTFunc.children.get(i), ASTFunc.addChild(CSTFunc.children.get(i).data));
			else
				Func(CSTFunc.children.get(i), ASTFunc);
		}
	}
	
	private void FUNCTION(TreeNode<Symbol> CSTFunction, TreeNode<Symbol> ASTFunction ) {
		TreeNode<Symbol> FunctionBegin = CSTFunction.children.get(0);
		TreeNode<Symbol> Stmtlst = CSTFunction.children.get(2);
		
		/*
		 * 		Function -> FunctionAssign id Varlist StmtLst
		 * 		
		 */
		
		
		// FunctionAssign -> id (lparR rparR)?
		TreeNode<Symbol> FunctionId = FunctionBegin.children.get(1);
		TreeNode<Symbol> VarFunc = FunctionBegin.children.get(3);
		
		TreeNode<Symbol> FunctionAssign = FunctionId.children.get(1); 
		
		// FunctionAssign -> EPSILON (there is no return)
		if(FunctionAssign.children.get(0).data.getID() == TokenID.EPSILON) {
			ASTFunction.addChild(FunctionAssign.data);
			ASTFunction.addChild(FunctionId.children.get(0).data);
		}else {
			// FunctionAssign -> id [ ]
			if(FunctionAssign.children.size() == 4) {
				TreeNode<Symbol> ASTFunctionAssign = ASTFunction.addChild(FunctionAssign.data);
				ASTFunctionAssign.addChild(FunctionId.children.get(0).data);		// array name			
				ASTFunctionAssign.addChild(FunctionAssign.children.get(1).data);	// [
				ASTFunctionAssign.addChild(FunctionAssign.children.get(2).data);	// ]
				ASTFunction.addChild(FunctionAssign.children.get(3).data);			// function id
				
			}else {	// FunctionAssign -> id
				TreeNode<Symbol> ASTFunctionAssign = ASTFunction.addChild(FunctionAssign.data);
				ASTFunctionAssign.addChild(FunctionId.children.get(0).data);
				ASTFunction.addChild(FunctionAssign.children.get(1).data);
			}
		}
		
		
		if(VarFunc.children.get(0).data.getID() == TokenID.EPSILON && ASTFunction.children.size() == 2) {
			ASTFunction.addChild(new NonTerminal(NonTerminalID.VarList));
			
		}else {
		
			// VarList -> (ArrayOrScalarElement)*
			TreeNode<Symbol> VarList = VarFunc.children.get(0);
			if(VarList.children.size() == 0 || VarList.children.get(0).data.getID() == TokenID.EPSILON)
				ASTFunction.addChild(VarList.data);
			else		
				VARLIST(VarList, ASTFunction.addChild(VarList.data));
			
		}
		/*
		 * 			STMTLst:
		 */
		
		STMTLST(CSTFunction.children.get(2), ASTFunction.addChild(CSTFunction.children.get(2).data));
		
	}
	

	private void STMTLST(TreeNode<Symbol> CSTStmtLst, TreeNode<Symbol> ASTStmtLst) {
		// TODO Auto-generated method stub
		if(CSTStmtLst.children.get(0).data.getID() == TokenID.EPSILON)
			return;
		
		TreeNode<Symbol> CSTStmt = CSTStmtLst.children.get(0);
		TreeNode<Symbol> ASTStmt = ASTStmtLst.addChild(CSTStmt.data);
		
		STMT(CSTStmt, ASTStmt);
		
		
		STMTLST(CSTStmtLst.children.get(1), ASTStmtLst);
	}
	
	private void STMT(TreeNode<Symbol> CSTStmt, TreeNode<Symbol> ASTStmt) {
		
		
		//CST: WHILE -> while EXPRtest lchav Stmtlst rchav			
		//AST: WHILE -> EXPRtest Stmtlst
		if(CSTStmt.children.get(0).data.getID() == NonTerminalID.WHILE) {
			TreeNode<Symbol> CSTWHILE = CSTStmt.children.get(0); 
			
			TreeNode<Symbol> ASTWHILE = ASTStmt.addChild(CSTWHILE.data);
			
			TreeNode<Symbol> EXPRtest = CSTWHILE.children.get(1);			
			TreeNode<Symbol> Stmtlst = CSTWHILE.children.get(3);
			
			EXPRTest(EXPRtest, ASTWHILE.addChild(EXPRtest.data));
			
			STMTLST(Stmtlst, ASTWHILE.addChild(Stmtlst.data));
			
			
		}else if(CSTStmt.children.get(0).data.getID() == NonTerminalID.IF){
		//CST:	IF -> if EXPRtest lchav Stmtlst rchav ELSE
		//AST:  ID -> EXPRtest Stmtlst ELSE	
			
			TreeNode<Symbol> CSTIF = CSTStmt.children.get(0); 
			TreeNode<Symbol> ASTIF = ASTStmt.addChild(CSTIF.data);
			
			TreeNode<Symbol> EXPRtest = CSTIF.children.get(1);			
			TreeNode<Symbol> Stmtlst = CSTIF.children.get(3);
			
			EXPRTest(EXPRtest, ASTIF.addChild(EXPRtest.data));
			
			STMTLST(Stmtlst, ASTIF.addChild(Stmtlst.data));
			
			TreeNode<Symbol> ELSE = CSTIF.children.get(5);
			
			//ELSE -> '' | else lchav Stmtlst rchav
			if(ELSE.children.get(0).data.getID() == TokenID.EPSILON)
				return;
			
			TreeNode<Symbol> ASTELSE = ASTIF.addChild(ELSE.data); 
			Stmtlst = ELSE.children.get(2);
			STMTLST(Stmtlst, ASTELSE.addChild(Stmtlst.data));
			
			
			
		}else if(CSTStmt.children.get(0).data.getID() == NonTerminalID.CallAssign) {
			// We now must figure out if it's Call or Assign :(
			/*	CST: 
			 * 		STMT -> CallAssign | (..)
			 * 		CallAssign -> id CallAssignAux pvirg 
			 * 		CallAssignAux -> CallAssign1 | Call | Assign | Assign2
			 * 		CallAssign1 -> ponto CallAssign2
			 * 		CallAssign2 -> size Assign2 | id Call
			 * 		Call -> lpar ArgumentList rpar
			 * 		Assign ->Assign1 Assign2
			 * 		Assign1 -> lparR Index rparR
			 * 		Assign2 -> assign RHS
			 * 
			 * 	AST:
			 * 		STMT ->  Call | Assign | (..)
			 * 		Call -> id (ponto id)? ArgumentList
			 * 		Assign -> LHS assign RHS
			 */
			
			TreeNode<Symbol> CallAssign = CSTStmt.children.get(0);
			
			Symbol ID = CallAssign.children.get(0).data;				// TODO: Check
			
			
			TreeNode<Symbol> CallAssignAux = CallAssign.children.get(1);
			
			if(CallAssignAux.children.get(0).data.getID() == NonTerminalID.Call) {
				
				TreeNode<Symbol> CSTCall = CallAssignAux.children.get(0);
				TreeNode<Symbol> ASTCall = ASTStmt.addChild(CSTCall.data);
				
				ASTCall.addChild(ID);
				
				TreeNode<Symbol> ArgumentList = CSTCall.children.get(1);
				
				ArgumentList(ArgumentList, ASTCall.addChild(ArgumentList.data));
				
				
				return;
			}
			
			if(CallAssignAux.children.get(0).data.getID() == NonTerminalID.Assign) {
				//AST: 
				//Assign -> LHS assign RHS
				//LHS -> ArrayAccess 
				//ArrayAccess -> id lparR Index rparR
				TreeNode<Symbol> Assign = CallAssignAux.children.get(0);
				TreeNode<Symbol> Assign1 = Assign.children.get(0);
				TreeNode<Symbol> Assign2 = Assign.children.get(1);
				
				TreeNode<Symbol> ASTAssign = ASTStmt.addChild(new NonTerminal(NonTerminalID.Assign));
				TreeNode<Symbol> LHS = ASTAssign.addChild(new NonTerminal(NonTerminalID.LHS));
				TreeNode<Symbol> ArrayAccess = LHS.addChild(new NonTerminal(NonTerminalID.ArrayAccess));
				
				// ArrayAccess:
				ArrayAccess.addChild(ID);													// id
				ArrayAccess.addChild(Assign1.children.get(0).data);							// lparR	
				TreeNode<Symbol> Index = Assign1.children.get(1);
				ArrayAccess.addChild(Index.data).addChild(Index.children.get(0).data);		// Index -> (..)
				ArrayAccess.addChild(Assign1.children.get(2).data);							// rparR
				
				// Assign:
				ASTAssign.addChild(Assign2.children.get(0).data);
				
				TreeNode<Symbol> Rhs = Assign2.children.get(1);
				RHS(Rhs, ASTAssign.addChild(Rhs.data));
				
				return;
			}
			
			if(CallAssignAux.children.get(0).data.getID() == NonTerminalID.Assign2) {		
				TreeNode<Symbol> Assign2 = CallAssignAux.children.get(0);
				TreeNode<Symbol> ASTAssign = ASTStmt.addChild(new NonTerminal(NonTerminalID.Assign));
				TreeNode<Symbol> LHS = ASTAssign.addChild(new NonTerminal(NonTerminalID.LHS));
				
				//ScalarAccess:
				TreeNode<Symbol> ScalarAccess = LHS.addChild(new NonTerminal(NonTerminalID.ScalarAccess));
				ScalarAccess.addChild(ID);
				
				//Assign:
				ASTAssign.addChild(Assign2.children.get(0).data);
				TreeNode<Symbol> Rhs = Assign2.children.get(1);
				RHS(Rhs, ASTAssign.addChild(Rhs.data));
				return;
			}
			
			if(CallAssignAux.children.get(0).data.getID() == NonTerminalID.CallAssign1) {
				
				TreeNode<Symbol> CallAssign1 = CallAssignAux.children.get(0);
				
				Symbol ponto = CallAssign1.children.get(0).data;
				TreeNode<Symbol> CallAssign2 = CallAssign1.children.get(1);
 				
				if(CallAssign2.children.get(0).data.getID() == TokenID.SIZE){
					TreeNode<Symbol> ASTAssign = ASTStmt.addChild(new NonTerminal(NonTerminalID.Assign));
					TreeNode<Symbol> LHS = ASTAssign.addChild(new NonTerminal(NonTerminalID.LHS));
					TreeNode<Symbol> ScalarAccess = LHS.addChild(new NonTerminal(NonTerminalID.ScalarAccess));
					ScalarAccess.addChild(ID);
					ScalarAccess.addChild(ponto);
					ScalarAccess.addChild(CallAssign2.children.get(0).data);		//size
					
					TreeNode<Symbol> Assign2 = CallAssign2.children.get(1);  
					ASTAssign.addChild(Assign2.children.get(0).data);
					TreeNode<Symbol> Rhs = Assign2.children.get(1);
					RHS(Rhs, ASTAssign.addChild(Rhs.data));
					
				}else{
					TreeNode<Symbol> ASTCall = ASTStmt.addChild(new NonTerminal(NonTerminalID.Call));
					ASTCall.addChild(ID);									// id
					ASTCall.addChild(ponto);								// ponto
					ASTCall.addChild(CallAssign2.children.get(0).data);		// id
					
					TreeNode<Symbol> CSTCall = CallAssign2.children.get(1);	
					TreeNode<Symbol> ArgumentList = CSTCall.children.get(1);
					
					ArgumentList(ArgumentList, ASTCall.addChild(ArgumentList.data));
				}
				
				
				
				return;
			}
			
			
			
			
		}
		
	}
	
	
	//	CST: lpar LHS rela_op RHS rpar
	//	AST: LHS rela_op RHS 
	private void EXPRTest(TreeNode<Symbol> CSTEXPRtest, TreeNode<Symbol> ASTEXPRtest) {
		
		TreeNode<Symbol> LHS = CSTEXPRtest.children.get(1);
		TreeNode<Symbol> rela_op = CSTEXPRtest.children.get(2);
		TreeNode<Symbol> RHS = CSTEXPRtest.children.get(3);
		
		LHS(LHS, ASTEXPRtest.addChild(LHS.data));
		ASTEXPRtest.addChild(rela_op.data);
		RHS(RHS, ASTEXPRtest.addChild(RHS.data));
		
	}
	
	
	/*
	 * 	CST:
	 * 		LHS -> id Access
	 *		Access -> '' | ponto size | lparR Index rparR
	 *		Index -> id | integer			
	 *  AST: 
	 *  	LHS -> ArrayAccess | ScalarAccess 
	 */
	private void LHS (TreeNode<Symbol> CSTLhs, TreeNode<Symbol> ASTLhs) {		
		
		Symbol ID = CSTLhs.children.get(0).data;
		
		TreeNode<Symbol> Access = CSTLhs.children.get(1);
		
		
		if(Access.children.get(0).data.getID() == TokenID.EPSILON) {	// ScalarAccess 
			
			ASTLhs.addChild(new NonTerminal(NonTerminalID.ScalarAccess)).addChild(ID);
						
			return;
		}
		
		if(Access.children.get(0).data.getID() == TokenID.PONTO){ 		// ScalarAccess
			TreeNode<Symbol> ScalarAccess = ASTLhs.addChild(new NonTerminal(NonTerminalID.ScalarAccess));
			ScalarAccess.addChild(ID);								// id
			ScalarAccess.addChild(Access.children.get(0).data);		// ponto
			ScalarAccess.addChild(Access.children.get(1).data);		// size
		}
		
		if(Access.children.get(0).data.getID() == TokenID.LPARR){		// ArrayAccess
			TreeNode<Symbol> ArrayAccess = ASTLhs.addChild(new NonTerminal(NonTerminalID.ArrayAccess));
			ArrayAccess.addChild(ID);
			ArrayAccess.addChild(Access.children.get(0).data);
			ArrayAccess.addChild(Access.children.get(1).data).addChild(Access.children.get(0).children.get(0).data);
			ArrayAccess.addChild(Access.children.get(2).data);
		}
		
		
		
	}
	
	
	
	/*
	 * CST:
	 * 		RHS -> RHS1 | | lparR ArraySize rparR
	 * 		RHS1 -> Term ArithTerm
	 * 		ArithTerm -> EPSILON | OP Term 
	 *     	OP -> arith | bitwise | addsub
	 *     	Term -> Term1 Term2
	 *     	Term1 -> '' | addsub
	 *		Term2 -> integer | id Term3
	 *		Term3 -> '' | lparR Index rparR | ponto CallOrScalarAccess | Call 
	 *		CallOrScalarAccess -> size | id Call 
	 *		ArraySize -> ScalarAccess | integer
	 *		ScalarAccess -> id ScalarAccess1
	 *		ScalarAccess1 -> '' | ponto size
	 * 
	 * AST:
	 * 		RHS -> Term | Term OP Term | lparR ArraySize rparR
	 * 		Term -> (addsub_op)? ( integer | Call | ArrayAccess | ScalarAccess )
	 *		Call -> id (ponto id)? ArgumentList
	 *		ArrayAccess -> id lparR Index rparR
	 *		ScalarAccess -> id (ponto size)?
	 */
	private void RHS(TreeNode<Symbol> CSTRhs,  TreeNode<Symbol> ASTRhs) {
		
		TreeNode<Symbol> RHS1 = CSTRhs.children.get(0);
		
		if(RHS1.data.getID() == TokenID.LPARR) {
			ASTRhs.addChild(CSTRhs.children.get(0).data);
			ArraySize(CSTRhs.children.get(1), ASTRhs.addChild(CSTRhs.children.get(1).data));
			ASTRhs.addChild(CSTRhs.children.get(2).data);
			return;
		}
		
		TreeNode<Symbol> Term = RHS1.children.get(0);
		TreeNode<Symbol> ArithTerm = RHS1.children.get(1);
		
		
		// Term
		Term(Term, ASTRhs.addChild(Term.data));
		
		// ArithTerm
		if(ArithTerm.children.get(0).data.getID() == TokenID.EPSILON) 
			return;
		
		TreeNode<Symbol> OP = ArithTerm.children.get(0);
		Term = ArithTerm.children.get(1);
		ASTRhs.addChild(OP.data).addChild(OP.children.get(0).data);
		
		Term(Term, ASTRhs.addChild(Term.data));
		
		
	}
	
	
	
	
	
	private void Term(TreeNode<Symbol> CSTerm, TreeNode<Symbol> ASTerm) {
		// TODO Auto-generated method stub
		TreeNode<Symbol> Term1 = CSTerm.children.get(0);
		TreeNode<Symbol> Term2 = CSTerm.children.get(1);
				
		if(Term1.children.get(0).data.getID() != TokenID.EPSILON) 	//addsub_op
			ASTerm.addChild(Term1.children.get(0).data);
		
		// now we must figure out what term2 is: Call | Integer | ArrayAccess | ScalarAccess   :(
		
		TreeNode<Symbol> Term2Child = Term2.children.get(0);
		
		if(Term2Child.data.getID() == TokenID.INTEGER) {
			ASTerm.addChild(Term2Child.data);
			return;
		}
		
		// now it can only be: Call | ArrayAccess | ScalarAccess
		
		Symbol ID = Term2Child.data;
		TreeNode<Symbol> Term3 = Term2.children.get(1);
		
		
		if(Term3.children.get(0).data.getID() == TokenID.EPSILON) {	 // It's a ScalarAccess
			TreeNode<Symbol> ScalarAccess = ASTerm.addChild(new NonTerminal(NonTerminalID.ScalarAccess));
			ScalarAccess.addChild(ID);
			return;
		}
		
		
		if(Term3.children.get(0).data.getID() == TokenID.LPARR) {				// ArrayAccess
			TreeNode<Symbol> ArrayAccess = ASTerm.addChild(new NonTerminal(NonTerminalID.ArrayAccess));
			ArrayAccess.addChild(ID);
			ArrayAccess.addChild(Term3.children.get(0).data);
			TreeNode<Symbol> Index = Term3.children.get(1);
			ArrayAccess.addChild(Index.data).addChild(Index.children.get(0).data);  	// ArrayAccess -> id lparR (Index -> id | integer) rparR 
			
			ArrayAccess.addChild(Term3.children.get(2).data);
			
			return;
		}
		
		if(Term3.children.get(0).data.getID() == NonTerminalID.Call ){ 			// Call
			TreeNode<Symbol> Call = ASTerm.addChild(Term3.children.get(0).data);
			Call.addChild(ID);
			
			// ArgumentList:
			TreeNode<Symbol> CSTCall = Term3.children.get(0);
			TreeNode<Symbol> ArgumentList = CSTCall.children.get(1);
			
			ArgumentList(ArgumentList, Call.addChild(ArgumentList.data));
			
			return;
		}
		
		if(Term3.children.get(0).data.getID() == TokenID.PONTO) {				//	ScalarAccess or Call
			
			TreeNode<Symbol> CallOrScalarAccess = Term3.children.get(1);
			
			TreeNode<Symbol> CallOrScalarAccessChild = CallOrScalarAccess.children.get(0);
			
			if(CallOrScalarAccessChild.data.getID() == TokenID.SIZE) {			// ScalarAccess
				TreeNode<Symbol> ScalarAccess = ASTerm.addChild(new NonTerminal(NonTerminalID.ScalarAccess));
				ScalarAccess.addChild(ID);
				ScalarAccess.addChild(Term3.children.get(0).data);
				ScalarAccess.addChild(CallOrScalarAccessChild.data);
				return;
			}else {																// Call
				TreeNode<Symbol> Call = ASTerm.addChild(CallOrScalarAccess.children.get(1).data);	
				Call.addChild(ID);								// id
				Call.addChild(Term3.children.get(0).data);		// ponto
				Call.addChild(CallOrScalarAccessChild.data);	// id	
				
				// ArgumentList:
				TreeNode<Symbol> CSTCall = CallOrScalarAccess.children.get(1);
				TreeNode<Symbol> ArgumentList = CSTCall.children.get(1);
				
				ArgumentList(ArgumentList, Call.addChild(ArgumentList.data));
				
				
				return;
			}
			
		}
		
		
	}

	private void ArgumentList(TreeNode<Symbol> CSTArgumentList, TreeNode<Symbol> ASTArgumentList) {
		// TODO Auto-generated method stub
		
		
		if(CSTArgumentList.data.getID() == NonTerminalID.Argument2) {		// Argument2 -> EPSILON | virg Argument Argument2
			if(CSTArgumentList.children.get(0).data.getID() == TokenID.EPSILON)
				return;
			
			TreeNode<Symbol> Argument = CSTArgumentList.children.get(1); 
			
			ASTArgumentList.addChild(Argument.data).addChild(Argument.children.get(0).data);
			ArgumentList(CSTArgumentList.children.get(2), ASTArgumentList);
			
		}else {																// ArgumentList	-> EPSILON | Argument Argument2
			if(CSTArgumentList.children.get(0).data.getID() == TokenID.EPSILON)
				return;
			TreeNode<Symbol> Argument = CSTArgumentList.children.get(0);
			ASTArgumentList.addChild(Argument.data).addChild(Argument.children.get(0).data);
			ArgumentList(CSTArgumentList.children.get(1), ASTArgumentList);
			
		}
		
		
		
		
	}

	private void VARLIST(TreeNode<Symbol> CSTVarList, TreeNode<Symbol> ASTVarList) {
		// TODO Auto-generated method stub
		TreeNode<Symbol> ArrayOrScalarElement = null;
		if(CSTVarList.data.getID() == NonTerminalID.VarList) {
		
			ArrayOrScalarElement = CSTVarList.children.get(0);
			
		}else if(CSTVarList.data.getID() == NonTerminalID.VarListEnd) {
			if(CSTVarList.children.get(0).data.getID() == TokenID.EPSILON)
				return;
			
			ArrayOrScalarElement = CSTVarList.children.get(1);
		}
		
		
		TreeNode<Symbol> ASTArrayOrScalarElement = ASTVarList.addChild(ArrayOrScalarElement.data);
		
		ASTArrayOrScalarElement.addChild(ArrayOrScalarElement.children.get(0).data);	// id
		TreeNode<Symbol> ArrOrScal = ArrayOrScalarElement.children.get(1);
		if(ArrOrScal.children.get(0).data.getID() != TokenID.EPSILON) {
			ASTArrayOrScalarElement.addChild(ArrOrScal.children.get(0).data);
			ASTArrayOrScalarElement.addChild(ArrOrScal.children.get(1).data);
		}
			
		if(CSTVarList.data.getID() == NonTerminalID.VarList)
			VARLIST(CSTVarList.children.get(1), ASTVarList);
		else
			VARLIST(CSTVarList.children.get(2), ASTVarList);
	
		
	}

	// Dec -> Declaration*
	public void Dec(TreeNode<Symbol> CSTDec, TreeNode<Symbol> ASTDeclarations) {
		
		for(int i = 0; i < CSTDec.children.size(); i++) {
			if(CSTDec.children.get(i).data.getID() == NonTerminalID.DECLARATION)
				DECLARATION(CSTDec.children.get(i), ASTDeclarations.addChild(CSTDec.children.get(i).data));
			else
				Dec(CSTDec.children.get(i), ASTDeclarations);
		}
		
	}
	
	//CST: Declaration -> ArrayOrScalarElement DeclarationEnd pvirg    AST: Declaration -> id (lparR rparR)? DeclarationEnd
	public void DECLARATION(TreeNode<Symbol> CSTdeclaration, TreeNode<Symbol> ASTdeclaration) {
		 ArrayList<Symbol> ID = ArrOrScal(CSTdeclaration);
		 
		 for(int i = 0; i < ID.size(); i++) {
			 ASTdeclaration.addChild(ID.get(i));
			
		 } //Declaration -> id (lparR rparR)? 
		 
		 //Declaration -> (...) DeclarationEnd
		 DeclarationEnd(CSTdeclaration.children.get(1), ASTdeclaration.addChild(CSTdeclaration.children.get(1).data));
		 
		 
	}
	
	
	//ArrayOrScalarElement -> id ArrOrScal && ArrOrScal -> EPSILON | lparR rparR
	public ArrayList<Symbol> ArrOrScal(TreeNode<Symbol> node){
		TreeNode<Symbol> ArrayOrScalarElement = node.children.get(0);
		
		ArrayList<Symbol> arrayOrScalarElement = new ArrayList<Symbol>();
		arrayOrScalarElement.add(ArrayOrScalarElement.children.get(0).data);		//id
		
		TreeNode<Symbol> ArrOrScal = ArrayOrScalarElement.children.get(1);
		if(ArrOrScal.children.size() == 0)
			return arrayOrScalarElement;
		else if(ArrOrScal.children.get(0).data.getID() == TokenID.EPSILON) {
			return arrayOrScalarElement;
		}
		
		else {
			arrayOrScalarElement.add(ArrOrScal.children.get(0).data);		//lparR
			arrayOrScalarElement.add(ArrOrScal.children.get(1).data);		//rparR
		}
			
		
		//Node -> id (lparR rparR)
		
		return arrayOrScalarElement;
	}
	
	
	//DeclarationEnd -> EPSILON | assign addsub_op integer | assign lparR ArraySize rparR | assign integer
	public void DeclarationEnd (TreeNode<Symbol> CSTDeclarationNode, TreeNode<Symbol> ASTDeclarationNode) {
		
		if(CSTDeclarationNode.children.size() == 0 || CSTDeclarationNode.children.get(0).data.getID() == TokenID.EPSILON)
			return;
		
		
		
		ASTDeclarationNode.addChild(CSTDeclarationNode.children.get(0).data); 	// ASTDeclarationEnd -> assign DecAssign
		
		TreeNode<Symbol> DecAssign = CSTDeclarationNode.children.get(1);
		
		if(DecAssign.children.get(0).data.getID() == TokenID.LPARR) {			// DecAssign -> lparR ArraySize rparR
			ASTDeclarationNode.addChild(DecAssign.children.get(0).data);
			TreeNode<Symbol> ArraySize = ASTDeclarationNode.addChild(DecAssign.children.get(1).data);
			ASTDeclarationNode.addChild(DecAssign.children.get(2).data);
			ArraySize(DecAssign.children.get(1), ArraySize);
			
		}else{																	// DecAssign -> addsubop? integer 
			TreeNode<Symbol> ADDSUB = DecAssign.children.get(0);
			if(ADDSUB.children.size() != 0 || ADDSUB.children.get(0).data.getID() != TokenID.EPSILON)
				ASTDeclarationNode.addChild(ADDSUB.children.get(0).data);
			ASTDeclarationNode.addChild(DecAssign.children.get(1).data);
			return ;
			
		}
		
		
		
		
		
		return ;
	}
	
	
	
	// ArraySize -> integer | id | id.size
	public void ArraySize (TreeNode<Symbol> CSTArraySize, TreeNode<Symbol> ASTArraySize) {
		
		if(CSTArraySize.children.get(0).data.getID() == TokenID.INTEGER) {
			ASTArraySize.addChild(CSTArraySize.children.get(0).data);
		}
		else {
			TreeNode<Symbol> ScalarAccess = CSTArraySize.children.get(0);
			ASTArraySize.addChild(ScalarAccess.children.get(0).data);
			TreeNode<Symbol> ScalarAccess1 = ScalarAccess.children.get(1);
			if(ScalarAccess1.children.size() == 0 || ScalarAccess1.children.get(0).data.getID() == TokenID.EPSILON)
				return ;
			
			ASTArraySize.addChild(ScalarAccess1.children.get(0).data);
			
			ASTArraySize.addChild(ScalarAccess1.children.get(1).data);
		}
			
		
		
		return;
	}
	
	
	
	
}
