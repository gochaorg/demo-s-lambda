package sample;

import java.net.URL;
import java.util.function.BiFunction;

public class Sample4 {
    public static int square(int num,int a){
        return num * a;
    }

    public static class LogCall implements BiFunction<Integer,Integer,Integer> {
        public final BiFunction<Integer,Integer,Integer> source;
        public final String name;
        public LogCall(String name,BiFunction<Integer,Integer,Integer> source){
            this.source = source;
            this.name = name;
        }

        @Override
        public Integer apply(Integer integer, Integer integer2) {
            System.out.println(name+" "+integer+", "+integer2);
            return source.apply(integer, integer2);
        }
    }

    public static void main(String[] args){
        BiFunction<Integer,Integer,Integer> f_ptr = new LogCall("square", Sample4::square);
        f_ptr.apply(12,14);
        System.out.println(f_ptr.getClass());
        URL url = LogCall.class.getResource("/"+LogCall.class.getName().replace(".","/")+".class");
        System.out.println(url);
    }
}
