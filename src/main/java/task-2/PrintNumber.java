package task2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//Завдання 2
//Напишіть програму, що виводить в консоль рядок, що складається з чисел від 1 до n,
// але з заміною деяких значень:
//
//        якщо число ділиться на 3 - вивести fizz
//        якщо число ділиться на 5 - вивести buzz
//        якщо число ділиться на 3 і на 5 одночасно - вивести fizzbuzz
// Наприклад, для n = 15, очікується такий результат: 1, 2, fizz, 4, buzz, fizz, 7, 8,
// fizz, buzz, 11, fizz, 13, 14, fizzbuzz.
//
//        Програма має бути багатопоточною, і працювати з 4 потоками:
//
// Потік A викликає fizz() щоб перевірити, чи ділиться число на 3, і якщо так -
// додати в чергу на виведення для потоку D рядок fizz.
// Потік B викликає buzz() щоб перевірити, чи ділиться число на 5, і якщо так -
// додати в чергу на виведення для потоку D рядок buzz.
// Потік C викликає fizzbuzz() щоб перевірити, чи ділиться число на 3 та 5 одночасно, і якщо так -
// додати в чергу на виведення для потоку D рядок fizzbuzz.
// Потік D викликає метод number(), щоб вивести наступне число з черги, якщо є таке число для виведення.
public class PrintNumber extends Thread {

    private int number;
    private AtomicBoolean processed = new AtomicBoolean(true);

    private NumberProcessor processor;

    public int getNumber() {
        return number;
    }

    public PrintNumber(int number) {
        this.number = number;
    }

    public PrintNumber(NumberProcessor processor) {
        this.processor = processor;
    }

    public void process(int number) {
        this.number = number;
        processed.set(false);
    }

    private boolean isProcessed() {
        return processed.get();

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (processed.get()) {
                break;
            }

            processor.process(number);
            processed.set(true);

        }
    }

    public static void main(String[] args) {

        PrintNumber digit = new PrintNumber(30);

        PrintNumber fizzbuzz = new PrintNumber((n) -> {
            if (n % 15 == 0) {
                System.out.print("fizzbuzz ");
            }
        });

        PrintNumber fizz = new PrintNumber((n) -> {
            if (n % 3 == 0 && n % 15 != 0) {
                System.out.print("fizz, ");
            }
        });

        PrintNumber buzz = new PrintNumber((n) -> {
            if (n % 5 == 0 && n % 15 != 0) {
                System.out.print("buzz, ");
            }
        });

        PrintNumber number = new PrintNumber((n) -> {
            if (n % 3 != 0 && n % 5 != 0) {
                System.out.print(n + ", ");
            }
        });
        List<PrintNumber> threads = new ArrayList<>();
        threads.add(fizzbuzz);
        threads.add(buzz);
        threads.add(fizz);
        threads.add(number);

        for (PrintNumber thread : threads) {
            thread.start();
        }

        for (int i = 1; i <= digit.getNumber(); i++) {
            for (PrintNumber thread : threads) {
                thread.process(i);
            }
            while (true) {
                int processedCount = 0;
                for (PrintNumber thread : threads) {
                    if (thread.isProcessed()) {
                        processedCount++;
                    }
                }

                if (processedCount == threads.size()) {
                    break;
                }
            }
        }
    }
}