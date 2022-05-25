package sample;

import java.util.function.BiFunction;

public class Sample2 {
    public static int square(int num,int a){
        return num * a;
    }

    public static class LogCall implements BiFunction<Integer,Integer,Integer> {
        // захваченная переменная
        public final BiFunction<Integer,Integer,Integer> source;

        // захваченная переменная
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
        square(10,11);
        // "завхват переменных"
        BiFunction<Integer,Integer,Integer> f_ptr = new LogCall("square",Sample2::square);
        f_ptr.apply(12,14);
    }
}
