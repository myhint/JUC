package example.exercise.lockdemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 线程同步锁 Lock
 * @Author blake
 * @Date 2018/12/1 上午9:31
 * @Version 1.0
 */
public class LockThreadTest {

    public static void main(String[] args) {

        LockThreadDemo lockThread = new LockThreadDemo();
        new Thread(lockThread, "1号窗口").start();
        new Thread(lockThread, "2号窗口").start();
        new Thread(lockThread, "3号窗口").start();
        new Thread(lockThread, "4号窗口").start();
        new Thread(lockThread, "5号窗口").start();
    }

}

class LockThreadDemo implements Runnable {

    private int ticketNums = 100;

    // 创建同步锁 对象 => Lock是一个接口，ReentrantLock 是其一个实现类
    private Lock lock = new ReentrantLock();

    @Override
    public void run() {

        while (true) {
            /**
             * 上锁
             */
            lock.lock();

            if (ticketNums > 0) {
                try {
                    Thread.sleep(10);

                    System.out.println(Thread.currentThread().getName() + "售票成功，余票：" + --ticketNums);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    /**
                     * 解锁
                     */
                    lock.unlock();
                }
            }
        }
    }

}
