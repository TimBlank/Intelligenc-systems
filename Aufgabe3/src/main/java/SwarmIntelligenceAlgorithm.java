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
    private int globalBestValue = Integer.MAX_VALUE;
    private int swarmWalk = 5; //Should be between 5-20

    @Override
    public void calculate() {
        List<GreedyAlgorithm> swarm = new ArrayList<>();
        /*
         * create inital Swarm
         */
//        System.out.println("itte: "+itterations);
        for (int i = 0; i < swarmSize; i++) {
            double[] weights = new double[data.jobs.size()];
            /*
             * create inital weights
             */
            for (int j = 0; j < weights.length; j++) {
                weights[j] = Math.random();
            }
            GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(new Data(data), weights);
            greedyAlgorithm.calculate();
            itterations += greedyAlgorithm.getItterations();
//            System.out.println("itte: "+itterations);
            swarm.add(greedyAlgorithm);
        }
        itterations = itterations / swarmSize;
//        System.out.println("itte: "+itterations);

        for (int i = 0; i < swarmWalk; i++) {
//          Die schnellsten zuerst
            swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
//            System.out.println(i + ". BestFinishTime: " + swarm.get(0).getFinishTime());
//          auf swarmSize kürzen
            while (swarm.size() > swarmSize) {
                swarm.remove(swarm.size() - 1);
            }
//          neue 'neighbourhood' erstellen aus den '#neighbourhoodSize' besten
            for (int j = 0; j < neighbourhoodSize; j++) {
                double[] weights = getNewWeights(swarm.get(j).getWeights(), i);
                GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(new Data(data), weights);
                greedyAlgorithm.calculate();
                itterations += greedyAlgorithm.getItterations();
//                System.out.println("itte: "+itterations);
                swarm.add(greedyAlgorithm);
            }
            itterations = itterations / neighbourhoodSize;
//            System.out.println("itte: "+itterations);
        }

        swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
        this.data = swarm.get(0).getData();
    }

    private double[] getNewWeights(double[] weights, double oldWeightsPower) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (weights[i] * oldWeightsPower + Math.random()) / (oldWeightsPower + 1);
        }
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
