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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ma.it.common.sm.annotation.State;
import de.ma.it.common.sm.annotation.Transition;
import de.ma.it.common.sm.annotation.Transitions;

/**
 * TODO Insert description
 *
 * @author Martin Absmeier
 */
public class TapeDeckManager {

    @State
    public static final String STATE_EMPTY = "Empty";

    @State
    public static final String STATE_LOADED = "Loaded";

    @State
    public static final String STATE_PLAYING = "Playing";

    @State
    public static final String STATE_PAUSED = "Paused";

    private static final Logger LOG = LoggerFactory.getLogger(TapeDeckManager.class);
    
    /** Constructor of TapeDeckManager */
    public TapeDeckManager() {
        super();
    }

    @Transition(on = "load", in = STATE_EMPTY, next = STATE_LOADED)
    public void loadTape(String nameOfTape) {
        LOG.info("Tape '" + nameOfTape + "' loaded");
    }

    @Transitions({ 
        @Transition(on = "play", in = STATE_LOADED, next = STATE_PLAYING),
        @Transition(on = "play", in = STATE_PAUSED, next = STATE_PLAYING) 
    })
    public void playTape() {
        LOG.info("Playing tape");
    }

    @Transition(on = "pause", in = STATE_PLAYING, next = STATE_PAUSED)
    public void pauseTape() {
        LOG.info("Tape paused");
    }

    @Transition(on = "stop", in = STATE_PLAYING, next = STATE_LOADED)
    public void stopTape() {
        LOG.info("Tape stopped");
    }

    @Transition(on = "eject", in = STATE_LOADED, next = STATE_EMPTY)
    public void ejectTape() {
        LOG.info("Tape ejected");
    }
}
