package task;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class MainWithTreeMap {
    public static void main(String[] args) {

        Map<Integer, TreeMap<Long, Integer>> requestForBuy = new HashMap<>();
        Map<Integer, TreeMap<Long, Integer>> requestForSale = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = input.split(";");
            processInput(parts, requestForBuy, requestForSale);
        }
    }

    public static void processInput(String[] parts, Map<Integer, TreeMap<Long, Integer>> requestForBuy,
                                    Map<Integer, TreeMap<Long, Integer>> requestForSale) {
        String userId = parts[0];
        String clOrderId = parts[1];
        char action = parts[2].charAt(0);
        int instrumentId = Integer.parseInt(parts[3]);
        char side = parts[4].charAt(0);
        long price = Long.parseLong(parts[5]);
        int amount = Integer.parseInt(parts[6]);
        int amountRest = Integer.parseInt(parts[7]);

//        long startProcess = System.nanoTime();

        Map<Integer, TreeMap<Long, Integer>> requests = (side == 'B') ? requestForBuy : requestForSale;

        if (!requests.containsKey(instrumentId)) {

            requests.put(instrumentId, new TreeMap<>());

        }

        TreeMap<Long, Integer> prices = requests.get(instrumentId);

        if (action == '0') {

            prices.put(price, prices.getOrDefault(price, 0) + amount);

        } else if (action == '1' || action == '2') {

            prices.compute(price, (k, v) -> (v != null && v - amount == 0) ? null : v - amount);

        }

        long bestPrice = findBestPrice(side, prices);

        int amountWithBestPrice = prices.getOrDefault(bestPrice, 0);

        String output = (side == 'B' && price < bestPrice) || (side == 'S' && price > bestPrice) ? "-" :
                        instrumentId + ";" + side + ";" + bestPrice + ";" + amountWithBestPrice;

        System.out.println(output);

//        System.out.println("Time: " + (System.nanoTime() - startProcess));
    }

    private static long findBestPrice(char side, TreeMap<Long, Integer> prices) {

        return side == 'B' ? (prices.isEmpty() ? 0 : prices.lastKey()) :
                             (prices.isEmpty() ? 999999999999999999L : prices.firstKey());

    }
}
