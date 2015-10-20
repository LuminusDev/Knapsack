package knapsack.solver;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.*;

import knapsack.KnapsackItem;

public class CoreDynamicKnapsackSolver extends AbstractKnapsackSolver {

    private ArrayList<ItemStateCore> listStates;

    public CoreDynamicKnapsackSolver(KnapsackItem[] instance, int capacity) {
        super(instance, capacity);
        this.solverName = "Core Dynamic Program";
    }

    private boolean stateIsDominated(ItemStateCore testedItem, ItemStateCore initialItem) {
        return (initialItem.weight < testedItem.weight && initialItem.profit >= testedItem.profit)
            || (initialItem.weight == testedItem.weight && initialItem.profit > testedItem.profit);
    }

    private int sumProfitTo(int a) {
        int sum = 0;
        for (int i = 0; i < a; i++) {
            sum += this.instance[i].profit;
        }
        return sum;
    }

    private int sumWeightTo(int a) {
        int sum = 0;
        for (int i = 0; i < a; i++) {
            sum += this.instance[i].weight;
        }
        return sum; 
    }

    private int calculateLowerBound(ArrayList<ItemStateCore> listStates) {
        int lowerBound = -1;
        try{
             lowerBound = listStates.stream()
                .filter(e -> e.weight <= this.capacity)
                .mapToInt(e -> e.profit)
                .max()
                .getAsInt();
        } catch(Exception e){}
        return lowerBound;
    }

    private int getUpperBound(ItemStateCore item) {
        return item.profit - sumProfitTo(item.a) + (int)solveRelaxation(0, item.a, item.b+1, this.instance.length, this.capacity - item.weight + sumWeightTo(item.a));
    }
    
    protected int solveInstance() {
        this.solution = null;
        this.solutionValue = 0;

        this.sortInstanceByRatio();
        int c = this.getCriticItem();

        listStates = new ArrayList<ItemStateCore>();
        listStates.add(new ItemStateCore(c, c-1, sumWeightTo(c), sumProfitTo(c), null)); // initial state
        
        int lowerBound = sumProfitTo(c);
        System.out.println("LB: "+lowerBound);

        ArrayList<ItemStateCore> tmpListStates;

        int a = c;
        int b = c-1;
        while(a > 0 || b < this.instance.length-1) {
            if (a > 0) {
                a--;
                tmpListStates = new ArrayList<ItemStateCore>();
                for (ItemStateCore item : listStates) {
                    tmpListStates.add(new ItemStateCore(item.a-1, item.b, item.weight, item.profit, item));
                    tmpListStates.add(new ItemStateCore(item.a-1, item.b, item.weight - this.instance[item.a-1].weight, item.profit - this.instance[item.a-1].profit, item));

                }
                lowerBound = calculateLowerBound(tmpListStates);
                // eliminer par borne et dominance
                Iterator<ItemStateCore> ik = tmpListStates.iterator();
                while (ik.hasNext()) {
                    ItemStateCore item = ik.next();
                    if (getUpperBound(item) < lowerBound) {
                        ik.remove();
                    } else {
                        for (ItemStateCore tmpItem : tmpListStates) {
                            if (stateIsDominated(item, tmpItem)) {
                                ik.remove();
                                break;
                            }
                        }
                    }
                }
                listStates = tmpListStates;
            }
            if (b < this.instance.length-1) {
                b++;
                tmpListStates = new ArrayList<ItemStateCore>();
                for (ItemStateCore item : listStates) {
                    tmpListStates.add(new ItemStateCore(item.a, item.b+1, item.weight, item.profit, item));
                    int w2 = item.weight - sumWeightTo(item.a);
                    if (w2 + this.instance[item.b+1].weight <= this.capacity) {
                        tmpListStates.add(new ItemStateCore(item.a, item.b+1, item.weight + this.instance[item.b+1].weight, item.profit + this.instance[item.b+1].profit, item));
                    }
                }
                lowerBound = calculateLowerBound(tmpListStates);
                // eliminer par borne et dominance
                Iterator<ItemStateCore> ik = tmpListStates.iterator();
                while (ik.hasNext()) {
                    ItemStateCore item = ik.next();
                    if (getUpperBound(item) < lowerBound) {
                        ik.remove();
                    } else {
                        for (ItemStateCore tmpItem : tmpListStates) {
                            if (stateIsDominated(item, tmpItem)) {
                                ik.remove();
                                break;
                            }
                        }
                    }
                }
                listStates = tmpListStates;
            }
        }

        this.solutionValue = lowerBound;

        System.out.println("In List : "+listStates.size());

        return this.solutionValue;
    }

    @Override
    public boolean[] getSolution() {
        if (this.solution == null) {
            this.solution = new boolean[this.instance.length];

            ItemStateCore currentState = listStates.stream()
                .filter(e -> e.profit == this.solutionValue)
                .min(KnapsackItem.comparatorByWeight()).get();

            int idxItem;

            while (currentState.lastState != null) {
                idxItem = currentState.a != currentState.lastState.a ? currentState.a : currentState.b;
                if (idxItem == currentState.a) {
                    if (currentState.profit == currentState.lastState.profit && currentState.weight == currentState.lastState.weight) {
                        this.solution[idxItem] = true;
                    } else {
                        this.solution[idxItem] = false;
                    }
                } else {
                    if (currentState.profit != currentState.lastState.profit && currentState.weight != currentState.lastState.weight) {
                        this.solution[idxItem] = true;
                    } else {
                        this.solution[idxItem] = false;
                    }
                }
                currentState = currentState.lastState;
            }
        }
        return this.solution;
    }
}

class ItemStateCore extends KnapsackItem {
    public int a;
    public int b;
    public ItemStateCore lastState;

    public ItemStateCore(int a, int b, int weight, int profit, ItemStateCore lastState) {
        super(weight, profit);
        this.a = a;
        this.b = b;
        this.lastState = lastState;
    }
}