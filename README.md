# Graph Algorithms – Lab 3

A Java project implementing and benchmarking classic graph algorithms on four graph distributions.

## Algorithms

| Algorithm | Type | Time Complexity |
|---|---|---|
| **Prim's** | Minimum Spanning Tree | O((V + E) log V) |
| **Kruskal's** | Minimum Spanning Tree | O(E log E) |
| **Dijkstra's** | Single-Source Shortest Path | O((V + E) log V) |
| **DAG Shortest Path** | SSSP (topological sort) | O(V + E) |

## Graph Distributions

| Generator | Edges | Type |
|---|---|---|
| **Sparse** | 5 × V | Undirected, connected (spanning tree + random) |
| **Dense** | ~25% of all pairs + spanning chain | Undirected, connected |
| **Complete** | V(V−1)/2 | Undirected, fully connected |
| **DAG** | 5 × V | Directed acyclic (forward edges only) |

## Project Structure

```
graphAlgorithms/
├── src/
│   ├── core/
│   │   ├── Edge.java                  # Edge record (u, v, weight)
│   │   └── Graph.java                 # Graph with adj list + edge list
│   ├── algorithms/
│   │   ├── prims.java                 # Prim's MST (priority queue)
│   │   ├── kruskal.java               # Kruskal's MST (union-find)
│   │   ├── Dijkstra.java              # Dijkstra's SSSP (priority queue)
│   │   └── DAG_Sh_Path.java           # DAG shortest path (topo sort + relax)
│   ├── generators/
│   │   ├── GraphGenerator.java        # Generator interface
│   │   ├── SparseGraphGenerator.java
│   │   ├── DenseGraphGenerator.java
│   │   ├── CompleteGraphGenerator.java
│   │   └── DAGGenerator.java
│   ├── test/
│   │   ├── Benchmark.java             # Full benchmark suite with CSV export
│   │   ├── BenchmarkResults.java      # Collects per-run data → CSV file
│   │   ├── core/
│   │   │   └── GraphTest.java         # addEdge / addDirectedEdge correctness
│   │   ├── algorithms/
│   │   │   ├── PrimMSTTest.java        # MST correctness on small known graphs
│   │   │   ├── KruskalMSTTest.java     # MST correctness + matches Prim weight
│   │   │   ├── DijkstraTest.java       # SSSP correctness (linear, diamond, etc.)
│   │   │   └── DAGShortestPathTest.java # DAG SSSP + cycle-detection exception
│   │   └── generators/
│   │       └── GraphGeneratorTest.java # Edge counts, connectivity, acyclicity
│   └── Main.java
└── README.md
```

## How to Run

### Benchmark

Run from your IDE by executing the `main` method in `test.Benchmark`.

The benchmark will:
1. Run Prim's vs Kruskal's on Sparse, Dense, and Complete graphs
2. Run Dijkstra's on all four distributions
3. Run Dijkstra's vs DAG Shortest Path on the DAG
4. Verify correctness (Dijkstra == DAG-SP on the same DAG)
5. Export all per-run results to `benchmark_results.csv`

### Unit Tests

Run each test class individually from your IDE:

| Test Class | What it covers |
|---|---|
| `test.core.GraphTest` | `addEdge` / `addDirectedEdge` correctness |
| `test.algorithms.PrimMSTTest` | Prim's MST on small known graphs |
| `test.algorithms.KruskalMSTTest` | Kruskal's MST + cross-check with Prim |
| `test.algorithms.DijkstraTest` | Dijkstra's SSSP (linear, shortcut, diamond, unreachable) |
| `test.algorithms.DAGShortestPathTest` | DAG SSSP + cycle-detection exception |
| `test.generators.GraphGeneratorTest` | Edge counts, connectivity, DAG acyclicity, determinism |

## CSV Output

The benchmark exports `benchmark_results.csv` with one row per individual run:

```
Run,Algorithm,Distribution,V,Time_ms,EdgesOrReachable,TotalWeightOrV
1,Kruskal,Sparse,5000,3.911,4999,592214
1,Prim,Sparse,5000,2.177,4999,592214
...
```

| Column | Description |
|---|---|
| `Run` | 1-based run number |
| `Algorithm` | Kruskal, Prim, Dijkstra, or DAG-SP |
| `Distribution` | Sparse, Dense, Complete, or DAG |
| `V` | Number of vertices |
| `Time_ms` | Execution time in milliseconds |
| `EdgesOrReachable` | MST edge count / SSSP reachable vertex count |
| `TotalWeightOrV` | Total MST weight / sum of shortest distances |

## Configuration

Constants in `Benchmark.java`:

| Constant | Default | Description |
|---|---|---|
| `RUNS` | 5 | Number of measured runs per algorithm |
| `WARMUP_RUNS` | 2 | JVM warm-up iterations (not measured) |
| `V_SIZE` | 5000 | Number of vertices |
| `BASE_SEED` | 42 | Random seed for reproducibility |
