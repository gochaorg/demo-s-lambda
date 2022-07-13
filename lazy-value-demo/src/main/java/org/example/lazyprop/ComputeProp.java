package org.example.lazyprop;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;

public abstract class ComputeProp<T> extends PropBase implements Prop<T> {
    protected abstract T compute();

    protected ComputeProp<T> addDependencies( Prop<?> ... props ){
        for( var prop : props ){
            prop.listen(dependenciesListener);
        }
        return this;
    }
    protected ComputeProp<T> addDependencies( Iterable<Prop<?>> props ){
        for( var prop : props ){
            prop.listen(dependenciesListener);
        }
        return this;
    }

    protected PropListener dependenciesListener = new PropListener() {
        @Override
        public void propEvent(PropEvent ev) {
            if( ev instanceof PropChanged ){
                dropComputedValue();
            }
        }
    };

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected Optional<T> computedValue = Optional.empty();

    protected void dropComputedValue(){
        computedValue = Optional.empty();
        fire(PropChanged.create(this));
    }

    @Override
    public T get() {
        var value = computedValue;
        if( value.isEmpty() ){
            var res = compute();
            computedValue = Optional.of(res);
            return res;
        }else{
            return value.get();
        }
    }

    public static <A,Z> ComputeProp<Z> eval( Prop<A> pa, F1<A,Z> compute ){
        return new ComputeProp<Z>() {
            @Override
            protected Z compute() {
                return compute.apply(pa.get());
            }
        }.addDependencies( pa );
    }

    public static <A,B,Z> ComputeProp<Z> eval( Prop<A> a, Prop<B> b, F2<A,B,Z> compute ){
        return new ComputeProp<Z>() {
            @Override
            protected Z compute() {
                return compute.apply(a.get(), b.get());
            }
        }.addDependencies( a,b );
    }

    public static <A,B,C,Z> ComputeProp<Z> eval( Prop<A> a, Prop<B> b, Prop<C> c, F3<A,B,C,Z> compute ){
        return new ComputeProp<Z>() {
            @Override
            protected Z compute() {
                return compute.apply(a.get(), b.get(), c.get());
            }
        }.addDependencies( a,b,c );
    }

    public static <A,B,C,E,Z> ComputeProp<Z> eval( Prop<A> a, Prop<B> b, Prop<C> c, Prop<E> e, F4<A,B,C,E,Z> compute ){
        return new ComputeProp<Z>() {
            @Override
            protected Z compute() {
                return compute.apply(a.get(), b.get(), c.get(), e.get());
            }
        }.addDependencies( a,b,c,e );
    }

    public static <A,B,C,D,E,Z> ComputeProp<Z> eval( Prop<A> a, Prop<B> b, Prop<C> c, Prop<D> d, Prop<E> e,
                                                     F5<A,B,C,D,E,Z> compute ){
        return new ComputeProp<Z>() {
            @Override
            protected Z compute() {
                return compute.apply(a.get(), b.get(), c.get(), d.get(), e.get());
            }
        }.addDependencies( a,b,c,d,e );
    }

    public static <Z> ComputeProp<Z> eval( Compute<Z> compute, F1<ComputeProp<Z>,ComputeProp<Z>> wrapper  ){
        if( compute==null )throw new IllegalArgumentException( "compute==null" );
        try {
            var meth = compute.getClass().getDeclaredMethod("writeReplace");
            meth.setAccessible(true);
            var sl = (SerializedLambda)meth.invoke(compute);
            var depProps = new HashSet<Prop<?>>();
            for( var ai=0; ai< sl.getCapturedArgCount(); ai++ ){
                var arg = sl.getCapturedArg(ai);
                if( arg instanceof Prop ){
                    depProps.add((Prop<?>) arg);
                }
            }
            var computedProp = new ComputeProp<Z>(){
                @Override
                protected Z compute() {
                    return compute.get();
                }
            };
            computedProp.addDependencies(depProps);
            if( wrapper!=null ){
                return wrapper.apply(computedProp);
            }
            return computedProp;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <Z> ComputeProp<Z> eval( Compute<Z> compute ){
        if( compute==null )throw new IllegalArgumentException( "compute==null" );
        return eval( compute, null );
    }
}
