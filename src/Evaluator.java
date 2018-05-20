import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Stack;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import java.awt.Dimension;

/**
 * This class evaluates the received string.
 * 
 * @author Sumandang, AJ Ruth H.
 *
 */
public class Evaluator {
	// for GUI
	private String output = "";
	private JFrame frame;
	private JTextField txtInput;
	private JTextPane txtOutput = new JTextPane();

	// for operations
	private Operand variable;
	private Stack<Operand> stack = new Stack<Operand>();
	private Stack<Operation> pendingStack = new Stack<Operation>();
	private Helper helper = new Helper();

	// for source inputs
	private ArrayList<String[]> parsedWords = new ArrayList<String[]>();
	private ArrayList<String[]> sourceWords = new ArrayList<String[]>();
	private ExpressionEvaluator exprEval;
	private String fileName;
	private int numOfErrors;
	private SymbolTable st;

	// for flags (checking whether to execute, where to execute, etc.)
	private Integer[] lastIndex = { -1, -1, null, null};
	private Integer[] lastLoopIteration = { -1, -1};
	private boolean isPaused = false;
	private boolean isLoopOngoing = false;

	/**
	 * Constructor for Evaluator.
	 * 
	 * @param sourceString
	 *            source string (code string)
	 * @param parsedString
	 *            parsed string (obtained from syntax)
	 * @param file
	 *            file name
	 * @param errors
	 *            number of errors
	 * @param symbolTable
	 *            current symbol table used by the program
	 * @param exprEval
	 *            the instance of the caller class (ExpressionEvaluator)
	 */
	public Evaluator(String sourceString, String parsedString, String file, int errors, SymbolTable symbolTable,
			ExpressionEvaluator exprEval) {
		// initialize GUI
		initialize();

		// intro
		output += fileName + "compiled with " + numOfErrors
				+ " error(s) found. Program test will now be executed...\n\n";
		output += "SNuBL Executition: \n\n";

		// split code to words
		String[] srcStmt = sourceString.trim().split("\n");
		String[] prsStmt = parsedString.trim().split("\n");
		for (int i = 0; i < srcStmt.length; i++) {
			sourceWords.add(srcStmt[i].trim().split("\\s"));
			parsedWords.add(prsStmt[i].trim().split("\\s"));
		}		
		// store received params
		fileName = file;
		numOfErrors = errors;
		st = symbolTable;
		this.exprEval = exprEval;

		// evaluate
		setStart();
		evaluate(lastIndex[0], lastIndex[1], null, null);
	}

