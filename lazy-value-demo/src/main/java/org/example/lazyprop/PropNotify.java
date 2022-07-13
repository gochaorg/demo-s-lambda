package org.example.lazyprop;

public interface PropNotify {
    public AutoCloseable listen( PropListener listener );
}
