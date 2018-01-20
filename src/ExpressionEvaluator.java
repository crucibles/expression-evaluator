import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

public class ExpressionEvaluator{

	private JFrame frame;
	private JFileChooser fileChooser = new JFileChooser();
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
		JPanel homePanel = new JPanel();
		homePanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(homePanel, "homePanel");
		homePanel.setLayout(null);

		JButton btnOpenFile = new JButton();
		btnOpenFile.setText("Open File");
		btnOpenFile.setForeground(Color.DARK_GRAY);
		btnOpenFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnOpenFile.setBounds(173, 146, 160, 45);
		btnOpenFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnOpenFile.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnOpenFile);

		JButton btnViewOutput = new JButton();
		btnViewOutput.setText("View Output");
		btnViewOutput.setForeground(Color.DARK_GRAY);
		btnViewOutput.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnViewOutput.setBounds(173, 197, 160, 45);
		btnViewOutput.setHorizontalAlignment(SwingConstants.CENTER);
		btnViewOutput.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnViewOutput);
		
		JButton btnDescription = new JButton();
		btnDescription.setVerticalTextPosition(SwingConstants.CENTER);
		btnDescription.setText("Description");
		btnDescription.setHorizontalAlignment(SwingConstants.CENTER);
		btnDescription.setForeground(Color.DARK_GRAY);
		btnDescription.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnDescription.setBounds(173, 249, 160, 45);
		homePanel.add(btnDescription);

		JLabel welcomeText = new JLabel("Expression Evaluator Program");
		welcomeText.setForeground(Color.DARK_GRAY);
		welcomeText.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeText.setBounds(70, 56, 353, 57);
		homePanel.add(welcomeText);

		btnOpenFile.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
			
			@Override 
			public void mouseClicked(MouseEvent e){
				cl.show(frame.getContentPane(), "openFilePanel");
			}
		});
		
		btnViewOutput.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
			
			@Override 
			public void mouseClicked(MouseEvent e){
				cl.show(frame.getContentPane(), "viewOutputPanel");
			}
		});
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

		JLabel programTitle = new JLabel("Expression Evaluator Program");
		programTitle.setHorizontalAlignment(SwingConstants.CENTER);
		programTitle.setForeground(Color.DARK_GRAY);
		programTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		programTitle.setBounds(70, 0, 353, 51);
		openFilePanel.add(programTitle);
		
		JLabel btnsmHome_1 = new JLabel("");
		btnsmHome_1.setBackground(SystemColor.inactiveCaption);
		btnsmHome_1.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome_1.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome_1.setForeground(Color.WHITE);
		btnsmHome_1.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome_1.setBounds(443, 11, 40, 40);
		btnsmHome_1.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome_1.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		openFilePanel.add(btnsmHome_1);

		JButton btnLoadFile = new JButton();
		btnLoadFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnLoadFile.setText("Load File");
		btnLoadFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadFile.setForeground(Color.DARK_GRAY);
		btnLoadFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnLoadFile.setBounds(265, 174, 160, 45);
		openFilePanel.add(btnLoadFile);

		JButton btnChooseFile = new JButton();
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

		btnChooseFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chooseFile();
			}
		});
		btnLoadFile.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
			
			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "viewOutputPanel");
				loadFile();
			}
		});
		btnsmHome_1.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
			
			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "homePanel");
				loadFile();
			}
		});
	}

	/**
	 * Initializes the contents of the viewoutput panel
	 */
	private void initializeViewOutputPanel() {

		JPanel viewOutputPanel = new JPanel();
		viewOutputPanel.setBackground(SystemColor.inactiveCaption);
		viewOutputPanel.setLayout(null);

		JLabel btnsmHome = new JLabel("");
		btnsmHome.setVerticalTextPosition(SwingConstants.CENTER);
		btnsmHome.setHorizontalTextPosition(SwingConstants.CENTER);
		btnsmHome.setForeground(Color.WHITE);
		btnsmHome.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		btnsmHome.setBounds(443, 11, 40, 40);
		btnsmHome.setToolTipText("Click if you want to go back to the home menu.");
		btnsmHome.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		viewOutputPanel.add(btnsmHome);

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

		JButton btnLoadFile = new JButton();
		btnLoadFile.setVerticalTextPosition(SwingConstants.CENTER);
		btnLoadFile.setText("Load File");
		btnLoadFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnLoadFile.setForeground(Color.DARK_GRAY);
		btnLoadFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnLoadFile.setBounds(20, 260, 160, 34);
		btnLoadFile.setToolTipText("Click here if you made changes to the input file and want to load it again.");
		viewOutputPanel.add(btnLoadFile);

		JButton btnProcess = new JButton();
		btnProcess.setVerticalTextPosition(SwingConstants.CENTER);
		btnProcess.setText("Process");
		btnProcess.setHorizontalAlignment(SwingConstants.CENTER);
		btnProcess.setForeground(Color.DARK_GRAY);
		btnProcess.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnProcess.setBounds(306, 259, 160, 36);
		btnProcess.setToolTipText("Click here if you want to process the most recently opened file.");
		viewOutputPanel.add(btnProcess);

		JScrollPane spOutput;
		spOutput = new JScrollPane();
		spOutput.setBounds(20, 80, 447, 176);
		viewOutputPanel.add(spOutput);
		
		tpOutput.setEditable(false);
		tpOutput.setFont(new Font("Arial", Font.PLAIN, 13));
		spOutput.setViewportView(tpOutput);
		

		btnLoadFile.addMouseListener(new MouseAdapter() {	
			@Override 
			public void mouseClicked(MouseEvent e){
				loadFile();
			}
		});
		btnProcess.addMouseListener(new MouseAdapter() {	
			@Override 
			public void mouseClicked(MouseEvent e){
				process();
			}
		});
		btnsmHome.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
			
			@Override 
			public void mouseClicked(MouseEvent e){
				cl.show(frame.getContentPane(), "homePanel");
			}
		});
	}
	
	private void initializeDescriptionPanel(){
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(null);
		descriptionPanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(descriptionPanel, "descriptionPanel");
		
		JLabel smbtnHome = new JLabel("");
		smbtnHome.setVerticalTextPosition(SwingConstants.CENTER);
		smbtnHome.setToolTipText("Click if you want to go back to the home menu.");
		smbtnHome.setHorizontalTextPosition(SwingConstants.CENTER);
		smbtnHome.setForeground(Color.WHITE);
		smbtnHome.setFont(new Font("BubbleGum", Font.PLAIN, 20));
		smbtnHome.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/ts_backButton.png")).getImage()));
		smbtnHome.setBounds(443, 11, 40, 40);
		descriptionPanel.add(smbtnHome);
		
		smbtnHome.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());
			
			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "homePanel");
			}
		});
		
		JLabel label_1 = new JLabel("Expression Evaluator Program");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		label_1.setBounds(74, 0, 345, 51);
		descriptionPanel.add(label_1);
		
		JLabel lblDescrpt = new JLabel("Program Description:");
		lblDescrpt.setHorizontalAlignment(SwingConstants.LEFT);
		lblDescrpt.setForeground(Color.DARK_GRAY);
		lblDescrpt.setFont(new Font("Franklin Gothic Book", Font.PLAIN, 15));
		lblDescrpt.setBounds(20, 42, 145, 27);
		descriptionPanel.add(lblDescrpt);
		
		JScrollPane spDescription = new JScrollPane();
		spDescription.setBounds(20, 80, 447, 214);
		descriptionPanel.add(spDescription);
		
		JTextPane tpDescription = new JTextPane();
		tpDescription.setEditable(false);
		tpDescription.setFont(new Font("Arial", Font.PLAIN, 13));
		spDescription.setViewportView(tpDescription);
	}

	/**
	 * Choose file from the directory
	 */
	private void chooseFile() {
		System.out.println("choose file!");
		int file = fileChooser.showOpenDialog(frame);
		if (file == JFileChooser.APPROVE_OPTION) {
			System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getAbsolutePath());
			File selectedFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
			//get the extension of the file
			System.out.println(getExtension(fileChooser.getSelectedFile().getName()));

			tfDocUrl.setText(fileChooser.getSelectedFile().getAbsolutePath().toString());
		}
	}

	/**
	 * Gets the extension of the file selected.
	 */
	private String getExtension(String fileName) {
		System.out.println(fileName);
		int index = fileName.indexOf(".");
		return fileName.substring(index + 1, fileName.length());
	}

	/**
	 * Load the file of the given url
	 */
	private void loadFile() {
		System.out.println("load file!");
	}

	/**
	 * Processes the most recently opened valid file. Only works if there has been a valid file opened.
	 */
	private void process() {
		//AHJ: unimplemented; checks if there is a local file already stored. If yes, process. If no, show error message

		//<---- codes here for file checking
		
		//AHJ: loop here the lines of codes
		//for(){
		int i = 0;
		String line = "x = 1 + 2 * 3 + 4";
		String lexicalString = lexicalAnalyzer();
		String checker = syntaxAnalyzer(lexicalString);
		System.out.println(checker);
		
		LinkedList<String> postFix = new LinkedList<String>();
		int result = 0;
		if (checker.substring(0, 3).equals("err")) {
			System.out.println("Error in ordering the elements.");
		} else if (checker.equals("skip")) {
			//continue;
		} else {
			String expr = getExpression(lexicalString);
			postFix = toPostFix(expr);
			result = evaluateExpression(postFix);
			System.out.println("result = " + result);
		}
		
		System.out.println(postFix);
		
		StyledDocument doc = tpOutput.getStyledDocument();
		try
		{
			String outputLine = "Line1: " + line + "\n";
			doc.insertString(doc.getLength(), outputLine, null);
			
			if (checker.substring(0, 3).equals("err")) {
				outputLine = checker + "\n\n";
				doc.insertString(doc.getLength(), outputLine, null);
				return;
			} else {
				outputLine = "Postfix: " + postFix + "\n";
				doc.insertString(doc.getLength(), outputLine, null);
				
				outputLine = "Result: " + result + "\n\n";
				doc.insertString(doc.getLength(), outputLine, null);
			}
		}
		catch(Exception e) { System.out.println(e); }
		//}
	}

	private String getExpression(String line) {
		int index = line.lastIndexOf("=");
		return line.substring(index + 1, line.length());
	}

	private String lexicalAnalyzer() {
		return "<var-x> = <int-1> <op-+> <int-2> <op-*> <int-3> <op-+> <int-4>";
	}

	private String syntaxAnalyzer(String line) {
		String[] tokens = line.split(" ");
		if (line.contains("=")) {
			String var = tokens[0].substring(1, 4);
			String eq = tokens[1];
			if (!var.equals("var") || !eq.equals("=")) {
				return "err";
			}
		}

		tokens = getExpression(line).split(" ");
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
				System.out.println();
				if (!tokens[i].substring(1, 4).equals("int") && !tokens[i].substring(1, 4).equals("var")) {
					return "error1";
				} else {
					return "accept";
				}
			} else if (tokens[i].substring(1, 3).equals("op")
					&& !(tokens[i - 1].substring(1, 4).equals("int") || tokens[i - 1].substring(1, 4).equals("var"))
					&& !(tokens[i + 1].substring(1, 4).equals("int") || tokens[i + 1].substring(1, 4).equals("var"))) {
				return "error2";
			} else if ((tokens[i].substring(1, 4).equals("var") || tokens[i].substring(1, 4).equals("int"))
					&& (tokens[i + 1].substring(1, 4).equals("var") || tokens[i + 1].substring(1, 4).equals("int"))) {
				return "error3";
			}

		}
		return "accept";

	}

	/**
	 * Returns the postfix form of an expression
	 * @
	 */
	private LinkedList<String> toPostFix(String expr) {
		Stack<String> stack = new Stack<String>();
		LinkedList<String> postFix = new LinkedList<String>();
		String[] tokens = expr.split(" ");
		for (int i = 0; i < tokens.length; i++) {

			if (tokens[i].isEmpty()) {
				continue;
			}

			if (tokens[i].substring(1, 4).equals("int")) {
				String number = tokens[i].substring(5, tokens[i].length() - 1);
				postFix.add(number);
			} else if (tokens[i].substring(1, 4).equals("var")) {
				String var = tokens[i].substring(5, tokens[i].length() - 1);
				//AHJ: unimplemented; find variable in symbol; if not found, return error
				// String number = var;
				String number = "21";
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

		while (!stack.isEmpty()) {
			String poppedElem = stack.pop();
			postFix.add(poppedElem);
		}

		return postFix;
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

	/**
	 * Initialize the variables for the program.
	 */
	private void initializeVariables() {
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		fileChooser.setAcceptAllFileFilterUsed(false);
	}
}
