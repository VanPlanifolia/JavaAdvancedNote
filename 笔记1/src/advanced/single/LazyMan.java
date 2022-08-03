package advanced.single;

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
