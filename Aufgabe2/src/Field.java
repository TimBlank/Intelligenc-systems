import java.util.ArrayList;

public class Field {

    ArrayList<ArrayList<String>> field;


    //The Tents in the column
    int[] columnDemand;
    //Tents in the row
    int[] rowDemand;


    //A Field is a 2D Array with singular Characters signifying "solved" content.
    //Tent T
    //Tree (N, S, E, W) when connected to it's Tent, t when unsolved. According to rules, each tree has one attached.
    //Grass G , something that is never a tent/tree
    //Unspecified " " or pretty much whatever


    public Field() {
        field = new ArrayList<>();

    }

    public void showField() {
        System.out.println("Field!");
        System.out.print("x ");
        for(int i = 0; i< columnDemand.length; i++) {
            System.out.print(columnDemand[i]+ " ");
        }
        System.out.println();
        for(int y= 0; y< rowDemand.length; y++) {
            System.out.print(rowDemand[y]+ " ");
            for(int x = 0; x <field.size(); x++) {
                System.out.print(field.get(x).get(y)+ " ");
            }
            System.out.println();
        }
    }

    /**
     * If a square has no adjacent tree, it cannot contain a Tent, so this Method is useful
     * @param x
     * @param y
     * @return
     */
    public boolean hasAdjacentTree(int x, int y) {
        if(x > 0 && isTree(x-1,y)) {
            return true;
        }
        if(isTree(x+1, y)) {
            return true;
        }
        if(y > 0 && isTree(x, y-1)) {
            return true;
        }
        if(isTree(x, y+1)) {
            return true;
        }
        return false;
    }

    /**
     * If a given field is a Tree. Performs boundary check
     * @param x
     * @param y
     * @return
     */
    public boolean isTree(int x, int y) {
        if(x < 0 || x > field.size() || y < 0 || y > field.get(x).size()) {
            return false;
        }
        String c = field.get(x).get(y);
        return c.equals("N") || c.equals("S") || c.equals("E") || c.equals("W") || c.equals("t");
    }

}
