package helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class FileManager {
    private final byte[] BYTES = new byte[1024];
    private final int START_OFFSET = 0;

    @Value("${user.home.temp}") String USER_FOLDER;
    private Queue<File> fileQueue;

    public FileManager() {
        this.fileQueue = new ConcurrentLinkedDeque<File>();
    }

    public Queue<File> getFileQueue() {
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

        while ((read = stream.read(BYTES)) != -1) {
            out.write(BYTES, START_OFFSET, read);
        }
        this.fileQueue.add(file);
    }

    public void includeFile(String filePath, OutputStream outputStream) throws IOException {
        final Iterator<File> iterator = this.fileQueue.iterator();
        File file = new File("");
        int read;

        while (iterator.hasNext()) {
            file = iterator.next();
            if (file.getAbsolutePath().endsWith(filePath)) {
                break;
            }
        }
        if (!file.exists()) {
            InputStream imageStream = new FileInputStream(file);
            while ((read = imageStream.read(BYTES)) != -1) {
                outputStream.write(BYTES, START_OFFSET, read);
            }
            outputStream.flush();
        }

    }
}
