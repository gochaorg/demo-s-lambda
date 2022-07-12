package org.example.client;

import org.example.api.OSProcess;
import xyz.cofe.fn.Tuple;
import xyz.cofe.fn.Tuple2;
import xyz.cofe.iter.Eterable;

import java.time.Instant;
import java.util.*;

public class Snapshot {
    public final Map<Long,OSProcess> processMap = new HashMap<>();
    public final List<OSProcess> processes;
    public final Instant time;

    public Snapshot(Instant time,List<OSProcess> processes){
        if(time==null)throw new IllegalArgumentException("time==null");
        if(processes==null)throw new IllegalArgumentException("processes==null");
        this.processes = processes;
        this.time = time;

        processes.forEach(proc -> {
            processMap.put(proc.getPid(), proc);
        });
    }

    public Optional<OSProcess> find( long pid ){
        var proc = processMap.get(pid);
        return proc!=null ? Optional.of(proc) : Optional.empty();
    }

    public Eterable<Tuple2<OSProcess, Optional<OSProcessDelta>>> zip( SnapshotDelta delta ){
        if( delta==null )throw new IllegalArgumentException( "delta==null" );
        return Eterable.of(processes).map( proc -> {
            var d = delta.deltaMap.get(proc.getPid());
            return Tuple2.of( proc, d!=null ? Optional.of(d) : Optional.empty());
        });
    }
}
