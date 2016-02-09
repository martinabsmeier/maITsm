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
import de.ma.it.common.sm.StateMachine;
import de.ma.it.common.sm.event.Event;

/**
 * The interface implemented by classes which need to react on transitions
 * between states.
 *
 * @author Martin Absmeier
 */
public interface Transition {
    /**
     * Executes this {@link Transition}. It is the responsibility of this
     * {@link Transition} to determine whether it actually applies for the
     * specified {@link Event}. If this {@link Transition} doesn't apply
     * nothing should be executed and <code>false</code> must be returned.
     * 
     * @param event the current {@link Event}.
     * @return <code>true</code> if the {@link Transition} was executed, 
     *         <code>false</code> otherwise.
     */
    boolean execute(Event event);

    /**
     * Returns the {@link State} which the {@link StateMachine} should move to 
     * if this {@link Transition} is taken and {@link #execute(Event)} returns
     * <code>true</code>.
     * 
     * @return the next {@link State} or <code>null</code> if this 
     *         {@link Transition} is a loopback {@link Transition}.
     */
    State getNextState();
}
