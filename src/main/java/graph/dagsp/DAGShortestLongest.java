package graph.dagsp;

import graph.Graph;
import graph.Metrics;
import java.util.*;

public class DAGShortestLongest {

    private final Graph graph;
    private final Metrics metrics;

    public DAGShortestLongest(Graph graph, Metrics metrics) {
        this.graph = Objects.requireNonNull(graph);
        this.metrics = metrics;
    }

    public Map<String, Double> shortestPaths(String source, List<String> topoOrder, Map<String, Double> weights) {
        Map<String, Double> dist = new HashMap<>();

        for (String node : graph.getNodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);
        }
        dist.put(source, 0.0);

        if (metrics != null) metrics.startTimer();

        for (String u : topoOrder) {
            for (String v : graph.getNeighbors(u)) {
                double w = weights.getOrDefault(u + "->" + v, 1.0); // default weight = 1
                if (dist.get(u) + w < dist.get(v)) {
                    dist.put(v, dist.get(u) + w);
                    if (metrics != null) metrics.relaxations++;
                }
            }
        }

        if (metrics != null) metrics.stopTimer();
        return dist;
    }

    public Map<String, Double> longestPaths(String source, List<String> topoOrder, Map<String, Double> weights) {
        Map<String, Double> dist = new HashMap<>();
        for (String node : graph.getNodes()) {
            dist.put(node, Double.NEGATIVE_INFINITY);
        }
        dist.put(source, 0.0);

        if (metrics != null) metrics.startTimer();

        for (String u : topoOrder) {
            for (String v : graph.getNeighbors(u)) {
                double w = weights.getOrDefault(u + "->" + v, 1.0);
                if (dist.get(u) + w > dist.get(v)) {
                    dist.put(v, dist.get(u) + w);
                    if (metrics != null) metrics.relaxations++;
                }
            }
        }

        if (metrics != null) metrics.stopTimer();
        return dist;
    }
}
