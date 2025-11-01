package demo;

import graph.Graph;
import graph.Metrics;
import graph.scc.TarjanSCC;
import graph.scc.CondensationBuilder;
import graph.topo.KahnTopologicalSort;
import graph.dagsp.DAGShortestLongest;
import util.JSONGraphLoader;

import java.util.List;
import java.util.Map;

public class SmartSchedulingDemo {

    public static void main(String[] args) throws Exception {

        String path = "src/data/small-1.json";

        Graph g = JSONGraphLoader.loadGraph(path);
        String source = JSONGraphLoader.getSource(path);
        Map<String, Double> weights = JSONGraphLoader.getEdgeWeights(path);

        System.out.println("Graph loaded from JSON:");
        System.out.println("Nodes: " + g.getNodes());
        System.out.println("Edges: " + g.getAllEdges());
        System.out.println("Source: " + source);
        System.out.println("Weight model: edge\n");

        Metrics metrics = new Metrics();
        TarjanSCC tarjan = new TarjanSCC(g, metrics);
        List<List<String>> components = tarjan.run();

        System.out.println("SCCs found: " + components.size());
        for (int i = 0; i < components.size(); i++) {
            System.out.println("C" + i + ": " + components.get(i));
        }

        System.out.println("\nMetrics after SCC:");
        System.out.println(metrics);

        Graph condensation = CondensationBuilder.buildCondensation(g, components);

        System.out.println("\nCondensation DAG:");
        for (String node : condensation.getNodes()) {
            System.out.println(node + " -> " + condensation.getNeighbors(node));
        }

        KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation, metrics);
        List<String> topoOrder = topoSort.sort();
        System.out.println("\nTopological order of components: " + topoOrder);

        DAGShortestLongest dagSP = new DAGShortestLongest(condensation, metrics);
        Map<String, Double> shortest = dagSP.shortestPaths(source, topoOrder, weights);
        Map<String, Double> longest = dagSP.longestPaths(source, topoOrder, weights);

        System.out.println("\nShortest distances from " + source + ":");
        shortest.forEach((k, v) -> System.out.printf("%s = %.1f\n", k, v));

        System.out.println("\nLongest distances from " + source + ":");
        longest.forEach((k, v) -> System.out.printf("%s = %.1f\n", k, v));

        System.out.println("\nFinal Metrics:");
        System.out.println(metrics);
    }
}
