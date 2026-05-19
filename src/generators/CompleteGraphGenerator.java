package generators;

import core.Edge;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompleteGraphGenerator implements GraphGenerator {

    @Override
    public List<List<Edge>> generateGraph(int V, long seed) {
        List<List<Edge>> adjlist = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjlist.add(new ArrayList<>());
        }

        Random rand = new Random(seed);

        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                int weight = rand.nextInt(1000) + 1;

                adjlist.get(i).add(new Edge(i, j, weight));
                adjlist.get(j).add(new Edge(j, i, weight));
            }
        }

        return adjlist;
    }
}