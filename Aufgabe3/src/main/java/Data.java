import java.util.ArrayList;
import java.util.List;

/**
 * Aus den Dateien wird ein Data-Objekt erzeugt, dass dann mit den verschiedenen Algorithmen bearbeitet werden kann
 * Dafür werden die gegeben resources & jobs gespeichert und weitere Werte gespeichert, um diese Werte für die
 * Konsolenausgabe bereit zu legen.
 */
public class Data {
    public List<Resource> resources;
    public List<Job> jobs;
    public long time;
    public int minTime;
    public int finishTime;
    public int itterations;

    /**
     * Kopie erstellen
     *
     * @param data
     */
    public Data(Data data) {
        this.resources = new ArrayList<>();
        data.resources.forEach(resource -> this.resources.add(new Resource(resource)));
        this.jobs = new ArrayList<>();
        data.jobs.forEach(job -> this.jobs.add(new Job(job)));
    }

    /**
     * Diese Methode ruft die Ausführung der Data mit einem ausgewählen Algorithmus auf
     *
     * @param className Algorithmus Klasse
     */
    public void work(Class className) {
        int duDateDelay = 100;
        long timeStart;
        long timeEnd;
        Algorithm algorithm = null;

        System.out.println(className + ":");

        for (Job job : this.jobs) {
            for (Operation operation : job.operations) {
                operation.job = job.id;
            }
        }
        timeStart = System.nanoTime();
        minTime = this.getminTime();

        /* Je nachdem welcher Algorithmus gewählt ist wird das algorithm-Objekt erstellt */
        if (RandomAlgorithm.class.equals(className)) {
            algorithm = new RandomAlgorithm(this);

        } else if (ShortestJobNextAlgorithm.class.equals(className)) {
            algorithm = new ShortestJobNextAlgorithm(this);

        } else if (SwarmIntelligenceAlgorithm.class.equals(className)) {
            algorithm = new SwarmIntelligenceAlgorithm(this);

        } else if (EarliestDueDateAlgorithm.class.equals(className)) {
            /* In EarliestDueDateAlgorithm wird eine dueDate benötigt, diese wird zu nächst zufällig erstellt */
            double[] dueDate = new double[this.jobs.size()];
            for (int i = 0; i < dueDate.length; i++) {
                dueDate[i] = Math.floor(Math.random() * Math.floor(duDateDelay));
//            System.out.println(dueDate[i]);
            }
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                dueDate[i] = dueDate[i] + job.getminTime();
//            System.out.println(job.getminTime());
//            System.out.println(dueDate[i]);
            }
            algorithm = new EarliestDueDateAlgorithm(this, dueDate);
        } else if (GreedyAlgorithm.class.equals(className)) {
            /* Erstellt einen Swarm */
            double[] weights = new double[this.jobs.size()];
            /* create inital weights */
            for (int i = 0; i < weights.length; i++) {
                weights[i] = Math.random();
//            System.out.println(weights[i]);
            }
            algorithm = new GreedyAlgorithm(this, weights);
        } else {
            /* Sollte die Klasse kein hier aufgenommenen Algorithmus beinhalten wird die Ausführung beendet */
            System.exit(0);
        }
        /* Der gewählte Algorithmus wird nun berechnet */
        algorithm.calculate();

        /* Zeigt alle Operatioen der Einzelnen Resourcen mit anfangs und endzeit sowie Job zugehörigkeit an */
//        for (Resource resource : algorithm.getResources()) {
//            System.out.println(resource);
//        }

        timeEnd = System.nanoTime();
        time = (timeEnd - timeStart) / 1000;
        finishTime = getFinishTime(algorithm);
        itterations = algorithm.getItterations();

        //System.out.println("  Ende der letzten operation: " + finishTime + "| Laufzeit: " + time + " qs");
        //System.out.println("Anzahl Itterationen: " + itterations + " | Minimale Jobzeit: " + minTime);
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

