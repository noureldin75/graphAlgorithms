package test.algorithms;

import core.Edge;
import core.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class KruskalMSTTest {

    // Triangle: 0--1 (1), 1--2 (2), 0--2 (3) → MST weight = 3
    @Test
    void testTriangle() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(0, 2, 3);

        List<Edge> mst = g.kruskalMST();
        int w = mst.stream().mapToInt(Edge::weight).sum();

        assertEquals(2, mst.size(), "triangle MST has 2 edges");
        assertEquals(3, w, "triangle MST weight == 3");
    }

    //   0--1(1)  0--2(4)  1--2(2)  1--3(6)  2--3(3) → MST = 1+2+3 = 6
    @Test
    void testFourVertexKnownMST() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 4);
        g.addEdge(1, 2, 2);
        g.addEdge(1, 3, 6);
        g.addEdge(2, 3, 3);

        List<Edge> mst = g.kruskalMST();
        int w = mst.stream().mapToInt(Edge::weight).sum();

        assertEquals(3, mst.size(), "4-vertex MST has 3 edges");
        assertEquals(6, w, "4-vertex MST weight == 6");
    }

    // Prim and Kruskal must produce same total weight
    @Test
    void testPrimAndKruskalAgree() {
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

        assertEquals(primW, kruskalW, "Prim and Kruskal produce same MST weight");
    }
}
