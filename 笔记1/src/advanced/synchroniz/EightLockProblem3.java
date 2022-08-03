package advanced.synchroniz;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA
 * 八锁问题，其三：被synchronized修饰的静态方法与非静态方法被同一个对象调用会有什么效果？
 * @author Planifolia.Van
 * @Date 2022/8/3 20:03
 * @Version 1.0
 */
public class EightLockProblem3 {
    public static void main(String[] args) {
        Phone3 phone3=new Phone3();
        //发短信
        new Thread(phone3::sendMail,"A").start();
        //休眠3秒
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打电话,只不过是静态方法我们通过类名+方法名的方式调用
        new Thread(()->{
            phone3.call();
        },"B").start();
    }
    /*
     我们运行之后得到的结果可以看出，也是先打出电话才发送的短信，根据之前的两个案例我们可以得知显然这静态方法与非静态方法
     也不是同一把锁，调用发短信方法并且获得到锁之后并没有影响到获取静态方法的锁，所以通过这个例子我们可以知道普通方法的锁
     和静态方法中的锁不是同一把，所以在获取到其中一种方法的锁之后还允许获取另外一把锁。
     */
}

/**
 * 编写一个操作类以及两个同步方法 打电话 发短信
 */
class Phone3{
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
     * 模拟打电话，这次不在休眠并且将这个方法设置为static
     */
    public static synchronized void call(){
        System.out.println(Thread.currentThread().getName()+"----->打电话");
    }
}