package example.exercise.callabledemo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Blake on 2018/11/19
 * <p>
 * 使用implements Callable创建线程 VS implements Runnable 创建线程的区别在于：
 * 1）Callable 有返回值，而Runnable没有返回值
 * ==> 需要借助FutureTask(implements Future)接收返回值
 * 2）Callable可以处理异常，而Runnable没有异常处理能力
 */
public class CallableThreadTest {

    public static void main(String[] args) {

        CallableThread tr = new CallableThread();

        // Callable使用，需要借助 FutureTask类的支持，用于接收线程返回值，由于其具备"闭锁"的特性，因此，亦可以使用FutureTask实现闭锁功能
        FutureTask<Integer> ft = new FutureTask<Integer>(tr);

        long start = System.currentTimeMillis();

        new Thread(ft).start();

        try {
            System.out.println("=========================first");
            Integer sum = ft.get(); // tag1
            long end = System.currentTimeMillis();
            System.out.println("====sum:" + sum);
            System.out.println("=========================second");  // tag2

            /**
             * 闭锁特性的体现：
             * tag2的执行须得在tag1完全执行完毕后再执行。因此，判定FutureTask的表现和 CountDownLatch 特性是保持一致的。
             */

            System.out.println("线程计算共耗时：" + (end - start) + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

class CallableThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            sum += i;
        }
        Thread.sleep(2000);
        return sum;
    }
}
