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

import de.ma.it.common.sm.State;
import de.ma.it.common.sm.StateMachine;
import java.io.Serializable;

/**
 * {@link StateContext} objects are used to store the current {@link State} and any application specific attributes for
 * a specific client of a {@link StateMachine}. Since {@link StateMachine}s are singletons and shared by all clients
 * using the {@link StateMachine} this is where client specific data needs to be stored.
 *
 * @author Martin Absmeier
 */
public interface StateContext extends Serializable {

    /**
     * Returns the current {@link State}. This is only meant for internal use.
     *
     * @return the current {@link State}.
     */
    State getCurrentState();

    /**
     * Sets the current {@link State}. This is only meant for internal use. Don't call it directly!
     *
     * @param state the new current {@link State}.
     */
    void setCurrentState(State state);

    /**
     * Returns the value of the attribute with the specified key or <code>null</code>if not found.
     *
     * @param key the key.
     * @return the value or <code>null</code>.
     */
    Object getAttribute(Object key);

    /**
     * Sets the value of the attribute with the specified key.
     *
     * @param key the key.
     * @param value the value.
     */
    void setAttribute(Object key, Object value);

}
