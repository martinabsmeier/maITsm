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

import de.ma.it.common.sm.context.StateContext;

/**
 * Default {@link EventFactory} implementation. Uses the method's name as
 * event id.
 * 
 * @author Martin Absmeier
 */
public class DefaultEventFactory implements EventFactory {

    public Event create(StateContext context, Method method, Object[] arguments) {
        return new Event(method.getName(), context, arguments);
    }

}
