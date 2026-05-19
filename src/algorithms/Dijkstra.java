package algorithms;

import core.Edge;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

record pair(int vertex,int weight){}

public class Dijkstra {
    int v;
    private int[] dist;

    public Dijkstra(int v) {
        this.v = v;
        this.dist = new int[v];
    }

    public int[] dijkstra(int source,List<List<Edge>> adjlist){
        PriorityQueue<pair> pq=new PriorityQueue<>(Comparator.comparingInt(pair::weight));
        Arrays.fill(dist,Integer.MAX_VALUE);
        pq.add(new pair(source,0));
        dist[source]=0;
        while(!pq.isEmpty()){
            pair highest_priority=pq.poll();
            int node=highest_priority.vertex();
            int weight=highest_priority.weight();
            if (weight > dist[node]) continue; // remove any redundanvy from proirity queue
            for(Edge edge: adjlist.get(node)){
                int ellyRay7lo = edge.v();
                int weight_ellyRay7lo= edge.weight();
                if(dist[node] + weight_ellyRay7lo < dist[ellyRay7lo]){
                    dist[ellyRay7lo]=dist[node] + weight_ellyRay7lo;
                    pq.add(new pair(ellyRay7lo,dist[ellyRay7lo]));
                }
            }


        }
        return dist;

    }
}
