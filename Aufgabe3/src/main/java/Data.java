import java.util.List;

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
            case "Greedy" -> algorithm = new Greedy(this, new double[0]);
            case "Shortest-Job-Next" -> algorithm = new SPT(this);
            case "Earliest Due Date" -> algorithm = new EDD(this, new double[0]);
            case "Swarm Intelligence" -> algorithm = new swarmIntelligence(this);
            default -> System.exit(0);
        }
        algorithm.calculate();

        for (Resource resource : algorithm.getResources()) {
            System.out.println(resource);
        }

        long timeEnd = System.nanoTime();

        System.out.println("Anzahl operationen: " + algorithm.getItterations() + " |  Ende der letzten operation: " + getFinishTime(algorithm));
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

    int getFinishTime(Algorithm algorithm) {
        int finishTime = 0;

        for (Resource resource : algorithm.getResources()) {
//            System.out.println(resource);
            int newFinishTime = resource.getFinishTime();
            if (newFinishTime > finishTime) {
                finishTime = newFinishTime;
            }
        }
        return finishTime;
    }
}

