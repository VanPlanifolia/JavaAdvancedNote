## 1.单例模式

1.1 综述：单例模式是java中的一种设计模式，他规定某一个类只允许被初始化一次，也就是后续无论如何去获取这个对象都要是同一个对象，常见的单例模式为，饿汉模式与懒汉模式，其中饿汉模式一上来就把这个单例对象创建好然后等其他的模块去调用这个对象，而懒汉模式则是等有人使用到这个对象时才去创建。显然我们可以看到，饿汉模式是线程安全的但是会浪费资源空间，而懒汉模式是线程不安全的但是节省资源，后面我们来用代码来展示饿汉与懒汉模式。

1.2 饿汉模式，饿汉模式顾名思义上来就需要创建这个对象准备着，然后等待被他人调用，而且方式外部使用构造器来创建其他的对象我们要将构造方法私有化。然后我们还需要创建一个外部获取这个对象的接口，也就是一个公开的获取这个对象的方法。这个模式绝对安全吗？当然不，我们知道反射是一个很强大功能，我们使用反射很轻易的就能拿到他的构造方法并且破坏它的私有性。从而去创建一个新的对象。

```java
/**
 * 饿汉模式实现单例，首先单例模式最重要的一点构造方法私有化
 */
public class HangryMan {
    //私有化的构造方法
    private HangryMan(){}
    //刚开始我们就创建这个对象,设置成私有
    private static final HangryMan hangryMan=new HangryMan();
    //公有的获取对象方法
    public static HangryMan getHangryMan(){
        return hangryMan;
    }
}

```

1.3 懒汉模式。懒汉模式则是他不会上来就创建好这个对象而是等待有人去调用他的时候他才去创建这个对象，显然这样节省了内存资源。

* 单例检查v1，单线程下防止多次创建，但是多线程下不安全

```java
/**
 * 懒汉模式，v1，最基础的单例检测，线程不安全
 */
public class LazyMan {
    //创建这个对象变量
    private static LazyMan lazyMan;
    //构造器私有化
    private LazyMan(){}
    //公有的获取对象方法
    public LazyMan getLazyMan(){
        //当这个对象为空的时候我们再去创建他
        if(lazyMan==null){
            return new LazyMan();
        }
        //否则我们只返这个对象
        return lazyMan;
    }
}
```

* 单例检测v2，双重锁防止单例，为什么我们要用双重校验呢？首先我们来看下面的代码，比如同时有两个线程进入了getLazyMan()方法此时lazyMan没有被初始化，也就是null，显然两个都可以通过第一次校验，然后这两个线程中的某一个拿到了锁，另一个线程被阻塞等待，等第一个线程创建完对象后第二个线程进入锁，如果我们在这没添加第二层检测那么第二个进来的线程不知道第一个线程已经创建了这个对象，他也会去new一个新的对象，显然这样就破坏了我们单例的原则。所以需要加入第二曾检测。

```java
/**
 * 懒汉模式，v2，双重检测锁模式
 */
public class LazyMan {
    //创建这个对象变量
    private static LazyMan lazyMan;
    //构造器私有化
    private LazyMan(){}
    //公有的获取对象方法
    public static LazyMan getLazyMan(){
        //当这个对象为空的时候我们再去创建他
        if(lazyMan==null){
            //当准备创建对象的时候我们上锁
            synchronized (LazyMan.class){
                //拿到锁后我们在检测意见
                if (lazyMan==null){
                    return new LazyMan();
                }
            }
        }
        //否则我们只返这个对象
        return lazyMan;
    }
}
```

* 单例模检测v3，双重检测锁+防止指令重排
  * 由于我们这一步操作 new LazyMan();并不是原子操作。
    * 他可以分为三步
    * 1.为这个实例对象分配内存空间
    * 2.执行构造方法，初始化对象
    * 3.将对象的引用指向对应的对象实例地址
    * 由于jvm虚拟机可能会发生指令重排，比如132的次序，那么我们可能还在对象没初始化完毕就把对象引用指向了对于的内存空间，导致另外一个线程发现这个对象引用不为空而直接去返回这个对象但是这个对象还没有完成初始化，从而获得一个错误的对象素养小狐妖引入volatile关键字防止指令重排

