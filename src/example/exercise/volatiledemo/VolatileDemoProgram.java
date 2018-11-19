package example.exercise.volatiledemo;

/**
 * Created by Blake on 2018/11/18
 * <p>
 * volatile 关键字 : 当涉及 多个线程进行操作共享数据时，保证共享数据的内存可见性。
 */
public class VolatileDemoProgram {

    public static void main(String[] args) {

        ThreadDemoTest thread = new ThreadDemoTest();
        new Thread(thread).start();

        while (true) {
            if (thread.isFlag()) {
                System.out.println("====================");
                break;
            }
        }

    }


}

class ThreadDemoTest implements Runnable {

    private volatile boolean flag = false;

    public boolean isFlag() {
        return flag;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!flag) {
            flag = true;
            System.out.println("flag == " + isFlag());
        }
    }
}


