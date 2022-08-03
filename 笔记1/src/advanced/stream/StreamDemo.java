package advanced.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Intellij IDEA
 * 本类用于测试jdk8新特性 Stream
 * @author Planifolia.Van
 * @Date 2022/8/3 17:23
 * @Version 1.0
 */
public class StreamDemo {
    public static void main(String[] args) {
        //创建一个list用于生产流
        List<Integer> integerList=new ArrayList<>();
        integerList.add(10);
        integerList.add(2);
        integerList.add(3);
        integerList.add(7);
        integerList.add(9);
        integerList.add(3);
        integerList.add(3);
        integerList.add(4);
        integerList.add(2);
        String[] strings={"g","b","d","c","e","f","a","c","c"};
        //stream的特性。由数据源生成的stream只能使用一次，如果使用第二次就会报
        //stream has already been operated upon or closed 错误！

        //使用forEach()去便利stream内容，这种便利方法比原版的for 要方便多
        System.out.println("-----------------正序便利-----------------");
        integerList.forEach(s-> System.out.print(" "+s));
        System.out.println();
        Arrays.stream(strings).forEach(s-> System.out.print(" "+s));
        System.out.println();

        //使用sorted()去排序stream然后再便利输出,sorted不仅仅能排序数字还能排序字符
        System.out.println("-----------------排序后便利-----------------");
        integerList.stream().sorted().forEach(s-> System.out.print(" "+s));
        System.out.println();
        Arrays.stream(strings).sorted().forEach(s -> System.out.print(" "+s));
        System.out.println();

        //使用filter()去快速检索出内容
        System.out.println("-----------------检索信息-----------------");
        integerList.stream().filter(s-> s>3).forEach(t-> System.out.print(" "+t));
        long count = Arrays.stream(strings).filter(s-> s.equals("c")).count();
        System.out.println();
        System.out.println("在strings中 c一共出现了"+count+"次");
    }
}
