// Idee entnommen aus https://github.com/HenrikKronborg/JSSP-Swarm-Intelligence/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EvolutinaryAlgorithm implements Algorithm {
    Data data;
    int itterations = 0;

    public EvolutinaryAlgorithm(Data data) {
        this.data = data;
    }

    private final int population = 10; // Should be between 10-50
    private final int neighbourhoodSize = 5; // Should be greater than 3
    private final int gernartion = 5; //Should be between 5-20

    @Override
    public void calculate() {
        List<GreedyAlgorithm> swarm = new ArrayList<>();
        /* create inital Population */
        for (int i = 0; i < population; i++) {
            double[] weights = new double[data.jobs.size()];
            /* create inital weights */
            for (int j = 0; j < weights.length; j++) {
                weights[j] = Math.random();
            }
            GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(new Data(data), weights);
            greedyAlgorithm.calculate();
            itterations += greedyAlgorithm.getItterations();
            swarm.add(greedyAlgorithm);
        }
        itterations = itterations / population;
        for (int i = 0; i < gernartion; i++) {
            /* Die schnellsten zuerst */
            swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
//            System.out.println(i + ". BestFinishTime: " + swarm.get(0).getFinishTime());
            /* auf Population kürzen */
            while (swarm.size() > population) {
                swarm.remove(swarm.size() - 1);
            }
            /* neue 'neighbourhood' erstellen aus den '#neighbourhoodSize' besten */
            for (int j = 0; j < neighbourhoodSize; j++) {
                double[] weights = getNewWeights(swarm.get(j).getWeights(), i);
                GreedyAlgorithm greedyAlgorithm = new GreedyAlgorithm(new Data(data), weights);
                greedyAlgorithm.calculate();
                itterations += greedyAlgorithm.getItterations();
                swarm.add(greedyAlgorithm);
            }
            itterations = itterations / neighbourhoodSize;
        }

        swarm.sort((Comparator.comparingInt(Algorithm::getFinishTime)));
        this.data = swarm.get(0).getData();
    }

    /**
     * @param weights         ein Array der Gewichte der einzelnen Jobs
     * @param oldWeightsPower je größer die Zahl, desto weniger wird der Wert geändert.
     *                        Bsp:
     *                        oldWeightsPower=3 => 1/4 wird zufällig geändert
     *                        oldWeightsPower=4 => 1/5 wird zufällig geändert
     * @return zufällige neue Gewichte der Jobs
     */
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
