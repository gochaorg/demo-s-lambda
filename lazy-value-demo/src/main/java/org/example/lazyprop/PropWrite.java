package org.example.lazyprop;

public interface PropWrite<T> extends Prop<T> {
    public void set(T value);

    @Override
    default PropWrite<T> named(String name) {
        if( name==null )throw new IllegalArgumentException( "name==null" );
        return NamedProp.create(name, this);
    }

    @Override
    default PropWrite<T> observable(Observer<T> observer) {
        if( observer==null )throw new IllegalArgumentException( "observer==null" );
        return ObservableProp.create(observer, this);
    }
}
