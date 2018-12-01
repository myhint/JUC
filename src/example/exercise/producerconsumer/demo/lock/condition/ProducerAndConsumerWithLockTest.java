package example.exercise.producerconsumer.demo.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description 生产者和消费者 & 虚假唤醒解决方案
 * <p>
 * ==> Lock & Condition 实现线程的同步，等待与唤醒
 * 特别注意：使用 lock & condition 配合控制线程通信，应该切换至使用 condition 自己的等待与唤醒机制，而非 object 对象的等待与唤醒机制，
 * 否则将抛出异常 java.lang.IllegalMonitorStateException
 * <p>
 * ===> 需要考虑的问题：
 * ===> 1）等待&唤醒 机制的使用
 * ===> 2）线程同步 -- synchronized关键字使用同步代码块或者同步方法 | Lock 同步锁
 * ===> 3）虚假唤醒的解决方案：唤醒 wait() 应放置在循环体(while loop or for loop)当中，而非条件判断的if
 * @Author blake
 * @Date 2018/12/1 上午11:19
 * @Version 1.0
 */
public class ProducerAndConsumerWithLockTest {

    public static void main(String[] args) {
        // 店员实例
        Clerk clerkInstance = new Clerk();

        // 生产者实例
        Producer producerInstance = new Producer(clerkInstance);

        // 消费者实例
        Consumer consumerInstance = new Consumer(clerkInstance);

        // 线程启动模拟 => 生产者和消费者模型
        new Thread(producerInstance, "生产者C1").start();
        new Thread(consumerInstance, "消费者D1").start();

        new Thread(producerInstance, "生产者C2").start();
        new Thread(consumerInstance, "消费者D2").start();
    }

}

/**
 * 生产者
 */
class Producer implements Runnable {

    private Clerk clerk;

    public Producer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {

        for (int i = 1; i <= 50; i++) {

            try {
                // 延迟1秒
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 生产者通知店员买进商品
            try {
                clerk.buyIn();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

/**
 * 消费者
 */
class Consumer implements Runnable {

    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {

        for (int i = 1; i <= 50; i++) {
            // 店员将商品售出给消费者
            try {
                clerk.sellOut();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

/**
 * 商店员工
 */
class Clerk {

    // 商品数量
    private int productQuantity = 0;

    // lock 实例
    private Lock lock = new ReentrantLock();

    // 由已有的lock实例引出condition对象，进而控制线程通信
    private Condition condition = lock.newCondition();

    /**
     * 买进 商品 => 作加法
     * => 同步方法
     */
    void buyIn() throws InterruptedException {

        // 上锁
        lock.lock();

        try {
            // if (productQuantity >= 1) {
            while (productQuantity >= 1) {
                System.out.println(" ======== 商品已满 ======== ");
                // 等待
                condition.await();
            }

            // 唤醒
            condition.signalAll();
            System.out.println(Thread.currentThread().getName() + "买进商品，当前商品余量：" + ++productQuantity);
        } finally {

            // 解锁
            lock.unlock();
        }
    }

    /**
     * 卖出 商品 => 作减法
     * => 同步方法
     */
    void sellOut() throws InterruptedException {

        // 上锁
        lock.lock();

        try {
            // if (productQuantity <= 0) {
            while (productQuantity <= 0) {
                System.out.println(" ======== 商品售完 ======== ");
                // 等待
                condition.await();
            }

            // 唤醒
            condition.signalAll();
            System.out.println(Thread.currentThread().getName() + "售出商品，当前商品余量：" + --productQuantity);
        } finally {
            // 解锁
            lock.unlock();
        }
    }

}
