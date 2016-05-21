/**
 * Helper classes used to work with files and packages
 * <p>
 * These classes contain the some CRUD with files and detecting classes in the package functionality
 * </p>
 *
 * @since 1.0
 * @author Alex Pinta, Oleh Pinta
 * @version 1.0
 */
package helper;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * The FileHelper class implements CRUD operation with files
 *
 * @author  Alex Pinta, Oleh Pinta
 */
public class FileHelper {
	private static Logger logger = Logger.getRootLogger();
	/**
	 * This method is used to create file on your local machine.
	 * @param filepath Path to the folder, where file will be created
	 * @param fileName Name of the file to be created
	 */
	public static boolean createFile(final String filepath, final String fileName) {
		String lastSymbol = filepath.substring(filepath.length() - 1);
		String splitter = "/";
		String fullpath = filepath;
		if (!lastSymbol.equals("/")) {
			fullpath += splitter;
		}
		fullpath += fileName;

		return createFileByFullpath(fullpath);
	}

	/**
	 * This method is used to create file on your local machine.
	 * @param fullpath Contains full path (folder and file name) of the file to be created
	 */
	public static boolean createFileByFullpath(final String fullpath) {
		Path path = Paths.get(fullpath);

		try {
			if (Files.exists(path)) {
				Files.delete(path);
			} else {
				Files.createFile(path);
				logger.info("File " + fullpath + " created successfully.");
			}
		} catch (IOException e) {
			logger.error("Error while creating a File " + fullpath + ".");
		}

		return true;
	}

	/**
	 * This method is used to delete file from your local machine.
	 * @param fullpath Contains full path (folder and file name) of the file to be deleted
	 */
	public static void deleteFile(final String fullpath) {
		Path path = Paths.get(fullpath);

		try {
			Files.deleteIfExists(path);
			logger.info("File " + fullpath + " deleted successfully.");
		} catch (IOException e) {
			logger.error("Error while deleting a File " + fullpath + ".");
		}
	}

	/**
	 * This method is used to add string line to the file on your local machine.
	 * @param fullpath Contains full path (folder and file name) of the file to be edited
	 * @param content content to be added to the file
	 */
	public static void addLineToFile(final String fullpath, final String content) {
		try {
			FileWriter fw = new FileWriter(fullpath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			logger.error("Error while writing line to the fiel " + fullpath);
		}
	}
}
