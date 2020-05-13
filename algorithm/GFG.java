package algorithm;
import java.util.ArrayList;
import java.util.LinkedList;


public class GFG {
    static class Edge {
        int source;
        int destination;
        double weight;

        public Edge(int source, int destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    public static class Graph {
        int vertices;
        LinkedList<Edge>[] adjacencylist;

         Graph(int vertices) {
            this.vertices = vertices;
            adjacencylist = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i < vertices; i++) {
                adjacencylist[i] = new LinkedList<>();
            }
        }

        // Driver program to test above functions
        public void addEgde(int source, int destination, double weight) {
            Edge edge = new Edge(source, destination, weight);
            adjacencylist[source].addFirst(edge); //for directed graph
        }

        public void getWeight(int i, int j) {
            LinkedList<Edge> list = adjacencylist[i];
            if (j < list.size()) {
                System.out.println("weight of "+ i+" , "+j+" is: "+ list.get(j).weight);
            } else {
                System.out.println("Notice that: list.size is: " + list.size());
            }
        }

        public void printGraph() {
            for (int i = 0; i < vertices; i++) {
                LinkedList<Edge> list = adjacencylist[i];
                for (int j = 0; j < list.size(); j++) {
                        System.out.println("vertex-" + i + " is connected to " +
                                list.get(j).destination + " with weight " + list.get(j).weight);

                }
            }
        }

    }

    public static Graph Graphdata(ArrayList<String> array) {
        int vertices = array.size();
        Graph graph = new Graph(vertices);
        for (int i = 0; i < vertices; i++) {
            //LinkedList<Edge> list = graph.adjacencylist[i];
            //System.out.println(list.size());
            for (int j = 0; j < vertices; j++) {
                if (j == i)
                    continue;
                else {
                    // if (SimFunction.Jaccardsim(array.get(i), array.get(j))!=0)
                    graph.addEgde(i, j, SimFunction.Jaccardsim(array.get(i), array.get(j)));
                }
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        String fileName2= "/Users/binbingu/Documents/Datasets/DySM/source2.txt";
        ArrayList<String> source2  = SaveToArray.SaveArray(fileName2);
        Graph graph = Graphdata(source2);
        graph.printGraph();
        graph.getWeight(0, 0);

    }


}
