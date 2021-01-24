// Idee entnommen aus https://github.com/HenrikKronborg/JSSP-Swarm-Intelligence/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class swarmIntelligence implements Algorithm {
    Data data;
    int itterations = 0;

    public swarmIntelligence(Data data) {
        this.data = data;
    }


    private int swarmSize = 10; // Should be between 10-50

    private int neighbourhoodSize = 5; // Should be either 3 or 5
    //    private double importanceOfPersonalBest = 2;      // C1
//    private double importanceOfNeighbourhoodBest = 2; // C2
    public static final double maxVelocity = 0.05;

    private int globalBestValue = Integer.MAX_VALUE;

    @Override
    public void calculate() {
        List<Greedy> swarm = new ArrayList<>();
        /*
         * create inital Swarm
         */
        for (int i = 0; i < swarmSize; i++) {
            double[] weights = new double[data.jobs.size()];
            /*
             * create inital weights
             */
            for (double weight : weights) {
                weight = Math.random();
            }
            Greedy greedy = new Greedy(data, weights);
            greedy.calculate();
            swarm.add(greedy);
        }

        for (int i = 0; i < 5; i++) {
//           Die schnellsten zuerst
            swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
//           TODO: auf swarmSize kürzen
            for (int j = 0; j < neighbourhoodSize; j++) {
                double[] weights = swarm.get(j).getWeights();
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
                swarm.add(greedy);
            }
        }
        swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
        this.data = swarm.get(0).getData();
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
