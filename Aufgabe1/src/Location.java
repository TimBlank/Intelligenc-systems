public class Location {
    public int fieldID;
    public Location next;
    public Colors occupation = Colors.NONE;
    public Speciality specialField = null;
    public Colors colorSpecial = Colors.NONE;
    public Location goalEntry = null;

    public Location(int fieldID) {
        this.fieldID = fieldID;
    }
    public Location(int fieldID, Speciality s, Colors c) {
        this.fieldID = fieldID;
    }

    public void makeSpecial(Speciality s, Colors c) {
        this.specialField = s;
        this.colorSpecial = c;
    }
}
