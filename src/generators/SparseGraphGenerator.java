package generators;

import core.Edge;

import java.util.*;

public class SparseGraphGenerator implements GraphGenerator{
    @Override
    public List<List<Edge>> generateGraph(int V,long seed){
        List<List<Edge>> adjlist=new ArrayList<>();
        Random rand = new Random(seed);

        int targetEdges = 5 * V;
        int edgeCount = 0;

        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < V; i++){
            vertices.add(i);
            adjlist.add(new ArrayList<>());
        }
        Collections.shuffle(vertices, rand);
        Set<Long> seenEdges = new HashSet<>();
        for(int i =1;i<V;i++){
            int u = vertices.get(i);
            int v = vertices.get(rand.nextInt(i));
            int weight = rand.nextInt(1000) + 1;
            adjlist.get(u).add(new Edge(u, v, weight));
            adjlist.get(v).add(new Edge(v, u, weight));

            long edgeHash = getEdgeHash(u, v);
            seenEdges.add(edgeHash);

            edgeCount++;
        }
        while(edgeCount<targetEdges){
            int u = rand.nextInt(V);
            int v = rand.nextInt(V);
            if(u==v) continue;
            long edgeHash = getEdgeHash(u, v);
            if (!seenEdges.contains(edgeHash)) {
                int weight = rand.nextInt(1000) + 1;

                adjlist.get(u).add(new Edge(u, v, weight));
                adjlist.get(v).add(new Edge(v, u, weight));

                seenEdges.add(edgeHash);
                edgeCount++;
            }
        }
        return adjlist;
    }
    private long getEdgeHash(int u, int v) {
        int min = Math.min(u, v);
        int max = Math.max(u, v);

        return (long) min * 100000L + max;
    }
}
