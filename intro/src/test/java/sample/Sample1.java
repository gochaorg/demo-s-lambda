package sample;

import java.util.function.BiFunction;

public class Sample1 {
    public static int square(int num,int a){
        return num * a;
    }

    public static void main(String[] args){
        square(10,11);
        BiFunction<Integer,Integer,Integer> f_ptr = Sample1::square;
        f_ptr.apply(12,14);
    }
}

