package generators;

import core.Edge;

import java.util.List;

public interface GraphGenerator {
    List<List<Edge>> generateGraph(int numVertices,long seed);
}
