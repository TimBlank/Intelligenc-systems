import java.util.List;

public class Resource {
    public int id;
    public List<Operation> operations;

    public int getEndTime() {
        int time = 0;
        for (Operation operation : operations) {
            time += operation.duration;
        }
        return time;
    }

    public void addOperation(Operation operation, int time) {
        int endTime = getEndTime();
        if (endTime == time) {
            operation.startTime = endTime;
            operations.add(operation);
        } else if (endTime < time) {
            operations.add(new Operation(time - endTime));
            operation.startTime = getEndTime();
            operations.add(operation);
        } else {
//        } else if (endTime>time) {
            System.out.println("HIER ist 1 Fehler!");
        }
    }

    @Override
    public String toString() {
        return "[ id: " + id + " ]";
    }
}
