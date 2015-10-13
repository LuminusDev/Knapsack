package knapsack.solver;

import java.util.Collections;
import java.util.Arrays;

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
        Arrays.sort(this.instance, Collections.reverseOrder());
    }

    public int solve() {
        return solveInstance();
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
}