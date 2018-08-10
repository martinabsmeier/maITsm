package de.ma.it.common.sm.examples.vg;

import de.ma.it.common.sm.annotation.State;
import de.ma.it.common.sm.annotation.Transition;

public class ProcessManager {

    @State
    public static final String INITIAL = "initial";

    @State
    public static final String PROCESSING = "processing";

    @State
    public static final String RELEASED = "released";

    @State
    public static final String CANCELED = "canceled";
    /** Constructor of TapeDeckManager */
    public ProcessManager() {
        super();
    }

    @Transition(on = "create", in = INITIAL, next = PROCESSING)
    public void createProcess() {
        System.out.println("Create OK. New state is " + PROCESSING );
    }

    @Transition(on = "release", in = PROCESSING, next = RELEASED)
    public void releaseProcess(Integer processNumber) {
        System.out.println("Release process " + processNumber + " OK. New state is " + RELEASED );
    }

    @Transition(on = "cancel", in = PROCESSING, next = CANCELED)
    public void cancelProcess(Integer processNumber) {
        System.out.println("Cancel  process " + processNumber + " OK. New state is " + CANCELED );
    }
}
