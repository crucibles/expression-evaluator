import java.util.Vector;

/**
 * Class that represent an instance of a symboltable
 * 
 * @author Alvaro, Cedric Y.
 */
public class SymbolTable {
	private String lexeme = "";
	private String token = "";
	private String type = "";
	private String value = "";
	private Vector<SymbolTable> symbolTable;

	/*
	*	The three constructors for the symbolTable
	*/
	public SymbolTable() {

	}

	public SymbolTable(String lexeme, String token, String type, String value) {
		this.token = token;
		this.type = type;
		this.value = value;
		SymbolTable sb = new SymbolTable(lexeme, token, type, value);
		this.symbolTable.add(sb);
	}

	public SymbolTable(String lexeme, String token, String type) {
		this.token = token;
		this.type = type;
		this.value = "";
		SymbolTable sb = new SymbolTable(token, type, "");
		this.symbolTable.add(sb);
	}

	/**
	 * Gets the token or name of the symbol.
	 * 
	 * @return String token name
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getLexeme() {
		return this.lexeme;
	}
	
	/**
	 * Gets the token or name of the symbol.
	 * 
	 * @return String token name
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Gets the token or name of the symbol.
	 * 
	 * @return String token name
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Gets the variable's value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Gets the vector of symbolTable
	 */
	public Vector<SymbolTable> getVector() {
		return this.symbolTable;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Locates the received variable in the symbol table
	 * 
	 * @param var variable to be searched in the symbol table
	 * 
	 * @return symbol table containing the received variable; null if not found
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public SymbolTable findVariable(String var) {
		for (int index = 0; index < this.symbolTable.size(); index++) {
			SymbolTable sb = this.symbolTable.get(index);
			if (var.equals(sb.token)) { // returns symbol table if its token
										// matches the received variable
				return sb;
			}
		}
		return null; // returns null if variable not found
	}

	/**
	 * Stores value of variables in the LHS (left-hand side) in the symbol
	 * table.
	 * 
	 * @param value value to be stored to the variable
	 * @param stmt statement that contains the variable whose value is to be stored
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void storeResult(int value, String stmt, boolean flag) {
		// AHJ: optimizstion unimplemented; since Ced has tried looking for
		// duplicate variables; why not use a separate function for this
		// and just return a null if not found
		String tokens[] = stmt.split("\\s");

		for (int i = 0; i < tokens.length; i++) {
			// locates the LHS variables by locatingthe '='sign
			if (!tokens[i].isEmpty() && tokens[i].equals("=")) {

				String var = tokens[i - 1].substring(5, tokens[i - 1].length() - 1); // obtains the variable name

				// locating the variable in the symbol table
				for (int index = 0; index < this.symbolTable.size(); index++) {

					SymbolTable sb = this.symbolTable.get(index);

					if (var.equals(sb.token)) { // sets the value of the variable if found
						sb.setValue(Integer.toString(value));
						if (flag == false)
							sb.setValue("");
						this.symbolTable.set(index, sb);
						break;
					}
				}
			}
			i++;
		}

	}

	/**
	 * Gets the current size of the symbol table.
	 * @return size of the symbol table
	 */
	public int getSize() {
		int dummy = 1;
		return dummy;
	}

	/**
	 * Gets the lexeme of the symbol table at the given index.
	 * @return the lexeme string of symbol table at index 'i'
	 */
	public String getTokenAt(int i) {
		String token = this.symbolTable.get(i).getToken();
		return token;
	}

	/**
	 * Gets the lexeme of the symbol table at the given index.
	 * @return the lexeme string of symbol table at index 'i'
	 */
	public String getLexemeAt(int i) {
		String dummy = this.symbolTable.get(i).getLexeme();
		return dummy;
	}

	/**
	 * Gets the type of the symbol table at the given index (for variables/identifiers).
	 * INT - integer
	 * FLOAT - float 
	 * @return the type of symbol table at index 'i'
	 */
	public String getTypeAt(int i) {
		String dummy = this.symbolTable.get(i).getType();
		return dummy;
	}

	/**
	 * Gets the type of the symbol table at the given index.
	 * @return the type of symbol table at index 'i'
	 */
	public String getValueAt(int i) {
		String dummy = this.symbolTable.get(i).getValue();
		return dummy;
	}
}
