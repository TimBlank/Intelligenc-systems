package src;

import sim.engine.SimState;

/**
 * A player that immediately quits the game
 */
public class LosesInstantly extends Player {

    public LosesInstantly(String color, String name, int order) {
        super(color, name, order);
    }
    @Override
    public void step(SimState state) {
        return;
    }
}
