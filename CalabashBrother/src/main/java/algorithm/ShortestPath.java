package algorithm;

import java.util.*;

public class ShortestPath {

    public static void main(String[] args) {
        ShortestPath sp = new ShortestPath();
        ShortestPath.Graph<Character> graph = sp.new Graph<>();
        graph.add('s', Arrays.asList(sp.new Edge<>('t', 10.0),
                sp.new Edge<>('y', 5.0)));
        graph.add('t', Arrays.asList(sp.new Edge<>('x', 1.0),
                sp.new Edge<>('y', 2.0)));
        graph.add('y', Arrays.asList(sp.new Edge<>('t', 3.0),
                sp.new Edge<>('x', 9.0), sp.new Edge<>('z', 2.0)));
        graph.add('z', Arrays.asList(sp.new Edge<>('s', 7.0),
                sp.new Edge<>('x', 6.0)));
        graph.add('x', Arrays.asList(sp.new Edge<>('z', 4.0)));
        System.out.println(graph.getShortestPath('s', 't'));
    }

    public class Edge<T> implements Comparable<Edge<T>> {

        private T vertex;
        private Double distance;

        public Edge(T vertex, Double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        T getVertex() {
            return vertex;
        }

        void setVertex(T vertex) {
            this.vertex = vertex;
        }

        Double getDistance() {
            return distance;
        }

        void setDistance(Double distance) {
            this.distance = distance;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((distance == null) ? 0 : distance.hashCode());
            result = prime * result + ((vertex == null) ? 0 : vertex.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Edge other = (Edge) obj;
            if (distance == null) {
                if (other.distance != null)
                    return false;
            } else if (!distance.equals(other.distance)) {
                return false;
            }
            if (vertex == null) {
                if (other.vertex != null) {
                    return false;
                }
            } else if (!vertex.equals(other.vertex)) {
                return false;
            }

            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compareTo(Edge o) {
            return distance.compareTo(o.distance);
        }
    }

    public class Graph<T> {

        private final Map<T, List<Edge<T>>> graph;

        public Graph() {
            graph = new HashMap<>();
        }

        public void add(T vertex, List<Edge<T>> edges) {
            graph.put(vertex, edges);
        }

        public List<T> getShortestPath(T start, T end) {
            final Map<T, Double> distances = new HashMap<>();
            final Map<T, Edge<T>> previous = new HashMap<>();
            PriorityQueue<Edge<T>> edges = new PriorityQueue<>();
            for (T vertex : graph.keySet()) {
                if (vertex.equals(start)) {
                    distances.put(vertex, 0.0);
                    edges.add(new Edge<>(vertex, 0.0));
                } else {
                    distances.put(vertex, Double.MAX_VALUE);
                    edges.add(new Edge<>(vertex, Double.MAX_VALUE));
                }
                previous.put(vertex, null);
            }
            while (!edges.isEmpty()) {
                Edge<T> shortest = edges.poll();
                if (shortest.getVertex().equals(end)) {
                    final List<T> path = new ArrayList<>();
                    while (previous.get(shortest.getVertex()) != null) {
                        path.add(shortest.getVertex());
                        shortest = previous.get(shortest.getVertex());
                    }
                    return path;
                }
                if (distances.get(shortest.getVertex()) == Double.MAX_VALUE) {
                    break;
                }
                for (Edge<T> neighbor : graph.get(shortest.getVertex())) {
                    Double alt = distances.get(shortest.getVertex()) + neighbor.getDistance();
                    if (alt < distances.get(neighbor.getVertex())) {
                        distances.put(neighbor.getVertex(), alt);
                        previous.put(neighbor.getVertex(), shortest);

                        for (Edge<T> node : edges) {
                            if (node.getVertex().equals(neighbor.getVertex())) {
                                edges.remove(node);
                                node.setDistance(alt);
                                edges.add(node);
                                break;
                            }
                        }
                    }
                }
            }
            return new ArrayList<>(distances.keySet());
        }
    }
}
