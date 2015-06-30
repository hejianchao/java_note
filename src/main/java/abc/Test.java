package abc;

public class Test {


    public static void main(String[] args) {

        CSD cs = new CSD();
        CSD cs1 = new CSD();
        Thread th1 = new Thread(cs);
        Thread th2 = new Thread(cs1);
//        Thread th3 = new Thread(cs1);
        th1.start();
        th2.start();
//        th3.start();
    }

}

class CSD implements Runnable {

    public int a;

    public void run() {
        synchronized (CSD.class) { // 该对象锁就为cs
            a++;
            try {

                System.out.println("【当前线程】-----" + Thread.currentThread().getName() + "----a---" + a);
                Thread.sleep(5000);// 休眠5秒
                System.out.println(" 休眠5秒结束");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


}