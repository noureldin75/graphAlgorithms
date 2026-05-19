package algorithms;

import core.Edge;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class DAG_Sh_Path {
    static final byte NOT_VISITED = 0;
    static final byte PROCESSING = 1;
    static final byte VISITED = 2;

    private int v;
    private byte[] state;
    private int[] dist;

    public DAG_Sh_Path(int v) {
        this.v = v;
        this.state = new byte[v];
        this.dist = new int[v];
    }

    public int[] dagShortestPath(int source, List<List<Edge>> adjlist) {
        Stack<Integer> st = new Stack<>();
        Arrays.fill(state, NOT_VISITED);
        Arrays.fill(dist, Integer.MAX_VALUE);

        for (int i = 0; i < v; i++) {
            if (state[i] == NOT_VISITED) {
                DFS(adjlist, i, st);
            }
        }

        dist[source] = 0;

        while (!st.isEmpty()) {
            int node = st.pop();

            if (dist[node] != Integer.MAX_VALUE) {
                for (Edge edge : adjlist.get(node)) {
                    int neighbor = edge.v();
                    int weight = edge.weight();

                    if (dist[node] + weight < dist[neighbor]) {
                        dist[neighbor] = dist[node] + weight;
                    }
                }
            }
        }

        return dist;
    }

    private void DFS(List<List<Edge>> adjlist, int node, Stack<Integer> st) {
        state[node] = PROCESSING;

        for (Edge edge : adjlist.get(node)) {
            int to = edge.v();

            if (state[to] == PROCESSING) {
                throw new IllegalArgumentException("Cycle detected! Graph is not a DAG.");
            }

            if (state[to] == NOT_VISITED) {
                DFS(adjlist, to, st);
            }
        }

        state[node] = VISITED;
        st.push(node);
    }
}