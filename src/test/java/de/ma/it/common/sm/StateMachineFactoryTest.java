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
package de.ma.it.common.sm;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.ma.it.common.sm.State;
import de.ma.it.common.sm.StateMachine;
import de.ma.it.common.sm.StateMachineFactory;
import de.ma.it.common.sm.annotation.Transition;
import de.ma.it.common.sm.annotation.Transitions;
import de.ma.it.common.sm.exception.StateMachineCreationException;
import de.ma.it.common.sm.transition.MethodTransition;

/**
 * Tests {@link StateMachineFactory}.
 *
 * @author Martin Absmeier
 */
public class StateMachineFactoryTest extends TestCase {
    private Method barInA;

    private Method error;

    private Method fooInA;

    private Method fooInB;

    private Method barInC;

    private Method fooOrBarInCOrFooInD;

    @Before
    @Override
    public void setUp() throws Exception {
        barInA = States.class.getDeclaredMethod("barInA", new Class[0]);
        error = States.class.getDeclaredMethod("error", new Class[0]);
        fooInA = States.class.getDeclaredMethod("fooInA", new Class[0]);
        fooInB = States.class.getDeclaredMethod("fooInB", new Class[0]);
        barInC = States.class.getDeclaredMethod("barInC", new Class[0]);
        fooOrBarInCOrFooInD = States.class.getDeclaredMethod("fooOrBarInCOrFooInD", new Class[0]);
    }

    @After
    @Override
    protected void tearDown() throws Exception {
        barInA = null;
        error = null;
        fooInA = null;
        fooInB = null;
        barInC = null;
        fooOrBarInCOrFooInD = null;
    }
    
    @Test
    public void testCreate() throws Exception {
        States states = new States();
        StateMachine sm = StateMachineFactory.getInstance(Transition.class).create(States.A, states);

        State a = sm.getState(States.A);
        State b = sm.getState(States.B);
        State c = sm.getState(States.C);
        State d = sm.getState(States.D);

        assertEquals(States.A, a.getId());
        assertNull(a.getParent());
        assertEquals(States.B, b.getId());
        assertSame(a, b.getParent());
        assertEquals(States.C, c.getId());
        assertSame(b, c.getParent());
        assertEquals(States.D, d.getId());
        assertSame(a, d.getParent());

        List<de.ma.it.common.sm.transition.Transition> trans = null;

        trans = a.getTransitions();
        assertEquals(3, trans.size());
        assertEquals(new MethodTransition("bar", barInA, states), trans.get(0));
        assertEquals(new MethodTransition("*", error, states), trans.get(1));
        assertEquals(new MethodTransition("foo", b, fooInA, states), trans.get(2));

        trans = b.getTransitions();
        assertEquals(1, trans.size());
        assertEquals(new MethodTransition("foo", c, fooInB, states), trans.get(0));

        trans = c.getTransitions();
        assertEquals(3, trans.size());
        assertEquals(new MethodTransition("bar", a, barInC, states), trans.get(0));
        assertEquals(new MethodTransition("foo", d, fooOrBarInCOrFooInD, states), trans.get(1));
        assertEquals(new MethodTransition("bar", d, fooOrBarInCOrFooInD, states), trans.get(2));

        trans = d.getTransitions();
        assertEquals(1, trans.size());
        assertEquals(new MethodTransition("foo", fooOrBarInCOrFooInD, states), trans.get(0));
    }

    @Test
    public void testCreateStates() throws Exception {
        State[] states = StateMachineFactory.createStates(StateMachineFactory.getFields(States.class));
        assertEquals(States.A, states[0].getId());
        assertNull(states[0].getParent());
        assertEquals(States.B, states[1].getId());
        assertEquals(states[0], states[1].getParent());
        assertEquals(States.C, states[2].getId());
        assertEquals(states[1], states[2].getParent());
        assertEquals(States.D, states[3].getId());
        assertEquals(states[0], states[3].getParent());
    }

    @Test
    public void testCreateStatesMissingParents() throws Exception {
        try {
            StateMachineFactory.createStates(StateMachineFactory.getFields(StatesWithMissingParents.class));
            fail("Missing parents. FsmCreationException expected.");
        } catch (StateMachineCreationException fce) {
        }
    }

    public static class States {
        @de.ma.it.common.sm.annotation.State
        protected static final String A = "a";

        @de.ma.it.common.sm.annotation.State(A)
        protected static final String B = "b";

        @de.ma.it.common.sm.annotation.State(B)
        protected static final String C = "c";

        @de.ma.it.common.sm.annotation.State(A)
        protected static final String D = "d";

        @Transition(on = "bar", in = A)
        protected void barInA() {
        }

        @Transition(on = "bar", in = C, next = A)
        protected void barInC() {
        }

        @Transition(in = A)
        protected void error() {
        }

        @Transition(on = "foo", in = A, next = B)
        protected void fooInA() {
        }

        @Transition(on = "foo", in = B, next = C)
        protected void fooInB() {
        }

        @Transitions({ @Transition(on = { "foo", "bar" }, in = C, next = D), @Transition(on = "foo", in = D) })
        protected void fooOrBarInCOrFooInD() {
        }

    }

    public static class StatesWithMissingParents {
        @de.ma.it.common.sm.annotation.State("b")
        public static final String A = "a";

        @de.ma.it.common.sm.annotation.State("c")
        public static final String B = "b";

        @de.ma.it.common.sm.annotation.State("d")
        public static final String C = "c";

        @de.ma.it.common.sm.annotation.State("e")
        public static final String D = "d";
    }
}
