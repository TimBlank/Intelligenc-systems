package src;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;
import java.util.List;

public class Player implements Steppable {
    public String color;
    public String name;


    public Player(String color, String name) {
        this.color = color;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "color='" + color + " " + Game.ANSI_RESET + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /*
    public String getBoard() {
        StringBuilder board = new StringBuilder();
        for (int i = this.goals.size() - 1; i >= 0; i--) {
            board.append(this.goals.get(i).getChar()).append("|");
        }
        int home = 0;
        for (Stone stone : this.stones) {
            if (stone.field == null) {
                home++;
            }
        }
        board.append(color).append(home).append(Game.ANSI_RESET).append("|");
        for (Field field : this.fields) {
            board.append(field.getChar()).append("|");
        }
        return board.toString();
    }
    */


    @Override
    public void step(SimState state) {
        Game game = (Game) state;
        boolean itsYourTurn = true;
        int rollsThisTurn = 0;
        while(itsYourTurn) {
            List<Field> possibleMoves = game.findAllStones(this);
            int roll = state.random.nextInt(6) + 1;
            List<Field> occupiedFields = game.findAllStones(this);
            rollsThisTurn++;
            //No pieces outside house
            if(game.findAllStones(this).size() == 0) {
                //Then you can roll twice again
                if(roll != 6 && rollsThisTurn < 3) {
                    continue; //Roll again!
                }
            }

            //Has one in the house, rolled a 6 and doesn't block itself
            if (occupiedFields.size() < Game.STONES && roll == 6 && game.findHouseExit(this).occupation != this) {
                game.findHouseExit(this).occupation = this;
                continue; //Do another roll!
            }

            //Has a stone on the House Exit which needs to be moved
            if(game.findHouseExit(this).occupation == this) {
                Field f = game.findHouseExit(this);
                while(!game.validMove(roll, f, this)) {
                    f = f.getNFurtherField(roll, this); //Find the next piece we have to move to clear house
                }
                game.moveStone(roll, f, this);
                itsYourTurn = false;
                break;
            }

            //Find all ways to kill another stone of another player
            List<Field> killMoves = new ArrayList<>();
            for(int i=0; i< occupiedFields.size(); i++) {
                if(occupiedFields.get(i).getNFurtherField(roll, this) != null && occupiedFields.get(i).getNFurtherField(roll, this).occupation != this) {
                    killMoves.add(occupiedFields.get(i));
                }
            }
            if(killMoves.size() > 0) {
                possibleMoves = killMoves;
            }

            for(int i=0; i<possibleMoves.size(); i++) {
                if(!game.validMove(roll, possibleMoves.get(i), this)) {
                    possibleMoves.remove(i); //Remove invalid moves
                    i--;
                }
            }

            //No turn possible
            if(possibleMoves.size() == 0) {
                itsYourTurn = false;
                break;
            }

            Field chosenMove = chooseMove(roll,possibleMoves, game);
            game.moveStone(roll, chosenMove, this);

            //Has at least one thing out of house



        }
        System.out.println(game.findAllStones(this));

    }

    public Field chooseMove(int roll, List<Field> possibleMoves, Game game) {


        return possibleMoves.get(0);
    }
}
