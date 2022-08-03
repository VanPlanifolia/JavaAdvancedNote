package advanced.synchroniz;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

/**
 * 拓展案例: 使用JUC中的Lock锁来实现精准的唤醒操作
 * @author Van.Planifolia
 */
public class ConsumerProducerSp {
    public static void main(String[] args) {
        //创建资源类，然后起3个线程循环有序的执行
        CUSp cu=new CUSp();
        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                cu.printA();
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                cu.printB();
            }
        },"B").start();

        new Thread(()->{
            for (int i = 0; i < 20; i++) {
                cu.printC();
            }
        },"C").start();
    }

}

//精确唤醒测试类
class CUSp{
    //标志位，用来判断应该执行那个方法了 1执行pa，2执行pb，3执行pc
    int nub=0;
    //创建一个lock锁，用来锁上这三个同步方法
    Lock lock=new ReentrantLock();
    //虽然只有一个lock锁但是我们可以创建三个condition对象来实现精准唤醒
    Condition condition1=lock.newCondition();
    Condition condition2=lock.newCondition();
    Condition condition3=lock.newCondition();
    //方法A当nub=0的时候准许执行
    public void printA(){
        /*
        我们要实现这三个方法的同步我们就需要为这三个方法都上lock锁，当然上了锁一定记得要在finally中解锁，
        下面就是解锁开锁的案例
         */
        lock.lock();
        try {
            //使用while循环能够更好的解决假唤醒的问题
            //当nub不等于0的时候我们就让他等待，而如果条件满足了我们就将nub变成1并且唤醒condition2也就是方法pb
            while(nub!=0){
               condition1.await();
            }
            System.out.println("线程->>>>"+Thread.currentThread().getName()+"说：AAA");
            nub=1;
            condition2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    //方法B当nub=1的时候准许执行
    public void printB(){
        //try catch finally 上锁解锁，并且使用condition2进行方法同步
        lock.lock();
        try {
            while(nub!=1){
                condition2.await();
            }
            System.out.println("线程->>>>"+Thread.currentThread().getName()+"说：BBB");
            nub=2;
            //唤醒condition3的方法
            condition3.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    //方法B当nub=1的时候准许执行
    public void printC(){
        //try catch finally 上锁解锁，并且使用condition2进行方法同步
        lock.lock();
        try {
            while(nub!=2){
                condition3.await();
            }
            System.out.println("线程->>>>"+Thread.currentThread().getName()+"说：CCC");
            nub=0;
            //唤醒condition3的方法
            condition1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}