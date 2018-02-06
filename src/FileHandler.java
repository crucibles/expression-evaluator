import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
	private CustomFileChooser fileChooser = new CustomFileChooser("in");
	private Vector<CustomFileChooser> fileHandlers = new Vector<CustomFileChooser>();

	public FileHandler() {
		System.out.println("im in");
		this.fileChooser
				.setCurrentDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
		//this.fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		this.fileChooser.setAcceptAllFileFilterUsed(false);
	}

	public CustomFileChooser getFileChooser() {
		return this.fileChooser;
	}

	public Vector<CustomFileChooser> getfileHandlers() {
		return fileHandlers;
	}

	// to be implemented
	public String saveFile(String output, JFrame frame) {
		try {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println(selectedFile + "hehe");
			if (selectedFile != null) {
				System.out.println("hello");
				String name = selectedFile.getName();

				if (!name.contains(".in")) {
					selectedFile = new File(selectedFile.getParentFile(), name + '.' + "in");
				}

				return fileChooser.getSelectedFile().getName();
			} else {
				String fileName = createFile(output, frame);
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
			createFile(output, frame);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Creates the .out file of the resulting output
	 * 
	 * @param output the text to be stored in the .out file
	 * 
	 * @author Alvaro, Cedric Y.
	 * @return 
	 */
	public String createFile(String output, JFrame frame) throws IOException {
		Writer writer = null;

		try {
			int status = fileChooser.showSaveDialog(frame);

			if (status == JFileChooser.APPROVE_OPTION) {
				System.out.println("hi");
				File selectedFile = fileChooser.getSelectedFile();

				try {
					//AHJ: unimplemented; #01: weird part here. Filechooser can choose in or out for extension in saving file... so unsaon pagkabalo? (Also, this savefile function does not include saving of .in file)
					String fileName = selectedFile.getCanonicalPath();
					if (!fileName.endsWith(".in")) {
						selectedFile = new File(fileName + ".in");
					}
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile)));
					writer.write(output);
					//AHJ: unimplemented; (not properly implemented)refer to comment #01
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
	 * Choose file from the user's home directory. Checks if file exists
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public boolean chooseFile(JFrame frame) {
		int file = fileChooser.showOpenDialog(frame);
		if (file == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
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
		File selectedFile = fileChooser.getSelectedFile();

		if(selectedFile == null){
			return true;
		} else {
			return false;
		}
		
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

class CustomFileChooser extends JFileChooser {
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
				if (response != JOptionPane.YES_OPTION)
					return;
			}
		}

		super.approveSelection();
	}
}