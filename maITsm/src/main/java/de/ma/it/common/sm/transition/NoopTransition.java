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


import de.ma.it.common.sm.State;
import de.ma.it.common.sm.event.Event;

/**
 * {@link Transition} implementation which does nothing but change the state.
 *
 * @author Martin Absmeier
 */
public class NoopTransition extends AbstractTransition {

    /**
     * Creates a new instance which will loopback to the same {@link State} 
     * for the specified {@link Event} id.
     * 
     * @param eventId the {@link Event} id.
     */
    public NoopTransition(Object eventId) {
        super(eventId);
    }

    /**
     * Creates a new instance with the specified {@link State} as next state 
     * and for the specified {@link Event} id.
     * 
     * @param eventId the {@link Event} id.
     * @param nextState the next {@link State}.
     */
    public NoopTransition(Object eventId, State nextState) {
        super(eventId, nextState);
    }

    /**
     * 
     * @param event
     * @return 
     */
    @Override
    protected boolean doExecute(Event event) {
        return true;
    }

}
