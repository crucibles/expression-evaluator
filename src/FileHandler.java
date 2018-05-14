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
	private Vector<CustomFileChooser> fileHandlers;

	/**
	 * Constructor
	 */
	public FileHandler() {
		fileHandlers = new Vector<CustomFileChooser>();
	}

	/**
	 * Adds a new file chooser into the FileHandler vector.
	 * 
	 * @param newInput
	 *            the new file chooser to be added to the vector
	 * 
	 * @author Sumandang, AJ Ruth
	 */
	public void addFileChooser(CustomFileChooser newInput) {
		newInput.setCurrentDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
		newInput.setAcceptAllFileFilterUsed(false);

		fileHandlers.addElement(newInput);
	}

	/**
	 * Gets the FileChooser at some index
	 * 
	 * @param index
	 * @return FileChooser for the mainClass to use for altering the selected
	 *         file
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public CustomFileChooser getFileChooserAt(int index) {
		if (this.fileHandlers.isEmpty() || index > this.fileHandlers.size() - 1) {
			return null;
		} else {
			return this.fileHandlers.elementAt(index);
		}
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
	 * Remove a file chooser at some index.
	 * 
	 * @param index
	 *            index of the file chooser to be removed
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public void removeFileChooserAt(int index) {
		fileHandlers.remove(index);
	}

	/**
	 * Save the file to an output file. Save automatic if existing already, else
	 * choose a directory where to save the file.
	 * 
	 * @param output
	 *            output to be stored to the new file
	 * @param frame
	 *            frame to center the dialog
	 * @index index of the file chooser to be used
	 * @return file's extension (.e.g. in (file.in), out (file.out))
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String saveFile(String output, JFrame frame, int index) {
		try {
			File selectedFile = fileHandlers.elementAt(index).getSelectedFile();
			if (selectedFile != null) {
				String name = selectedFile.getName();

				if (!name.contains(".in")) {
					selectedFile = new File(selectedFile.getParentFile(), name + '.' + "in");
				}

				Writer writer = null;
				File file = new File(fileHandlers.elementAt(index).getSelectedFile().getAbsolutePath());
				writer = new BufferedWriter(new FileWriter(file));
				writer.write(output);
				writer.close();

				fileHandlers.elementAt(index).setSelectedFile(selectedFile);

				return fileHandlers.elementAt(index).getSelectedFile().getName();
			} else {
				String fileName = createFile(output, frame, ".in", index);
				return fileName;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Save the file to an output file by showing the directory and choosing
	 * where to save the file.
	 * 
	 * @param output
	 *            the file content to be saved in the file
	 * @param frame
	 *            the frame to center the 'save' dialog
	 * @param index
	 *            the index on where in the file handlers the file is saved
	 * @return the saved name of the file
	 * 
	 * @author Sumandang, AJ Ruth H.
	 */
	public String saveAsFile(String output, JFrame frame, int index) {
		String fileName = "";
		try {
			fileName = createFile(output, frame, ".in", index);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * Creates the .out file of the resulting output
	 * 
	 * @param output
	 *            the text to be stored in the .out file
	 * @return FileName of the file created
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String createFile(String output, JFrame frame, String extension, int index) throws IOException {
		Writer writer = null;

		try {
			int status = this.getFileChooserAt(index).showSaveDialog(frame);

			if (status == JFileChooser.APPROVE_OPTION) {
				File selectedFile = this.getFileChooserAt(index).getSelectedFile();

				try {
					// AHJ: unimplemented; #01: weird part here. Filechooser can
					// choose in or out for extension in saving file... so
					// unsaon pagkabalo? (Also, this savefile function does not
					// include saving of .in file)
					String fileName = selectedFile.getCanonicalPath();
					if (!fileName.endsWith(".in")) {
						selectedFile = new File(fileName + ".in");
					}
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile)));
					writer.write(output);
					// AHJ: unimplemented; (not properly implemented)refer to
					// comment #01
					this.fileHandlers.elementAt(index).setSelectedFile(selectedFile);
					return getFileNameAt(index);
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
	 * 
	 * @param str
	 *            string whose extension is to be removed
	 * @return the string/name of the string without extension (.in, etc.)
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String stripExtension(String str) {
		if (str == null) { // Handle null case specially.
			return null;
		}

		int pos = str.lastIndexOf("."); // Get position of last '.'.
		if (pos == -1) {
			return str; // If there wasn't any '.' just return the string as is.
		}

		return str.substring(0, pos); // Otherwise return the string, up to the
										// dot.
	}

	/**
	 * Creates new file based on the output. It also saves the current program
	 * if current opened file is not saved.
	 * 
	 * @param output
	 *            the output to be written in the new output file
	 * @param frame
	 *            frame to center the dialog
	 * @param extension
	 *            extension of the file to be used for the new output file
	 * @param sourceProgram
	 *            the output to be written if the current file is not saved
	 * @return
	 * @throws IOException
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String createNewFile(String output, JFrame frame, String extension, String sourceProgram, int index)
			throws IOException {
		Writer writer = null;

		saveFile(sourceProgram, frame, index);

		try {
			try {
				// AHJ: unimplemented; #01: weird part here. Filechooser can
				// choose in or out for extension in saving file... so unsaon
				// pagkabalo? (Also, this savefile function does not include
				// saving of .in file)
				String fileName = stripExtension(getFileNameAt(index));

				writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(fileName + extension), "utf-8"));
				writer.write(output);
				// AHJ: unimplemented; (not properly implemented)refer to
				// comment #01
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		return getFileNameAt(index);
	}

	/**
	 * Choose file from the user's home directory. Checks if file exists
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean chooseFile(JFrame frame, int index) {
		if (this.getFileChooserAt(index) == null) {
			this.addFileChooser(new CustomFileChooser("in"));
		}

		int file = this.getFileChooserAt(index).showOpenDialog(frame);
		if (file == JFileChooser.APPROVE_OPTION) {
			File selectedFile = this.getFileChooserAt(index).getSelectedFile();
			if (selectedFile.isFile() && getFileExtension(getFileNameAt(index)).equals("in")) {
				fileHandlers.elementAt(index).setSelectedFile(selectedFile);
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
	public boolean isCurrFileAt(int index) {
		File selectedFile = this.getFileChooserAt(index).getSelectedFile();

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
	public String getFileContentAt(int index) throws IOException {
		File selectedFile = this.getFileChooserAt(index).getSelectedFile();

		// stores the selected file and obtained a line
		FileReader fileReader = new FileReader(selectedFile.getAbsolutePath());
		reader = new BufferedReader(fileReader);
		String line = reader.readLine();
		String fileContent = "";
		while (line != null) {
			fileContent += line;
			fileContent += "\n";
			line = reader.readLine(); // reads next line
		}

		reader.close();
		return fileContent;
	}

	/**
	 * 
	 * Gets the name of the file selected.
	 * 
	 * @return name of the file received
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String getFileNameAt(int index) {
		return this.getFileChooserAt(index).getSelectedFile().getName();
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
 * A CustomFileChooser to implement the overwrite of .in file
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

	public CustomFileChooser(File file) {
		super.setSelectedFile(file);
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