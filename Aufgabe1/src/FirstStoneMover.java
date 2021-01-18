package src;

import java.util.List;

public class FirstStoneMover extends  Player{
    public FirstStoneMover(String color, String name, int order) {
        super(color, name, order);
    }

    @Override
    public Field chooseMove(int roll, List<Field> possibleMoves, Game game) {
        int bestIndex = -1;
        int bestDistance = 1000;
        for(int i=0; i< possibleMoves.size(); i++) {
            if(bestDistance > possibleMoves.get(i).getDistanceToGoal(this)) {
                bestDistance = possibleMoves.get(i).getDistanceToGoal(this);
                bestIndex = i;
            }
        }
        return possibleMoves.get(bestIndex);
    }
}
