package org.example.api.oshi;

import org.example.api.OSInfo;
import org.junit.jupiter.api.Test;

public class OSInfoTest {
    @Test
    public void procList(){
        new OSInfo().processes().forEach( proc -> {
            System.out.println("proc pid="+proc.getPid()
                +" ppid="+proc.getPpid()
                +" cmd="+proc.getCommandLine());
        });
    }
}
