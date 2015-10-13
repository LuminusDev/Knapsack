package knapsack.generator;

import java.util.Random;

import knapsack.KnapsackItem;

public class LinearKnapsackGenerator extends AbstractKnapsackGenerator {

	private int minWeight;
	private int minProfit;
	private float correlation;
	private int currentWeight;
	
	public LinearKnapsackGenerator(int minWeight, int minProfit, float correlation) {
		this.minWeight = minWeight;
		this.minProfit = minProfit;
		this.correlation = correlation;
		this.currentWeight = 0;
	}

	// weight = x+a with x = currentWeight, a = minWeight
	// profit = ax+b with a = correlation, x = currentWeight, b = minProfit
    protected KnapsackItem generateItem() {
    	int weight = this.currentWeight + this.minWeight;
    	int profit = (int)(this.correlation*this.currentWeight) + this.minProfit;
    	KnapsackItem ki = new KnapsackItem(weight, profit);
    	this.currentWeight++;
    	return ki;
    }

    @Override
    public KnapsackItem[] generateKnapsack(int nbItems) {
    	this.currentWeight = 0;
    	return super.generateKnapsack(nbItems);
    }
}
