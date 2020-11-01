import sim.engine.SimState;

public class Test1 extends SimState {

    public Location playingField;

    public Test1(long seed) {
        super(seed);
    }
    public static void main(String[] args) {


        doLoop(Test1.class, args);
        System.exit(0);
    }

    public void setupPlayingField() {
        Location l = new Location(0);
        l.makeSpecial(Speciality.HOUSE_EXIT, Colors.BLUE);
        playingField = l;

        //Make the ring which is 40 fields. 0 exists
        for(int i=1; i< 40; i++) {
            Location thisLocation = new Location(i);

            //Mark house exits
            if(i == 10) {
                thisLocation.makeSpecial(Speciality.HOUSE_EXIT, Colors.BLUE);
            }
            if(i == 20) {
                thisLocation.makeSpecial(Speciality.HOUSE_EXIT, Colors.GREEN);
            }
            if(i == 30) {
                thisLocation.makeSpecial(Speciality.HOUSE_EXIT, Colors.RED);
            }

            //Make Goals
            if(i == 9) {
                thisLocation.makeSpecial(Speciality.GOAL_ENTRY, Colors.YELLOW);
                thisLocation.goalEntry = new Location(100, Speciality.GOAL_FIELD, Colors.YELLOW);
                thisLocation.goalEntry.next = new Location(101, Speciality.GOAL_FIELD, Colors.YELLOW);
                thisLocation.goalEntry.next.next = new Location(102, Speciality.GOAL_FIELD, Colors.YELLOW);
                thisLocation.goalEntry.next.next.next = new Location(103, Speciality.GOAL_FIELD, Colors.YELLOW);
            }
            if(i == 19) {
                thisLocation.makeSpecial(Speciality.GOAL_ENTRY, Colors.GREEN);
                thisLocation.goalEntry = new Location(200, Speciality.GOAL_FIELD, Colors.GREEN);
                thisLocation.goalEntry.next = new Location(201, Speciality.GOAL_FIELD, Colors.GREEN);
                thisLocation.goalEntry.next.next = new Location(202, Speciality.GOAL_FIELD, Colors.GREEN);
                thisLocation.goalEntry.next.next.next = new Location(203, Speciality.GOAL_FIELD, Colors.GREEN);
            }
            if(i == 29) {
                thisLocation.makeSpecial(Speciality.GOAL_ENTRY, Colors.RED);
                thisLocation.goalEntry = new Location(300, Speciality.GOAL_FIELD, Colors.RED);
                thisLocation.goalEntry.next = new Location(301, Speciality.GOAL_FIELD, Colors.RED);
                thisLocation.goalEntry.next.next = new Location(302, Speciality.GOAL_FIELD, Colors.RED);
                thisLocation.goalEntry.next.next.next = new Location(303, Speciality.GOAL_FIELD, Colors.RED);
            }
            if(i == 39) {
                thisLocation.makeSpecial(Speciality.GOAL_ENTRY, Colors.BLUE);
                thisLocation.goalEntry = new Location(400, Speciality.GOAL_FIELD, Colors.BLUE);
                thisLocation.goalEntry.next = new Location(401, Speciality.GOAL_FIELD, Colors.BLUE);
                thisLocation.goalEntry.next.next = new Location(402, Speciality.GOAL_FIELD, Colors.BLUE);
                thisLocation.goalEntry.next.next.next = new Location(403, Speciality.GOAL_FIELD, Colors.BLUE);
            }

            l.next = thisLocation;
            l = l.next;
        }
        l.next = playingField;

    }
}
