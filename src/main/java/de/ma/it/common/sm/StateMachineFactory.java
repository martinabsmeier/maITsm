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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.ma.it.common.sm.annotation.OnEntry;
import de.ma.it.common.sm.annotation.OnExit;
import de.ma.it.common.sm.annotation.Transition;
import de.ma.it.common.sm.annotation.TransitionAnnotation;
import de.ma.it.common.sm.annotation.Transitions;
import de.ma.it.common.sm.event.Event;
import de.ma.it.common.sm.exception.StateMachineCreationException;
import de.ma.it.common.sm.transition.MethodSelfTransition;
import de.ma.it.common.sm.transition.MethodTransition;
import de.ma.it.common.sm.transition.SelfTransition;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates {@link StateMachine}s by reading {@link de.ma.it.common.sm.annotation.State}, {@link Transition} and
 * {@link Transitions} (or equivalent) and {@link SelfTransition} annotations from one or more arbitrary objects.
 *
 * @author Martin Absmeier
 */
public class StateMachineFactory {

    private final Class<? extends Annotation> transitionAnnotation;

    private final Class<? extends Annotation> transitionsAnnotation;

    private final Class<? extends Annotation> entrySelfTransitionsAnnotation;

    private final Class<? extends Annotation> exitSelfTransitionsAnnotation;

    protected StateMachineFactory(Class<? extends Annotation> transitionAnnotation, Class<? extends Annotation> transitionsAnnotation,
            Class<? extends Annotation> entrySelfTransitionsAnnotation, Class<? extends Annotation> exitSelfTransitionsAnnotation) {
        this.transitionAnnotation = transitionAnnotation;
        this.transitionsAnnotation = transitionsAnnotation;
        this.entrySelfTransitionsAnnotation = entrySelfTransitionsAnnotation;
        this.exitSelfTransitionsAnnotation = exitSelfTransitionsAnnotation;
    }

    /**
     * Returns a new {@link StateMachineFactory} instance which creates {@link StateMachine}s by reading the specified
     * {@link Transition} equivalent annotation.
     *
     * @param transitionAnnotation the {@link Transition} equivalent annotation.
     * @return the {@link StateMachineFactory}.
     */
    public static StateMachineFactory getInstance(Class<? extends Annotation> transitionAnnotation) {
        TransitionAnnotation a = transitionAnnotation.getAnnotation(TransitionAnnotation.class);
        if (a == null) {
            throw new IllegalArgumentException("The annotation class " + transitionAnnotation + " has not been annotated with the "
                    + TransitionAnnotation.class.getName() + " annotation");
        }

        return new StateMachineFactory(transitionAnnotation, a.value(), OnEntry.class, OnExit.class);
    }

    /**
     * Creates a new {@link StateMachine} from the specified handler object and using a start state with id
     * <code>start</code>.
     *
     * @param handler the object containing the annotations describing the state machine.
     * @return the {@link StateMachine} object.
     */
    public StateMachine create(Object handler) {
        return create(handler, new Object[0]);
    }

    /**
     * Creates a new {@link StateMachine} from the specified handler object and using the {@link State} with the
     * specified id as start state.
     *
     * @param start the id of the start {@link State} to use.
     * @param handler the object containing the annotations describing the state machine.
     * @return the {@link StateMachine} object.
     */
    public StateMachine create(String start, Object handler) {
        return create(start, handler, new Object[0]);
    }

    /**
     * Creates a new {@link StateMachine} from the specified handler objects and using a start state with id
     * <code>start</code>.
     *
     * @param handler the first object containing the annotations describing the state machine.
     * @param handlers zero or more additional objects containing the annotations describing the state machine.
     * @return the {@link StateMachine} object.
     */
    public StateMachine create(Object handler, Object... handlers) {
        return create("start", handler, handlers);
    }

    /**
     * Creates a new {@link StateMachine} from the specified handler objects and using the {@link State} with the
     * specified id as start state.
     *
     * @param start the id of the start {@link State} to use.
     * @param handler the first object containing the annotations describing the state machine.
     * @param handlers zero or more additional objects containing the annotations describing the state machine.
     * @return the {@link StateMachine} object.
     */
    public StateMachine create(String start, Object handler, Object... handlers) {

        Map<String, State> states = new HashMap<>();
        List<Object> handlersList = new ArrayList<>(1 + handlers.length);
        handlersList.add(handler);
        handlersList.addAll(Arrays.asList(handlers));

        LinkedList<Field> fields = new LinkedList<Field>();
        for (Object h : handlersList) {
            fields.addAll(getFields(h instanceof Class ? (Class<?>) h : h.getClass()));
        }
        for (State state : createStates(fields)) {
            states.put(state.getId(), state);
        }

        if (!states.containsKey(start)) {
            throw new StateMachineCreationException("Start state '" + start + "' not found.");
        }

        setupTransitions(transitionAnnotation, transitionsAnnotation, entrySelfTransitionsAnnotation, exitSelfTransitionsAnnotation,
                         states, handlersList);

        return new StateMachine(states.values(), start);
    }

