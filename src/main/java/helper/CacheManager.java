package helper;

import org.springframework.stereotype.Component;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class CacheManager {
    private ConcurrentLinkedDeque<OutputStream> fileQueue;

    public CacheManager() {
        this.fileQueue = new ConcurrentLinkedDeque<>();
    }

    public ConcurrentLinkedDeque<OutputStream> getFileQueue() {
        return this.fileQueue;
    }

    public void addFile(OutputStream outputStream) {
        this.fileQueue.addLast(outputStream);
    }
}
