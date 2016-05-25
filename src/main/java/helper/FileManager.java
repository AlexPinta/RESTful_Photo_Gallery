package helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class FileManager {
    private final byte[] BYTES = new byte[1024];
    private final int START_OFFSET = 0;

    @Value("${user.home.temp}") String USER_FOLDER;
    private Queue<FileProperty> fileQueue;

    public FileManager() {
        this.fileQueue = new ConcurrentLinkedDeque<>();
    }

    public Queue<FileProperty> getFileQueue() {
        return this.fileQueue;
    }

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
    }

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
        }
        return fileBytes;
   }

    public void clearFileQueue() {
        this.fileQueue.clear();
    }

    class FileProperty {
        private File file;
        private String fileHashCode;
        private FileProperty(File file) {
            this.file = file;
        }

        private void generateHashCode(byte[] buffer) {
            try {
                final byte[] md5Transformation = MessageDigest.getInstance("MD5").digest(buffer);
                this.fileHashCode = DatatypeConverter.printHexBinary(md5Transformation);
            } catch (NoSuchAlgorithmException e) {
                this.fileHashCode = "";
                //TODO logging
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
