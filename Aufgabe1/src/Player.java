package src;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.ArrayList;
import java.util.List;

public class Player implements Steppable {
    public String color;
    public String name;
    public List<Stone> stones = new ArrayList<>();
    public List<Field> goals = new ArrayList<>();
    public List<Field> fields = new ArrayList<>();

    public Player(String color, String name) {
        this.color = color;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "color='" + color + " " + Game.ANSI_RESET + '\'' +
                ", name='" + name + '\'' +
                ", stones='" + stones.size() + '\'' +
                '}';
    }

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

    @Override
    public void step(SimState state) {
        System.out.println("Hi, step. Rolled: "+(state.random.nextInt(6)+1));
    }
}
