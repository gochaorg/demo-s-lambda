package org.example.api.oshi;

import org.example.api.OSProcess;
import org.example.api.ProcState;
import org.example.api.spi.OSInfoProvider;
import oshi.software.os.OperatingSystem;
import oshi.software.os.linux.LinuxOperatingSystem;
import oshi.software.os.mac.MacOperatingSystem;
import oshi.software.os.windows.WindowsOperatingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OSHIProvider implements OSInfoProvider {
    private volatile OperatingSystem os;
    private volatile boolean osInitilized;
    private Optional<OperatingSystem> os(){
        if( osInitilized ){
            return os!=null ? Optional.of(os) : Optional.empty();
        }else{
            synchronized (this){
                if( osInitilized )return os!=null ? Optional.of(os) : Optional.empty();
                osInitilized = true;
                os = osInitilize();
                return os!=null ? Optional.of(os) : Optional.empty();
            }
        }
    }

    private OperatingSystem osInitilize(){
        var osName = System.getProperties().getProperty("os.name", "undef");
        if( osName.contains("Mac") ){
            return new MacOperatingSystem();
        }else if( osName.toLowerCase().contains("linux") ){
            return new LinuxOperatingSystem();
        }else if( osName.toLowerCase().contains("windows") ){
            return new WindowsOperatingSystem();
        }else{
            return null;
        }
    }

    @Override
    public boolean available() {
        return os().isPresent();
    }

    private OSProcess map( oshi.software.os.OSProcess proc ) {
        var p = new OSProcess();
        p.setName(proc.getName());
        p.setPath(proc.getPath());
        p.setCurrentWorkingDirectory(proc.getCurrentWorkingDirectory());
        p.setUser(proc.getUser());
        p.setUserID(proc.getUserID());
        p.setGroup(proc.getGroup());
        p.setGroupID(proc.getGroupID());
        p.setThreadCount(proc.getThreadCount());
        p.setPriority(proc.getPriority());
        p.setVirtualSize(proc.getVirtualSize());
        p.setResidentSetSize(proc.getResidentSetSize());
        p.setKernelTime(proc.getKernelTime());
        p.setUpTime(proc.getUpTime());
        p.setUserTime(proc.getUserTime());
        p.setStartTime(proc.getStartTime());
        p.setBytesRead(proc.getBytesRead());
        p.setBytesWritten(proc.getBytesWritten());
        p.setOpenFiles(proc.getOpenFiles());
        p.setContextSwitches(proc.getContextSwitches());
        p.setPid(proc.getProcessID());
        p.setPpid(proc.getParentProcessID());
        p.setCommandLine(proc.getCommandLine());

        switch (proc.getState()){
            case NEW:       p.setState(ProcState.NEW); break;
            case OTHER:     p.setState(ProcState.OTHER); break;
            case ZOMBIE:    p.setState(ProcState.ZOMBIE); break;
            case INVALID:   p.setState(ProcState.INVALID); break;
            case RUNNING:   p.setState(ProcState.RUNNING); break;
            case STOPPED:   p.setState(ProcState.STOPPED); break;
            case WAITING:   p.setState(ProcState.WAITING); break;
            case SLEEPING:  p.setState(ProcState.SLEEPING); break;
            case SUSPENDED: p.setState(ProcState.SUSPENDED); break;
        }

        return p;
    }

    @Override
    public List<OSProcess> processes() {
        return os().map( os ->
            os.getProcesses().stream().map(this::map)
                .collect(Collectors.toList()))
            .orElse(new ArrayList<>());
    }
}
