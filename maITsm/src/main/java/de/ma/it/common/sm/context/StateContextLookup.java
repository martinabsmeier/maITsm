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
package de.ma.it.common.sm.context;

import de.ma.it.common.sm.event.Event;

/**
 * Lookups a {@link StateContext} from a collection of event arguments.
 * 
 * @author Martin Absmeier
 */
public interface StateContextLookup {

	/**
	 * Searches the arguments from an {@link Event} and returns a {@link StateContext} if any of the
	 * arguments holds one. NOTE! This method must create a new {@link StateContext} if a compatible
	 * object is in the arguments and the next time that same object is passed to this method the
	 * same {@link StateContext} should be returned.
	 */
	StateContext lookup(Object[] eventArgs);

}
