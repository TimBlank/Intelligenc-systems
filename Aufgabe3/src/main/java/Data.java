import java.util.ArrayList;
import java.util.List;

public class Data {
    public List<Resource> resources;
    public List<Job> jobs;

    public Data(Data data) {
        this.resources = new ArrayList<>();
        data.resources.forEach(resource -> this.resources.add(new Resource(resource)));
        this.jobs = new ArrayList<>();
        data.jobs.forEach(job -> this.jobs.add(new Job(job)));
    }

    public void work(Class className) {
        System.out.println(className + ":");

        for (Job job : this.jobs) {
            for (Operation operation : job.operations) {
                operation.job = job.id;
            }
        }
        long timeStart = System.nanoTime();
        Algorithm algorithm = null;

        double[] weights = new double[this.jobs.size()];
        /*
         * create inital weights
         */
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
//            System.out.println(weights[i]);
        }
        double[] dueDate = new double[this.jobs.size()];
        /*
         * create inital do Date
         */
        for (int i = 0; i < dueDate.length; i++) {
            dueDate[i] = Math.random();
//            System.out.println(dueDate[i]);
        }

        if (RandomAlgorithm.class.equals(className)) {
            algorithm = new RandomAlgorithm(this);
//            case "Random" -> algorithm = new Randoms(this);
        } else if (GreedyAlgorithm.class.equals(className)) {
            algorithm = new GreedyAlgorithm(this, weights);
//            case "Greedy" -> algorithm = new Greedy(this, weights);
        } else if (ShortestJobNextAlgorithm.class.equals(className)) {
            algorithm = new ShortestJobNextAlgorithm(this);
//            case "Shortest-Job-Next" -> algorithm = new SPT(this);
        } else if (EarliestDueDateAlgorithm.class.equals(className)) {
            algorithm = new EarliestDueDateAlgorithm(this, weights);
//            case "Earliest Due Date" -> algorithm = new EDD(this, weights);
        } else if (SwarmIntelligenceAlgorithm.class.equals(className)) {
            algorithm = new SwarmIntelligenceAlgorithm(this);
//            case "Swarm Intelligence" -> algorithm = new swarmIntelligence(this);
        } else {
            System.exit(0);
        }
        algorithm.calculate();

//        Zeigt alle Operatioen der Einzelnen Resourcen mit anfangs und endzeit sowie Job zugehörigkeit an
        for (Resource resource : algorithm.getResources()) {
//            System.out.println(resource);
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

