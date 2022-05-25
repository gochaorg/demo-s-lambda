package sample;

import java.util.function.BiFunction;

public class Sample3 {
    public static int square(int num,int a){
        return num * a;
    }

    public static void main(String[] args){
        square(10,11);

        BiFunction<Integer,Integer,Integer> source = Sample3::square;
        String name = "square";
        BiFunction<Integer,Integer,Integer> f_ptr = (a,b) -> {
            System.out.println( name+" "+a+", "+b );
            return source.apply(a,b);
        };
        f_ptr.apply(12,14);
    }
}
