package sample;

import java.util.function.BiFunction;

public class Sample3 {
    public static int square(int num,int a){
        return num * a;
    }

    public static void main(String[] args){
        square(10,11);

        // Внешняя переменаня
        BiFunction<Integer,Integer,Integer> source = Sample3::square;

        // Внешняя переменаня
        String name = "square";
        BiFunction<Integer,Integer,Integer> f_ptr = (a,b) -> {
            System.out.println( name+" "+a+", "+b ); // использование захваченной переменной name
            return source.apply(a,b); // использование захваченной переменной source
        };
        f_ptr.apply(12,14);
    }
}
