package task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Мапы для хранения инструментов с ценами и количеством заявок по этим ценам
        Map<Integer, Map<Long, Integer>> requestForBuy = new HashMap<>();
        Map<Integer, Map<Long, Integer>> requestForSale = new HashMap<>();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = input.split(";");

            String userId = parts[0];
            String clOrderId = parts[1];
            char action = parts[2].charAt(0);
            int instrumentId = Integer.parseInt(parts[3]);
            char side = parts[4].charAt(0);
            long price = Long.parseLong(parts[5]);
            int amount = Integer.parseInt(parts[6]);
            int amountRest = Integer.parseInt(parts[7]);

            // С чем работаем
            Map<Integer, Map<Long, Integer>> requests = side == 'B' ? requestForBuy : requestForSale;

            if (requests.containsKey(instrumentId)) {

                Map<Long, Integer> prices = requests.get(instrumentId);

                if (action == '0') {

                    if (prices.containsKey(price)) {

                        prices.compute(price, (k, v) -> v + amount);

                    } else {

                        prices.put(price, amount);

                    }

                } else if (action == '1' || action == '2') {

                    // Снятия или сведение заявки
                    prices.computeIfPresent(price, (k, v) -> v - amount);

                    removePricesWithZeroAmount(prices);

                }
            } else {

                HashMap<Long, Integer> priceAmountHashMap = new HashMap<>();

                priceAmountHashMap.put(price, amount);

                requests.put(instrumentId, priceAmountHashMap);

            }

            long bestPrice = findBestPrice(side, requests.get(instrumentId));

            int amountWithBestPrice = 0;

            if (bestPrice != 0L && bestPrice != 999999999999999999L) {

                amountWithBestPrice = requests.get(instrumentId).get(bestPrice);

            }

            if (side == 'B') {

                if (price < bestPrice) {
                    System.out.println("-");
                } else {
                    System.out.println(instrumentId + ";" + side + ";" + bestPrice + ";" + amountWithBestPrice);
                }

            } else {

                if (price > bestPrice) {
                    System.out.println("-");
                } else {
                    System.out.println(instrumentId + ";" + side + ";" + bestPrice + ";" + amountWithBestPrice);
                }
            }
        }
    }

    private static long findBestPrice(char side, Map<Long, Integer> longIntegerMap) {
        if (side == 'B') {

            return longIntegerMap.keySet()
                    .stream()
                    .max((b, c) -> (int) (b - c))
                    .orElse(0L);

        } else {

            return longIntegerMap.keySet()
                    .stream()
                    .max((b, c) -> (int) (c - b))
                    .orElse(999999999999999999L);

        }
    }

    private static void removePricesWithZeroAmount(Map<Long, Integer> prices) {

        Iterator<Map.Entry<Long, Integer>> iterator = prices.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<Long, Integer> entry = iterator.next();

            if (entry.getValue() == 0) {

                iterator.remove();

            }
        }
    }
}
