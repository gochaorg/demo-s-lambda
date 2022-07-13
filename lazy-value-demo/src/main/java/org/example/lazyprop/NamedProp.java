package org.example.lazyprop;

import java.util.Optional;

public abstract class NamedProp {
    public final String name;
    public NamedProp(String name){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        this.name = name;
    }

    public static class ReadProp<T> extends NamedProp implements Prop<T>, Wrapper {
        public final Prop<T> source;

        @Override
        public Object source() {
            return source;
        }

        public ReadProp(String name, Prop<T> source ){
            super(name);
            if( source==null )throw new IllegalArgumentException( "source==null" );
            this.source = source;
        }

        public T get() {
            return source.get();
        }

        public AutoCloseable listen(PropListener listener) {
            return source.listen(listener);
        }
    }
    public static <T> ReadProp<T> create( String name, Prop<T> prop){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        if( prop==null )throw new IllegalArgumentException( "prop==null" );
        return new ReadProp<>(name, prop);
    }

    public static class RWProp<T> extends NamedProp implements PropWrite<T>, Wrapper {
        public final PropWrite<T> source;

        @Override
        public Object source() {
            return source;
        }

        public RWProp(String name, PropWrite<T> source ){
            super(name);
            if( source==null )throw new IllegalArgumentException( "source==null" );
            this.source = source;
        }

        public T get() {
            return source.get();
        }

        @Override
        public void set(T value) {
            source.set(value);
        }

        public AutoCloseable listen(PropListener listener) {
            return source.listen(listener);
        }
    }
    public static <T> RWProp<T> create( String name, PropWrite<T> prop){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        if( prop==null )throw new IllegalArgumentException( "prop==null" );
        return new RWProp<>(name, prop);
    }

    public static Optional<String> nameOf( Object obj ){
        if( obj==null )return Optional.empty();
        if( obj instanceof NamedProp )return Optional.of(((NamedProp) obj).name);
        if( obj instanceof Wrapper ){
            return
                ((Wrapper) obj).unwrap().toStream()
                    .filter( o -> o instanceof NamedProp )
                    .map( o -> (NamedProp)o )
                    .findFirst()
                    .map( named -> named.name );
        }else{
            return Optional.empty();
        }
    }
}
