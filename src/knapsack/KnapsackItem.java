package knapsack;

import java.util.Comparator;

public class KnapsackItem {
    public int weight;
    public int profit;

    public KnapsackItem(int weight, int profit) {
        this.weight = weight;
        this.profit = profit;
    }

    static public Comparator<KnapsackItem> comparatorByRatio() {
        return new KnapsackItemComparatorByRatio();
    }

    static public Comparator<KnapsackItem> comparatorByProfit() {
        return new KnapsackItemComparatorByProfit();
    }

    static public Comparator<KnapsackItem> comparatorByWeight() {
        return new KnapsackItemComparatorByWeight();
    }
}

class KnapsackItemComparatorByProfit implements Comparator<KnapsackItem> {
    @Override
    public int compare(KnapsackItem o1, KnapsackItem o2) {
        return o1.profit - o2.profit;
    }
}

class KnapsackItemComparatorByWeight implements Comparator<KnapsackItem> {
    @Override
    public int compare(KnapsackItem o1, KnapsackItem o2) {
        return o1.weight - o2.weight;
    }
}

class KnapsackItemComparatorByRatio implements Comparator<KnapsackItem> {
    @Override
    public int compare(KnapsackItem o1, KnapsackItem o2) {
        // compareTo should return < 0 if this is supposed to be
        // less than o2, > 0 if this is supposed to be greater than 
        // o2 and 0 if they are supposed to be equal
        if (o1.weight == 0 && o2.weight == 0) {
            return Integer.compare(o1.profit, o2.profit);
        } else if (o1.weight == 0) {
            return o2.weight < 0 ? -1 : 1;
        } else if (o2.weight == 0) {
            return o1.weight < 0 ? 1 : -1;
        }
        float thisRatio = (float)o1.profit/(float)o1.weight;
        float otherRatio = (float)o2.profit/(float)o2.weight;
        if (thisRatio == otherRatio) {
            return Integer.compare(o1.profit, o2.profit);
        }
        return Float.compare(thisRatio, otherRatio);
    }
}