package example.exercise.alternationdemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 线程按序交替
 * ==> 有3个线程A,B,C,按照ABBCCC的顺序打印3次
 * @Author blake
 * @Date 2018/12/1 上午10:40
 * @Version 1.0
 */
public class AlternationThreadTest {

    // 整个流程总共执行多少次（轮）
    private static final int TOTAL_TIMES = 3;

    /**
     * 入口main函数
     *
     * @param args
     */
    public static void main(String[] args) {

        final AlternativeThread alternativeThread = new AlternativeThread();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= TOTAL_TIMES; i++) {
                    alternativeThread.turnToA();
                }
            }

        }, "线程A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= TOTAL_TIMES; i++) {
                    alternativeThread.turnToB();
                }
            }

        }, "线程B").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= TOTAL_TIMES; i++) {
                    alternativeThread.turnToC();
                    System.out.println("============ 华丽的分割线 ============ ");
                }
            }

        }, "线程C").start();

    }

}

class AlternativeThread {

    // 当前线程的数值标识码
    private int order = 1;

    // 同步锁
    private Lock lock = new ReentrantLock();

    private Condition conditionA = lock.newCondition();
    private Condition conditionB = lock.newCondition();
    private Condition conditionC = lock.newCondition();

    void turnToA() {
        // 上锁
        lock.lock();

        try {
            if (order != 1) {
                try {
                    // 等待
                    conditionA.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 1; i <= 1; i++) {
                System.out.println(Thread.currentThread().getName() + " => A");
            }

            order = 2;
            // 唤醒特定线程
            conditionB.signal();
        } finally {
            // 解锁
            lock.unlock();
        }
    }

    void turnToB() {
        // 上锁
        lock.lock();

        try {
            if (order != 2) {
                try {
                    // 唤醒
                    conditionB.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 1; i <= 2; i++) {
                System.out.println(Thread.currentThread().getName() + " => B");
            }

            order = 3;
            // 唤醒特定线程
            conditionC.signal();
        } finally {
            // 解锁
            lock.unlock();
        }
    }

    void turnToC() {
        // 上锁
        lock.lock();

        try {
            if (order != 3) {
                try {
                    // 唤醒
                    conditionC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 1; i <= 3; i++) {
                System.out.println(Thread.currentThread().getName() + " => C");
            }

            order = 1;
            // 唤醒特定线程
            conditionA.signal();
        } finally {
            // 解锁
            lock.unlock();
        }
    }

}

