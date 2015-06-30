package abc;

import java.util.concurrent.TimeUnit;

public class NoVisibility {
    private static int number;
    private static boolean ready;

    static class ReadThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReadThread().start();
        number = 42;
        ready = true;

        try {
            TimeUnit.SECONDS.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
