package graph;

public class GraphTest {
    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge("A", "B");
        g.addEdge("B", "C");
        System.out.println(g.getNeighbors("A"));
        System.out.println(g.getNeighbors("B"));

    }

}
