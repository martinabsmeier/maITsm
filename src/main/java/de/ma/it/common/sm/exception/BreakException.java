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

import de.ma.it.common.sm.StateControl;
import de.ma.it.common.sm.StateMachine;

/**
 * The base exception of the exceptions thrown by the methods in the 
 * {@link StateControl} class. If you use any of the {@link StateControl} 
 * methods to change the execution of a {@link StateMachine} you must make sure
 * that exceptions of this type aren't caught and swallowed by your code.
 * 
 * @author Martin Absmeier
 */
public class BreakException extends RuntimeException {
    private static final long serialVersionUID = -1898782004087949199L;

    protected BreakException() {
        super();
    }
}
