package test.algorithms;

import core.Edge;
import core.Graph;

import java.util.List;


public class KruskalMSTTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ KruskalMSTTest ═══");

        testTriangle();
        testFourVertexKnownMST();
        testPrimAndKruskalAgree();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // Triangle: 0--1 (1), 1--2 (2), 0--2 (3) → MST weight = 3
    static void testTriangle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 2, 3);

        List<Edge> mst = g.kruskalMST();
        int w = mst.stream().mapToInt(Edge::weight).sum();

        check("triangle MST has 2 edges", mst.size() == 2);
        check("triangle MST weight == 3", w == 3);
    }

    //   0--1(1)  0--2(4)  1--2(2)  1--3(6)  2--3(3) → MST = 1+2+3 = 6
    static void testFourVertexKnownMST() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 4);
        g.addEdge(1, 2, 2);
        g.addEdge(1, 3, 6);
        g.addEdge(2, 3, 3);

        List<Edge> mst = g.kruskalMST();
        int w = mst.stream().mapToInt(Edge::weight).sum();

        check("4-vertex MST has 3 edges", mst.size() == 3);
        check("4-vertex MST weight == 6", w == 6);
    }

    // Prim and Kruskal must produce same total weight
    static void testPrimAndKruskalAgree() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 2);
        g.addEdge(0, 3, 6);
        g.addEdge(1, 2, 3);
        g.addEdge(1, 3, 8);
        g.addEdge(1, 4, 5);
        g.addEdge(2, 4, 7);
        g.addEdge(3, 4, 9);

        int primW    = g.primMST().stream().mapToInt(Edge::weight).sum();
        int kruskalW = g.kruskalMST().stream().mapToInt(Edge::weight).sum();

        check("Prim and Kruskal produce same MST weight", primW == kruskalW);
    }

    private static void check(String name, boolean condition) {
        if (condition) { System.out.println("  ✓ " + name); passed++; }
        else           { System.out.println("  ✗ " + name); failed++; }
    }
}
