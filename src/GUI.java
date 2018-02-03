import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class GUI {
	public JFrame frame;
	
	public JButton btnViewOutput;
	public JMenuItem mntmNew;
	public JMenuItem mntmOpenFile;
	public JMenuItem mntmSave;
	public JMenuItem mntmSaveAs;
	public JMenuItem mntmCompile;
	public JMenuItem mntmRun;
	public JMenuItem mntmCompileRun;
	public JMenuItem mntmProgDescript;
	public JMenuItem mntmAboutUs;

	public JTextField tfDocUrl;
	public JTextPane tpEditor;
	public JTable tblVariables;
	public JTable tblTokens;
	public JTabbedPane tbpEditor;
	public JScrollPane spEditor;
	
	public GUI(){
		initializeFrameContents();
	}
	
	private void initializeFrameContents() {
		frame = new JFrame();
		frame.setBounds(100, 100, 667, 515);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		
		mntmOpenFile = new JMenuItem("Open File");
		mnFile.add(mntmOpenFile);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmSaveAs = new JMenuItem("Save As...");
		mnFile.add(mntmSaveAs);
		
		JMenu mnExecute = new JMenu("Execute");
		menuBar.add(mnExecute);
		
		mntmCompile = new JMenuItem("Compile");
		mnExecute.add(mntmCompile);
		
		mntmRun = new JMenuItem("Run");
		mnExecute.add(mntmRun);
		
		mntmCompileRun = new JMenuItem("Compile & Run");
		mnExecute.add(mntmCompileRun);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		mntmProgDescript = new JMenuItem("Program Description");
		mnHelp.add(mntmProgDescript);
		
		mntmAboutUs = new JMenuItem("About Us");
		mnHelp.add(mntmAboutUs);

		initializeHomePanel();
		initializeOpenFilePanel();
		initializeViewOutputPanel();
		initializeDescriptionPanel();
	}
	
	/**
	 * Initializes the contents of the Home panel
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void initializeHomePanel() {
		// home panel
		JPanel homePanel = new JPanel();
		homePanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(homePanel, "homePanel");
		homePanel.setLayout(null);

		// button for going to 'Open File' panel
		JButton btnOpenFile = new JButton();
		btnOpenFile.setText("Open File");
		btnOpenFile.setForeground(Color.DARK_GRAY);
		btnOpenFile.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnOpenFile.setBounds(173, 146, 160, 45);
		btnOpenFile.setHorizontalAlignment(SwingConstants.CENTER);
		btnOpenFile.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnOpenFile);

		// button for going to 'View Output' panel
		btnViewOutput = new JButton();
		btnViewOutput.setText("View Output");
		btnViewOutput.setForeground(Color.DARK_GRAY);
		btnViewOutput.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnViewOutput.setBounds(173, 197, 160, 45);
		btnViewOutput.setHorizontalAlignment(SwingConstants.CENTER);
		btnViewOutput.setVerticalTextPosition(JLabel.CENTER);
		homePanel.add(btnViewOutput);

		// button for going to 'Description' panel
		JButton btnDescription = new JButton();
		btnDescription.setVerticalTextPosition(SwingConstants.CENTER);
		btnDescription.setText("Description");
		btnDescription.setHorizontalAlignment(SwingConstants.CENTER);
		btnDescription.setForeground(Color.DARK_GRAY);
		btnDescription.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnDescription.setBounds(173, 249, 160, 45);
		homePanel.add(btnDescription);

		// welcoming text
		JLabel welcomeText = new JLabel("Expression Evaluator Program");
		welcomeText.setForeground(Color.DARK_GRAY);
		welcomeText.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		welcomeText.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeText.setBounds(70, 56, 353, 57);
		homePanel.add(welcomeText);

		// goes to 'Open File' panel on click
		btnOpenFile.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "openFilePanel");
			}
		});

		// goes to 'View Output' panel on click
		btnViewOutput.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "viewOutputPanel");
			}
		});

		// goes to 'Description' panel on click
		btnDescription.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frame.getContentPane(), "descriptionPanel");
			}
		});
	}

	/**
	 * Initializes the contents of the 'Open File' panel
	 * 
	 * @author Sumandang, AJ Ruth H.
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
		tfDocUrl.setBounds(32, 128, 423, 35);
		tfDocUrl.setColumns(10);
		tfDocUrl.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 14));
		tfDocUrl.setForeground(Color.BLACK);
		openFilePanel.add(tfDocUrl);
	}

	/**
	 * Initializes the contents of the viewoutput panel
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void initializeViewOutputPanel() {
		// 'View Output' panel
		JPanel viewOutputPanel = new JPanel();
		viewOutputPanel.setBackground(SystemColor.inactiveCaption);
		viewOutputPanel.setLayout(null);
		frame.getContentPane().add(viewOutputPanel, "viewOutputPanel");

		JScrollPane spConsole = new JScrollPane();
		spConsole.setBounds(20, 303, 420, 149);
		viewOutputPanel.add(spConsole);

		JTextPane tpConsole = new JTextPane();
		tpConsole.setEditable(false);
		spConsole.setViewportView(tpConsole);

		String[] varTableHeader = { "Var", "Type", "Value" };
		
		JScrollPane spVariables = new JScrollPane();
		spVariables.setBounds(450, 33, 201, 203);
		viewOutputPanel.add(spVariables);
		
		tblVariables = new JTable(new DefaultTableModel(varTableHeader, 0)){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){
				return false; 
			}
		};
		spVariables.setViewportView(tblVariables);
		
		JScrollPane spTokens = new JScrollPane();
		spTokens.setBounds(450, 247, 199, 205);
		viewOutputPanel.add(spTokens);
		
		String[] tknTableHeader = { "Token", "Lexeme" };
		tblTokens = new JTable(new DefaultTableModel(tknTableHeader, 0)){
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){
				return false; 
			}
		};
		spTokens.setViewportView(tblTokens);
		tpEditor = new JTextPane();
		tpEditor.setText("This program is able to evaluate expressions. It is also capable of storing variables and error checking.\n\n-------------------------------------------------\n[User Manual]\n1. Click 'Open File' in home.\n2. Click 'Choose File' button in the 'Open File' page to pick a file you want to process.\n- Choose valid and existing .in file in the home.\n- You will be redirected to the 'Output' panel if you chose a valid .in file.\n3. Click 'Process' button to process the loaded file.\n4. If changes occurred in the .in file, click 'Load File' before clicking 'Process'.\n-------------------------------------------------\n[Process]\nThe program loads a file (.in file only) from the home directory chosen by the user and process it when user clicks 'process' button. After evaluating and checking for errors, the program produces an output which both place in the output text panel and in an .out file..\n\nError checking includes...\n - Lexical error\n - Syntax error\n - Evaluation error\n\n[Lexical Checking]\nEach element must be separated by spaces.\n(e.g. x = y + zinstead of x=y+z)\n\n- Variable (can only start with underscore (_) or letters (a-z))\n- Operators (add (+), substract (-), multiply (*), divide (/), remainder/module (%))\n- Integer (whole numbers of 0-9 digits)\n\n[Syntax Checking]\n- No hanging operator (e.g. x +)\n- No consecutive variable or operator (e.g. x y, x + - y) \n- Invalid left-hand side(e.g. 4 = x + y)\n- Operator not surrounded by variable or integer (e.g x = + y / z)\n\n[Evaluation Checking]\n- Undefined variables (variables without assigned value)\n- Undefined division (e.g. y = x / 0)\n\n-------------------------------------------------\nAuthors:\nAlvaro, Cedric Y.\nSumandang, AJ Ruth H.\n-------------------------------------------------\n");
		tpEditor.setFont(new Font("Arial", Font.PLAIN, 13));
		tpEditor.setEditable(false);
		
		tbpEditor = new JTabbedPane(JTabbedPane.TOP);
		tbpEditor.setBounds(20, 11, 420, 281);
		viewOutputPanel.add(tbpEditor);
		
		spEditor = new JScrollPane();
		spEditor.setViewportView(tpEditor);
		tbpEditor.addTab("*New.in", null, spEditor, null);
	}

	/**
	 * Initializes the contents of the 'Description' panel
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
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
		String description = "This program is able to evaluate expressions. It is also capable of storing variables and error checking.\n\n"
				+ "-------------------------------------------------\n" + "[User Manual]\n"
				+ "1. Click 'Open File' in home.\n"
				+ "2. Click 'Choose File' button in the 'Open File' page to pick a file you want to process.\n- Choose valid and existing .in file in the home.\n"
				+ "- You will be redirected to the 'Output' panel if you chose a valid .in file.\n"
				+ "3. Click 'Process' button to process the loaded file.\n"
				+ "4. If changes occurred in the .in file, click 'Load File' before clicking 'Process'.\n"
				+ "-------------------------------------------------\n" + "[Process]\n"
				+ "The program loads a file (.in file only) from the home directory chosen by the user and process it when user clicks 'process' button. "
				+ "After evaluating and checking for errors, the program produces an output which both place in the output text panel and in an .out file..\n\n"
				+ "Error checking includes...\n" + " - Lexical error\n" + " - Syntax error\n"
				+ " - Evaluation error\n\n" + "[Lexical Checking]\n"
				+ "Each element must be separated by spaces.\n(e.g. x = y + zinstead of x=y+z)\n\n"
				+ "- Variable (can only start with underscore (_) or letters (a-z))\n"
				+ "- Operators (add (+), substract (-), multiply (*), divide (/), remainder/module (%))\n"
				+ "- Integer (whole numbers of 0-9 digits)\n\n" + "[Syntax Checking]\n"
				+ "- No hanging operator (e.g. x +)\n" + "- No consecutive variable or operator (e.g. x y, x + - y) \n"
				+ "- Invalid left-hand side(e.g. 4 = x + y)\n"
				+ "- Operator not surrounded by variable or integer (e.g x = + y / z)\n\n" + "[Evaluation Checking]\n"
				+ "- Undefined variables (variables without assigned value)\n"
				+ "- Undefined division (e.g. y = x / 0)\n\n" + "-------------------------------------------------\n"
				+ "Authors:\n" + "Alvaro, Cedric Y.\n" + "Sumandang, AJ Ruth H.\n"
				+ "-------------------------------------------------\n";
		tpDescription.setText(description);
		spDescription.setViewportView(tpDescription);
	}
}
