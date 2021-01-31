package src;

import java.util.List;

/**
 * This clown moves the figure that has moved the least amount. Ideally, it would move all figures evenly.
 */
public class LastStoneMover extends Player {
    public LastStoneMover(String color, String name, int order) {
        super(color, name, order);
    }

    @Override
    public Field chooseMove(int roll, List<Field> possibleMoves, Game game) {
        int worstIndex = -1;
        int worstDistance = -1;
        for(int i=0; i< possibleMoves.size(); i++) {
            if(worstDistance < possibleMoves.get(i).getDistanceToGoal(this)) {
                worstDistance = possibleMoves.get(i).getDistanceToGoal(this);
                worstIndex = i;
            }
        }
        return possibleMoves.get(worstIndex);
    }
}
