

public class Stone {
    public Player player;
    public Field field;

    public Stone(Player player, Field field) {
        this.player = player;
        this.field = field;
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Stone{" +
                "player=" + player.toString() +
                ", field=" + field +
                '}';
    }
}
