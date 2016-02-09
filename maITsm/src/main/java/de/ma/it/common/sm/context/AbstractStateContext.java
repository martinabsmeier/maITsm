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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.ma.it.common.sm.State;

/**
 * Abstract {@link StateContext} which uses a {@link Map} to store the attributes.
 *
 * @author Martin Absmeier
 */
public abstract class AbstractStateContext implements StateContext {

	private static final long serialVersionUID = -9163548435437416302L;

	private State currentState = null;

    private Map<Object, Object> attributes = null;

    @Override
    public Object getAttribute(Object key) {
        return getAttributes().get(key);
    }

    @Override
    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void setAttribute(Object key, Object value) {
        getAttributes().put(key, value);
    }

    @Override
    public void setCurrentState(State state) {
        currentState = state;
    }

    protected Map<Object, Object> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<Object, Object>();
        }
        return attributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("currentState", currentState).append("attributes", attributes).toString();
    }

}
