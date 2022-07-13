package org.example.lazyprop;

public interface PropChanged extends PropEvent {
    public static PropChanged create(Object source){
        return new PropChanged() {
            @Override
            public Object source() {
                return source;
            }
        };
    }
}
