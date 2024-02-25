package task;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Map<Integer, Map<Long, Integer>> requestForBuy = new HashMap<>();
        Map<Integer, Map<Long, Integer>> requestForSale = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = input.split(";");
            processInput(parts, requestForBuy, requestForSale);
        }
    }

    public static void processInput(String[] parts, Map<Integer, Map<Long, Integer>> requestForBuy,
                                     Map<Integer, Map<Long, Integer>> requestForSale) {
        String userId = parts[0];
        String clOrderId = parts[1];
        char action = parts[2].charAt(0);
        int instrumentId = Integer.parseInt(parts[3]);
        char side = parts[4].charAt(0);
        long price = Long.parseLong(parts[5]);
        int amount = Integer.parseInt(parts[6]);
        int amountRest = Integer.parseInt(parts[7]);

//        long startProcess = System.nanoTime();

        Map<Integer, Map<Long, Integer>> requests = (side == 'B') ? requestForBuy : requestForSale;

        if (!requests.containsKey(instrumentId)) {

            requests.put(instrumentId, new HashMap<>());

        }

        Map<Long, Integer> prices = requests.get(instrumentId);

        if (action == '0') {

            prices.put(price, prices.getOrDefault(price, 0) + amount);

        } else if (action == '1' || action == '2') {

            int currentAmount = prices.get(price) - amount;

            if (currentAmount == 0) {

                prices.remove(price);

            } else {

                prices.computeIfPresent(price, (k, v) -> v - amount);

            }
//            removePricesWithZeroAmount(prices);
        }

        long bestPrice = findBestPrice(side, prices);

        int amountWithBestPrice = prices.getOrDefault(bestPrice, 0);

        if ((side == 'B' && price < bestPrice) || (side == 'S' && price > bestPrice)) {

            System.out.println("-");

        } else {

            System.out.println(instrumentId + ";" + side + ";" + bestPrice + ";" + amountWithBestPrice);

        }

//        System.out.println("Time: " + (System.nanoTime() - startProcess));
    }

    private static long findBestPrice(char side, Map<Long, Integer> prices) {

        return side == 'B' ?
                prices.keySet().stream().max(Long::compareTo).orElse(0L) :
                prices.keySet().stream().min(Long::compareTo).orElse(999999999999999999L);

    }

    private static void removePricesWithZeroAmount(Map<Long, Integer> prices) {

        prices.entrySet().removeIf(entry -> entry.getValue() == 0);

    }
}
