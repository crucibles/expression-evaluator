import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import java.awt.CardLayout;

public class Description extends MouseAdapter {

	public JFrame frmProgramDescription;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Description window = new Description();
					window.frmProgramDescription.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Description() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// description panel
		frmProgramDescription = new JFrame();
		frmProgramDescription.setTitle("About This Program");
		frmProgramDescription.setBounds(100, 100, 509, 383);
		frmProgramDescription.setResizable(false);
		frmProgramDescription.getContentPane().setLayout(new CardLayout(0, 0));

		initializeDescriptionPanel();
		initializeAboutUsPanel();
	}

	/**
	 * Initialize the contents of the description panel.
	 */
	private void initializeDescriptionPanel() {
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(null);
		frmProgramDescription.getContentPane().add(descriptionPanel, "descriptionPanel");

		// description panel's title
		JLabel lblTitle = new JLabel("Program Description"); 
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.DARK_GRAY);
		lblTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));
		lblTitle.setBounds(79, 0, 345, 42);
		descriptionPanel.add(lblTitle);
		
		// scrollpane for the description textpane
		JScrollPane spDescription = new JScrollPane();
		spDescription.setBounds(0, 53, 503, 237);
		descriptionPanel.add(spDescription);

		//AHJ: unimplemented; check if file is already opened; save as function too
		String description = "The ExpressionEvaluator program evaluates expression. It is capable of creating new files, and opening "
				+ "and/or modifying existing ones. This program is also capable of checking lexical, syntactical, and semantic errors.\n\n"
				+ "-------------------------------------------------\n" + "[User Manual]\n"
				+ "- Click 'New' under 'File' if you want to create new file. Shortcut key: Ctrl + N.\n"
				+ "- Click 'Open File' under 'File' menu bar to open a file from your home directory. Shortcut key: Ctrl + O.\n> Choose valid and existing snub file in the home.\n"
				+ "- Click 'Close' under 'File' if you want to close an open tab. Shortcut key: Ctrl + W\n"
				+ "- Click 'Save' under 'File' if you want to save the current open tab's file. Shortcut key: Ctrl + S.\n"
				+ "- Click 'Save As' under 'File' if you want to save the current open tab's file with a new file name. Shortcut key: Ctrl + Shift + S.\n"
				+ "- Open 'Program Description' under 'Help' if you need help with something.\n"
				+ "-------------------------------------------------\n" + "[Available Features]\n"
				+ "The program is not yet capable of checking if the file is already open or not.\n"
				+ "-------------------------------------------------\n" + "[Process]\n"
				+ "The program loads a file (.snub file only) from the home directory chosen by the user and process it when user clicks 'compile'/'run'/'compile and run' button. "
				+ "After evaluating and checking for errors, the program produces an output in an .out file. It also updates the output box in the console, variable, and lexemes tables.\n\n"
				+ "Error checking includes...\n" 
				+ " - Lexical error\n" 
				+ " - Syntax error\n"
				+ " - Evaluation error\n\n" 
				+ "[Lexical Checking]\n"
				+ "Each element must be separated by spaces.\n(e.g. x = y + zinstead of x=y+z)\n\n"
				+ "- Variable (can only start with underscore (_) or letters (a-z))\n"
				+ "- Operators (e.g. ADD, GTE?, NOT?, etc.)\n"
				+ "- Keywords (e.g. NEWLN, COMMAND, DEFINE, END, etc.)\n"
				+ "- Integer and Float literal (whole numbers of 0-9 digits OR {0-9}.{0.9} respectively)\n"
				+ "- Comments (strings surrounded by asterisk '*' )\n\n"
				+ "[Syntax Error]\n"
				+ "The program checks the source code for errors based on the language string available in one of its class.\n\n"
				+ "[Semantic Error]\n"
				+ "In this compiler phase, the program hecks for...\n"
				+ "- Undefined variables ()\n"
				+ "- Wrong data type stored in a variable\n\n"
				+ "[Evaluation]\n"
				+ "When no errors had occured so far in the previous phase, the program is able to execute the source code. Operations include...\n"
				+ "- Mathematical Operations (ADD, SUB, MULT, DIV, MOD)\n"
				+ "- Relational Operations (GT?, GTE?, LT?, LTE?, EQ?, NEQ?)\n"
				+ "- Logicaal Operations (AND?, OR?, NOT?)\n"
				+ "- Undefined division (e.g. y = x / 0)\n\n" 
				+ "-------------------------------------------------\n"
				+ "Authors:\n" + "Alvaro, Cedric Y.\n" + "Sumandang, AJ Ruth H.\n"
				+ "-------------------------------------------------\n";
		
		// text pane for description
		JTextPane tpDescription = new JTextPane();
		spDescription.setViewportView(tpDescription);
		tpDescription.setText(description);
		tpDescription.setFont(new Font("Arial", Font.PLAIN, 13));
		tpDescription.setEditable(false);

		// button to go to description panel
		JButton btnDescription = new JButton();
		btnDescription.setVerticalTextPosition(SwingConstants.CENTER);
		btnDescription.setText("Description");
		btnDescription.setHorizontalAlignment(SwingConstants.CENTER);
		btnDescription.setForeground(Color.DARK_GRAY);
		btnDescription.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnDescription.setBounds(10, 301, 129, 33);
		descriptionPanel.add(btnDescription);

		// button to go to about us panel
		JButton btnAboutUs = new JButton();
		btnAboutUs.setVerticalTextPosition(SwingConstants.CENTER);
		btnAboutUs.setText("About Us");
		btnAboutUs.setHorizontalAlignment(SwingConstants.CENTER);
		btnAboutUs.setForeground(Color.DARK_GRAY);
		btnAboutUs.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnAboutUs.setBounds(364, 301, 129, 33);
		descriptionPanel.add(btnAboutUs);

		btnDescription.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frmProgramDescription.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frmProgramDescription.getContentPane(), "descriptionPanel");
			}
		});
		btnAboutUs.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frmProgramDescription.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frmProgramDescription.getContentPane(), "aboutUsPanel");
			}
		});
	}

	/**
	 * Initialize the contents of the about us panel.
	 */
	private void initializeAboutUsPanel() {
		JPanel aboutUsPanel = new JPanel();
		frmProgramDescription.getContentPane().add(aboutUsPanel, "aboutUsPanel");
		aboutUsPanel.setLayout(null);

		// program title
		JLabel lblTitle = new JLabel("About Us");
		lblTitle.setBounds(79, 0, 345, 42);
		aboutUsPanel.add(lblTitle);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.DARK_GRAY);
		lblTitle.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 18));

		// scroll pane for descrption text
		JScrollPane spAboutUs = new JScrollPane();
		spAboutUs.setBounds(0, 53, 503, 237);
		aboutUsPanel.add(spAboutUs);

		// text pane for description text
		String aboutUs = "This program is made in requirement for course CMSC 129.\n\n"
				+ "_______________________________________\n\n" 
				+ "[Title]\n"
				+ "ExpressionEvaluator Compiler\n"
				+ "_______________________________________\n\n" 
				+ "[Authors]\n"
				+ "> Alvaro, Cedric Y.\n"
				+ "- Mainly in charge of the file handler, lexical, syntax, and semantic checking.\n"
				+ "- Minor: debugger/tester (reporting minor and major bugs)\n\n"
				+ "> Sumandang, AJ Ruth H.\n"
				+ "- Mainly in charge of the GUI and its functions, integration of syntax analyzer (NRPP) to the compiler, and evaluation phase.\n"
				+ "- Minor: in charge of documentating of functions and classes\n\n"
				+ "> Both have their own contribution in finding and fixing bugs.\n";

		// textpane for about us
		JTextPane tpAboutUs = new JTextPane();
		tpAboutUs.setEditable(false);
		tpAboutUs.setFont(new Font("Arial", Font.PLAIN, 13));
		tpAboutUs.setText(aboutUs);
		spAboutUs.setViewportView(tpAboutUs);

		// button for going to about us panel
		JButton btnAboutUs = new JButton();
		btnAboutUs.setBounds(364, 301, 129, 33);
		aboutUsPanel.add(btnAboutUs);
		btnAboutUs.setVerticalTextPosition(SwingConstants.CENTER);
		btnAboutUs.setText("About Us");
		btnAboutUs.setHorizontalAlignment(SwingConstants.CENTER);
		btnAboutUs.setForeground(Color.DARK_GRAY);
		btnAboutUs.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));

		// button for going to description panel
		JButton btnDescription = new JButton();
		btnDescription.setBounds(10, 301, 129, 33);
		aboutUsPanel.add(btnDescription);
		btnDescription.setVerticalTextPosition(SwingConstants.CENTER);
		btnDescription.setText("Description");
		btnDescription.setHorizontalAlignment(SwingConstants.CENTER);
		btnDescription.setForeground(Color.DARK_GRAY);
		btnDescription.setFont(new Font("Franklin Gothic Book", Font.BOLD, 15));
		btnDescription.setName("description");
		
		btnDescription.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frmProgramDescription.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frmProgramDescription.getContentPane(), "descriptionPanel");
			}
		});
		btnAboutUs.addMouseListener(new MouseAdapter() {
			private CardLayout cl = (CardLayout) (frmProgramDescription.getContentPane().getLayout());

			@Override
			public void mouseClicked(MouseEvent e) {
				cl.show(frmProgramDescription.getContentPane(), "aboutUsPanel");
			}
		});
	}

}
