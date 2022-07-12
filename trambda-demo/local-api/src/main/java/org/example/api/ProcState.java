package org.example.api;

public enum ProcState {
    /**
     * Intermediate state in process creation
     */
    NEW,
    /**
     * Actively executing process
     */
    RUNNING,
    /**
     * Interruptible sleep state
     */
    SLEEPING,
    /**
     * Blocked, uninterruptible sleep state
     */
    WAITING,
    /**
     * Intermediate state in process termination
     */
    ZOMBIE,
    /**
     * Stopped by the user, such as for debugging
     */
    STOPPED,
    /**
     * Other or unknown states not defined
     */
    OTHER,
    /**
     * The state resulting if the process fails to update statistics, probably due
     * to termination.
     */
    INVALID,
    /**
     * Special case of waiting if the process has been intentionally suspended
     * (Windows only)
     */
    SUSPENDED
}
