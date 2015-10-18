package knapsack.solver;

import java.util.Collections;
import java.util.Arrays;
import java.util.stream.*;

import knapsack.KnapsackItem;

public class SimpleDynamicKnapsackSolver extends AbstractKnapsackSolver {

    private int[][] solutionValueMatrix;

    public SimpleDynamicKnapsackSolver(KnapsackItem[] instance, int capacity) {
        super(instance, capacity);
        this.solverName = "Backward Dynamic Program";
    }
    
    protected int solveInstance() {
        this.solution = null;
        this.solutionValue = 0;

        solutionValueMatrix = new int[this.instance.length+1][capacity+1];

        // initialization
        for (int b = 0; b <= capacity; b++) {
            solutionValueMatrix[0][b] = 0;
        }

        // fill
        int valueTakeItem = 0;
        int valueDontTakeItem = 0;
        for (int k = 0; k < this.instance.length; k++) {
            for (int b = capacity; b >= this.instance[k].weight; b--) {
                valueDontTakeItem = solutionValueMatrix[k][b];

                valueTakeItem =
                    solutionValueMatrix[k][b - this.instance[k].weight] + this.instance[k].profit > solutionValueMatrix[k][b]
                    ? solutionValueMatrix[k][b - this.instance[k].weight] + this.instance[k].profit
                    : Integer.MIN_VALUE;

                solutionValueMatrix[k+1][b] = Math.max(valueDontTakeItem, valueTakeItem);
            }
            for (int b = 0; b < Math.min(this.instance[k].weight, capacity+1); b++) {
                solutionValueMatrix[k+1][b] = solutionValueMatrix[k][b];
            }
        }

        this.solutionValue = Arrays.stream(solutionValueMatrix[this.instance.length])
                .boxed()
                .mapToInt(e -> e)
                .max()
                .getAsInt();

        return this.solutionValue;
    }

    @Override
    public boolean[] getSolution() {
        if (this.solution == null) {
            this.solution = new boolean[this.instance.length];
            int b = capacity;
            for (int k = this.instance.length; k > 0; k--) {
                if (this.instance[k-1].weight <= b && solutionValueMatrix[k][b] - this.instance[k-1].profit == solutionValueMatrix[k-1][b - this.instance[k-1].weight]) {
                    this.solution[k-1] = true;
                    b -= this.instance[k-1].weight;
                } else {
                    this.solution[k-1] = false;
                }
            }
        }
        return this.solution;
    }

    public void printMatrix() {
        for (int k = 0; k <= this.instance.length; k++) {
            System.out.print("k = "+k+" ");
            for (int b = 0; b <= capacity; b++) {
                System.out.print(solutionValueMatrix[k][b]+" ");
            }
            System.out.println(" ");
        } 
    }
}
