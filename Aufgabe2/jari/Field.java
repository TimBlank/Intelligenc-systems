package jari;

public class Field {
    public Type type() {
        return type;
    }

    public void setGras() {
        type = Type.Gras;
    }

    public void setTent(Field tree) {
        type = Type.Tent;
        this.partner = tree;
        tree.partner = this;
    }

    public Field getPartner() {
        return partner;
    }

    public void otherUnknownFields(Field tree, int unknownFields) {
        if (unknownFields > 1) {
            this.tentAble = 1 - ((1 - this.tentAble) * (1 - 1 / (double) unknownFields));
        } else {
            setTent(tree);
        }
    }

    public double getTentAble() {
        return tentAble;
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
                return String.valueOf((int) (tentAble * 10));
            }
        }
    }
}
