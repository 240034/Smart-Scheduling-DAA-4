package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import graph.Edge;
import graph.Graph;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class JSONGraphLoader {

    private static final Gson gson = new Gson();

    public static Graph loadGraph(String path) throws Exception {
        FileReader reader = new FileReader(path);
        JsonObject obj = gson.fromJson(reader, JsonObject.class);

        Graph g = new Graph();

        JsonArray edges = obj.getAsJsonArray("edges");
        for (var e : edges) {
            JsonObject edge = e.getAsJsonObject();
            String u = String.valueOf(edge.get("u").getAsInt());
            String v = String.valueOf(edge.get("v").getAsInt());
            g.addEdge(u, v);
        }

        int n = obj.get("n").getAsInt();
        for (int i = 0; i < n; i++) {
            g.addNode(String.valueOf(i));
        }

        return g;
    }

    public static String getSource(String path) throws Exception {
        FileReader reader = new FileReader(path);
        JsonObject obj = gson.fromJson(reader, JsonObject.class);
        return String.valueOf(obj.get("source").getAsInt());
    }

    public static Map<String, Double> getEdgeWeights(String path) throws Exception {
        FileReader reader = new FileReader(path);
        JsonObject obj = gson.fromJson(reader, JsonObject.class);

        Map<String, Double> weights = new HashMap<>();
        JsonArray edges = obj.getAsJsonArray("edges");
        for (var e : edges) {
            JsonObject edge = e.getAsJsonObject();
            String key = edge.get("u").getAsInt() + "->" + edge.get("v").getAsInt();
            double w = edge.get("w").getAsDouble();
            weights.put(key, w);
        }

        return weights;
    }
}
