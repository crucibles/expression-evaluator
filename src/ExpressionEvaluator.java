import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Vector;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ExpressionEvaluator {

	private JFrame frame;

	private boolean flag = true;
	private BufferedReader reader;
	private FileReader selectedFile;
	private JFileChooser fileChooser = new JFileChooser();
	private Vector<SymbolTable> symbolTable = new Vector<SymbolTable>();

	private JTextField tfDocUrl;
	private JTextPane tpOutput = new JTextPane();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExpressionEvaluator window = new ExpressionEvaluator();
					window.frame.setVisible(true);
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
		initializeVariables();
		initializeFrameContents();
	}

	/**
	 * Initialize the variables for the program.
	 */
	private void initializeVariables() {
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrameContents() {
		frame = new JFrame();
		frame.setBounds(100, 100, 499, 334);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		initializeHomePanel();
		initializeOpenFilePanel();
		initializeViewOutputPanel();
		initializeDescriptionPanel();
	}

	/**
	 * Initializes the contents of the home panel
	 */
	private void initializeHomePanel() {
		//home panel
		JPanel homePanel = new JPanel();
		homePanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(homePanel, "homePanel");
		homePanel.setLayout(null);

		//button for going to 'Open File' panel
		JButton btnOpenFile = new JButton();
		btnOpenFile.setText("Open File");
		btnOpenFile.setForeground(Color.DARK_GRAY);
		btnOpenFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnOpenFile.setBounds(173, 146, 160, 45);
		btnOpenFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnOpenFile.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnOpenFile);

		//button for going to 'View Output' panel
		JButton btnViewOutput = new JButton();
		btnViewOutput.setText("View Output");
		btnViewOutput.setForeground(Color.DARK_GRAY);
		btnViewOutput.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnViewOutput.setBounds(173, 197, 160, 45);
		btnViewOutput.setHorizontalAlignment(SwingConstants.CENTER);
		btnViewOutput.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnViewOutput);

		//button for going to 'Description' panel
		JButton btnDescription = new JButton();
		btnDescription.setVerticalTextPosition(SwingConstants.CENTER);
		btnDescription.setText("Description");
		btnDescription.setHorizontalAlignment(SwingConstants.CENTER);
		btnDescription.setForeground(Color.DARK_GRAY);
		btnDescription.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnDescription.setBounds(173, 249, 160, 45);
		homePanel.add(btnDescription);

		//welcoming text
		JLabel welcomeText = new JLabel("Expression Evaluator Program");
		welcomeText.setForeground(Color.DARK_GRAY);
		welcomeText.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeText.setBounds(70, 56, 353, 57);
		homePanel.add(welcomeText);

		//mouse listener for 'Open File' button
		btnOpenFile.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "openFilePanel");
			}
		});

		//mouse listener for 'View Output' button
		btnViewOutput.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "viewOutputPanel");
			}
		});

		//mouse listener for 'Description' button
		btnDescription.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "descriptionPanel");
			}
		});
	}

	/**
	 * Initializes the contents of the openfile panel
	 */
	private void initializeOpenFilePanel() {
		JPanel openFilePanel = new JPanel();
		openFilePanel.setBackground(SystemColor.inactiveCaption);
		openFilePanel.setLayout(null);
		frame.getContentPane().add(openFilePanel, "openFilePanel");

		// program title
		JLabel programTitle = new JLabel("Expression Evaluator Program");
		programTitle.setHorizontalAlignment(SwingConstants.CENTER);
		programTitle.setForeground(Color.DARK_GRAY);
		programTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		programTitle.setBounds(70, 0, 353, 51);
		openFilePanel.add(programTitle);

		// the small arrow button in the upperright corner
		JLabel btnsmHome = new JLabel("");
		btnsmHome.setBackground(SystemColor.inactiveCaption);
		btnsmHome.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome.setForeground(Color.WHITE);
		btnsmHome.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome.setBounds(443, 11, 40, 40);
		btnsmHome.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		openFilePanel.add(btnsmHome);

		// button for loading file
		JButton btnLoadFile = new JButton();
		btnLoadFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnLoadFile.setText("Load File");
		btnLoadFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadFile.setForeground(Color.DARK_GRAY);
		btnLoadFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnLoadFile.setBounds(265, 174, 160, 45);
		openFilePanel.add(btnLoadFile);

		// button for choosing file
		JButton btnChooseFile = new JButton();
		btnChooseFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnChooseFile.setText("Choose File");
		btnChooseFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnChooseFile.setForeground(Color.DARK_GRAY);
		btnChooseFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnChooseFile.setBounds(58, 174, 160, 45);
		openFilePanel.add(btnChooseFile);

		// textfield for the document url
		tfDocUrl = new JTextField();
		tfDocUrl.setEditable(false);
		tfDocUrl.setEnabled(false);
		tfDocUrl.setBounds(32, 128, 423, 35);
		openFilePanel.add(tfDocUrl);
		tfDocUrl.setColumns(10);

		//mouse listener for choosing file
		btnChooseFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chooseFile();
			}
		});

		//mouse listener for loading file
		btnLoadFile.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				loadFile();
				// only goes to the output panel if the extension is correct (.in)
				if (flag == true) {
					cl.show(frame.getContentPane(), "viewOutputPanel");
				}
			}
		});

		//mouse listener for small arrow home button
		btnsmHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				goToHomePanel();
			}
		});
	}

	/**
	 * Initializes the contents of the viewoutput panel
	 */
	private void initializeViewOutputPanel() {
		// 'View Output' panel
		JPanel viewOutputPanel = new JPanel();
		viewOutputPanel.setBackground(SystemColor.inactiveCaption);
		viewOutputPanel.setLayout(null);

		// small arrow button for home
		JLabel btnsmHome = new JLabel("");
		btnsmHome.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome.setForeground(Color.WHITE);
		btnsmHome.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome.setBounds(443, 11, 40, 40);
		btnsmHome.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		viewOutputPanel.add(btnsmHome);

		// program title
		JLabel programTitle = new JLabel("Expression Evaluator Program");
		programTitle.setBounds(74, 0, 345, 51);
		viewOutputPanel.add(programTitle);
		programTitle.setHorizontalAlignment(SwingConstants.CENTER);
		programTitle.setForeground(Color.DARK_GRAY);
		programTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		frame.getContentPane().add(viewOutputPanel, "viewOutputPanel");

		// output label
		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setHorizontalAlignment(SwingConstants.LEFT);
		lblOutput.setForeground(Color.DARK_GRAY);
		lblOutput.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 15));
		lblOutput.setBounds(20, 42, 85, 27);
		viewOutputPanel.add(lblOutput);

		// button for loading file
		JButton btnLoadFile = new JButton();
		btnLoadFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnLoadFile.setText("Load File");
		btnLoadFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadFile.setForeground(Color.DARK_GRAY);
		btnLoadFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnLoadFile.setBounds(20, 260, 160, 34);
		btnLoadFile.setToolTipText("Click here if you made changes to the input file and want to load it again.");
		viewOutputPanel.add(btnLoadFile);

		// button for processing
		JButton btnProcess = new JButton();
		btnProcess.setVerticalTextPosition(SwingConstants.CENTER);
		btnProcess.setText("Process");
		btnProcess.setHorizontalAlignment(SwingConstants.CENTER);
		btnProcess.setForeground(Color.DARK_GRAY);
		btnProcess.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnProcess.setBounds(306, 259, 160, 36);
		btnProcess.setToolTipText("Click here if you want to process the most recently opened file.");
		viewOutputPanel.add(btnProcess);

		//scroll pane for the output text
		JScrollPane spOutput;
		spOutput = new JScrollPane();
		spOutput.setBounds(20, 80, 447, 176);
		viewOutputPanel.add(spOutput);

		//text pane for the output text
		tpOutput.setEditable(false);
		tpOutput.setFont(new Font("Arial", Font.PLAIN, 13));
		spOutput.setViewportView(tpOutput);

		//loads file on click
		btnLoadFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadFile();
			}
		});

		//process on click
		btnProcess.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					process();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		//goes to Home panel on click
		btnsmHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				goToHomePanel();
			}
		});
	}

	private void initializeDescriptionPanel() {
		// description panel
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(null);
		descriptionPanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(descriptionPanel, "descriptionPanel");

		// small arrow home button
		JLabel btnsmHome = new JLabel("");
		btnsmHome.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome.setForeground(Color.WHITE);
		btnsmHome.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		btnsmHome.setBounds(443, 11, 40, 40);
		descriptionPanel.add(btnsmHome);

		// program title
		JLabel programTitle = new JLabel("Expression Evaluator Program");
		programTitle.setHorizontalAlignment(SwingConstants.CENTER);
		programTitle.setForeground(Color.DARK_GRAY);
		programTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		programTitle.setBounds(74, 0, 345, 51);
		descriptionPanel.add(programTitle);

		// description label
		JLabel lblDescrpt = new JLabel("Program Description:");
		lblDescrpt.setHorizontalAlignment(SwingConstants.LEFT);
		lblDescrpt.setForeground(Color.DARK_GRAY);
		lblDescrpt.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 15));
		lblDescrpt.setBounds(20, 42, 145, 27);
		descriptionPanel.add(lblDescrpt);

		// scroll pane for descrption text
		JScrollPane spDescription = new JScrollPane();
		spDescription.setBounds(20, 80, 447, 214);
		descriptionPanel.add(spDescription);

		// text pane for description text
		JTextPane tpDescription = new JTextPane();
		tpDescription.setEditable(false);
		tpDescription.setFont(new Font("Arial", Font.PLAIN, 13));
		spDescription.setViewportView(tpDescription);

		// goes to home panel on click
		btnsmHome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				goToHomePanel();
			}
		});
	}

	/**
	 * Choose file from the directory
	 */
	private void chooseFile() {
		int file = fileChooser.showOpenDialog(frame);
		if (file == JFileChooser.APPROVE_OPTION) {
			tfDocUrl.setText(fileChooser.getSelectedFile().getAbsolutePath().toString());
		} else {

		}
	}

	/**
	 * Gets the name of the file selected.
	 */
	private String getFileName() {
		return fileChooser.getSelectedFile().getName();
	}

	/**
	 * Gets the extension of the file selected.
	 * @return file's extension
	 */
	private String getExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1, fileName.length());
	}

	/**
	 * Load the file of the given url
	 */
	private void loadFile() {
		tpOutput.setText("");
		if (tfDocUrl.getText().equals("")) {
			JOptionPane.showMessageDialog(frame, "Please choose a file first.");
			flag = false;
		} else if (getExtension(getFileName()).equals("in")) {
			flag = true;
			System.out.println("loading a valid file...");
		}
	}

	/**
	 * Processes the most recently opened valid file. Only works if there has been a valid file opened. 
	 * This function contains the lexical, syntax and semantic analyzer as well as the execution of the file
	 * @throws IOException
	 */
	private void process() throws IOException {
		LinkedList<String> postFix = new LinkedList<String>();
		int result = 0;
		String strPostFix = "";

		selectedFile = new FileReader(fileChooser.getSelectedFile().getAbsolutePath());
		reader = new BufferedReader(selectedFile);
		String line = reader.readLine();
		tpOutput.setText("");

		while (line != null) {

			String lexicalString = lexicalAnalyzer(line);
			if (lexicalString.equals("err")) { //returns to home panel if loaded file has lexical errors
				goToHomePanel();
				break;
			}

			String checker = syntaxAnalyzer(lexicalString);
			if (checker.substring(0, 3).equals("err")) { // prompts user of error when encountered
				JOptionPane.showMessageDialog(frame, "Syntax error!");
			} else {
				//AHJ: unimplemented; error checking for dual '=' sign
				String expr = getExpression(lexicalString);
				if (!isUndefined(expr)) { // if the variables on the RHS has values
					postFix = toPostFix(expr);
					strPostFix = toPostFixString(postFix);
					result = evaluateExpression(postFix);
					storeResult(result, lexicalString);
				} else {
					checker = "error: Variable does not have value yet.";
				}
			}

			displayOutput(line, checker, strPostFix, result);
			line = reader.readLine();
		}
		reader.close();
		System.out.println(tpOutput.getText());
		print(tpOutput.getText());
		//}
		//}
	}

	private void displayOutput(String line, String checker, String strPostFix, int result) {
		StyledDocument doc = tpOutput.getStyledDocument();
		try {
			String outputLine = "Line1: " + line + "\n";
			doc.insertString(doc.getLength(), outputLine, null);

			if (checker.substring(0, 3).equals("err")) {
				outputLine = "Result: " + checker + "\n\n";
				doc.insertString(doc.getLength(), outputLine, null);
				return;
			} else {
				outputLine = "Postfix: " + getLHS(line) + " " + strPostFix + "\n";
				doc.insertString(doc.getLength(), outputLine, null);

				outputLine = "Result: " + result + "\n\n";
				doc.insertString(doc.getLength(), outputLine, null);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void storeResult(int value, String stmt) {
		String tokens[] = stmt.split("\\s");
		for (int i = 0; i < tokens.length; i++) {
			if (!tokens[i].isEmpty() && tokens[i].equals("=")) {
				String var = tokens[i - 1].substring(5, tokens[i-1].length() - 1);
				for (int index = 0; index < symbolTable.size(); index++) {
					SymbolTable sb = symbolTable.get(index);
					if (var.equals(sb.token)) {
						sb.setValue(sb, Integer.toString(value));
						symbolTable.set(index, sb);
						System.out.println("Store Result: ");
						showSymbolTable(sb);
						break;
					}
				}
			}
			i++;
		}

	}

	public void print(String output) throws IOException {
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
	 * Return a string into its lexical form
	 * 
	 * @param String:  x = 2 + 2 
	 * @return String: with a format <var-x> = <int-2> <op-+> <int-2>
	 * @see wordsLoop, is a loop that iterates each word found in each line of the .in file
	 * 
	 */
	private String lexicalAnalyzer(String line) {
		String[] words = line.split("\\s");
		String result = "";
		String operator = "*+-/%";

		for (String word : words) {
			System.out.println(word);
			String firstLetter = "" + word.charAt(0);
			if (word.length() == 1 && operator.indexOf(word.charAt(0)) >= 0) {
				SymbolTable st = new SymbolTable(word, "operator", "");
				symbolTable.add(st);
				result += " <op-" + word + ">";

			} else if (
				(
					operator.indexOf(word.charAt(0))>=0 && 
					word.length()>1? word.substring(1, word.length()).matches("[0-9]+"): false
				) || (
					word.matches("[0-9]+")
				)			
			) {
				SymbolTable st = new SymbolTable(word, "integer", "");
				symbolTable.add(st);
				result += " <int-" + word + ">";

			} else if (

				word.length()>1? 
				(firstLetter.equals("-") || firstLetter.equals("+") || firstLetter.equals("_") || firstLetter.matches("[a-zA-Z ]+")) && 
				word.substring(1, word.length()).matches("[^a-zA-Z0-9 ]+")
				:
				word.matches("[a-zA-Z]+")


			) {
				result += " <var-" + word + ">";
				if(word.indexOf("+") >= 0 || word.indexOf("-") >= 0){
					word = word.substring(1, word.length());
				}
				if (findVariable(word) == null) {
					System.out.println("inserting new var");
					SymbolTable st = new SymbolTable(word, "variable", "");
					symbolTable.add(st);
				}

			} else if (firstLetter.equals("=")) {
				SymbolTable st = new SymbolTable(word, "=", "");
				symbolTable.add(st);
				result += " =";

			} else {

				JOptionPane.showMessageDialog(frame, "Error token: " + word);
				return "err";

			}
		}

		return result;
	}

	private SymbolTable findVariable(String var) {
		SymbolTable sb = new SymbolTable();
		for (int index = 0; index < symbolTable.size(); index++) {
			sb = symbolTable.get(index);
			if (var.equals(sb.token)) {
				return sb;
			}
		}
		return null;
	}

	private boolean isUndefined(String expr) {
		String[] tokens = expr.split("\\s");

		for (int i = 0; i < tokens.length; i++) {
			if (!tokens[i].isEmpty() && tokens[i].substring(1, 4).equals("var")) {
				String var = tokens[i].substring(5, tokens[i].length() - 1);
				for (int j = 0; j < symbolTable.size(); j++) {
					SymbolTable sb = symbolTable.get(j);
					if (var.equals(sb.token) && sb.value.equals("")) {
						JOptionPane.showMessageDialog(frame, "Undefined variable: " + var);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Accessing the fields of a symbol table in a string
	 */
	private void showSymbolTable(SymbolTable st) {

		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(st.getClass().getName());
		result.append(" Object {");
		result.append(newLine);

		Field[] fields = st.getClass().getDeclaredFields();
		for (Field field : fields) {
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				result.append(field.get(st));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		System.out.println(result);
	}

	private String getExpression(String line) {
		int index = line.lastIndexOf("=");
		return line.substring(index + 1, line.length());
	}

	private String getLHS(String line) {
		int index = line.lastIndexOf("=");
		return line.substring(0, index + 1);
	}

	private String syntaxAnalyzer(String line) {
		//AHJ: unimplemented; no error checking for multiple valid LHS
		String[] tokens = line.split("\\s");
		int index = 0;

		for (String token : tokens) {
			if (!token.isEmpty() && token.equals("=")) {
				String var = tokens[index - 1].substring(1, 4);
				String eq = tokens[index];
				if (!var.equals("var") || !eq.equals("=")) {
					return "error5: Syntax error - Incorrect left-hand side element.";
				}
			}
			index++;
		}

		tokens = getExpression(line).split("\\s");
		for (int i = 0; i < tokens.length; i++) {

			if (tokens[i].isEmpty()) {
				continue;
			}

			/*
			returns error if...
			- end of the expression is not a variable or a number
			- operator not surround by number/var
			- adjacent variables
			*/
			if (i == tokens.length - 1) {
				if (!tokens[i].substring(1, 4).equals("int") && !tokens[i].substring(1, 4).equals("var")) {
					return "error1: Hanging operator not allowed";
				} else {
					return "accept";
				}
			} /*else if (tokens[i].equals("=")) {
				return "error5: Syntax error - Incorrect left-hand side element.";
				}*/else if (tokens[i].substring(1, 3).equals("op") && tokens[i + 1].substring(1, 3).equals("op")) {
				return "error2: Syntax error - Invalid operator";
			} else if (tokens[i].substring(1, 3).equals("op")
					&& !(tokens[i - 1].substring(1, 4).equals("int") || tokens[i - 1].substring(1, 4).equals("var"))
					&& !(tokens[i + 1].substring(1, 4).equals("int") || tokens[i + 1].substring(1, 4).equals("var"))) {
				return "error3: Syntax error - Operator not surrounded by valid numbers/variables";
			} else if ((tokens[i].substring(1, 4).equals("var") || tokens[i].substring(1, 4).equals("int"))
					&& (tokens[i + 1].substring(1, 4).equals("var") || tokens[i + 1].substring(1, 4).equals("int"))) {
				return "error4: Syntax error - Consecutive order variable or number";
			}

		}
		return "accept";

	}

	/**
	 * Returns the postfix form of an expression
	 * @
	 */
	private LinkedList<String> toPostFix(String expr) {
		System.out.println(expr);
		Stack<String> stack = new Stack<String>();
		LinkedList<String> postFix = new LinkedList<String>();
		String[] tokens = expr.split("\\s");
		for (int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i]);
			if (tokens[i].isEmpty()) {
				continue;
			}

			if (tokens[i].substring(1, 4).equals("int")) {
				String number = tokens[i].substring(5, tokens[i].length() - 1);
				postFix.add(number);
			} else if (tokens[i].substring(1, 4).equals("var")) {
				boolean isPositive = tokens[i].charAt(5) != '-';
				System.out.println(isPositive);
				String var = isPositive? tokens[i].substring(5, tokens[i].length() - 1): tokens[i].substring(6, tokens[i].length() - 1);
				SymbolTable sb = findVariable(var);
				
				String number = sb.value;
				if(!isPositive && sb.value.charAt(0) != '-'){
					number = "-" + sb.value;
				} else if(!isPositive && sb.value.charAt(0) == '-'){
					number = sb.value.substring(1);
				}

				//AHJ: unimplemented; find variable in symbol; if not found, return error
				// String number = var;
				//String number = "21";
				postFix.add(number);
			} else if (tokens[i].substring(1, 3).equals("op")) {
				String op = "" + tokens[i].charAt(4);
				if (!stack.isEmpty()) {
					while (!stack.isEmpty() && isHighOrEqualPrecedence(stack.peek(), op)) {
						String poppedElem = stack.pop();
						postFix.add(poppedElem);
					}
				}
				stack.push(op);
			}
		}

		while (!stack.isEmpty())

		{
			String poppedElem = stack.pop();
			postFix.add(poppedElem);
		}

		return postFix;
	}

	private String toPostFixString(LinkedList<String> postFix) {
		String strPostFix = "";

		for (int i = 0; i < postFix.size(); i++) {
			strPostFix += postFix.get(i) + " ";
		}

		return strPostFix;
	}

	private boolean isHighOrEqualPrecedence(String firstElem, String secondElem) {
		int firstPrec = 0;
		switch (firstElem) {
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
		case "/":
		case "*":
			secondPrec = 2;
			break;
		case "+":
		case "-":
			secondPrec = 1;
			break;
		}

		if (firstPrec >= secondPrec) {
			return true;
		} else {
			return false;
		}
	}

	private int evaluateExpression(LinkedList<String> postFix) {
		Stack<String> stack = new Stack<String>();
		while (!postFix.isEmpty()) {
			String poppedElem = postFix.pop().toString();
			if (isNumeric(poppedElem)) {
				stack.push(poppedElem);
			} else {
				int firstElem = Integer.parseInt(stack.pop());
				int secondElem = Integer.parseInt(stack.pop());
				int result = 0;
				switch (poppedElem) {
				case "+":
					result = firstElem + secondElem;
					break;
				case "-":
					result = firstElem - secondElem;
					break;
				case "*":
					result = firstElem * secondElem;
					break;
				case "/":
					result = firstElem / secondElem;
					break;
				}
				stack.push(Integer.toString(result));
			}
		}

		int answer = Integer.parseInt(stack.pop());
		return answer;
	}

	public boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	private void goToHomePanel() {
		CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
		cl.show(frame.getContentPane(), "homePanel");
	}

}

class SymbolTable {
	String token;
	String type;
	String value;

	public SymbolTable() {

	}

	public SymbolTable(String token, String type, String value) {
		this.token = token;
		this.type = type;
		this.value = value;
	}

	public SymbolTable(String token, String type) {
		this.token = token;
		this.type = type;
		this.value = "";
	}

	public String getToken(SymbolTable st) {
		return st.token;
	}

	public String getType(SymbolTable st) {
		return st.type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(SymbolTable st, String value) {
		st.value = value;
		this.value = value;
	}

}