package test.generators;

import core.Edge;
import core.Graph;
import generators.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class GraphGeneratorTest {

    private static final int V = 500;        // smaller V for fast tests
    private static final long SEED = 12345L;

    // sparse: E = 5*V

    @Test
    void testSparseEdgeCount() {
        Graph g = new SparseGraphGenerator().generateGraph(V, SEED);
        int expected = 5 * V;
        assertEquals(expected, g.getEdgeList().size(), "Sparse edge count = 5V (" + expected + ")");
    }

    @Test
    void testSparseConnectivity() {
        Graph g = new SparseGraphGenerator().generateGraph(V, SEED);
        assertTrue(isConnected(g), "Sparse graph is connected");
    }

    // Dense: more edges than sparse, connected
    @Test
    void testDenseEdgeCountAboveSparse() {
        Graph gDense  = new DenseGraphGenerator().generateGraph(V, SEED);
        Graph gSparse = new SparseGraphGenerator().generateGraph(V, SEED);
        assertTrue(gDense.getEdgeList().size() > gSparse.getEdgeList().size(),
                "Dense has more edges than Sparse");
    }

    @Test
    void testDenseConnectivity() {
        Graph g = new DenseGraphGenerator().generateGraph(V, SEED);
        assertTrue(isConnected(g), "Dense graph is connected");
    }

    //  Complete: E = V*(V-1)/2

    @Test
    void testCompleteEdgeCount() {
        int smallV = 100; // keep manageable
        Graph g = new CompleteGraphGenerator().generateGraph(smallV, SEED);
        int expected = smallV * (smallV - 1) / 2;
        assertEquals(expected, g.getEdgeList().size(),
                "Complete edge count = V(V-1)/2 (" + expected + ")");
    }

    @Test
    void testCompleteConnectivity() {
        Graph g = new CompleteGraphGenerator().generateGraph(100, SEED);
        assertTrue(isConnected(g), "Complete graph is connected");
    }

    // DAG: E = 5*V, no cycles

    @Test
    void testDAGEdgeCount() {
        Graph g = new DAGGenerator().generateGraph(V, SEED);
        int expected = 5 * V;
        assertEquals(expected, g.getEdgeList().size(),
                "DAG edge count = 5V (" + expected + ")");
    }

    @Test
    void testDAGIsAcyclic() {
        Graph g = new DAGGenerator().generateGraph(V, SEED);
        // If dagShortestPath doesn't throw, the graph is acyclic
        assertDoesNotThrow(() -> g.dagShortestPath(0),
                "DAG generator produces acyclic graph");
    }

    // Deterministic: same seed → same graph

    @Test
    void testDeterministic() {
        Graph g1 = new SparseGraphGenerator().generateGraph(V, SEED);
        Graph g2 = new SparseGraphGenerator().generateGraph(V, SEED);

        List<Edge> e1 = g1.getEdgeList();
        List<Edge> e2 = g2.getEdgeList();
        assertEquals(e1.size(), e2.size(), "Same seed produces same edge count");

        for (int i = 0; i < e1.size(); i++) {
            Edge a = e1.get(i), b = e2.get(i);
            assertEquals(a.u(), b.u(), "Edge " + i + " u matches");
            assertEquals(a.v(), b.v(), "Edge " + i + " v matches");
            assertEquals(a.weight(), b.weight(), "Edge " + i + " weight matches");
        }
    }

    //connectivity check via BFS on undirected adjList

    private static boolean isConnected(Graph g) {
        int n = g.getV();
        if (n <= 1) return true;

        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited[0] = true;
        int count = 1;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge e : g.getAdjList().get(u)) {
                int v = e.v();
                if (!visited[v]) {
                    visited[v] = true;
                    count++;
                    queue.add(v);
                }
            }
        }
        return count == n;
    }
}
