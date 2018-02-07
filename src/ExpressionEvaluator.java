
/**
 * Name: Alvaro, Cedric	(2014-60690)	Sumandang, AJ Ruth (2014-37728)
 * Deadline Date and Time:  	January 26, 2018; 11:59 a.m.
 * 
 * Program Exercise #	: 1
 * Program Title		: Expression Evaluator
 * 
 * Program Description:
 * 		This program is able to evaluate expressions. It is also capable of storing variables and error checking. 
 * 
 * 		The program loads a file (.in file only) from the home directory chosen by the user and process it when user clicks 'process' button.
 * 		After evaluating and checking for errors, the program produces an output which both place in the output text panel and in an .out file.
 * 
 * 		Error checking includes...
 * 		- Lexical error
 * 		- Syntax error
 * 		- Evaluation error
 * 
 * [User Manual]
 * 1. Click 'Open File' in home.
 * 2. Click 'Choose File' button in the 'Open File' page to pick a file you want to process.
 * - Choose valid and existing .in file in the home.
 * - You will be redirected to the 'Output' panel if you chose a valid .in file.
 * 3. Click 'Process' button to process the loaded file.
 * 4. If changes occurred in the .in file, click 'Load File' before clicking 'Process'.
 * 
 * [Lexical Checking]
 * Each element must be separated by spaces.
 * (e.g. x = y + zinstead of x=y+z)
 * - Variable (can only start with underscore (_) or letters (a-z))
 * - Operators (add (+), substract (-), multiply (*), divide (/), remainder/module (%))
 * - Integer (whole numbers of 0-9 digits)
 * 
 * [Syntax Checking]
 * - No hanging operator (e.g. x +)
 * - No consecutive variable or operator (e.g. x y, x + - y) 
 * - Invalid left-hand side(e.g. 4 = x + y)
 * - Operator not surrounded by variable or integer (e.g x = + y / z)
 * 
 * [Evaluation Checking]
 * - Undefined variables (variables without assigned value)
 * - Undefined division (e.g. y = x / 0)
 */

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public class ExpressionEvaluator {
	public GUI gui;
	public FileHandler fileHandler = new FileHandler();
	private JFrame frame;
	private Vector<SymbolTable> symbolTables = new Vector<SymbolTable>();
	private boolean flag = true;
	private String errorMsg = new String("");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExpressionEvaluator window = new ExpressionEvaluator();
					window.gui.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExpressionEvaluator() {
		gui = new GUI();
		initializeVariables();
	}

	/**
	 * Initialize the variables for the program.
	 */
	private void initializeVariables() {

		/*
		*	Adding a new temp File and adding a tab for it with a text editor.
		*/
		Action newAction = new AbstractAction("New") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				addNewTab("*Untitled_" + (getCurrentTabIndex() + 2) + ".in");
				fileHandler.getfileHandlers().add(new CustomFileChooser("in"));
			}
		};
		newAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmNew.setAction(newAction);

		/*
		*	Open an existing file and put its content to a new text editor also adding a new tab.
		*/
		Action openAction = new AbstractAction("Open") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileHandler.chooseFile(frame)) {
					// AHJ: unimplemented; surround textpane with scrollpane
					String fileName = fileHandler.getFileName().isEmpty() ? fileHandler.getFileName()
							: "Untitled_" + (getCurrentTabIndex() + 2);
					addNewTab(fileName);
					fileHandler.loadFile();

				} else {
					if (!fileHandler.isCurrFile()) {
						JOptionPane.showMessageDialog(frame, "This file does not exist.");
					}
				}
			}
		};
		openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmOpenFile.setAction(openAction);

		/*
		*	Saving the texts written in the text editor and making an output file for it.
		*/
		Action saveAction = new AbstractAction("Save") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String title = fileHandler.saveFile(getCurrentTabText(), gui.frame);
				if (title != null) {
					gui.updateTabInfo(title);
				}
			}
		};
		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmSave.setAction(saveAction);

		/*
		*	Adding a new temp File and adding a tab for it with a text editor.
		*/
		Action closeAction = new AbstractAction("Close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int tabIndex = getCurrentTabIndex();
				String tabText = getCurrentTabText();
				if (tabIndex >= 0 && gui.closeCurrentTab(tabText)) {
					symbolTables.remove(tabIndex);
				}
			}
		};
		closeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmClose.setAction(closeAction);

	}

	/*
	*	Getting the text from the text editor as a String.
	*/
	public String getCurrentTabText() {
		int tabIndex = getCurrentTabIndex();
		if (tabIndex < 0) {
			return null;
		}
		JScrollPane sp = (JScrollPane) gui.tbpEditor.getComponentAt(tabIndex);
		Component[] comps = sp.getViewport().getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof JTextPane) {
				JTextPane textPane = (JTextPane) comps[i];
				return textPane.getText();
			}
		}
		return null;
	}

	/*
	*	Adding a new tab for multiple files to be opened simultaneously.
	*/
	private void addNewTab(String title) {
		gui.addNewTab(title);
		/*
		 * AHJ: unimplemented; there may be something wrong here; check if
		 * mawagtang ang storing the symbol table
		 */
		symbolTables.add(new SymbolTable());
	}

	/**
	 * Processes the most recently opened valid file. This function contains the
	 * lexical, syntax and semantic analyzer as well as the execution of the
	 * file
	 * 
	 * @throws IOException
	 * 
	 * @author Alvaro, Cedric Y.
	 * @author Sumandang, AJ Ruth H.
	 */
	private void process() throws IOException {
		LinkedList<String> postFix = new LinkedList<String>();
		int result = 0;
		String strPostFix = "";

		// stores the selected file and obtained a line
		FileReader selectedFile = new FileReader(fileHandler.getFileChooser().getSelectedFile().getAbsolutePath());
		fileHandler.reader = new BufferedReader(selectedFile);
		String line = fileHandler.reader.readLine();
		gui.tpEditor.setText("");

		for (int lineNum = 1; line != null; lineNum++) {
			if (line.equals("")) { // if current line read is empty
				line = fileHandler.reader.readLine();
				continue;
			}

			String lexicalString = lexicalAnalyzer(line, lineNum);
			String checker = "";

			/* if lexical error is not encountered */
			if (!lexicalString.substring(0, 3).equals("err")) {
				checker = syntaxAnalyzer(lexicalString, lineNum);

				// if syntax error encountered
				if (!checker.substring(0, 3).equals("err")) {
					// obtains the expression part (or RHS) of the line
					String expr = getRHS(lexicalString);

					postFix = toPostFix(expr);

					/* converts postfix linkedlist to string */
					strPostFix = convertToString(postFix);

					/*
					 * if RHS has undefined variables or variables with empty
					 * values
					 */
					if (!hasEmptyValues(expr, lineNum)) {
						result = evaluateExpression(postFix);
						/* AHJ: unimplemeted; (to Ced) tama akung pag-ilis? */
						/*
						 * insert index at the get(index) for the specified tab
						 * of editor
						 */
						/* store in symbol table */
						symbolTables.get(getCurrentTabIndex()).storeResult(result, lexicalString, true);
					} else { // if some variables are undefined
						checker = "Warning: Variable does not have value yet.";
					}
				}
			}

			/* display the result in the output text pane */
			// displayOutput(line, checker, strPostFix, result, lineNum);
			line = fileHandler.reader.readLine(); // reads next line
		}

		// displayAdditionalOutput();

		fileHandler.reader.close();
		fileHandler.saveFile(getCurrentTabText(), gui.frame);
		gui.updateTabInfo();
		System.out.println("Saving...");
	}

	/**
	 * Looks for any variables with no assigned values
	 * 
	 * @param expr
	 *            the line whose variables are to be checked
	 * @return true if a variable with empty value has been found; false if all
	 *         variables has values
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private boolean hasEmptyValues(String expr, int lineNum) {
		String[] tokens = expr.trim().split("\\s");

		for (int i = 0; i < tokens.length; i++) {
			/* if token is not empty and is a variable/identifier*/
			if (!tokens[i].isEmpty() && tokens[i].substring(1, 4).equals("var")) {
				String var = getAbsoluteValue(tokens[i].substring(5, tokens[i].length() - 1));
				SymbolTable sb = symbolTables.get(1).findVariable(var);
				if (sb.getValue().equals("")) { // if the variable has no value
					// stored in the symbol table
					errorMsg += "Undefined variable: " + var + " (line " + lineNum + ")\n";
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the absolute value of a number string by removing the negative sign
	 * 
	 * @param num
	 *            the number string whose absoluate value is to be retrieved
	 * @return the absolute value of the received string
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String getAbsoluteValue(String num) {
		if (num.charAt(0) == '-') {
			num = num.substring(1);
		}
		return num;
	}

	/**
	 * Returns the left-hand side of the received line (for assignment
	 * statements)
	 * 
	 * @param line
	 *            line whose LHS is to be returned
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String getLHS(String line) {
		int index = line.lastIndexOf("=");
		return line.substring(0, index + 1);
	}

	/**
	 * Returns the right-hand side of the received line (for assignment
	 * statements)
	 * 
	 * @param line
	 *            line whose RHS is to be returned
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String getRHS(String line) {
		int index = line.lastIndexOf("=");
		return line.substring(index + 1, line.length());
	}

	/**
	 * Return a string into its lexical form
	 * 
	 * @param line
	 *            the line/statement to be converted into a lexical string
	 * @return the lexical string of the received line/statement (e.g. : <var-x>
	 *         = <int-2> <op-+> <int-2>)
	 * @see wordsLoop, is a loop that iterates each word found in each line of
	 *      the .in file
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private String lexicalAnalyzer(String line, int lineNum) {

		String[] words = line.trim().split("\\s");
		String result = "";

		for (String word : words) {
			String firstLetter = "" + word.charAt(0);
			if (isOperator(word)) { // if token is an operator

				result += " <op-" + word + ">";

			} else if (isNumeric(word)) { // if token is an integer

				result += " <int-" + word + ">";

			} else if (isVariable(word)) { // if token is a variable

				result += " <var-" + word + ">";
				if (word.indexOf("+") == 0 || word.indexOf("-") == 0) {
					word = word.substring(1, word.length());
				}
				if (symbolTables.get(1).findVariable(word) == null) {
					SymbolTable st = new SymbolTable(word, "variable", "");
					st.getVector().add(st);
				}

			} else if (firstLetter.equals("=")) { // if token is an assignment
													// operator

				result += " =";

			} else { // if the token does not fall in the previous categories,
						// hence, encountering an unidentifiable symbol

				errorMsg += "Invalid symbol: " + word + " (line " + lineNum + ")\n";
				return "error: Lexical error - " + word;

			}
		}

		return result;
	}

	/**
	 * Checks the received line for syntax
	 * 
	 * @param line
	 *            lexical string/line whose syntax is to be checked
	 * @return string of either acceptance (if no error is found) or error
	 *         message of the received error
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String syntaxAnalyzer(String line, int lineNum) {
		String[] tokens = getLHS(line).trim().split("\\s");
		int index = 0;

		// checking of LHS syntax
		for (String token : tokens) {
			String nextToken = index + 1 < tokens.length ? tokens[index + 1] : "";
			if (!token.isEmpty() && token.equals("=")) { // if a '=' is located
				String var = tokens[index - 1].length() > 4 ? tokens[index - 1].substring(1, 4) : "";
				String eq = tokens[index];
				/*
				 * checks if preceding element to the assignment symbol is
				 * correct (if it's a variable)
				 */
				if (!var.equals("var") || !eq.equals("=")) {
					errorMsg += "Incorrect left-hand side element" + " (line " + lineNum + ")\n";
					return "error5: Syntax error - Incorrect left-hand side element.";
				}
				/* if operator or number is found in LHS */
			} else if (!token.isEmpty() && (token.length() > 3
					&& (token.substring(1, 4).equals("int") || token.substring(1, 3).equals("op")))) {
				errorMsg += "Incorrect left-hand side element" + " (line " + lineNum + ")\n";
				return "error5: Syntax error - Incorrect left-hand side element.";
				/* if adjacent variables found in LHS */
			} else if (!token.isEmpty() && !nextToken.isEmpty()
					&& (token.length() > 4 && token.substring(1, 4).equals("var"))
					&& (nextToken.length() > 4 && nextToken.substring(1, 4).equals("var"))) {
				errorMsg += "Incorrect left-hand side element" + " (line " + lineNum + ")\n";
				return "error5: Syntax error - Incorrect left-hand side element.";
			}
			index++;
		}

		// checking of expression/RHS syntax
		tokens = getRHS(line).trim().split("\\s");
		for (int i = 0; i < tokens.length; i++) {
			String currToken = tokens[i].substring(1, 4);
			String nextToken = "";
			String prevToken = "";

			if (currToken.isEmpty()) { // skip if token is empty
				continue;
			}

			if (i < tokens.length - 1) {
				nextToken = tokens[i + 1].length() > 4 ? tokens[i + 1].substring(1, 4) : tokens[i + 1].substring(1, 3);
			}

			if (i > 0) {
				prevToken = tokens[i - 1].length() > 4 ? tokens[i - 1].substring(1, 4) : tokens[i - 1].substring(1, 3);
			}

			if (i == tokens.length - 1) { // if end of the expression is not a
											// variable or a number
				if (!currToken.equals("int") && !currToken.equals("var")) {
					errorMsg += "Hanging operator not allowed" + " (line " + lineNum + ")\n";
					return "error1: Hanging operator not allowed";
				} else {
					return "accept";
				}
				/* if consecutive operator found */
			} else if (currToken.equals("op") && nextToken.equals("op")) {
				errorMsg += "Invalid operator" + " (line " + lineNum + ")\n";
				return "error2: Syntax error - Invalid operator";
				/* if operator not surrounded with var/num */
			} else if (currToken.equals("op") && (!(prevToken.equals("int") || prevToken.equals("var"))
					|| !(nextToken.equals("int") || nextToken.equals("var")))) {
				errorMsg += "Operator not surrounded by valid numbers/variables" + " (line " + lineNum + ")\n";
				return "error3: Syntax error - Operator not surrounded by valid numbers/variables";
				/* if adjacent variables/number found */
			} else if ((currToken.equals("var") || currToken.equals("int"))
					&& (nextToken.equals("var") || nextToken.equals("int"))) {
				errorMsg += "Consecutive order variable or number" + " (line " + lineNum + ")\n";
				return "error4: Syntax error - Consecutive order variable or number";
			}

		}
		return "accept"; // if no error has been encountered

	}

	/**
	 * Returns the postfix form of an expression
	 * 
	 * @param expr
	 *            the expression to be converted into postfix
	 * @return postfix form of the received expression
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private LinkedList<String> toPostFix(String expr) {
		Stack<String> stack = new Stack<String>();
		LinkedList<String> postFix = new LinkedList<String>();
		String[] tokens = expr.trim().split("\\s");
		for (int i = 0; i < tokens.length; i++) {

			if (tokens[i].isEmpty()) { // skip if token is empty
				continue;
			}
			// if a viable integer is encountered add to postFix
			if (tokens[i].substring(1, 4).equals("int") || tokens[i].substring(1, 4).equals("var")) {

				String number = tokens[i].substring(5, tokens[i].length() - 1);
				postFix.add(number);

				// else if an operator, then push it to the stack
			} else if (tokens[i].substring(1, 3).equals("op")) {

				String op = "" + tokens[i].charAt(4);
				if (!stack.isEmpty()) { // if stack is not empty
					while (!stack.isEmpty() && isHighOrEqualPrecedence(stack.peek(), op)) {
						// add to postfix if operator in the stack is of higher
						// or equal precedence than the current operator
						String poppedElem = stack.pop();
						postFix.add(poppedElem);
					}
				}
				stack.push(op);
			}
		}

		// popping all elements left from the stack to be added to the PostFix
		while (!stack.isEmpty()) {
			String poppedElem = stack.pop();
			postFix.add(poppedElem);
		}

		return postFix;
	}

	/**
	 * Converts linkedlist type to its string
	 * 
	 * @param linkedList linkedlist to be converted into string
	 * @return the string of the received linkedlist
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String convertToString(LinkedList<String> linkedList) {
		String strPostFix = "";

		for (int i = 0; i < linkedList.size(); i++) {
			strPostFix += linkedList.get(i) + " ";
		}

		return strPostFix;
	}

	/**
	 * Determines whether the an element is of higher or equal precedence than
	 * another element
	 * 
	 * @param firstElem the element to be determined if it is of higher or equal
	 *            precedence
	 * @param secondElem the element to be compared to the first element
	 * @return true if first element (first parameter) is higher in precedence
	 *         than second element; false if first element is lower in
	 *         precedence
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private boolean isHighOrEqualPrecedence(String firstElem, String secondElem) {
		int firstPrec = 0;
		switch (firstElem) {
		case "%":
		case "/":
		case "*":
			firstPrec = 2;
			break;
		case "+":
		case "-":
			firstPrec = 1;
			break;
		}

		int secondPrec = 0;
		switch (secondElem) {
		case "%":
		case "/":
		case "*":
			secondPrec = 2;
			break;
		case "+":
		case "-":
			secondPrec = 1;
			break;
		}

		return firstPrec >= secondPrec;
	}

	/**
	 * Evaluate the received postfix and returns its result
	 * 
	 * @param postFix postfix to be evaluated
	 * @return result of evaluating the expression
	 * 
	 * @author Sumandang, AJ Ruth H.
	 * @author Alvaro, Cedric Y.
	 */
	private int evaluateExpression(LinkedList<String> postFix) {
		Stack<String> stack = new Stack<String>();
		while (!postFix.isEmpty()) {
			String poppedElem = postFix.pop().toString();

			if (isNumeric(poppedElem)) {

				stack.push(poppedElem);
				/*
				 * if popped element from postfix is a number, push it to stack
				 */
			} else if (symbolTables.get(1).findVariable(getAbsoluteValue(poppedElem)) != null) {
				/*
				 * determines if variable in the source code is positive or not
				 */
				boolean isPositive = poppedElem.charAt(0) != '-';

				/*
				 * obtains variable name and search for it in the symbol table
				 */
				String var = isPositive ? poppedElem : poppedElem.substring(1);
				SymbolTable sb = symbolTables.get(1).findVariable(var);

				String number = sb.getValue();
				/* for single negative */
				if (!isPositive && sb.getValue().charAt(0) != '-') {
					number = "-" + sb.getValue();
					/*
					 * for double negative note: this is done to avoid having
					 * double negative sign that causes error in the code
					 */
				} else if (!isPositive && sb.getValue().charAt(0) == '-') {
					number = sb.getValue().substring(1);
				}
				stack.push(number);
				/*if popped element from postfix is encountered, pop two element from stack and evaluate based on the popped operator*/
			} else {
				int secondElem = Integer.parseInt(stack.pop());
				int firstElem = Integer.parseInt(stack.pop());

				int result = 0;
				/* Execution of the operators with the popped elements */
				switch (poppedElem) {
				case "+":
					result = firstElem + secondElem;
					flag = true;
					break;
				case "-":
					result = firstElem - secondElem;
					flag = true;
					break;
				case "*":
					result = firstElem * secondElem;
					flag = true;
					break;
				case "/":
					try {
						result = firstElem / secondElem;
					} catch (ArithmeticException e) {
						System.out.println("Divided a zero as a denominator.");
						flag = false;
					}
					break;
				case "%":
					result = firstElem % secondElem;
					flag = true;
					break;
				}

				/*push result to stack*/
				stack.push(Integer.toString(result));

			}
		}

		/*pop the remaining element and return it as the answer/result*/
		int answer = Integer.parseInt(stack.pop());
		return answer;
	}

	/**
	 * Determines whether the received word is a valid variable or not
	 * 
	 * @param word word to be checked
	 * @return true if word is a valid variable; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean isVariable(String word) {
		String firstLetter = "" + word.charAt(0);
		if (word.length() > 1
				? !firstLetter.matches("[0-9]+") && word.substring(1, word.length()).matches("[a-zA-Z0-9_ ]+")
				: word.matches("[a-zA-Z]+")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Determines whether the received word is an operator or not
	 * 
	 * @param word word to be checked
	 * @return true if word is an operator; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private boolean isOperator(String word) {
		String operator = new String("+-/*%");

		return word.length() == 1 && operator.contains(word);
	}

	/**
	 * Determines whether received element is numeric or not
	 * 
	 * @param check the element to be checked
	 * @return true if the received element is numeric; false if not
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public boolean isNumeric(String check) {
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
	 * Obtains index of the current opened tab
	 * 
	 * @return index of current opened tab; returns negative if tabbed pane has no more tab
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private int getCurrentTabIndex() {
		return gui.tbpEditor.getSelectedIndex();
	}
}