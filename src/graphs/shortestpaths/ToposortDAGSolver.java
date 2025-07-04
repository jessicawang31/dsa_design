package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Topological sorting implementation of the {@link ShortestPathSolver} interface for <b>directed acyclic graphs</b>.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing the toposort-DAG-shortest-paths algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public ToposortDAGSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        // TODO: Replace with your code
        List<V> topologicalOrder = new ArrayList<>();
        Set<V> visited = new HashSet<>();
        dfsPostOrder(graph, start, visited, topologicalOrder);
        Collections.reverse(topologicalOrder);

        for (V vertex : topologicalOrder) {
            distTo.put(vertex, Double.POSITIVE_INFINITY);
            edgeTo.put(vertex, null);
        }
        distTo.put(start, 0.0);

        for (V vertex : topologicalOrder) {
            if (distTo.get(vertex) < Double.POSITIVE_INFINITY) {
                for (Edge<V> edge : graph.neighbors(vertex)) {
                    relaxEdge(edge);
                }
            }
        }
    }

    private void relaxEdge(Edge<V> edge) {
        V from = edge.from;
        V to = edge.to;
        double weight = edge.weight;
        if (distTo.get(to) > distTo.get(from) + weight) {
            distTo.put(to, distTo.get(from) + weight);
            edgeTo.put(to, edge);
        }
    }

    /**
     * Recursively adds nodes from the graph to the result in DFS postorder from the start vertex.
     *
     * @param graph   the input graph.
     * @param start   the start vertex.
     * @param visited the set of visited vertices.
     * @param result  the destination for adding nodes.x
     */
    private void dfsPostOrder(Graph<V> graph, V start, Set<V> visited, List<V> result) {
        // TODO: Replace with your code
        if (!visited.contains(start)) {
            visited.add(start);
            for (Edge<V> edge : graph.neighbors(start)) {
                V neighbour = edge.to;
                if (!visited.contains(neighbour)) {
                    dfsPostOrder(graph, neighbour, visited, result);
                }
            }
            result.add(start);
        }
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}