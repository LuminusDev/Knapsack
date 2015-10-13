package knapsack.generator;

import java.util.Random;

import knapsack.KnapsackItem;

public abstract class AbstractKnapsackGenerator {

	protected Random random;

	protected abstract KnapsackItem generateItem();

	public AbstractKnapsackGenerator() {
		this.random = new Random();
	}

    public void setSeed(long seed) {
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
