package src;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {

    ArrayList<HashMap<Player, Integer>> rankings = new ArrayList<>();
    Integer[] Agressiveplacing = new Integer[4];
    Integer[] Defensiveplacing = new Integer[4];
    Integer[] WorstStoneFirstplacing = new Integer[4];
    Integer[] BeststoeFirstplacing = new Integer[4];


    public void add(Game game) {
        //        System.out.println("\nRanking!");
        HashMap<Player, Integer> winners = new HashMap();

        if (Agressiveplacing[0] == null) {
            for (int i = 0; i < 4; i++) {
                Agressiveplacing[i] = 0;
                Defensiveplacing[i] = 0;
                WorstStoneFirstplacing[i] = 0;
                BeststoeFirstplacing[i] = 0;
            }
        }
        for (int i = 0; i < game.winners.size(); i++) {
       //     System.out.println(i + 2 + ". " + game.winners.get(i).name + "(" + game.winners.get(i).rounds + ")!");
            //Speichert Rang & anzahl züge

            int n= i % 4;
                    switch (game.winners.get(i).name) {//Beliebige anzahl an playern
                        case "AGRESSIVE":
                            Agressiveplacing[n] = Agressiveplacing[n] +1;
                            break;
                        case "WorstStoneFirst":
                            Defensiveplacing[n] = Defensiveplacing[n] +1;
                            break;
                        case "DEFENSIVE":
                            WorstStoneFirstplacing[n] = WorstStoneFirstplacing[n] +1;
                            break;
                        case "BestStoneFirst":
                            BeststoeFirstplacing[n] = BeststoeFirstplacing[n] +1;
                            break;
                        default:
                            System.out.println("Problem with: " + i + " " + game.winners.get(i).name);
                    }

            winners.put(game.winners.get(i), game.winners.get(i).rounds);
        }
        this.rankings.add(winners);

        /**for(int i=0;i<4;i++){
            System.out.println("Platz "+(i+1)+ " | Aggresive:" +Agressiveplacing[i] + " | Defensive:"+Defensiveplacing[i]+ " | Worst:"+ WorstStoneFirstplacing[i]+ " | Best:" +BeststoeFirstplacing[i]);
        }
        System.out.println("Runden ende");
        **/
    }

    @Override
    public String toString() {
        HashMap<String, Integer> ranking = new HashMap<>();
        for (HashMap<Player, Integer> game : rankings) {
                for (Map.Entry<Player, Integer> entry : game.entrySet()) {
                    // Zählt alle Züge aus allen Runden zusammen
                    int count = ranking.getOrDefault(entry.getKey().name, 0);
                    ranking.put(entry.getKey().name, count + entry.getKey().rounds);
                }
            }

        StringBuilder string = new StringBuilder("Durchschnitt der " + NumberFormat.getIntegerInstance().format(rankings.size()) + " Runden:\n");
        Map<String, Integer> order = sortByValue(ranking, true);
        int place = 1;
        int last = 0;
        for (Map.Entry<String, Integer> entry : order.entrySet()) {
            string.append(place++ + ". " + entry.getKey() + ": " + entry.getValue() / rankings.size()
                    + " (" + NumberFormat.getIntegerInstance().format(entry.getValue()) + ") "
                    + (last > 0 ? "+" + NumberFormat.getIntegerInstance().format(entry.getValue() - last) : "")
                    + "\n");
            last = entry.getValue();
        }
        for(int i=0;i<4;i++){
            System.out.println("Platz "+(i+1)+ " | Aggresive:" +Agressiveplacing[i] + " | Defensive:"+Defensiveplacing[i]+ " | Worst:"+ WorstStoneFirstplacing[i]+ " | Best:" +BeststoeFirstplacing[i]);
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
