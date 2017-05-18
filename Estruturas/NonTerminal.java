package Estruturas;

public class NonTerminal extends Symbol{
	
	NonTerminalID nonTerminalID;
	
	public NonTerminal(NonTerminalID nonTerminalID) {
		// TODO Auto-generated constructor stub
		this.nonTerminalID = nonTerminalID;
	}
	
	@Override
	public Object getID() {
		// TODO Auto-generated method stub
		return nonTerminalID;
	}

}
