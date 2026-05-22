package test.algorithms;

import core.Graph;

import java.util.Arrays;


public class DAGShortestPathTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ DAGShortestPathTest ═══");

        testSimpleDAG();
        testMatchesDijkstraOnDAG();
        testCycleDetection();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // 0→1(2), 0→2(4), 1→2(1) → dist[2] = 3
    static void testSimpleDAG() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 2);
        g.addDirectedEdge(0, 2, 4);
        g.addDirectedEdge(1, 2, 1);

        int[] dist = g.dagShortestPath(0);
        check("dag dist[0] == 0", dist[0] == 0);
        check("dag dist[1] == 2", dist[1] == 2);
        check("dag dist[2] == 3", dist[2] == 3);
    }

    // DAG-SP and Dijkstra must return identical distances on a DAG
    static void testMatchesDijkstraOnDAG() {
        Graph g = new Graph(5);
        g.addDirectedEdge(0, 1, 3);
        g.addDirectedEdge(0, 2, 6);
        g.addDirectedEdge(1, 2, 2);
        g.addDirectedEdge(1, 3, 4);
        g.addDirectedEdge(2, 3, 1);
        g.addDirectedEdge(3, 4, 5);

        int[] dijkDist = g.dijkstra(0);
        int[] dagDist  = g.dagShortestPath(0);

        check("DAG-SP matches Dijkstra", Arrays.equals(dijkDist, dagDist));
    }

    // Cycle: 0→1→2→0 — should throw IllegalArgumentException
    static void testCycleDetection() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(1, 2, 1);
        g.addDirectedEdge(2, 0, 1);

        boolean threw = false;
        try {
            g.dagShortestPath(0);
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("cycle throws IllegalArgumentException", threw);
    }

    private static void check(String name, boolean condition) {
        if (condition) { System.out.println("  ✓ " + name); passed++; }
        else           { System.out.println("  ✗ " + name); failed++; }
    }
}
