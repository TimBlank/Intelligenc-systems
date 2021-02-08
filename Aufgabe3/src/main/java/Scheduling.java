import com.google.gson.Gson;

import java.io.*;

public class Scheduling {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
//        InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("swv20_50_jobs_10_resources.json");
//         InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("la01_10_jobs_5_resources.json");
//        String json = readFromInputStream(is);
//        Data data = gson.fromJson(json, Data.class);

        /**
         * Speichert alle Datei namen aus Ressource in eine Liste
         */
        File[] files;
        files = new File(".\\Aufgabe3\\src\\main\\resources").listFiles();
        int fileLength;
        String Dateiname;

        int ShortItts = 0;
        int ShortJobtime = 0;
        int Shorttime = 0;

        int randomItts = 0;
        int randomJobtime = 0;
        int randomtime = 0;

        int eddItts = 0;
        int eddJobtime = 0;
        int eddtime = 0;

        int greedyItts = 0;
        int greedyJobtime = 0;
        int greedytime = 0;

        int swarmItts = 0;
        int swarmJobtime = 0;
        int swarmtime = 0;

        /*
         * Führt, für alle Dateien aus der Liste, die Sortierungen aus
         * i von 0 bis 80
         */
        fileLength = files.length;
        for (int i = 0; i < fileLength; i++) {
            Dateiname = files[i].getName();
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Nummer:" + i + " | " + Dateiname);
            InputStream is = Scheduling.class.getClassLoader().getResourceAsStream(Dateiname);
            String json = readFromInputStream(is);
            Data data = gson.fromJson(json, Data.class);

            /* Shortest-Job-Next
             * Es wird in Reihenfolge immer die
             * kürzeste Operation ausgewählt
             */
            data = gson.fromJson(json, Data.class);
            data.work(ShortestJobNextAlgorithm.class);
            ShortItts += data.itterations;
            ShortJobtime += data.finishTime;
            Shorttime += data.time;

            /* Random
             * Es wird zuffällig zwischen den möglichen
             * Jobs einer, in reihenfolge ausgewählt
             */
            data = gson.fromJson(json, Data.class);
            data.work(RandomAlgorithm.class);
            randomItts += data.itterations;
            randomJobtime += data.finishTime;
            randomtime += data.time;


            /* Earliest Due Date
             * Da kein Due Date für die Jobs mit gegeben wurde
             * wird der Gesamt Entzeitpunkt des Jobs
             * als Due Date verwendet
             */
            data = gson.fromJson(json, Data.class);
            data.work(EarliestDueDateAlgorithm.class);
            eddItts += data.itterations;
            eddJobtime += data.finishTime;
            eddtime += data.time;

            /* Greedy
             * (longest Operation next)
             */
            data = gson.fromJson(json, Data.class);
            data.work(GreedyAlgorithm.class);
            greedyItts += data.itterations;
            greedyJobtime += data.finishTime;
            greedytime += data.time;

            /* Swarm Intelligence
             * Erstellt Greedys mit
             * leicht geänderten gewichten
             * übergibt besten Greedy
             */
            data = gson.fromJson(json, Data.class);
            data.work(SwarmIntelligenceAlgorithm.class);
            swarmItts += data.itterations;
            swarmJobtime += data.finishTime;
            swarmtime += data.time;
        }

        ShortItts = ShortItts / fileLength;
        ShortJobtime = ShortJobtime / fileLength;
        Shorttime = Shorttime / fileLength;

        randomItts = randomItts / fileLength;
        randomJobtime = randomJobtime / fileLength;
        randomtime = randomtime / fileLength;

        eddItts = eddItts / fileLength;
        eddJobtime = eddJobtime / fileLength;
        eddtime = eddtime / fileLength;

        greedyItts = greedyItts / fileLength;
        greedyJobtime = greedyJobtime / fileLength;
        greedytime = greedytime / fileLength;

        swarmItts = swarmItts / fileLength;
        swarmJobtime = swarmJobtime / fileLength;
        swarmtime = swarmtime / fileLength;

        System.out.println("--------------------------ERGEBNIS---------------------------------:");
        System.out.println("Shortest-Job-Next:");
        System.out.println("Ende der letzten operation: " + ShortJobtime + " | Anzahl Itterationen: " + ShortItts + " | Laufzeit:" + Shorttime + " qs");
        System.out.println("Random:");
        System.out.println("Ende der letzten operation: " + randomJobtime + " | Anzahl Itterationen: " + randomItts + " | Laufzeit:" + randomtime + " qs");
        System.out.println("Earliest Due Date:");
        System.out.println("Ende der letzten operation: " + eddJobtime + " | Anzahl Itterationen: " + eddItts + " | Laufzeit: " + eddtime + " qs");
        System.out.println("Greedy:");
        System.out.println("Ende der letzten operation: " + greedyJobtime + " | Anzahl Itterationen: " + greedyItts + " | Laufzeit: " + greedytime + " qs");
        System.out.println("Swarm Intelligence:");
        System.out.println("Ende der letzten operation: " + swarmJobtime + " | Anzahl Itterationen: " + swarmItts + " | Laufzeit: " + swarmtime + " qs");
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


}
