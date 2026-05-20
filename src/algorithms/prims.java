package algorithms;

import core.Edge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class prims {
    private record Pair(int vertex, int weight) {}

    private final int V;
    private boolean[] visited;
    private int[] parent;
    private int[] key;

    public prims(int v) {
        this.V = v;
        this.visited = new boolean[V];
        this.parent = new int[V];
        this.key = new int[V];
    }

    public List<Edge> primMST(List<List<Edge>> adjlist) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.weight()));

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        Arrays.fill(visited, false);

        key[0] = 0;
        pq.add(new Pair(0, 0));

        while (!pq.isEmpty()) {
            Pair p = pq.poll();
            int u = p.vertex();

            if (visited[u]) continue;

            visited[u] = true;

            for (Edge edge : adjlist.get(u)) {
                int ellyRay7lo = edge.v();
                int weight = edge.weight();

                if (!visited[ellyRay7lo] && weight < key[ellyRay7lo]) {
                    key[ellyRay7lo] = weight;
                    pq.add(new Pair(ellyRay7lo, weight));
                    parent[ellyRay7lo] = u;
                }
            }
        }

        List<Edge> mst = new ArrayList<>();
        for (int i = 1; i < V; i++) {
            if (parent[i] != -1) {
                mst.add(new Edge(parent[i], i, key[i]));
            }
        }
        return mst;
    }
}