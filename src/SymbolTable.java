/**
 * Class that represent an instance of a symboltable
 * 
 * @author Alvaro, Cedric Y.
 */
public class SymbolTable {
	String token;
	String type;
	String value;

	public SymbolTable() {

	}

	public SymbolTable(String token, String type, String value) {
		this.token = token;
		this.type = type;
		this.value = value;
	}

	public SymbolTable(String token, String type) {
		this.token = token;
		this.type = type;
		this.value = "";
	}

	public String getToken(SymbolTable st) {
		return st.token;
	}

	public String getType(SymbolTable st) {
		return st.type;
	}

	/**
	 * Gets the variable's value
	 */
	public String getValue() {
		return this.value;
	}

	public void setValue(SymbolTable st, String value) {
		st.value = value;
		this.value = value;
	}

}