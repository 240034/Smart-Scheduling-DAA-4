package graph.scc;

import graph.Graph;
import java.util.*;

public class CondensationBuilder {
    public static Graph buildCondensation(Graph originalGraph, List<List<String>> sccList) {
        Graph dag = new Graph();

        Map <String, String> vertexToComp = new HashMap<>();
        for (int i=0; i<sccList.size(); i++) {
            String compId = "C" + i;
            for (String v : sccList.get(i)) {
                vertexToComp.put(v, compId);
            }
            dag.addNode(compId);
        }

        for (String u : originalGraph.getNodes()) {
            for (String v : originalGraph.getNeighbors(u)) {
                String compU = vertexToComp.get(u);
                String compV = vertexToComp.get(v);
                if (!compU.equals(compV)) {
                    dag.addEdge(compU, compV);
                }
            }
        }
        return dag;
    }
}
