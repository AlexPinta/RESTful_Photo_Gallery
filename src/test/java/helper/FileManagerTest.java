package helper;

import bootstrap.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestPropertySource(locations="classpath:/settings/application.properties")
public class FileManagerTest {
    String message;

    @Autowired
    FileManager fileManager;

    @Test
    public void testSaveFile() throws Exception {
        final String PATH_FILE = "testSaveFile.txt";
        message = "When saveFile is triggered, "
                + "then it should save as new file and add into the cache";
        fileManager.saveFile(new FileInputStream(PATH_FILE), PATH_FILE);
        assertTrue(message, !fileManager.getFileQueue().isEmpty());


        message = "When DeleteFile is triggered, "
                + "then it should delete file";
        fileManager.deleteFile(PATH_FILE);
        assertTrue(message, fileManager.getFileQueue().isEmpty());
    }

    @Test
    public void testRetrieveFile() throws Exception {
        final String PATH_FILE = "testSaveFile.txt";
        byte[] bytes = {0};
        fileManager.saveFile(new FileInputStream(PATH_FILE), PATH_FILE);
        FileManager.FileProperty fileProperty = fileManager.getFileQueue().peek();

        message = "When retrieveFile is triggered, "
                + "then it should return array of bytes of file";
        assertArrayEquals(message, fileManager.retrieveFile(fileProperty.getFileHashCode()), bytes);
    }
}