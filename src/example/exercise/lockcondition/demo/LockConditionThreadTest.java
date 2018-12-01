package example.exercise.lockcondition.demo;

/**
 * @Description Condition 控制线程通信
 * <p>
 * ==> 0）Lock lock = new ReentrantLock();
 * Condition condition = lock.newCondition();
 * <p>
 * ==> 1）condition.await();
 * ==> 2）condition.signal();
 * ==> 3）condition.signalAll();
 * <p>
 * ===========
 * @Author blake
 * @Date 2018/12/1 下午12:19
 * @Version 1.0
 */
public class LockConditionThreadTest {

    public static void main(String[] args) {

    }

}
