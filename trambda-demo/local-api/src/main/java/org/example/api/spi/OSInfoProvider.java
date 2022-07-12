package org.example.api.spi;

import org.example.api.OSProcess;

import java.util.List;

public interface OSInfoProvider {
    boolean available();
    List<OSProcess> processes();
}
