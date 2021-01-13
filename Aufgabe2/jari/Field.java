import java.util.ArrayList;

public class Field {
    public Type type() {
        return type;
    }

    public void setGras() {
        type = Type.Gras;
        this.tentAble = 0;
    }

    public void setTent(Field tree) {
        if (board.getRow(this).getTents() == board.getRow(this).getDemand()) {
            System.out.printf("Row: %s - %s/%s%n", board.getRow(this).getPosition(), board.getRow(this).getTents(), board.getRow(this).getDemand());
        }
        if (board.getColumn(this).getTents() == board.getColumn(this).getDemand()) {
            System.out.printf("Column: %s - %s/%s%n", board.getColumn(this).getPosition(), board.getColumn(this).getTents(), board.getColumn(this).getDemand());
        }
        type = Type.Tent;
        this.partner = tree;
        this.tentAble = 1;
        tree.partner = this;
    }

    public Field getPartner() {
        return partner;
    }

    public void otherUnknownFields(Field tree, int unknownFields) {
        if (unknownFields == 1) {
//            setTent(tree);
            this.tentAble = 1;
        } else {
            this.tentAble = 1 - ((1 - this.tentAble) * (1 - 1 / (double) unknownFields));
        }
    }

    public double getTentAble() {
        return tentAble;
    }

    public void resetTentAble() {
        tentAble = 0.0;
    }

    public ArrayList<Field> getNearFreeTrees() {
        ArrayList<Field> trees = new ArrayList<>();
        if (left() != null && left().type == Type.Tree && left().partner == null) trees.add(left());
        if (up() != null && up().type == Type.Tree && up().partner == null) trees.add(up());
        if (right() != null && right().type == Type.Tree && right().partner == null) trees.add(right());
        if (down() != null && down().type == Type.Tree && down().partner == null) trees.add(down());
        return trees;
    }

    private Type type;
    private final Board board;
    private double tentAble = 0.0;
    private Field partner = null;

    public Field(Board board, Type type) {
        this.board = board;
        this.type = type;
    }

    public Field up() {
        Field lastField = null;
        for (Field field : board.getColumn(this).getFields()) {
            if (field == this) return lastField;
            lastField = field;
        }
        return null;
    }

    public Field down() {
        boolean nextField = false;
        for (Field field : board.getColumn(this).getFields()) {
            if (nextField) return field;
            if (field == this) nextField = true;
        }
        return null;
    }

    public Field left() {
        Field lastField = null;
        for (Field field : board.getRow(this).getFields()) {
            if (field == this) return lastField;
            lastField = field;
        }
        return null;
    }

    public Field right() {
        boolean nextField = false;
        for (Field field : board.getRow(this).getFields()) {
            if (nextField) return field;
            if (field == this) nextField = true;
        }
        return null;
    }

    @Override
    public String toString() {
        switch (type) {
            case Tent -> {
                return "X";
            }
            case Tree -> {
                return "█";
            }
            case Gras -> {
                return "_";
            }
            default -> {
                if (tentAble == 1) {
                    return "E"; // TODO ERROR
                } else {
                    return String.valueOf((int) (tentAble * 10));
                }
            }
        }
    }
}
