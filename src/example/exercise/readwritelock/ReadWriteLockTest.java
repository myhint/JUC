package example.exercise.readwritelock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description 读写锁
 * ==> 1）A写B写，需要互斥，A读B写或者B读A写，同样需要互斥
 * ==> 2）A读B读，无需互斥
 * @Author blake
 * @Date 2018/12/1 下午7:48
 * @Version 1.0
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {

        final ReadAndWrite readAndWrite = new ReadAndWrite();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 模拟线程延迟 0.01秒 => 可近似认为此次往磁盘写入数据，耗费0.01秒
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 写操作
                readAndWrite.set((int) (Math.random() * 100));
            }
        }, "Write :").start();

        for (int i = 1; i <= 100; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    // 读操作 多个线程并发执行。可能出现某些线程读取不到数据，这是因为这些线程在读操作之前抢先拿到锁，所以读不到数据。
                    System.out.println(Thread.currentThread().getName() + "读取到数据为：" + readAndWrite.get());
                }
            }).start();
        }

    }
}

class ReadAndWrite {

    private int quantity = 0;

    // 读写锁实例
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 写操作
     *
     * @param quantity
     */
    void set(int quantity) {
        // 上锁
        readWriteLock.writeLock().lock();

        try {
            this.quantity = quantity;
        } finally {
            // 解锁
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * 读操作
     *
     * @return
     */
    int get() {
        // 上锁
        readWriteLock.readLock().lock();

        try {
            return this.quantity;
        } finally {
            // 解锁
            readWriteLock.readLock().unlock();
        }
    }

}
