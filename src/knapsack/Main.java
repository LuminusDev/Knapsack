package knapsack;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import knapsack.generator.*;
import knapsack.generator.output.*;
import knapsack.solver.*;

public class Main {
    public static void main(String[] args) {

        /* test */
        // int maxCapacity = 300000;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(1, 10000, 1, 5000);
        // // generator.setSeed(1445273741725L);
        // KnapsackItem[] knapsack = generator.generateKnapsack(500000);

        // int maxCapacity = 32000;
        // int nbItems = 16000;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(1, 8000, 1, 16000);
        // multipleSolve(generator, maxCapacity, nbItems, 10);

        /* test 24s branch and bound. seed = 1445263039090L */
        /* test 8s branch and bound. seed = 1445262961192L */
        /* test 0.2s branch and bound. seed = 1445263002833L */
        int nbItems = 10000;
        int maxCapacity = nbItems*5;
        AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(nbItems/10, nbItems/10+nbItems/100, 1, nbItems);
        // generator.setSeed(1445263002833L);
        multipleSolve(generator, maxCapacity, nbItems, 10);

        /* test VERY LONG branch and bound. seed = 1445262961192L */
        // int maxCapacity = 2000;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(40, 44, 120, 140);
        // generator.setSeed(1445262961192L);
        // KnapsackItem[] knapsack = generator.generateKnapsack(400);
        // outputRandomGeneratorLatex(maxCapacity, 400, 40, 44, 120, 140);

        /* test linear 1s backward */
        // int maxCapacity = 20000;
        // AbstractKnapsackGenerator generator = new LinearKnapsackGenerator(100, 5000, -0.2f);
        // KnapsackItem[] knapsack = generator.generateKnapsack(10000);
        // outputLinearGeneratorLatex(maxCapacity, 10000, 100, 5000, -0.2f);

        // int nbItems = 5000000;
        // int maxCapacity = 5*nbItems;
        // AbstractKnapsackGenerator generator = new LinearKnapsackGenerator(1, 1, 0.2f);
        // multipleSolve(generator, maxCapacity, nbItems, 10);

        /* test 39s branch and bound. seed = 1445350665030L*/
        // int maxCapacity = 20000;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(200, 1000, 500, 550);
        // generator.setSeed(1445350665030L);
        // KnapsackItem[] knapsack = generator.generateKnapsack(1000);

        /* test linear 3m36 forward (trop long core) backward immediat solution = relaxation pourtant */
        /* branch and bound immediat */
        // int nbItems = 10000000;
        // int maxCapacity = 2*nbItems;
        // AbstractKnapsackGenerator generator = new LinearKnapsackGenerator(1, 1, 1f);
        // multipleSolve(generator, maxCapacity, nbItems, 10);

        /* test */
        // int maxCapacity = 50000;
        // int nbItems = 100000;
        // AbstractKnapsackGenerator generator = new RandomKnapsackGenerator(1, 500, 1, 500);
        // // generator.setSeed(1445273741725L);
        // multipleSolve(generator, maxCapacity, nbItems, 50);


        System.exit(0);
    }

    public static class SolverData {
        public String name;
        public Constructor<?> constructor;
        public long time;
        public long nbTime;
        public long minTime;
        public long maxTime;

        public SolverData(String name, Constructor<?> constructor) {
            this.name = name;
            this.constructor = constructor;
            time = 0;
            nbTime = 0;
            minTime = Long.MAX_VALUE;
            maxTime = 0;
        }
    }

    public static void multipleSolve(AbstractKnapsackGenerator generator, int capacity, int nbItem, int nbRepetition) {
        System.out.println("Taille Knapsack :"+nbItem);
        System.out.println("Capacite Knapsack :"+capacity);

        KnapsackItem[] knapsack;

        ArrayList<SolverData> solvers = new ArrayList<SolverData>();
        try {
            solvers.add(new SolverData("Forward Dynamic Program", Class.forName("knapsack.solver.ForwardDynamicKnapsackSolver").getConstructors()[0]));
            // solvers.add(new SolverData("Branch And Bound",Class.forName("knapsack.solver.BabKnapsackSolver").getConstructors()[0]));
            // solvers.add(new SolverData("Backward Dynamic Program",Class.forName("knapsack.solver.SimpleDynamicKnapsackSolver").getConstructors()[0]));
            // solvers.add(new SolverData("Core Dynamic Program",Class.forName("knapsack.solver.CoreDynamicKnapsackSolver").getConstructors()[0]));
            // solvers.add(new SolverData("PCC in Graph",Class.forName("knapsack.solver.GraphKnapsackSolver").getConstructors()[0]));
        } catch(Exception e){System.out.println(e);}

        for (int i = 0; i < nbRepetition ; i++) {
            knapsack = generator.generateKnapsack(nbItem);
            for (SolverData solverData : solvers) {
                try {
                    AbstractKnapsackSolver solver = (AbstractKnapsackSolver)solverData.constructor.newInstance(knapsack, capacity);
                    long g = solveKnapsack(solver);
                    solverData.time += g;
                    solverData.nbTime++;
                    solverData.minTime = Math.min(g, solverData.minTime);
                    solverData.maxTime = Math.max(g, solverData.maxTime);
                } catch(Exception e){System.out.println(e);}
            }
            // CplexDatFile.createFile(knapsack, capacity, "iclForwardCoreLong");
        }

        // calculate data
        for (SolverData solverData : solvers) {
            System.out.println("*******************");
            System.out.println(solverData.name);
            System.out.println("Mean time : "+solverData.time/solverData.nbTime);
            System.out.println("Min time : "+solverData.minTime);
            System.out.println("Max time : "+solverData.maxTime);
        }
    }

    public static long solveKnapsack(AbstractKnapsackSolver solver) {
        Instant startSolve = Instant.now();
        int solutionValue = solver.solve();

        Instant startSolution = Instant.now();
        boolean[] solution = solver.getSolution();

        Instant end = Instant.now();
        // System.out.println("Time solve : "+Duration.between(startSolve, startSolution));
        // System.out.println("Time find solution : "+Duration.between(startSolution, end));
        System.out.println("TIME TOTAL : "+Duration.between(startSolve, end));

        System.out.println("Solution value : "+solutionValue);
        // System.out.println("Solution : "+Arrays.toString(solution));

       //  try {
       //      String memoryUsage = new String();
       //      List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
       //      for (MemoryPoolMXBean pool : pools) {
       //          MemoryUsage peak = pool.getPeakUsage();
       //          if (peak.getUsed() != 0) {
       //              memoryUsage += String.format("Peak %s memory used: %d%n", pool.getName(),peak.getUsed());
       //          }
       //          // memoryUsage += String.format("Peak %s memory reserved: %d%n", pool.getName(), peak.getCommitted());
       //      }
       //      System.out.println(memoryUsage);
       // } catch (Throwable t) {
       //      System.err.println("Exception in agent: " + t);
       // }

       // System.out.println(solver.shortName+" & "+Duration.between(startSolve, end)+" & Faible \\\\");

       return ChronoUnit.MILLIS.between(startSolve, end);
    }
}