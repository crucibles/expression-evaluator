
/**
 * Name: Alvaro, Cedric	(2014-60690)	Sumandang, AJ Ruth (2014-37728)
 * Deadline Date and Time:  	March 21, 2018; 11:59 p.m.
 * 
 * Program Exercise #	: 5
 * Program Title		: Non-recursive Predictive Parsing
 * 
 * Program Description:
 * 		The program is capable of checking whether a string of tokens is valid based on the syntatic rules given.
 * 		It only works if both .prod and .ptbl files are loaded.
 * 
 * [User Manual]
 * > Load .prod & .ptbl files by clicking 'Load' button.
 * > Input string whose syntax is to be checked.
 * 		- Only works if both .prod and .ptbl file are loaded
 * > Press 'Parse' button to parse input string
 * 		- After pressing the button, a save dialog will appear. Save the output to where you want to place the output file.
 */

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class NRPP {
	private JTable tblParse;
	private JTable tblProduction;
	private String errors = "";
	private ArrayList<Integer> lineNumberErrors = new ArrayList<Integer>();

	/**
	 * Constructor
	 */
	public NRPP(String text) {

		String strProdRules = "1,SNuBL,DEFINE VarDef END COMMAND Statements END\n"
				+ "2,VarDef,FLOAT IDENT FloatVarDef VarDef\n" + "3,VarDef,INT IDENT IntVarDef VarDef\n"
				+ "4,VarDef,STR IDENT VarDef\n" + "5,VarDef,e\n" + "6,FloatVarDef,IS FLOAT_LIT\n" + "7,FloatVarDef,e\n"
				+ "8,IntVarDef,IS INT_LIT\n" + "9,IntVarDef,e\n" + "10,Statements,Assignment Statements\n"
				+ "11,Statements,Input Statements\n" + "12,Statements,Output Statements\n"
				+ "13,Statements,NumericOperations Statements\n" + "14,Statements,RelationalAndLogical Statements\n"
				+ "15,Statements,Conditional Statements\n" + "16,Statements,Loop Statements\n"
				+ "17,Statements,NEWLN Statements\n" + "18,Statements,e\n" + "19,Assignment,INTO IDENT IS Expr\n"
				+ "20,Input,BEG IDENT\n" + "21,Output,PRINT OutputOptions\n" + "22,OutputOptions,Expr\n"
				+ "23,OutputOptions,RelationalAndLogical\n" + "24,NumericOperations,ADD Expr Expr\n"
				+ "25,NumericOperations,SUB Expr Expr\n" + "26,NumericOperations,MULT Expr Expr\n"
				+ "27,NumericOperations,DIV Expr Expr\n" + "28,NumericOperations,MOD Expr Expr\n"
				+ "29,RelationalAndLogical,GT? Expr Expr\n" + "30,RelationalAndLogical,GTE? Expr Expr\n"
				+ "31,RelationalAndLogical,LT? Expr Expr\n" + "32,RelationalAndLogical,LTE? Expr Expr\n"
				+ "33,RelationalAndLogical,EQ? Expr Expr\n" + "34,RelationalAndLogical,NEQ? Expr Expr\n"
				+ "35,RelationalAndLogical,AND? Expr Expr\n" + "36,RelationalAndLogical,OR? Expr Expr\n"
				+ "37,RelationalAndLogical,NOT? Expr\n"
				+ "38,Conditional,IF BoolExpr Statements ELSE Statements ENDIF\n"
				+ "39,Loop,FROM INT_LIT TO INT_LIT Statements ENDFROM\n" + "40,Expr,FLOAT_LIT\n" + "41,Expr,INT_LIT\n"
				+ "42,Expr,IDENT\n" + "43,Expr,NumericOperations\n" + "44,BoolExpr,TRUE\n" + "45,BoolExpr,FALSE\n"
				+ "46,BoolExpr,RelationalAndLogical\n";

		String strParse = "PRODUCTIONS,DEFINE,COMMAND,END,FLOAT,IDENT,INT,STR,IS,FLOAT_LIT,INT_LIT,NEWLN,INTO,BEG,PRINT,ADD,SUB,MULT,DIV,MOD,GT?,GTE?,LT?,LTE?,EQ?,NEQ?,AND?,OR?,NOT?,IF,ELSE,ENDIF,FROM,TO,ENDFROM,TRUE,FALSE,$\n"
				+ "SNuBL ,1,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n" + "VarDef,,,5,2,,3,4,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
				+ "FloatVarDef,,,7,7,,7,7,6,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
				+ "IntVarDef ,,,9,9,,9,9,8,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n"
				+ "Statements,,,18,,,,,,,,17,10,11,12,13,13,13,13,13,14,14,14,14,14,14,14,14,14,15,18,18,16,,18,,,\n"
				+ "Assignment,,,,,,,,,,,,19,,,,,,,,,,,,,,,,,,,,,,,,,\n"
				+ "Input ,,,,,,,,,,,,,20,,,,,,,,,,,,,,,,,,,,,,,,\n" + "Output,,,,,,,,,,,,,,21,,,,,,,,,,,,,,,,,,,,,,,\n"
				+ "OutputOptions,,,,,22,,,,22,22,,,,,22,22,22,22,22,,23,23,23,23,,23,23,23,,,,,,,,,\n"
				+ "NumericOperations ,,,,,,,,,,,,,,,24,25,26,27,28,,,,,,,,,,,,,,,,,,\n"
				+ "RelationalAndLogical ,,,,,,,,,,,,,,,,,,,,29,30,31,32,33,34,35,36,37,,,,,,,,,\n"
				+ "Conditional ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,38,,,,,,,,\n"
				+ "Loop ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,39,,,,,\n"
				+ "Expr,,,,,42,,,,40,41,,,,,43,43,43,43,43,,,,,,,,,,,,,,,,,,\n"
				+ "BoolExpr,,,,,,,,,,,,,,,,,,,,46,46,46,46,46,46,46,46,46,,,,,,,44,45,\n";

		setProductionTable(strProdRules);
		setParseTable(strParse);
		parseInput(text);
	}

	/**
	 * Parse the input of the user if .prod and .pbtl file is loaded.
	 * 
	 * @return the output from the user input based on the specified grammar
	 *         found in .prod and .ptbl files.
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String parseInput(String text) {
		// @index is the index of the current word being matched from the input
		// @rules is the index of the nonTerminal to be used, from the parse
		// table. Identifying what row it is in Production table.
		int index = 0;
		int rules = 0;

		// @result is the string to be returned by the parser to fill in the
		// Output table
		// @input is the input to be parsed
		// @inputWords is the tokenized input of words for parsing
		// @inputBuffer is the running input for the output table
		// @currentWord is the currentWord to be matched in the production
		// @action is the action description, what is being done during parsing
		// @production is the current NonTerminal or terminal to be matched to
		// the currentWord or to be expanded
		// @currentStack is the stack per line for the outputTable
		// @produced is the produced words of the nonterminals
		// @pdt is the production table
		// @prt is the parse table
		String result = "";
		String input = text + " $";
		String inputWords[] = splitter(input.trim());
		String currentWord = inputWords[0];
		String inputBuffer = input;
		String currProduction = "";
		String action = "";
		String production = "";
		String currentStack = "";
		String produced = "";
		int lineNum = 1;
		TableModel pdt = tblProduction.getModel();
		TableModel prt = tblParse.getModel();
		production = pdt.getValueAt(index, 1).toString();
		currentStack = production + " $";

		result += currentStack + ",";
		result += inputBuffer + ",";
		result += action + "\n";

		while (!action.equals("Match $")) {
			if (production.equals("VarDef") || production.equals("Statements")) {
				currProduction = production;
			}

			if (production.equals(currentWord)) {
				if (production.equals("" + "$")) {
					action = "Match $";
					result += ",";
					result += ",";
					result += action + "\n";
					return result;
				}

				action = "Match " + currentWord;
				index++;
				inputBuffer = inputBuffer.substring(production.length(), inputBuffer.length());
				inputBuffer = inputBuffer.trim();
				currentStack = currentStack.substring(production.length(), currentStack.length());
				currentStack = currentStack.trim();
				while (inputWords[index].equals("ln")) {
					lineNum++;
					index++;
				}
				currentWord = inputWords[index];
				production = nextWord(currentStack);
				production = production.trim();
				result += currentStack + ",";
				result += inputBuffer + ",";
				result += action + "\n";
			} else {
				int row = getRowIndex(prt, production);
				int column = getColumnIndex(prt, currentWord);
				String prodNum = "";
				if (row >= 0 && column >= 0) {
					prodNum = prt.getValueAt(row, column).toString();
				}

				if (prodNum != "") {
					rules = Integer.parseInt(prodNum);
					produced = pdt.getValueAt(rules - 1, 2).toString();
					if (rules != 0) {
						action = "Output " + production + " > " + produced;
					}

					if (produced.equals("" + "e")) {

						currentStack = currentStack.replaceFirst(production, "");
						currentStack = currentStack.trim();
						production = nextWord(currentStack);

					} else {

						currentStack = currentStack.replaceFirst(production, produced);
						currentStack = currentStack.trim();
						production = nextWord(currentStack);

					}

					result += currentStack + ",";
					result += inputBuffer + ",";
					result += action + "\n";
				} else {
					int errLineNum = lineNum;
					System.out.println("dsadasdjasgdashgdjhasj"+inputWords[index - 1]);
					if (inputWords[index - 1].equals("ln")) {
						errLineNum -= 1;
					}

					action = "Error on " + currentStack + " trying to parse " + currentWord + ">" + production;
					if (row < 0) {
						errors += "(Line #" + errLineNum + ") Syntax Error: Missing " + production + "\n";
					} else {
						errors += "(Line #" + errLineNum + ") Syntax Error: Error on trying to parse " + currentWord
								+ "\n";
					}
					lineNumberErrors.add(errLineNum);

					while (!inputWords[index].equals("ln") && !inputWords[index].equals("END")
							&& !inputWords[index].equals("$")) {
						index++;
					}

					if (inputWords[index].equals("$")) {
						result += currentStack + ",";
						result += inputBuffer + ",";
						result += action + "\n";
						return result;
					}

					do {
						currentStack = currentStack.substring(production.length(), currentStack.length());
						currentStack = currentStack.trim();
						production = currentStack.split("\\s")[0];
						production = production.trim();
					} while (!production.equals("END"));

					if (inputWords[index].equals("ln")) {
						index++;
						lineNum++;
						currentWord = inputWords[index];
						currentStack = currProduction + " " + currentStack;
					}

					production = currentStack.split("\\s")[0];
					production = production.trim();
					inputBuffer = inputBuffer.substring(production.length(), inputBuffer.length());
					inputBuffer = inputBuffer.trim();

					result += currentStack + ",";
					result += inputBuffer + ",";
					result += action + "\n";
				}

			}

		}
		System.out.println(result);
		return result;
	}

	/**
	 * @return array of inputted words
	 * @author Alvaro, Cedric Y.
	 */
	public String[] splitter(String input) {
		String inputted = input.trim();
		String[] splitted = new String[input.split("\\s").length];
		String temp = "";
		int x = 0;

		for (int i = 0; i < inputted.length(); i++) {

			if (inputted.charAt(i) == ' ' || temp.equals("ln") || inputted.charAt(i) == '\t') {
				splitted[x] = temp;
				x++;
				temp = "";
				i++;
			}

			temp += inputted.charAt(i);

		}
		splitted[x] = "$";
		return splitted;
	}

	/**
	 * @return the next word of the input buffer
	 * @author Alvaro, Cedric Y.
	 */
	public String nextWord(String input) {
		String inputted = input.trim();
		String nextWord = "";
		String temp = "";

		for (int i = 0; i < inputted.length(); i++) {

			if (inputted.charAt(i) == ' ') {
				nextWord = temp;
				return nextWord;
			}

			temp += inputted.charAt(i);
		}
		return "$";
	}

	/**
	 * Get the index of the row where the searched string is found
	 * 
	 * @param table
	 *            table where the string will be searched
	 * @param rowName
	 *            string to be searched
	 * 
	 * @return the index where the searched string is found
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private int getRowIndex(TableModel table, String rowName) {
		for (int i = 0; i < table.getRowCount(); i++) {
			String currentRowName = table.getValueAt(i, 0).toString().trim();

			if (currentRowName.equals(rowName)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Get the index of the column where the searched string is found
	 * 
	 * @param table
	 *            table where the string will be searched
	 * @param columnName
	 *            string to be searched
	 * 
	 * @return the index of the column where the searched string is found
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private int getColumnIndex(TableModel table, String columnName) {
		for (int i = 0; i < table.getColumnCount(); i++) {
			String currentColumnName = table.getColumnName(i);
			if (currentColumnName.equals(columnName)) {
				return i;
			}
		}

		return -1;
	}

	public String getErrors() {
		return errors;
	}
	
	public ArrayList<Integer> getLineNumErrors(){
		return lineNumberErrors;
	}

	/**
	 * Sets the parse table when a .ptbl file is loaded.
	 * 
	 * @param text
	 *            the text whose content will be placed in the table
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void setParseTable(String text) {
		tblParse = new JTable();

		boolean isFirst = true;
		DefaultTableModel model = (DefaultTableModel) tblParse.getModel();
		model.setRowCount(0);
		model.setColumnCount(0);
		String word = "";
		Vector<String> row = new Vector<String>();
		for (int i = 0; i < text.length(); i++) {
			if (isFirst && text.charAt(i) == '\n') {
				model.addColumn(word);
				word = "";
				isFirst = false;
			} else if (isFirst && text.charAt(i) == ',') {
				model.addColumn(word);
				word = "";
			} else if (text.charAt(i) == '\n') {
				row.add(word);
				word = "";
				if (!row.isEmpty()) {
					model.addRow(row);
				}
				row = new Vector<String>();
			} else if (text.charAt(i) == ',') {
				row.add(word);
				word = "";
			} else {
				word += text.charAt(i);
			}
		}

	}

	/**
	 * Sets the production table when a .prod file is loaded.
	 * 
	 * @param text
	 *            the text whose content will be placed in the table
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void setProductionTable(String text) {
		tblProduction = new JTable();
		tblProduction.setEnabled(false);
		tblProduction.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "NT", "P" }));

		DefaultTableModel model = (DefaultTableModel) tblProduction.getModel();
		model.setRowCount(0);
		String word = "";
		Vector<String> row = new Vector<String>();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '\n') {
				row.add(word);
				word = "";
				if (!row.isEmpty()) {
					model.addRow(row);
				}
				row = new Vector<String>();
			} else if (text.charAt(i) == ',') {
				row.add(word);
				word = "";
			} else {
				word += text.charAt(i);
			}
		}
	}
}
