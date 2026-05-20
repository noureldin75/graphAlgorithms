package test.generators;

import core.Edge;
import core.Graph;
import generators.*;

import java.util.*;

/**
 * Tests for all graph generators: edge counts, connectivity, and DAG acyclicity.
 */
public class GraphGeneratorTest {

    private static int passed = 0, failed = 0;

    private static final int V = 500;        // smaller V for fast tests
    private static final long SEED = 12345L;

    public static void main(String[] args) {
        System.out.println("═══ GraphGeneratorTest ═══");

        testSparseEdgeCount();
        testSparseConnectivity();
        testDenseEdgeCountAboveSparse();
        testDenseConnectivity();
        testCompleteEdgeCount();
        testCompleteConnectivity();
        testDAGEdgeCount();
        testDAGIsAcyclic();
        testDeterministic();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ── Sparse: E = 5*V ────────────────────────────────────────

    static void testSparseEdgeCount() {
        Graph g = new SparseGraphGenerator().generateGraph(V, SEED);
        int expected = 5 * V;
        check("Sparse edge count = 5V (" + expected + ")", g.getEdgeList().size() == expected);
    }

    static void testSparseConnectivity() {
        Graph g = new SparseGraphGenerator().generateGraph(V, SEED);
        check("Sparse graph is connected", isConnected(g));
    }

    // ── Dense: more edges than sparse, connected ───────────────

    static void testDenseEdgeCountAboveSparse() {
        Graph gDense  = new DenseGraphGenerator().generateGraph(V, SEED);
        Graph gSparse = new SparseGraphGenerator().generateGraph(V, SEED);
        check("Dense has more edges than Sparse",
                gDense.getEdgeList().size() > gSparse.getEdgeList().size());
    }

    static void testDenseConnectivity() {
        Graph g = new DenseGraphGenerator().generateGraph(V, SEED);
        check("Dense graph is connected", isConnected(g));
    }

    // ── Complete: E = V*(V-1)/2 ────────────────────────────────

    static void testCompleteEdgeCount() {
        int smallV = 100; // keep manageable
        Graph g = new CompleteGraphGenerator().generateGraph(smallV, SEED);
        int expected = smallV * (smallV - 1) / 2;
        check("Complete edge count = V(V-1)/2 (" + expected + ")", g.getEdgeList().size() == expected);
    }

    static void testCompleteConnectivity() {
        Graph g = new CompleteGraphGenerator().generateGraph(100, SEED);
        check("Complete graph is connected", isConnected(g));
    }

    // ── DAG: E = 5*V, no cycles ────────────────────────────────

    static void testDAGEdgeCount() {
        Graph g = new DAGGenerator().generateGraph(V, SEED);
        int expected = 5 * V;
        check("DAG edge count = 5V (" + expected + ")", g.getEdgeList().size() == expected);
    }

    static void testDAGIsAcyclic() {
        Graph g = new DAGGenerator().generateGraph(V, SEED);
        // If dagShortestPath doesn't throw, the graph is acyclic
        boolean acyclic = true;
        try {
            g.dagShortestPath(0);
        } catch (IllegalArgumentException e) {
            acyclic = false;
        }
        check("DAG generator produces acyclic graph", acyclic);
    }

    // ── Deterministic: same seed → same graph ──────────────────

    static void testDeterministic() {
        Graph g1 = new SparseGraphGenerator().generateGraph(V, SEED);
        Graph g2 = new SparseGraphGenerator().generateGraph(V, SEED);

        boolean edgesMatch = true;
        List<Edge> e1 = g1.getEdgeList();
        List<Edge> e2 = g2.getEdgeList();
        if (e1.size() != e2.size()) {
            edgesMatch = false;
        } else {
            for (int i = 0; i < e1.size(); i++) {
                Edge a = e1.get(i), b = e2.get(i);
                if (a.u() != b.u() || a.v() != b.v() || a.weight() != b.weight()) {
                    edgesMatch = false;
                    break;
                }
            }
        }
        check("Same seed produces identical Sparse graph", edgesMatch);
    }

    // ── connectivity check via BFS on undirected adjList ───────

    private static boolean isConnected(Graph g) {
        int n = g.getV();
        if (n <= 1) return true;

        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;
        int count = 1;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge e : g.getAdjList().get(u)) {
                int v = e.v();
                if (!visited[v]) {
                    visited[v] = true;
                    count++;
                    queue.add(v);
                }
            }
        }
        return count == n;
    }

    // ── helper ──────────────────────────────────────────────────

    private static void check(String name, boolean condition) {
        if (condition) {
            System.out.println("  ✓ " + name);
            passed++;
        } else {
            System.out.println("  ✗ " + name);
            failed++;
        }
    }
}
