package generators;

import core.Graph;
import java.util.*;

public class DAGGenerator implements GraphGenerator {

    @Override
    public Graph generateGraph(int V, long seed) {
        Graph graph = new Graph(V);
        Random rand = new Random(seed);

        Set<Long> seenEdges = new HashSet<>();
        int targetEdges = 5 * V;
        int edgeCount = 0;


        for (int v = 1; v < V; v++) {
            int u = rand.nextInt(v);
            int weight = rand.nextInt(1000) + 1;

            graph.addDirectedEdge(u, v, weight);
            seenEdges.add((long) u * 100_000L + v);
            edgeCount++;
        }

        while (edgeCount < targetEdges) {
            int u = rand.nextInt(V - 1);
            int remaining = V - 1 - u;
            int v = u + 1 + rand.nextInt(remaining);

            long edgeHash = (long) u * 100_000L + v;

            if (!seenEdges.contains(edgeHash)) {
                int weight = rand.nextInt(1000) + 1;

                graph.addDirectedEdge(u, v, weight);
                seenEdges.add(edgeHash);
                edgeCount++;
            }
        }

        return graph;
    }
}