	/**
	 * Sets the starting indexes of the execution (by finding command).
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void setStart() {
		for (int i = 0; i < parsedWords.size(); i++) {
			String[] prsStmt = parsedWords.get(i);

			for (int j = 0; j < prsStmt.length; j++) {
				String currentWord = prsStmt[j];
				if (currentWord.equals("COMMAND")) {
					setLastIndex(i, j, null, null);
					return;
				}
			}
		}
	}

	/**
	 * Evaluates the parsed string.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private boolean evaluate(int row, int column, Integer rowEnd, Integer colEnd) {
		int j = column;
		System.out.println("-Entered Eval-");
		for (int i = row; i < parsedWords.size(); i++) {
			String[] srcStmt = sourceWords.get(i);
			String[] prsStmt = parsedWords.get(i);

			// stores the value of column if available; for call
			j = j < 0 ? prsStmt.length - 1 : column;
			if (prsStmt.length - 1 < j) {
				j = -1;
				continue;
			}

			int index = colEnd == null ? getIndexes("FROM", i, j, 0, false)[1] : -1;
			int ifIndex = colEnd == null ? getIndexes("IF", i, j, 0, false)[1] : -1;
			j = index >= 0 ? index : j;
			j = ifIndex >= 0 ? ifIndex : j;
			if (colEnd != null && rowEnd == i) {
				j = colEnd;
			}
			
			// stops at start if loop or if statements is currently executed
			// it must not include words before 'start' (conditions, bool expr)
			// else program stops the end of the stmt (index 0 or leftmost)
			int start = colEnd != null && row == i && column >= 0 ? column : 0;
			
			while (j >= start) {
				System.out.println("c:" + prsStmt[j]);
				System.out.println("s:" + srcStmt[j]);
				String currentWord = prsStmt[j];
				String sourceWord = srcStmt[j];
				evaluateExpression(currentWord, sourceWord);
				j--;
			}

			emptyPendingOperation();

			if (isPaused) { // pauses when BEG operation has been encountered
				System.out.println("isentered?");
				if (index < 0) {
					// set to i + 1 since the current stmt is finished;
					// set to -1 so checking of string starts at the end
					System.out.println("1isentered?");
					setLastIndex(i + 1, -1, null, null);
				} else {
					System.out.println("2isentered?");
					setLastIndex(i, index, null, null); // if contains FROM loop, set
											// last index to i and index
											// (location of from loop)
				}
				return false;
			}

			if (index >= 0) {
				// if contains FROM loop, set last index
				// to i and index (location of from
				// loop);
				if(!executeLoop(i, index)){
					return false;
				}
										
				i = lastIndex[0];
				j = lastIndex[1];
			}
			if (ifIndex >= 0) {
				// if "IF" keyword found in statement
				executeIfStatement(i, ifIndex);
				i = lastIndex[0];
				j = lastIndex[1];
			}
			if (rowEnd != null && i >= rowEnd) {
				return true;
			}
			j = -1;
		}
		if (rowEnd == null && !isLoopOngoing) {
			output += "Program terminated successfully...";
			txtOutput.setText(output);
		}
		return true;
	}

	/**
	 * Executes the if statement.
	 * 
	 * @param i
	 * @param j
	 */
	private void executeIfStatement(int i, int j) {
		int elseIndex = getIndexes("ELSE", i, j, 0, false)[1];
		elseIndex = elseIndex >= 0 ? elseIndex - 1 : parsedWords.get(i).length - 1;
		while (elseIndex > j) {
			String[] ps = parsedWords.get(i);
			String currentWord = ps[elseIndex];
			String sourceWord = sourceWords.get(i)[elseIndex];
			evaluateExpression(currentWord, sourceWord);
			elseIndex--;
		}
		Operand condition = stack.pop();

		if (condition.bool) {
			emptyPendingOperation();
		} else {
			pendingStack.removeAllElements();
		}


		// evaluate
		Integer[] startIndex = condition.bool ? null : getIndexes("ELSE", i, j, 1, true);
		int row = startIndex != null ? startIndex[0] : i + 1;
		int column = startIndex != null ? startIndex[1] : -1;

		Integer[] endIndex = condition.bool ? getIndexes("ELSE", i, j, -1, true) : getIndexes("ENDIF", i, j, -1, true);
		int rowEnd = endIndex[0];
		int columnEnd = endIndex[1];

		System.out.println(row);
		System.out.println(column);
		System.out.println(rowEnd);
		System.out.println(columnEnd);
		System.out.println("_________");

		evaluate(row, column, rowEnd, columnEnd);

		endIndex = getIndexes("ENDIF", i, j, 1, true);
		setLastIndex(endIndex[0], endIndex[1], null, null);
	}

	/**
	 * Empties the pending operation in the stack.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void emptyPendingOperation() {
		while (!pendingStack.isEmpty()) {
			Operation operation = pendingStack.pop();
			executeOutput(operation.operation, operation.op1, operation.op2);
			if (isPaused) {
				return;
			}
		}
	}
	
	/**
	 * Executes output operation. (output operations touches the GUI execution
	 * window)
	 * 
	 * @param operation
	 *            operation to operate on the operand
	 * @param op1
	 *            first operand
	 * @param op2
	 *            second operand (if available)
	 */
	private void executeOutput(String operation, Operand op1, Operand op2) {
		if (operation.equals("NEWLN")) {
			output += "\n";
		} else if (operation.equals("BEG")) {
			begInput(op1);
			isPaused = true;
		} else if (operation.equals("PRINT")) {
			output += print(op1);
		} else if (operation.equals("INTO")) {
			String value = op2.type.equals("FLOAT") ? op2.getStrFloatValue() : op2.getStrIntValue();
			st.storeResult(op1.sourceName, value, op2.type);
		}
	}
	
