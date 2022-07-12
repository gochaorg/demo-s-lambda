package org.example.client;

import org.example.api.OSInfo;
import org.example.api.OSProcess;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.time.Instant;

public class SnapshotTest {
    @Test
    public void test01(){
        var osInfo = new OSInfo();

        long tStart = System.currentTimeMillis();
        long timeout = 30*1000;
        Snapshot snapshot = null;
        while ( (System.currentTimeMillis()-tStart)<timeout ){
            Snapshot snapshot1 = new Snapshot(Instant.now(), osInfo.processes());
            if( snapshot==null ){
                snapshot = snapshot1;
            }else{
                var pctFmt = new xyz.cofe.text.FullDecFormat("###.##");
                var pidFmt = new xyz.cofe.text.FullDecFormat("#######");
                var sdelta = SnapshotDelta.create(snapshot, snapshot1);
                var upTimeMax_ = sdelta.getUpTimeMax();
                var userTimeSummary_ = sdelta.getUserTimeSummary();

                System.out.println();
                System.out.println("upTimeMax.d "+upTimeMax_+" userTime.d "+userTimeSummary_);
                upTimeMax_.ifPresent( upTimeMax ->
                    userTimeSummary_.ifPresent( userTimeSummary -> {
                        sdelta.topUserTime().limit(10).forEach( proc -> {
                            var pct = 100.0 * proc.getUserTime() / userTimeSummary;
                            System.out.println(
                                pctFmt.format(pct)+"% "+
                                    pidFmt.format(proc.getPid())+" "+
                                    snapshot1.find(proc.getPid()).map(OSProcess::getName).orElse("")
                            );
                        });
                    })
                );
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
