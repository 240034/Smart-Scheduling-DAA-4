# Smart Scheduling (DAA Assignment 4)

## Overview
This project consolidates two key course topics:  
1. **Strongly Connected Components (SCC) & Topological Ordering**  
2. **Shortest and Longest Paths in Directed Acyclic Graphs (DAGs)**  

The scenario simulates **smart city / smart campus scheduling**:  
Tasks such as street cleaning, repairs, or analytics subtasks may have dependencies that are cyclic (SCCs) or acyclic (DAGs). The algorithms detect cycles, compress SCCs, and compute optimal task execution orders.

---

## Features Implemented

### 1. SCC Detection
- **Algorithm:** Tarjan's algorithm for SCC.  
- **Input:** Directed dependency graphs in JSON format (`/data/*.json`).  
- **Output:** List of SCCs (each as a list of vertices) and their sizes.  
- **Condensation Graph:** Builds a DAG of SCC components.

### 2. Topological Sorting
- **Algorithm:** Kahn's algorithm (BFS variant).  
- **Input:** Condensation DAG from SCC.  
- **Output:** Topological order of components and derived order of original tasks.

### 3. Shortest and Longest Paths in DAG
- **Weight Model:** Edge weights are used.  
- **Algorithms:**
  - Single-source shortest paths (DP over topological order).  
  - Longest path (critical path) using max-DP.  
- **Output:** 
  - Shortest distances from a given source.
  - Longest path and its length (critical path).
  - One reconstructed optimal path.

---

## Project Structure
├─ pom.xml

├─ README.md

├─ src/main/java/

│ ├─ graph/

│ │ ├─ Graph.java

│ │ ├─ Node.java

│ │ ├─ Edge.java

│ │ ├─ Metrics.java

│ │ ├─ scc/

│ │ │ ├─ TarjanSCC.java

│ │ │ └─ CondensationBuilder.java

│ │ ├─ topo/

│ │ │ └─ KahnTopologicalSort.java

│ │ └─ dagsp/

│ │ └─ DAGShortestLongest.java

│ └─ util/

│ └─ JSONGraphLoader.java

├─ src/test/java/

│ ├─ graph/scc/TarjanSCCTest.java

│ ├─ graph/topo/KahnTopologicalSortTest.java

│ └─ graph/dagsp/DAGSPTest.java

└─ data/

├─ small-1.json

├─ small-2.json

└─ ...


---

## Dependencies
- Java 23
- Maven
- [Gson 2.10.1](https://github.com/google/gson) (for JSON parsing)
- JUnit 4 & 5 (for testing)

Add Gson to `pom.xml`:
```xml
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.10.1</version>
</dependency>
Running the Demo
```
## Compile and run the main demo:
```
mvn clean compile
mvn exec:java -Dexec.mainClass="demo.SmartSchedulingDemo"
```

The demo will:

Load a JSON graph from /data/

Compute SCCs and build condensation DAG

Topologically sort components

Compute shortest and longest paths in the DAG

Display metrics (DFS visits, edges, Kahn pushes/pops, relaxations, execution time)


## Example JSON Format
```
{
  "directed": true,
  "n": 6,
  "edges": [
    {"u": 0, "v": 1, "w": 2},
    {"u": 1, "v": 2, "w": 3},
    {"u": 2, "v": 3, "w": 1},
    {"u": 3, "v": 1, "w": 4},
    {"u": 4, "v": 5, "w": 2}
  ],
  "source": 0,
  "weight_model": "edge"
}
```

## Metrics & Performance

SCC: DFS visits / edges

Topological Sort (Kahn): pushes / pops

DAG Shortest/Longest Path: relaxations

Timing: measured in ms via System.nanoTime()

Example metric table (from small, medium, large datasets):
| Dataset  | Nodes | Edges | SCC | DFS visits | Kahn pushes/pops | Relaxations | Time (ms) |
| -------- | ----- | ----- | --- | ---------- | ---------------- | ----------- | --------- |
| small-1  | 6     | 5     | 4   | 6          | 0/0              | 0           | 0.13      |
| medium-1 | 12    | 14    | 5   | 12         | 6/6              | 14          | 0.25      |
| large-1  | 40    | 55    | 8   | 40         | 25/25            | 55          | 0.75      |

## Testing

JUnit tests under src/test/java

Covers:

Small deterministic graphs

Edge cases (single node, disconnected, fully cyclic graphs)



