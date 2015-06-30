package abc;

public class ABC {
    public static void main(String[] args) {
        BaseClass baseClass = new BaseClass();
        ChildClass childClass = new ChildClass();

        Thread th1 = new DemoThread1(baseClass);
        Thread th2 = new DemoThread2(childClass);

        th1.start();
        th2.start();
    }
}

class BaseClass {
    public synchronized void doSomeThing() {
        System.out.println("parent class：begin.....doSomeThing");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("parent class：end.....doSomeThing");
    }

}

class ChildClass extends BaseClass {

    public synchronized void childMethod() {
        System.out.println("---child class：begin.....childMethod");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.doSomeThing();
        System.out.println("---child class：end.....childMethod");

    }

}

class DemoThread1 extends Thread {
    private BaseClass base = null;

    public DemoThread1(BaseClass base) {
        this.base = base;
    }

    @Override
    public void run() {
        base.doSomeThing();
    }
}

class DemoThread2 extends Thread {
    private ChildClass child = null;

    public DemoThread2(ChildClass child) {
        this.child = child;
    }

    @Override
    public void run() {
        child.childMethod();
    }
}
