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
        while(itsYourTurn) {
            int roll = state.random.nextInt(6) + 1;
            //Has one in the house, rolled a 6 and doesn't block itself
            if (game.findAllStones(this).size() < Game.STONES && roll == 6 && game.findHouseExit(this).occupation != this) {
                game.findHouseExit(this).occupation = this;
                continue; //Do another roll!
            }
            //Has at least one thing out of house
            if(game.findAllStones(this).size() > 0) {
                List<Field> myStones = game.findAllStones(this);
                game.moveStone(roll, myStones.get(0), this);
                itsYourTurn = false;
                break;
            }


        }
        System.out.println(game.findAllStones(this));

    }
}
