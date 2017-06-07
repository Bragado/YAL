package AnalisadorSemantico;

import java.util.ArrayList;

import Estruturas.NonTerminalID;
import Estruturas.Symbol;
import Estruturas.TSymbol;
import Estruturas.Table;
import Estruturas.TokenID;
import Estruturas.TreeNode;


public class SemanticAnalyses {
	
	private TreeNode<Symbol> AST;
	private Table SymbolTable; 
	
	
	private ArrayList<Semantic_Error> erros = new ArrayList<Semantic_Error>();
	
	
	
	public SemanticAnalyses(TreeNode<Symbol> AST) {
		this.AST = AST;
		this.SymbolTable = new Table();
		
	}
	
	
	
	public ArrayList<Semantic_Error> run() {
		
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



	private void function_check(TreeNode<Symbol> treeNode, Table function) {
		
		
		
		
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
						declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, true));
						break;
					case 4:			// ex: a = [100] 
						TreeNode<Symbol> ArraySize = DeclarationEnd.children.get(2);
						
						switch(ArraySize.children.size()){
							case 1:						
								if(ArraySize.children.get(0).data.getID() == TokenID.INTEGER) {				// ex: a = [100]
									declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));		
									return;
								}
								
								Symbol ArraySize_ID = ArraySize.children.get(0).data;
								if(declarations.variable_exists(ArraySize_ID, TSymbol.TYPE.INT)) {										// ex: a = [N]	-> N must be declare and must be an integer	
									declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
									
								}else{
									System.out.println("error1");
								}
								
									
								break;
							case 3:
								Symbol ArraySize_IDD = ArraySize.children.get(0).data;
								if(declarations.variable_exists(ArraySize_IDD, TSymbol.TYPE.ARRAY)) {					// ex: a = [b.size] -> b must be declare and must be an array
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
					declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, true));
					break;
				case 3:			// ex: a[] = -1  <=> a = -1
					declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.INT, true));
					break;
				case 4:			// ex: a[] = [ArraySize]
					TreeNode<Symbol> ArraySize = DeclarationEnd.children.get(2);
					
					switch(ArraySize.children.size()) {
						case 1:			// integer | id
							if(ArraySize.children.get(0).data.getID() == TokenID.INTEGER) {				// ex: a[] = [100]
								declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
								return;
							}
							Symbol ArraySize_ID = ArraySize.children.get(0).data;
							if(declarations.variable_exists(ArraySize_ID, TSymbol.TYPE.INT)) {					// ex: a = [N]	-> N must be declare and must be an integer	
								declarations.addEntrie(new TSymbol(ID, TSymbol.TYPE.ARRAY, true));
							}else{
								System.out.println("error3");
							}
							
							
							break;
						case 3:			// id.size
							Symbol ArraySize_IDD = ArraySize.children.get(0).data;
							if(declarations.variable_exists(ArraySize_IDD, TSymbol.TYPE.ARRAY)) {					// ex: a[] = [b.size] -> b must be declare and must be an array
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
