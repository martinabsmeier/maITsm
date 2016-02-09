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

import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Test;

import de.ma.it.common.sm.annotation.OnEntry;
import de.ma.it.common.sm.annotation.OnExit;
import de.ma.it.common.sm.annotation.Transition;
import de.ma.it.common.sm.annotation.Transitions;
import de.ma.it.common.sm.context.StateContext;
import de.ma.it.common.sm.event.Event;
import de.ma.it.common.sm.transition.MethodSelfTransition;
import de.ma.it.common.sm.transition.MethodTransition;

/**
 * Tests {@link StateMachineProxyBuilder}.
 *
 * @author Martin Absmeier
 */
public class StateMachineProxyBuilderTest extends TestCase {
    @Test
    public void testReentrantStateMachine() throws Exception {
        ReentrantStateMachineHandler handler = new ReentrantStateMachineHandler();

        State s1 = new State("s1");
        State s2 = new State("s2");
        State s3 = new State("s3");

        s1.addTransition(new MethodTransition("call1", s2, handler));
        s2.addTransition(new MethodTransition("call2", s3, handler));
        s3.addTransition(new MethodTransition("call3", handler));

        StateMachine sm = new StateMachine(new State[] { s1, s2, s3 }, "s1");
        Reentrant reentrant = new StateMachineProxyBuilder().create(Reentrant.class, sm);
        reentrant.call1(reentrant);
        assertTrue(handler.finished);
    }

    @Test
    public void testTapeDeckStateMachine() throws Exception {
        TapeDeckStateMachineHandler handler = new TapeDeckStateMachineHandler();

        State parent = new State("parent");
        State s1 = new State("s1", parent);
        State s2 = new State("s2", parent);
        State s3 = new State("s3", parent);
        State s4 = new State("s4", parent);
        State s5 = new State("s5", parent);

        parent.addTransition(new MethodTransition("*", "error", handler));
        s1.addTransition(new MethodTransition("insert", s2, "inserted", handler));
        s2.addTransition(new MethodTransition("start", s3, "playing", handler));
        s3.addTransition(new MethodTransition("stop", s4, "stopped", handler));
        s3.addTransition(new MethodTransition("pause", s5, "paused", handler));
        s4.addTransition(new MethodTransition("eject", s1, "ejected", handler));
        s5.addTransition(new MethodTransition("pause", s3, "playing", handler));

        s2.addOnEntrySelfTransaction(new MethodSelfTransition("onEntryS2", handler));
        s2.addOnExitSelfTransaction(new MethodSelfTransition("onExitS2", handler));

        s3.addOnEntrySelfTransaction(new MethodSelfTransition("onEntryS3", handler));
        s3.addOnExitSelfTransaction(new MethodSelfTransition("onExitS3", handler));

        s4.addOnEntrySelfTransaction(new MethodSelfTransition("onEntryS4", handler));
        s4.addOnExitSelfTransaction(new MethodSelfTransition("onExitS4", handler));

        StateMachine sm = new StateMachine(new State[] { s1, s2, s3, s4, s5 }, "s1");
        TapeDeck player = new StateMachineProxyBuilder().create(TapeDeck.class, sm);
        player.insert("Kings of convenience - Riot on an empty street");
        player.start();
        player.pause();
        player.pause();
        player.eject();
        player.stop();
        player.eject();

        LinkedList<String> messages = handler.messages;
        assertEquals("Tape 'Kings of convenience - Riot on an empty street' inserted", messages.removeFirst());
        assertEquals("S2 entered", messages.removeFirst());
        assertEquals("Playing", messages.removeFirst());
        assertEquals("S2 exited", messages.removeFirst());
        assertEquals("S3 entered with stateContext", messages.removeFirst());
        assertEquals("Paused", messages.removeFirst());
        assertEquals("S3 exited with stateContext", messages.removeFirst());
        assertEquals("Playing", messages.removeFirst());
        assertEquals("S3 entered with stateContext", messages.removeFirst());
        assertEquals("Error: Cannot eject at this time", messages.removeFirst());
        assertEquals("Stopped", messages.removeFirst());
        assertEquals("S3 exited with stateContext", messages.removeFirst());
        assertEquals("S4 entered with stateContext and state", messages.removeFirst());
        assertEquals("Tape ejected", messages.removeFirst());
        assertEquals("S4 exited with stateContext and state", messages.removeFirst());

        assertTrue(messages.isEmpty());
    }

