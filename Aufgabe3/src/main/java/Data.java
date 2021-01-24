import java.util.List;
import java.util.Random;

public class Data {
    public List<Resource> resources;
    public List<Job> jobs;

    public void work(String name) {
        System.out.println(name + ":");

        for (Job job : this.jobs) {
            for (Operation operation : job.operations) {
                operation.job = job.id;
            }
        }
        long timeStart = System.nanoTime();
        Algorithm algorithm = null;

        switch (name) {
            case "Random" -> algorithm = new Randoms(this);
            case "Greedy" -> algorithm = new Greedy(this);
            case "Shortest-Job-Next" -> algorithm = new SPT(this);
            case "Earliest Due Date"-> algorithm = new EDD(this);
            default -> System.exit(0);
        }
        algorithm.calculate();
        long timeEnd = System.nanoTime();
        int finishTime = 0;

        for (Resource resource : algorithm.getResources()) {
            System.out.println(resource);
            int newFinishTime = resource.getFinishTime();
            if (newFinishTime>finishTime){
                finishTime=newFinishTime;
            }
        }
        System.out.println("Anzahl operationen: "+ algorithm.getItterations() +" |  Ende der letzten operation: " +finishTime);
        System.out.println("Laufzeit: " + (timeEnd - timeStart) + " Ns" + " | minimale Jobzeit: " + this.getminTime());
    }

    int getminTime() {
        int jobTime = 0;
        for (Job job : jobs) {
            int minTime = job.getminTime();
            if (minTime > jobTime) {
                jobTime += minTime;
            }
        }
        int resourceTime = 0;
        for (Resource resource : resources) {
            int minTime = resource.getminTime();
            if (minTime > resourceTime) {
                resourceTime += minTime;
            }
        }
        return Math.max(jobTime, resourceTime);
    }


    @Override
    public String toString() {
        return "[ resources: " + resources + ", operations: " + jobs + " ]";
    }



}
