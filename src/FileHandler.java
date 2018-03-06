import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileHandler {

	public BufferedReader reader;
	// change to in when in the expression evaluator
	private CustomFileChooser fileChooser = new CustomFileChooser("in");
	private Vector<CustomFileChooser> fileHandlers = new Vector<CustomFileChooser>();
	private File selectedFile;

	public FileHandler() {
		this.fileChooser
				.setCurrentDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
		this.fileChooser.setAcceptAllFileFilterUsed(false);
	}

	/**
	 * Gets the FileChooser.
	 * 
	 * @return FileChooser for the mainClass to use for altering the selected
	 *         File
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public CustomFileChooser getFileChooser() {
		return this.fileChooser;
	}

	/**
	 * Gets the vector of FileHandler
	 * 
	 * @return the vector of FileHandlers for each tab
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public Vector<CustomFileChooser> getfileHandlers() {
		return fileHandlers;
	}

	/**
	 * Save the file to an output file. Save automatic if existing already, else
	 * choose a directory where to save the file.
	 * 
	 * @return file's extension (.e.g. in (file.in), out (file.out))
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String saveFile(String output, JFrame frame) {
		try {
			selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile + "hehe");
			if (selectedFile != null) {
				System.out.println("hello");
				String name = selectedFile.getName();

				if (!name.contains(".in")) {
					selectedFile = new File(selectedFile.getParentFile(), name + '.' + "in");
				}

				Writer writer = null;
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(output);
				writer.close();

				return fileChooser.getSelectedFile().getName();
			} else {
				String fileName = createFile(output, frame, ".in");
				System.out.println(fileName);
				return fileName;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// to be implemented
	public void saveAsFile(String output, JFrame frame) {
		try {
			createFile(output, frame, ".in");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates the .out file of the resulting output
	 * 
	 * @param output
	 *            the text to be stored in the .out file
	 * 
	 * @author Alvaro, Cedric Y.
	 * @return FileName of the file created
	 */
	public String createFile(String output, JFrame frame, String extension) throws IOException {
		Writer writer = null;

		try {
			int status = fileChooser.showSaveDialog(frame);

			if (status == JFileChooser.APPROVE_OPTION) {
				System.out.println("hi");
				selectedFile = fileChooser.getSelectedFile();

				try {
					// AHJ: unimplemented; #01: weird part here. Filechooser can
					// choose in or out for extension in saving file... so
					// unsaon pagkabalo? (Also, this savefile function does not
					// include saving of .in file)
					String fileName = selectedFile.getCanonicalPath();
					if (!fileName.endsWith(".in")) {
						System.out.println("CHECK HERE (if not .in):");
						System.out.println(fileName);
						selectedFile = new File(fileName + ".in");
					}
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile)));
					writer.write(output);
					// AHJ: unimplemented; (not properly implemented)refer to
					// comment #01
					return getFileName();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				;
			}
		}
	}

	/**
	 * Strips the extension of the received filename string
	 * @param str string whose extension is to be removed
	 * @return the string/name of the string without extension (.in, etc.)
	 */
	public String stripExtension(String str) {
		if (str == null){ // Handle null case specially.
			return null;			
		}
		
		int pos = str.lastIndexOf("."); // Get position of last '.'.
		if (pos == -1){
			return str; // If there wasn't any '.' just return the string as is.
		}
		
		return str.substring(0, pos); // Otherwise return the string, up to the dot.
	}

	/**
	 * Creates new file based on the output. It also saves the current program if current opened file is not saved.
	 * @param output the output to be written in the new output file
	 * @param frame frame to center the dialog
	 * @param extension extension of the file to be used for the new output file
	 * @param sourceProgram the output to be written if the current file is not saved
	 * @return
	 * @throws IOException
	 */
	public String createNewFile(String output, JFrame frame, String extension, String sourceProgram)
			throws IOException {
		Writer writer = null;

		saveFile(sourceProgram, frame);

		try {
			try {
				// AHJ: unimplemented; #01: weird part here. Filechooser can
				// choose in or out for extension in saving file... so unsaon
				// pagkabalo? (Also, this savefile function does not include
				// saving of .in file)
				String fileName = stripExtension(getFileName());

				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".stv"), "utf-8"));
				writer.write(output);
				// AHJ: unimplemented; (not properly implemented)refer to
				// comment #01
			} catch (IOException e) {
				e.printStackTrace();
			}
			return getFileName();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				;
			}
		}
	}

	/**
	 * Choose file from the user's home directory. Checks if file exists
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean chooseFile(JFrame frame) {
		int file = fileChooser.showOpenDialog(frame);
		if (file == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			if (selectedFile.isFile() && getFileExtension(getFileName()).equals("in")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Choose file from the user's home directory. Checks if file exists
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean isCurrFile() {
		selectedFile = fileChooser.getSelectedFile();

		if (selectedFile == null) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Load the file of the given path
	 * 
	 * @author Alvaro, Cedric Y.
	 * @throws IOException
	 */
	public String getFileContent() throws IOException {
		selectedFile = fileChooser.getSelectedFile();

		// stores the selected file and obtained a line
		FileReader fileReader = new FileReader(fileChooser.getSelectedFile().getAbsolutePath());
		reader = new BufferedReader(fileReader);
		String line = reader.readLine();
		String fileContent = "";
		while (line != null) {
			fileContent += line;
			fileContent += "\n";
			line = reader.readLine(); // reads next line
		}

		System.out.println(fileContent);
		reader.close();
		return fileContent;
	}

	/**
	 * Load the file of the given url
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public void loadFile() {

	}

	/**
	 * 
	 * Gets the name of the file selected.
	 * 
	 * @return name of the file received
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getFileName() {
		return fileChooser.getSelectedFile().getName();
	}

	/**
	 * Gets the extension of the file selected.
	 * 
	 * @return file's extension (.e.g. in (file.in), out (file.out))
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getFileExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(index + 1, fileName.length());
	}
}

/**
 * A CustomFileChooser to implement the overwrite of
 * 
 * @author Alvaro, Cedric Y.
 */
class CustomFileChooser extends JFileChooser {
	private static final long serialVersionUID = -4789704212540593370L;
	private String extension;

	public CustomFileChooser(String extension) {
		super();
		this.extension = extension;
		addChoosableFileFilter(new FileNameExtensionFilter(String.format("*in files", extension), extension));
	}

	@Override
	public File getSelectedFile() {
		File selectedFile = super.getSelectedFile();

		if (selectedFile != null) {
			String name = selectedFile.getName();
			if (!name.contains("."))
				selectedFile = new File(selectedFile.getParentFile(), name + '.' + extension);
		}

		return selectedFile;
	}

	@Override
	public void approveSelection() {
		if (getDialogType() == SAVE_DIALOG) {
			File selectedFile = getSelectedFile();
			if ((selectedFile != null) && selectedFile.exists()) {
				int response = JOptionPane.showConfirmDialog(this,
						"The file " + selectedFile.getName()
								+ " already exists. Do you want to replace the existing file?",
						"Ovewrite file", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (response != JOptionPane.YES_OPTION) {
					return;
				}
			}
		}

		super.approveSelection();
	}
}