package org.example.api;

import org.example.api.spi.OSInfoProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public class OSInfo {
    private volatile List<OSInfoProvider> providers;
    private List<OSInfoProvider> providers(){
        if( providers!=null )return providers;
        synchronized (this) {
            if( providers!=null )return providers;
            List<OSInfoProvider> lst = new ArrayList<>();
            for( var provider: ServiceLoader.load(OSInfoProvider.class) ){
                lst.add(provider);
            }
            lst = Collections.unmodifiableList(lst);
            return lst;
        }
    }
    public List<OSProcess> processes() {
        var procList = new ArrayList<OSProcess>();
        for( var provider : providers() ){
            if( provider.available() ){
                procList.addAll(provider.processes());
            }
        }
        return procList;
    }
}
