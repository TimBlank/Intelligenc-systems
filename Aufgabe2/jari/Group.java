package jari;

import java.util.ArrayList;

public class Group {
    public int getDemand() {
        return demand;
    }

    public int getTents() {
        int tents = 0;
        for (Field field : fields) {
            if (field.type() == Type.Tent) tents++;
        }
        return tents;
    }

    public int getPossibleTents() {
        int tents = 0;
        for (Field field : fields) {
            if (field.type() == Type.Tent) tents++;
            if (field.type() == Type.unknown) tents++;
        }
        return tents;
    }

    public double getPossibleTentsPossibility() {
        double tents = 0.0;
        for (Field field : fields) {
            if (field.type() == Type.Tent) tents++;
            if (field.type() == Type.unknown) tents += field.getTentAble();
        }
        return tents;
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    private int demand;
    private int position;
    private ArrayList<Field> fields = new ArrayList<>();

    public Group(int demand, int position) {
        this.demand = demand;
        this.position = position;
    }
}
