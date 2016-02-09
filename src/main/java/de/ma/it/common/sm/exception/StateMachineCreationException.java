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
import de.ma.it.common.sm.StateMachineFactory;

/**
 * Exception thrown by {@link StateMachineFactory} when a {@link StateMachine}
 * could not be constructed for some reason.
 *
 * @author Martin Absmeier
 */
public class StateMachineCreationException extends RuntimeException {
    private static final long serialVersionUID = 4103502727376992746L;

    /**
     * Creates a new instance.
     * 
     * @param message the message.
     */
    public StateMachineCreationException(String message) {
        super(message);
    }

    /**
    /**
     * Creates a new instance.
     * 
     * @param message the message.
     * @param cause the cause.
     */
    public StateMachineCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
