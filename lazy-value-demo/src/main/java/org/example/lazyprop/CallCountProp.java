package org.example.lazyprop;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CallCountProp<T,SELF extends CallCountProp<T,SELF>> {
    //region readCallCount
    private int readCallCount = 0;
    public int getReadCallCount() {
        return readCallCount;
    }
    private final List<Consumer<T>> readingListeners = new ArrayList<>();
    public SELF onReading(Consumer<T> ls){
        if( ls==null )throw new IllegalArgumentException( "ls==null" );
        readingListeners.add(ls);
        //noinspection unchecked
        return (SELF) this;
    }
    protected T reading(T value){
        readCallCount++;
        readingListeners.forEach(ls -> ls.accept(value));
        return value;
    }
    //endregion
    //region writeCallCount
    private int writeCallCount = 0;
    public int getWriteCallCount() {
        return writeCallCount;
    }
    private final List<Consumer<T>> writingListeners = new ArrayList<>();
    public SELF onWriting(Consumer<T> ls){
        if( ls==null )throw new IllegalArgumentException( "ls==null" );
        writingListeners.add(ls);
        //noinspection unchecked
        return (SELF) this;
    }
    protected T writing(T value) {
        writeCallCount++;
        writingListeners.forEach(ls -> ls.accept(value));
        return value;
    }
    //endregion

    public static class ReadCounter<T> extends CallCountProp<T,ReadCounter<T>> implements Prop<T>, Wrapper {
        public final Prop<T> source;

        @Override
        public Object source() {
            return source;
        }

        public ReadCounter(Prop<T> source){
            if( source==null )throw new IllegalArgumentException( "source==null" );
            this.source = source;
        }

        @Override
        public T get() {
            return reading(source.get());
        }

        @Override
        public AutoCloseable listen(PropListener listener) {
            return source.listen(listener);
        }

    }
    public static class RWCounter<T> extends CallCountProp<T,RWCounter<T>> implements PropWrite<T>, Wrapper {
        public final PropWrite<T> source;

        @Override
        public Object source() {
            return source;
        }

        public RWCounter(PropWrite<T> source){
            this.source = source;
        }

        @Override
        public T get() {
            return reading(source.get());
        }

        @Override
        public AutoCloseable listen(PropListener listener) {
            return source.listen(listener);
        }

        @Override
        public void set(T value) {
            source.set(writing(value));
        }
    }

    public static <T> ReadCounter<T> readCounter( Prop<T> prop ){
        if( prop==null )throw new IllegalArgumentException( "prop==null" );
        return new ReadCounter<>(prop);
    }
    public static <T> ReadCounter<T> of(Prop<T> prop){
        return readCounter(prop);
    }

    public static <T> RWCounter<T> rwCounter( PropWrite<T> prop ){
        if( prop==null )throw new IllegalArgumentException( "prop==null" );
        return new RWCounter<>(prop);
    }
    public static <T> RWCounter<T> of(PropWrite<T> prop){
        return rwCounter(prop);
    }

}
