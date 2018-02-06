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
				+ "-------------------------------------------------\n" + "[Title]\n"
				+ "-------------------------------------------------\n" + "[Process]\n"
				+ "-------------------------------------------------\n";

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
