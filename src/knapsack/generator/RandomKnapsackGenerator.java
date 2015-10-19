package knapsack.generator;

import java.util.Random;

import knapsack.KnapsackItem;

public class RandomKnapsackGenerator extends AbstractKnapsackGenerator {
	
	private int minWeight;
	private int maxWeight;
	private int minProfit;
	private int maxProfit;

	public RandomKnapsackGenerator(int minWeight, int maxWeight, int minProfit, int maxProfit) {
		super();
		this.minWeight = minWeight;
		this.maxWeight = maxWeight;
		this.minProfit = minProfit;
		this.maxProfit = maxProfit;
	}

    protected KnapsackItem generateItem() {
    	int weight = this.random.nextInt(this.maxWeight - this.minWeight + 1) + this.minWeight;
    	int profit = this.random.nextInt(this.maxProfit - this.minProfit + 1) + this.minProfit;
    	KnapsackItem ki = new KnapsackItem(weight, profit);
    	return ki;
    }
}