```java
/**
 * 懒汉模式，v3，双重检测锁+防止指令重排
 */
public class LazyMan {
    //创建这个对象变量,volatile关键字是用来防止指令重排的
    private static volatile LazyMan lazyMan;
    //构造器私有化
    private LazyMan(){}
    //公有的获取对象方法
    public static LazyMan getLazyMan(){
        //当这个对象为空的时候我们再去创建他
        if(lazyMan==null){
            //当准备创建对象的时候我们上锁
            synchronized (LazyMan.class){
                //拿到锁后我们在检测意见
                if (lazyMan==null){
                    /**
                     * 由于我们这一步操作 new LazyMan();并不是原子操作。
                     * 他可以分为三步
                     * 1.为这个实例对象分配内存空间
                     * 2.执行构造方法，初始化对象
                     * 3.将对象的引用指向对应的对象实例地址
                     * 由于jvm虚拟机可能会发生指令重排，比如132的次序，那么我们
                     * 可能还在对象没初始化完毕就把对象引用指向了对于的内存空间，导致
                     * 另外一个线程发现这个对象引用不为空而直接去返回这个对象但是这个对象
                     * 还没有完成初始化，从而获得一个错误的对象素养小狐妖引入volatile关键字
                     * 防止指令重排
                     */
                    lazyMan = new LazyMan();
                }
            }
        }
        //返回这个对象
        return lazyMan;
    }
}
```

* 这样就是万无一失的单例模式了吗？当然不是使用反射很容易就能够破解我们的这种单例模式，通过反射来获取到构造器，并且去除他的私有特性即可去创建一个新的对象。
  然后我们可以在其中引入一个红绿灯检测方法，也就是引入一个变量当懒汉模式创建过一次对象之后就把标记置为false然后不允许他再次进行new操作，然鹅这种也会被反射破解....，在java中最完美的单例就是枚举类型！

## 2. 使用synchronized锁来实现生产者消费者的问题

2.1 什么是synchronized，synchronized是一种线程锁，他可以锁住某个实例方法当锁住实例方法的时候，他会对当前这个对象加锁。也就是不允许当前对象在进入这个方法以及其他同步方法，但是允许其他对象访问synchronized方法以及静态方法。反之亦然，如果synchronized锁的是静态方法那么则允许其他对象进入非静态方法。但是要是两个方法都是静态方法或者都是非静态方法那么如果在这两个方法中调用了同步监视器的wait方法那么也是允许其他线程进入这个方法的，但是如果没有同步监视器的wait方法那么就不允许其他的线程进入这个方法。

2.2 经典案例使用synchronized来实现生产者消费者同步的问题，生产者与消费者，当生产者生产出来一个产品的时候则通知消费者去消费产品，而如果生产者没有生产出来的时候消费者需要等待生产者生产出来的时候需要通知消费者来消费，反之当消费者还么消费的时候生产者需要等待而当消费者消费完了之后就通知生产者来生产。案例的思想基本上就这样具体看下面的例子。

```java
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
```

## 3.使用Lock锁来实现三个线程之间的精准唤醒。

3.1 Lock锁是JUC包中的一款锁他相较于synchronized能够更加优雅的处理方法间的同步问题，总的来说他是一个相较于synchronized更好用的一个锁，不仅能够解决线程间的同步问题而且能够精确的唤醒哪一个方法。如你所见Lock是一个接口类，我们需要去创建他的实现类

ReentrantLock()来获取到Lock对象，而且我们要对一些方法实现同步操作则需要通过lock对象来创建Condition，Condition同样也是一个接口，这个接口提供了await(),single()这些方法来实现线程同步问题，就如同object类中的wait(),notyfal()方法一样而与synchronized不同的是我们可以创建多个Condition对象来实现对不同方法的精确控制，具体见我们下面的代码案例。

```java
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
```

## 4. synchronized八锁问题探究

4.1 synchronized可以锁住实例方法，可以锁住静态方法，可以锁住代码块，可以锁住类这些都是我们知道的，其实本质上synchronized上锁就只有两种锁一个是类锁锁住整个类，一个是对象锁锁住当前对象，接下来我们就用经典的八锁问题去探究一下这两把锁的区别。

