package knapsack;

import java.util.Arrays;

import knapsack.generator.*;
import knapsack.solver.*;

public class Main {
    public static void main(String[] args) {

        int maxCapacity = 1000;

        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(0, 10, 0, 30);
        // KnapsackItem[] knapsack = generator.generateKnapsack(10);

        AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(0, 500, 0, 1500);
        KnapsackItem[] knapsack = generator.generateKnapsack(500);

        // AbstractKnapsackGenerator generator = new LinearKnapsackGenerator(100, 120, 2.2f);
        // KnapsackItem[] knapsack = generator.generateKnapsack(10000);

        System.out.println("Taille Knapsack :"+knapsack.length);



        /* BRANCH AND BOUND */
        AbstractKnapsackSolver solverBab = new BabKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverBab);

        /* int capacity = 0;
        KnapsackItem[] instance = solverBab.getInstance();
        for (int i = 0; i < instance.length; i++) {
            System.out.print(instance[i].weight+"/"+instance[i].profit+"/"+(float)instance[i].profit/(float)instance[i].weight+" ");
            capacity += solution[i] ? instance[i].weight : 0;
        }
        System.out.println("");
        System.out.println("Poids total :"+capacity); */



        /* BACKWARD DYNAMIC PROGRAM */
        AbstractKnapsackSolver solverDP = new SimpleDynamicKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverDP);

        /* ((SimpleDynamicKnapsackSolver)solverDP).printMatrix(); */



        /* FORWARD DYNAMIC PROGRAM */
        AbstractKnapsackSolver solverDPF = new ForwardDynamicKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverDPF);

        /* GRAPH SHORTEST LENGTH */
        AbstractKnapsackSolver solverGraph = new GraphKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverGraph);


        System.exit(0);
    }

    public static void solveKnapsack(AbstractKnapsackSolver solver) {
        System.out.println("");
        System.out.println(solver);
        int solutionValue = solver.solve();

        boolean[] solution = solver.getSolution();

        System.out.println("Solution value : "+solutionValue);
        // System.out.println("Solution : "+Arrays.toString(solution));
    }
}