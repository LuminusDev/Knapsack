package knapsack.solver;

import knapsack.KnapsackItem;

public class BabKnapsackSolver extends AbstractKnapsackSolver {

    public BabKnapsackSolver(KnapsackItem[] instance, int capacity) {
        super(instance, capacity);
        this.solverName = "Branch And Bound";
    }
    
    protected int solveInstance() {
        this.solution = new boolean[this.instance.length];
        this.solutionValue = 0;

        boolean[] currentSolution = new boolean[this.instance.length];
        int upperBound = Integer.MAX_VALUE;
        int lowerBound = 0;
        int currentCapacity = this.capacity;
        int currentProfit = 0;
        int currentItem = 0;

        // Arrays.sort(this.instance, Collections.reverseOrder());
        this.sortInstanceByRatio();

        depthSearch:do {
            //compute UB = z + PL(i; n; c) (linear relaxation between i and n with c capacity)
            upperBound = currentProfit + (int)solveRelaxation(currentItem, this.instance.length, currentCapacity);
            //  goto backtrack if UB <= LB
            if (upperBound > lowerBound) {
                //dive to 1
                //  while i <= n & c >= wi
                while (currentItem < this.instance.length && currentCapacity >= this.instance[currentItem].weight) {
                    //  xi = 1, z = z + pi , c = c - wi , i = i + 1
                    currentSolution[currentItem] = true;
                    currentProfit += this.instance[currentItem].profit;
                    currentCapacity -= this.instance[currentItem].weight;
                    currentItem++;
                }
                //fix OutOfBound
                if (currentItem == this.instance.length) {
                    currentItem--;
                }
                //fixe to 0
                //  If (i < n)
                if (currentItem < this.instance.length - 1) {
                    //  set xi = 0, i = i + 1 & goto Compute UB
                    currentSolution[currentItem] = false;
                    currentItem++;
                    continue depthSearch;
                }
                //If (i = n & z > LB)
                if (currentItem == this.instance.length - 1 && currentProfit > lowerBound) {
                    // x* = x, LB = z.
                    lowerBound = currentProfit;
                    this.solution = currentSolution.clone();
                }
            }
            //Backtrack
            //  If (i = n)
            if (currentItem == this.instance.length - 1 && currentSolution[currentItem] == true) {
                //  set xn = 0, z = z - pn, c = c + wn, i = i - 1
                currentSolution[currentItem] = false;
                currentProfit -= this.instance[currentItem].profit;
                currentCapacity += this.instance[currentItem].weight;
                currentItem--;
            }
            //  While xi = 0, do i = i - 1. If i = 0, STOP
            while (currentSolution[currentItem] == false) {
                if (--currentItem == 0) {
                    break depthSearch;
                }
            }
            //  Set xi = 0, z = z -  pi, c = c + wi , i = i + 1.
            currentSolution[currentItem] = false;
            currentProfit -= this.instance[currentItem].profit;
            currentCapacity += this.instance[currentItem].weight;
            currentItem++;
            //  Goto compute UB
        } while (currentItem != 0);

        this.solutionValue = lowerBound;
        return lowerBound;
    }
}
