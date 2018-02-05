import java.awt.CardLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class GUI {
	public JFrame frame;
	public JFrame descriptionFrame;
	public JMenuItem mntmNew;
	public JMenuItem mntmOpenFile;
	public JMenuItem mntmClose;
	public JMenuItem mntmSave;
	public JMenuItem mntmSaveAs;
	public JMenuItem mntmCompile;
	public JMenuItem mntmRun;
	public JMenuItem mntmCompileRun;
	public JMenuItem mntmProgDescript;
	public JMenuItem mntmAboutUs;
	public JTextPane tpEditor;
	public JTable tblVariables;
	public JTable tblTokens;
	public JTabbedPane tbpEditor;
	public JScrollPane spEditor;

	public GUI() {
		initializeFrameContents();
	}

	private void initializeFrameContents() {
		frame = new JFrame();
		frame.setBounds(100, 100, 667, 515);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		initializeViewOutputPanel();
		initializeDescriptionPanel();
		initializeMenuBar();
	}

	private void initializeMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);

		mntmOpenFile = new JMenuItem("Open File");
		mnFile.add(mntmOpenFile);

		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);

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

		mntmProgDescript.addActionListener(new ActionListener() {
			private CardLayout cl = (CardLayout) (descriptionFrame.getContentPane().getLayout());

			public void actionPerformed(ActionEvent ev) {
				descriptionFrame.setVisible(true);
				cl.show(descriptionFrame.getContentPane(), "descriptionPanel");
			}
		});
		mntmAboutUs.addActionListener(new ActionListener() {
			private CardLayout cl = (CardLayout) (descriptionFrame.getContentPane().getLayout());

			public void actionPerformed(ActionEvent ev) {
				descriptionFrame.setVisible(true);
				cl.show(descriptionFrame.getContentPane(), "aboutUsPanel");
			}
		});
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

		tblVariables = new JTable(new DefaultTableModel(varTableHeader, 0)) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		spVariables.setViewportView(tblVariables);

		JScrollPane spTokens = new JScrollPane();
		spTokens.setBounds(450, 247, 199, 205);
		viewOutputPanel.add(spTokens);

		String[] tknTableHeader = { "Token", "Lexeme" };
		tblTokens = new JTable(new DefaultTableModel(tknTableHeader, 0)) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		spTokens.setViewportView(tblTokens);

		tbpEditor = new JTabbedPane(JTabbedPane.TOP);
		tbpEditor.setBounds(20, 11, 420, 281);
		viewOutputPanel.add(tbpEditor);

		spEditor = new JScrollPane();
		spEditor.setViewportView(tpEditor);
	}

	/**
	 * Initializes the contents of the 'Description' panel
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	private void initializeDescriptionPanel() {
		// description panel
		Description description = new Description();
		descriptionFrame = description.frmProgramDescription;
	}

	public void addNewTab(String title) {
		JTextPane tpEditor = new JTextPane();
		tpEditor.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				// added '*' to current tab title if user typed into the
				// editor
				int selectedIndex = tbpEditor.getSelectedIndex();
				String title = tbpEditor.getTitleAt(selectedIndex);
				if (title.charAt(0) != '*') {
					title = '*' + title;
					tbpEditor.setTitleAt(selectedIndex, title);
				}
			}
		});
		spEditor = new JScrollPane();
		spEditor.setViewportView(tpEditor);
		tbpEditor.addTab(title, spEditor);
		int lastIndex = tbpEditor.getTabCount() - 1;
		tbpEditor.setSelectedIndex(lastIndex);

	}

	/**
	 * Closes the current tab.
	 * It also checks whether current tab is saved or not. The tab automatically closes if current tab is saved. 
	 * Else, it prompts the user if he/she wants to save changes. Option 'yes' saves files and exits tab. Option 'no' automatically
	 * removes tab. Option 'cancel' 
	 */
	public boolean closeCurrentTab(String text) {
		int selectedIndex = tbpEditor.getSelectedIndex();
		if (selectedIndex >= 0) {
			String title = tbpEditor.getTitleAt(selectedIndex);
			if (title.charAt(0) != '*') {
				tbpEditor.remove(selectedIndex);
				return true;
			} else {
				String msg = "'" + title.substring(1) + "'" + " has been modified. Save changes?";
				int result = JOptionPane.showConfirmDialog(frame, msg, "Save", JOptionPane.YES_NO_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					//AHJ: Unimplemented; must contain checking if file already exist in the directory
					FileHandler fileHandler = new FileHandler();
					title = fileHandler.saveFile(text, frame);
					if (title != null) {
						tbpEditor.remove(selectedIndex);
						return true;
					} else {
						return false;
					}
				} else if (result == JOptionPane.NO_OPTION) {
					tbpEditor.remove(selectedIndex);
					return true;
				} else {

				}
			}
		}
		return false;
	}

	/**
	 * Displays the resulting output in the panel.
	 * 
	 * @param line
	 *            the current line being evaluated
	 * @param checker
	 *            the error in the result (if the source code has errors)
	 * @param strPostFix
	 *            the postfix string of the expression (if no errors has been
	 *            found in the source code)
	 * @param result
	 *            the result of the expression (if no errors has been found in
	 *            the source code)
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	//	private void displayOutput(String line, String checker, String strPostFix, int result, int lineNum) {
	//		StyledDocument doc = gui.tpEditor.getStyledDocument();
	//		try {
	//			// outputs the line read
	//			String outputLine = "Line" + lineNum + ": " + line + "\n";
	//			doc.insertString(doc.getLength(), outputLine, null);
	//
	//			/* if error has been detected*/
	//			if (checker.substring(0, 3).equals("err")) {
	//
	//				// outputs the error that had occurred
	//				outputLine = "Result: " + checker + "\n\n";
	//				doc.insertString(doc.getLength(), outputLine, null);
	//			/* if no errors found in the source code*/
	//			} else {
	//				outputLine = "Postfix: " + getLHS(line) + " " + strPostFix + "\n"; // postfix
	//				doc.insertString(doc.getLength(), outputLine, null);
	//
	//				if (checker.substring(0, 4).equals("Warn")) {
	//					outputLine = checker + "\n\n"; /* warning (for lines containing undefined variables) */
	//				} else {
	//					outputLine = "Result: " + getLHS(line) + " " + result + "\n\n"; // result/answer
	//					if (!flag) {
	//						/* if result is undefined (for zero divisors) */
	//						outputLine = "Result: " + getLHS(line) + " " + "undefined" + "\n\n";
	//						flag = true;
	//					}
	//				}
	//				doc.insertString(doc.getLength(), outputLine, null);
	//
	//			}
	//		} catch (Exception e) {
	//			System.out.println(e);
	//		}
	//	}
	//
	//	/**
	//	 * Display addition output such as variables declared and errors encountered
	//	 * 
	//	 * @author Sumandang, AJ Ruth H.
	//	 */
	//	private void displayAdditionalOutput() {
	//		StyledDocument doc = gui.tpEditor.getStyledDocument();
	//		try {
	//			String outputLine = "-------------------------\n" + "Variables Used:\n";
	//			doc.insertString(doc.getLength(), outputLine, null);
	//
	//			for (int i = 0; i < symbolTables.size(); i++) {
	//				outputLine = symbolTables.get(i).getToken() + "\n";
	//				doc.insertString(doc.getLength(), outputLine, null);
	//			}
	//
	//			outputLine = "-------------------------\n" + "Errors found:\n";
	//			doc.insertString(doc.getLength(), outputLine, null);
	//
	//			outputLine = !errorMsg.equals("") ? errorMsg + "\n" : "No errors encountered\n";
	//			doc.insertString(doc.getLength(), outputLine, null);
	//
	//		} catch (Exception e) {
	//			System.out.println(e);
	//		}
	//	}

	public void updateTabInfo() {
		int selectedIndex = tbpEditor.getSelectedIndex();
		String title = tbpEditor.getTitleAt(selectedIndex);
		if (title.charAt(0) == '*') {
			tbpEditor.setTitleAt(selectedIndex, title.substring(1));
		}
	}

	public void updateTabInfo(String title) {
		int selectedIndex = tbpEditor.getSelectedIndex();
		tbpEditor.setTitleAt(selectedIndex, title);
	}
}
