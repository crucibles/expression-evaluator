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
	//for GUI
	private String output = "";
	private JFrame frame;
	private JTextField txtInput;
	private JTextPane txtOutput = new JTextPane();
	
	//for operations
	private Stack<Operand> stack = new Stack<Operand>();
	private Stack<String> operationStack = new Stack<String>();
	
	//for source inputs
	private ArrayList<String[]> parsedWords = new ArrayList<String[]>();
	private ArrayList<String[]> sourceWords = new ArrayList<String[]>();
	private ArrayList<Integer> exclude;
	private String fileName;
	private int numOfErrors;
	
	//for flags (checking whether to execute, where to execute, etc.)
	private int[] lastIndex = { -1, -1 };
	private boolean isStartExecution = false;
	private boolean isPaused = false;

	/**
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public Evaluator(String sourceString, String parsedString, ArrayList<Integer> exclud, String file, int errors) {
		//initialize GUI
		initialize();
		
		//intro
		output += fileName + "compiled with " + numOfErrors
				+ " error(s) found. Program test will now be executed...\n\n";
		output += "SNuBL Executition: \n\n";

		//split code to words
		String[] srcStmt = sourceString.trim().split("\n");
		String[] prsStmt = parsedString.trim().split("\n");
		for (int i = 0; i < srcStmt.length; i++) {
			sourceWords.add(srcStmt[i].trim().split("\\s"));
			parsedWords.add(prsStmt[i].trim().split("\\s"));
		}

		//store received params
		exclude = exclud;
		fileName = file;
		numOfErrors = errors;

		//evaluate
		evaluate();
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
				output += begInput(true) + "\n";
				evaluate();
			}
		};
		txtInput.setAction(action);
	}

	/**
	 * Evaluates the parsed string.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void evaluate() {
		int row = lastIndex[0] < 0 ? 0 : lastIndex[0];
		System.out.println("LENGTH:" + parsedWords.size());
		for (int i = row; i < parsedWords.size(); i++) {
			String[] srcStmt = sourceWords.get(i);
			String[] prsStmt = parsedWords.get(i);

			if (prsStmt == null) {
				continue;
			}

			int column = lastIndex[1] < 0 ? 0 : lastIndex[1];
			String operation = "";
			for (int j = column; j < prsStmt.length; j++) {
				String currentWord = prsStmt[j];
				String sourceWord = srcStmt[j];
				if (currentWord.equals("COMMAND")) {
					isStartExecution = true;
					continue;
				} else if (!isStartExecution) {
					continue;
				}
				if (currentWord.equals("NEWLN")) {
					output += "\n";
					continue;
				}
				
				if (isOperator(currentWord) || isRelationalOp(currentWord) || isLogicalOp(currentWord)
						|| currentWord.equals("INTO") || currentWord.equals("BEG") || currentWord.equals("PRINT")) {
					operationStack.push(currentWord);
					System.out.println("pushed OP:" + currentWord);
				}

				if (isOperand(currentWord)) {
					Float value = Float.valueOf(sourceWord);
					stack.push(new Operand(sourceWord, value, "FLOAT", null));
					// AHJ: unimplemented: get type of operand in the
					operation = operationStack.peek();
					while (operate(operation) && !operationStack.isEmpty()) {
						operation = operationStack.peek();
						System.out.println("SET OP:" + operation);
					}
				}

				if (isPaused) {
					lastIndex[1] = j + 1 > prsStmt.length - 1 ? -1 : j + 1;
					lastIndex[0] = lastIndex[1] < 0 ? i + 1 : i;
					return;
				}
			}
		}
		System.out.println(lastIndex);
		output += "Program terminated successfully...";
		txtOutput.setText(output);
	}

	/**
	 * Operate based the given operation.
	 * @param currentWord the operation to be used
	 * @return true; if operation is successful; false if not.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private boolean operate(String currentWord) {
		System.out.println("______________");
		System.out.println("ENTERED OPERATE");
		System.out.println("CURRWORD:" + currentWord);
		System.out.println("stacksize:" + stack.size());
		if (isOperator(currentWord) && stack.size() > 1) {
			evaluateStmt(currentWord);
		} else if (isRelationalOp(currentWord) && stack.size() > 1) {
			compareRelation(currentWord);
		} else if (isLogicalOp(currentWord)) {
			compareLogic(currentWord);
		} else if (currentWord.equals("INTO") && stack.size() > 0) {

		} else if (currentWord.equals("BEG") && stack.size() > 0) {
			begInput();
			System.out.println("BEEEGIIING");
			isPaused = true;
		} else if (currentWord.equals("PRINT") && stack.size() > 0) {
			output += print(currentWord);
			System.out.println("ADDED OUTPUT: " + output);
		} else {
			return false;
		}
		operationStack.pop();
		return true;
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
			result = op1.value > op2.value;
			break;
		case "GTE?":
			result = op1.value >= op2.value;
			break;
		case "LT?":
			result = op1.value < op2.value;
			break;
		case "LTE?":
			result = op1.value <= op2.value;
			break;
		case "EQ?":
			result = op1.value == op2.value;
			break;
		case "NEQ?":
			result = op1.value != op2.value;
			break;
		}

		stack.push(new Operand(null, null, "BOOL", result));
	}

	private void evaluateStmt(String operator) {
		Operand op2 = stack.pop();
		Operand op1 = stack.pop();
		Float result = null;
		switch (operator) {
		case "ADD":
			result = op1.value + op2.value;
			break;
		case "SUB":
			result = op1.value - op2.value;
			break;
		case "DIV":
			result = op1.value / op2.value;
			break;
		case "MULT":
			result = op1.value * op2.value;
			break;
		case "MOD":
			result = op1.value % op2.value;
			break;
		}

		stack.push(new Operand(null, result, op1.type, null));
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

	private void begInput() {
		Operand op = stack.pop();
		output += "Input for " + op.sourceName + ": ";
		txtOutput.setText(output);
		txtInput.setText("");
		txtInput.setEditable(true);
		txtInput.setEnabled(true);
	}

	private String begInput(boolean b) {
		String result = txtInput.getText();
		txtInput.setEditable(false);
		txtInput.setEnabled(false);
		isPaused = false;
		return result;
	}

	private String print(String word) {
		Operand op1 = stack.pop();
		System.out.println("PRINT THIS:" + op1.type);
		String output = "Printing: ";
		switch (op1.type) {
		case "INT":
		case "FLOAT":
			output += op1.value;
			break;
		case "STR":
			output += op1.strValue;
		case "BOOL":
			output += op1.bool;
		}
		return output + "\n";
	}

	private boolean isOperator(String word) {
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
}

class Operand {
	public String sourceName;
	public Float value;
	public String type;
	public Boolean bool;
	public String strValue = null;

	public Operand(String src, Float valu, String typ, Boolean boo) {
		sourceName = src != null ? src : null;
		value = valu != null ? valu : null;
		type = typ != null ? typ : null;
		bool = boo != null ? boo : null;
	}
}
