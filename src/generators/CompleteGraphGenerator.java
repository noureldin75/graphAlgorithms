package generators;

import core.Graph;
import java.util.Random;

public class CompleteGraphGenerator implements GraphGenerator {

    @Override
    public Graph generateGraph(int V, long seed) {
        Graph graph = new Graph(V);
        Random rand = new Random(seed);

        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                int weight = rand.nextInt(1000) + 1;
                graph.addEdge(i, j, weight);
            }
        }

        return graph;
    }
}