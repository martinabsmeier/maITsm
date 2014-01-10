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

/**
 * {@link StateContextLookup} implementation which always returns the same
 * {@link StateContext} instance.
 *
 * @author Martin Absmeier
 */
public class SingletonStateContextLookup implements StateContextLookup {
    private final StateContext context;

    /**
     * Creates a new instance which always returns the same 
     * {@link DefaultStateContext} instance.
     */
    public SingletonStateContextLookup() {
        context = new DefaultStateContext();
    }

    /**
     * Creates a new instance which uses the specified {@link StateContextFactory}
     * to create the single instance.
     * 
     * @param contextFactory the {@link StateContextFactory} to use to create 
     *        the singleton instance.
     */
    public SingletonStateContextLookup(StateContextFactory contextFactory) {
        if (contextFactory == null) {
            throw new IllegalArgumentException("contextFactory");
        }
        context = contextFactory.create();
    }

    public StateContext lookup(Object[] eventArgs) {
        return context;
    }
}
