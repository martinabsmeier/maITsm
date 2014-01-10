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
package de.ma.it.common.sm.exception;

import de.ma.it.common.sm.StateMachine;

/**
 * Exception thrown by {@link StateMachine} when a transition in the state
 * machine references a state which doesn't exist.
 *
 * @author Martin Absmeier
 */
public class NoSuchStateException extends RuntimeException {
    private static final long serialVersionUID = -886869696039996478L;

    /**
     * Creates a new instance.
     * 
     * @param stateId the id of the state which could not be found.
     */
    public NoSuchStateException(String stateId) {
        super("Could not find the state with id: " + stateId);
    }

}
