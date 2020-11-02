package Aufgabe1.src;

import java.util.ArrayList;
import java.util.List;

public class Player {
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
        for (Field goal : this.goals) {
            board.append(goal.getChar()).append("|");
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
}
