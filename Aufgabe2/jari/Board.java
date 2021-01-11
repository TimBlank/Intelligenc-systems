package jari;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Board {
    public ArrayList<Group> getRows() {
        return rows;
    }

    public ArrayList<Group> getColumns() {
        return columns;
    }

    private ArrayList<Group> rows = new ArrayList<>();
    private ArrayList<Group> columns = new ArrayList<>();
    private ArrayList<Field> fields = new ArrayList<>();

    public Board(String filePath) {
        try {
            //Open File, fill rows with Fields
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

            String line = bufferedReader.readLine();
            //Parse first line as demands for column
            String[] yDemand = line.split(",", -1);
            for (int x = 1; x < yDemand.length; x++) {
                columns.add(new Group(Integer.parseInt(yDemand[x]), x - 1));
            }

            line = bufferedReader.readLine();
            int y = 0; //Counter for row we are going down
            while (line != null) {
                String[] lineTokens = line.split(",", -1);
                //Save the demand for this row
                rows.add(new Group(Integer.parseInt(lineTokens[0]), y));
                for (int x = 1; x < lineTokens.length; x++) {
                    //Add a field for this
                    if (lineTokens[x].equals("t")) {
                        rows.get(y).getFields().add(new Field(this, Type.Tree));
                    } else {
                        rows.get(y).getFields().add(new Field(this, Type.unknown));
                    }
                }

                line = bufferedReader.readLine();
                y++;
            }

            for (Group column : columns) {
                for (Group row : rows) {
                    column.getFields().add(row.getFields().get(column.getPosition()));
                }
            }
            for (Group row : rows) {
                fields.addAll(row.getFields());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Group getColumn(Field field) {
        for (Group column : columns) {
            if (column.getFields().contains(field)) return column;
        }
        return null;
    }

    public Group getRow(Field field) {
        for (Group row : rows) {
            if (row.getFields().contains(field)) return row;
        }
        return null;
    }

    public void markImpossibleFieldsAsGrass() {
        System.out.println(new Throwable().getStackTrace()[0].getMethodName());
        for (Field field : fields) {
            if (field.type() == Type.unknown) {
                if (field.left() != null && field.left().type() != Type.Tree) {
                    if (field.up() != null && field.up().type() != Type.Tree) {
                        if (field.right() != null && field.right().type() != Type.Tree) {
                            if (field.down() != null && field.down().type() != Type.Tree) {
                                field.setGarden();
                            }
                        }
                    }
                }
            }
        }
    }

    public void markFullGroupsAsGrass() {
        System.out.println(new Throwable().getStackTrace()[0].getMethodName());
        for (Group row : rows) {
            if (row.getDemand() == row.getTents()) {
                for (Field field : row.getFields()) {
                    if (field.type() == Type.unknown) {
                        field.setGarden();
                    }
                }
            }
        }
        for (Group column : columns) {
            if (column.getDemand() == column.getTents()) {
                for (Field field : column.getFields()) {
                    if (field.type() == Type.unknown) {
                        field.setGarden();
                    }
                }
            }
        }
    }

    public void markSingleUnknownAsTent() {
        System.out.println(new Throwable().getStackTrace()[0].getMethodName());
        for (Field field : fields) {
            if (field.type() == Type.Tree) {
                int count = 0;
                Field newTent = null;
                Field[] directions = new Field[]{field.up(), field.right(), field.down(), field.left()};
                for (Field field2 : directions) {
                    if (field2 != null && field2.type() == Type.unknown) {
                        newTent = field2;
                        count++;
                    }
                }
                if (count == 1) newTent.setTent(field);
            }
        }
    }

    public void printBoard() {
        System.out.print("x  ");
        for (Group column : columns) {
            System.out.print(column.getDemand() + " ");
        }
        System.out.println();
        for (Group row : rows) {
            System.out.print(row.getDemand() + (row.getDemand() < 10 ? " " : "") + " ");
            for (Field field : row.getFields()) {
                System.out.print(field.toString() + " ");
            }
            System.out.println();
        }
    }
}
