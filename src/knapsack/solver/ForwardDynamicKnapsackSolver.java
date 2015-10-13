package knapsack.solver;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.*;

import knapsack.KnapsackItem;

public class ForwardDynamicKnapsackSolver extends AbstractKnapsackSolver {

    private ArrayList<KnapsackItem> listStates;

    public ForwardDynamicKnapsackSolver(KnapsackItem[] instance, int capacity) {
        super(instance, capacity);
    }

    private boolean stateIsDominated(KnapsackItem testedItem, KnapsackItem initialItem) {
    	return initialItem.weight <= testedItem.weight && initialItem.profit >= testedItem.profit;
    }
    
    protected int solveInstance() {
        this.solution = null;
        this.solutionValue = 0;

        listStates = new ArrayList<KnapsackItem>();
        listStates.add(new KnapsackItem(0,0)); // initial state

        ArrayList<KnapsackItem> tmpListStates;

        // fill
        int valueTakeItem = 0;
        int valueDontTakeItem = 0;
        for (int k = 0; k < this.instance.length; k++) {
        	tmpListStates = new ArrayList<KnapsackItem>();
            for (KnapsackItem item : listStates) {
                if (item.weight + this.instance[k].weight <= capacity) {
                    // merge by dominance
                    KnapsackItem tmpState = new KnapsackItem(item.weight + this.instance[k].weight, item.profit + this.instance[k].profit);
                    boolean isDominated = false;
                    for (KnapsackItem itemDominance : listStates) {
                        if (stateIsDominated(tmpState, itemDominance)) {
                            isDominated = true;
                            break;
                        }
                    }
                    if (! isDominated) {
                        for (KnapsackItem itemDominance : tmpListStates) {
                            if (stateIsDominated(tmpState, itemDominance)) {
                                isDominated = true;
                                break;
                            }
                        }
                    }
                    if (! isDominated) {
                        tmpListStates.add(tmpState);
                    }
                }
            }
            // delete old state dominated by new state
            for (KnapsackItem newItem : tmpListStates) {
                Iterator<KnapsackItem> i = listStates.iterator();
                while (i.hasNext()) {
                    if (stateIsDominated(i.next(), newItem)) {
                        i.remove();
                    }
                }
                // merge
                listStates.add(newItem);
            }
        }

        this.solutionValue = listStates.stream()
                .mapToInt(e -> e.profit)
                .max()
                .getAsInt();

        // System.out.println("In List : "+listStates.size());

        return this.solutionValue;
    }

    @Override
    public boolean[] getSolution() {
        if (this.solution == null) {
            this.solution = new boolean[this.instance.length];
            
            int currentProfit = this.solutionValue;
            int currentCapacity = listStates.stream()
                .filter(e -> e.profit == this.solutionValue)
                .mapToInt(e -> e.weight)
                .min()
                .getAsInt();

            for (int k = this.instance.length-1; k >= 0; k--) {
                int k2 = k;
                int currentProfit2 = currentProfit;
                int currentCapacity2 = currentCapacity;
                if (listStates.stream().anyMatch(e -> (e.profit == currentProfit2-this.instance[k2].profit && e.weight == currentCapacity2-this.instance[k2].weight))) {
                    this.solution[k] = true;
                    currentCapacity -= this.instance[k].weight;
                    currentProfit -= this.instance[k].profit;
                } else {
                    this.solution[k] = false;
                }
            }
        }
        return this.solution;
    }
}
