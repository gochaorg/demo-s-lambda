package org.example.lazyprop;

public interface Prop<T> extends PropNotify {
    public T get();
    public default Prop<T> named(String name){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        return NamedProp.create(name, this);
    }
    public default Prop<T> observable(Observer<T> observer){
        if( observer==null )throw new IllegalArgumentException( "observer==null" );
        return ObservableProp.create(observer, this);
    }
}