	/**
	 * Asks for user input. Enables the input textfield, allowing the user to
	 * type.
	 * 
	 * @param op
	 *            operand in which the input will be stored
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void begInput(Operand op) {
		variable = op;
		output += "Input for " + op.sourceName + ": ";
		txtOutput.setText(output);
		txtInput.setText("");
		txtInput.requestFocusInWindow();
		txtInput.setEditable(true);
		txtInput.setEnabled(true);
	}

	/**
	 * Stores input after user enters an input. Includes type casting and input
	 * error checking.
	 * 
	 * @return the string to be added to the output
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String begInput() {
		String result = txtInput.getText();
		txtInput.setEditable(false);
		txtInput.setEnabled(false);

		isPaused = false;
		if (variable.type.equals("STR")) {
			variable.setValue(result);
			this.storeToSymbolTable(variable.sourceName, variable);
			result = variable.getStrFloatValue();
			System.out.println("<<<STORED " + result + "|" + variable.sourceName);
		} else if (helper.isFloat(result) || helper.isInteger(result)) {
			Integer intVal = Math.round(Float.parseFloat(result));
			variable.setValue(variable.type.equals("INT") ? intVal.toString() : result);
			this.storeToSymbolTable(variable.sourceName, variable);
			result = variable.getStrFloatValue();
			System.out.println("<<<STORED " + result + "|" + variable.sourceName);
		} else {
			result = "ERROR: Invalid input '" + result + "' for type " + variable.type + ".";

		}
		return result;
	}
	
	/**
	 * Gets the obtained operand to be printed.
	 * 
	 * @param op1
	 *            the operand to be printed.
	 * @return string to be printed (or placed in the execution GUI window)
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private String print(Operand op1) {
		String output = "Print: ";
		if (op1.isIdent) {
			Entry e = st.findVariable(op1.sourceName);
			op1.setValue(e.getValue());
		}
		
		switch (op1.type) {
		case "INT":
		case "INT_LIT":
			output += Math.round(op1.getNumberValue());
			break;
		case "FLOAT":
		case "FLOAT_LIT":
		case "STR":
			output += op1.getStrFloatValue();
			break;
		case "BOOL":
			output += op1.bool;
		}
		System.out.println("printing " + op1.sourceName + "<" + output + ">");
		return output + "\n";
	}

	/**
	 * Evaluates based on the operation/operand received.
	 * 
	 * @param currentWord
	 *            the operation/operand
	 * @param sourceWord
	 *            the name of the operation/operand
	 */
	private void evaluateExpression(String currentWord, String sourceWord) {
		if (currentWord.equals("NEWLN") || currentWord.equals("BEG") || currentWord.equals("PRINT")
				|| currentWord.equals("INTO")) {
			Operand op1 = null;
			Operand op2 = null;
			if (currentWord.equals("INTO")) {
				op1 = stack.pop();
				op2 = stack.pop();
			} else if (currentWord.equals("BEG") || currentWord.equals("PRINT")) {
				op1 = stack.pop();
			}
			pendingStack.push(new Operation(currentWord, op1, op2));
		} else if (helper.isOperator(currentWord) && stack.size() > 1) {
			if (!solveMath(currentWord)) {
				output += "ERROR: Invalid operand!\n";
			}
		} else if (helper.isRelational(currentWord) && stack.size() > 1) {
			compareRelation(currentWord);
		} else if (helper.isLogicalOp(currentWord) && stack.size() > 0) {
			compareLogic(currentWord);
		} else if (currentWord.equals("IS")) {
			return;
		} else if (helper.isOperand(currentWord)) {
			String strValue = sourceWord;
			Boolean isIdent = false;
			if (currentWord.equals("IDENT")) {
				Entry e = st.findVariable(sourceWord);
				strValue = e.getValue();
				currentWord = e.getType();
				isIdent = true;
			}

			stack.push(new Operand(sourceWord, isIdent, strValue, currentWord, null));

		}
	}

	/**
	 * Stores the variable and its attributes to the symbol table.
	 * 
	 * @param variable
	 *            the name of the variable
	 * @param operand
	 *            the attributes of the variable
	 */
	private void storeToSymbolTable(String variable, Operand operand) {
		String value = operand.getStrFloatValue();
		String type = "";
		switch (operand.type) {
		case "STR":
			type = "STR";
			break;
		case "FLOAT":
		case "FLOAT_LIT":
			type = "FLOAT";
			break;
		case "INT":
		case "INT_LIT":
			Integer newVal = Math.round(operand.getNumberValue());
			value = newVal.toString();
			type = "INT";
			break;
		}
		st.storeResult(variable, value, type);
	}

