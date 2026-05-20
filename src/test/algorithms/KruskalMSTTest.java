package test.algorithms;

import core.Edge;
import core.Graph;

import java.util.List;

/**
 * Correctness tests for Kruskal's MST on small known graphs.
 * Uses the same graphs as PrimMSTTest to ensure both produce equivalent results.
 */
public class KruskalMSTTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ KruskalMSTTest ═══");

        testTriangleGraph();
        testFourVertexGraph();
        testMSTHasVMinus1Edges();
        testMatchesPrimWeight();
        testSingleVertex();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ── Triangle: 0─1(1), 1─2(2), 0─2(3)  →  MST weight = 3 ──

    static void testTriangleGraph() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 2, 3);

        List<Edge> mst = g.kruskalMST();
        long totalWeight = mst.stream().mapToLong(Edge::weight).sum();

        check("triangle MST edge count", mst.size() == 2);
        check("triangle MST weight = 3", totalWeight == 3);
    }

    // ── 4-vertex:  known optimal MST weight = 6 ────────────────
    //    0─1(1), 0─2(4), 1─2(2), 1─3(6), 2─3(3)
    //    MST: 0─1(1), 1─2(2), 2─3(3) = 6

    static void testFourVertexGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 4);
        g.addEdge(1, 2, 2);
        g.addEdge(1, 3, 6);
        g.addEdge(2, 3, 3);

        List<Edge> mst = g.kruskalMST();
        long totalWeight = mst.stream().mapToLong(Edge::weight).sum();

        check("4-vertex MST edge count", mst.size() == 3);
        check("4-vertex MST weight = 6", totalWeight == 6);
    }

    // ── MST must have exactly V-1 edges for a connected graph ──

    static void testMSTHasVMinus1Edges() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 2, 3);
        g.addEdge(0, 3, 6);
        g.addEdge(0, 4, 7);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 5);
        g.addEdge(1, 4, 8);
        g.addEdge(2, 3, 4);
        g.addEdge(2, 4, 9);
        g.addEdge(3, 4, 10);

        List<Edge> mst = g.kruskalMST();
        check("5-vertex complete graph MST has 4 edges", mst.size() == 4);
    }

    // ── Kruskal and Prim must produce the same total weight ─────

    static void testMatchesPrimWeight() {
        Graph g = new Graph(6);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 4, 2);
        g.addEdge(4, 5, 6);
        g.addEdge(3, 5, 5);

        long primWeight  = g.primMST().stream().mapToLong(Edge::weight).sum();
        long krusWeight  = g.kruskalMST().stream().mapToLong(Edge::weight).sum();

        check("Kruskal weight matches Prim weight (" + primWeight + ")", primWeight == krusWeight);
    }

    // ── Single vertex: empty MST ───────────────────────────────

    static void testSingleVertex() {
        Graph g = new Graph(1);
        List<Edge> mst = g.kruskalMST();
        check("single vertex MST is empty", mst.isEmpty());
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
