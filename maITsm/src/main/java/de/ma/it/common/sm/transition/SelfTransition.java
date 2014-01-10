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
package de.ma.it.common.sm.transition;

import de.ma.it.common.sm.State;
import de.ma.it.common.sm.context.StateContext;

/**
 * The interface implemented by classes which need to react on entering
 * a certain states.
 *
 * @author Martin Absmeier
 */
public interface SelfTransition {
    /**
     * Executes this {@link SelfTransition}.
     * 
     * @return <code>true</code> if the {@link SelfTransition} was executed,
     *         <code>false</code> otherwise.
     */

    boolean execute(StateContext stateContext, State state);

}
