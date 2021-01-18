package src;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.*;

public class Player implements Steppable {
    public String color;
    public String name;
    public int rounds = 0;
    public int order;

    /**
     * Players goal Fields.
     */
    public List<Field> goals = new ArrayList<>();
    /**
     * These are the fields on this player side or ?
     */
    public List<Field> fields = new ArrayList<>();

    public boolean hasWon = false;

    public Player(String color, String name, int order) {
        this.color = color;
        this.name = name;
        this.order = order;

    }

    @Override
    public String toString() {
        return "Player{" +
                "color='" + color + " " + Game.ANSI_RESET + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getBoard(Game game) {
        StringBuilder board = new StringBuilder();
        for (int i = this.goals.size() - 1; i >= 0; i--) {
            board.append(this.goals.get(i).getChar()).append("|");
//            board.append(this.goals.get(i).getChar()).append(this.goals.get(i).formerField.fieldId + ":" + this.goals.get(i).fieldId).append("|");
        }
        board.append(color).append(Game.STONES - game.findAllStones(this).size()).append(Game.ANSI_RESET).append("|");
        for (Field field : this.fields) {
            board.append(field.getChar()).append("|");
//            board.append(field.getChar()).append(field.formerField.fieldId + ":" + field.fieldId).append("|");
        }
        return board.toString();
    }

    @Override
    public void step(SimState state) {
        //A Player who has Won doesn't get to play anymore FeelsBadMan
        if (this.hasWon) {
            return;
        }

        Game game = (Game) state;
        this.rounds++;
        boolean itsYourTurn = true;
        int rollsThisTurn = 0;

        //Since the player wasn't done, we get to go again next time!
        game.schedule.scheduleOnce(this, this.order);
        while (itsYourTurn) {
            List<Field> possibleMoves = game.findAllStones(this);

            possibleMoves = game.removeDoneStones(this, possibleMoves);

            int roll = state.random.nextInt(6) + 1;
//            System.out.print(color + " " + Game.ANSI_RESET + roll);
            List<Field> occupiedFields = game.findAllStones(this);
            rollsThisTurn++;

            // All Stones outside house, but all are done means we have won!
            if (game.findAllStones(this).size() == Game.STONES && possibleMoves.size() == 0) {
                this.hasWon = true;
                game.winners.add(this);
                return;
            }

            //No pieces outside house OR all stones outside are done
            if (game.findAllStones(this).size() == 0 || possibleMoves.size() == 0) {
                //Then you can roll twice again
                if (roll != 6 && rollsThisTurn < 3) {
                    continue; //Roll again!
                }
            }

            //Has one in the house, rolled a 6 and doesn't block itself
            if (occupiedFields.size() < Game.STONES && roll == 6 && game.findHouseExit(this).occupation != this) {
                game.findHouseExit(this).occupation = this;
                continue; //Do another roll!
            }

            //Has a stone on the House Exit which needs to be moved
            if (game.findHouseExit(this).occupation == this) {
                Field f = game.findHouseExit(this);
                while (!game.validMove(roll, f, this)) {
                    f = f.getNFurtherField(roll, this); //Find the next piece we have to move to clear house
                }
                game.moveStone(roll, f, this);
                itsYourTurn = false;
                break;
            }

            //Find all ways to kill another stone of another player
            List<Field> killMoves = new ArrayList<>();
            for (int i = 0; i < occupiedFields.size(); i++) {
                if (occupiedFields.get(i).getNFurtherField(roll, this) != null && occupiedFields.get(i).getNFurtherField(roll, this).occupation != this) {
                    killMoves.add(occupiedFields.get(i));
                }
            }
            //If we must kill, restrict possible moves with killing moves, if any
            if (killMoves.size() > 0 && Game.MUST_KILL) {
                possibleMoves = killMoves;
            }

            for (int i = 0; i < possibleMoves.size(); i++) {
                if (!game.validMove(roll, possibleMoves.get(i), this)) {
                    possibleMoves.remove(i); //Remove invalid moves
                    i--;
                }
            }

            //No turn possible
            if (possibleMoves.size() == 0) {
                itsYourTurn = false;
                break;
            }

            Field chosenMove = chooseMove(roll, possibleMoves, game);
            game.moveStone(roll, chosenMove, this);
            if (roll != 6) {
                itsYourTurn = false;
            }


            //Has at least one thing out of house


        }

        //System.out.println(((Game) state).getBoard());
        //System.out.print("");

    }

    public Field chooseMove(int roll, List<Field> possibleMoves, Game game) {
        return possibleMoves.get(game.random.nextInt(possibleMoves.size()));
    }

    private HashMap<Field, Integer> multiplyStrength(HashMap<Field, Integer> moveStrength) {
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            entry.setValue(entry.getValue() * 2);
        }
        return moveStrength;
    }

    private HashMap<Field, Integer> agressiveMove(Game game, HashMap<Field, Integer> moveStrength, int roll) {
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            int furtherPlayers = 0;
            for (int i = 1; i <= 6; i++) {
                Field furtherField = entry.getKey().getNFurtherField(roll + i, this);
                if (furtherField != null && furtherField.occupation != null && furtherField.occupation != this) {
                    furtherPlayers++;
                }
            }
//            if (furtherPlayers>1) {
//                System.out.println("furtherPlayers: " + furtherPlayers + " Field: " + entry.getKey() + " distance: " + entry.getKey().getDistanceToGoal(this));
//                System.out.print("");
//            }
            entry.setValue(entry.getValue() + furtherPlayers);
        }
        return moveStrength;
    }

    private HashMap<Field, Integer> defensiveMove(Game game, HashMap<Field, Integer> moveStrength, int roll) {
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            int formerPlayers = 0;
            for (int i = 1; i <= 6; i++) {
                Player occupation = entry.getKey().getNFurtherField(roll - i, this).occupation;
                if (occupation != null && occupation != this) {
                    formerPlayers++;
                }
            }
            entry.setValue(entry.getValue() + 6 - formerPlayers);
        }
        return moveStrength;
    }

    private HashMap<Field, Integer> worstStoneMove(HashMap<Field, Integer> moveStrength) {
//        if (moveStrength.size()>1){
//            System.out.println(moveStrength);
//        }
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            int distanceToGoal = entry.getKey().getDistanceToGoal(this);
            // TODO: Division mit mehr als zwei?
            entry.setValue(entry.getValue() + distanceToGoal / 2);
        }
//        if (moveStrength.size()>1){
//            System.out.println(moveStrength);
//            System.out.print("");
//        }
        return moveStrength;
    }

    private HashMap<Field, Integer> bestStoneMove(Game game, HashMap<Field, Integer> moveStrength) {
//        if (moveStrength.size()>1){
//            System.out.println(moveStrength);
//        }
        for (Map.Entry<Field, Integer> entry : moveStrength.entrySet()) {
            int distanceToGoal = entry.getKey().getDistanceToGoal(this);
            // TODO: Division mit mehr als zwei?
            entry.setValue(entry.getValue() + (Game.DISTANCE * game.players.length - distanceToGoal) / 2);
        }
//        if (moveStrength.size()>1){
//            System.out.println(moveStrength);
//            System.out.print("");
//        }
        return moveStrength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return color.equals(player.color) &&
                name.equals(player.name);
    }
}
