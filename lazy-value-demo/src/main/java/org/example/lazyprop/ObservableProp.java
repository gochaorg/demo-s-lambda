package org.example.lazyprop;

public class ObservableProp<T> {
    public final Observer<T> observer;

    public ObservableProp(Observer<T> observer){
        if( observer==null )throw new IllegalArgumentException( "observer==null" );
        this.observer = observer;
    }

    public static class ReadProp<T> extends ObservableProp<T> implements Prop<T>, Wrapper {
        public final Prop<T> source;

        @Override
        public Object source() {
            return source;
        }

        public ReadProp(Observer<T> observer, Prop<T> source ){
            super(observer);
            if( source==null )throw new IllegalArgumentException( "source==null" );
            this.source = source;
        }

        public T get() {
            observer.beginRead(source);
            var r = source.get();
            observer.endRead(source,r);
            return r;
        }

        public AutoCloseable listen(PropListener listener) {
            return source.listen(listener);
        }
    }

    public static <T> ReadProp<T> create(Observer<T> observer, Prop<T> prop){
        if( observer==null )throw new IllegalArgumentException( "observer==null" );
        if( prop==null )throw new IllegalArgumentException( "prop==null" );
        return new ReadProp<>(observer, prop);
    }

    public static class RWProp<T> extends ObservableProp<T> implements PropWrite<T>, Wrapper {
        public final PropWrite<T> source;

        @Override
        public Object source() {
            return source;
        }

        public RWProp(Observer<T> observer, PropWrite<T> source ){
            super(observer);
            if( source==null )throw new IllegalArgumentException( "source==null" );
            this.source = source;
        }

        public T get() {
            observer.beginRead(source);
            var r = source.get();
            observer.endRead(source,r);
            return r;
        }

        @Override
        public void set(T value) {
            observer.beginWrite(source, value);
            source.set(value);
            observer.endWrite(source, value);
        }

        public AutoCloseable listen(PropListener listener) {
            return source.listen(listener);
        }
    }
    public static <T> RWProp<T> create(Observer<T> observer, PropWrite<T> prop){
        if( observer==null )throw new IllegalArgumentException( "observer==null" );
        if( prop==null )throw new IllegalArgumentException( "prop==null" );
        return new RWProp<>(observer, prop);
    }
}
