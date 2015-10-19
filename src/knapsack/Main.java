package knapsack;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import knapsack.generator.*;
import knapsack.generator.output.*;
import knapsack.solver.*;

public class Main {
    public static void main(String[] args) {

        /* test 13s graph. seed = 1445263733912L */
        // int maxCapacity = 40;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(1, 10, 1, 20);
        // generator.setSeed(1445273741725L);
        // KnapsackItem[] knapsack = generator.generateKnapsack(20);

        /* test 13s graph. seed = 1445263733912L */
        // int maxCapacity = 400;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(10, 100, 1, 200);
        // generator.setSeed(1445262961192L);
        // KnapsackItem[] knapsack = generator.generateKnapsack(200);

        /* test 24s branch and bound. seed = 1445263039090L */
        /* test 8s branch and bound. seed = 1445262961192L */
        /* test 0.2s branch and bound. seed = 1445263002833L */
        int maxCapacity = 2000;
        AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(40, 44, 1, 300);
        generator.setSeed(1445263039090L);
        KnapsackItem[] knapsack = generator.generateKnapsack(400);

        /* test VERY LONG branch and bound. seed = 1445262961192L */
        // int maxCapacity = 2000;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(40, 44, 120, 140);
        // generator.setSeed(1445262961192L);
        // KnapsackItem[] knapsack = generator.generateKnapsack(400);

        // AbstractKnapsackGenerator generator = new LinearKnapsackGenerator(100, 120, 2.2f);
        // KnapsackItem[] knapsack = generator.generateKnapsack(10000);






        System.out.println("Taille Knapsack :"+knapsack.length);

        // CplexDatFile.createFile(knapsack, maxCapacity, "instanceRandom400");

        /* BRANCH AND BOUND */
        // AbstractKnapsackSolver solverBab = new BabKnapsackSolver(knapsack, maxCapacity);
        // solveKnapsack(solverBab);

        /* FORWARD DYNAMIC PROGRAM */
        AbstractKnapsackSolver solverDPF = new ForwardDynamicKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverDPF);

        /* BACKWARD DYNAMIC PROGRAM */
        AbstractKnapsackSolver solverDP = new SimpleDynamicKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverDP);

        /* CORE DYNAMIC PROGRAM */
        AbstractKnapsackSolver solverCore = new CoreDynamicKnapsackSolver(knapsack, maxCapacity);
        solveKnapsack(solverCore);

        /* GRAPH SHORTEST LENGTH */
        // AbstractKnapsackSolver solverGraph = new GraphKnapsackSolver(knapsack, maxCapacity);
        // solveKnapsack(solverGraph);

        System.exit(0);
    }

    public static void solveKnapsack(AbstractKnapsackSolver solver) {
        System.out.println("*******************");
        System.out.println(solver);

        Instant startSolve = Instant.now();
        int solutionValue = solver.solve();

        // boolean[] solution = solver.getSolution();
        Instant startSolution = Instant.now();

        Instant end = Instant.now();
        System.out.println("Time solve : "+Duration.between(startSolve, startSolution));
        System.out.println("Time find solution : "+Duration.between(startSolution, end));
        System.out.println("TIME TOTAL : "+Duration.between(startSolve, end));

        System.out.println("Solution value : "+solutionValue);
        // System.out.println("Solution : "+Arrays.toString(solution));

        try {
            String memoryUsage = new String();
            List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean pool : pools) {
                MemoryUsage peak = pool.getPeakUsage();
                if (peak.getUsed() != 0) {
                    memoryUsage += String.format("Peak %s memory used: %d%n", pool.getName(),peak.getUsed());
                }
                // memoryUsage += String.format("Peak %s memory reserved: %d%n", pool.getName(), peak.getCommitted());
            }
            System.out.println(memoryUsage);
       } catch (Throwable t) {
            System.err.println("Exception in agent: " + t);
       }
    }
}