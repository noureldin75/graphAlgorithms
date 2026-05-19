package generators;

import core.Edge;
import core.Graph;

import java.util.List;

public interface GraphGenerator {
    Graph generateGraph(int numVertices, long seed);
}
