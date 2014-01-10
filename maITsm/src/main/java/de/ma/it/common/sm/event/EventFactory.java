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

import java.lang.reflect.Method;

import de.ma.it.common.sm.StateMachineProxyBuilder;
import de.ma.it.common.sm.context.StateContext;

/**
 * Used by {@link StateMachineProxyBuilder} to create {@link Event} objects when methods are invoked
 * on the proxy.
 * 
 * @author Martin Absmeier
 */
public interface EventFactory {

	/**
	 * Creates a new {@link Event} from the specified method and method arguments.
	 * 
	 * @param context
	 *            the current {@link StateContext}.
	 * @param method
	 *            the method being invoked.
	 * @param args
	 *            the method arguments.
	 * @return the {@link Event} object.
	 */
	Event create(StateContext context, Method method, Object[] arguments);

}
