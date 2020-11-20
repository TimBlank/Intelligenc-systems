import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TreeSolver {


    public static String FILE  = "Aufgabe2/tents_trees_0.csv";

    public static void main(String[] args) {
        try {
            //Open File, make new Field
            Field treeField = new Field();
            BufferedReader bR = new BufferedReader(new FileReader(FILE));
            String line = bR.readLine();

            //Parse first line as demands for column
            String[] yDemand = line.split(",", -1);
            int[] actualYNumbers = new int[yDemand.length-1];
            for(int i=1; i< yDemand.length; i++) {
                actualYNumbers[i-1] = Integer.parseInt(yDemand[i]);
                treeField.field.add(new ArrayList<>());
            }
            treeField.columnDemand = actualYNumbers;

            //Parse each other line for 1st the demand for the row and trees
            ArrayList<Integer> xDemand = new ArrayList<>();
            line = bR.readLine();
            int y = 0; //Counter for row we are going down
            while(line != null) {
                String[] lineTokens = line.split(",", -1);
                //Save the demand for this row
                xDemand.add(Integer.parseInt(lineTokens[0]));
                for(int i=1; i< lineTokens.length; i++) {
                    //Add a coordinate for this
                    System.out.print(lineTokens[i]);
                    if(lineTokens[i].equals("t")) {
                        treeField.field.get(i-1).add("t");
                    } else {
                        treeField.field.get(i-1).add(" ");
                    }
                }
                System.out.println();
                line = bR.readLine();
                y++;
            }
            //Save row Demands
            treeField.rowDemand = new int[xDemand.size()];
            for(int i=0; i< xDemand.size(); i++) {
                treeField.rowDemand[i] = xDemand.get(i);
            }
            treeField.showField();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
