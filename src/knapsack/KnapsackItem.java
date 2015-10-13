package knapsack;

public class KnapsackItem implements Comparable<KnapsackItem> {
    public int weight;
    public int profit;

    public KnapsackItem(int weight, int profit) {
        this.weight = weight;
        this.profit = profit;
    }

    @Override
    public int compareTo(KnapsackItem other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than 
        // other and 0 if they are supposed to be equal
        if (this.weight == 0 && other.weight == 0) {
            return Integer.compare(this.profit, other.profit);
        } else if (this.weight == 0) {
            return other.weight < 0 ? -1 : 1;
        } else if (other.weight == 0) {
            return this.weight < 0 ? 1 : -1;
        }
        float thisRatio = (float)this.profit/(float)this.weight;
        float otherRatio = (float)other.profit/(float)other.weight;
        if (thisRatio == otherRatio) {
            return Integer.compare(this.profit, other.profit);
        }
        return Float.compare(thisRatio, otherRatio);
    }
}
