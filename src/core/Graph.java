package core;


import java.util.List;

public class Graph {
    private List<List<Edge>> adjList;
    private List<Edge> allEdges;

    public void addEdge(int u,int v,int weight){
        Edge edge = new Edge(u,v,weight);
        adjList.get(u).add(edge);
        adjList.get(v).add(edge);
        allEdges.add(edge);
    }
    public void addDirectedEdge(int u ,int v,int weight){
        Edge edge = new Edge(u,v,weight);
        adjList.get(u).add(edge);
        allEdges.add(edge);
    }



}
