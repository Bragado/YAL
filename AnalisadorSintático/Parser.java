package AnalisadorSintático;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import AnalisadorSintático.ParserTable.SymbolID;
import Estruturas.*;


public class Parser {
	
	private ArrayList<Token> InputTokens;
	private ParserTable table;
	private TreeNode<Symbol> concrete_tree;
	
	
	public Parser(ArrayList<Token> tokens) {
		this.InputTokens = tokens;
		this.table = new ParserTable();
		System.out.println("starting parser");
	 
	}
	
	
	public TreeNode parse() {
		
		Symbol start = new NonTerminal(NonTerminalID.MODULE);
		this.concrete_tree = new TreeNode<Symbol>(start);
		
		
		Stack<TreeNode> stack = new Stack<TreeNode>();
		Symbol end = new Token(TokenID.END, "", 0,  0);
		
		stack.push(new TreeNode<Symbol>(end));
		stack.push(this.concrete_tree);
		
		
		int pointer = 0;
		int i = 0;	
		while(!stack.empty()) {
			i++;
			TreeNode treeNode = stack.peek();
			Symbol symbol = (Symbol) treeNode.data;
			
			 
			
			if(symbol instanceof NonTerminal) {
				
				if(table.getSymbols((NonTerminalID)symbol.getID(), InputTokens.get(pointer).getID()) != null) {
					stack.pop();
					this.addSymbolsToStack(table.getSymbols((NonTerminalID)symbol.getID(), InputTokens.get(pointer).getID()), stack, treeNode);
				
				}else {
					error(this.InputTokens.get(pointer));
					return null;
				}
			}else if(symbol instanceof Token) {
				if((TokenID)symbol.getID() == this.InputTokens.get(pointer).getID()) {
					treeNode.data = this.InputTokens.get(pointer);			// the tree does not contain the values of the token
					stack.pop();
					pointer++;
				}else if((TokenID)symbol.getID() == TokenID.EPSILON){
					stack.pop();
					
				}
				
				else  {
					error(this.InputTokens.get(pointer));
					return null;
				}
					
				
			}
			
			
			
		}
		
		
		
		
		return this.concrete_tree;
	}


	private void addSymbolsToStack(ArrayList<SymbolID> symbols, Stack<TreeNode> stack, TreeNode parent) {
		// TODO Auto-generated method stub
		ArrayList<TreeNode> treeNodes = new ArrayList<TreeNode>();
		
		for(int i = 0; i < symbols.size(); i++) {
			SymbolID symbol = symbols.get(i);
			if(symbol.isToken()) {
				
				treeNodes.add(parent.addChild(new Token((TokenID)symbol.getID(),"",0,0 )));
			} else {
				treeNodes.add(parent.addChild(new NonTerminal((NonTerminalID)symbol.getID())));
			}
			
			
		}
		
		Collections.reverse(treeNodes);
		
		for(int i = 0; i < treeNodes.size(); i++) {
			stack.push(treeNodes.get(i));
		}
		
		
	}


	private boolean error(Token err) {
		// TODO Auto-generated method stub
		System.out.println("Line: " + err.line() + " | Column: " + err.getColumn() + " : Error");
		return false;
	}
	
	
	
	
	
	
	
}
