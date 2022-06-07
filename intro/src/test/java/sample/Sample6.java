package sample;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

public class Sample6 {
    public static int square(int num,int a){
        return num * a;
    }

    public static void main(String[] args){
        square(10,11);

        BiFunction<Integer,Integer,Integer> source = (BiFunction<Integer,Integer,Integer> & Serializable)Sample6::square;

        String name = "square";
        BiFunction<Integer,Integer,Integer> f_ptr = (BiFunction<Integer,Integer,Integer> & Serializable)((a,b) -> {
            System.out.println( name+" "+a+", "+b );
            return source.apply(a,b);
        });
        f_ptr.apply(12,14);

        System.out.println(f_ptr.getClass());

        /////////////////////////////
        System.out.println(f_ptr instanceof Serializable);
        try {
            var meth = f_ptr.getClass().getDeclaredMethod("writeReplace");
            meth.setAccessible(true);
            var sl = (SerializedLambda) meth.invoke(f_ptr);
            System.out.println("impl kind "+sl.getImplMethodKind());
            System.out.println("impl class "+sl.getImplClass());
            System.out.println("impl meth "+sl.getImplMethodName());
            System.out.println("impl m.sign "+sl.getImplMethodSignature());
            System.out.println("capture:");
            for( var ci=0; ci<sl.getCapturedArgCount(); ci++ ){
                var arg = sl.getCapturedArg(ci);
                System.out.println("  ["+ci+"] value=\""+arg+"\""+(arg!=null ? " "+arg.getClass() : ""));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
