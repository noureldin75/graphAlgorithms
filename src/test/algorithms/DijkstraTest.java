package test.algorithms;

import core.Graph;

import java.util.Arrays;

/**
 * Correctness tests for Dijkstra's SSSP algorithm.
 */
public class DijkstraTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ DijkstraTest ═══");

        testSimpleLinearGraph();
        testShortcutPath();
        testSourceDistanceIsZero();
        testUnreachableVertex();
        testDiamondGraph();
        testSingleVertex();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ── Linear: 0─1(2), 1─2(3), 2─3(4) ────────────────────────
    //    dist from 0: [0, 2, 5, 9]

    static void testSimpleLinearGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 4);

        int[] dist = g.dijkstra(0);

        check("linear dist[0] = 0", dist[0] == 0);
        check("linear dist[1] = 2", dist[1] == 2);
        check("linear dist[2] = 5", dist[2] == 5);
        check("linear dist[3] = 9", dist[3] == 9);
    }

    // ── Shortcut: direct 0→3(8) vs 0→1→2→3(9) ────────────────
    //    0─1(2), 1─2(3), 2─3(4), 0─3(8)
    //    dist from 0: [0, 2, 5, 8]

    static void testShortcutPath() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 4);
        g.addEdge(0, 3, 8);

        int[] dist = g.dijkstra(0);

        check("shortcut dist[3] = 8 (direct edge)", dist[3] == 8);
    }

    // ── Source always has distance 0 ───────────────────────────

    static void testSourceDistanceIsZero() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 7);

        int[] dist = g.dijkstra(1);
        check("source vertex distance = 0", dist[1] == 0);
    }

    // ── Unreachable vertex in directed graph ───────────────────

    static void testUnreachableVertex() {
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(1, 2, 2);
        // vertex 3 has no incoming or outgoing edges

        int[] dist = g.dijkstra(0);

        check("reachable dist[1] = 1", dist[1] == 1);
        check("reachable dist[2] = 3", dist[2] == 3);
        check("unreachable dist[3] = MAX_VALUE", dist[3] == Integer.MAX_VALUE);
    }

    // ── Diamond: pick the shorter path ─────────────────────────
    //        1
    //      / | \
    //   1/   |5  \2
    //   0    |    3
    //    \   |  /
    //   10\  | /1
    //       2
    //
    //   0→1(1), 1→3(2), 0→2(10), 2→3(1), 1→2(5)
    //   dist from 0: [0, 1, 6, 3]

    static void testDiamondGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(0, 2, 10);
        g.addEdge(2, 3, 1);
        g.addEdge(1, 2, 5);

        int[] dist = g.dijkstra(0);

        check("diamond dist[0] = 0", dist[0] == 0);
        check("diamond dist[1] = 1", dist[1] == 1);
        check("diamond dist[2] = 6", dist[2] == 6);
        check("diamond dist[3] = 3", dist[3] == 3);
    }

    // ── Single vertex ──────────────────────────────────────────

    static void testSingleVertex() {
        Graph g = new Graph(1);
        int[] dist = g.dijkstra(0);
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
