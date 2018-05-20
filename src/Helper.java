import java.util.ArrayList;

/**
 * 
 */

/**
 * Class that contains functions that are useful to most or all of the other
 * classes.
 * 
 * @author Alvaro, Cedric Y.
 * @author Sumandang, AJ Ruth H.
 *
 */
public class Helper {

	public Helper() {

	}

	/**
	 * Counts the character found in a string.
	 * 
	 * @param x
	 *            the string to look for the searched character
	 * @param y
	 *            the character to be searched in the string
	 * @return the index where the character is found
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public int CharCounter(String x, Character y) {
		int counter = 0;
		for (int i = 0; i < x.length(); i++) {
			if (x.charAt(i) == y) {
				counter++;
			}
		}
		return counter;
	}

	public boolean isConditional(String input) {

		if (input.equals("IF")) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if the received word is a logical operator or not.
	 * 
	 * @param word
	 *            the word to be checked
	 * @return true if word is logical operator; false if not
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public boolean isLogicalOp(String word) {
		String[] logicalOperators = { "AND?", "OR?", "NOT?" };
		for (int i = 0; i < logicalOperators.length; i++) {
			if (word.equals(logicalOperators[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the received word is a mathematical operator or not.
	 * 
	 * @param word
	 *            the word to be checked
	 * @return true if word is mathematical operator; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean isOperator(String input) {
		String[] operators = { "ADD", "SUB", "MULT", "DIV", "MOD" };

		for (int i = 0; i < operators.length; i++) {
			if (input.equals(operators[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean isRelational(String input) {
		String[] operators = { "GT?", "GTE?", "LT?", "LTE?", "EQ?", "NEQ?", "AND?", "OR?", "NOT?" };

		for (int i = 0; i < operators.length; i++) {
			if (input.equals(operators[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if obtained word is an operand or not.
	 * 
	 * @param word
	 *            word to be checked
	 * @return true if word is operand; false if not
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public boolean isOperand(String word) {
		String[] operands = { "IDENT", "INT_LIT", "FLOAT_LIT" };
		for (int i = 0; i < operands.length; i++) {
			if (word.equals(operands[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether received element is float point number or not
	 * 
	 * @param check
	 *            the element to be checked
	 * @return true if the received element is float point number; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean isFloat(String check) {
		String number = new String("0123456789.");
		if (check.indexOf('.') != check.lastIndexOf('.')) {
			return false;
		} else {
			for (int i = 0; i < check.length(); i++) {
				String symbol = "" + check.charAt(i);
				if (i == 0 && (symbol.equals("-") || symbol.equals("+")) && check.length() > 1) {
					continue;
				} else if (!number.contains(symbol)) {
					return false;
				}
			}
			return check != null && true;
		}

	}

	/**
	 * Determines whether received element is numeric or not
	 * 
	 * @param check
	 *            the element to be checked
	 * @return true if the received element is numeric; false if not
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public boolean isInteger(String check) {
		String number = new String("0123456789");
		for (int i = 0; i < check.length(); i++) {
			String symbol = "" + check.charAt(i);
			if (i == 0 && (symbol.equals("-") || symbol.equals("+")) && check.length() > 1) {
				continue;
			} else if (!number.contains(symbol)) {
				return false;
			}
		}
		return check != null && true;
	}

	/**
	 * Returns the splitted version of the string
	 * 
	 * @return array of inputted words
	 * @author Alvaro, Cedric Y.
	 */
	public String[] splitter(String input) {
		String inputted = input.trim();
		ArrayList<String> tmpArr = new ArrayList<String>();
		String word = "";

		for (int i = 0; i < inputted.length(); i++) {
			if (i == inputted.length() - 1 || (!Character.isWhitespace(inputted.charAt(i)) && !word.equals("ln"))) {
				word += inputted.charAt(i);
			} else {
				if (!word.isEmpty() && !word.equals("ln")) {
					tmpArr.add(word);
				}
				word = "";
			}
		}
		if (!word.isEmpty() && !word.equals("ln")) {
			tmpArr.add(word);
		}

		String[] splitted = tmpArr.toArray(new String[tmpArr.size()]);
		System.out.println(input);
		System.out.println("_________");
		System.out.println(splitted.length);
		System.out.println(input.split("//s").length);
		System.out.println("_________");
		return splitted;
	}

	/**
	 * Returns the splitted version of the string
	 * 
	 * @return array of inputted words
	 * @author Alvaro, Cedric Y.
	 */
	public String[] splitter(String input, Character ch) {
		String inputted = input.trim();
		ArrayList<String> tmpArr = new ArrayList<String>();
		String word = "";

		for (int i = 0; i < inputted.length(); i++) {
			if(ch != null && ch == inputted.charAt(i)){
				System.out.println("FOUND");
			}
			if (i == inputted.length() - 1 || ch != inputted.charAt(i)) {
				word += inputted.charAt(i);
			} else {
				if (!word.isEmpty() && !word.equals("ln")) {
					tmpArr.add(word);
				}
				word = "";
			}
		}
		if (!word.isEmpty() && !word.equals("ln")) {
			tmpArr.add(word);
		}

		String[] splitted = tmpArr.toArray(new String[tmpArr.size()]);
		return splitted;
	}
}
