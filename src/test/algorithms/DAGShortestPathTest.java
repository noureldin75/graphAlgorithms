package test.algorithms;

import core.Graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class DAGShortestPathTest {

    // 0→1(2), 0→2(4), 1→2(1) → dist[2] = 3
    @Test
    void testSimpleDAG() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 2);
        g.addDirectedEdge(0, 2, 4);
        g.addDirectedEdge(1, 2, 1);

        int[] dist = g.dagShortestPath(0);
        assertEquals(0, dist[0], "dag dist[0] == 0");
        assertEquals(2, dist[1], "dag dist[1] == 2");
        assertEquals(3, dist[2], "dag dist[2] == 3");
    }

    // DAG-SP and Dijkstra must return identical distances on a DAG
    @Test
    void testMatchesDijkstraOnDAG() {
        Graph g = new Graph(5);
        g.addDirectedEdge(0, 1, 3);
        g.addDirectedEdge(0, 2, 6);
        g.addDirectedEdge(1, 2, 2);
        g.addDirectedEdge(1, 3, 4);
        g.addDirectedEdge(2, 3, 1);
        g.addDirectedEdge(3, 4, 5);

        int[] dijkDist = g.dijkstra(0);
        int[] dagDist  = g.dagShortestPath(0);

        assertArrayEquals(dijkDist, dagDist, "DAG-SP matches Dijkstra");
    }

    // Cycle: 0→1→2→0 — should throw IllegalArgumentException
    @Test
    void testCycleDetection() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(1, 2, 1);
        g.addDirectedEdge(2, 0, 1);

        assertThrows(IllegalArgumentException.class, () -> g.dagShortestPath(0),
                "cycle throws IllegalArgumentException");
    }
}
