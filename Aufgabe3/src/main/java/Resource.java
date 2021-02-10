import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Resource {
    public int id;
    public List<Operation> operations;

    public Resource() {
        this.operations = new ArrayList<>();
    }


    /**
     * Kopie erstellen
     *
     * @param resource
     */
    public Resource(Resource resource) {
        this.id = resource.id;
        this.operations = new ArrayList<>();
    }

    public int getEndTime() {
        int time = 0;
        for (Operation operation : operations) {
            time += operation.duration;
        }
        return time;
    }

    public void addOperation(Operation operation, int startTime) {
        operations.add(operation);
        operation.setStartTime(startTime);
//        int endTime = getEndTime();
//        if (endTime == time) {
//            operation.startTime = endTime;
//            operations.add(operation);
//        } else if (endTime < time) {
//            operations.add(new Operation(time - endTime));
//            operation.startTime = getEndTime();
//            operations.add(operation);
//        } else {
////        } else if (endTime>time) {
//            System.out.println("HIER ist 1 Fehler!");
//        }
    }

    public int getOptimalTime(int jobEnd, int duration) {
        int starttime = jobEnd;
        operations.sort(Comparator.comparingInt(o -> o.startTime));
        for (Operation operation : operations) {
            if (operation.startTime + operation.duration <= starttime) continue;
            if (starttime + duration <= operation.startTime) return starttime;
            starttime = operation.startTime + operation.duration;
        }
        return starttime;
    }

    @Override
    public String toString() {
        operations.sort(Comparator.comparingInt(o -> o.startTime));
        StringBuilder stringBuilder = new StringBuilder();
        for (Operation operation : operations) {
            stringBuilder.append(String.format("[%s-%s-%s]", operation.startTime, operation.job, operation.startTime + operation.duration));
        }
        return "[ id: " + id + " Ops:" + stringBuilder + " ]";
    }

    int getminTime() {
        int time = 0;
        for (Operation operation : operations) {
            time += operation.duration;
        }
        return time;
    }

    public int getFinishTime() {
        int time = 0;
        for (Operation operation : operations) {
            int newTime = operation.startTime + operation.duration;
            if (newTime > time) {
                time = newTime;
            }
        }
        return time;
    }

}
