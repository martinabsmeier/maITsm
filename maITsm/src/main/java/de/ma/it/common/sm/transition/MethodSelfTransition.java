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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ma.it.common.sm.State;
import de.ma.it.common.sm.context.StateContext;

/**
 * {@link SelfTransition} which invokes a {@link Method}. The {@link Method} can
 * have zero or any number of StateContext and State regarding order
 * <p>
 * Normally you wouldn't create instances of this class directly but rather use the
 * {@link SelfTransition} annotation to define the methods which should be used as
 * transitions in your state machine and then let {@link StateMachineFactory} create a
 * {@link StateMachine} for you.
 * </p>
 *
 * @author Martin Absmeier
 */
public class MethodSelfTransition extends AbstractSelfTransition {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodTransition.class);

    private Method method;

    private final Object target;

    private static final Object[] EMPTY_ARGUMENTS = new Object[0];

    public MethodSelfTransition(Method method, Object target) {
        super();
        this.method = method;
        this.target = target;
    }

    /**
     * Creates a new instance
     * 
     * @param method the target method.
     * @param target the target object.
     */
    public MethodSelfTransition(String methodName, Object target) {

        this.target = target;

        Method[] candidates = target.getClass().getMethods();
        Method result = null;
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i].getName().equals(methodName)) {
                if (result != null) {
                    throw new AmbiguousMethodException(methodName);
                }
                result = candidates[i];
            }
        }

        if (result == null) {
            throw new NoSuchMethodException(methodName);
        }

        this.method = result;

    }

    /**
     * Returns the target {@link Method}.
     * 
     * @return the method.
     */
    public Method getMethod() {
        return method;
    }

    public boolean doExecute(StateContext stateContext, State state) {
        Class<?>[] types = method.getParameterTypes();

        if (types.length == 0) {
            invokeMethod(EMPTY_ARGUMENTS);
            return true;
        }

        if (types.length > 2) {
            return false;
        }

        Object[] args = new Object[types.length];

        int i = 0;
        if (types[i].isAssignableFrom(StateContext.class)) {
            args[i++] = stateContext;
        }
        if (i < types.length && types[i].isAssignableFrom(State.class)) {
            args[i++] = state;
        }

        invokeMethod(args);

        return true;
    }

    private void invokeMethod(Object[] arguments) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Executing method " + method + " with arguments " + Arrays.asList(arguments));
            }
            method.invoke(target, arguments);
        } catch (InvocationTargetException ite) {
            if (ite.getCause() instanceof RuntimeException) {
                throw (RuntimeException) ite.getCause();
            }
            throw new MethodInvocationException(method, ite);
        } catch (IllegalAccessException iae) {
            throw new MethodInvocationException(method, iae);
        }
    }

}
