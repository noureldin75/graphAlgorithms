package test;

import core.Edge;
import core.Graph;
import generators.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Benchmark {

    private static final int RUNS = 5;         // Required by assignment (§4.2)
    private static final int WARMUP_RUNS = 2;  // JVM warm-up for accurate measurement
    private static final int V_SIZE = 5000;    // Fixed size required by assignment (§3)
    private static final long BASE_SEED = 42L;
    private static final int DAG_SOURCE = 0;   // vertex 0 reaches all vertices in DAG

    private static final BenchmarkResults results = new BenchmarkResults();

    public static void main(String[] args) {

        System.out.println("=".repeat(100));
        System.out.println("                        GRAPH ALGORITHMS  -  BENCHMARK REPORT");
        System.out.println("=".repeat(100));
        System.out.printf("  Warm-up runs    : %d%n", WARMUP_RUNS);
        System.out.printf("  Measured runs   : %d%n", RUNS);
        System.out.printf("  Vertex size (V) : %d%n", V_SIZE);
        System.out.printf("  Random seed     : %d%n", BASE_SEED);
        System.out.println("=".repeat(100));
        System.out.println();

        // Fixed random source for SSSP (§4.2: "same input")
        Random srcRand = new Random(BASE_SEED);
        int source = srcRand.nextInt(V_SIZE);

        // ── MST Construction (Prim vs Kruskal) ──────────────────────
        System.out.println("─".repeat(105));
        System.out.println("  1. MST Construction  -  Prim's  vs  Kruskal's");
        System.out.println("─".repeat(105));

        GraphGenerator[] mstGenerators = {
                new SparseGraphGenerator(),
                new DenseGraphGenerator(),
                new CompleteGraphGenerator()
        };
        String[] mstNames = {"3.1 Sparse", "3.2 Dense", "3.3 Complete"};

        printTableHeader("Graph", "Prim median(ms)", "Prim mean(ms)", "Prim stddev",
                "Kruskal median(ms)", "Kruskal mean(ms)", "Kruskal stddev");

        for (int gi = 0; gi < mstGenerators.length; gi++) {
            Graph graph = mstGenerators[gi].generateGraph(V_SIZE, BASE_SEED);

            // Warm-up JVM
            for (int i = 0; i < WARMUP_RUNS; i++) {
                graph.primMST();
                graph.kruskalMST();
            }

            String distName = mstNames[gi].replaceFirst("^\\S+\\s+", ""); // "Sparse", "Dense", ...

            // Isolated Loop for Prim (CPU Cache Locality)
            long[] primTimes = new long[RUNS];
            for (int r = 0; r < RUNS; r++) {
                long t0 = System.nanoTime();
                List<Edge> mst = graph.primMST();
                long t1 = System.nanoTime();
                primTimes[r] = t1 - t0;
                results.addResult(r + 1, "Prim", distName, V_SIZE, primTimes[r],
                        mst.size(), totalWeight(mst));
            }

            // Isolated Loop for Kruskal (CPU Cache Locality)
            long[] kruskalTimes = new long[RUNS];
            for (int r = 0; r < RUNS; r++) {
                long t0 = System.nanoTime();
                List<Edge> mst = graph.kruskalMST();
                long t1 = System.nanoTime();
                kruskalTimes[r] = t1 - t0;
                results.addResult(r + 1, "Kruskal", distName, V_SIZE, kruskalTimes[r],
                        mst.size(), totalWeight(mst));
            }

            printTableRow(mstNames[gi], primTimes, kruskalTimes);
        }

        // ── SSSP (General) Dijkstra ─────────────────────────────────
        System.out.println();
        System.out.println("─".repeat(70));
        System.out.println("  2. SSSP (General)  -  Dijkstra's Algorithm");
        System.out.println("─".repeat(70));

        GraphGenerator[] allGenerators = {
                new SparseGraphGenerator(),
                new DenseGraphGenerator(),
                new CompleteGraphGenerator(),
                new DAGGenerator()
        };
        String[] allNames = {"3.1 Sparse", "3.2 Dense", "3.3 Complete", "3.4 DAG"};

        printSingleHeader("Graph", "Dijkstra median(ms)", "Dijkstra mean(ms)", "Dijkstra stddev");

        for (int gi = 0; gi < allGenerators.length; gi++) {
            Graph graph = allGenerators[gi].generateGraph(V_SIZE, BASE_SEED);
            // For DAG, use DAG_SOURCE (root that reaches all vertices)
            int ssspSource = (allGenerators[gi] instanceof DAGGenerator) ? DAG_SOURCE : source;
            String distName = allNames[gi].replaceFirst("^\\S+\\s+", "");

            // Warm-up JVM
            for (int i = 0; i < WARMUP_RUNS; i++) {
                graph.dijkstra(ssspSource);
            }

            long[] dijkstraTimes = new long[RUNS];
            for (int r = 0; r < RUNS; r++) {
                long t0 = System.nanoTime();
                int[] dist = graph.dijkstra(ssspSource);
                long t1 = System.nanoTime();
                dijkstraTimes[r] = t1 - t0;
                results.addResult(r + 1, "Dijkstra", distName, V_SIZE, dijkstraTimes[r],
                        reachableCount(dist), distSum(dist));
            }

            printSingleRow(allNames[gi], dijkstraTimes);
        }

        // ── SSSP (DAG) Dijkstra vs DAG Shortest Path ────────────────
        System.out.println();
        System.out.println("─".repeat(95));
        System.out.println("  3. SSSP (DAG)  -  Dijkstra  vs  DAG Shortest Path");
        System.out.println("─".repeat(95));

        printDagHeader();

        Graph dagGraph = new DAGGenerator().generateGraph(V_SIZE, BASE_SEED);

        // Warm-up JVM (both algorithms on same graph instance)
        for (int i = 0; i < WARMUP_RUNS; i++) {
            dagGraph.dijkstra(DAG_SOURCE);
            dagGraph.dagShortestPath(DAG_SOURCE);
        }

        // Isolated Loop for Dijkstra
        long[] dijkTimes = new long[RUNS];
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            int[] dist = dagGraph.dijkstra(DAG_SOURCE);
            long t1 = System.nanoTime();
            dijkTimes[r] = t1 - t0;
            results.addResult(r + 1, "Dijkstra", "DAG", V_SIZE, dijkTimes[r],
                    reachableCount(dist), distSum(dist));
        }

        // Isolated Loop for DAG Shortest Path
        long[] dagTimes = new long[RUNS];
        for (int r = 0; r < RUNS; r++) {
            long t0 = System.nanoTime();
            int[] dist = dagGraph.dagShortestPath(DAG_SOURCE);
            long t1 = System.nanoTime();
            dagTimes[r] = t1 - t0;
            results.addResult(r + 1, "DAG-SP", "DAG", V_SIZE, dagTimes[r],
                    reachableCount(dist), distSum(dist));
        }

        printDagRow("3.4 DAG", dijkTimes, dagTimes);

        // Correctness verification: both algorithms must produce the same result
        int[] dijkResult = dagGraph.dijkstra(DAG_SOURCE);
        int[] dagResult  = dagGraph.dagShortestPath(DAG_SOURCE);
        boolean match = Arrays.equals(dijkResult, dagResult);
        System.out.printf("%n  Correctness check: Dijkstra == DAG Shortest Path -> %s%n",
                match ? "PASS" : "FAIL");

        // ── CSV Export ────────────────────────────────────────────
        results.exportToCSV("benchmark_results.csv");

        System.out.println();
        System.out.println("=".repeat(100));
        System.out.println("  Benchmark complete.  (" + results.size() + " rows exported)");
        System.out.println("=".repeat(100));
    }

    // ── Result helpers ─────────────────────────────────────────

    private static long totalWeight(List<Edge> mst) {
        long sum = 0;
        for (Edge e : mst) sum += e.weight();
        return sum;
    }

    private static long reachableCount(int[] dist) {
        long count = 0;
        for (int d : dist) if (d != Integer.MAX_VALUE) count++;
        return count;
    }

    private static long distSum(int[] dist) {
        long sum = 0;
        for (int d : dist) if (d != Integer.MAX_VALUE) sum += d;
        return sum;
    }

    // ── Statistics helpers ──────────────────────────────────────

    private static double medianMs(long[] nanos) {
        long[] sorted = nanos.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        if (n % 2 == 0)
            return (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0 / 1_000_000.0;
        else
            return sorted[n / 2] / 1_000_000.0;
    }

    private static double meanMs(long[] nanos) {
        double sum = 0;
        for (long t : nanos) sum += t;
        return (sum / nanos.length) / 1_000_000.0;
    }

    private static double stddevMs(long[] nanos) {
        double mean = meanMs(nanos) * 1_000_000.0;
        double sumSq = 0;
        for (long t : nanos) sumSq += (t - mean) * (t - mean);
        return Math.sqrt(sumSq / nanos.length) / 1_000_000.0;
    }

    // ── Printing helpers ────────────────────────────────────────

    private static void printTableHeader(String... cols) {
        System.out.printf("  %-12s | %-15s %-13s %-11s | %-18s %-16s %-14s%n",
                cols[0], cols[1], cols[2], cols[3], cols[4], cols[5], cols[6]);
        System.out.println("  " + "-".repeat(101));
    }

    private static void printTableRow(String graphName, long[] t1, long[] t2) {
        System.out.printf("  %-12s | %15.3f   %13.3f   %11.3f   | %18.3f   %16.3f   %14.3f%n",
                graphName,
                medianMs(t1), meanMs(t1), stddevMs(t1),
                medianMs(t2), meanMs(t2), stddevMs(t2));
    }

    private static void printSingleHeader(String... cols) {
        System.out.printf("  %-12s | %-19s %-17s %-15s%n",
                cols[0], cols[1], cols[2], cols[3]);
        System.out.println("  " + "-".repeat(68));
    }

    private static void printSingleRow(String graphName, long[] t) {
        System.out.printf("  %-12s | %19.3f   %17.3f   %15.3f%n",
                graphName, medianMs(t), meanMs(t), stddevMs(t));
    }

    private static void printDagHeader() {
        System.out.printf("  %-12s | %-12s %-10s %-8s | %-12s %-10s %-8s | %-10s%n",
                "Graph",
                "Dijk median", "Dijk mean", "Dijk std",
                "DAG median", "DAG mean", "DAG std",
                "Speed-up");
        System.out.println("  " + "-".repeat(93));
    }

    private static void printDagRow(String graphName, long[] dijkstra, long[] dag) {
        double dijkMedian = medianMs(dijkstra);
        double dagMedian = medianMs(dag);
        double speedup = (dagMedian > 0) ? dijkMedian / dagMedian : Double.NaN;
        double speedupPct = (speedup - 1.0) * 100.0;

        System.out.printf("  %-12s | %12.3f   %10.3f   %8.3f   | %12.3f   %10.3f   %8.3f   | %7.2fx (%+.1f%%)%n",
                graphName,
                dijkMedian, meanMs(dijkstra), stddevMs(dijkstra),
                dagMedian, meanMs(dag), stddevMs(dag),
                speedup, speedupPct);
    }
}