    private static void setupTransitions(Class<? extends Annotation> transitionAnnotation,
            Class<? extends Annotation> transitionsAnnotation, Class<? extends Annotation> onEntrySelfTransitionAnnotation,
            Class<? extends Annotation> onExitSelfTransitionAnnotation, Map<String, State> states, List<Object> handlers) {
        for (Object handler : handlers) {
            setupTransitions(transitionAnnotation, transitionsAnnotation, onEntrySelfTransitionAnnotation, onExitSelfTransitionAnnotation,
                             states, handler);
        }
    }

    private static void setupSelfTransitions(Method m, Class<? extends Annotation> onEntrySelfTransitionAnnotation,
            Class<? extends Annotation> onExitSelfTransitionAnnotation, Map<String, State> states, Object handler) {
        if (m.isAnnotationPresent(OnEntry.class)) {
            OnEntry onEntryAnnotation = (OnEntry) m.getAnnotation(onEntrySelfTransitionAnnotation);
            State state = states.get(onEntryAnnotation.value());
            if (state == null) {
                throw new StateMachineCreationException("Error encountered " + "when processing onEntry annotation in method " + m
                        + ". state " + onEntryAnnotation.value() + " not Found.");

            }
            state.addOnEntrySelfTransaction(new MethodSelfTransition(m, handler));
        }

        if (m.isAnnotationPresent(OnExit.class)) {
            OnExit onExitAnnotation = (OnExit) m.getAnnotation(onExitSelfTransitionAnnotation);
            State state = states.get(onExitAnnotation.value());
            if (state == null) {
                throw new StateMachineCreationException("Error encountered " + "when processing onExit annotation in method " + m
                        + ". state " + onExitAnnotation.value() + " not Found.");

            }
            state.addOnExitSelfTransaction(new MethodSelfTransition(m, handler));
        }

    }

