package knapsack.generator;

import java.util.Random;

import knapsack.KnapsackItem;

public abstract class AbstractKnapsackGenerator {

	protected Random random;

	protected abstract KnapsackItem generateItem();

	public AbstractKnapsackGenerator() {
        long rgenseed = System.currentTimeMillis();
        System.out.println("Seed used : "+rgenseed);
        this.random = new Random(rgenseed);
    }

    public void setSeed(long seed) {
        System.out.println("Seed used : "+seed);
        this.random.setSeed(seed);
    }

    public KnapsackItem[] generateKnapsack(int nbItems) {
    	KnapsackItem[] knapsackDataItems = new KnapsackItem[nbItems];
    	for (int i = 0; i < nbItems; i++) {
    		knapsackDataItems[i] = generateItem();
    	}
    	return knapsackDataItems;
    }
}
