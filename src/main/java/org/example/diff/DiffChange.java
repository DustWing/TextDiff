package org.example.diff;

public record DiffChange<T>(int leftLine, int rightLine, DiffType type, T newValue, T oldValue) {

}