    private static void setupTransitions(Class<? extends Annotation> transitionAnnotation,
            Class<? extends Annotation> transitionsAnnotation, Class<? extends Annotation> onEntrySelfTransitionAnnotation,
            Class<? extends Annotation> onExitSelfTransitionAnnotation, Map<String, State> states, Object handler) {

        Method[] methods = handler.getClass().getDeclaredMethods();
        Arrays.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(Method m1, Method m2) {
                return m1.toString().compareTo(m2.toString());
            }
        });

        for (Method m : methods) {
            setupSelfTransitions(m, onEntrySelfTransitionAnnotation, onExitSelfTransitionAnnotation, states, handler);

            List<TransitionWrapper> transitionAnnotations = new ArrayList<>();
            if (m.isAnnotationPresent(transitionAnnotation)) {
                transitionAnnotations.add(new TransitionWrapper(transitionAnnotation, m.getAnnotation(transitionAnnotation)));
            }
            if (m.isAnnotationPresent(transitionsAnnotation)) {
                transitionAnnotations.addAll(Arrays.asList(new TransitionsWrapper(transitionAnnotation, transitionsAnnotation, m
                                                                                  .getAnnotation(transitionsAnnotation)).value()));
            }

            if (transitionAnnotations.isEmpty()) {
                continue;
            }

            for (TransitionWrapper annotation : transitionAnnotations) {
                Object[] eventIds = annotation.on();
                if (eventIds.length == 0) {
                    throw new StateMachineCreationException("Error encountered " + "when processing method " + m
                            + ". No event ids specified.");
                }
                if (annotation.in().length == 0) {
                    throw new StateMachineCreationException("Error encountered " + "when processing method " + m + ". No states specified.");
                }

                State next = null;
                if (!annotation.next().equals(Transition.SELF)) {
                    next = states.get(annotation.next());
                    if (next == null) {
                        throw new StateMachineCreationException("Error encountered " + "when processing method " + m
                                + ". Unknown next state: " + annotation.next() + ".");
                    }
                }

                for (Object event : eventIds) {
                    if (event == null) {
                        event = Event.WILDCARD_EVENT_ID;
                    }
                    if (!(event instanceof String)) {
                        event = event.toString();
                    }
                    for (String in : annotation.in()) {
                        State state = states.get(in);
                        if (state == null) {
                            throw new StateMachineCreationException("Error encountered " + "when processing method " + m
                                    + ". Unknown state: " + in + ".");
                        }

                        state.addTransition(new MethodTransition(event, next, m, handler), annotation.weight());
                    }
                }
            }
        }
    }

    public static List<Field> getFields(Class<?> clazz) {
        LinkedList<Field> fields = new LinkedList<>();

        for (Field f : clazz.getDeclaredFields()) {
            if (!f.isAnnotationPresent(de.ma.it.common.sm.annotation.State.class)) {
                continue;
            }

            if ((f.getModifiers() & Modifier.STATIC) == 0 || (f.getModifiers() & Modifier.FINAL) == 0 || !f.getType().equals(String.class)) {
                throw new StateMachineCreationException("Error encountered when " + "processing field " + f + ". Only static final "
                        + "String fields can be used with the @State " + "annotation.");
            }

            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            fields.add(f);
        }

        return fields;
    }

    public static State[] createStates(List<Field> fields) {
        LinkedHashMap<String, State> states = new LinkedHashMap<>();

        while (!fields.isEmpty()) {
            int size = fields.size();
            int numStates = states.size();
            for (int i = 0; i < size; i++) {
                Field f = fields.remove(0);

                String value = null;
                try {
                    value = (String) f.get(null);
                } catch (IllegalAccessException iae) {
                    throw new StateMachineCreationException("Error encountered when " + "processing field " + f + ".", iae);
                }

                de.ma.it.common.sm.annotation.State stateAnnotation = f.getAnnotation(de.ma.it.common.sm.annotation.State.class);
                if (stateAnnotation.value().equals(de.ma.it.common.sm.annotation.State.ROOT)) {
                    states.put(value, new State(value));
                } else if (states.containsKey(stateAnnotation.value())) {
                    states.put(value, new State(value, states.get(stateAnnotation.value())));
                } else {
					// Move to the back of the list of fields for later
                    // processing
                    fields.add(f);
                }
            }

            /*
             * If no new states were added to states during this iteration it means that all fields in fields specify non-existent parents.
             */
            if (states.size() == numStates) {
                throw new StateMachineCreationException("Error encountered while creating "
                        + "FSM. The following fields specify non-existing parent states: " + fields);
            }
        }

        return states.values().toArray(new State[states.size()]);
    }

    private static class TransitionWrapper {

        private final Class<? extends Annotation> transitionClazz;

        private final Annotation annotation;

        public TransitionWrapper(Class<? extends Annotation> transitionClazz, Annotation annotation) {
            this.transitionClazz = transitionClazz;
            this.annotation = annotation;
        }

        Object[] on() {
            return getParameter("on", Object[].class);
        }

        String[] in() {
            return getParameter("in", String[].class);
        }

        String next() {
            return getParameter("next", String.class);
        }

        int weight() {
            return getParameter("weight", Integer.TYPE);
        }

        @SuppressWarnings("unchecked")
        private <T> T getParameter(String name, Class<T> returnType) {
            try {
                Method m = transitionClazz.getMethod(name);
                if (!returnType.isAssignableFrom(m.getReturnType())) {
                    throw new NoSuchMethodException();
                }
                return (T) m.invoke(annotation);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException t) {
                throw new StateMachineCreationException("Could not get parameter '" + name + "' from Transition annotation "
                        + transitionClazz);
            }
        }
    }

    private static class TransitionsWrapper {

        private final Class<? extends Annotation> transitionsclazz;

        private final Class<? extends Annotation> transitionClazz;

        private final Annotation annotation;

        public TransitionsWrapper(Class<? extends Annotation> transitionClazz, Class<? extends Annotation> transitionsclazz,
                Annotation annotation) {
            this.transitionClazz = transitionClazz;
            this.transitionsclazz = transitionsclazz;
            this.annotation = annotation;
        }

        TransitionWrapper[] value() {
            Annotation[] annos = getParameter("value", Annotation[].class);
            TransitionWrapper[] wrappers = new TransitionWrapper[annos.length];
            for (int i = 0; i < annos.length; i++) {
                wrappers[i] = new TransitionWrapper(transitionClazz, annos[i]);
            }
            return wrappers;
        }

        @SuppressWarnings("unchecked")
        private <T> T getParameter(String name, Class<T> returnType) {
            try {
                Method m = transitionsclazz.getMethod(name);
                if (!returnType.isAssignableFrom(m.getReturnType())) {
                    throw new NoSuchMethodException();
                }
                return (T) m.invoke(annotation);
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException t) {
                throw new StateMachineCreationException("Could not get parameter '" + name + "' from Transitions annotation "
                        + transitionsclazz);
            }
        }
    }
}
