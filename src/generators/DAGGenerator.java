package generators;

import core.Edge;

import java.util.*;

public class DAGGenerator implements GraphGenerator {
    @Override
    public List<List<Edge>> generateGraph(int V, long seed) {
        List<List<Edge>> adjlist = new ArrayList<>();
        Random rand = new Random(seed);
        int [] vertices = new int[V];
        for (int i = 0; i < V; i++) {
            vertices[i] = i;
            adjlist.add(new ArrayList<>());
        }

        //manual shuffle as collections shuffle doesnt work on arrays
        for (int i = V - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = vertices[i];
            vertices[i] = vertices[j];
            vertices[j] = temp;
        }
        Set<Long> seenEdges = new HashSet<>();
        int edgeCount = 0;
        int targetEdges = 5 * V;
        for (int i = 0; i < V - 1; i++) {
            int u      = vertices[i];
            int v      = vertices[i + 1];
            int weight = rand.nextInt(1000) + 1;

            adjlist.get(u).add(new Edge(u, v, weight));

            seenEdges.add(getEdgeHash(u, v));
            edgeCount++;
        }
        int[] rank = new int[V];

        for (int i = 0; i < V; i++)
            rank[vertices[i]] = i;

        while (edgeCount < targetEdges) {
            int u = rand.nextInt(V);
            int v = rand.nextInt(V);

            // Only allow forward edges (no cycles)
            if (rank[u] >= rank[v]) continue;

            long edgeHash = getEdgeHash(u, v);

            if (!seenEdges.contains(edgeHash)) {
                int weight = rand.nextInt(1000) + 1;

                adjlist.get(u).add(new Edge(u, v, weight));
                seenEdges.add(edgeHash);
                edgeCount++;
            }
        }

        return adjlist;


    }
    private long getEdgeHash(int u, int v) {
        return (long) u * 100000L + v;
    }

}
