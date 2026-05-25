package generators;

import core.Edge;
import core.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DenseGraphGenerator implements GraphGenerator {

    @Override
    public Graph generateGraph(int V, long seed) {
        Graph graph = new Graph(V);
        Random rand = new Random(seed);

        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            vertices.add(i);
        }

        Collections.shuffle(vertices, rand);

        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {

                int u = vertices.get(i);
                int v = vertices.get(j);

                if (j == i + 1 || rand.nextDouble() < 0.25) {
                    int weight = rand.nextInt(1000) + 1;


                    graph.addEdge(u, v, weight);
                }
            }
        }

        return graph;
    }
}