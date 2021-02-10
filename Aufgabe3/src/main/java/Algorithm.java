import java.util.List;

public interface Algorithm {
    /**
     * Die einzelnen erstellten Aufgaben werden in dieser Methode berechnet
     */
    void calculate();

    /**
     * @return resources
     */
    List<Resource> getResources();

    /**
     * @return itterations
     */
    int getItterations();

    /**
     * @return finishTime
     */
    int getFinishTime();
}
