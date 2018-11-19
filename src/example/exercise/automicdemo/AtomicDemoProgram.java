package example.exercise.automicdemo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Blake on 2018/11/18
 * <p>
 * 原子变量：JDK1.5 之后，java.util.concurrent.atomic 包下提供了许多常用的原子变量：
 * 1）其中的变量皆使用 volatile 关键字修饰，保证了内存可见性
 * 2）使用 CAS(Compare-And-Swap) 算法，保证数据的原子性
 * CAS ==> 是计算机硬件对并发操作共享数据的支持
 * <p>
 * CAS 包含3个操作数：
 * ==> 内存值 V
 * ==> 预估值 A
 * ==> 更新值 B
 * 当且仅当 V==A时，V==B，赋值成功；否则，将不再执行任何操作
 * <p>
 * CAS 算法的效率要比同步锁的效率要高得多
 */
public class AtomicDemoProgram {

    public static void main(String[] args) {

        AtomicThreadTest thread = new AtomicThreadTest();

        for (int i = 0; i < 10; i++) {
            new Thread(thread).start();
        }

    }
}

class AtomicThreadTest implements Runnable {

    //    private volatile int serialNumber = 0;
    // 使用原子变量保证数据更新操作的唯一可行性
    private AtomicInteger serialNumber = new AtomicInteger();

    public int getSerialNumber() {
        return serialNumber.getAndIncrement();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " ==> " + getSerialNumber());
    }
}
