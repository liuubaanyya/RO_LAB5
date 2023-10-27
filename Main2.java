import java.util.concurrent.atomic.AtomicInteger;

public class Main2 {
    public static void main(String[] args) {
        String[] strings = new String[] {"ABCD", "ABCD", "ABCD", "ABCD"};
        AtomicInteger aCount = new AtomicInteger(0);
        AtomicInteger bCount = new AtomicInteger(0);

        Thread[] threads = new Thread[4];

        for (int i = 0; i < 4; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                while (aCount.get() < 3 || bCount.get() < 3) {
                    synchronized (strings) {
                        if (strings[index].contains("A")) {
                            strings[index] = strings[index].replace("A", "C");
                            aCount.incrementAndGet();
                        } else if (strings[index].contains("C")) {
                            strings[index] = strings[index].replace("C", "A");
                            aCount.decrementAndGet();
                        } else if (strings[index].contains("B")) {
                            strings[index] = strings[index].replace("B", "D");
                            bCount.incrementAndGet();
                        } else if (strings[index].contains("D")) {
                            strings[index] = strings[index].replace("D", "B");
                            bCount.decrementAndGet();
                        }
                        System.out.println("Thread " + (index + 1) + ": " + strings[index]);
                    }
                }
            });
            threads[i].start();
        }
    }
}
