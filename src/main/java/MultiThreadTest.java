import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//Завдання 1
//Напишіть програму, яка кожну секунду відображає на екрані дані про час, що пройшов
// від моменту запуску програми.
//Другий потік цієї ж програми кожні 5 секунд виводить повідомлення Пройшло 5 секунд.

public class MultiThreadTest extends Thread {
    private AtomicInteger counter = new AtomicInteger();

    @Override
    public void run() {
        while (true) {
            System.out.println("Час виконання програми " + counter + " секунд");
            try {
                Thread.sleep(1000);
                counter.getAndIncrement();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {

        MultiThreadTest thread = new MultiThreadTest();
        thread.start();

        while (true) {
            Thread.sleep(5000);
            System.out.println("Пройшло 5 секунд");
        }
    }
}
