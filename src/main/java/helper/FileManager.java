package helper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
* The FileHelper class implements CRUD operation with files
* 
* @author  Alex Pinta, Oleh Pinta
*/
@Component
public class FileManager {
    private static Logger logger = Logger.getRootLogger();
    private final byte[] BYTES = new byte[1024];
    private final int START_OFFSET = 0;

    @Value("${user.home.temp}") public String USER_FOLDER;
    private Queue<FileProperty> fileQueue;

    public FileManager() {
        this.fileQueue = new ConcurrentLinkedDeque<>();
    }

    public Queue<FileProperty> getFileQueue() {
        return this.fileQueue;
    }

    /**
     * This method is used to save file on server.
     * @param stream It reads photo by bytes from stream
     * @param pathFile It writes bytes of photo to the file with path = pathFile
     */
    public void saveFile(InputStream stream, String pathFile) throws IOException {
        int read;
        File file = new File(USER_FOLDER + File.separator + pathFile);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file.getAbsolutePath());
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        while ((read = stream.read(BYTES)) != -1) {
            buffer.write(BYTES, START_OFFSET, read);
        }
        buffer.writeTo(out);
        out.close();

        final FileProperty fileProperty = new FileProperty(file);
        fileProperty.generateHashCode(buffer.toByteArray());
        this.fileQueue.add(fileProperty);
        logger.info("File " + fileProperty.getFile().getPath() + " retreived successfully.");
    }

    /**
     * This method is used to retrieve file from cache.
     * @param hashCode This is the unique value of the photo in the cache
     */
    public byte[] retrieveFile(String hashCode) throws IOException {
        final Iterator<FileProperty> iterator = this.fileQueue.iterator();
        FileProperty fileProperty = null;
        byte[] fileBytes = {0};
        int read;

        while (iterator.hasNext()) {
            fileProperty = iterator.next();
            if (fileProperty.getFileHashCode().equals(hashCode)) {
                break;
            }
        }
        if (fileProperty != null) {
            InputStream imageStream = new FileInputStream(fileProperty.getFile());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            while ((read = imageStream.read(BYTES)) != -1) {
                buffer.write(BYTES, START_OFFSET, read);
            }
            buffer.flush();
            fileBytes = buffer.toByteArray();
            buffer.close();
            logger.info("File " + fileProperty.getFile().getPath() + " retreived successfully.");
        }
        return fileBytes;
   }

    /**
     * This method is used to delete file from your local machine.
     * @param hashCode Contains hashcode (MD5) of the file to be deleted
     */
    public void deleteFile(final String hashCode) {
        final Iterator<FileProperty> iterator = this.fileQueue.iterator();
        FileProperty fileProperty = null;
        while (iterator.hasNext()) {
            fileProperty = iterator.next();
            if (fileProperty.getFileHashCode().equals(hashCode)) {
                break;
            }
        }
        if (fileProperty == null) {
            logger.warn("File " + hashCode + " wasn\'t deleted due to it wasn\'t found.");
            return;
        }
        fileProperty.getFile().deleteOnExit();
		getFileQueue().remove(fileProperty);
		logger.info("File " + fileProperty.getFile().getPath() + " deleted successfully.");
    }

    public void clearFileQueue() {
        this.fileQueue.clear();
    }

    /**
    * The FileProperty class is used data about photo
    * 
    * @author  Alex Pinta, Oleh Pinta
    */
    public class FileProperty {
        private File file;
        private String fileHashCode;
        private FileProperty(File file) {
            this.file = file;
        }

        /**
         * This method is used to generate unique value for the file property.
         * @param buffer Buffer is used to generate hash MD5
         */
        private void generateHashCode(byte[] buffer) {
            final String ENCODING_METHOD = "MD5";
            try {
                final byte[] md5Transformation = MessageDigest.getInstance(ENCODING_METHOD).digest(buffer);
                this.fileHashCode = DatatypeConverter.printHexBinary(md5Transformation);
                logger.info("Hash code generated succesfully" + this.fileHashCode);
            } catch (NoSuchAlgorithmException e) {
                this.fileHashCode = "";
                logger.error("Error while generating hash code.", e);
            }
        }

        public String getFileHashCode() {
            return this.fileHashCode;
        }

        public File getFile() {
            return file;
        }
    }
}
