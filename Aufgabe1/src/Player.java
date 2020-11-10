package src;

import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;
import java.util.List;

public class Player implements Steppable {
    public String color;
    public String name;
    public int order;
    /**
     * Players goal Fields.
     */
    public List<Field> goals = new ArrayList<>();
    /**
     * These are the fields on this player side or ?
     */
    public List<Field> fields = new ArrayList<>();
    public int homeStones = 0;

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
                ", homeStones='" + homeStones + '\'' +
                '}';
    }

    public void afterSetup(int playerStones) {
        this.homeStones = playerStones;
    }

    public String getBoard() {
        StringBuilder board = new StringBuilder();
        for (int i = this.goals.size() - 1; i >= 0; i--) {
            board.append(this.goals.get(i).getChar()).append("|");
        }
        board.append(color).append(homeStones).append(Game.ANSI_RESET).append("|");
        for (Field field : this.fields) {
            board.append(field.getChar()).append("|");
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
        boolean itsYourTurn = true;
        int rollsThisTurn = 0;

        //Since the player wasn't done, we get to go again next time!
        game.schedule.scheduleOnce(this, this.order);
        while (itsYourTurn) {
            List<Field> possibleMoves = game.findAllStones(this);

            possibleMoves = game.removeDoneStones(this, possibleMoves);

            int roll = state.random .nextInt(6) + 1;
            System.out.print(color + " " + Game.ANSI_RESET + roll);
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
            if (killMoves.size() > 0) {
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
//        System.out.println(game.findAllStones(this));

    }

    public Field chooseMove(int roll, List<Field> possibleMoves, Game game) {


        return possibleMoves.get(0);
    }
}
