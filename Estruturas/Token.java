package Estruturas;

public class Token  {
	
	private TokenID tokenID;
	private String data = "";
	private int column;
	private int line;	
	
	public Token (TokenID tokenID, String data, int column, int line)  {
		this.tokenID = tokenID;
		this.data = data;
		this.column = column;
		this.line = line;	
	}

	public String getDate() {
		return this.data;
	}
	
	public TokenID getTokenID() {
		return this.tokenID;
	}

	public int getColumn() {
		return this.column;
	}

	public int line() {
		return this.line;
	}


}