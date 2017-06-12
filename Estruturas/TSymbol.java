package Estruturas;

public class TSymbol {
	
	public Symbol data;
	
	public enum TYPE {
		INT, ARRAY, FunctionName, ModuleName, ArraySize
	}
	
	public TYPE type;
	
	public boolean inicialized = true;
	
	
	public TSymbol(Symbol data, TYPE type, boolean inicialized) {
		
		this.data = data;
		this.type = type;
		this.inicialized = inicialized;
	}
	
	
	public TSymbol(Symbol data, TYPE type) {
		this.data = data;
		this.type = type;
	}
	
	
}
