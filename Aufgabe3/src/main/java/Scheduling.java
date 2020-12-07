import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Scheduling {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        // TODO: Other files
        InputStream is = Scheduling.class.getClassLoader().getResourceAsStream("abz6_10_jobs_10_resources.json");
        String json = readFromInputStream(is);
//        System.out.println(json);
        Data data = gson.fromJson(json, Data.class);
        System.out.println(data.toString());
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
