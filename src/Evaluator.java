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

	// for source inputs
	private ArrayList<String[]> parsedWords = new ArrayList<String[]>();
	private ArrayList<String[]> sourceWords = new ArrayList<String[]>();
	private ArrayList<Integer> exclude;
	private ExpressionEvaluator exprEval;
	private String fileName;
	private int numOfErrors;
	private SymbolTable st;

	// for flags (checking whether to execute, where to execute, etc.)
	private int[] lastIndex = { -1, -1 };
	private boolean isPaused = false;

	/**
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public Evaluator(String sourceString, String parsedString, ArrayList<Integer> exclud, String file, int errors,
			SymbolTable symbolTable, ExpressionEvaluator exprEval) {
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
		exclude = exclud;
		fileName = file;
		numOfErrors = errors;
		st = symbolTable;
		this.exprEval = exprEval;

		// evaluate
		setStart();
		evaluate(lastIndex[0], lastIndex[1]);
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
					setLastIndex(i, j);
					return;
				}
			}
		}
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
		frame.getContentPane().add(pnInput, BorderLayout.SOUTH);

		JLabel lblInput = new JLabel("Input:");
		pnInput.add(lblInput);

		txtInput = new JTextField();
		txtInput.setEditable(false);
		txtInput.setEnabled(false);
		pnInput.add(txtInput);
		txtInput.setColumns(30);
		Action action = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				output += begInput() + "\n";
				while (!pendingStack.isEmpty()) {
					emptyPendingOperation();
					if (isPaused) {
						return;
					}
				}
				evaluate(lastIndex[0], lastIndex[1]);
				exprEval.gui.setTablesInfo(st);
			}
		};
		txtInput.setAction(action);
	}

	/**
	 * Evaluates the parsed string.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void evaluate(int row, int column) {
		int j = column;
		for (int i = row; i < parsedWords.size(); i++) {
			if (exclude.contains(i + 1)) {
				continue;
			}

			String[] srcStmt = sourceWords.get(i);
			String[] prsStmt = parsedWords.get(i);

			j = j < 0 ? prsStmt.length - 1 : column;
			if (prsStmt.length - 1 < j) {
				continue;
			}
			while (j >= 0) {
				String currentWord = prsStmt[j];
				String sourceWord = srcStmt[j];

				evaluateExpression(currentWord, sourceWord);
				j--;
			}

			while (!pendingStack.isEmpty()) {
				System.out.println("evaluate");
				emptyPendingOperation();
				if (isPaused) {
					// set to i + 1 since the current stmt is finished;
					// set to -1 so checking of string starts at the end
					setLastIndex(i + 1, -1);
					return;
				}
			}

		}

		output += "Program terminated successfully...";
		txtOutput.setText(output);
	}

	private void emptyPendingOperation() {
		while (!pendingStack.isEmpty()) {
			Operation operation = pendingStack.pop();
			executeOutput(operation.operation, operation.op1, operation.op2);
			if (isPaused) {
				return;
			}
		}
	}

	private void executeOutput(String operation, Operand op1, Operand op2) {
		if (operation.equals("NEWLN")) {
			output += "\n";
		} else if (operation.equals("BEG")) {
			begInput(op1);
			isPaused = true;
		} else if (operation.equals("PRINT")) {
			System.out.println("HEREprint");
			output += print(op1);
		} else if (operation.equals("INTO")) {
			Integer intVal = Math.round(op2.getNumberValue());
			String value = op2.type.equals("FLOAT") ? op2.value : intVal.toString();
			st.storeResult(op1.sourceName, value, op2.type);
		}
	}

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
			System.out.println("==========");
			System.out.println("sourceWord:" + sourceWord);
			System.out.println("stackSize@push:" + stack.size());
			System.out.println("==========");
			pendingStack.push(new Operation(currentWord, op1, op2));
		} else if (currentWord.equals("FROM")) {
			executeLoop(lastIndex[0], lastIndex[1]);
		}
		if (isMathematicalOp(currentWord) && stack.size() > 1) {
			evaluateStmt(currentWord);
		} else if (isRelationalOp(currentWord) && stack.size() > 1) {
			compareRelation(currentWord);
		} else if (isLogicalOp(currentWord)) {
			compareLogic(currentWord);
		} else if (currentWord.equals("IS")) {
			return;
		} else if (isOperand(currentWord)) {
			String strValue = sourceWord;
			if (currentWord.equals("IDENT")) {
				Entry e = st.findVariable(sourceWord);
				strValue = e.getValue();
				currentWord = e.getType();
			}

			stack.push(new Operand(sourceWord, strValue, currentWord, null));

		}
	}
	//
	// private boolean isOperator(String currentWord) {
	// String[] operators = { "INTO", "BEG", "PRINT", "IS" };
	// boolean isOper = false;
	// for (int i = 0; i < operators.length; i++) {
	// if (operators[i].equals(currentWord)) {
	// isOper = true;
	// break;
	// }
	// }
	// return isMathematicalOp(currentWord) || isRelationalOp(currentWord) ||
	// isLogicalOp(currentWord) || isOper;
	// }

	private void storeToSymbolTable(String variable, Operand operand) {
		String value = operand.value;
		String type = "";
		System.out.println("TYPE:" + operand.type);
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
		System.out.println("____________");
		System.out.println(variable);
		System.out.println(operand.sourceName);
		System.out.println(value);
		System.out.println("____________");
		st.storeResult(variable, value, type);
	}

	public SymbolTable getSymbolTable() {
		return st;
	}

	private void executeLoop(int i, int j) {
		Integer[] data = getStartAndEnd(i, j);
		int countSize = data[1] - data[0] + 1;
		ArrayList<String[]> loopStmts = getLoopStatements(i, j);
		for (i = 0; i < countSize; i++) {
			for (j = 0; j < loopStmts.size(); j++) {
				evaluateExpression(loopStmts.get(j)[0], loopStmts.get(j)[1]);
			}
		}
	}

	private Integer[] getStartAndEnd(int i, int j) {
		int column = j;
		int start = 0;
		int end = 0;
		boolean isCondition = true;
		String prevWord = "";

		for (; i < parsedWords.size(); i++) {
			String[] prsStmt = parsedWords.get(i);

			String currentWord = parsedWords.get(i)[j];
			String sourceWord = sourceWords.get(i)[j];
			for (j = column; j < prsStmt.length; j++) {
				if (currentWord.equals("FROM") || currentWord.equals("TO")) {
					isCondition = true;
					prevWord = currentWord;
					continue;
				} else if (isCondition) {
					if (prevWord.equals("FROM")) {
						start = Integer.parseInt(sourceWord);
					} else {
						end = Integer.parseInt(sourceWord);
						setLastIndex(i, j - 1);
						Integer[] result = { start, end };
						return result;
					}
					isCondition = false;
				}
			}
			column = 0;
		}
		return null;
	}

	private ArrayList<String[]> getLoopStatements(int i, int j) {
		int column = j;
		ArrayList<String[]> result = new ArrayList<String[]>();
		for (; i < parsedWords.size(); i++) {
			String[] prsStmt = parsedWords.get(i);

			String currentWord = parsedWords.get(i)[j];
			String sourceWord = sourceWords.get(i)[j];
			for (j = column; j < prsStmt.length; j++) {
				if (currentWord.equals("ENDFROM")) {
					setLastIndex(i, j);
					return result;
				} else {
					String[] res = { currentWord, sourceWord };
					result.add(res);
				}
			}
			column = 0;
		}
		return null;
	}

	private void compareLogic(String operator) {
		Operand op1 = stack.pop();
		Operand op2 = stack.pop();
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

	private void evaluateStmt(String operator) {
		Operand op1 = stack.pop();
		Operand op2 = stack.pop();
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
			break;
		}

		stack.push(new Operand(null, result.toString(), op1.type, null));
	}

	private boolean isOperand(String word) {
		String[] operands = { "IDENT", "INT_LIT", "FLOAT_LIT" };
		for (int i = 0; i < operands.length; i++) {
			if (word.equals(operands[i])) {
				return true;
			}
		}
		return false;
	}

	private void begInput(Operand op) {
		variable = op;
		output += "Input for " + op.sourceName + ": ";
		txtOutput.setText(output);
		txtInput.setText("");
		txtInput.setEditable(true);
		txtInput.setEnabled(true);
	}

	/**
	 * Stores input after user enters an input.
	 * Includes type casting and input error checking.
	 * @param b
	 * @return
	 */
	private String begInput() {
		String result = txtInput.getText();
		txtInput.setEditable(false);
		txtInput.setEnabled(false);
		
		isPaused = false;
		if(variable.type.equals("STR")){
			variable.value = result;
			this.storeToSymbolTable(variable.sourceName, variable);
			result = variable.value;
		} else if(isFloat(result)){
			Integer intVal = Math.round(Float.parseFloat(result));
			variable.value = variable.type.equals("INT")? intVal.toString(): result;
			System.out.println("STOOOOOOOOOOOOORE");
			this.storeToSymbolTable(variable.sourceName, variable);
			result = variable.value;
		} else {
			result = "ERROR: Invalid input '" + result + "' for type " + variable.type + ".";
			
		}
		return result;
	}

	private String print(Operand op1) {
		String output = "Printing: ";
		switch (op1.type) {
		case "INT":
		case "INT_LIT":
			output += Math.round(op1.getNumberValue());
			break;
		case "FLOAT":
		case "FLOAT_LIT":
		case "STR":
			output += op1.value;
			break;
		case "BOOL":
			output += op1.bool;
		}
		return output + "\n";
	}

	private boolean isMathematicalOp(String word) {
		String[] operators = { "ADD", "SUB", "MULT", "DIV", "MOD" };
		for (int i = 0; i < operators.length; i++) {
			if (word.equals(operators[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isLogicalOp(String word) {
		String[] logicalOperators = { "AND?", "OR?", "NOT?" };
		for (int i = 0; i < logicalOperators.length; i++) {
			if (word.equals(logicalOperators[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isRelationalOp(String word) {
		String[] relationalOperators = { "GT?", "GTE?", "LT?", "LTE?", "EQ?", "NEQ?" };
		for (int i = 0; i < relationalOperators.length; i++) {
			if (word.equals(relationalOperators[i])) {
				return true;
			}
		}
		return false;
	}

	private void setLastIndex(int i, int j) {
		lastIndex[0] = i;
		lastIndex[1] = j;
	}
	
	/**
	 * Determines whether received element is float point number or not
	 * 
	 * @param check the element to be checked
	 * @return true if the received element is float point number; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private boolean isFloat(String check) {
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
}

class Operand {
	public String sourceName;
	public String value;
	public String type;
	public Boolean bool;

	/**
	 * Constructor for operand
	 * @param src variable name (actual name in the code)
	 * @param value value of the variable name
	 * @param type type of variable
	 * @param bool boolean of value of the variable (if available)
	 */
	public Operand(String sourceName, String value, String type, Boolean bool) {
		this.sourceName = sourceName;
		this.value = value;
		this.type = type;
		this.bool = bool;
	}

	public Float getNumberValue() {
		return Float.parseFloat(value);
	}
}

class Operation {
	public String operation;
	public Operand op1;
	public Operand op2;

	public Operation(String operation, Operand op1, Operand op2) {
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
	}

}