	/**
	 * Gets the symbol table of the class.
	 * 
	 * @return symbol table
	 */
	public SymbolTable getSymbolTable() {
		return st;
	}

	/**
	 * Executes the loop.
	 * 
	 * @param i
	 *            the starting row index
	 * @param j
	 *            the starting column index (location of FROM)
	 */
	private boolean executeLoop(int i, int j) {
		System.out.println("--------------STARTLOOP---------------");
		isLoopOngoing = true;
		Integer[] data = getStartAndEnd(i, j);
		int countSize = data[1] - data[0] + 1;

		// indexes after the conditions of FROM loop
		int row = data[2];
		int column = data[3];

		data = getIndexes("ENDFROM", row, column, -1, true);
		int rowEnd = data[0];
		int colEnd = data[1];
		for (i = 0; i < countSize; i++) {
			if(!evaluate(row, column, rowEnd, colEnd)){
				System.out.println("ADDEDLOOP");
				lastLoopIteration[0] = i + 1;
				lastLoopIteration[1] = countSize;
				setLastIndex(row, column, rowEnd, colEnd);
				System.out.println("<<<<i:" + i);
				System.out.println("<<<<csize:" + countSize);
				return false;
			}
		}
		isLoopOngoing = false;
		setLastIndex(rowEnd, colEnd, null, null);
		return true;
	}

	/**
	 * Obtains the conditions.
	 * 
	 * @param i
	 *            starting row index
	 * @param j
	 *            starting column index
	 * @return the conditions
	 */
	private Integer[] getStartAndEnd(int i, int j) {
		int column = j;
		int start = 0;
		int end = 0;
		boolean isCondition = true;
		String prevWord = "";

		for (; i < parsedWords.size(); i++) {
			String[] prsStmt = parsedWords.get(i);

			for (j = column; j < prsStmt.length; j++) {
				String currentWord = parsedWords.get(i)[j];
				String sourceWord = sourceWords.get(i)[j];
				if (currentWord.equals("FROM") || currentWord.equals("TO")) {
					isCondition = true;
					prevWord = currentWord;
					continue;
				} else if (isCondition) {
					if (prevWord.equals("FROM")) {
						start = Integer.parseInt(sourceWord);
					} else {
						end = Integer.parseInt(sourceWord);
						if (j >= prsStmt.length) {
							j = 0;
							++i;
						}
						Integer[] result = { start, end, i, j + 1 };
						return result;
					}
					isCondition = false;
				}
			}
			column = 0;
		}
		return null;
	}

