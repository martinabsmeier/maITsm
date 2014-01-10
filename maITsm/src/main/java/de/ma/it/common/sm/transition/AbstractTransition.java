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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import de.ma.it.common.sm.State;
import de.ma.it.common.sm.StateMachine;
import de.ma.it.common.sm.event.Event;

/**
 * Abstract {@link Transition} implementation. Takes care of matching the
 * current {@link Event}'s id against the id of the {@link Event} this 
 * {@link Transition} handles. To handle any {@link Event} the id should be set
 * to {@link Event#WILDCARD_EVENT_ID}.
 *
 * @author Martin Absmeier
 */
public abstract class AbstractTransition implements Transition {
    private final Object eventId;

    private final State nextState;

    /**
     * Creates a new instance which will loopback to the same {@link State} 
     * for the specified {@link Event} id.
     * 
     * @param eventId the {@link Event} id.
     */
    public AbstractTransition(Object eventId) {
        this(eventId, null);
    }

    /**
     * Creates a new instance with the specified {@link State} as next state 
     * and for the specified {@link Event} id.
     * 
     * @param eventId the {@link Event} id.
     * @param nextState the next {@link State}.
     */
    public AbstractTransition(Object eventId, State nextState) {
        this.eventId = eventId;
        this.nextState = nextState;
    }

    public State getNextState() {
        return nextState;
    }

    public boolean execute(Event event) {
        if (!eventId.equals(Event.WILDCARD_EVENT_ID) && !eventId.equals(event.getId())) {
            return false;
        }

        return doExecute(event);
    }

    /**
     * Executes this {@link Transition}. This method doesn't have to check
     * if the {@link Event}'s id matches because {@link #execute(Event)} has
     * already made sure that that is the case.
     * 
     * @param event the current {@link Event}.
     * @return <code>true</code> if the {@link Transition} has been executed 
     *         successfully and the {@link StateMachine} should move to the 
     *         next {@link State}. <code>false</code> otherwise.
     */
    protected abstract boolean doExecute(Event event);

    public boolean equals(Object o) {
        if (!(o instanceof AbstractTransition)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        AbstractTransition that = (AbstractTransition) o;
        return new EqualsBuilder().append(eventId, that.eventId).append(nextState, that.nextState).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(11, 31).append(eventId).append(nextState).toHashCode();
    }

    public String toString() {
        return new ToStringBuilder(this).append("eventId", eventId).append("nextState", nextState).toString();
    }
}
