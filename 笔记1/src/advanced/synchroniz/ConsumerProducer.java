package advanced.synchroniz;

/**
 * 经典案例:使用synchronized锁来实现生产者消费者的问题
 * @author Van.Planifolia
 */
public class ConsumerProducer {
    public static void main(String[] args) {
        //创建资源类，然后起2个线程来消费，2个线程来生产
        CU cu=new CU();
        //创建俩消费线程
        new Thread( ()->{
            for (int i = 0; i < 20; i++) {
                try {
                    cu.decNub();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者a").start();
        new Thread( ()->{
            for (int i = 0; i < 20; i++) {
                try {
                    cu.decNub();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"消费者b").start();
        //俩生产者线程
        new Thread( ()->{
            for (int i = 0; i < 20; i++) {
                try {
                    cu.addNub();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"生产者a").start();
        new Thread( ()->{
            for (int i = 0; i < 20; i++) {
                try {
                    cu.addNub();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"生产者b").start();
    }

}
//生产者消费者资源类
class CU{
    //票的资源数
    int ticke=0;
    //生产方法
    public synchronized void addNub() throws InterruptedException {
        //推荐使用while循环来进行判断，当票不为0时我们就让他等待,外部的唤醒
        while(ticke!=0){
            this.wait();
        }
        //票数增加之后唤醒消费者
        ticke++;
        System.out.println(Thread.currentThread().getName()+"->"+ticke);
        this.notifyAll();
    }
    //消费方法
    public synchronized void decNub() throws InterruptedException {
        //当票数为0的时候无法消费我们就等待外部唤醒
        while(ticke==0){
            this.wait();
        }
        //被唤醒后进行消费然后去唤醒生产者
        ticke--;
        System.out.println(Thread.currentThread().getName()+"->"+ticke);
        this.notifyAll();
    }
}