	/**
	 * Gets the index of the searched word.
	 * 
	 * @param i
	 *            starting row index
	 * @param j
	 *            starting column index
	 * @param addToIndex
	 *            add or subtract to the searched index
	 * @return the indexes of the end of the FROM loop
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private Integer[] getIndexes(String word, int i, int j, int addToIndex, boolean doProceed) {
		for (; i < parsedWords.size(); i++) {
			String[] prsStmt = parsedWords.get(i);

			for (j = 0; j < prsStmt.length; j++) {
				String currentWord = parsedWords.get(i)[j];
				if (currentWord.equals(word)) {
					System.out.println("LOCATED:" + i + "," + j);
					j = j + addToIndex;
					if (j < 0) {
						i = i - 1;
						j = parsedWords.get(i).length - 1;
					} else if (j >= parsedWords.get(i).length) {
						i = i + 1;
						j = 0;
					}
					Integer[] result = { i, j };
					setLastIndex(i, j, null, null);
					return result;
				}
			}
			if (!doProceed) {
				Integer[] NULL = { -1, -1 };
				return NULL;
			}
		}
		Integer[] NULL = { -1, -1 };
		return NULL;
	}

	/**
	 * Operates the logical operation based on the operator obtained.
	 * 
	 * @param operator
	 *            the logical operator to use for evaluation
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void compareLogic(String operator) {
		Operand op1 = null;
		Operand op2 = null;
		if (stack.size() < 2 && (operator.equals("OR?") || operator.equals("AND?"))) {
			op1 = stack.pop();
			op2 = !operator.equals("NOT?") ? stack.pop() : null;
		}
		Boolean result = null;
		switch (operator) {
		case "AND?":
			result = op1.bool && op2.bool;
			break;
		case "OR?":
			result = op1.bool || op2.bool;
			break;
		case "NOT?":
			result = !op1.bool;
			stack.push(op2);
			break;
		}

		stack.push(new Operand(null, null, "BOOL", result));
	}

	/**
	 * Compares operands in the stack based on the received operator.
	 * 
	 * @param operator
	 *            operator to be used to the operands
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void compareRelation(String operator) {
		Operand op1 = stack.pop();
		Operand op2 = stack.pop();
		Boolean result = null;
		switch (operator) {
		case "GT?":
			result = op1.getNumberValue() > op2.getNumberValue();
			break;
		case "GTE?":
			result = op1.getNumberValue() >= op2.getNumberValue();
			break;
		case "LT?":
			result = op1.getNumberValue() < op2.getNumberValue();
			break;
		case "LTE?":
			result = op1.getNumberValue() <= op2.getNumberValue();
			break;
		case "EQ?":
			result = op1.getNumberValue() == op2.getNumberValue();
			break;
		case "NEQ?":
			result = op1.getNumberValue() != op2.getNumberValue();
			break;
		}

		stack.push(new Operand(null, null, "BOOL", result));
	}

	/**
	 * Evaluates the operation given.
	 * 
	 * @param operator
	 *            the operator to be used to the operands
	 * @return true if the operation is succesful; false if not (encountered STR
	 *         type)
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private boolean solveMath(String operator) {
		Operand op1 = stack.pop();
		Operand op2 = stack.pop();
		String type = op1.type;
		if (op1.type.equals("STR") || op2.type.equals("STR")) {
			return false;
		}
		if (!op1.type.equals(op2.type) && (op1.type.equals("FLOAT") || op2.type.equals("FLOAT"))) {
			type = "FLOAT";
		}
		Float result = null;
		switch (operator) {
		case "ADD":
			result = op1.getNumberValue() + op2.getNumberValue();
			break;
		case "SUB":
			result = op1.getNumberValue() - op2.getNumberValue();
			break;
		case "DIV":
			result = op1.getNumberValue() / op2.getNumberValue();
			break;
		case "MULT":
			result = op1.getNumberValue() * op2.getNumberValue();
			break;
		case "MOD":
			result = op1.getNumberValue() % op2.getNumberValue();
			Integer temp = Math.round(result);
			result = temp.floatValue();
			type = "INT";
			break;
		}

		stack.push(new Operand(null, result.toString(), type, null));
		return true;
	}

	/**
	 * Sets the last index visited.
	 * 
	 * @param i
	 *            the last row index visited
	 * @param j
	 *            the last column index visited
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void setLastIndex(int i, int j, Integer rowEnd, Integer colEnd) {
		System.out.println("[changed index]");
		lastIndex[0] = i;
		lastIndex[1] = j;
		lastIndex[2] = rowEnd;
		lastIndex[3] = colEnd;
	}
	
	
	/**
	 * Initializes the frame contents.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);

		JScrollPane spOutput = new JScrollPane();
		frame.getContentPane().add(spOutput, BorderLayout.CENTER);

		txtOutput.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtOutput.setText("");
		txtOutput.setEditable(false);
		spOutput.setViewportView(txtOutput);

		JPanel pnInput = new JPanel();
		pnInput.setPreferredSize(new Dimension(10, 30));
		frame.getContentPane().add(pnInput, BorderLayout.SOUTH);
		pnInput.setLayout(new BoxLayout(pnInput, BoxLayout.X_AXIS));

		JLabel lblInput = new JLabel("Input:");
		pnInput.add(lblInput);

		txtInput = new JTextField();
		txtInput.setMaximumSize(new Dimension(2147483647, 20));
		txtInput.setMinimumSize(new Dimension(6, 15));
		txtInput.setPreferredSize(new Dimension(6, 15));
		txtInput.setEditable(false);
		txtInput.setEnabled(false);
		pnInput.add(txtInput);
		txtInput.setColumns(30);
		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String in = begInput();
				System.out.println("---Entered Enter---");
				System.out.println("Input:" + in);
				output += in + "\n";
				emptyPendingOperation();
				
				if (isPaused) {
					return;
				}
				
				if(isLoopOngoing){
					System.out.println("HEREENTER" + "|" + isLoopOngoing + "|" + "lp[0]:" + lastLoopIteration[0] + "|" + lastLoopIteration[1]);
					int row = lastIndex[0];
					int col = lastIndex[1];
					int rowEnd = lastIndex[2];
					int colEnd = lastIndex[3];
					
					if(isLoopOngoing && lastLoopIteration[0] >= lastLoopIteration[1]){
						isLoopOngoing =  false;
						Integer[] startIndex = getIndexes("ENDFROM", row, col, 0, true);
						row = startIndex[0];
						col = startIndex[1];
						if(row == parsedWords.get(startIndex[0]).length - 1){
							row++;
							col = parsedWords.get(row).length - 1;
						} else {
							col++;
						}
						evaluate(row, col, null, null);
						System.out.println("HELLO!");
					}
					
					while(lastLoopIteration[0] < lastLoopIteration[1]){
						if(!evaluate(row, col, rowEnd, colEnd) && isLoopOngoing){
							setLastIndex(row, col, rowEnd, colEnd);
							System.out.println("ITERATION:" +lastLoopIteration[0]);
							System.out.println("ITERATION:" +lastLoopIteration[1]);
							lastLoopIteration[0]++;
							break;
						}
						lastLoopIteration[0]++;
					}
				} else {
					evaluate(lastIndex[0], lastIndex[1], null, null);
				}

				System.out.println("---Exited Enter---" + "|" + isLoopOngoing + "|lp[0]:" + lastLoopIteration[0] + "|" + lastLoopIteration[1]);
				exprEval.gui.setTablesInfo(st);
			}
		};
		txtInput.setAction(action);
	}
}

/**
 * Class for holding operands. Contains source name (name in the source code)
 * and its value, type, and optionally boolean value.
 * 
 * @author Sumandang, AJ Ruth H.
 *
 */
class Operand {
	public String sourceName;
	private String value;
	public Boolean isIdent;
	public String type;
	public Boolean bool;

