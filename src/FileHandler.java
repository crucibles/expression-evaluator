import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileHandler {

	public BufferedReader reader;
	public JFileChooser fileChooser = new JFileChooser();

	public FileHandler() {
		System.out.println("im in");
		this.fileChooser.setCurrentDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));
		this.fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		this.fileChooser.setAcceptAllFileFilterUsed(false);
	}

	// to be implemented
	public void saveFile(String output, JFrame frame) {
		try {
			createFile(output, frame);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 */
	public void createFile(String output, JFrame frame) throws IOException {
		Writer writer = null;

		try {
			int status = fileChooser.showSaveDialog(frame);

			if (status == JFileChooser.APPROVE_OPTION) {
				System.out.println("hi");
				File selectedFile = fileChooser.getSelectedFile();

				try {
					String fileName = selectedFile.getCanonicalPath();
					if (!fileName.endsWith(".in")) {
						selectedFile = new File(fileName + ".in");
					}
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile)));
					writer.write(output);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} finally {
			try {
				writer.close();
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