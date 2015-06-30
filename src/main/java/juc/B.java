package juc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class B {
    public static void main(String[] args) throws Exception {
        Unsafe unsafe = getUnsafeInstance();
        System.out.println(unsafe.pageSize());

        //This creates an instance of player class without any initialization
        Player p = (Player) unsafe.allocateInstance(Player.class);

        //Print 0
        System.out.println(p.getAge());

        //Let's now set age 45 to un-initialized object
        p.setAge(45);
        //Print 45
        System.out.println(p.getAge());

        //This the normal way to get fully initialized object; Prints 50
        System.out.println(new Player().getAge());


        String password = new String("l00k@myHor$e");
        String fake = new String(password.replaceAll(".", "?"));
        System.out.println(password); // l00k@myHor$e
        System.out.println(fake); // ????????????

//        unsafe.copyMemory(fake, 0L, null, unsafe.toAddress(password), sizeOf(password));

        System.out.println(password); // ????????????
        System.out.println(fake); // ????????????
    }

    public static Unsafe getUnsafeInstance() throws Exception {
        // 通过反射获取rt.jar下的Unsafe类
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        // return (Unsafe) theUnsafeInstance.get(null);是等价的
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);
    }
}

class Player {
    private int age = 12;

    public Player() {        //Even if you create this constructor private;
        //You can initialize using Unsafe.allocateInstance()
        this.age = 50;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
