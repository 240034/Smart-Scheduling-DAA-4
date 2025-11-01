package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private final Random rnd;

    public DatasetGenerator(long seed) {
        this.rnd = new Random(seed);
    }

    public DatasetGenerator() {
        this(System.currentTimeMillis());
    }

    public static class NodeSpec {
        public String id;
        public int duration;
        public NodeSpec(String id, int duration) { this.id = id; this.duration = duration; }
    }

    public static class EdgeSpec {
        public String from;
        public String to;
        public int travel;
        public EdgeSpec(String from, String to, int travel) { this.from = from; this.to = to; this.travel = travel; }
    }

    public static class GraphSpec {
        public List<NodeSpec> nodes;
        public List<EdgeSpec> edges;
        public GraphSpec(List<NodeSpec> nodes, List<EdgeSpec> edges) { this.nodes = nodes; this.edges = edges; }
    }

    public GraphSpec generate(int n, double density, boolean allowCycles, boolean ensureSomeSCCs) {
        List<NodeSpec> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new NodeSpec("N" + i, 1 + rnd.nextInt(10))); // duration 1..10
        }

        List<EdgeSpec> edges = new ArrayList<>();
        if (!allowCycles) {
            List<Integer> order = new ArrayList<>();
            for (int i = 0; i < n; i++) order.add(i);
            Collections.shuffle(order, rnd);
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (rnd.nextDouble() < density) {
                        String from = "N" + order.get(i);
                        String to = "N" + order.get(j);
                        edges.add(new EdgeSpec(from, to, rnd.nextInt(5)));
                    }
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) continue;
                    if (rnd.nextDouble() < density) {
                        edges.add(new EdgeSpec("N" + i, "N" + j, rnd.nextInt(5)));
                    }
                }
            }
            if (ensureSomeSCCs && n >= 4) {
                int groups = Math.max(1, Math.min(3, n / 6)); // 1..3 groups
                for (int g = 0; g < groups; g++) {
                    int a = rnd.nextInt(n);
                    int b = rnd.nextInt(n);
                    int c = rnd.nextInt(n);
                    if (a==b || b==c || a==c) continue;

                    edges.add(new EdgeSpec("N"+a, "N"+b, rnd.nextInt(3)));
                    edges.add(new EdgeSpec("N"+b, "N"+c, rnd.nextInt(3)));
                    edges.add(new EdgeSpec("N"+c, "N"+a, rnd.nextInt(3)));
                }
            }
        }

        return new GraphSpec(nodes, edges);
    }

    public void writeToFile(GraphSpec spec, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(spec, fw);
        }
    }

    public static void main(String[] args) throws Exception {
        DatasetGenerator gen = new DatasetGenerator(12345L);
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();

        // Small (6..8)
        var s1 = gen.generate(6, 0.12, false, false); // sparse DAG
        gen.writeToFile(s1, new File(dataDir, "src/data/small-1.json"));
        var s2 = gen.generate(7, 0.25, true, true); // mixed, some SCCs
        gen.writeToFile(s2, new File(dataDir, "src/data/small-2.json"));
        var s3 = gen.generate(8, 0.45, true, false); // denser, cycles likely
        gen.writeToFile(s3, new File(dataDir, "src/data/small-3.json"));

        // Medium (11..18)
        var m1 = gen.generate(11, 0.08, false, false); // sparse DAG
        gen.writeToFile(m1, new File(dataDir, "src/data/medium-1.json"));
        var m2 = gen.generate(15, 0.18, true, true); // mixed
        gen.writeToFile(m2, new File(dataDir, "src/data/medium-2.json"));
        var m3 = gen.generate(18, 0.35, true, true); // denser with SCCs
        gen.writeToFile(m3, new File(dataDir, "src/data/medium-3.json"));

        // Large (22..48)
        var l1 = gen.generate(22, 0.06, false, false); // sparse DAG
        gen.writeToFile(l1, new File(dataDir, "src/data/large-1.json"));
        var l2 = gen.generate(35, 0.15, true, true); // medium density mixed
        gen.writeToFile(l2, new File(dataDir, "src/data/large-2.json"));
        var l3 = gen.generate(48, 0.30, true, true); // dense, performance test
        gen.writeToFile(l3, new File(dataDir, "src/data/large-3.json"));

        System.out.println("Generated 9 datasets in ./data/");
    }
}
