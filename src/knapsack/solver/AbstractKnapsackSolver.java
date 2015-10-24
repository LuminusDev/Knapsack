package knapsack.solver;

import java.util.Arrays;
import java.util.Collections;

import knapsack.KnapsackItem;

public abstract class AbstractKnapsackSolver {

    protected KnapsackItem[] instance;
    protected int capacity;
    protected boolean[] solution;
    protected int solutionValue;

    protected abstract int solveInstance();
    
    public AbstractKnapsackSolver(KnapsackItem[] instance, int capacity) {
        this.instance = instance;
        this.capacity = capacity;
    }

    public boolean[] getSolution() {
        return this.solution;
    }

    public KnapsackItem[] getInstance() {
        return this.instance;
    }

    //sort by profit/weight descending
    //change initial array
    public void sortInstanceByRatio() {
        Arrays.sort(this.instance, Collections.reverseOrder(KnapsackItem.comparatorByRatio()));
    }

    //sort by profit/weight descending
    //change initial array
    public void sortInstanceByProfit() {
        Arrays.sort(this.instance, Collections.reverseOrder(KnapsackItem.comparatorByProfit()));
    }

    public int solve() {
        return solveInstance();
    }

    public int getCriticItem() {
        int currentCapacity = this.capacity;
        for (int i = 0; i < this.instance.length; i++) {
            if (this.instance[i].weight >= currentCapacity) {
                return i;
            } else {
                currentCapacity -= this.instance[i].weight;
            }
        }
        return -1;
    }

    public float solveRelaxation(int idxFirstItem, int idxSecondItem, int maxCapacity) {
        float solution = 0;
        int currentCapacity = maxCapacity;
        for (int i = idxFirstItem; i < idxSecondItem; i++) {
            if (this.instance[i].weight >= currentCapacity) {
                //we take what we can and lets go
                solution += this.instance[i].profit * (float)currentCapacity / (float)this.instance[i].weight;
                break;
            } else {
                solution += this.instance[i].profit;
                currentCapacity -= this.instance[i].weight;
            }
        }
        return solution;
    }

    public float solveRelaxation(int idxFirstItem, int idxSecondItem, int idxThirdItem, int idxFourthItem, int maxCapacity) {
        float solution = 0;
        int currentCapacity = maxCapacity;
        for (int i = idxFirstItem; i < idxSecondItem; i++) {
            if (this.instance[i].weight >= currentCapacity) {
                //we take what we can and lets go
                solution += this.instance[i].profit * (float)currentCapacity / (float)this.instance[i].weight;
                currentCapacity = 0;
                break;
            } else {
                solution += this.instance[i].profit;
                currentCapacity -= this.instance[i].weight;
            }
        }
        for (int i = idxThirdItem; i < idxFourthItem && currentCapacity != 0; i++) {
            if (this.instance[i].weight >= currentCapacity) {
                //we take what we can and lets go
                solution += this.instance[i].profit * (float)currentCapacity / (float)this.instance[i].weight;
                break;
            } else {
                solution += this.instance[i].profit;
                currentCapacity -= this.instance[i].weight;
            }
        }
        return solution;
    }

    public int valueGreedyInteger() {
        this.sortInstanceByProfit();
        int solutionProfit = solveGreedyInteger();
        this.sortInstanceByRatio();
        int solutionRatio = solveGreedyInteger();
        return Integer.max(solutionRatio, solutionProfit);
    }

    public int solveGreedyInteger() {
        int solution = 0;
        int currentCapacity = this.capacity;
        for (int i = 0; i < this.instance.length; i++) {
            if (this.instance[i].weight <= currentCapacity) {
                solution += this.instance[i].profit;
                currentCapacity -= this.instance[i].weight;
            }
        }
        return solution;
    }
}