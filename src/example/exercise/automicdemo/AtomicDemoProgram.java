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
 * 通俗点的解释: ==> CPU去更新一个值，但如果想改的值不再是原来的值，操作就失败，因为很明显，有其它操作先改变了这个值
 * ==> 就是指当两者进行比较时，如果相等，则证明共享数据没有被修改，替换成新值，然后继续往下运行；如果不相等，说明共享数据已经被修改，
 * 放弃已经所做的操作，然后重新执行刚才的操作。容易看出 CAS 操作是基于共享数据不会被修改的假设，采用了类似于数据库的 commit-retry 的模式。
 * 当同步冲突出现的机会很少时，这种假设能带来较大的性能提升。
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
