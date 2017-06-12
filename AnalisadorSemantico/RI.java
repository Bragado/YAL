package AnalisadorSemantico;

import Estruturas.Node;
import Estruturas.NonTerminalID;
import Estruturas.Symbol;
import Estruturas.Token;
import Estruturas.TokenID;
import Estruturas.TreeNode;

public class RI {
	
	public TreeNode<Symbol> AST;
	public Node<String> RI;
	
	public RI(TreeNode<Symbol> AST) {
		this.AST = AST;
	}
	
	public Node<String> run() {
		RI = new Node<String>("MODULE");
		
		Token module_name = (Token)AST.children.get(0).data;
		
		RI.addChild(new Node<String>(module_name.getDate()));
		
		// declarations
		Node<String> declarations = new Node<String>("DECLARATIONS");
		RI.addChild(declarations);
		
		//functions
		Node<String> functions = new Node<String>("FUNCTIONS");
		RI.addChild(functions);
		
		
		TreeNode<Symbol> Dec = AST.children.get(1);
		TreeNode<Symbol> Func = AST.children.get(2);
		
		
		
		// for each function
		for(int i = 0; i < Func.children.size(); i++) {
			Node<String> function = new Node<String>("function");
			
			create_function(Func.children.get(i), function);
		
		}
		
		return RI;
	}

	
	// AST: Function -> FunctionAssign id Varlist Stmtlst
	// RI:	function -> Assign Parameters BLOCK
	private void create_function(TreeNode<Symbol> ASTfunction, Node<String> RIfunction) {
		// TODO Auto-generated method stub
		TreeNode<Symbol> FunctionAssign = ASTfunction.children.get(0);
		Token ID = (Token) ASTfunction.children.get(1).data;
		TreeNode<Symbol> Varlist = ASTfunction.children.get(2);
		TreeNode<Symbol> Stmtlst = ASTfunction.children.get(3);
		
		// Assign
		if(FunctionAssign.children.size() == 0){					// does not have assign
			RIfunction.addChild(new Node<String>(ID.getDate()));
		}else {														// has assign, it can either be array or integer
			
			Node<String> assign = new Node<String>("ASSIGN");
			
			
			Token assign_var = (Token) FunctionAssign.children.get(0).data;
			
			if(FunctionAssign.children.size() == 3){				// array
				Node<String> array = new Node<String>("ARRAY");
				array.addChild(new Node<String>(assign_var.getDate()));
				
				assign.addChild(array);
				
			}else if(FunctionAssign.children.size() == 1){			// integer
				assign.addChild(new Node<String>(assign_var.getDate()));
			}
			
			assign.addChild(new Node<String>(ID.getDate()));
			RIfunction.addChild(assign);
			
		}
		
		//parameters
		Node<String> parameters = new Node<String>("PARAMETERS");
		
		for(int i = 0; i < Varlist.children.size(); i++) {
			TreeNode<Symbol> ArrayOrScalarElement = Varlist.children.get(i);
			Token param = (Token)ArrayOrScalarElement.children.get(0).data;
			if(ArrayOrScalarElement.children.size() == 1){			// integer
				
				parameters.addChild(new Node<String>(param.getDate()));
			}else {													// array
				Node<String> array = new Node<String>("ARRAY");
				array.addChild(new Node<String>(param.getDate()));
				
				parameters.addChild(array);
			}
			
		}
		
		RIfunction.addChild(parameters);
		
		
		Node<String> block = new Node<String>("BLOCK");
		
		for(int i = 0; i < Stmtlst.children.size(); i++) {
			create_Stmt(Stmtlst.children.get(i), block);
		}
		
		RIfunction.addChild(block);
		
	}

	private void create_Stmt(TreeNode<Symbol> stmt, Node<String> block) {
		TreeNode<Symbol> stmtChild = stmt.children.get(0);
		
		if(stmtChild.data.getID() == NonTerminalID.Call) {
			
		}else if(stmtChild.data.getID() == NonTerminalID.Assign){
			TreeNode<Symbol> LHS = stmtChild.children.get(0);
			TreeNode<Symbol> RHS = stmtChild.children.get(2);
			
			Node<String> assign = new Node<String> ("ASSIGN");
			
			if(LHS.children.get(0).data.getID() == NonTerminalID.ArrayAccess){			// id[N] = ... ; id[1] = ...
				
				
				
				
			}else if(LHS.children.get(0).data.getID() == NonTerminalID.ScalarAccess){	// id = ...
				
				if(RHS.children.size() == 1) {						// RHS -> Term
					TreeNode<Symbol> ScalarAccess = LHS.children.get(0);
					assign.addChild(new Node<String>(((Token)ScalarAccess.children.get(0).data).getDate()));
					assign.addChild(Term(RHS.children.get(0)));	
					
				}else {												// RHS -> Term OP Term
					
					
				}
				
				
				
			}
			
			block.addChild(assign);
			
			
		}else if(stmtChild.data.getID() == NonTerminalID.IF) {
			
		}else if(stmtChild.data.getID() == NonTerminalID.WHILE){
			
		}
		
		
	}
	
	public Node<String> Term(TreeNode<Symbol> Term) {
		
		TreeNode<Symbol> TermChild;
		
		
		if(Term.children.size() > 1) {					// Term -> addsub_op (integer | call | ArrayAccess | ScalarAccess
			TermChild = Term.children.get(1);
		}else {											// Term -> integer | call | ArrayAccess | ScalarAccess
			TermChild = Term.children.get(0);
		}
		
			
			if(Term.children.get(0).data.getID() == TokenID.INTEGER){			// a = 1
				
				return new Node<String>(((Token)TermChild.data).getDate());
				
			}else if(Term.children.get(0).data.getID() == NonTerminalID.ArrayAccess){	// a = b[N] ; a = b[1]
				
				TreeNode<Symbol> Index = TermChild.children.get(2);
				
				
				Node<String> array = new Node<String>("ARRAY");
				array.addChild(new Node<String>(   ((Token)TermChild.children.get(0).data).getDate()));
				array.addChild(new Node<String>( ((Token)Index.children.get(0).data).getDate()));
				
				return array;
				
			}else if(Term.children.get(0).data.getID() == NonTerminalID.ScalarAccess){		// a = b ; a  = b.size
				
				TreeNode<Symbol> scalarAccess2 = Term.children.get(0);
				
				if(scalarAccess2.children.size() == 1) {				// a = b;
					return new Node<String>( ((Token)scalarAccess2.children.get(0).data).getDate());
					
					
				}else if(scalarAccess2.children.size() == 3){			// a = b.size
					
					Node<String> call = new Node<String>("CALL");
					
					Token id1 = (Token)scalarAccess2.children.get(0).data;
					
					call.addChild(new Node<String>(id1.getDate()));
					call.addChild(new Node<String>("size"));
					call.addChild(new Node<String>("PARAMETERS"));
					call.addChild(new Node<String>("INTEGER"));
				
					return call;
				}
				
			}else if(Term.children.get(0).data.getID() == NonTerminalID.Call){
				
			}
		
		return null;
	}
	
	
	
	
	
}
