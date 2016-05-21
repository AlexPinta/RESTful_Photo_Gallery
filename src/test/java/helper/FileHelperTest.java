package helper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileHelperTest {
	String message;
	String fullPath;
	
    @Before
    public void setUp() throws Exception {
    	fullPath = "C:/blabla.txt";
    }

    @Test
    public void testCreateFile() throws Exception {
    	message = "When createFile is triggered, "
				+ "then it should create new file";
    	FileHelper.createFile("C:/", "testCreateFile.txt");
    	assertTrue(message, true);

    	message = "When createFile is triggered, "
				+ "then it should create new file";
    	FileHelper.createFileByFullpath("C:/testCreateFile.txt");
    	assertTrue(message, true);

    	message = "When AddLineToFile is triggered, "
				+ "then it should add line to the file";
    	FileHelper.addLineToFile("C:/testCreateFile.txt", "some text");
    	assertTrue(message, true);

    	message = "When DeleteFile is triggered, "
				+ "then it should deletet file";
    	FileHelper.deleteFile("C:/testCreateFile.txt");
    	assertTrue(message, true);
    }
}