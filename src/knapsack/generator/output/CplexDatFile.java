package knapsack.generator.output;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import knapsack.KnapsackItem;

public class CplexDatFile {
    public CplexDatFile() {}

    static public void createFile(KnapsackItem[] instance, int capacity, String filename) {
        if (filename != "") {
            filename = "Knapsack/cplex/"+filename+".dat";
        } else {
            filename = "Knapsack/cplex/data"+capacity+".dat";
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, false), StandardCharsets.UTF_8))) {
            writer.write("Capacity = "+capacity+";");
            writer.write("Items = {");
            for(int i = 0; i < instance.length; i++) {
                String virgule = i != instance.length-1 ? "," : "";
                writer.write("<"+instance[i].weight+","+instance[i].profit+">"+virgule);
            }
            writer.write("};");
        } catch ( IOException ioe ) { ioe.printStackTrace(); }
    }
}