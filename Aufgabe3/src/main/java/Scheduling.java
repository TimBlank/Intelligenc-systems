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
        String Dateiname;
//        for (int i=0; i< files.length;i++) {
//            Dateiname = files[i].getName();
//            System.out.println("Nummer:"+i+ " | "+Dateiname);
//        }
//        System.out.println(json);

        /**
         * Führt, für alle Dateien aus der Liste, die Sortierungen aus
         * i von 0 bis 80
         */
        long timeStart;
        long timeEnd;
//        for (int i=0; i< files.length;i++) {
        for (int i = 0; i < 1; i++) {
            Dateiname = files[i].getName();
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("Nummer:" + i + " | " + Dateiname);
            InputStream is = Scheduling.class.getClassLoader().getResourceAsStream(Dateiname);
            String json = readFromInputStream(is);
            Data data = gson.fromJson(json, Data.class);

            //System.out.println(data.toString());


            /** Random
             * Es wird zuffälig zwischen den möglichen
             * Jobs einer in reihenfolge ausgewählt
             */
            data = gson.fromJson(json, Data.class);
            data.work("Random");

            /** Shortest-Job-Next
             * Es wird in Reihenfolge immer die
             * kürzeste Operation ausgewählt
             */
            data = gson.fromJson(json, Data.class);
            data.work("Shortest-Job-Next");

            /** Earliest Due Date
             * Da kein Due Date für die Jobs mit gegeben wurde
             * wird der Gesamt Entzeitpunkt des Jobs
             * als Due Date verwendet
             */
            data = gson.fromJson(json, Data.class);
            data.work("Earliest Due Date");

            /** Greedy
             * (longest Operation next)
             */
            data = gson.fromJson(json, Data.class);
            data.work("Greedy");

            /** Swarm Intelligence
             * Erstellt Greedys mit
             * leicht geänderten gewichten
             * übergibt besten Greedy
             */
//            data = gson.fromJson(json, Data.class);
//            data.work("Swarm Intelligence");

        }
//        System.out.println(greedy.getResources());

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
