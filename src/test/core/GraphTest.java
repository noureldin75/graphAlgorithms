package test.core;

import core.Edge;
import core.Graph;

import java.util.List;

/**
 * Tests for {@link Graph}: addEdge, addDirectedEdge correctness.
 */
public class GraphTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ GraphTest ═══");

        testAddEdgeCreatesBidirectional();
        testAddEdgeWeight();
        testAddEdgePopulatesEdgeList();
        testAddDirectedEdgeIsOneWay();
        testAddDirectedEdgePopulatesEdgeList();
        testMultipleEdges();
        testVertexCount();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ── addEdge ─────────────────────────────────────────────────

    static void testAddEdgeCreatesBidirectional() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 10);

        // u→v exists
        boolean uToV = g.getAdjList().get(0).stream()
                .anyMatch(e -> e.v() == 1 && e.weight() == 10);
        // v→u exists
        boolean vToU = g.getAdjList().get(1).stream()
                .anyMatch(e -> e.v() == 0 && e.weight() == 10);

        check("addEdge creates u→v", uToV);
        check("addEdge creates v→u", vToU);
    }

    static void testAddEdgeWeight() {
        Graph g = new Graph(3);
        g.addEdge(1, 2, 42);

        int w1 = g.getAdjList().get(1).get(0).weight();
        int w2 = g.getAdjList().get(2).get(0).weight();
        check("addEdge weight in u→v", w1 == 42);
        check("addEdge weight in v→u", w2 == 42);
    }

    static void testAddEdgePopulatesEdgeList() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 5);
        g.addEdge(2, 3, 7);

        // edgeList stores one Edge per addEdge call
        check("edgeList size after 2 addEdge calls", g.getEdgeList().size() == 2);
    }

    // ── addDirectedEdge ─────────────────────────────────────────

    static void testAddDirectedEdgeIsOneWay() {
        Graph g = new Graph(4);
        g.addDirectedEdge(0, 1, 10);

        boolean uToV = g.getAdjList().get(0).stream()
                .anyMatch(e -> e.v() == 1);
        boolean vToU = g.getAdjList().get(1).stream()
                .anyMatch(e -> e.v() == 0);

        check("addDirectedEdge creates u→v", uToV);
        check("addDirectedEdge does NOT create v→u", !vToU);
    }

    static void testAddDirectedEdgePopulatesEdgeList() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 5);
        g.addDirectedEdge(1, 2, 7);

        check("edgeList size after 2 addDirectedEdge calls", g.getEdgeList().size() == 2);
    }

    // ── general ─────────────────────────────────────────────────

    static void testMultipleEdges() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 2);
        g.addEdge(0, 3, 3);

        List<Edge> adj0 = g.getAdjList().get(0);
        check("vertex 0 has 3 neighbours after 3 addEdge calls", adj0.size() == 3);
    }

    static void testVertexCount() {
        Graph g = new Graph(100);
        check("getV returns constructor arg", g.getV() == 100);
        check("adjList size matches V", g.getAdjList().size() == 100);
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
