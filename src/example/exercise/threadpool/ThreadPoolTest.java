package example.exercise.threadpool;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description 线程池
 * @Author blake
 * @Date 2018/12/1 下午8:46
 * @Version 1.0
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 按照给定配置项，初始化线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 3000, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));

        for (int i = 0; i < 10; i++) {

            FutureTask<Integer> integerFutureTask = new FutureTask<Integer>(new Callable<Integer>() {

                private int sum = 0;

                @Override
                public Integer call() throws Exception {

                    for (int i = 1; i <= 100; i++) {
                        sum += i;
                    }

                    System.out.println(Thread.currentThread().getName() + " : " + sum);
                    return sum;
                }
            });

            threadPoolExecutor.execute(integerFutureTask);
        }

        System.out.println(threadPoolExecutor);

        System.out.println("=========");

        // 关闭线程池
        threadPoolExecutor.shutdown();
    }

}


class Calculator implements Callable<Integer> {

    private int sum = 0;

    @Override
    public Integer call() throws Exception {

        for (int i = 0; i <= 100; i++) {
            sum += i;
        }

        return sum;
    }
}