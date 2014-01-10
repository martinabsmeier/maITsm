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

import org.junit.Test;

import de.ma.it.common.sm.context.DefaultStateContext;
import de.ma.it.common.sm.context.StateContext;
import de.ma.it.common.sm.event.Event;
import de.ma.it.common.sm.transition.AbstractSelfTransition;
import de.ma.it.common.sm.transition.AbstractTransition;

/**
 * Tests {@link StateMachine}.
 *
 * @author Martin Absmeier
 */
public class StateMachineTest extends TestCase {

    @Test
    public void testBreakAndContinue() throws Exception {
        State s1 = new State("s1");
        s1.addTransition(new BreakAndContinueTransition("foo"));
        s1.addTransition(new SuccessTransition("foo"));

        StateContext context = new DefaultStateContext();
        StateMachine sm = new StateMachine(new State[] { s1 }, "s1");
        sm.handle(new Event("foo", context));
        assertEquals(true, context.getAttribute("success"));
    }

    @Test
    public void testBreakAndGotoNow() throws Exception {
        State s1 = new State("s1");
        State s2 = new State("s2");
        s1.addTransition(new BreakAndGotoNowTransition("foo", "s2"));
        s2.addTransition(new SuccessTransition("foo"));

        StateContext context = new DefaultStateContext();
        StateMachine sm = new StateMachine(new State[] { s1, s2 }, "s1");
        sm.handle(new Event("foo", context));
        assertEquals(true, context.getAttribute("success"));
    }

    @Test
    public void testBreakAndGotoNext() throws Exception {
        State s1 = new State("s1");
        State s2 = new State("s2");
        s1.addTransition(new BreakAndGotoNextTransition("foo", "s2"));
        s2.addTransition(new SuccessTransition("foo"));

        StateContext context = new DefaultStateContext();
        StateMachine sm = new StateMachine(new State[] { s1, s2 }, "s1");
        sm.handle(new Event("foo", context));
        assertSame(s2, context.getCurrentState());
        sm.handle(new Event("foo", context));
        assertEquals(true, context.getAttribute("success"));
    }

    private static class SuccessTransition extends AbstractTransition {
        public SuccessTransition(Object eventId) {
            super(eventId);
        }

        public SuccessTransition(Object eventId, State nextState) {
            super(eventId, nextState);
        }

        @Override
        protected boolean doExecute(Event event) {
            event.getContext().setAttribute("success", true);
            return true;
        }
    }

    private static class BreakAndContinueTransition extends AbstractTransition {
        public BreakAndContinueTransition(Object eventId) {
            super(eventId);
        }

        public BreakAndContinueTransition(Object eventId, State nextState) {
            super(eventId, nextState);
        }

        @Override
        protected boolean doExecute(Event event) {
            StateControl.breakAndContinue();
            return true;
        }
    }

    private static class BreakAndGotoNowTransition extends AbstractTransition {
        private final String stateId;

        public BreakAndGotoNowTransition(Object eventId, String stateId) {
            super(eventId);
            this.stateId = stateId;
        }

        public BreakAndGotoNowTransition(Object eventId, State nextState, String stateId) {
            super(eventId, nextState);
            this.stateId = stateId;
        }

        @Override
        protected boolean doExecute(Event event) {
            StateControl.breakAndGotoNow(stateId);
            return true;
        }
    }

    private static class BreakAndGotoNextTransition extends AbstractTransition {
        private final String stateId;

        public BreakAndGotoNextTransition(Object eventId, String stateId) {
            super(eventId);
            this.stateId = stateId;
        }

        public BreakAndGotoNextTransition(Object eventId, State nextState, String stateId) {
            super(eventId, nextState);
            this.stateId = stateId;
        }

        @Override
        protected boolean doExecute(Event event) {
            StateControl.breakAndGotoNext(stateId);
            return true;
        }
    }

    private static class SampleSelfTransition extends AbstractSelfTransition {
        public SampleSelfTransition() {
            super();
        }

        @Override
        protected boolean doExecute(StateContext stateContext, State state) {
            stateContext.setAttribute("SelfSuccess" + state.getId(), true);
            return true;
        }

    }

    @Test
    public void testOnEntry() throws Exception {
        State s1 = new State("s1");
        State s2 = new State("s2");

        s1.addTransition(new SuccessTransition("foo", s2));
        s1.addOnExitSelfTransaction(new SampleSelfTransition());
        s2.addOnEntrySelfTransaction(new SampleSelfTransition());

        StateContext context = new DefaultStateContext();
        StateMachine sm = new StateMachine(new State[] { s1, s2 }, "s1");
        sm.handle(new Event("foo", context));
        assertEquals(true, context.getAttribute("success"));
        assertEquals(true, context.getAttribute("SelfSuccess" + s1.getId()));
        assertEquals(true, context.getAttribute("SelfSuccess" + s2.getId()));

    }

}
