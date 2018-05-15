
/**
 * Name: Alvaro, Cedric	(2014-60690)	Sumandang, AJ Ruth (2014-37728)
 * Deadline Date and Time:  	April 17, 2018; 11:59 a.m.
 * 
 * Program Exercise #	: 6
 * Program Title		: Syntax Analysis
 * 
 * Program Description:
 * 		The program is capable of creating, opening storing, and modifying files. 
 * 		If compiled, it checks whether the code in the opened file is lexically and syntactically correct.
 * 		
 * [User Manual]
 * > Click 'New' under 'File' if you want to create new file. Shortcut key: Ctrl + N
 * > Click 'Open File' under 'File' menu bar to open a file from your home directory. Shortcut key: Ctrl + O
 * 		- Choose valid and existing .in file in the home.
 * > Click 'Close' under 'File' if you want to close an open tab. Shortcut key: Ctrl + W
 * > Click 'Save' under 'File' if you want to save the current open tab's file. Shortcut key: Ctrl + S
 * > To compile (and check for lexical errors), click 'Compile' under 'Execute'. Shortcut key: F5
 * 
 * > Open 'Program Description' under 'Help' if you need help with something.
 * 
 * [Lexical Checking]
 * Each element must be separated by spaces.
 * (e.g. x = y + z instead of x=y+z)
 * - Variable (can only start with underscore (_) or letters (a-z))
 * - Operators (add (+), subtract (-), multiply (*), divide (/), remainder/module (%))
 * - Integer (whole numbers of 0-9 digits)
 * - Float (0-9 digits '.' 0-9 digits)
 * - Other accepted words (e.g. COMMAND, END, DEFINE)
 * 
 * [Syntax Checking]
 * - Has two division: DEFINE and COMMAND. Both are closed with an 'END'
 * - Correct ordering of the tokens
 * 
 * [Evaluation Checking]
 * - Under Construction
 * 
 * [Backlogs]
 * - (Fixed but check for further errors)Program compiles/saves if save dialog canceled when there is a name input 
 * - The program does not include error-checking if the file is already open or not.
 * - Errors when opening without new tab
 * - Opening a file (not 'New')
 * - Cannot determine if the same file is opened
 */

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ExpressionEvaluator {
	public GUI gui;
	public FileHandler fileHandler = new FileHandler();
	private JFrame frame;
	public Vector<SymbolTable> symbolTables = new Vector<SymbolTable>();
	String sourceString = "";
	String parsedString = "";
	private String[] errorMsg = new String[100];

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
	 * Initialize the variables for the program. Adds the corresponding actions to
	 * the buttons, menu tab, etc.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void initializeVariables() {

		/* If the tabbedpane is switched */
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				gui.clearConsole();

				if (index >= 0 && index < symbolTables.size()) {
					gui.setTablesInfo(symbolTables.get(index));
					gui.console(errorMsg[index]);
				} else {
					gui.clearTable();
				}
			}
		};

		gui.tbpEditor.addChangeListener(changeListener);

		/*
		 * Adding a new temp File and adding a tab for it with a text editor.
		 */
		Action newAction = new AbstractAction("New") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				addNewTab("*Untitled_" + (getCurrentTabIndex() + 2) + ".in");
				fileHandler.addFileChooser(new CustomFileChooser("in"));
			}
		};
		newAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmNew.setAction(newAction);

		/*
		 * Open an existing file and put its content to a new text editor also adding a
		 * new tab.
		 */
		Action openAction = new AbstractAction("Open") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = getCurrentTabIndex() + 1;

				if (fileHandler.chooseFile(frame, index)) {
					String fileName = !fileHandler.getFileNameAt(index).isEmpty() ? fileHandler.getFileNameAt(index)
							: "Untitled_" + (+2);
					addNewTab(fileName);
					try {
						gui.setEditorText(fileHandler.getFileContentAt(index));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else {
					if (!fileHandler.isCurrFileAt(index)) {
						JOptionPane.showMessageDialog(frame, "This file does not exist.");
					}
				}
			}
		};
		openAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmOpenFile.setAction(openAction);

		/*
		 * Saving the texts written in the text editor and making an output file for it.
		 */
		Action saveAction = new AbstractAction("Save") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String title = fileHandler.saveFile(gui.getEditorText(), gui.frame, getCurrentTabIndex());
				if (title != null) {
					gui.setTabTitle(title);
				}
			}
		};
		saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmSave.setAction(saveAction);

		/*
		 * Saving the texts written in the text editor and making an output file for it.
		 */
		Action saveAsAction = new AbstractAction("SaveAs") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String title = fileHandler.saveAsFile(gui.getEditorText(), gui.frame, getCurrentTabIndex());
				if (title != null && !title.equals("")) {
					gui.setTabTitle(title);
				}
			}
		};
		saveAsAction.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK));
		gui.mntmSaveAs.setAction(saveAsAction);

		/*
		 * Closes the currently opened tab and deleting/erasing necessary information.
		 */
		Action closeAction = new AbstractAction("Close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int tabIndex = getCurrentTabIndex();
				String tabText = gui.getEditorText() != null ? gui.getEditorText() : "";
				if (tabIndex >= 0 && gui.closeCurrentTab(tabText)) {
					symbolTables.remove(tabIndex);
					fileHandler.removeFileChooserAt(tabIndex);
				}
			}
		};
		closeAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
		gui.mntmClose.setAction(closeAction);

		/*
		 * Compile the current file opened.
		 */
		Action compileAction = new AbstractAction("Compile") {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (gui.tbpEditor.getTabCount() > 0) {
						compile();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		compileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
		gui.mntmCompile.setAction(compileAction);

	}

	/**
	 * Compiles the source program. Only includes lexical analysis for now.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 * @author Alvaro, Cedric Y.
	 */
	private void compile() throws IOException {
		String tokenStream = "";
		String forSyntax = "";
		int index = getCurrentTabIndex();
		String sourceProgram = gui.getEditorText();
		String file = fileHandler.saveFile(sourceProgram, gui.frame, index);

		// if file not found
		if (file == null || file.equals("")) {
			return;
		}

		// stores the selected file and obtained a line
		FileReader selectedFile = new FileReader(
				fileHandler.getFileChooserAt(index).getSelectedFile().getAbsolutePath());
		fileHandler.reader = new BufferedReader(selectedFile);
		String line = fileHandler.reader.readLine();

		// clearing of necessary information
		SymbolTable sb = symbolTables.get(getCurrentTabIndex());
		sb.clear();
		gui.clearConsole();
		errorMsg[getCurrentTabIndex()] = "";

		for (int lineNum = 1; line != null; lineNum++) {
			if (line.equals("")) { // if current line read is empty
				line = fileHandler.reader.readLine();
				continue;
			}

			String lexicalStmt = lexicalAnalyzer(line, lineNum);
			forSyntax += lexicalStmt + "ln ";

			sourceString += line + "\n";
			parsedString += lexicalStmt + "\n";

			tokenStream += lexicalStmt + " ";
			gui.setTablesInfo(symbolTables.get(getCurrentTabIndex()));

			line = fileHandler.reader.readLine(); // reads next line
		}

		forSyntax = forSyntax.trim();
		System.out.println(forSyntax);
		NRPP syntaxAnalyzer = new NRPP(forSyntax);
		errorMsg[getCurrentTabIndex()] += syntaxAnalyzer.getErrors();
		gui.console(errorMsg[getCurrentTabIndex()]);

		// displayAdditionalOutput();
		String varOutput = gui.getVariableTableInformation();

		// create files
		String fileName = fileHandler.createNewFile(tokenStream, gui.frame, ".sobj", sourceProgram, index);
		fileName = fileHandler.createNewFile(varOutput, gui.frame, ".stv", sourceProgram, index);

		gui.setTabTitle(fileName);
		fileHandler.reader.close();
		new SemanticsHandler(parsedString, sourceString, symbolTables.get(getCurrentTabIndex()));
	}

	/**
	 * Adding a new tab for multiple files to be opened simultaneously.
	 * 
	 * @param title the title of the new tab
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void addNewTab(String title) {
		gui.addNewTab(title);
		/*
		 * AHJ: unimplemented; there may be something wrong here; check if mawagtang ang
		 * storing the symbol table
		 */
		symbolTables.add(new SymbolTable());
	}

	/**
	 * Return a string into its lexical form
	 * 
	 * @param line the line/statement to be converted into a lexical string
	 * @return the lexical string of the received line/statement (e.g. : <var-x> =
	 *         <int-2> <op-+> <int-2>)
	 * @see wordsLoop, is a loop that iterates each word found in each line of the
	 *      .in file
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	private String lexicalAnalyzer(String line, int lineNum) {
		String word = "";
		String result = "";
		String runningWord = "";
		SymbolTable sb = symbolTables.get(getCurrentTabIndex());

		/*
		 * String[] words = line.trim().split("\\s"); String result = "";
		 * 
		 * for (String word : words) { String firstLetter = "" + word.charAt(0); if
		 * (isOperator(word)) { // if token is an operator
		 * 
		 * result += " <op-" + word + ">";
		 * 
		 * } else if (isInteger(word)) { // if token is an integer
		 * 
		 * result += " <int-" + word + ">";
		 * 
		 * } else if (isVariable(word)) { // if token is a variable
		 * 
		 * result += " <var-" + word + ">"; if (word.indexOf("+") == 0 ||
		 * word.indexOf("-") == 0) { word = word.substring(1, word.length()); } if
		 * (symbolTables.get(1).findVariable(word) == null) { SymbolTable st = new
		 * SymbolTable(word, "variable", ""); st.getVector().add(st); }
		 * 
		 * } else if (firstLetter.equals("=")) { // if token is an assignment //
		 * operator
		 * 
		 * result += " =";
		 * 
		 * } else { // if the token does not fall in the previous categories, // hence,
		 * encountering an unidentifiable symbol
		 * 
		 * errorMsg += "Lexical error: Invalid symbol " + word + " (line " + lineNum +
		 * ")\n"; return "error: Lexical error - " + word;
		 * 
		 * }
		 * 
		 * return result; }
		 */

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			int lastIndex = line.length() - 1;
			runningWord = word + c;

			if ((c == ' ' && !word.equals("")) || ((i == lastIndex) && (runningWord != "" && runningWord != " "))) {

				if ((i == lastIndex) && c != ' ') {
					word += c;
				}

				String acceptedStrings[] = { "INTO", "DEFINE", "COMMAND", "END", "FLOAT", "INT", "STR", "IS", "INT_LIT",
						"FLOAT_LIT", "ADD", "SUB", "DIV", "MULT", "MOD", "LT?", "LTE?", "GT?", "GTE?", "EQ?", "NEQ?",
						"AND?", "OR?", "NOT?", "NEWLN", "PRINT", "BEG", "IF", "ELSE", "ENDIF", "FROM", "TO",
						"ENDFROM" };

				for (int j = 0; j < acceptedStrings.length; j++) {
					if (word.equals(acceptedStrings[j])) {
						sb.add(word, word, "", "");
						result += word + " ";
						word = "";
						break;
					}
				}

				if (word != "") {
					if (isVariable(word)) {
						if (sb.findVariable(word) == null) {
							sb.add(word, "IDENT", "", "");
							// System.out.println("FINDVAR: " +
							// sb.findVariable(word).getLexeme());
						}
						result += "IDENT ";
						word = "";
					} else if (isInteger(word)) {
						sb.add(word, "INT_LIT", "INT", word);
						result += "INT_LIT ";
						word = "";
					} else if (isFloat(word)) {
						sb.add(word, "FLOAT_LIT", "FLOAT", word);
						result += "FLOAT_LIT ";
						word = "";
					} else if (word.charAt(0) == '*') {
						System.out.println("word:" + word + ":");

						String holder = "";
						holder = word + line.substring(i, lastIndex + 1);
						System.out.println("holder:" + holder + ":");
						int last = holder.lastIndexOf('*');
						word = holder;
						System.out.println("last:" + last);
						if (last == 0) {
							System.out.println("1");
							result += "ERR_LEX ";
							errorMsg[getCurrentTabIndex()] += "(Line #" + lineNum
									+ ") Lexical Error: Error Comment format " + word + "\n";
							break;
						}

						if (line.length() == 1) {
							result += "ERR_LEX ";
							errorMsg[getCurrentTabIndex()] += "(Line #" + lineNum
									+ ") Lexical Error: Error Comment format " + word + "\n";
							break;
						}

						result += "COMMENT ";
						i = line.lastIndexOf('*');
						word = "";
					} else {
						result += "ERR_LEX ";
						errorMsg[getCurrentTabIndex()] += "(Line #" + lineNum + ") Lexical Error: Undefined symbol "
								+ word + "\n";
						word = "";
					}
				}

			}

			if (c != ' ' && i < lastIndex) {
				word += c;
			}

		}

		System.out.println("running:" + result);
		return result;

	}

	public int CharCounter(String x, Character y) {
		int counter = 0;
		for (int i = 0; i < x.length(); i++) {
			if (x.charAt(i) == 'y') {
				counter++;
			}
		}
		return counter;
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

	/**
	 * Determines whether the received word is a valid variable or not
	 * 
	 * @param word word to be checked
	 * @return true if word is a valid variable; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean isVariable(String word) {
		if (word != "") {
			String firstLetter = "" + word.charAt(0);
			if (word.length() > 1
					? (firstLetter.matches("[a-zA-Z]+") || firstLetter == "_")
							&& word.substring(1, word.length()).matches("[a-zA-Z0-9_ ]+")
					: word.matches("[a-zA-Z]+")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Determines whether the received word is a valid variable or not
	 * 
	 * @param word word to be checked
	 * @return true if word is a valid variable; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean isAlphabet(Character c) {
		int ascii = (int) c;
		if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122)) {
			return true;
		}
		return false;
	}

	/**
	 * Determines whether the received word is an operator or not
	 * 
	 * @param word word to be checked
	 * @return true if word is an operator; false if not
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	// private boolean isOperator(String word) {
	// String operator = new String("+-/*%");
	//
	// return word.length() == 1 && operator.contains(word);
	// }

	/**
	 * Determines whether received element is numeric or not
	 * 
	 * @param check the element to be checked
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
	 * Obtains index of the current opened tab
	 * 
	 * @return index of current opened tab; returns negative if tab pane has no more
	 *         tab
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public int getCurrentTabIndex() {
		return gui.tbpEditor.getSelectedIndex();
	}
}