    @Test
    public void testTapeDeckStateMachineAnnotations() throws Exception {
        TapeDeckStateMachineHandler handler = new TapeDeckStateMachineHandler();

        StateMachine sm = StateMachineFactory.getInstance(Transition.class).create(TapeDeckStateMachineHandler.S1,
                handler);

        TapeDeck player = new StateMachineProxyBuilder().create(TapeDeck.class, sm);
        player.insert("Kings of convenience - Riot on an empty street");
        player.start();
        player.pause();
        player.pause();
        player.eject();
        player.stop();
        player.eject();

        LinkedList<String> messages = handler.messages;
        assertEquals("Tape 'Kings of convenience - Riot on an empty street' inserted", messages.removeFirst());
        assertEquals("S2 entered", messages.removeFirst());
        assertEquals("Playing", messages.removeFirst());
        assertEquals("S2 exited", messages.removeFirst());
        assertEquals("S3 entered with stateContext", messages.removeFirst());
        assertEquals("Paused", messages.removeFirst());
        assertEquals("S3 exited with stateContext", messages.removeFirst());
        assertEquals("Playing", messages.removeFirst());
        assertEquals("S3 entered with stateContext", messages.removeFirst());
        assertEquals("Error: Cannot eject at this time", messages.removeFirst());
        assertEquals("Stopped", messages.removeFirst());
        assertEquals("S3 exited with stateContext", messages.removeFirst());
        assertEquals("S4 entered with stateContext and state", messages.removeFirst());
        assertEquals("Tape ejected", messages.removeFirst());
        assertEquals("S4 exited with stateContext and state", messages.removeFirst());

        assertTrue(messages.isEmpty());
    }

    public interface Reentrant {
        void call1(Reentrant proxy);

        void call2(Reentrant proxy);

        void call3(Reentrant proxy);
    }

    public static class ReentrantStateMachineHandler {
        private boolean finished = false;

        public void call1(Reentrant proxy) {
            proxy.call2(proxy);
        }

        public void call2(Reentrant proxy) {
            proxy.call3(proxy);
        }

        public void call3(Reentrant proxy) {
            finished = true;
        }
    }

    public interface TapeDeck {
        void insert(String name);

        void eject();

        void start();

        void pause();

        void stop();
    }

    public static class TapeDeckStateMachineHandler {
        @de.ma.it.common.sm.annotation.State
        public static final String PARENT = "parent";

        @de.ma.it.common.sm.annotation.State(PARENT)
        public static final String S1 = "s1";

        @de.ma.it.common.sm.annotation.State(PARENT)
        public static final String S2 = "s2";

        @de.ma.it.common.sm.annotation.State(PARENT)
        public static final String S3 = "s3";

        @de.ma.it.common.sm.annotation.State(PARENT)
        public static final String S4 = "s4";

        @de.ma.it.common.sm.annotation.State(PARENT)
        public static final String S5 = "s5";

        private LinkedList<String> messages = new LinkedList<String>();

        @OnEntry(S2)
        public void onEntryS2() {
            messages.add("S2 entered");
        }

        @OnExit(S2)
        public void onExitS2() {
            messages.add("S2 exited");
        }

        @OnEntry(S3)
        public void onEntryS3(StateContext stateContext) {
            messages.add("S3 entered with stateContext");
        }

        @OnExit(S3)
        public void onExitS3(StateContext stateContext) {
            messages.add("S3 exited with stateContext");
        }

        @OnEntry(S4)
        public void onEntryS4(StateContext stateContext, State state) {
            messages.add("S4 entered with stateContext and state");
        }

        @OnExit(S4)
        public void onExitS4(StateContext stateContext, State state) {
            messages.add("S4 exited with stateContext and state");
        }

        @Transition(on = "insert", in = "s1", next = "s2")
        public void inserted(String name) {
            messages.add("Tape '" + name + "' inserted");
        }

        @Transition(on = "eject", in = "s4", next = "s1")
        public void ejected() {
            messages.add("Tape ejected");
        }

        @Transitions({ @Transition(on = "start", in = "s2", next = "s3"),
                @Transition(on = "pause", in = "s5", next = "s3") })
        public void playing() {
            messages.add("Playing");
        }

        @Transition(on = "pause", in = "s3", next = "s5")
        public void paused() {
            messages.add("Paused");
        }

        @Transition(on = "stop", in = "s3", next = "s4")
        public void stopped() {
            messages.add("Stopped");
        }

        @Transition(on = "*", in = "parent")
        public void error(Event event) {
            messages.add("Error: Cannot " + event.getId() + " at this time");
        }
    }
}
