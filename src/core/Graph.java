package core;

import algorithms.DAG_Sh_Path;
import algorithms.Dijkstra;
import algorithms.kruskal;
import algorithms.prims;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int V;
    private final List<List<Edge>> adjList;   // for Prim/Dijkstra
    private final List<Edge> edgeList;        // for Kruskal (sorted edge list)

    public Graph(int V) {
        this.V = V;
        this.adjList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }
    }


    public void addEdge(int u, int v, int weight) {
        Edge edge = new Edge(u, v, weight);
        adjList.get(u).add(edge);
        adjList.get(v).add(new Edge(v, u, weight));
        edgeList.add(edge);
    }

    public void addDirectedEdge(int u, int v, int weight) {
        Edge edge = new Edge(u, v, weight);
        adjList.get(u).add(edge);
        edgeList.add(edge);
    }


    public int getV() {
        return V;
    }

    public List<List<Edge>> getAdjList() {
        return adjList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }


    public List<Edge> primMST() {
        prims p = new prims(V);
        return p.primMST(adjList);
    }

    public List<Edge> kruskalMST() {
        kruskal k = new kruskal(V);
        return k.kruskalMst(new ArrayList<>(edgeList));
    }

    public int[] dijkstra(int source) {
        Dijkstra d = new Dijkstra(V);
        return d.dijkstra(source, adjList);
    }

    public int[] dagShortestPath(int source) {
        DAG_Sh_Path dag = new DAG_Sh_Path(V);
        return dag.dagShortestPath(source, adjList);
    }
}

