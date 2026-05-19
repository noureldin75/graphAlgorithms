package generators;

import core.Edge;
import core.Graph;

import java.util.*;

public class SparseGraphGenerator implements GraphGenerator{
    @Override
    public Graph generateGraph(int V, long seed){
        Graph graph = new Graph(V);
        Random rand = new Random(seed);

        int targetEdges = 5 * V;
        int edgeCount = 0;

        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < V; i++){
            vertices.add(i);
        }
        Collections.shuffle(vertices, rand);
        Set<Long> seenEdges = new HashSet<>();
        for(int i =1;i<V;i++){
            int u = vertices.get(i);
            int v = vertices.get(rand.nextInt(i));
            int weight = rand.nextInt(1000) + 1;
            graph.addEdge(u, v, weight);


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

              graph.addEdge(u, v, weight);

                seenEdges.add(edgeHash);
                edgeCount++;
            }
        }
        return graph;
    }
    private long getEdgeHash(int u, int v) {
        int min = Math.min(u, v);
        int max = Math.max(u, v);

        return (long) min * 100000L + max;
    }
}
