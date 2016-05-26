package helper;

import bootstrap.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class FileManagerTest {
    String message;
    final String PATH_FILE = "testSaveFile.txt";

    @Autowired
    FileManager fileManager;

    String USER_FOLDER = System.getProperty("user.home")+"/test";

    @Test
    public void testSaveFile() throws Exception {
        message = "When saveFile is triggered, "
                + "then it should save as new file and add into the cache";
        fileManager.USER_FOLDER = USER_FOLDER;

        File file = new File(USER_FOLDER + File.pathSeparator + PATH_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        fileManager.saveFile(new FileInputStream(file), PATH_FILE);
        assertTrue(message, !fileManager.getFileQueue().isEmpty());


        message = "When DeleteFile is triggered, "
                + "then it should delete file";
        FileManager.FileProperty fileProperty = fileManager.getFileQueue().peek();
        fileManager.deleteFile(fileProperty.getFileHashCode());
        assertTrue(message, fileManager.getFileQueue().isEmpty());
    }

    @Test
    public void testRetrieveFile() throws Exception {
        byte[] bytes = {};
        fileManager.USER_FOLDER = USER_FOLDER;
        File file = new File(USER_FOLDER + File.pathSeparator + PATH_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        fileManager.saveFile(new FileInputStream(file), PATH_FILE);
        FileManager.FileProperty fileProperty = fileManager.getFileQueue().peek();

        message = "When retrieveFile is triggered, "
                + "then it should return array of bytes of file";
        assertArrayEquals(message, bytes, fileManager.retrieveFile(fileProperty.getFileHashCode()));
    }
}