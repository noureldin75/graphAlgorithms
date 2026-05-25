package test.core;

import core.Edge;
import core.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class GraphTest {

    @Test
    void testAddEdgeBidirectional() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 10);

        boolean uv = g.getAdjList().get(0).stream().anyMatch(e -> e.v() == 1);
        boolean vu = g.getAdjList().get(1).stream().anyMatch(e -> e.v() == 0);
        assertTrue(uv, "addEdge creates u→v");
        assertTrue(vu, "addEdge creates v→u");
    }

    @Test
    void testAddDirectedEdgeOneWay() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 10);

        boolean uv = g.getAdjList().get(0).stream().anyMatch(e -> e.v() == 1);
        boolean vu = g.getAdjList().get(1).stream().anyMatch(e -> e.v() == 0);
        assertTrue(uv, "addDirectedEdge creates u→v");
        assertFalse(vu, "addDirectedEdge does NOT create v→u");
    }

    @Test
    void testEdgeWeights() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 42);

        int w1 = g.getAdjList().get(0).get(0).weight();
        int w2 = g.getAdjList().get(1).get(0).weight();
        assertEquals(42, w1, "weight correct in u→v");
        assertEquals(42, w2, "weight correct in v→u");
    }

    @Test
    void testEdgeListPopulated() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 5);
        g.addDirectedEdge(2, 3, 7);

        // addEdge adds 1 to edgeList, addDirectedEdge adds 1
        assertEquals(2, g.getEdgeList().size(), "edgeList size == 2");
    }

    @Test
    void testVertexCount() {
        Graph g = new Graph(50);
        assertEquals(50, g.getV(), "getV == 50");
        assertEquals(50, g.getAdjList().size(), "adjList size == 50");
    }
}
