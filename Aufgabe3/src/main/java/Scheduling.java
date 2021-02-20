import com.google.gson.Gson;

import java.io.*;

public class Scheduling {
    /**
     * Die verschiedenen Algorithmen werden gestartet und die Ergebnisse am Ende verglichen
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
//        InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("swv20_50_jobs_10_resources.json");
//         InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("la01_10_jobs_5_resources.json");
//        String json = readFromInputStream(is);
//        Data data = gson.fromJson(json, Data.class);

        long timeStart;
        long timeEnd;
        /*
         * Speichert alle Dateinamen aus Ressource in eine Liste
         */
        File[] files;
        files = new File(".\\Aufgabe3\\src\\main\\resources").listFiles();
        int fileLength;
        String filename;

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

        int runs = 1; //Wert muss größer geleich 1 sein damit die Funktion laufen kann
        timeStart = System.currentTimeMillis();
        fileLength = files.length;
        for (int j = 0; j < runs; j++) {
            /*
             * Führt, für alle Dateien aus der Liste, die Sortierungen aus
             * i von 0 bis 80
             */

            for (int i = 0; i < fileLength; i++) {
                filename = files[i].getName();
                //System.out.println("----------------------------------------------------------------------------------");
                //System.out.println("Nummer:" + i + " | " + filename);
                InputStream is = Scheduling.class.getClassLoader().getResourceAsStream(filename);
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
                 * Es wird zufällig zwischen den möglichen
                 * Opertionen gewählt
                 */
                data = gson.fromJson(json, Data.class);
                data.work(RandomAlgorithm.class);
                randomItts += data.itterations;
                randomJobtime += data.finishTime;
                randomtime += data.time;

                /* Earliest Due Date
                 * Da kein Due Date für die Jobs mit gegeben wurde
                 * wird der Gesamt Entzeitpunkt des Jobs + Random wert
                 * als Due Date verwendet
                 */
                data = gson.fromJson(json, Data.class);
                data.work(EarliestDueDateAlgorithm.class);
                eddItts += data.itterations;
                eddJobtime += data.finishTime;
                eddtime += data.time;

                /* Greedy
                 * Da kein Gewichtung für die Jobs mit gegeben wurde
                 * wird ein Random wert zwischen 0 und 1
                 * als Gewichtung verwendet
                 */
                data = gson.fromJson(json, Data.class);
                data.work(GreedyAlgorithm.class);
                greedyItts += data.itterations;
                greedyJobtime += data.finishTime;
                greedytime += data.time;

                /* Swarm Intelligence
                 * Erstellt Greedys mit
                 * leicht geänderten Gewichten
                 * übergibt besten Greedy
                 */
                data = gson.fromJson(json, Data.class);
                data.work(SwarmIntelligenceAlgorithm.class);
                swarmItts += data.itterations;
                swarmJobtime += data.finishTime;
                swarmtime += data.time;
            }
            if ((j % 1000) == 0) {
                timeEnd = System.currentTimeMillis();
                System.out.println("Run: " + j + " Zeit: " + (timeEnd - timeStart) / 1000 + " s");
            }

        }
        System.out.println("--------------------------PRE-ERGEBNIS---------------------------------:");
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

        /* Durchschnittswerte für das Ergebnis berechnen */
        ShortItts = ShortItts / (fileLength * runs);
        ShortJobtime = ShortJobtime / (fileLength * runs);
        Shorttime = Shorttime / (fileLength * runs);

        randomItts = randomItts / (fileLength * runs);
        randomJobtime = randomJobtime / (fileLength * runs);
        randomtime = randomtime / (fileLength * runs);

        eddItts = eddItts / (fileLength * runs);
        eddJobtime = eddJobtime / (fileLength * runs);
        eddtime = eddtime / (fileLength * runs);

        greedyItts = greedyItts / (fileLength * runs);
        greedyJobtime = greedyJobtime / (fileLength * runs);
        greedytime = greedytime / (fileLength * runs);

        swarmItts = swarmItts / (fileLength * runs);
        swarmJobtime = swarmJobtime / (fileLength * runs);
        swarmtime = swarmtime / (fileLength * runs);

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
