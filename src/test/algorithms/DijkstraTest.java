package test.algorithms;

import core.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DijkstraTest {

    // 0--1(2), 1--2(3), 0--2(10) → dist[0]=0, dist[1]=2, dist[2]=5
    @Test
    void testSimplePath() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(0, 2, 10);

        int[] dist = g.dijkstra(0);
        assertEquals(0, dist[0], "dist[0] == 0");
        assertEquals(2, dist[1], "dist[1] == 2");
        assertEquals(5, dist[2], "dist[2] == 5 (via 0→1→2)");
    }

    // Vertex 2 has no path from source 0 in a directed graph
    @Test
    void testUnreachableVertex() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 5);
        // no edge to 2

        int[] dist = g.dijkstra(0);
        assertEquals(0, dist[0], "dist[0] == 0");
        assertEquals(5, dist[1], "dist[1] == 5");
        assertEquals(Integer.MAX_VALUE, dist[2], "dist[2] == MAX (unreachable)");
    }

    // 0→1(1), 0→2(4), 1→2(2) → dist[2] = 3
    @Test
    void testDirectedGraph() {
        Graph g = new Graph(3);
        g.addDirectedEdge(0, 1, 1);
        g.addDirectedEdge(0, 2, 4);
        g.addDirectedEdge(1, 2, 2);

        int[] dist = g.dijkstra(0);
        assertEquals(3, dist[2], "directed dist[2] == 3 (via 0→1→2)");
    }
}
