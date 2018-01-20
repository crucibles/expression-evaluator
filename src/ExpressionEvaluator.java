import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.SystemColor;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class ExpressionEvaluator extends MouseAdapter {

	private JFrame frame;

	private boolean flag = true;
	private BufferedReader reader;
	private FileReader selectedFile;
	private JFileChooser fileChooser = new JFileChooser();
	private JButton btnOpenFile = new JButton();
	private JButton btnViewOutput = new JButton();
	private JButton btnLoadFile = new JButton();
	private JButton btnChooseFile = new JButton();
	private JButton btnLoadFile_1 = new JButton();
	private JButton btnProcess = new JButton();
	private JLabel btnsmHome_1 = new JLabel("");
	private JLabel btnsmHome = new JLabel("");
	private String operator = new String("+-/*%");
	private Vector<SymbolTable> symbolTable = new Vector<SymbolTable>();

	private JTextField tfDocUrl;
	private String fileContent = new String("");

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
	}

	/**
	 * Initializes the contents of the home panel
	 */
	private void initializeHomePanel() {
		JPanel homePanel = new JPanel();
		homePanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(homePanel, "homePanel");
		homePanel.setLayout(null);

		btnOpenFile.setText("Open File");
		btnOpenFile.setForeground(Color.DARK_GRAY);
		btnOpenFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnOpenFile.setBounds(173, 193, 160, 45);
		btnOpenFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnOpenFile.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnOpenFile);

		btnViewOutput.setText("View Output");
		btnViewOutput.setForeground(Color.DARK_GRAY);
		btnViewOutput.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnViewOutput.setBounds(173, 249, 160, 45);
		btnViewOutput.setHorizontalAlignment(SwingConstants.CENTER);
		btnViewOutput.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnViewOutput);

		JLabel welcomeText = new JLabel("Expression Evaluator Program");
		welcomeText.setForeground(Color.DARK_GRAY);
		welcomeText.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeText.setBounds(70, 56, 353, 57);
		homePanel.add(welcomeText);

		btnOpenFile.addMouseListener(this);
		btnViewOutput.addMouseListener(this);
	}

	/**
	 * Initializes the contents of the openfile panel
	 */
	private void initializeOpenFilePanel() {
		JPanel openFilePanel = new JPanel();
		openFilePanel.setBackground(SystemColor.inactiveCaption);
		openFilePanel.setLayout(null);
		frame.getContentPane().add(openFilePanel, "openFilePanel");

		JLabel programTitle = new JLabel("Expression Evaluator Program");
		programTitle.setHorizontalAlignment(SwingConstants.CENTER);
		programTitle.setForeground(Color.DARK_GRAY);
		programTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		programTitle.setBounds(70, 0, 353, 51);
		openFilePanel.add(programTitle);
		btnsmHome_1.setBackground(SystemColor.inactiveCaption);

		btnsmHome_1.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome_1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome_1.setForeground(Color.WHITE);
		btnsmHome_1.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome_1.setBounds(443, 11, 40, 40);
		btnsmHome_1.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome_1.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		openFilePanel.add(btnsmHome_1);

		btnLoadFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnLoadFile.setText("Load File");
		btnLoadFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadFile.setForeground(Color.DARK_GRAY);
		btnLoadFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnLoadFile.setBounds(265, 174, 160, 45);
		openFilePanel.add(btnLoadFile);

		btnChooseFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnChooseFile.setText("Choose File");
		btnChooseFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnChooseFile.setForeground(Color.DARK_GRAY);
		btnChooseFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnChooseFile.setBounds(58, 174, 160, 45);
		openFilePanel.add(btnChooseFile);

		tfDocUrl = new JTextField();
		tfDocUrl.setEditable(false);
		tfDocUrl.setEnabled(false);
		tfDocUrl.setBounds(32, 128, 423, 35);
		openFilePanel.add(tfDocUrl);
		tfDocUrl.setColumns(10);

		btnChooseFile.addMouseListener(this);
		btnLoadFile.addMouseListener(this);
		btnsmHome_1.addMouseListener(this);
	}

	/**
	 * Initializes the contents of the viewoutput panel
	 */
	private void initializeViewOutputPanel() {

		JPanel viewOutputPanel = new JPanel();
		viewOutputPanel.setBackground(SystemColor.inactiveCaption);
		viewOutputPanel.setLayout(null);

		btnsmHome.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome.setForeground(Color.WHITE);
		btnsmHome.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome.setBounds(443, 11, 40, 40);
		btnsmHome.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		viewOutputPanel.add(btnsmHome);

		btnsmHome.addMouseListener(this);

		JLabel programTitle = new JLabel("Expression Evaluator Program");
		programTitle.setBounds(74, 0, 345, 51);
		viewOutputPanel.add(programTitle);
		programTitle.setHorizontalAlignment(SwingConstants.CENTER);
		programTitle.setForeground(Color.DARK_GRAY);
		programTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		frame.getContentPane().add(viewOutputPanel, "viewOutputPanel");

		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setHorizontalAlignment(SwingConstants.LEFT);
		lblOutput.setForeground(Color.DARK_GRAY);
		lblOutput.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 15));
		lblOutput.setBounds(20, 42, 85, 27);
		viewOutputPanel.add(lblOutput);

		btnLoadFile_1.setVerticalTextPosition(SwingConstants.CENTER);
		btnLoadFile_1.setText("Load File");
		btnLoadFile_1.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadFile_1.setForeground(Color.DARK_GRAY);
		btnLoadFile_1.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnLoadFile_1.setBounds(20, 260, 160, 34);
		btnLoadFile_1.setToolTipText("Click here if you made changes to the input file and want to load it again.");
		viewOutputPanel.add(btnLoadFile_1);

		btnProcess.setVerticalTextPosition(SwingConstants.CENTER);
		btnProcess.setText("Process");
		btnProcess.setHorizontalAlignment(SwingConstants.CENTER);
		btnProcess.setForeground(Color.DARK_GRAY);
		btnProcess.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnProcess.setBounds(306, 259, 160, 36);
		btnProcess.setToolTipText("Click here if you want to process the most recently opened file.");
		viewOutputPanel.add(btnProcess);

		JScrollPane spOutput = new JScrollPane();
		spOutput.setBounds(20, 80, 447, 176);
		viewOutputPanel.add(spOutput);

		btnLoadFile_1.addMouseListener(this);
		btnProcess.addMouseListener(this);
	}

	/**
	 * Choose file from the directory
	 */
	private void chooseFile() {
		System.out.println("choose file!");
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
	 */
	private String getExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1, fileName.length());
	}

	/**
	 * Load the file of the given url
	 * 
	 */
	private void loadFile() {
		fileContent = "";
		System.out.println("hello");
		if (tfDocUrl.getText().equals("")) {
			System.out.println("Please choose a file first.");
			JOptionPane.showMessageDialog(frame, "Please choose a file first.");
			flag = false;
		} else if (getExtension(getFileName()).equals("in")) {
			flag = true;
			System.out.println("loading a valid file...");
		}
	}

	/**
	 * Processes the most recently opened valid file. Only works if there has been a valid file opened.
	 * 
	 * @throws IOException
	 * @return <var-x> = <int-10> <op-+> <int-31>
	 */
	private void process() throws IOException {
		selectedFile = new FileReader(fileChooser.getSelectedFile().getAbsolutePath());
		reader = new BufferedReader(selectedFile);
		String line = reader.readLine();

		while (line != null) {
			fileContent += line + "\n";

			//String line = "x = 4 + 2";
			String lexicalString = lexicalAnalyzer(line);
			System.out.println(lexicalString);
			String checker = syntaxAnalyzer(lexicalString);
			if (checker.equals("err")) {
				System.out.println("Error in ordering the elements.");
			} else {
				//String expr = getExpression(lexicalString);
				//String postFix = toPostFix(expr);
				//int result = evaluateExpression(postFix);
			}
			line = reader.readLine();
		}
		reader.close();
		System.out.println(fileContent);
		//AHJ: unimplemented; checks if there is a local file already stored. If yes, process. If no, show error message

		//<---- codes here for file checking

		//AHJ: loop here the lines of codes
		//for(){

		//}
		//}
	}

	/**
	 * Return a string into its lexical form
	 * 
	 * @input String:  x = 2 + 2 
	 * @Output String: <var-x> = <int-2> <op-+> <int-2>
	 * 
	 */
	private String lexicalAnalyzer(String line) {
		String[] words = line.split("\\s");
		String result = "";

		for (String word : words) {
			System.out.println(word);
			String firstLetter = "" + word.charAt(0);
			if (word.length() == 1 && operator.indexOf(word.charAt(0)) == 0) {
				System.out.println("im in the op");
				SymbolTable st = new SymbolTable(word, "operator", "");
				showSymbolTable(st);
				symbolTable.add(st);
				result += " <op-" + word + ">";
			} else if (!firstLetter.equals("=") && (firstLetter.equals("_") || firstLetter.matches("[a-zA-Z ]+")
					|| word.substring(1, word.length()).matches("[^a-zA-Z0-9 ]+"))) {
				System.out.println("im in the var or identifier");
				SymbolTable st = new SymbolTable(word, "variable", "");
				showSymbolTable(st);
				symbolTable.add(st);
				result += " <var-" + word + ">";
			} else if (word.matches("[0-9]+")) {
				System.out.println("im in the integer");
				SymbolTable st = new SymbolTable(word, "integer", "");
				showSymbolTable(st);
				symbolTable.add(st);
				result += " <int-" + word + ">";
			} else {
				System.out.println("What is this word?!!!");
			}

			System.out.println(result);

		}

		/*
		Sample snippet for accessing/updating the symbol table
		
		for (int index = 0; index < symbolTable.size(); index++) {
			SymbolTable sb = symbolTable.get(index);
			System.out.println("The current token" + sb.token);
		}
		
		*/

		return "done";
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

		System.out.println(result.toString());
	}

	/**
	 * Initialize the variables for the program.
	 */
	private void initializeVariables() {
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		fileChooser.setAcceptAllFileFilterUsed(false);
	}

	private String syntaxAnalyzer(String lexString) {
		return "err";
	}

	private boolean semanticAnalyzer() {
		return false;
	}

	/**
	 * Returns the postfix form of an expression
	 */
	private String toPostFix() {
		return "String";
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
		if (e.getSource() == btnOpenFile) {
			cl.show(frame.getContentPane(), "openFilePanel");
		} else if (e.getSource() == btnViewOutput) {
			cl.show(frame.getContentPane(), "viewOutputPanel");
		} else if (e.getSource() == btnsmHome_1 || e.getSource() == btnsmHome) {
			cl.show(frame.getContentPane(), "homePanel");
		}

		if (e.getSource() == btnChooseFile) {
			chooseFile();
		}
		if (e.getSource() == btnProcess) {
			try {
				process();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == btnLoadFile || e.getSource() == btnLoadFile_1) {
			loadFile();
			if (e.getSource() == btnLoadFile && flag == true) {
				cl.show(frame.getContentPane(), "viewOutputPanel");
			}
		}
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

	public String getValue(SymbolTable st) {
		return st.value;
	}

	public void setValue(SymbolTable st, String value) {
		st.value = value;
	}

}