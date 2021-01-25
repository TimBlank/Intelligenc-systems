// Idee entnommen aus https://github.com/HenrikKronborg/JSSP-Swarm-Intelligence/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class swarmIntelligence implements Algorithm {
    Data data;
    int itterations = 0;
    private int swarmSize = 10; // Should be between 10-50
    private int neighbourhoodSize = 5; // Should be either 3 or 5
    private double maxVelocity = 0.05;
    private double duDateDelay = 25; //

    public swarmIntelligence(Data data) {
        this.data = data;
    }

    @Override
    public void calculate() {

        List<Greedy> greedySwarm = new ArrayList<>();
        /*
         * create inital Swarm
         */
        for (int i = 0; i < swarmSize; i++) {
            double[] weights = new double[data.jobs.size()];
            /*
             * create inital weights
             */
            for (int n = 0; n < weights.length; n++) {
                weights[i] = Math.random();
//            System.out.println(weights[i]);
            }
            Greedy greedy = new Greedy(data, weights);
            //TODO:FIX beim 2 durchlauf (i=1) läuft der Fehler
            greedy.calculate();
            for (Resource resource : greedy.getResources()) {
                System.out.println(resource);
            }
            greedySwarm.add(greedy);
        }

        for (int i = 0; i < 5; i++) {
//           Die schnellsten zuerst
            greedySwarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
//           TODO: auf swarmSize kürzen
            for (int j = 0; j < neighbourhoodSize; j++) {
                double[] weights = greedySwarm.get(j).getWeights();
                for (double weight : weights) {
                    //TODO: schwieriger die zahl 1 und 0 zu erreichen

                    if (Math.random() >= 0.5) {
                        weight = weight + maxVelocity * Math.random();
                    } else {
                        weight = weight - maxVelocity * Math.random();
                    }
                    if (weight > 1 || weight < 0) {
                        System.out.println("Fehler:" + " gewicht im Swarm von" + weight);
                        System.exit(0);
                    }
                }
                Greedy greedy = new Greedy(data, weights);
                greedy.calculate();
                greedySwarm.add(greedy);
            }
        }
        greedySwarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));

//        ------------------------------------------------------------Earliest Du Date------------------------------------------------------------
        List<EDD> eddSwarm = new ArrayList<>();
        for (int i = 0; i < swarmSize; i++) {
            double[] dueDates = new double[data.jobs.size()];
            /*
             * create inital do Date
             */
            int duDateDelay = 100;
            for (int n = 0; n < swarmSize; n++) {

                dueDates[i] = Math.floor(Math.random() * Math.floor(duDateDelay));
//            System.out.println(dueDate[i]);
            }
            for (int m = 0; m < data.jobs.size(); m++) {
                Job job = data.jobs.get(i);
                dueDates[i] = dueDates[i] + job.getminTime();
            }
            EDD edd = new EDD(data, dueDates);
            edd.calculate();
            for (Resource resource : edd.getResources()) {
                System.out.println(resource);
            }
            eddSwarm.add(edd);
        }
        for (int i = 0; i < 5; i++) {
//           Die schnellsten zuerst
            eddSwarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
//           TODO: auf swarmSize kürzen
            for (int j = 0; j < neighbourhoodSize; j++) {
                double[] dueDates = eddSwarm.get(j).getDuDate();
                for (double duDate : dueDates) {
                    //TODO: schwieriger die zahl 1 und 0 zu erreichen

                    if (Math.random() >= 0.5) {
                        duDate = duDate + Math.floor(Math.random() * Math.floor(duDateDelay));
                    } else {
                        duDate = duDate - Math.floor(Math.random() * Math.floor(duDateDelay));
                    }
                    if (duDate > 1 || duDate < 0) {
                        System.out.println("Fehler:" + " gewicht im Swarm von" + duDate);
                        System.exit(0);
                    }
                }
                EDD edd = new EDD(data, dueDates);
                edd.calculate();
                eddSwarm.add(edd);
            }
        }
        eddSwarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
        if (greedySwarm.get(0).getFinishTime() > eddSwarm.get(0).getFinishTime()) {
            this.data = greedySwarm.get(0).getData();
        } else {
            this.data = eddSwarm.get(0).getData();
        }
    }


    @Override
    public List<Resource> getResources() {
        return data.resources;
    }

    @Override
    public int getItterations() {
        return itterations;
    }

    @Override
    public int getFinishTime() {
        return data.getFinishTime(this);
    }

}
