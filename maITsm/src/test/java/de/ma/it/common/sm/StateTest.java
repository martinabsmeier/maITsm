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

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.ma.it.common.sm.transition.Transition;

/**
 * Tests {@link State}.
 *
 * @author Martin Absmeier
 */
public class StateTest extends TestCase {
    State state;

    Transition transition1;

    Transition transition2;

    Transition transition3;

    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        state = new State("test");
        transition1 = (Transition) Mockito.mock(Transition.class);
        transition2 = (Transition) Mockito.mock(Transition.class);
        transition3 = (Transition) Mockito.mock(Transition.class);
    }

    @After
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        state = null;
        transition1 = null;
        transition2 = null;
        transition3 = null;
    }
    
    @Test
    public void testAddFirstTransition() throws Exception {
        assertTrue(state.getTransitions().isEmpty());
        state.addTransition(transition1);
        assertFalse(state.getTransitions().isEmpty());
        assertEquals(1, state.getTransitions().size());
        assertSame(transition1, state.getTransitions().get(0));
    }

    @Test
    public void testUnweightedTransitions() throws Exception {
        assertTrue(state.getTransitions().isEmpty());
        state.addTransition(transition1);
        state.addTransition(transition2);
        state.addTransition(transition3);
        assertEquals(3, state.getTransitions().size());
        assertSame(transition1, state.getTransitions().get(0));
        assertSame(transition2, state.getTransitions().get(1));
        assertSame(transition3, state.getTransitions().get(2));
    }

    @Test
    public void testWeightedTransitions() throws Exception {
        assertTrue(state.getTransitions().isEmpty());
        state.addTransition(transition1, 10);
        state.addTransition(transition2, 5);
        state.addTransition(transition3, 7);
        assertEquals(3, state.getTransitions().size());
        assertSame(transition2, state.getTransitions().get(0));
        assertSame(transition3, state.getTransitions().get(1));
        assertSame(transition1, state.getTransitions().get(2));
    }

    @Test
    public void testAddTransitionReturnsSelf() throws Exception {
        assertSame(state, state.addTransition(transition1));
    }

    @Test
    public void testAddNullTransitionThrowsException() throws Exception {
        try {
            state.addTransition(null);
            fail("null transition added. IllegalArgumentException expected.");
        } catch (IllegalArgumentException npe) {
        }
    }
}
