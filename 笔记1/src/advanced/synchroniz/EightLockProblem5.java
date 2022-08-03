package advanced.synchroniz;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA
 * 八锁问题，其五 若两个方法均为静态方法然后在通过两个对象进行调用呢？
 * @author Planifolia.Van
 * @Date 2022/8/3 20:03
 * @Version 1.0
 */
public class EightLockProblem5 {
    public static void main(String[] args) {
        Phone5 phone5=new Phone5();
        Phone5 phone55=new Phone5();

        //发短信
        new Thread(()->{
            phone5.sendMail();
        },"A").start();
        //休眠3秒
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打电话,只不过是静态方法我们通过类名+方法名的方式调用
        new Thread(()->{
            phone55.call();
        },"B").start();
    }
    /*
     发短信！又是他！这次又是发短信先执行，这样我们就很明朗了，因为我们已经知道了锁住静态方法的锁是锁整个类，因为静态
     方法是随着类加载而被加载的甚至可以通过类名去调用它所以不管我们有多少个对象都没什么用本质上还是相当于类名.方法名的
     调用，所以打电话必须等发短信执行完毕释放锁才能继续执行！
     */

    /*
     其实讲到这里我们已经把所有的情况已经列举完毕了，总的概括来说在java中一共有两种锁。

     第一种对象锁：这种锁会锁住整个对象，不允许当前这个对象再去进入其他的同步非静态方法(也就是被synchronized修饰的其他
     非静态方法)，但是允许当前对象去访问非同步方法，这点毋庸置疑因为非同步方法并不受同步的限制。并且允许当前对象去访问同步
     的静态方法，在这说是这个对象去访问静态方法不如说是类去访问静态方法，因为静态方法的锁和非静态方法的锁不是一把静态方法
     的锁是锁住整个对象，所以这两者也不互相干扰！

     第二种类锁：这种锁会锁住整个类，就算我们创建若干个对象去分别访静态同步方法都是不允许的，因为这个锁锁住了整个类不允许
     其他人再去访问同样拥有类锁的同步方法也就是被synchronized修饰的静态方法！但是对于普通方法和非静态的同步方法则不受影
     响，因为这两位其中一个是对象锁与类锁互不干扰，另一位压根就没锁更不受影响！还有一点需要注意的是synchronized锁代码
     块的时候使用的是类锁！
     */
}

/**
 * 编写一个操作类以及两个同步方法 打电话 发短信
 */
class Phone5{
    /**
     * 模拟同步方法发短信，并且延迟三秒方便看到同步效果
     */
    public static synchronized void sendMail(){
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
