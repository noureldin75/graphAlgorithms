package test.algorithms;

import core.Graph;

import java.util.Arrays;


public class DijkstraTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ DijkstraTest ═══");

        testSimplePath();
        testUnreachableVertex();
        testDirectedGraph();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // 0--1(2), 1--2(3), 0--2(10) → dist[0]=0, dist[1]=2, dist[2]=5
    static void testSimplePath() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(0, 2, 10);

        int[] dist = g.dijkstra(0);
        check("dist[0] == 0", dist[0] == 0);
        check("dist[1] == 2", dist[1] == 2);
        check("dist[2] == 5 (via 0→1→2)", dist[2] == 5);
    }

    // Vertex 2 has no path from source 0 in a directed graph
    static void testUnreachableVertex() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 5);
        // no edge to 2

        int[] dist = g.dijkstra(0);
        check("dist[0] == 0", dist[0] == 0);
        check("dist[1] == 5", dist[1] == 5);
        check("dist[2] == MAX (unreachable)", dist[2] == Integer.MAX_VALUE);
    }

    // 0→1(1), 0→2(4), 1→2(2) → dist[2] = 3
    static void testDirectedGraph() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(0, 2, 4);
        g.addDirectedEdge(1, 2, 2);

        int[] dist = g.dijkstra(0);
        check("directed dist[2] == 3 (via 0→1→2)", dist[2] == 3);
    }

    private static void check(String name, boolean condition) {
        if (condition) { System.out.println("  ✓ " + name); passed++; }
        else           { System.out.println("  ✗ " + name); failed++; }
    }
}
