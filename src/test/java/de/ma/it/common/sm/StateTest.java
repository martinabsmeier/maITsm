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

import de.ma.it.common.sm.transition.Transition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

/**
 * Tests {@link State}.
 *
 * @author Martin Absmeier
 */
public class StateTest {
    State state;

    Transition transition1;

    Transition transition2;

    Transition transition3;

    @Before
    public void setUp() {
        state = new State("test");
        transition1 = Mockito.mock(Transition.class);
        transition2 = Mockito.mock(Transition.class);
        transition3 = Mockito.mock(Transition.class);
    }

    @After
    public void tearDown() {
        state = null;
        transition1 = null;
        transition2 = null;
        transition3 = null;
    }

    @Test
    public void testAddFirstTransition() {
        assertTrue(state.getTransitions().isEmpty());
        state.addTransition(transition1);
        assertFalse(state.getTransitions().isEmpty());
        assertEquals(1, state.getTransitions().size());
        assertSame(transition1, state.getTransitions().get(0));
    }

    @Test
    public void testUnweightedTransitions() {
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
    public void testWeightedTransitions() {
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
    public void testAddTransitionReturnsSelf() {
        assertSame(state, state.addTransition(transition1));
    }

    @Test
    public void testAddNullTransitionThrowsException() {
        try {
            state.addTransition(null);
            fail("null transition added. IllegalArgumentException expected.");
        } catch (IllegalArgumentException npe) {
        }
    }
}
