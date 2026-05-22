package test;

import core.Edge;
import core.Graph;
import generators.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Benchmark {

    private static final int    RUNS        = 5;
    private static final int    WARMUP_RUNS = 10;
    private static final int    V_SIZE      = 5_000;
    private static final long   BASE_SEED   = 42L;
    private static final int    DAG_SOURCE  = 0;

    public static void main(String[] args) {

        BenchmarkResults results = new BenchmarkResults();

        header("GRAPH ALGORITHMS  –  BENCHMARK REPORT");
        System.out.printf("  Runs: %d  |  Warm-up: %d  |  V = %,d  |  Seed = %d%n",
                RUNS, WARMUP_RUNS, V_SIZE, BASE_SEED);
        divider('=');

        // fixed source, same across all SSSP runs
        int source = new Random(BASE_SEED).nextInt(V_SIZE);

        // ── 1. MST: Prim vs Kruskal
        section("1.  MST Construction  –  Prim's vs Kruskal's  (Sparse / Dense / Complete)");

        GraphGenerator[] mstGens  = { new SparseGraphGenerator(),
                new DenseGraphGenerator(),
                new CompleteGraphGenerator() };
        String[]         mstTags  = { "Sparse", "Dense", "Complete" };

        System.out.printf("  %-10s | %10s %10s %10s | %10s %10s %10s | %s%n",
                "Graph", "Prim med", "Prim mean", "Prim σ",
                "Krus med", "Krus mean", "Krus σ", "Speed-up");
        thinDivider(95);

        for (int i = 0; i < mstGens.length; i++) {
            // competing algorithms run on the EXACT same graph instance
            Graph g = mstGens[i].generateGraph(V_SIZE, BASE_SEED);

            warmup(g, false);   // false = undirected (MST warm-up)

            long[] primTimes    = new long[RUNS];
            long[] kruskalTimes = new long[RUNS];

            // Compute MST once to get edge count and total weight for CSV
            List<Edge> mstResult = g.primMST();
            long mstEdges = mstResult.size();
            long mstWeight = 0;
            for (Edge e : mstResult) mstWeight += e.weight();

            // Isolated loops keep CPU branch-predictor & cache state fair
            for (int r = 0; r < RUNS; r++) {
                long t = System.nanoTime(); g.primMST();    primTimes[r]    = System.nanoTime() - t;
                results.addResult(r + 1, "Prim", mstTags[i], V_SIZE, primTimes[r], mstEdges, mstWeight);
            }
            for (int r = 0; r < RUNS; r++) {
                long t = System.nanoTime(); g.kruskalMST(); kruskalTimes[r] = System.nanoTime() - t;
                results.addResult(r + 1, "Kruskal", mstTags[i], V_SIZE, kruskalTimes[r], mstEdges, mstWeight);
            }

            double primMed = median(primTimes);
            double krusMed = median(kruskalTimes);
            double speedup = (primMed > 0) ? krusMed / primMed : Double.NaN;

            System.out.printf("  %-10s | %10.3f %10.3f %10.3f | %10.3f %10.3f %10.3f | %.2fx%n",
                    mstTags[i],
                    primMed,  mean(primTimes),  stddev(primTimes),
                    krusMed, mean(kruskalTimes), stddev(kruskalTimes),
                    speedup);
        }
        System.out.println("  (all times in ms)");

        // ── 2. SSSP Dijkstra on all four distributions (§4.1.2) ─────────────
        section("2.  SSSP (General)  –  Dijkstra's Algorithm  (all distributions)");

        GraphGenerator[] allGens = { new SparseGraphGenerator(),
                new DenseGraphGenerator(),
                new CompleteGraphGenerator(),
                new DAGGenerator() };
        String[]         allTags = { "Sparse", "Dense", "Complete", "DAG" };

        System.out.printf("  %-10s | %12s %12s %12s%n",
                "Graph", "Median (ms)", "Mean (ms)", "Std Dev (ms)");
        thinDivider(54);

        for (int i = 0; i < allGens.length; i++) {
            Graph g      = allGens[i].generateGraph(V_SIZE, BASE_SEED);
            int   src    = (allGens[i] instanceof DAGGenerator) ? DAG_SOURCE : source;
            long[] times = new long[RUNS];

            for (int w = 0; w < WARMUP_RUNS; w++) g.dijkstra(src);

            // Run Dijkstra once to get reachable count and distance sum for CSV
            int[] dists = g.dijkstra(src);
            long reachable = 0, distSum = 0;
            for (int d : dists) {
                if (d < Integer.MAX_VALUE) { reachable++; distSum += d; }
            }

            for (int r = 0; r < RUNS; r++) {
                long t = System.nanoTime(); g.dijkstra(src); times[r] = System.nanoTime() - t;
                results.addResult(r + 1, "Dijkstra", allTags[i], V_SIZE, times[r], reachable, distSum);
            }

            System.out.printf("  %-10s | %12.3f %12.3f %12.3f%n",
                    allTags[i], median(times), mean(times), stddev(times));
        }
        System.out.println("  (all times in ms)");

        // ── 3. SSSP DAG: Dijkstra vs DAG Shortest Path (§4.1.3) ─────────────
        section("3.  SSSP (DAG)  –  Dijkstra  vs  DAG Shortest Path");

        // exact same graph instance for both algorithms
        Graph dagGraph = new DAGGenerator().generateGraph(V_SIZE, BASE_SEED);

        for (int w = 0; w < WARMUP_RUNS; w++) {
            dagGraph.dijkstra(DAG_SOURCE);
            dagGraph.dagShortestPath(DAG_SOURCE);
        }

        // Compute DAG distances once for CSV metadata
        int[] dagDists = dagGraph.dijkstra(DAG_SOURCE);
        long dagReachable = 0, dagDistSum = 0;
        for (int d : dagDists) {
            if (d < Integer.MAX_VALUE) { dagReachable++; dagDistSum += d; }
        }

        long[] dijkTimes = new long[RUNS];
        long[] dagTimes  = new long[RUNS];

        for (int r = 0; r < RUNS; r++) {
            long t = System.nanoTime(); dagGraph.dijkstra(DAG_SOURCE);       dijkTimes[r] = System.nanoTime() - t;
            results.addResult(r + 1, "Dijkstra", "DAG", V_SIZE, dijkTimes[r], dagReachable, dagDistSum);
        }
        for (int r = 0; r < RUNS; r++) {
            long t = System.nanoTime(); dagGraph.dagShortestPath(DAG_SOURCE); dagTimes[r]  = System.nanoTime() - t;
            results.addResult(r + 1, "DAG-SP", "DAG", V_SIZE, dagTimes[r], dagReachable, dagDistSum);
        }

        double dijkMed  = median(dijkTimes);
        double dagMed   = median(dagTimes);
        double speedup  = (dagMed > 0) ? dijkMed / dagMed : Double.NaN;
        double pct      = (speedup - 1.0) * 100.0;

        System.out.printf("  %-12s | %10s %10s %10s | %10s %10s %10s | %s%n",
                "Graph", "Dijk med", "Dijk mean", "Dijk σ",
                "DAG med",  "DAG mean",  "DAG σ",  "Speed-up");
        thinDivider(95);
        System.out.printf("  %-12s | %10.3f %10.3f %10.3f | %10.3f %10.3f %10.3f | %.2fx (%+.1f%%)%n",
                "DAG",
                dijkMed,          mean(dijkTimes), stddev(dijkTimes),
                dagMed,           mean(dagTimes),  stddev(dagTimes),
                speedup, pct);
        System.out.println("  (all times in ms)");

        // correctness – both must produce identical distances
        boolean match = Arrays.equals(
                dagGraph.dijkstra(DAG_SOURCE),
                dagGraph.dagShortestPath(DAG_SOURCE));
        System.out.printf("%n  Correctness check  Dijkstra == DAG-SP : %s%n",
                match ? "PASS ✓" : "FAIL ✗");

        // ── CSV export ───────────────────────────────────────────────────────

        results.exportToCSV("benchmark_results.csv");

        divider('=');
        System.out.println("  Benchmark complete.");
        divider('=');
    }

    // ── Statistics ────────────────────────────────────────────────────────────

    private static double median(long[] ns) {
        long[] s = ns.clone(); Arrays.sort(s);
        int n = s.length;
        return (n % 2 == 0 ? (s[n/2-1] + s[n/2]) / 2.0 : s[n/2]) / 1_000_000.0;
    }

    private static double mean(long[] ns) {
        double sum = 0; for (long v : ns) sum += v;
        return sum / ns.length / 1_000_000.0;
    }

    private static double stddev(long[] ns) {
        double m = mean(ns) * 1_000_000.0, sq = 0;
        for (long v : ns) sq += (v - m) * (v - m);
        return Math.sqrt(sq / ns.length) / 1_000_000.0;
    }

    // ── Warm-up helpers ───────────────────────────────────────────────────────

    private static void warmup(Graph g, boolean directed) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            g.primMST();
            g.kruskalMST();
        }
    }

    // ── Formatting helpers ────────────────────────────────────────────────────

    private static void header(String title) {
        divider('=');
        System.out.println("  " + title);
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("  ── " + title);
        System.out.println();
    }

    private static void divider(char c) {
        System.out.println("  " + String.valueOf(c).repeat(90));
    }

    private static void thinDivider(int len) {
        System.out.println("  " + "-".repeat(len));
    }
}