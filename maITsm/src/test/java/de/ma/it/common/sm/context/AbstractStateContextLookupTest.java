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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Tests {@link AbstractStateContextLookup}.
 *
 * @author Martin Absmeier
 */
public class AbstractStateContextLookupTest extends TestCase {

    @Test
    public void testLookup() throws Exception {
        Map<String, StateContext> map = new HashMap<String, StateContext>();
        AbstractStateContextLookup lookup = new AbstractStateContextLookup(new DefaultStateContextFactory()) {
            protected boolean supports(Class<?> c) {
                return Map.class.isAssignableFrom(c);
            }

            @SuppressWarnings("unchecked")
            protected StateContext lookup(Object eventArg) {
                Map<String, StateContext> map = (Map<String, StateContext>) eventArg;
                return map.get("context");
            }

            @SuppressWarnings("unchecked")
            protected void store(Object eventArg, StateContext context) {
                Map<String, StateContext> map = (Map<String, StateContext>) eventArg;
                map.put("context", context);
            }
        };
        Object[] args1 = new Object[] { new Object(), map, new Object() };
        Object[] args2 = new Object[] { map, new Object() };
        StateContext sc = lookup.lookup(args1);
        assertSame(map.get("context"), sc);
        assertSame(map.get("context"), lookup.lookup(args1));
        assertSame(map.get("context"), lookup.lookup(args2));
    }

}
