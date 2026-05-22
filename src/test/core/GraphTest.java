package test.core;

import core.Edge;
import core.Graph;


public class GraphTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ GraphTest ═══");

        testAddEdgeBidirectional();
        testAddDirectedEdgeOneWay();
        testEdgeWeights();
        testEdgeListPopulated();
        testVertexCount();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    static void testAddEdgeBidirectional() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 10);

        boolean uv = g.getAdjList().get(0).stream().anyMatch(e -> e.v() == 1);
        boolean vu = g.getAdjList().get(1).stream().anyMatch(e -> e.v() == 0);
        check("addEdge creates u→v", uv);
        check("addEdge creates v→u", vu);
    }

    static void testAddDirectedEdgeOneWay() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 10);

        boolean uv = g.getAdjList().get(0).stream().anyMatch(e -> e.v() == 1);
        boolean vu = g.getAdjList().get(1).stream().anyMatch(e -> e.v() == 0);
        check("addDirectedEdge creates u→v", uv);
        check("addDirectedEdge does NOT create v→u", !vu);
    }

    static void testEdgeWeights() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 42);

        int w1 = g.getAdjList().get(0).get(0).weight();
        int w2 = g.getAdjList().get(1).get(0).weight();
        check("weight correct in u→v", w1 == 42);
        check("weight correct in v→u", w2 == 42);
    }

    static void testEdgeListPopulated() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 5);
        g.addDirectedEdge(2, 3, 7);

        // addEdge adds 1 to edgeList, addDirectedEdge adds 1
        check("edgeList size == 2", g.getEdgeList().size() == 2);
    }

    static void testVertexCount() {
        Graph g = new Graph(50);
        check("getV == 50", g.getV() == 50);
        check("adjList size == 50", g.getAdjList().size() == 50);
    }

    // ── helper ──────────────────────────────────────────────────

    private static void check(String name, boolean condition) {
        if (condition) { System.out.println("  ✓ " + name); passed++; }
        else           { System.out.println("  ✗ " + name); failed++; }
    }
}
