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
package de.ma.it.common.sm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.ma.it.common.sm.StateMachine;
import de.ma.it.common.sm.event.Event;

/**
 * Annotation used on methods to indicate that the method handles a specific kind of event when in a
 * specific state.
 * 
 * @author Martin Absmeier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@TransitionAnnotation(Transitions.class)
public @interface Transition {

	public static final String SELF = "__self__";

	/**
	 * Specifies the ids of one or more events handled by the annotated method. If not specified the
	 * handler method will be executed for any event.
     * @return 
	 */
	String[] on() default Event.WILDCARD_EVENT_ID;

	/**
	 * The id of the state or states that this handler applies to. Must be specified.
     * @return 
	 */
	String[] in();

	/**
	 * The id of the state the {@link StateMachine} should move to next after executing the
	 * annotated method. If not specified the {@link StateMachine} will remain in the same state.
     * @return 
	 */
	String next() default SELF;

	/**
	 * The weight used to order handler annotations which match the same event in the same state.
	 * Transitions with lower weight will be matched first. The default weight is 0.
     * @return 
	 */
	int weight() default 0;

}
