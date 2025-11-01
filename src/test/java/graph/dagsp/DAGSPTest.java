package graph.dagsp;

import graph.Graph;
import graph.Metrics;
import graph.topo.KahnTopologicalSort;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DAGSPTest {

    @Test
    public void testShortestAndLongestPaths() {

        Graph g = new Graph();
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "D");
        g.addEdge("C", "D");
        g.addEdge("C", "E");
        g.addEdge("D", "E");

        Map<String, Double> weights = new HashMap<>();
        weights.put("A->B", 2.0);
        weights.put("A->C", 4.0);
        weights.put("B->D", 3.0);
        weights.put("C->D", 1.0);
        weights.put("C->E", 5.0);
        weights.put("D->E", 2.0);

        Metrics metrics = new Metrics();
        KahnTopologicalSort sorter = new KahnTopologicalSort(g, metrics);
        List<String> topoOrder = sorter.sort();


        System.out.println("Topological order: " + topoOrder);

        metrics = new Metrics();
        DAGShortestLongest dagSP = new DAGShortestLongest(g, metrics);

        Map<String, Double> shortest = dagSP.shortestPaths("A", topoOrder, weights);
        Map<String, Double> longest = dagSP.longestPaths("A", topoOrder, weights);

        System.out.println("\nShortest distances from A:");
        for (var e : shortest.entrySet()) {
            System.out.printf("%s = %.1f\n", e.getKey(), e.getValue());
        }

        System.out.println("\nLongest distances from A:");
        for (var e : longest.entrySet()) {
            System.out.printf("%s = %.1f\n", e.getKey(), e.getValue());
        }

        assertEquals(0.0, shortest.get("A"));
        assertTrue(shortest.get("E") > 0);
        assertTrue(longest.get("E") >= shortest.get("E"));

        System.out.println("\nMetrics: " + metrics);
    }
}
