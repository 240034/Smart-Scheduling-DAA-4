package graph.scc;

import graph.Graph;
import graph.Metrics;
import java.util.*;

public class TarjanSCC {
    private final Graph graph;

    private final Map<String, Integer> index = new HashMap<>();
    private final Map<String, Integer> low = new HashMap<>();

    private final Deque<String> stack = new ArrayDeque<>();
    private final Set<String> onStack = new HashSet<>();

    private int currentIndex = 0;
    private final List<List<String>> components = new ArrayList<>();

    private final Metrics metrics;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = Objects.requireNonNull(graph, "graph must not be null");
        this.metrics = metrics;
    }

    public List<List<String>> run() {
        if (metrics != null) metrics.startTimer();

        for (String v : graph.getNodes()) {
            if (!index.containsKey(v)) {
                strongConnect(v);
            }
        }

        if (metrics != null) metrics.stopTimer();
        return components;
    }

    private void strongConnect(String v) {
        index.put(v, currentIndex);
        low.put(v, currentIndex);
        currentIndex++;

        stack.push(v);
        onStack.add(v);

        if (metrics != null) metrics.dfsVisits++;

        for (String w : graph.getNeighbors(v)) {
            if (metrics != null) metrics.dfsEdges++;

            if (!index.containsKey(w)) {
                strongConnect(w);
                low.put(v, Math.min(low.get(v), low.get(w)));
            } else if (onStack.contains(w)) {
                low.put(v, Math.min(low.get(v), index.get(w)));
            }

        }

        if (low.get(v).equals(index.get(v))) {
            List<String> comp = new ArrayList<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                comp.add(w);
            } while (!w.equals(v));
            components.add(comp);
        }
    }

}
