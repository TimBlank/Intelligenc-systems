package src;

import java.util.List;

public class AggressiveAgent extends Player {

    public AggressiveAgent(String color, String name, int order) {
        super(color, name, order);
    }

    // extends the ChooseMove methode for the AggressiveAgent Class
    public Field chooseMove(int roll, List<Field> possibleMoves, Game game)
    {
        possibleMoves = mostAggresiveMove(possibleMoves, game);

        //TODO Liste Sortieren
        return possibleMoves.get(0);
    }

    public List<Field> mostAggresiveMove (List<Field> possibleMoves, Game game)
    {
        possibleMoves = game.enemyDistance(possibleMoves, game);
        return possibleMoves;
    }
}
