package example.exercise.countDownLatchDemo;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Blake on 2018/11/19
 * <p>
 * CountDownLatch:闭锁 ==> 当完成某些运算时，只有当其他所有线程都执行完毕后，当前线程才能紧接着执行后续操作
 */
public class CountDownLatchTest {

    public static void main(String[] args) {

        // 创建"闭锁"实例
        final CountDownLatch countDownLatch = new CountDownLatch(10);

        ThreadInstance ti = new ThreadInstance(countDownLatch);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            new Thread(ti).start();
        }

        try {
            countDownLatch.await(); // 等待
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("线程执行完毕，全程耗时：" + (end - start) + " ms");

    }

}

class ThreadInstance implements Runnable {

    private CountDownLatch latch;

    public ThreadInstance(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        int sum = 0;

        try {
            for (int i = 0; i < 1000; i++) {
                sum += i;
            }
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }
    }

}
