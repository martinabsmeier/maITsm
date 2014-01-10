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

/**
 * Exception used internally by {@link StateControl}.
 *
 * @author Martin Absmeier
 */
public class BreakAndCallException extends BreakException {
    private static final long serialVersionUID = -5973306926764652458L;

    private final String stateId;

    private final String returnToStateId;

    private final boolean now;

    public BreakAndCallException(String stateId, boolean now) {
        this(stateId, null, now);
    }

    public BreakAndCallException(String stateId, String returnToStateId, boolean now) {
        if (stateId == null) {
            throw new IllegalArgumentException("stateId");
        }
        this.stateId = stateId;
        this.returnToStateId = returnToStateId;
        this.now = now;
    }

    public boolean isNow() {
        return now;
    }

    public String getStateId() {
        return stateId;
    }

    public String getReturnToStateId() {
        return returnToStateId;
    }
}
