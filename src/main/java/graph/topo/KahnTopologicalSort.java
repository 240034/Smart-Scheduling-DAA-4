package graph.topo;

import graph.Graph;
import graph.Metrics;

import java.util.*;

public class KahnTopologicalSort {


    private final Graph graph;
    private final Metrics metrics;

    public KahnTopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<String> sort() {

        Map<String, Integer> inDegree = new HashMap<>();
        for (String v : graph.getNodes()) {
            inDegree.put(v, 0);
        }

        for (String u : graph.getNodes()) {
            for (String v : graph.getNeighbors(u)) {
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        Queue <String> queue = new ArrayDeque<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        if (metrics != null) metrics.startTimer();

        List<String> topoOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            String u = queue.poll();
            topoOrder.add(u);
            if (metrics != null) metrics.kahnPops++;

            for (String v : graph.getNeighbors(u)) {
                if (metrics != null) metrics.kahnPushes++;

                inDegree.put(v, inDegree.get(v) - 1);
                if (inDegree.get(v) == 0) {
                    queue.add(v);
                }
            }
        }

        if (metrics != null) metrics.stopTimer();

        if (topoOrder.size() != graph.getNodes().size()) {
            throw new IllegalStateException("Graph is not a DAG! Topological sort not possible.");
        }
        return topoOrder;
    }
}