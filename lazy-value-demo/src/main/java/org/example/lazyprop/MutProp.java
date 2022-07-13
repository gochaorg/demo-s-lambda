package org.example.lazyprop;

public class MutProp<T> extends PropBase implements Prop<T>, PropWrite<T> {
    private T value;

    public MutProp(T initial){
        this.value = initial;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
        fire(PropChanged.create(this));
    }
}
