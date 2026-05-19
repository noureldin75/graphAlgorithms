package generators;

import core.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DenseGraphGenerator implements GraphGenerator {

    @Override
    public List<List<Edge>> generateGraph(int V, long seed) {
        List<List<Edge>> adjlist = new ArrayList<>();
        Random rand = new Random(seed);

        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            vertices.add(i);
            adjlist.add(new ArrayList<>());
        }

        Collections.shuffle(vertices, rand);

        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {

                int u = vertices.get(i);
                int v = vertices.get(j);

                if (j == i + 1 || rand.nextDouble() < 0.25) {
                    int weight = rand.nextInt(1000) + 1;

                    adjlist.get(u).add(new Edge(u, v, weight));
                    adjlist.get(v).add(new Edge(v, u, weight));
                }
            }
        }

        return adjlist;
    }
}