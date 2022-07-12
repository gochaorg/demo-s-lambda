package org.example.api;

import java.io.Serializable;

public class OSProcess implements Serializable {
    //region name
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    //endregion
    //region path
    private String path;
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    //endregion
    //region currentWorkingDirectory
    private String currentWorkingDirectory;
    public String getCurrentWorkingDirectory() {
        return currentWorkingDirectory;
    }
    public void setCurrentWorkingDirectory(String currentWorkingDirectory) {
        this.currentWorkingDirectory = currentWorkingDirectory;
    }
    //endregion
    //region user
    private String user;
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    //endregion
    //region userID
    private String userID;
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    //endregion
    //region group
    private String group;
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    //endregion
    //region groupID
    private String groupID;
    public String getGroupID() {
        return groupID;
    }
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    //endregion

    //region threadCount
    private int threadCount;
    public int getThreadCount() {
        return threadCount;
    }
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
    //endregion
    //region priority
    private int priority;
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }
    //endregion
    //region virtualSize
    private long virtualSize;
    public long getVirtualSize() {
        return virtualSize;
    }
    public void setVirtualSize(long virtualSize) {
        this.virtualSize = virtualSize;
    }
    //endregion
    //region residentSetSize
    private long residentSetSize;
    public long getResidentSetSize() {
        return residentSetSize;
    }
    public void setResidentSetSize(long residentSetSize) {
        this.residentSetSize = residentSetSize;
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
    //region startTime
    private long startTime;
    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
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
    //region bytesWritten
    private long bytesWritten;
    public long getBytesWritten() {
        return bytesWritten;
    }
    public void setBytesWritten(long bytesWritten) {
        this.bytesWritten = bytesWritten;
    }
    //endregion
    //region openFiles
    private long openFiles;
    public long getOpenFiles() {
        return openFiles;
    }
    public void setOpenFiles(long openFiles) {
        this.openFiles = openFiles;
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

    //region pid
    private long pid;
    public long getPid() {
        return pid;
    }
    public void setPid(long pid) {
        this.pid = pid;
    }
    //endregion
    //region ppid
    private long ppid;

    public long getPpid() {
        return ppid;
    }
    public void setPpid(long ppid) {
        this.ppid = ppid;
    }
    //endregion
    //region commandLine
    private String commandLine;
    public String getCommandLine() {
        return commandLine;
    }
    public void setCommandLine(String commandLine) {
        this.commandLine = commandLine;
    }
    //endregion

    //region state
    private ProcState state;
    public ProcState getState() {
        return state;
    }
    public void setState(ProcState state) {
        this.state = state;
    }
    //endregion
}
