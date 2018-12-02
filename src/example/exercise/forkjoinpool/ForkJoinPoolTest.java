package example.exercise.forkjoinpool;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @Description Fork/Join 分支合并框架 & 工作窃取模式
 * ===> 需要借助 ForkJoinPool
 * @Author blake
 * @Date 2018/12/2 上午11:07
 * @Version 1.0
 */
public class ForkJoinPoolTest {

    public static void main(String[] args) {

        Instant start = Instant.now();

        // ForkJoin框架专用线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // ForkJoin框架的线程任务调度
        ForkJoinTask<Long> calculator = new ForkJoinCalculator(0L, 100000000L);

        Long result = forkJoinPool.invoke(calculator);

        Instant end = Instant.now();

        long duration = Duration.between(start, end).toMillis();

        System.out.println("此次计算总共耗时为：" + duration + "毫秒");

        System.out.println("ForkJoin策略的最终计算结果为：" + result);

    }

}

class ForkJoinCalculator extends RecursiveTask<Long> {

    private static final long serialVersionUID = 2340013586181697365L;

    // 左边界
    private Long start = 0L;

    // 右边界
    private Long end = 0L;

    public ForkJoinCalculator(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    // 临界值
    private static final Long THRESHOLD = 100000L;

    @Override
    protected Long compute() {

        // 差值
        Long difference = end - start;

        Long sum = 0L;

        if (difference <= THRESHOLD) {

            for (long i = start; i <= end; i++) {
                sum += i;
            }

            return sum;
        } else {

            /**
             * 递归拆分并执行任务
             */
            Long tempThreshold = (start + end) / 2;

            ForkJoinCalculator leftCalculator = new ForkJoinCalculator(start, tempThreshold);
            // 左拆分
            leftCalculator.fork();

            ForkJoinCalculator rightCalculator = new ForkJoinCalculator(tempThreshold + 1, end);
            // 右拆分
            rightCalculator.fork();

            return leftCalculator.join() + rightCalculator.join();
        }
    }

}
