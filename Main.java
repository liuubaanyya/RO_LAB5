import java.util.concurrent.atomic.AtomicInteger;

class Mutex {
    private int state = 0;
    private Thread owner = null;

    public synchronized void lock() throws InterruptedException {
        while (state == 1) {
            wait();
        }
        state = 1;
        owner = Thread.currentThread();
    }

    public synchronized void unlock() {
        if (Thread.currentThread() == owner) {
            state = 0;
            owner = null;
            notify();
        }
    }
}

class NewRecruit implements Runnable {
    private Mutex mutex;
    private AtomicInteger recruitsInOrder;
    private int id;

    public NewRecruit(Mutex mutex, AtomicInteger recruitsInOrder, int id) {
        this.mutex = mutex;
        this.recruitsInOrder = recruitsInOrder;
        this.id = id;
    }

    @Override
    public void run() {
        while (recruitsInOrder.get() < 100) {
            try {
                mutex.lock();
                int currentRecruitId = recruitsInOrder.getAndIncrement();
                mutex.unlock();
                boolean isFacingEachOther = currentRecruitId % 2 == 1;
                if (isFacingEachOther) {
                    System.out.println("Recruit " + id + " turned around.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Mutex mutex = new Mutex();
        AtomicInteger recruitsInOrder = new AtomicInteger(0);

        for (int i = 1; i <= 100; i++) {
            Thread recruitThread = new Thread(new NewRecruit(mutex, recruitsInOrder, i));
            recruitThread.start();
        }
    }
}
