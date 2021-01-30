// Idee entnommen aus https://github.com/HenrikKronborg/JSSP-Swarm-Intelligence/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SwarmIntelligenceAlgorithm implements Algorithm {
    Data data;
    int itterations = 0;

    public SwarmIntelligenceAlgorithm(Data data) {
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
        List<GreedyAlgorithm> swarm = new ArrayList<>();
        /*
         * create inital Swarm
         */
        for (int i = 0; i < swarmSize; i++) {
            double[] weights = new double[data.jobs.size()];
            /*
             * create inital weights
             */
            for (int j = 0; j < weights.length; j++) {
                weights[j] = Math.random();
//                System.out.println(weights[j]);
            }
//            System.out.println(weights[0]);
            GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(new Data(data), weights);
            greedyAlgorithm.calculate();
//            for (Resource resource : greedyAlgorithm.getResources()) {
//                System.out.println(resource);
//            }
            swarm.add(greedyAlgorithm);
        }

        for (int i = 0; i < 5; i++) {
//          Die schnellsten zuerst
            swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
            System.out.println(i + ". BestFinishTime: " + swarm.get(0).getFinishTime());
//          auf swarmSize kürzen
            while (swarm.size() > swarmSize) {
                swarm.remove(swarm.size() - 1);
            }
//          neue 'neighbourhood' erstellen aus den '#neighbourhoodSize' besten
            for (int j = 0; j < neighbourhoodSize; j++) {
                double[] weights = getNewWeights(swarm.get(j).getWeights(), i);
                GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(new Data(data), weights);
                greedyAlgorithm.calculate();
                swarm.add(greedyAlgorithm);
            }
        }
        swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
        this.data = swarm.get(0).getData();
    }

    private double[] getNewWeights(double[] weights, double oldWeightsPower) {
//        System.out.print("1: ");
//        for(double weight : weights){
//            System.out.print(weight + ",");
//        }
//        System.out.println();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (weights[i] * oldWeightsPower + Math.random()) / (oldWeightsPower + 1);
        }
//        System.out.print("2: ");
//        for(double weight : weights){
//            System.out.print(weight + ",");
//        }
//        System.out.println();
        return weights;
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
