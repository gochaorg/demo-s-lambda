package org.example.client;

import org.example.api.OSProcess;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SnapshotDelta {
    public final List<OSProcess> newProcesses;
    public final List<OSProcess> oldProcesses;
    public final List<OSProcessDelta> deltaProcesses;
    public final Map<Long,OSProcessDelta> deltaMap;

    public SnapshotDelta(
        List<OSProcess> newProcesses,
        List<OSProcess> oldProcesses,
        List<OSProcessDelta> deltaProcesses
    ){
        if( newProcesses==null )throw new IllegalArgumentException("newProcesses==null");
        if( oldProcesses==null )throw new IllegalArgumentException("oldProcesses==null");
        if( deltaProcesses==null )throw new IllegalArgumentException("deltaProcesses==null");

        this.deltaProcesses = deltaProcesses;
        this.newProcesses = newProcesses;
        this.oldProcesses = oldProcesses;

        deltaMap = new HashMap<>();
        deltaProcesses.forEach( d -> {
            deltaMap.put(d.getPid(), d);
        });
    }

    public static SnapshotDelta create( Snapshot from, Snapshot to ){
        if( from==null )throw new IllegalArgumentException("from==null");
        if( to==null )throw new IllegalArgumentException("to==null");

        List<OSProcess> newProcesses = new ArrayList<>();
        List<OSProcess> oldProcesses = new ArrayList<>();
        Map<Long,OSProcessDelta> deltas = new HashMap<>();
        from.processes.forEach( fProc -> {
            to.find(fProc.getPid()).ifPresentOrElse( tProc -> {
                deltas.put(fProc.getPid(), OSProcessDelta.create(fProc, tProc));
            }, ()->{
                oldProcesses.add(fProc);
            });
        });
        to.processes.stream().filter( tProc -> !deltas.containsKey(tProc.getPid()) ).forEach(newProcesses::add);

        return new SnapshotDelta(newProcesses, oldProcesses, new ArrayList<>(deltas.values()));
    }

    public Optional<Long> getUpTimeMax() {
        if( deltaProcesses.isEmpty() )return Optional.empty();
        return deltaProcesses.stream().map(OSProcessDelta::getUpTime).max(Long::compareTo);
    }
    public Optional<Long> getUserTimeSummary() {
        return deltaProcesses.stream().map(OSProcessDelta::getUserTime).reduce(Long::sum);
    }
    public Optional<Long> getKernelTimeSummary() {
        return deltaProcesses.stream().map(OSProcessDelta::getKernelTime).reduce(Long::sum);
    }

    public Stream<OSProcessDelta> topUserTime(){
        return deltaProcesses.stream()
            .sorted( (a,b) -> Long.compare(b.getUserTime(), a.getUserTime()) )
            ;
    }
}
