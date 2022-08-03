package advanced.synchroniz;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA
 * 八锁问题，其一：被synchronized修饰的实例方法通过一个对象进行调用
 * @author Planifolia.Van
 * @date  2022/8/2 20:48
 * @version 1.0
 */
public class EightLockProblem1 {
    public static void main(String[] args) {
        //我们创建一个实体对象，然后让这个对象去打电话然后休眠三秒，试问此时程序会如何执行
        Phone phone1=new Phone();
        //函数式接口可以使用：：来代替拉姆得表达式->(){}
        new Thread(phone1::sendMail,"A").start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(phone1::call,"B").start();
    }
    /*
      显然我们在执行完毕之后得出了结果，必然是发短信先执行，但是为什么呢？
      我们都知道线程调用的顺序是不可估计的他是由jvm来分配的，但是我们为打电话与发短信方法都添加了synchronized进行修饰
      并且synchronized关键字锁的是实例方法也就是锁住了当前这个对象，不允许当前这个对象再去访问其他的同步方法，所以结果
      就很显然易见了，因为我们在启动第二个线程的时候中间休眠了三秒所以线程A会先拿到phone1对象的锁然后线程B去访问另一个
      同步方法就被拒之门外了，必须要等待线程A执行完毕才能进入另一个同步方法，所以就出现了先打电话后发短信的现象。
      当然若类中有没有被synchronized修饰的方法那么就不会收到synchronized锁的影响，能够正常调用！
     */
}
/**
 * 编写一个操作类以及两个同步方法 打电话 发短信
 */
class Phone{
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
     * 模拟打电话
     */
    public synchronized void call(){
        System.out.println(Thread.currentThread().getName()+"----->打电话");
    }
}