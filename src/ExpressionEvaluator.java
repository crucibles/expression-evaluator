
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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

public class ExpressionEvaluator{
	public GUI gui;

	private JFrame frame;

	private boolean flag = true;
	private BufferedReader reader;
	private JFileChooser fileChooser = new JFileChooser();
	private Vector<SymbolTable> symbolTable = new Vector<SymbolTable>();
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
		Action newAction = new AbstractAction("New") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				gui.tbpEditor.addTab("*New.in", new JTextPane());
				int lastIndex = gui.tbpEditor.getTabCount() - 1;
				gui.tbpEditor.setSelectedIndex(lastIndex);
			}
		};
		newAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmNew.setAction(newAction);
		
		Action openAction = new AbstractAction("Open") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseFile()){
					loadFile();
				}
			}
		};
		openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmOpenFile.setAction(openAction);
		
		Action saveAction = new AbstractAction("Save") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
				System.out.println("Saving...");
			}
		};
		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmSave.setAction(saveAction);
		
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

	private void saveFile(){
		int selectedIndex = gui.tbpEditor.getSelectedIndex();
		String title = gui.tbpEditor.getTitleAt(selectedIndex);
		if(title.charAt(0)== '*'){
			gui.tbpEditor.setTitleAt(selectedIndex, title.substring(1));
		}
	}

	

	/**
	 * Choose file from the user's home directory. Checks if file exists
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private boolean chooseFile() {
		int file = fileChooser.showOpenDialog(frame);
		if (file == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			if (selectedFile.isFile()) {
				//AHJ: unimplemented; surround textpane with scrollpane
				
				gui.tbpEditor.addTab(selectedFile.getName(), new JTextPane());
				return true;
			} else {
				JOptionPane.showMessageDialog(frame, "This file does not exist.");
				return false;
			}
		} else {
			return false;			
		}
	}

	/**
	 * Load the file of the given url
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private void loadFile() {
		gui.tpEditor.setText("");
		errorMsg = "";
		symbolTable.clear();
		if (getFileExtension(getFileName()).equals("in")) {
			flag = true;
			System.out.println("loading a valid file...");
		}
	}

	/**
	 * 
	 * Gets the name of the file selected.
	 * 
	 * @return name of the file received
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private String getFileName() {
		return fileChooser.getSelectedFile().getName();
	}

	/**
	 * Gets the extension of the file selected.
	 * 
	 * @return file's extension (.e.g. in (file.in), out (file.out))
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1, fileName.length());
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
		FileReader selectedFile = new FileReader(fileChooser.getSelectedFile().getAbsolutePath());
		reader = new BufferedReader(selectedFile);
		String line = reader.readLine();
		gui.tpEditor.setText("");

		for (int lineNum = 1; line != null; lineNum++) {
			if (line.equals("")) { // if current line read is empty
				line = reader.readLine();
				continue;
			}

			String lexicalString = lexicalAnalyzer(line, lineNum);
			String checker = "";
			if (!lexicalString.substring(0, 3).equals("err")) { // if lexical
																// error
																// encountered
				checker = syntaxAnalyzer(lexicalString, lineNum);

				if (!checker.substring(0, 3).equals("err")) { // if syntax error
																// encountered
					String expr = getRHS(lexicalString); // obtains the
															// expression part
															// (or RHS) of the
															// line

					postFix = toPostFix(expr);
					strPostFix = convertToString(postFix); // converts postfix
															// linkedlist to
															// string
					if (!hasEmptyValues(expr, lineNum)) { // if RHS has
															// undefined
															// variables or
															// variables with
															// empty values
						result = evaluateExpression(postFix);
						storeResult(result, lexicalString); // store result in
															// symbol table
					} else { // if some variables are undefined
						checker = "Warning: Variable does not have value yet.";
					}
				}
			}

			displayOutput(line, checker, strPostFix, result, lineNum); // displays
																		// the
																		// result
																		// in
																		// the
																		// 'Output'
																		// text
																		// pane
			line = reader.readLine(); // reads next line
		}

		displayAdditionalOutput();

		reader.close();
		createOutFile(gui.tpEditor.getText()); // create the .out file
	}

	/**
	 * Displays the resulting output in the panel.
	 * 
	 * @param line
	 *            the current line being evaluated
	 * @param checker
	 *            the error in the result (if the source code has errors)
	 * @param strPostFix
	 *            the postfix string of the expression (if no errors has been
	 *            found in the source code)
	 * @param result
	 *            the result of the expression (if no errors has been found in
	 *            the source code)
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void displayOutput(String line, String checker, String strPostFix, int result, int lineNum) {
		StyledDocument doc = gui.tpEditor.getStyledDocument();
		try {
			// outputs the line read
			String outputLine = "Line" + lineNum + ": " + line + "\n";
			doc.insertString(doc.getLength(), outputLine, null);

			if (checker.substring(0, 3).equals("err")) { // if error has been
															// detected

				// outputs the error that had occurred
				outputLine = "Result: " + checker + "\n\n";
				doc.insertString(doc.getLength(), outputLine, null);

			} else { // if no errors found in the source code
				outputLine = "Postfix: " + getLHS(line) + " " + strPostFix + "\n"; // postfix
				doc.insertString(doc.getLength(), outputLine, null);

				if (checker.substring(0, 4).equals("Warn")) {
					outputLine = checker + "\n\n"; // warning (for lines
													// containing undefined
													// variables)
				} else {
					outputLine = "Result: " + getLHS(line) + " " + result + "\n\n"; // result/answer
					if (!flag) {
						outputLine = "Result: " + getLHS(line) + " " + "undefined" + "\n\n"; // if
																								// result
																								// is
																								// undefined
																								// (for
																								// zero
																								// divisors)
						flag = true;
					}
				}
				doc.insertString(doc.getLength(), outputLine, null);

			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Display addition output such as variables declared and errors encountered
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void displayAdditionalOutput() {
		StyledDocument doc = gui.tpEditor.getStyledDocument();
		try {
			String outputLine = "-------------------------\n" + "Variables Used:\n";
			doc.insertString(doc.getLength(), outputLine, null);

			for (int i = 0; i < symbolTable.size(); i++) {
				outputLine = symbolTable.get(i).token + "\n";
				doc.insertString(doc.getLength(), outputLine, null);
			}

			outputLine = "-------------------------\n" + "Errors found:\n";
			doc.insertString(doc.getLength(), outputLine, null);

			outputLine = !errorMsg.equals("") ? errorMsg + "\n" : "No errors encountered\n";
			doc.insertString(doc.getLength(), outputLine, null);

		} catch (Exception e) {
			System.out.println(e);
		}
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
	private void storeResult(int value, String stmt) {
		// AHJ: optimizstion unimplemented; since Ced has tried looking for
		// duplicate variables; why not use a separate function for this
		// and just return a null if not found
		String tokens[] = stmt.split("\\s");

		for (int i = 0; i < tokens.length; i++) {
			if (!tokens[i].isEmpty() && tokens[i].equals("=")) { // locates the
																	// LHS
																	// variables
																	// by
																	// locating
																	// the '='
																	// sign

				String var = tokens[i - 1].substring(5, tokens[i - 1].length() - 1); // obtains
																						// the
																						// variable
																						// name
				for (int index = 0; index < symbolTable.size(); index++) { // locating
																			// the
																			// variable
																			// in
																			// the
																			// symbol
																			// table
					SymbolTable sb = symbolTable.get(index);
					if (var.equals(sb.token)) { // sets the value of the
												// variable if found
						sb.setValue(sb, Integer.toString(value));
						if (flag == false)
							sb.setValue(sb, "");
						symbolTable.set(index, sb);
						break;
					}
				}
			}
			i++;
		}

	}

	/**
	 * Creates the .out file of the resulting output
	 * 
	 * @param output
	 *            the text to be stored in the .out file
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public void createOutFile(String output) throws IOException {
		Writer writer = null;

		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(getFileName().replace(".in", ".out")), "utf-8"));
			writer.write(output);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * Locates the received variable in the symbol table
	 * 
	 * @param var
	 *            variable to be searched in the symbol table
	 * @return symbol table containing the received variable; null if not found
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private SymbolTable findVariable(String var) {
		for (int index = 0; index < symbolTable.size(); index++) { // loops
																	// through
																	// the
																	// symbol
																	// table
			SymbolTable sb = symbolTable.get(index);
			if (var.equals(sb.token)) { // returns symbol table if its token
										// matches the received variable
				return sb;
			}
		}
		return null; // returns null if variable not found
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
			if (!tokens[i].isEmpty() && tokens[i].substring(1, 4).equals("var")) { // if
																					// the
																					// token
																					// is
																					// not
																					// empty
																					// and
																					// is
																					// a
																					// variable/identifier
				String var = getAbsoluteValue(tokens[i].substring(5, tokens[i].length() - 1));
				SymbolTable sb = findVariable(var);
				if (sb.value.equals("")) { // if the variable has no value
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
				if (findVariable(word) == null) {
					SymbolTable st = new SymbolTable(word, "variable", "");
					symbolTable.add(st);
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
				if (!var.equals("var") || !eq.equals("=")) { // checks if a
																// preceding
																// element to
																// the
																// assignment
																// symbol is
																// correct (if
																// it's a
																// variable)
					errorMsg += "Incorrect left-hand side element" + " (line " + lineNum + ")\n";
					return "error5: Syntax error - Incorrect left-hand side element.";
				}
			} else if (!token.isEmpty() && (token.length() > 3
					&& (token.substring(1, 4).equals("int") || token.substring(1, 3).equals("op")))) { // if
																										// operator
																										// or
																										// number
																										// found
																										// in
																										// LHS
				errorMsg += "Incorrect left-hand side element" + " (line " + lineNum + ")\n";
				return "error5: Syntax error - Incorrect left-hand side element.";
			} else if (!token.isEmpty() && !nextToken.isEmpty()
					&& (token.length() > 4 && token.substring(1, 4).equals("var"))
					&& (nextToken.length() > 4 && nextToken.substring(1, 4).equals("var"))) { // if
																								// adjacent
																								// variables
																								// found
																								// in
																								// LHS
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
			} else if (currToken.equals("op") && nextToken.equals("op")) { // if
																			// consecutive
																			// operator
																			// found
				errorMsg += "Invalid operator" + " (line " + lineNum + ")\n";
				return "error2: Syntax error - Invalid operator";
			} else if (currToken.equals("op") && (!(prevToken.equals("int") || prevToken.equals("var"))
					|| !(nextToken.equals("int") || nextToken.equals("var")))) { // if
																					// operator
																					// not
																					// surrounded
																					// with
																					// var/num
				errorMsg += "Operator not surrounded by valid numbers/variables" + " (line " + lineNum + ")\n";
				return "error3: Syntax error - Operator not surrounded by valid numbers/variables";
			} else if ((currToken.equals("var") || currToken.equals("int")) // if
																			// adjacent
																			// variables/number
																			// found
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
			if (tokens[i].substring(1, 4).equals("int") || tokens[i].substring(1, 4).equals("var")) { // if
																										// integer
																										// encountered,
																										// add
																										// to
																										// postfix

				String number = tokens[i].substring(5, tokens[i].length() - 1);
				postFix.add(number);

			} else if (tokens[i].substring(1, 3).equals("op")) { // if operator
																	// encountered,
																	// push it
																	// to the
																	// stack
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

		while (!stack.isEmpty()) { // pop all of the remaining elements in the
									// stack and add it to the postfix
			String poppedElem = stack.pop();
			postFix.add(poppedElem);
		}

		return postFix;
	}

	/**
	 * Converts linkedlist type to its string
	 * 
	 * @param linkedList
	 *            linkedlist to be converted into string
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
	 * @param firstElem
	 *            the element to be determined if it is of higher or equal
	 *            precedence
	 * @param secondElem
	 *            the element to be compared to the first element
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
	 * @param postFix
	 *            postfix to be evaluated
	 * @return result of evaluating the expression
	 * 
	 * @author Sumandang, AJ Ruth H.
	 * @author Alvaro, Cedric Y.
	 */
	private int evaluateExpression(LinkedList<String> postFix) {
		Stack<String> stack = new Stack<String>();
		while (!postFix.isEmpty()) {
			String poppedElem = postFix.pop().toString();
			if (isNumeric(poppedElem)) { // if popped element from postfix is a
											// number, push it to stack
				stack.push(poppedElem);
			} else if (findVariable(getAbsoluteValue(poppedElem)) != null) { // if
																				// popped
																				// element
																				// from
																				// postfix
																				// is
																				// a
																				// number,
																				// push
																				// it
																				// to
																				// stack
				boolean isPositive = poppedElem.charAt(0) != '-'; // determines
																	// if
																	// variable
																	// in the
																	// source
																	// code is
																	// positive
																	// or not

				// obtains variable name and search for it in the symbol table
				String var = isPositive ? poppedElem : poppedElem.substring(1);
				SymbolTable sb = findVariable(var);

				String number = sb.value;
				if (!isPositive && sb.value.charAt(0) != '-') { // for single
																// negative
					number = "-" + sb.value;
				} else if (!isPositive && sb.value.charAt(0) == '-') { // for
																		// double
																		// negative
					// note: this is done to avoid having double negative sign
					// that causes error in the code
					number = sb.value.substring(1); // removed negative sign
													// (double negative =
													// positive)
				}
				stack.push(number);
			} else { // if popped element from postfix is encountered, pop two
						// element from stack and evaluate based on the popped
						// operator
				int secondElem = Integer.parseInt(stack.pop());
				int firstElem = Integer.parseInt(stack.pop());

				int result = 0;
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
				stack.push(Integer.toString(result)); // push the result into
														// the stack

			}
		}

		int answer = Integer.parseInt(stack.pop()); // pop the remaining element
													// and return it as the
													// answer/result
		return answer;
	}

	/**
	 * Determines whether the received word is a valid variable or not
	 * 
	 * @param word
	 *            word to be checked
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
	 * @param word
	 *            word to be checked
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
	 * @param check
	 *            the element to be checked
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
}