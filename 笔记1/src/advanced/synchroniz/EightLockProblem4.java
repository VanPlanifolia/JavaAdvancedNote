package advanced.synchroniz;

import java.util.concurrent.TimeUnit;

/**
 * Created by Intellij IDEA
 * 八锁问题，其四 若两个方法均为静态方法则然后在进行下面的调用会发生什么？
 * @author Planifolia.Van
 * @Date 2022/8/3 20:03
 * @Version 1.0
 */
public class EightLockProblem4 {
    public static void main(String[] args) {
        Phone4 phone4=new Phone4();
        //发短信
        new Thread(()->{
            phone4.sendMail();
        },"A").start();
        //休眠3秒
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打电话,只不过是静态方法我们通过类名+方法名的方式调用
        new Thread(()->{
            phone4.call();
        },"B").start();
    }
    /*
     这次我们则发现结果反而和八锁问题其一一样了，就是先输出发短信然后在是打电话，现在细心的读者可能就已经类比出来了
     因为我们为两个方法都加上了static进行修饰也就是说这两个方法的锁都是static锁都是类锁，是锁住了整个类的静态方法
     所以我们使用同一个对象去调用这两个静态方法则会出现先拿到锁的发短信执行然后再是后拿到锁的打电话执行，那么如果是
     两个对象去分别调用这两个方法呢？请看终章！八锁其五
     */
}

/**
 * 编写一个操作类以及两个同步方法 打电话 发短信
 */
class Phone4{
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