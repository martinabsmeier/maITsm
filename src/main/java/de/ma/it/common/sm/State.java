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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import de.ma.it.common.sm.event.Event;
import de.ma.it.common.sm.transition.SelfTransition;
import de.ma.it.common.sm.transition.Transition;

/**
 * Represents a state in a {@link StateMachine}. Normally you wouldn't create instances of this class directly but
 * rather use the {@link de.ma.it.common.sm.annotation.State} annotation to define your states and then let
 * {@link StateMachineFactory} create a {@link StateMachine} for you.
 * <p>
 * {@link State}s inherits {@link Transition}s from their parent. A {@link State} can override any of the parents
 * {@link Transition}s. When an {@link Event} is processed the {@link Transition}s of the current {@link State} will be
 * searched for a {@link Transition} which can handle the event. If none is found the {@link State}'s parent will be
 * searched and so on.
 * </p>
 *
 * @author Martin Absmeier
 */
public class State {

    private final String id;

    private final State parent;

    private final List<TransitionHolder> transitionHolders = new ArrayList<TransitionHolder>();

    private List<Transition> transitions = Collections.emptyList();

    private final List<SelfTransition> onEntries = new ArrayList<SelfTransition>();

    private final List<SelfTransition> onExits = new ArrayList<SelfTransition>();

    /**
     * Creates a new {@link State} with the specified id.
     *
     * @param id the unique id of this {@link State}.
     */
    public State(String id) {
        this(id, null);
    }

    /**
     * Creates a new {@link State} with the specified id and parent.
     *
     * @param id the unique id of this {@link State}.
     * @param parent the parent {@link State}.
     */
    public State(String id, State parent) {
        this.id = id;
        this.parent = parent;
    }

    /**
     * Returns the id of this {@link State}.
     *
     * @return the id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the parent {@link State}.
     *
     * @return the parent or <code>null</code> if this {@link State} has no parent.
     */
    public State getParent() {
        return parent;
    }

    /**
     * Returns an unmodifiable {@link List} of {@link Transition}s going out from this {@link State}.
     *
     * @return the {@link Transition}s.
     */
    public List<Transition> getTransitions() {
        return Collections.unmodifiableList(transitions);
    }

    /**
     * Returns an unmodifiable {@link List} of entry {@link SelfTransition}s
     *
     * @return the {@link SelfTransition}s.
     */
    public List<SelfTransition> getOnEntrySelfTransitions() {
        return Collections.unmodifiableList(onEntries);
    }

    /**
     * Returns an unmodifiable {@link List} of exit {@link SelfTransition}s
     *
     * @return the {@link SelfTransition}s.
     */
    public List<SelfTransition> getOnExitSelfTransitions() {
        return Collections.unmodifiableList(onExits);
    }

    /**
     * Adds an entry {@link SelfTransition} to this {@link State}
     *
     * @param onEntrySelfTransaction the {@link SelfTransition} to add.
     * @return this {@link State}.
     */
    public State addOnEntrySelfTransaction(SelfTransition onEntrySelfTransaction) {
        if (onEntrySelfTransaction == null) {
            throw new IllegalArgumentException("transition");
        }
        onEntries.add(onEntrySelfTransaction);
        return this;
    }

    /**
     * Adds an exit {@link SelfTransition} to this {@link State}
     *
     * @param onExitSelfTransaction The {@link SelfTransition} to add.
     * @return this {@link State}.
     */
    public State addOnExitSelfTransaction(SelfTransition onExitSelfTransaction) {
        if (onExitSelfTransaction == null) {
            throw new IllegalArgumentException("transition");
        }
        onExits.add(onExitSelfTransaction);
        return this;
    }

    private void updateTransitions() {
        transitions = new ArrayList<Transition>(transitionHolders.size());
        for (TransitionHolder holder : transitionHolders) {
            transitions.add(holder.transition);
        }
    }

    /**
     * Adds an outgoing {@link Transition} to this {@link State} with weight 0.
     *
     * @param transition the {@link Transition} to add.
     * @return this {@link State}.
     * @see #addTransition(Transition, int)
     */
    public State addTransition(Transition transition) {
        return addTransition(transition, 0);
    }

    /**
     * Adds an outgoing {@link Transition} to this {@link State} with the specified weight. The higher the weight the
     * less important a {@link Transition} is. If two {@link Transition}s match the same {@link Event} the
     * {@link Transition} with the lower weight will be executed.
     *
     * @param transition the {@link Transition} to add.
     * @param weight
     * @return this {@link State}.
     */
    public State addTransition(Transition transition, int weight) {
        if (transition == null) {
            throw new IllegalArgumentException("transition");
        }

        transitionHolders.add(new TransitionHolder(transition, weight));
        Collections.sort(transitionHolders);
        updateTransitions();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof State)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        State that = (State) o;
        return new EqualsBuilder().append(this.id, that.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 33).append(this.id).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).toString();
    }

    private static class TransitionHolder implements Comparable<TransitionHolder> {

        Transition transition;
        int weight;

        TransitionHolder(Transition transition, int weight) {
            this.transition = transition;
            this.weight = weight;
        }

        @Override
        public int compareTo(TransitionHolder o) {
            return (weight > o.weight) ? 1 : (weight < o.weight ? -1 : 0);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(13, 33).append(weight).toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TransitionHolder other = (TransitionHolder) obj;
            return new EqualsBuilder().append(this.weight, other.weight).isEquals();
        }

    }
}
