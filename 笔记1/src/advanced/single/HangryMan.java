package advanced.single;
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
