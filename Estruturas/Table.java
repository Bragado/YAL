package Estruturas;

import java.util.ArrayList;
import java.util.function.Function;

import Estruturas.TSymbol.TYPE;

public class Table {
		Table parent = null;
		
		public static String module_name = "";
		
		
		public enum TYPE {
			MODULE, DECLARATIONS, FUNCS, FUNCTION, WHILE
		}
		
		
		public TYPE type = TYPE.MODULE;
		
		
		ArrayList<Table> childs = new ArrayList<Table>();
		
		
		/*
		 * 	Entries:
		 * 		MODULE: Name
		 * 		DECLARATIONS: Declaração
		 * 		Functions: 	Name & Ret 
		 */
		ArrayList<TSymbol> entries = new ArrayList<TSymbol>();			// generic entries
		
		
		/*
		 * 	Only used in functions
		 */
		ArrayList<TSymbol> params = new ArrayList<TSymbol>();
		
		/*
		 * 	Only used in functions
		 */
		ArrayList<TSymbol> locals = new ArrayList<TSymbol>();
		
		
		public Table() {
			
		}
		
		public Table(Table parent, TYPE type) {
			this.parent = parent;
			this.type = type;
		}
		
		public ArrayList<TSymbol> getEntries() {
			return this.entries;
		}
		
		
		public ArrayList<Table> getchilds() {
			return this.childs;
		}

		public void addEntrie(TSymbol module_name) {
			// TODO Auto-generated method stub
			this.entries.add(module_name);
		}

		public Table addChild(Table table) {
			// TODO Auto-generated method stub
			childs.add(table);
			return table;
		}

		/*
		 *	Search for a variable name in entries 				 
		*/
		
		public boolean variable_exists(Symbol iD) {
			// TODO Auto-generated method stub
			switch(type){
			case DECLARATIONS:
				for(int i = 0; i < entries.size(); i++) {
					TSymbol declaration = entries.get(i);
					Symbol symbol = declaration.data;
					
					if(((Token)declaration.data).getDate().equals(((Token)iD).getDate()))
						return true;
							
				}
				
				return false;
				
			case FUNCTION:
				
			}
			
			
			return false;
		}
		
		/*
		 *  Search a variable by name
		 */
		public TSymbol search_variable(Symbol ID) {
			Token iD = (Token)ID;
			switch(type) {
			case FUNCTION:
				// 1st searchs at local variables
				// 2nd searchs at parameters
				// 3rd searchs at global variables
				
				
				for(int i = 0; i < locals.size(); i++) {
					Token var = (Token)(locals.get(i).data);
					if(iD.getDate().equals(var.getDate()))
						return locals.get(i);
					
				}
				for(int i = 0; i < params.size(); i++) {
					Token var = (Token)(params.get(i).data);
					if(iD.getDate().equals(var.getDate()))
						return params.get(i);
					
				}
				
				if(entries.size() > 1) {
					Token var = (Token)(entries.get(1).data);
					if(iD.getDate().equals(var.getDate()))
						return entries.get(1);
				}
				
				
				
				Table Tparent = this.parent;
				while(Tparent.parent != null){
					Tparent = Tparent.parent;
					
					
				}
				
				Tparent = Tparent.childs.get(0);
				for(int i = 0; i < Tparent.entries.size(); i++) {
					Token var = (Token)(Tparent.entries.get(i).data);
					if(iD.getDate().equals(var.getDate()))
						return Tparent.entries.get(i);
				}
				
				break;
			case WHILE:
								
				for(int i = 0; i < locals.size(); i++) {
					Token var = (Token)(locals.get(i).data);
					if(iD.getDate().equals(var.getDate()))
						return locals.get(i);
				
					}
				
				return parent.search_variable(iD);
		}
			return null;
	}	
		
		
		/*
		 * 	Search for a variable name and type in entries
		 */

		public boolean variable_exists(Symbol iD, TSymbol.TYPE ttype) {
			// TODO Auto-generated method stub
			switch(type) {
				case DECLARATIONS:
					for(int i = 0; i < entries.size(); i++) {
						TSymbol declaration = entries.get(i);
						Symbol symbol = declaration.data;
						
						if(declaration.type == ttype && ((Token)declaration.data).getDate().equals(((Token)iD).getDate()))
							return true;
								
					}
					
					return false;
				case FUNCTION:
					break;
			}
			
			
			return false;
		}
		
