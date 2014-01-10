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

/**
 * {@link RuntimeException} equivalent of {@link java.lang.NoSuchMethodException}.
 * Thrown by the constructors in {@link MethodTransition} if no method by 
 * the specifed name can be found. 
 *
 * @author Martin Absmeier
 */
public class NoSuchMethodException extends RuntimeException {
    private static final long serialVersionUID = -8877979996892420479L;

    /**
     * Creates a new instance using the specified method name as message.
     * 
     * @param methodName the name of the method.
     */
    public NoSuchMethodException(String methodName) {
        super(methodName);
    }

}
