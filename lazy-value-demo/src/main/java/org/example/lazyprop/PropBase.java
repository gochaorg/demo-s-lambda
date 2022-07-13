package org.example.lazyprop;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;

public abstract class PropBase implements PropNotify {
    private Collection<PropListener> listeners = new HashSet<>();

    public void removeListener(PropListener listener) {
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        listeners.remove(listener);
    }

    public void addListener(PropListener listener) {
        if( listener==null )throw new IllegalArgumentException( "listener==null" );
        listeners.add(listener);
    }

    protected void fire(PropEvent event){
        for( var ls : listeners ){
            ls.propEvent(event);
        }
    }

    @Override
    public AutoCloseable listen(PropListener propListener) {
        if( propListener ==null )throw new IllegalArgumentException( "listener==null" );
        addListener(propListener);
        var wr = new WeakReference<PropListener>(propListener);
        return () -> {
            var ref = wr.get();
            if( ref!=null ){
                removeListener(ref);
            }
            wr.clear();
        };
    }
}
