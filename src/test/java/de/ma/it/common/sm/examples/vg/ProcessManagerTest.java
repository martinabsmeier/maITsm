package de.ma.it.common.sm.examples.vg;

import de.ma.it.common.sm.StateMachine;
import de.ma.it.common.sm.StateMachineFactory;
import de.ma.it.common.sm.StateMachineProxyBuilder;
import de.ma.it.common.sm.annotation.Transition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ProcessManagerTest {

    private Process manager;

    @Before
    public void setUp() {
        StateMachineFactory factory = StateMachineFactory.getInstance(Transition.class);
        StateMachine sm = factory.create(ProcessManager.INITIAL, new ProcessManager());
        manager = new StateMachineProxyBuilder().create(Process.class, sm);
    }

    @After
    public void tearDown() {
        manager = null;
    }

    @Test
    public void testInit() {
        assertNotNull(manager);
    }

    @Test
    public void testCreateProcess() {
        manager.create();
        manager.release(Integer.parseInt("1"));
    }
}
