import java.awt.CardLayout;
import java.awt.Component;
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
import javax.swing.JViewport;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GridLayout;

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
	private JTextPane tpConsole;
	public JTabbedPane tbpEditor;
	public JScrollPane spEditor;
	private JPanel plTable;
	private JScrollPane spVariables;
	private JTable tblVariables;
	private JScrollPane spTokens;
	private JTable tblTokens;

	public GUI() {
		initializeFrameContents();
	}

	private void initializeFrameContents() {
		frame = new JFrame();
		frame.setBounds(new Rectangle(0, 0, 700, 500));
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		initializeViewOutputPanel();
		initializeDescriptionPanel();
		initializeMenuBar();
	}

	/**
	 * Initializes the program's menu bar
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
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
		frame.getContentPane().add(viewOutputPanel, "viewOutputPanel");
		viewOutputPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane spConsole = new JScrollPane();
		spConsole.setPreferredSize(new Dimension(2, 150));
		viewOutputPanel.add(spConsole, BorderLayout.SOUTH);

		tpConsole = new JTextPane();
		tpConsole.setEditable(false);
		spConsole.setViewportView(tpConsole);


		plTable = new JPanel();
		plTable.setPreferredSize(new Dimension(300, 10));
		plTable.setMinimumSize(new Dimension(200, 10));
		viewOutputPanel.add(plTable, BorderLayout.EAST);
		plTable.setMaximumSize(new Dimension(500, 32767));
		plTable.setLayout(new GridLayout(0, 1, 0, 0));

		spVariables = new JScrollPane();
		spVariables.setAlignmentX(Component.RIGHT_ALIGNMENT);
		plTable.add(spVariables);
		String[] varTableHeader = { "Var", "Type", "Value" };

		tblVariables = new JTable();
		tblVariables.setEnabled(false);
		tblVariables.setModel(new DefaultTableModel(new Object[][] {}, varTableHeader));
		spVariables.setViewportView(tblVariables);

		spTokens = new JScrollPane();
		spTokens.setAlignmentX(Component.RIGHT_ALIGNMENT);
		plTable.add(spTokens);
		
		String[] tknTableHeader = { "Token", "Lexeme" };

		tblTokens = new JTable();
		tblTokens.setEnabled(false);
		tblTokens.setModel(new DefaultTableModel(new Object[][] {}, tknTableHeader));
		spTokens.setViewportView(tblTokens);

		tbpEditor = new JTabbedPane(JTabbedPane.TOP);
		viewOutputPanel.add(tbpEditor);

		spEditor = new JScrollPane();
		JTextPane tpEditor = new JTextPane();
		spEditor.setViewportView(tpEditor);
		TextLineNumber tln = new TextLineNumber(tpEditor);
		spEditor.setRowHeaderView( tln );
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

	/**
	 * Adds new tab to the tabbed pane. It also directs the user to that newly
	 * created tab.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
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
		TextLineNumber tln = new TextLineNumber(tpEditor);
		tbpEditor.addTab(title, spEditor);
		int lastIndex = tbpEditor.getTabCount() - 1;
		tbpEditor.setSelectedIndex(lastIndex);
		spEditor.setRowHeaderView( tln );

	}

	/**
	 * Closes the current tab. It also checks whether current tab is saved or
	 * not. The tab automatically closes if current tab is saved. Else, it
	 * prompts the user if he/she wants to save changes. Option 'yes' saves
	 * files and exits tab. Option 'no' automatically removes tab. Option
	 * 'cancel'
	 * 
	 * @return true if closing of current tab is successful; false if not
	 * @author Sumandang, AJ Ruth H.
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
					// AHJ: Unimplemented; must contain checking if file already
					// exist in the directory
					FileHandler fileHandler = new FileHandler();
					title = fileHandler.saveFile(text, frame, selectedIndex);
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
	 * Updates the tab's title by removing the '*' from the title
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void setTabTitle() {
		int selectedIndex = tbpEditor.getSelectedIndex();
		String title = tbpEditor.getTitleAt(selectedIndex);
		if (title.charAt(0) == '*') {
			tbpEditor.setTitleAt(selectedIndex, title.substring(1));
		}
	}

	/**
	 * Updates the tab's title by changing the title by the received parameter
	 * 
	 * @param title
	 *            the title to replace the
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void setTabTitle(String title) {
		int selectedIndex = tbpEditor.getSelectedIndex();
		tbpEditor.setTitleAt(selectedIndex, title);
	}

	/**
	 * Sets the table in the GUI based on the current symboltable shown.
	 * 
	 * @param st
	 *            symbol table whose information are to be displayed
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void setTablesInfo(SymbolTable st) {
		DefaultTableModel modelTok = (DefaultTableModel) this.tblTokens.getModel();
		DefaultTableModel modelVar = (DefaultTableModel) this.tblVariables.getModel();
		clearTable();
		for (int i = 0; i < st.getSize(); i++) {
			String lexeme = st.getLexemeAt(i);
			String token = st.getTokenAt(i);
			String row[] = { token, lexeme };
			String type = st.getTypeAt(i);
			modelTok.addRow(row);
			if (token.equals("IDENT")) {
				String value = st.getValueAt(i);
				String row2[] = { lexeme, type, value };
				modelVar.addRow(row2);
			}
		}
	}

	/**
	 * Clears the GUI tables.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void clearTable() {
		DefaultTableModel modelTok = (DefaultTableModel) this.tblTokens.getModel();
		DefaultTableModel modelVar = (DefaultTableModel) this.tblVariables.getModel();
		modelTok.setRowCount(0);
		modelVar.setRowCount(0);
	}

	/**
	 * Stringify the variable table information.
	 * 
	 * @return the stringified value of the table
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public String getVariableTableInformation() {
		DefaultTableModel tm = (DefaultTableModel) tblVariables.getModel();
		String info = "Variable , Type , Value\n\n";
		for (int i = 0; i < tm.getRowCount(); i++) {
			info += tm.getValueAt(i, 0) + " , ";
			info += tm.getValueAt(i, 1) + " , ";
			info += tm.getValueAt(i, 2) + "\n";
		}
		return info;
	}

	/**
	 * Displays the text in the GUI console. Appends to the existing text in the
	 * console.
	 * 
	 * @param text
	 *            the text to be appended to the console
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void console(String text) {
		StyledDocument doc = tpConsole.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), text, null);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Clears the GUI error console.
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void clearConsole() {
		tpConsole.setText("");
	}

	/**
	 * Gets the text pane of the current tab
	 * 
	 * @return JTextPane of current tab
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public JTextPane getCurrentTextPane() {
		int selectedIndex = tbpEditor.getSelectedIndex();
		if (selectedIndex >= 0) {
			Component component = tbpEditor.getComponentAt(selectedIndex);
			JViewport view = ((JScrollPane) component).getViewport();
			Component components[] = view.getComponents();
			for (int i1 = 0; i1 < components.length; i1++) {
				if (components[i1] instanceof JTextPane) {
					return (JTextPane) components[i1];
				}
			}

		}
		return null;
	}

	/**
	 * Gets the text found in the editor pane.
	 * 
	 * @return editor pane content
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public String getEditorText() {
		JTextPane tp = this.getCurrentTextPane();
		return tp != null ? tp.getText() : "";
	}

	/**
	 * Sets the GUI editor pane with the received string
	 * 
	 * @param newText
	 *            the string to set/place/write on the GUI editor pane
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void setEditorText(String newText) {
		JTextPane tp = this.getCurrentTextPane();
		tp.setText(newText);
	}

}
