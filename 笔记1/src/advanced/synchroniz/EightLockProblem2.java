package advanced.synchroniz;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA
 *八锁问题，其二：被synchronized修饰的实例方法通过两个对象进行调用
 * @author Planifolia.Van
 * @Date: 2022/8/2 21:12
 * @Version 1.0
 */
public class EightLockProblem2 {
    public static void main(String[] args) throws InterruptedException {
        //我们这次创建两个对象去通过连个线程去调用Phone对象中的两个同步方法，试问会发生什么事情呢？
        Phone2 phone21=new Phone2();
        Phone2 phone22=new Phone2();

        //起一个线程去调用发送短信方法
        new Thread(phone21::sendMail, "A").start();
        TimeUnit.SECONDS.sleep(3);

        //起一个线程去调用打电话
        new Thread(phone22::call,"B").start();

    }
    /*
      显然我们在执行完毕之后得出了结果，是先打电话在去发短信，为什么捏？因为我们创建了两个对象并且synchronized锁
      锁的是对象也就是对于同一个对象同时只能进入一个同步方法，但是对于另外一个对象则不受影响，这样这连个对象是不同的
      锁互不干扰
     */
}
/**
 * 编写一个操作类以及两个同步方法 打电话 发短信
 */
class Phone2{
    /**
     * 模拟同步方法发短信，并且延迟三秒方便看到同步效果
     */
    public synchronized void sendMail(){
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"----->发短信");
    }

    /**
     * 模拟打电话，这次不在休眠
     */
    public synchronized void call(){
        System.out.println(Thread.currentThread().getName()+"----->打电话");
    }
}
