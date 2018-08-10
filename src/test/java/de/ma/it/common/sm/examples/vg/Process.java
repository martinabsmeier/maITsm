package de.ma.it.common.sm.examples.vg;

public interface Process {

    void create();

    void release(Integer processNumber);

    void cancel(Integer processNumber);
}
