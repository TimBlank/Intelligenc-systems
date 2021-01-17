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
        //TODO Dynamic Path
        files = new File("C:\\Users\\timyb\\IdeaProjects\\Intelligenc-systems\\Aufgabe3\\src\\main\\resources").listFiles();
        String Dateiname;
//        for (int i=0; i< files.length;i++) {
//            Dateiname = files[i].getName();
//            System.out.println("Nummer:"+i+ " | "+Dateiname);
//        }

//        System.out.println(json);
        /**
         * Führt, für alle Dateien aus der Liste, die Sortierungen aus
         */
        for (int i=0; i< files.length;i++) {
            Dateiname = files[i].getName();
            System.out.println("Nummer:"+i+ " | "+Dateiname);
            InputStream is = Scheduling.class.getClassLoader().getResourceAsStream(Dateiname);
            String json = readFromInputStream(is);
            Data data = gson.fromJson(json, Data.class);


            /** Random Job sorter
             *
             */
            System.out.println("Random:");
            data = gson.fromJson(json, Data.class);
            for (Job job : data.jobs) {
                for (Operation operation : job.operations) {
                    operation.job = job.id;
                }
            }
            Randoms random = new Randoms(data);
            random.calculate();
            for (Resource resource : random.resources) {
                System.out.println(resource);
            }
            /* * Greedy(longest job next) Job sorter
             *
             */
            System.out.println("Greedy:");
            data = gson.fromJson(json, Data.class);
            for (Job job : data.jobs) {
                for (Operation operation : job.operations) {
                    operation.job = job.id;
                }
            }
            System.out.println(data.toString());
            Greedy greedy = new Greedy(data);
            greedy.calculate();
            for (Resource resource : greedy.resources) {
                System.out.println(resource);
            }
            /* * Shortest-Job-Next sorter
             *
             */
            System.out.println("Shortest-Job-Next:");
            data = gson.fromJson(json, Data.class);
            for (Job job : data.jobs) {
                for (Operation operation : job.operations) {
                    operation.job = job.id;
                }
            }
            SPT spt = new SPT(data);
            spt.calculate();
            for (Resource resource : spt.resources) {
                System.out.println(resource);
            }
            /* * Earliest Due Date sorter
             *
             */
            System.out.println("Earliest Due Date:");
            System.out.println("NOT READY");
/*          data = gson.fromJson(json, Data.class);
                for (Job job : data.jobs) {
                    for (Operation operation : job.operations) {
                        operation.job = job.id;
                    }
                }
             EDD edd = new EDD(data);
             edd.calculate();
             for (Resource resource : edd.resources) {
                System.out.println(resource);
             }*/
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