		/*
		 * 	Search for a variable name, type and inicialization in entries
		 */
		
		
		public boolean variable_exists(Symbol iD, TSymbol.TYPE ttype, boolean inicialization) {
			// TODO Auto-generated method stub
			switch(type) {
				case DECLARATIONS:
					for(int i = 0; i < entries.size(); i++) {
						TSymbol declaration = entries.get(i);
						Symbol symbol = declaration.data;
						
						if(declaration.type == ttype && declaration.inicialized == inicialization && ((Token)declaration.data).getDate().equals(((Token)iD).getDate()))
							return true;
								
					}
					
					return false;
				case FUNCTION:
					break;
			}
			
			
			return false;
		}
		
		
		/*
		 * 		Verifies if a function already exists
		 */
		public boolean function_exists(Symbol ID) {
			
			Table tableFunctions = this;
			while(tableFunctions.parent != null) {
				tableFunctions = tableFunctions.parent;
			}
			
			tableFunctions = tableFunctions.childs.get(1);
			
			for(int i = 0; i < tableFunctions.childs.size(); i++) {
				
				Table function = tableFunctions.childs.get(i);
				if(function.entries.size() == 0)
					continue;
				
				TSymbol FunctionName = function.entries.get(0);
				if(((Token)FunctionName.data).getDate().equals(((Token)ID).getDate()))
					return true;
				
			}
			
			
			
			return false;
		}
		
		public Table search_function(Symbol ID) {
			
			Table tableFunctions = this;
			while(tableFunctions.parent != null) {
				tableFunctions = tableFunctions.parent;
			}
			
			tableFunctions = tableFunctions.childs.get(1);
			
			for(int i = 0; i < tableFunctions.childs.size(); i++) {
				
				Table function = tableFunctions.childs.get(i);
				if(function.entries.size() == 0)
					continue;
				
				TSymbol FunctionName = function.entries.get(0);
				if(((Token)FunctionName.data).getDate().equals(((Token)ID).getDate()))
					return function;
				
			}
			
			
			
			return null;
			
		}
		
		
		public TSymbol.TYPE getFunctionRetType(Symbol ID) {
			
			Table tableFunctions = this;
			while(tableFunctions.parent != null) {
				tableFunctions = tableFunctions.parent;
			}
			
			tableFunctions = tableFunctions.childs.get(1);
			
			for(int i = 0; i < tableFunctions.childs.size(); i++) {
				Table function = tableFunctions.childs.get(i);
				if(function.entries.size() == 0)
					continue;
				
				TSymbol FunctionName = function.entries.get(0);
				if(((Token)FunctionName.data).getDate().equals(((Token)ID).getDate()) ) {
					if(function.entries.size() > 1) 
						return function.entries.get(1).type;
					
					
					
				}
					
				
			}
			
			return null;
		}
		
		
		/*
		 * 		Verifies if a function already exists given name and parameters
		 * 		It will only be used in check_call
		 * 		
		 */
		public boolean function_exists(Symbol ID, ArrayList<Symbol> params) {
			
			
			
			Table tableFunctions = this;
			while(tableFunctions.parent != null) {
				tableFunctions = tableFunctions.parent;
			}
			
			tableFunctions = tableFunctions.childs.get(1);
			
			for(int i = 0; i < tableFunctions.childs.size(); i++) {
				Table function = tableFunctions.childs.get(i);
				if(function.entries.size() == 0)
					continue;
				
				TSymbol FunctionName = function.entries.get(0);
				if(((Token)FunctionName.data).getDate().equals(((Token)ID).getDate()) && matchParams(function, params)) // TODO: wrong!!!
					return true;
				
			}
			
			
			
			return false;
		}
		
		/*
		 * 	matchs two argument lists
		 * 		1st: 	check if the number of parameters if the same
		 * 		2nd:	check if all params type ID are inicialized and if the type correspond 
		 */
		
		/** Checks if all parameters of a call are correct
		 * 	@param function has the information of the function to be called
		 * 	@param params2 has the parameters passed to the function to be called
		 * 	@return true if the params match, false otherwise
		 */

		private boolean matchParams(Table function, ArrayList<Symbol> params2) {
			ArrayList<TSymbol> params = function.params;	
			
			if(params.size() != params2.size())
				return false;
			
			for(int i = 0; i < params2.size(); i++) {
				
				//1st check if the variable has been inicilized //
				TSymbol var = search_variable(params2.get(i));
				if(var == null) {
					System.out.println("the variable " + ((Token)params2.get(i)).getDate() + " was not declared");
					return false;
				}
				
				
				if(!var.inicialized) {
					System.out.println("the variable " + ((Token)params2.get(i)).getDate() + " was not initialized");
					
				}
				
				//2nd: check if types correspond				
				if(!(function.params.get(i).type == var.type))
					System.out.println("the variable " + ((Token)params2.get(i)).getDate() + " does not match the function parameter type");
			}
			
			return true;
		}
		
		
		

		public void addParam(TSymbol tSymbol) {
			// TODO Auto-generated method stub
			this.params.add(tSymbol);
		}

		public void addLocal(TSymbol tSymbol) {
			// TODO Auto-generated method stub
			this.locals.add(tSymbol);
		}
		
		
		
		/*
		public TSymbol searchVariable(String name) {
			
			switch(type) {
				case MODULE:
					return null;
					
				case DECLARATIONS:
					for(int i = 0; i <  entries.size(); i++) {
						if(((Token)entries.get(i).data).getDate().equals(name)) {
							return entries.get(i);
						}
					}
					return null;
				case FUNCS:
					for(int i = 0; i < entries.size(); i++) {
						
					}
					
					
					
					break;
				
				
			}
			
			return null;
		}
		
		*/
		
		
}
