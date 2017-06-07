package Estruturas;

import java.util.ArrayList;
import java.util.function.Function;

import Estruturas.TSymbol.TYPE;

public class Table {
		Table parent = null;
		
		public enum TYPE {
			MODULE, DECLARATIONS, FUNCS, FUNCTION
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