* 八锁问题，其一：被synchronized修饰的实例方法通过一个对象进行调用会发生什么？

  显然我们在执行完毕之后得出了结果，必然是发短信先执行，但是为什么呢？
  我们都知道线程调用的顺序是不可估计的他是由jvm来分配的，但是我们为打电话与发短信方法都添加了synchronized进行修饰
  并且synchronized关键字锁的是实例方法也就是锁住了当前这个对象，不允许当前这个对象再去访问其他的同步方法，所以结果
  就很显然易见了，因为我们在启动第二个线程的时候中间休眠了三秒所以线程A会先拿到phone1对象的锁然后线程B去访问另一个
  同步方法就被拒之门外了，必须要等待线程A执行完毕才能进入另一个同步方法，所以就出现了先打电话后发短信的现象。
  当然若类中有没有被synchronized修饰的方法那么就不会收到synchronized锁的影响，能够正常调用！

  ```java
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
  ```
* 八锁问题，其二：被synchronized修饰的实例方法通过两个对象进行调用会发生什么？

  显然我们在执行完毕之后得出了结果，是先打电话在去发短信，为什么捏？因为我们创建了两个对象并且synchronized锁
  锁的是对象也就是对于同一个对象同时只能进入一个同步方法，但是对于另外一个对象则不受影响，这样这连个对象是不同的
  锁互不干扰

  ```java
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
  ```
* 八锁问题，其三：被synchronized修饰的静态方法与非静态方法被同一个对象调用会有什么效果？\

  我们运行之后得到的结果可以看出，也是先打出电话才发送的短信，根据之前的两个案例我们可以得知显然这静态方法与非静态方法 也不是同一把锁，调用发短信方法并且获得到锁之后并没有影响到获取静态方法的锁，所以通过这个例子我们可以知道普通方法的锁 和静态方法中的锁不是同一把，所以在获取到其中一种方法的锁之后还允许获取另外一把锁。

  ```java
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
  ```
* 八锁问题，其四 若两个方法均为静态方法则然后在进行下面的调用会发生什么？

  这次我们则发现结果反而和八锁问题其一一样了，就是先输出发短信然后在是打电话，现在细心的读者可能就已经类比出来了 因为我们为两个方法都加上了static进行修饰也就是说这两个方法的锁都是static锁都是类锁，是锁住了整个类的静态方法 所以我们使用同一个对象去调用这两个静态方法则会出现先拿到锁的发短信执行然后再是后拿到锁的打电话执行，那么如果是 两个对象去分别调用这两个方法呢？请看终章！八锁其五

  ```java
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
  ```
* 八锁问题，其五 若两个方法均为静态方法然后在通过两个对象进行调用呢？

  发短信！又是他！这次又是发短信先执行，这样我们就很明朗了，因为我们已经知道了锁住静态方法的锁是锁整个类，因为静态 方法是随着类加载而被加载的甚至可以通过类名去调用它所以不管我们有多少个对象都没什么用本质上还是相当于类名.方法名的 调用，所以打电话必须等发短信执行完毕释放锁才能继续执行！

  ```java
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
  ```

4.2 八锁问题总结：

其实讲到这里我们已经把所有的情况已经列举完毕了，总的概括来说在java中一共有两种锁。 第一种对象锁：这种锁会锁住整个对象，不允许当前这个对象再去进入其他的同步非静态方法(也就是被synchronized修饰的其他 非静态方法)，但是允许当前对象去访问非同步方法，这点毋庸置疑因为非同步方法并不受同步的限制。并且允许当前对象去访问同步 的静态方法，在这说是这个对象去访问静态方法不如说是类去访问静态方法，因为静态方法的锁和非静态方法的锁不是一把静态方法 的锁是锁住整个对象，所以这两者也不互相干扰！ 第二种类锁：这种锁会锁住整个类，就算我们创建若干个对象去分别访静态同步方法都是不允许的，因为这个锁锁住了整个类不允许 其他人再去访问同样拥有类锁的同步方法也就是被synchronized修饰的静态方法！但是对于普通方法和非静态的同步方法则不受影 响，因为这两位其中一个是对象锁与类锁互不干扰，另一位压根就没锁更不受影响！还有一点需要注意的是synchronized锁代码 块的时候使用的是类锁！
