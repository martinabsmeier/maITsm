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
package de.ma.it.common.sm.event;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.ma.it.common.sm.context.StateContext;
import java.io.Serializable;

/**
 * Represents an event which typically corresponds to a method call on a proxy. An event has an id
 * and zero or more arguments typically corresponding to the method arguments.
 * 
 * @author Martin Absmeier
 */
public class Event implements Serializable {
	public static final String WILDCARD_EVENT_ID = "*";
    
    private static final long serialVersionUID = -7224996357207464822L;

	private final Object id;

	private final StateContext context;

	private final Object[] arguments;

	/**
	 * Creates a new {@link Event} with the specified id and no arguments.
	 * 
	 * @param id
	 *            the event id.
	 * @param context
	 *            the {@link StateContext} the event was triggered for.
	 */
	public Event(Object id, StateContext context) {
		this(id, context, new Object[0]);
	}

	/**
	 * Creates a new {@link Event} with the specified id and arguments.
	 * 
	 * @param id
	 *            the event id.
	 * @param context
	 *            the {@link StateContext} the event was triggered for.
	 * @param arguments
	 *            the event arguments.
	 */
	public Event(Object id, StateContext context, Object[] arguments) {
		if (id == null) {
			throw new IllegalArgumentException("id");
		}
		if (context == null) {
			throw new IllegalArgumentException("context");
		}
		if (arguments == null) {
			throw new IllegalArgumentException("arguments");
		}
		this.id = id;
		this.context = context;
		this.arguments = arguments;
	}

	/**
	 * Returns the {@link StateContext} this {@link Event} was triggered for.
	 * 
	 * @return the {@link StateContext}.
	 */
	public StateContext getContext() {
		return context;
	}

	/**
	 * Returns the id of this {@link Event}.
	 * 
	 * @return the id.
	 */
	public Object getId() {
		return id;
	}

	/**
	 * Returns the arguments of this {@link Event}.
	 * 
	 * @return the arguments. Returns an empty array if this {@link Event} has no arguments.
	 */
	public Object[] getArguments() {
		return arguments;
	}

    @Override
	public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this);
		sb.append("id", id).append("context", context).append("arguments", arguments);
		return sb.toString();
	}
}
