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
package de.ma.it.common.sm.examples.tp;

import de.ma.it.common.sm.StateMachine;
import de.ma.it.common.sm.StateMachineFactory;
import de.ma.it.common.sm.StateMachineProxyBuilder;
import de.ma.it.common.sm.annotation.Transition;

/**
 * TODO Insert description
 *
 * @author Martin Absmeier
 */
public class TapeDeckTest {

    public static void main(String[] args) {        
        TapeDeckManager manager = new TapeDeckManager();
        StateMachineFactory factory = StateMachineFactory.getInstance(Transition.class);
        StateMachine sm = factory.create(TapeDeckManager.STATE_EMPTY, manager);
        TapeDeck deck = new StateMachineProxyBuilder().create(TapeDeck.class, sm);

        deck.load("The Knife - Silent Shout");
        deck.play();
        deck.pause();
        deck.play();
        deck.stop();
        deck.eject();
    }

}
