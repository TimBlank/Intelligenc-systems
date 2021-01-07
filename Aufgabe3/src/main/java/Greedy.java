import java.util.List;

public class Greedy implements Algorithm {
    List<Resource> resources;
    List<Job> jobs;

    public void Greedy(Data data) {
        this.resources = data.resources;
        this.jobs = data.jobs;
    }

    @Override
    public void calculate() {
        // TODO - liste mit Operationen der Jobs (Name/id;Maschine;länge) "Unsortiert"
        unsortetjobs = jobs;

        //TODO - Operation in richtige Re


        // TODO - längste Zeit in der Liste finden und auswählen

        // TODO - liste aktualisieren (nächste Operation über job hinzufügen)
        //get next Operation


        // TODO - Wiederholen bis "Unsortiert" keinen eintrag mehr besitzt
        // TODO - Liste Sortiert ausgeben (Nach Position und Maschine Sortiert)
    }

    @Override
    public List<Resource> getResources() {
        // TODO - alle vorhandenen Maschinen laden
        return null;
    }


}