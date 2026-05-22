package test.algorithms;

import core.Edge;
import core.Graph;

import java.util.List;


public class PrimMSTTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) {
        System.out.println("═══ PrimMSTTest ═══");

        testTriangle();
        testFourVertexKnownMST();
        testSingleVertex();

        System.out.printf("%n  Results: %d passed, %d failed%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // Triangle: 0--1 (1), 1--2 (2), 0--2 (3) → MST weight = 3
    static void testTriangle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 2, 3);

        List<Edge> mst = g.primMST();
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

        List<Edge> mst = g.primMST();
        int w = mst.stream().mapToInt(Edge::weight).sum();

        check("4-vertex MST has 3 edges", mst.size() == 3);
        check("4-vertex MST weight == 6", w == 6);
    }

    static void testSingleVertex() {
        Graph g = new Graph(1);
        List<Edge> mst = g.primMST();
        check("single vertex MST is empty", mst.isEmpty());
    }

    private static void check(String name, boolean condition) {
        if (condition) { System.out.println("  ✓ " + name); passed++; }
        else           { System.out.println("  ✗ " + name); failed++; }
    }
}
