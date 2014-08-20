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
 * Abstract {@link StateContextLookup} implementation. The {@link #lookup(Object[])} method will
 * loop through the event arguments and call the {@link #supports(Class)} method for each of them.
 * The first argument that this method returns <code>true</code> for will be passed to the abstract
 * {@link #lookup(Object)} method which should try to extract a {@link StateContext} from the
 * argument. If none is found a new {@link StateContext} will be created and stored in the event
 * argument using the {@link #store(Object, StateContext)} method.
 * 
 * @author Martin Absmeier
 */
public abstract class AbstractStateContextLookup implements StateContextLookup {

	private final StateContextFactory contextFactory;

	/**
	 * Creates a new instance which uses the specified {@link StateContextFactory} to create
	 * {@link StateContext} objects.
	 * 
	 * @param contextFactory
	 *            the factory.
	 */
	public AbstractStateContextLookup(StateContextFactory contextFactory) {
		if (contextFactory == null) {
			throw new IllegalArgumentException("contextFactory");
		}
		this.contextFactory = contextFactory;
	}

    @Override
	public StateContext lookup(Object[] eventArgs) {
        for (Object eventArg : eventArgs) {
            if (supports(eventArg.getClass())) {
                StateContext sc = lookup(eventArg);
                if (sc == null) {
                    sc = contextFactory.create();
                    store(eventArg, sc);
                }
                return sc;
            }
        }
		return null;
	}

	/**
	 * Extracts a {@link StateContext} from the specified event argument which is an instance of a
	 * class {@link #supports(Class)} returns <code>true</code> for.
	 * 
	 * @param eventArg
	 *            the event argument.
	 * @return the {@link StateContext}.
	 */
	protected abstract StateContext lookup(Object eventArg);

	/**
	 * Stores a new {@link StateContext} in the specified event argument which is an instance of a
	 * class {@link #supports(Class)} returns <code>true</code> for.
	 * 
	 * @param eventArg
	 *            the event argument.
	 * @param context
	 *            the {@link StateContext} to be stored.
	 */
	protected abstract void store(Object eventArg, StateContext context);

	/**
	 * Must return <code>true</code> for any {@link Class} that this {@link StateContextLookup} can
	 * use to store and lookup {@link StateContext} objects.
	 * 
	 * @param c
	 *            the class.
	 * @return <code>true</code> or <code>false</code>.
	 */
	protected abstract boolean supports(Class<?> c);

}
