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
package de.ma.it.common.sm;

import de.ma.it.common.sm.event.Event;
import de.ma.it.common.sm.exception.BreakAndCallException;
import de.ma.it.common.sm.exception.BreakAndContinueException;
import de.ma.it.common.sm.exception.BreakAndGotoException;
import de.ma.it.common.sm.exception.BreakAndReturnException;
import de.ma.it.common.sm.transition.Transition;

/**
 * Allows for programmatic control of a state machines execution.
 * <p>
 * The <code>*Now()</code> family of methods move to a new {@link State} immediately and let the new {@link State} handle the current
 * {@link Event}. The <code>*Next()</code> family on the other hand let the new {@link State} handle the next {@link Event} which is
 * generated which make these method the programmatic equivalent of using the {@link de.ma.it.common.sm.annotation.Transition} annotation.
 * </p>
 * <p>
 * Using the <code>breakAndCall*()</code> and <code>breakAndReturn*</code> methods one can create sub state machines which behave very much
 * like sub routines. When calling a state the current state (or the specified <code>returnTo</code> state) will be pushed on a stack. When
 * returning from a state the last pushed state will be popped from the stack and used as the new state.
 * </p>
 * 
 * @author Martin Absmeier
 */
public class StateControl {

	/**
	 * Breaks the execution of the current {@link Transition} and tries to find another {@link Transition} with higher weight or a
	 * {@link Transition} of a parent {@link State} which can handle the current {@link Event}.
	 */
	public static void breakAndContinue() {
		throw new BreakAndContinueException();
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the {@link State} with the specified id handle the
	 * <strong>current</strong> {@link Event}.
	 * 
	 * @param state
	 *            the id of the {@link State} to go to.
	 */
	public static void breakAndGotoNow(String state) {
		throw new BreakAndGotoException(state, true);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the {@link State} with the specified id handle the
	 * <strong>next</strong> {@link Event}. Using this method is the programmatic equivalent of using the
	 * {@link de.ma.it.common.sm.annotation.Transition} annotation.
	 * 
	 * @param state
	 *            the id of the {@link State} to go to.
	 */
	public static void breakAndGotoNext(String state) {
		throw new BreakAndGotoException(state, false);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the {@link State} with the specified id handle the
	 * <strong>current</strong> {@link Event}. Before moving to the new state the current state will be recorded. The next call to
	 * {@link #breakAndReturnNow()} or {@link #breakAndReturnNext()} will return to the current state.
	 * 
	 * @param state
	 *            the id of the {@link State} to call.
	 */
	public static void breakAndCallNow(String state) {
		throw new BreakAndCallException(state, true);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the {@link State} with the specified id handle the
	 * <strong>next</strong> {@link Event}. Before moving to the new state the current state will be recorded. The next call to
	 * {@link #breakAndReturnNow()} or {@link #breakAndReturnNext()} will return to the current state.
	 * 
	 * @param state
	 *            the id of the {@link State} to call.
	 */
	public static void breakAndCallNext(String state) {
		throw new BreakAndCallException(state, false);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the {@link State} with the specified id handle the
	 * <strong>current</strong> {@link Event}. Before moving to the new state the current state will be recorded. The next call to
	 * {@link #breakAndReturnNow()} or {@link #breakAndReturnNext()} will return to the specified <code>returnTo</code> state.
	 * 
	 * @param state
	 *            the id of the {@link State} to call.
	 * @param returnTo
	 *            the id of the {@link State} to return to.
	 */
	public static void breakAndCallNow(String state, String returnTo) {
		throw new BreakAndCallException(state, returnTo, true);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the {@link State} with the specified id handle the
	 * <strong>next</strong> {@link Event}. Before moving to the new state the current state will be recorded. The next call to
	 * {@link #breakAndReturnNow()} or {@link #breakAndReturnNext()} will return to the specified <code>returnTo</code> state.
	 * 
	 * @param state
	 *            the id of the {@link State} to call.
	 * @param returnTo
	 *            the id of the {@link State} to return to.
	 */
	public static void breakAndCallNext(String state, String returnTo) {
		throw new BreakAndCallException(state, returnTo, false);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the last recorded {@link State} handle the <strong>current</strong>
	 * {@link Event}.
	 */
	public static void breakAndReturnNow() {
		throw new BreakAndReturnException(true);
	}

	/**
	 * Breaks the execution of the current {@link Transition} and lets the last recorded {@link State} handle the <strong>next</strong>
	 * {@link Event}.
	 */
	public static void breakAndReturnNext() {
		throw new BreakAndReturnException(false);
	}
}
