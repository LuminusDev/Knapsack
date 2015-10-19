package knapsack.solver;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.*;

import grph.Grph;
import grph.path.Path;
import grph.properties.NumericalProperty;

import knapsack.KnapsackItem;

public class GraphKnapsackSolver extends AbstractKnapsackSolver {

	private Grph g;
	private NumericalProperty edgeWeight = new NumericalProperty("edge weights", 32, 0);
	private int vertexDest;
	private int maxProfit;
	private int[] path;

    public GraphKnapsackSolver(KnapsackItem[] instance, int capacity) {
        super(instance, capacity);
        this.solverName = "PCC in Graph";
        this.maxProfit = Arrays.stream(this.instance)
            .mapToInt(e -> e.profit)
            .max()
            .getAsInt();
        this.g = null;
    }
    
    protected int solveInstance() {
        if (g == null) {
            Instant start = Instant.now();
            createGraph();
            Instant end = Instant.now();
            System.out.println("Time creation graph : "+Duration.between(start, end));
        }

        this.solution = new boolean[this.instance.length];
        this.solutionValue = 0;

        Instant startD = Instant.now();
        Path p = g.getShortestPath(0, this.vertexDest, this.edgeWeight);
        Instant endD = Instant.now();
        System.out.println("Time Dijkstra : "+Duration.between(startD, endD));

        this.path = p.toVertexArray();
        for (int i = 0; i < this.path.length-1; i++) {
        	int arcValue = edgeWeight.getValueAsInt(g.getSomeEdgeConnecting(this.path[i], this.path[i+1]));
        	this.solutionValue -= (arcValue - maxProfit);

        	if (i < this.instance.length) {
        		this.solution[i] = arcValue != maxProfit;
        	}
        }

        return this.solutionValue;
    }

    private void createGraph() {
    	this.g = new grph.in_memory.InMemoryGrph();
    	g.addVertex();
    	int[] stage = getInitStage();
    	stage[0] = 0;
    	for (int k = 0; k < this.instance.length; k++) {
    		int[] newStage = getInitStage();
    		for(int weight = 0; weight <= this.capacity; weight++) {
    			if (stage[weight] == -1) {continue;}
    			// on prend k
    			int newWeight = weight + this.instance[k].weight;
    			if (newWeight <= this.capacity) {
	    			if (newStage[newWeight] == -1) {
	    				newStage[newWeight] = g.addVertex();
	    			}
	    			addEdge(stage[weight], newStage[newWeight], maxProfit-this.instance[k].profit);
    			}
    			// on ne prend pas k
    			if (newStage[weight] == -1) {
    				newStage[weight] = g.addVertex();
    			}
    			addEdge(stage[weight], newStage[weight], maxProfit);
    		}
    		stage = newStage;
    	}
    	// set edges to go to vertex destination
    	this.vertexDest = g.addVertex();
    	for(int w = 0; w <= this.capacity; w++) {
    		if (stage[w] == -1) {continue;}
    		addEdge(stage[w], this.vertexDest, maxProfit);
    	}
    }

    private int[] getInitStage() {
    	int[] stage = new int[this.capacity+1];
    	Arrays.fill(stage, -1);
    	return stage;
    }

    private void addEdge(int src, int dest, int newWeight) {
    	int e = g.addDirectedSimpleEdge(src, dest);
    	edgeWeight.setValue(e, newWeight);
    }

    @Override
    public boolean[] getSolution() {
        return this.solution;
    }
}