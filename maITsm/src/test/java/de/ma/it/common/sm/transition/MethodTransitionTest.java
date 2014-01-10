/*
 * TODO Insert description here!
 * Copyright (C) 2014 Martin Absmeier, IT Consulting Services
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.ma.it.common.sm.transition;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ma.it.common.sm.State;
import de.ma.it.common.sm.context.StateContext;
import de.ma.it.common.sm.event.Event;

/**
 * Tests {@link MethodTransition}.
 *
 * @author Martin Absmeier
 */
public class MethodTransitionTest extends TestCase {

    private State nextState;

    private TestStateContext context;

    private Target target;

    private Method subsetAllArgsMethod1;

    private Method subsetAllArgsMethod2;

    private Event noArgsEvent;

    private Event argsEvent;

    private Object[] args;

    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        nextState = new State("next");
        target = (Target) Mockito.mock(Target.class);
        subsetAllArgsMethod1 = Target.class.getMethod("subsetAllArgs", new Class[] { TestStateContext.class, B.class, A.class, Integer.TYPE });
        subsetAllArgsMethod2 = Target.class.getMethod("subsetAllArgs", new Class[] { Event.class, B.class, B.class, Boolean.TYPE });
        args = new Object[] { new A(), new B(), new C(), new Integer(627438), Boolean.TRUE };
        context = (TestStateContext) Mockito.mock(TestStateContext.class);
        noArgsEvent = new Event("event", context, new Object[0]);
        argsEvent = new Event("event", context, args);
    }
    
    @After
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        nextState = null;
        target = null;
        subsetAllArgsMethod1 = null;
        subsetAllArgsMethod2 = null;
        args = null;
        context = null;
        noArgsEvent = null;
        argsEvent = null;
    }

    @Test
    public void testExecuteWrongEventId() throws Exception {
        MethodTransition t = new MethodTransition("otherEvent", nextState, "noArgs", target);
        assertFalse(t.execute(noArgsEvent));
    }

    @Test
    public void testExecuteNoArgsMethodOnNoArgsEvent() throws Exception {
        target.noArgs();
        MethodTransition t = new MethodTransition("event", nextState, "noArgs", target);
        assertTrue(t.execute(noArgsEvent));
    }

    @Test
    public void testExecuteNoArgsMethodOnArgsEvent() throws Exception {
        target.noArgs();
        MethodTransition t = new MethodTransition("event", nextState, "noArgs", target);
        assertTrue(t.execute(argsEvent));
    }

    @Test
    public void testExecuteExactArgsMethodOnNoArgsEvent() throws Exception {
        MethodTransition t = new MethodTransition("event", nextState, "exactArgs", target);
        assertFalse(t.execute(noArgsEvent));
    }

    @Test
    public void testExecuteExactArgsMethodOnArgsEvent() throws Exception {
        target.exactArgs((A) args[0], (B) args[1], (C) args[2], ((Integer) args[3]).intValue(),
                ((Boolean) args[4]).booleanValue());
        MethodTransition t = new MethodTransition("event", nextState, "exactArgs", target);
        assertTrue(t.execute(argsEvent));
    }

    @Test
    public void testExecuteSubsetExactArgsMethodOnNoArgsEvent() throws Exception {
        MethodTransition t = new MethodTransition("event", nextState, "subsetExactArgs", target);
        assertFalse(t.execute(noArgsEvent));
    }

    @Test
    public void testExecuteSubsetExactArgsMethodOnArgsEvent() throws Exception {
        target.subsetExactArgs((A) args[0], (A) args[1], ((Integer) args[3]).intValue());
        MethodTransition t = new MethodTransition("event", nextState, "subsetExactArgs", target);
        assertTrue(t.execute(argsEvent));
    }

    @Test
    public void testExecuteAllArgsMethodOnArgsEvent() throws Exception {
        target.allArgs(argsEvent, context, (A) args[0], (B) args[1], (C) args[2], ((Integer) args[3]).intValue(),
                ((Boolean) args[4]).booleanValue());
        MethodTransition t = new MethodTransition("event", nextState, "allArgs", target);
        assertTrue(t.execute(argsEvent));
    }

    @Test
    public void testExecuteSubsetAllArgsMethod1OnArgsEvent() throws Exception {
        target.subsetAllArgs(context, (B) args[1], (A) args[2], ((Integer) args[3]).intValue());
        MethodTransition t = new MethodTransition("event", nextState, subsetAllArgsMethod1, target);
        assertTrue(t.execute(argsEvent));
    }

    @Test
    public void testExecuteSubsetAllArgsMethod2OnArgsEvent() throws Exception {
        target.subsetAllArgs(argsEvent, (B) args[1], (B) args[2], ((Boolean) args[4]).booleanValue());
        MethodTransition t = new MethodTransition("event", nextState, subsetAllArgsMethod2, target);
        assertTrue(t.execute(argsEvent));
    }

    public interface Target {
        void noArgs();

        void exactArgs(A a, B b, C c, int integer, boolean bool);

        void allArgs(Event event, StateContext ctxt, A a, B b, C c, int integer, boolean bool);

        void subsetExactArgs(A a, A b, int integer);

        void subsetAllArgs(TestStateContext ctxt, B b, A c, int integer);

        void subsetAllArgs(Event event, B b, B c, boolean bool);
    }

    public interface TestStateContext extends StateContext {
    }

    public static class A {
    }

    public static class B extends A {
    }

    public static class C extends B {
    }
}
