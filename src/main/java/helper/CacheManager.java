package helper;

import org.springframework.stereotype.Component;
import java.io.OutputStream;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class CacheManager {
    private Deque<OutputStream> fileQueue;

    public CacheManager() {
        this.fileQueue = new ConcurrentLinkedDeque<>();
    }

    public Deque<OutputStream> getFileQueue() {
        return this.fileQueue;
    }

    public void addFile(OutputStream outputStream) {
        this.fileQueue.addLast(outputStream);
    }
}
