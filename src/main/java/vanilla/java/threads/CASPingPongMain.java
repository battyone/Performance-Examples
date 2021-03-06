package vanilla.java.threads;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by peter.lawrey on 12/01/2016.
 */
public class CASPingPongMain {
    final AtomicBoolean toggle = new AtomicBoolean();
    final AtomicLong count = new AtomicLong();

    public static void main(String... args) throws InterruptedException {
        CASPingPongMain ppm = new CASPingPongMain();

        Thread t1 = ppm.createThread(true);
        Thread t2 = ppm.createThread(false);
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        t1.interrupt();
        t2.interrupt();
        long time = System.currentTimeMillis() - start;
        t1.join();
        t2.join();
        System.out.printf("Counted to %,d toggles per second.%n", ppm.count.get() * 1000 / time);
    }

    @NotNull
    Thread createThread(boolean flag) {
        Thread thread = new Thread(() -> runLoop(flag), "toggle-" + flag);
        thread.start();
        return thread;
    }

    void runLoop(boolean flag) {
        while (!Thread.currentThread().isInterrupted()) {
            if (toggle.compareAndSet(!flag, flag)) {
//                count.getAndUpdate(l -> (l + 1) % 24);
                count.incrementAndGet();
            }
        }
        System.out.println(flag + " - finished");
    }
}
