package algorithms;
import core.Edge;

import java.util.Comparator;
import java.util.List;

public class kruskal {
    private int v;
    private int[] parent;
    private int[] rank;
    public kruskal(int v) {
        this.v = v;
        this.parent = new int[v];
        this.rank = new int[v];

    }
    public List<Edge> kruskalMst(List<Edge> alledges) {
        for (int i = 0; i < v; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
        alledges.sort(Comparator.comparingInt(Edge::weight));
        List<Edge> mst = new java.util.ArrayList<>();
        for (Edge edge : alledges) {
            int u = edge.u();
            int to = edge.v();          // renamed
            int rootU = find(u);
            int rootV = find(to);       // renamed
            if (rootU != rootV) {
                mst.add(edge);
                union(rootU, rootV);
                if (mst.size() == this.v - 1) break;  // now correctly refers to vertex count
            }
        }
        return mst;
    }

    private void union(int u, int v) {
        if(rank[u]>rank[v]){
            parent[v] = u;
        } else if (rank[u]<rank[v]) {
            parent[u] = v;
        } else {
            parent[v] = u;
            rank[u]++;
        }
    }

    public int find(int v){
        if (parent[v] != v) {
            parent[v] = find(parent[v]);
        }
        return parent[v];
    }

}
