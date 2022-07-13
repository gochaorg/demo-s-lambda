package org.example.lazyprop;

import java.io.Serializable;
import java.util.function.Supplier;

public interface Compute<T> extends Supplier<T>, Serializable {
}
