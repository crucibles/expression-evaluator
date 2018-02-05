import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileHandler {

	public BufferedReader reader;
	public JFileChooser fileChooser = new JFileChooser();

	public FileHandler() {
		System.out.println("im in");
		this.fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		this.fileChooser.setFileFilter(new FileNameExtensionFilter("in files", "in"));
		this.fileChooser.setAcceptAllFileFilterUsed(false);
	}

	// to be implemented
	public String saveFile(String output, JFrame frame) {
		try {
			String fileName = createFile(output, frame);
			System.out.println(fileName);
			return fileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates the .out file of the resulting output
	 * 
	 * @param output the text to be stored in the .out file
	 * 
	 * @author Alvaro, Cedric Y.
	 */
	public String createFile(String output, JFrame frame) throws IOException {
		this.fileChooser.setFileFilter(new FileNameExtensionFilter("out files", "out"));
		Writer writer = null;

		try {
			int status = fileChooser.showSaveDialog(frame);

			if (status == JFileChooser.APPROVE_OPTION) {
				System.out.println("hi");
				File selectedFile = fileChooser.getSelectedFile();

				try {
					//AHJ: unimplemented; #01: weird part here. Filechooser can choose in or out for extension in saving file... so unsaon pagkabalo? (Also, this savefile function does not include saving of .in file)
					String fileName = selectedFile.getCanonicalPath();
					if (!fileName.endsWith(".out")) {
						selectedFile = new File(fileName + ".out");
					}
					writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(selectedFile)));
					writer.write(output);
					//AHJ: unimplemented; (not properly implemented)refer to comment #01
					return getFileName() + ".in";
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		} finally {
			try {
				if(writer != null){
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