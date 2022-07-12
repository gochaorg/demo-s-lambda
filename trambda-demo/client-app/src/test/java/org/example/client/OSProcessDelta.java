package org.example.client;

import org.example.api.OSProcess;

public class OSProcessDelta {
    //region pid : long
    private long pid;
    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
    //endregion
    //region userTime
    private long userTime;
    public long getUserTime() {
        return userTime;
    }

    public void setUserTime(long userTime) {
        this.userTime = userTime;
    }
    //endregion
    //region kernelTime
    private long kernelTime;
    public long getKernelTime() {
        return kernelTime;
    }

    public void setKernelTime(long kernelTime) {
        this.kernelTime = kernelTime;
    }
    //endregion
    //region upTime
    private long upTime;
    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }
    //endregion
    //region contextSwitches
    private long contextSwitches;
    public long getContextSwitches() {
        return contextSwitches;
    }

    public void setContextSwitches(long contextSwitches) {
        this.contextSwitches = contextSwitches;
    }
    //endregion
    //region bytesWritten
    private long bytesWritten;
    public long getBytesWritten() {
        return bytesWritten;
    }

    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }
    //endregion
    //region bytesRead
    private long bytesRead;

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }
    //endregion

    public static OSProcessDelta create(OSProcess from, OSProcess to){
        if( from==null )throw new IllegalArgumentException( "from==null" );
        if( to==null )throw new IllegalArgumentException( "to==null" );
        if( from.getPid()!=to.getPid() )throw new IllegalArgumentException( "from.getPid()!=to.getPid()" );

        OSProcessDelta d = new OSProcessDelta();
        d.setPid(from.getPid());
        d.setUserTime(to.getUserTime() - from.getUserTime());
        d.setKernelTime(to.getKernelTime() - from.getKernelTime());
        d.setUpTime(to.getUpTime() - from.getUpTime());
        d.setContextSwitches(to.getContextSwitches() - from.getContextSwitches());
        d.setBytesWritten(to.getBytesWritten() - from.getBytesWritten());
        d.setBytesRead(to.getBytesRead() - from.getBytesRead());
        return d;
    }
}
