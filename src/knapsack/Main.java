package knapsack;

import java.util.Arrays;

import knapsack.generator.*;
import knapsack.solver.*;

public class Main {
    public static void main(String[] args) {

        int maxCapacity = 50;

        AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(0, 20, 0, 30);
        KnapsackItem[] knapsack = generator.generateKnapsack(20);

        // AbstractKnapsackGenerator generator = new LinearKnapsackGenerator(100, 120, 2.2f);
        // KnapsackItem[] knapsack = generator.generateKnapsack(10000);

        System.out.println("Taille Knapsack :"+knapsack.length);



        /* BRANCH AND BOUND */

        System.out.println("");
        System.out.println("Branch and bound :");
        AbstractKnapsackSolver solverBab = new BabKnapsackSolver(knapsack, maxCapacity);
        int solutionValueBab = solverBab.solve();

        boolean[] solution = solverBab.getSolution();

        System.out.println("Solution value :"+solutionValueBab);
        System.out.println("Solution :"+Arrays.toString(solution));


        /* int capacity = 0;
        KnapsackItem[] instance = solverBab.getInstance();
        for (int i = 0; i < instance.length; i++) {
            System.out.print(instance[i].weight+"/"+instance[i].profit+"/"+(float)instance[i].profit/(float)instance[i].weight+" ");
            capacity += solution[i] ? instance[i].weight : 0;
        }
        System.out.println("");
        System.out.println("Poids total :"+capacity); */



        /* BACKWARD DYNAMIC PROGRAM */
        
        System.out.println("");
        System.out.println("Backward dynamic program :");
        AbstractKnapsackSolver solverDP = new SimpleDynamicKnapsackSolver(knapsack, maxCapacity);
        int solutionValueDP = solverDP.solve();

        boolean[] solutionDP = solverDP.getSolution();

        System.out.println("Solution value :"+solutionValueDP);
        System.out.println("Solution :"+Arrays.toString(solutionDP));


        /* ((SimpleDynamicKnapsackSolver)solverDP).printMatrix(); */



        /* FORWARD DYNAMIC PROGRAM */
        System.out.println("");
        System.out.println("Forward dynamic program :");
        AbstractKnapsackSolver solverDPF = new ForwardDynamicKnapsackSolver(knapsack, maxCapacity);
        int solutionValueDPF = solverDPF.solve();

        boolean[] solutionDPF = solverDPF.getSolution();

        System.out.println("Solution value :"+solutionValueDPF);
        System.out.println("Solution :"+Arrays.toString(solutionDPF));


        System.exit(0);
    }
}