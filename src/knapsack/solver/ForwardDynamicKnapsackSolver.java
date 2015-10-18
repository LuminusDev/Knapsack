package knapsack.solver;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.*;

import knapsack.KnapsackItem;

public class ForwardDynamicKnapsackSolver extends AbstractKnapsackSolver {

    private ArrayList<ItemState> listStates;

    public ForwardDynamicKnapsackSolver(KnapsackItem[] instance, int capacity) {
        super(instance, capacity);
        this.solverName = "Forward Dynamic Program";
    }

    private boolean stateIsDominated(ItemState testedItem, ItemState initialItem) {
    	return initialItem.weight <= testedItem.weight && initialItem.profit >= testedItem.profit;
    }
    
    protected int solveInstance() {
        this.solution = null;
        this.solutionValue = 0;

        int lowerBound = valueGreedyInteger();
        System.out.println("LB: "+lowerBound);

        listStates = new ArrayList<ItemState>();
        listStates.add(new ItemState(0,0, null)); // initial state

        ArrayList<ItemState> tmpListStates;

        // fill
        int valueTakeItem = 0;
        int valueDontTakeItem = 0;
        for (int k = 0; k < this.instance.length; k++) {
        	tmpListStates = new ArrayList<ItemState>();
            Iterator<ItemState> ik = listStates.iterator();
            while (ik.hasNext()) {
                ItemState item = ik.next();
                // prune by bound
                if (item.profit + this.solveRelaxation(k, this.instance.length, this.capacity-item.weight) < lowerBound) {
                    ik.remove();
                } else if (item.weight + this.instance[k].weight <= capacity) {
                    // merge by dominance
                    ItemState tmpState = new ItemState(item.weight + this.instance[k].weight, item.profit + this.instance[k].profit, item);
                    boolean isDominated = false;
                    for (ItemState itemDominance : listStates) {
                        if (stateIsDominated(tmpState, itemDominance)) {
                            isDominated = true;
                            break;
                        }
                    }
                    if (! isDominated) {
                        for (ItemState itemDominance : tmpListStates) {
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
            for (ItemState newItem : tmpListStates) {
                Iterator<ItemState> i = listStates.iterator();
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

        System.out.println("In List : "+listStates.size());

        return this.solutionValue;
    }

    @Override
    public boolean[] getSolution() {
        if (this.solution == null) {
            this.solution = new boolean[this.instance.length];

            ItemState currentState = listStates.stream()
                .filter(e -> e.profit == this.solutionValue)
                .min(KnapsackItem.comparatorByWeight()).get();

            int itemProfit = 0;
            int itemWeight = 0;
            if (currentState.lastState != null) {
                itemProfit = currentState.profit - currentState.lastState.profit;
                itemWeight = currentState.weight - currentState.lastState.weight;
            }

            for (int k = this.instance.length-1; k >= 0; k--) {
                if (currentState != null && this.instance[k].profit == itemProfit && this.instance[k].weight == itemWeight) {
                    this.solution[k] = true;
                    currentState = currentState.lastState;
                    if (currentState.lastState != null) {
                        itemProfit = currentState.profit - currentState.lastState.profit;
                        itemWeight = currentState.weight - currentState.lastState.weight;
                    }
                } else {
                    this.solution[k] = false;
                }
            }
        }
        return this.solution;
    }
}

class ItemState extends KnapsackItem {
    public ItemState lastState;

    public ItemState(int weight, int profit, ItemState lastState) {
        super(weight, profit);
        this.lastState = lastState;
    }
}