	/**
	 * Constructor for operand
	 * 
	 * @param src
	 *            variable name (actual name in the code)
	 * @param value
	 *            value of the variable name
	 * @param type
	 *            type of variable
	 * @param bool
	 *            boolean of value of the variable (if available)
	 */
	public Operand(String sourceName, String value, String type, Boolean bool) {
		this.sourceName = sourceName;
		this.value = value;
		this.type = type;
		this.bool = bool;
	}
	/**
	 * Constructor for operand
	 * 
	 * @param src
	 *            variable name (actual name in the code)
	 * @param value
	 *            value of the variable name
	 * @param type
	 *            type of variable
	 * @param bool
	 *            boolean of value of the variable (if available)
	 */
	public Operand(String sourceName, Boolean isIdent, String value, String type, Boolean bool) {
		this.sourceName = sourceName;
		this.isIdent = isIdent;
		this.value = value;
		this.type = type;
		this.bool = bool;
	}

	/**
	 * Gets the string or float value of the operand.
	 * 
	 * @return operand's string-version of its float value
	 */
	public String getStrFloatValue() {
		return value;
	}

	/**
	 * Gets the float value of the operand.
	 * 
	 * @return
	 */
	public Float getNumberValue() {
		if(value!=""){
			return Float.parseFloat(value);
		}
		return Float.parseFloat("0.00");
	}

	/**
	 * Gets the int value of the operand.
	 * 
	 * @return operand's int value
	 */
	public Integer getIntValue() {
		return Integer.parseInt(value);
	}

	/**
	 * Gets the integer string value of the operand.
	 * 
	 * @return operand's string-version of its integer value
	 */
	public String getStrIntValue() {
		Integer res = Integer.parseInt(value);
		return res.toString();
	}

	public void setValue(String value) {
		this.value = "" + value;
	}

	public void setValue(Integer value) {
		this.value = "" + value;
	}

	public void setValue(Float value) {
		this.value = "" + value;
	}
}

/**
 * Class for holding operations and its operands.
 * 
 * @author Sumandang, AJ Ruth H.
 */
class Operation {
	public String operation;
	public Operand op1;
	public Operand op2;

	/**
	 * Constructor for Operation class.
	 * 
	 * @param operation
	 *            operation/operator to be used
	 * @param op1
	 *            first operand
	 * @param op2
	 *            second operand; may not be available depending on the operator
	 */
	public Operation(String operation, Operand op1, Operand op2) {
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
	}

}
