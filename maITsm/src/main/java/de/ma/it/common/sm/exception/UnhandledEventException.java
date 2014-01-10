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

import de.ma.it.common.sm.event.Event;

/**
 * Thrown when an {@link Event} passed to a state machine couldn't be handled.
 *
 * @author Martin Absmeier
 */
public class UnhandledEventException extends RuntimeException {
    private static final long serialVersionUID = -717373229954175430L;

    private final Event event;

    public UnhandledEventException(Event event) {
        super("Unhandled event: " + event);
        this.event = event;
    }

    /**
     * Returns the {@link Event} which couldn't be handled.
     * 
     * @return the {@link Event}.
     */
    public Event getEvent() {
        return event;
    }
}
