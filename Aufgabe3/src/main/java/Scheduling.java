import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Scheduling {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        // TODO: Other files
//        InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("swv20_50_jobs_10_resources.json");
         InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("la01_10_jobs_5_resources.json");
        String json = readFromInputStream(is);
//        System.out.println(json);
        System.out.println("Greedy:");
        Data data = gson.fromJson(json, Data.class);
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

        System.out.println("Random:");
        data = gson.fromJson(json, Data.class);
        for (Job job : data.jobs) {
            for (Operation operation : job.operations) {
                operation.job = job.id;
            }
        }
        Random random = new Random(data);
        random.calculate();
        for (Resource resource : random.resources) {
            System.out.println(resource);
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
