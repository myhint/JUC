package example.exercise.producerconsumer.demo;

/**
 * @Description 生产者和消费者 & 虚假唤醒解决方案
 * ===> 需要考虑的问题：
 * ===> 1）等待&唤醒 机制的使用
 * ===> 2）线程同步 -- synchronized关键字使用同步代码块或者同步方法 | Lock 同步锁
 * ===> 3）虚假唤醒的解决方案：唤醒 wait() 应放置在循环体(while loop or for loop)当中，而非条件判断的if
 * @Author blake
 * @Date 2018/12/1 上午11:19
 * @Version 1.0
 */
public class ProducerAndConsumerThreadTest {

    public static void main(String[] args) {

        // 店员实例
        Clerk clerk = new Clerk();

        // 生产者实例
        Producer producer = new Producer(clerk);

        // 消费者实例
        Consumer consumer = new Consumer(clerk);

        // 线程启动模拟 => 生产者和消费者模型
        new Thread(producer, "生产者A1").start();
        new Thread(consumer, "消费者B1").start();

        new Thread(producer, "生产者A2").start();
        new Thread(consumer, "消费者B2").start();

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
                Thread.sleep(1000);
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

    /**
     * 买进 商品 => 作加法
     * => 同步方法
     */
    synchronized void buyIn() throws InterruptedException {

        // if (productQuantity >= 1) {
        while (productQuantity >= 1) {
            System.out.println(" ======== 商品已满 ======== ");
            // 等待
            this.wait();
        }

        // 唤醒
        this.notifyAll();
        System.out.println(Thread.currentThread().getName() + "买进商品，当前商品余量：" + ++productQuantity);
    }

    /**
     * 卖出 商品 => 作减法
     * => 同步方法
     */
    synchronized void sellOut() throws InterruptedException {

        // if (productQuantity <= 0) {
        while (productQuantity <= 0) {
            System.out.println(" ======== 商品售完 ======== ");
            // 等待
            this.wait();
        }

        // 唤醒
        this.notifyAll();
        System.out.println(Thread.currentThread().getName() + "售出商品，当前商品余量：" + --productQuantity);
    }

}



