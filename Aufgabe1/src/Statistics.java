package src;

import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    ArrayList<HashMap<Player, Integer>> rankings = new ArrayList<>();

    public void add(Game game) {
//        System.out.println("\nRanking!");
        HashMap<Player, Integer> winners = new HashMap();
        for (int i = 0; i < game.winners.size(); i++) {
//            System.out.println(i + 1 + ". " + game.winners.get(i).name + "(" + game.winners.get(i).rounds + ")!");
            winners.put(game.winners.get(i), game.winners.get(i).rounds);
        }
        this.rankings.add(winners);
    }

    @Override
    public String toString() {
        HashMap<String, Integer> ranking = new HashMap<>();
        for (HashMap<Player, Integer> game : rankings) {
            for (Map.Entry<Player, Integer> entry : game.entrySet()) {
                int count = ranking.getOrDefault(entry.getKey().name, 0);
                ranking.put(entry.getKey().name, count + entry.getKey().rounds);
            }
        }
        StringBuilder string = new StringBuilder("Durchschnitt der " + rankings.size() + " Runden:\n");
        Map<String, Integer> order = sortByValue(ranking, true);
        int place = 1;
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            string.append(place++ + ". " + entry.getKey() + ": " + entry.getValue() / rankings.size() + "\n");
        }

        return string.toString();
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
}
