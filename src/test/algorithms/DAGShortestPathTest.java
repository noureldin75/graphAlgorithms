package test.algorithms;

import core.Graph;

import java.util.Arrays;

/**
 * Correctness tests for DAG Shortest Path (topological-sort based SSSP).
 * Includes cycle-detection exception test.
 */
public class DAGShortestPathTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ DAGShortestPathTest ═══");

        testSimpleDAG();
        testDiamondDAG();
        testMatchesDijkstraOnDAG();
        testUnreachableVertex();
        testCycleDetectionThrowsException();
        testSingleVertex();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ── Simple linear DAG: 0→1(3), 1→2(4), 2→3(2) ────────────
    //    dist from 0: [0, 3, 7, 9]

    static void testSimpleDAG() {
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 3);
        g.addDirectedEdge(1, 2, 4);
        g.addDirectedEdge(2, 3, 2);

        int[] dist = g.dagShortestPath(0);

        check("simple dist[0] = 0", dist[0] == 0);
        check("simple dist[1] = 3", dist[1] == 3);
        check("simple dist[2] = 7", dist[2] == 7);
        check("simple dist[3] = 9", dist[3] == 9);
    }

    // ── Diamond DAG ────────────────────────────────────────────
    //    0→1(1), 0→2(4), 1→3(6), 2→3(1)
    //    dist from 0: [0, 1, 4, 5]  (path 0→2→3 = 5 vs 0→1→3 = 7)

    static void testDiamondDAG() {
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(0, 2, 4);
        g.addDirectedEdge(1, 3, 6);
        g.addDirectedEdge(2, 3, 1);

        int[] dist = g.dagShortestPath(0);

        check("diamond dist[0] = 0", dist[0] == 0);
        check("diamond dist[1] = 1", dist[1] == 1);
        check("diamond dist[2] = 4", dist[2] == 4);
        check("diamond dist[3] = 5 (via 0→2→3)", dist[3] == 5);
    }

    // ── DAG-SP and Dijkstra must produce identical results ─────

    static void testMatchesDijkstraOnDAG() {
        Graph g = new Graph(6);
        g.addDirectedEdge(0, 1, 5);
        g.addDirectedEdge(0, 2, 3);
        g.addDirectedEdge(1, 3, 6);
        g.addDirectedEdge(1, 2, 2);
        g.addDirectedEdge(2, 4, 4);
        g.addDirectedEdge(2, 5, 2);
        g.addDirectedEdge(2, 3, 7);
        g.addDirectedEdge(3, 4, 1);
        g.addDirectedEdge(4, 5, 2);

        int[] dagDist  = g.dagShortestPath(0);
        int[] dijkDist = g.dijkstra(0);

        check("DAG-SP matches Dijkstra on 6-vertex DAG", Arrays.equals(dagDist, dijkDist));
    }

    // ── Unreachable vertices ───────────────────────────────────

    static void testUnreachableVertex() {
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 5);
        // vertex 2 and 3 not reachable from 0

        int[] dist = g.dagShortestPath(0);

        check("unreachable dist[2] = MAX_VALUE", dist[2] == Integer.MAX_VALUE);
        check("unreachable dist[3] = MAX_VALUE", dist[3] == Integer.MAX_VALUE);
    }

    // ── Cycle detection: must throw IllegalArgumentException ───

    static void testCycleDetectionThrowsException() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(1, 2, 2);
        g.addDirectedEdge(2, 0, 3); // creates cycle 0→1→2→0

        boolean threw = false;
        try {
            g.dagShortestPath(0);
        } catch (IllegalArgumentException e) {
            threw = true;
        }

        check("cycle throws IllegalArgumentException", threw);
    }

    // ── Single vertex ──────────────────────────────────────────

    static void testSingleVertex() {
        Graph g = new Graph(1);
        int[] dist = g.dagShortestPath(0);
        check("single vertex dist[0] = 0", dist[0] == 0);
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
