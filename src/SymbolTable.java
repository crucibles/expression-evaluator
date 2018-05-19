import java.util.Vector;

/**
 * Class that represent an instance of a symboltable
 * 
 * @author Alvaro, Cedric Y.
 */
public class SymbolTable {
	private Vector<Entry> symbolTable;

	/*
	 * The three constructors for the symbolTable
	 */
	public SymbolTable() {
		this.symbolTable = new Vector<Entry>();
	}

	public void add(String lexeme, String token, String type, String value) {
		Entry newEntry = new Entry(lexeme, token, type, value);
		this.symbolTable.add(newEntry);
	}

	public void add(String lexeme, String token, String type) {
		Entry newEntry = new Entry(token, "", "");
		this.symbolTable.add(newEntry);
	}

	public void clear() {
		this.symbolTable.clear();
	}

	/**
	 * Gets the vector of symbolTable
	 */
	public Vector<Entry> getVector() {
		return this.symbolTable;
	}

	/**
	 * Locates the received variable in the symbol table
	 * 
	 * @param var
	 *            variable to be searched in the symbol table
	 * 
	 * @return symbol table containing the received variable; null if not found
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public Entry findVariable(String var) {
		for (int index = 0; index < this.symbolTable.size(); index++) {
			String sb = this.symbolTable.get(index).getLexeme();
			if (var.equals(sb)) { // returns symbol table if its token
									// matches the received variable
				return this.symbolTable.get(index);
			}
		}
		return null; // returns null if variable not found
	}

	/**
	 * Stores value of variables in the LHS (left-hand side) in the symbol
	 * table.
	 * 
	 * @param value
	 *            value to be stored to the variable
	 * @param stmt
	 *            statement that contains the variable whose value is to be
	 *            stored
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void storeResult(String variable, String value, String type) {
		// AHJ: optimization unimplemented; since Ced has tried looking for
		// duplicate variables; why not use a separate function for this
		// and just return a null if not found

		// locating the variable in the symbol table
		for (int index = 0; index < this.symbolTable.size(); index++) {
			String sb = this.symbolTable.get(index).getLexeme();
			
			// if found, set value & type
			if(sb.equals(variable)){
				Entry e = new Entry(variable, "IDENT", type, value);
				this.symbolTable.set(index, e);
			}
		}
	}

	/**
	 * Gets the current size of the symbol table.
	 * 
	 * @return size of the symbol table
	 */
	public int getSize() {
		int size = this.symbolTable.size();
		return size;
	}

	/**
	 * Gets the lexeme of the symbol table at the given index.
	 * 
	 * @return the lexeme string of symbol table at index 'i'
	 */
	public String getTokenAt(int i) {
		String token = this.symbolTable.get(i).getToken();
		return token;
	}

	/**
	 * Gets the lexeme of the symbol table at the given index.
	 * 
	 * @return the lexeme string of symbol table at index 'i'
	 */
	public String getLexemeAt(int i) {
		String dummy = this.symbolTable.get(i).getLexeme();
		return dummy;
	}

	/**
	 * Gets the type of the symbol table at the given index (for
	 * variables/identifiers). INT - integer FLOAT - float
	 * 
	 * @return the type of symbol table at index 'i'
	 */
	public String getTypeAt(int i) {
		String dummy = this.symbolTable.get(i).getType();
		return dummy;
	}

	/**
	 * Gets the type of the symbol table at the given index.
	 * 
	 * @return the type of symbol table at index 'i'
	 */
	public String getValueAt(int i) {
		String dummy = this.symbolTable.get(i).getValue();
		return dummy;
	}
}

class Entry {
	private String lexeme = "";
	private String token = "";
	private String type = "";
	private String value = "";

	/*
	 * The three constructors for the symbolTable
	 */
	public Entry() {

	}

	public Entry(String lexeme, String token, String type, String value) {
		this.lexeme = lexeme;
		this.token = token;
		this.type = type;
		this.value = value;
	}

	public Entry(String lexeme, String token, String type) {
		this.token = token;
		this.type = "";
		this.value = "";
	}

	/**
	 * Gets the token or name of the symbol.
	 * 
	 * @return String token name
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getLexeme() {
		return trimString(this.lexeme);
	}

	/**
	 * Gets the token or name of the symbol.
	 * 
	 * @return String token name
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getToken() {
		return trimString(this.token);
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

	public void setValue(String value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Trims both ends of the received string.
	 * @param word trimmed string of the word
	 */
	private String trimString(String word){
		int i = 0;
		while(i < word.length() && Character.isWhitespace(word.charAt(i))){
			i++;
		}
		word = word.substring(i);
		
		i = word.length() - 1;
		while(i >= 0 && Character.isWhitespace(word.charAt(i))){
			i--;
		}
		word = word.substring(0, i + 1);
		return word;
	}
}
