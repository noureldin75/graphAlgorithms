package generators;

import core.Graph;

public interface GraphGenerator {
    Graph generateGraph(int numVertices, long seed);
}
