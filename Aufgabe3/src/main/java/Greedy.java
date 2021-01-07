import java.util.ArrayList;
import java.util.List;

public class Greedy implements Algorithm {
    List<Resource> resources;
    List<Job> jobs;

    public Greedy(Data data) {
        this.resources = data.resources;
        this.jobs = data.jobs;
    }

    @Override
    public void calculate() {
        while (true) {
            // 1. getLongestOperationDoAble
            List<Operation> operations = new ArrayList<>();
            for (Job job : jobs) {
                operations.add(job.getNextOperation());
            }
            operations.sort((o1, o2) -> Integer.compare(o2.duration, o1.duration));
            Operation nextOperation = operations.get(0);
            Resource nextResource = resources.get(0);
            for (Resource resource : resources) {
                if (resource.id == nextOperation.resource) {
//                    System.out.println("AOUFHAUPIOSh");
                    nextResource = resource;
                    break;
                }
            }
// 1. Ende Job
            int jobEnd = 0;
            for (Job job : jobs) {
                if (job.id == nextOperation.job) {
                    jobEnd = job.lastOperationEnd();
//                    System.out.println("job: " + job);
//                    System.out.println("jobEnd: " + jobEnd);
                    break;
                }
            }
// 2. Wo passt das in Resource
            nextResource.addOperation(nextOperation, nextResource.getOptimalTime(jobEnd, nextOperation.duration));
            System.out.println("thisResource: " + nextResource);
//            TODO getActualEndTimeOfJob danach erst starten
//            TODO
//            ----
//            System.out.println(operations);
//            Resource nextResource = resources.get(0);
//            System.out.println(nextResource);
//            for (Resource resource : resources) {
//            System.out.println(resource + " | " + nextResource);
//                if (resource.getEndTime() < nextResource.getEndTime()) {
//                    nextResource = resource;
//                }
//            }
//            Job nextJob = jobs.get(0);
//            for (Job job : jobs) {
//                if (job.id == nextOperation.job) {
//                    nextJob = job;
//                }
//            }
//        System.out.println("nextResource.getEndTime(): " + nextResource.getEndTime());
//        System.out.println("nextJob.lastOperationEnd(): " + nextJob.lastOperationEnd());
//            int time = Math.max(nextJob.lastOperationEnd(), nextResource.getEndTime());
//            nextResource.addOperation(nextOperation, time);
//            System.out.println("nextResource.o: " + nextResource.operations);
            int jobnumber = resources.stream().mapToInt(resource -> resource.operations.size()).sum();
//            System.out.println(jobnumber);
            if (nextResource.id == 0) {
                nextResource.id = 0;
            }
            boolean ready = true;
            for (Job job : jobs) {
                if (!job.ready()) {
                    ready = false;
                    break;
                }
            }
            if (ready) return;
        }
        // TODO - liste mit Operationen der Jobs (Name/id;Maschine;länge) "Unsortiert"

        //TODO - Operation in richtige Re


        // TODO - längste Zeit in der Liste finden und auswählen

        // TODO - liste aktualisieren (nächste Operation über job hinzufügen)
        //get next Operation


        // TODO - Wiederholen bis "Unsortiert" keinen eintrag mehr besitzt
        // TODO - Liste Sortiert ausgeben (Nach Position und Maschine Sortiert)
    }

    @Override
    public List<Resource> getResources() {
        return resources;
    }


}