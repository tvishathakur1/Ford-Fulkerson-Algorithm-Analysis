package main;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import entity.*;
import csv.GraphExporter;

public class FordFulkerson {
	
	private static final String FILE_PATH_1 = "src/files/generated_graph1.csv";
    private static final String FILE_PATH_2 = "src/files/generated_graph2.csv";
	
    public static void main(String args[]) throws IOException {
        int n = 100;
        double r = 0.2;
        int givenUpperCapacity = 2;

        Node[] nodeArray = new Node[n];
        int[][] randomisedGraph = generateGraph(n, r, givenUpperCapacity, nodeArray);

        //Trial values
        // int[][] graph = new int[4][4];
        // graph[0][2] = 3;
        // graph[0][3] = 5;
        // graph[1][0] = 6;
        // graph[3][1] = 34;
        // graph[3][2] = 9;
        // graph[2][1] = 3;
        // int source = 3;
        // int n = 4;

        // int[][] graph007 = new int[5][5];
        // graph007[0][1] = 1;
        // graph007[0][2] = 2;
        // graph007[0][3] = 4;
        // graph007[1][4] = 1;
        // graph007[2][4] = 13;
        // graph007[3][4] = 5;
        // int source007 = 0;
        // int n007 = 5;

        // int[][] graph = new int[5][5];
        // graph[0][4] = 11;
        // graph[1][0] = 13;
        // graph[2][0] = 14;
        // graph[2][1] = 3;
        // graph[3][0] = 17;
        // graph[3][2] = 10;
        // graph[4][2] = 12;
        // int source = 4;
        // int n = 5;

        // int[][] graph = new int[6][6];
        // graph[0][1] = 11;
        // graph[0][2] = 13;
        // graph[2][4] = 14;
        // graph[3][5] = 3;
        // graph[4][1] = 17;
        // graph[4][5] = 10;
        // graph[1][3] = 12;
        // int source = 0;
        // int n = 6;

        //Trial values

        int generatedSource = new Random().nextInt(n);

        // Apply BFS to find the longest acyclic path and define the end node as the sink
        List<Integer> result = longestAcyclicPath(randomisedGraph, generatedSource);

        int longestPathLength = result.size()-1;
        int generatedSink = result.get(result.size()-1);

        //Printing graph nodes
        for(Node vertexCoordinates:nodeArray){
            System.out.println("Nodes are: ");
            System.out.println(vertexCoordinates.x+" "+vertexCoordinates.y);
        }

        //Write graph into CSV
        GraphExporter graphExporter = new GraphExporter();
        try {
            graphExporter.csvConverterForVertices(randomisedGraph, FILE_PATH_1);
            graphExporter.csvConverterForNodes(randomisedGraph, nodeArray, generatedSource, generatedSink, FILE_PATH_2, givenUpperCapacity);
        } catch (IOException e) {
            
            e.printStackTrace();
        }

        //Read from CSV
        Map<String,Object> graphInforMap = graphExporter.readFromCSV(FILE_PATH_1, FILE_PATH_2);

        //Fetching values from the graphInfoMap
        int[][] graph = (int[][])graphInforMap.get("graph");
        int source = (Integer)graphInforMap.get("source");
        int sink = (Integer)graphInforMap.get("sink");
        int upperCapacity = (Integer)graphInforMap.get("upperCapacity");

        //Calculating number of edges
        int totalEdges = 0;
        for(int i=0;i<graph.length;i++){
            for(int j=0;j<graph.length;j++){
                if(graph[i][j] > 0){
                    totalEdges++;
                }
            }
        }

        // Display the generated graph
        System.out.println("\n Graph read from CSV:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(graph[i][j] + "\t");
            }
            System.out.println();
        }

        // Ford Fulkerson

        int SAPMaxFlow = fordFulkerson(graph, source, sink, "SAP", longestPathLength);
        int dfsLikeMaxFlow = fordFulkerson(graph, source, sink, "dfsLike", longestPathLength);
        int maxCapacityMaxFlow = fordFulkerson(graph, source, sink, "maxCapacity", longestPathLength);
        int randomDfsLikeMaxFlow = fordFulkerson(graph, source, sink, "randomDfsLike", longestPathLength);

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Total number of edges are: "+totalEdges);

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("The Maximum flows from all the algorithms is:");
        System.out.println(SAPMaxFlow + " " + dfsLikeMaxFlow + " " + maxCapacityMaxFlow + " " + randomDfsLikeMaxFlow);

    }

    private static int[][] generateGraph(int n, double r, int upperCapacity, Node[] nodes){
        int[][] graph = new int[n][n];

        // Assign random (x, y) coordinates to each node
        // Node[] nodes = new Node[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(random.nextDouble(), random.nextDouble());
        }

        // Randomly add edges within distance r
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && distance(nodes[i], nodes[j]) <= r && !edgeExists(graph, i, j)) {
                    if (random.nextDouble() < 0.5) {
                        graph[i][j] = random.nextInt(upperCapacity) + 1;
                    } else {
                        graph[j][i] = random.nextInt(upperCapacity) + 1;
                    }
                }
            }
        }

        return graph;
    }

    private static List<Integer> longestAcyclicPath(int[][] graph, int source) {
        int n = graph.length;
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] distance = new int[n];

        Arrays.fill(parent, -1);

        queue.add(source);
        visited[source] = true;
        distance[source] = 0;

        int lastVisited = source; // To track the last visited node during BFS

        while (!queue.isEmpty()) {
            int u = queue.poll();
            visited[u] = true;
            for (int v = 0; v < n; v++) {
                if (graph[u][v] != 0) { // Edge exists
                    if (!visited[v]) {
                        
                        queue.add(v);
                        if(parent[v]==-1){
                            parent[v] = u;
                            distance[v] = distance[u]+1;
                            lastVisited = v;
                        }else{
                            if(distance[v] < distance[u]+1){
                                parent[v] = u;
                                distance[v] = distance[u]+1;
                                lastVisited = v;
                            }
                        }
                        
                        
                    }
                }
            }
        }

        // Reconstruct the path
        List<Integer> path = new ArrayList<>();
        int current = lastVisited;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }

        Collections.reverse(path);
        return path;
    }

    public static int fordFulkerson(int[][] graph, int source, int sink, String algorithm, int longestPathLength) {

        //METRICS
        int numberOfAugmentingPaths = 0;
        int totalEdgesOfAugmentingPaths = 0;

        int n = graph.length;
        int[][] residualGraph = new int[n][n];

        // Initialize residual graph with the same capacities as the original graph
        for (int i = 0; i < n; i++) {
            System.arraycopy(graph[i], 0, residualGraph[i], 0, n);
        }

        int maxFlow = 0;

        while (true) {

            List<Integer> augmentingPath = null;

            switch (algorithm) {
                case "SAP":
                    augmentingPath = SAP(residualGraph, source, sink);
                    break;
                
                case "dfsLike":
                    augmentingPath = dfsLike(residualGraph, source, sink);
                    break;
                
                case "maxCapacity":
                    augmentingPath = maxCapacity(residualGraph, source, sink);
                    break;
                
                case "randomDfsLike":
                    augmentingPath = randomDfsLike(residualGraph, source, sink);
                    break;
                
                default:
                    break;
            }

            // If no augmenting path is found, break the loop
            if (augmentingPath==null || augmentingPath.isEmpty() || augmentingPath.size()==1) {
            	float meanLength = totalEdgesOfAugmentingPaths / numberOfAugmentingPaths;
            	float mpl = (float)meanLength / longestPathLength;
                System.out.println("--------------------------------------------------------------------------------");
                System.out.println("Number of augmenting paths for "+algorithm+" is "+numberOfAugmentingPaths);
                System.out.println("Mean length for "+algorithm+" is "+meanLength);
                System.out.println("Mean proportional length for "+algorithm+" is "+((float)meanLength / longestPathLength));
                break;
            }

            numberOfAugmentingPaths++;
            totalEdgesOfAugmentingPaths+=augmentingPath.size()-1;

            // Find the minimum capacity along the augmenting path
            int minCapacity = Integer.MAX_VALUE;
            for (int i = 0; i < augmentingPath.size() - 1; i++) {
                int u = augmentingPath.get(i);
                int v = augmentingPath.get(i + 1);
                minCapacity = Math.min(minCapacity, residualGraph[u][v]);
            }


            // Update the residual graph
            for (int i = 0; i < augmentingPath.size() - 1; i++) {
                int u = augmentingPath.get(i);
                int v = augmentingPath.get(i + 1);
                residualGraph[u][v] -= minCapacity;
                residualGraph[v][u] += minCapacity;
            }

            // Update the maximum flow
            maxFlow += minCapacity;
            
        }

        return maxFlow;
    }

    //ALGO: 1 Shortest paths between source and sink
    private static List<Integer> SAP(int[][] graph, int source, int sink)
    {
        PriorityQueue<DistancePair> pq = new PriorityQueue<DistancePair>((x,y) -> x.distance - y.distance);

        Integer []dist = new Integer[graph.length];
        int[] parent = new int[graph.length]; 

        for(int i = 0;i<graph.length;i++){
            dist[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        
        // Source initialised with dist=0.
        dist[source] = 0;
        pq.add(new DistancePair(source,0));

        while(pq.size() != 0) {
            int dis = pq.peek().distance; 
            int u = pq.peek().node;
            pq.remove(); 
            
            for(int v = 0;v<graph.length;v++) {
                if (graph[u][v] > 0){
                    if(dis + 1 < dist[v]) {
                        dist[v] = dis + 1; 
                        parent[v] = u;
                        pq.add(new DistancePair(v, dist[v])); 
                    }
                }
                
            }
        }

        // Reconstruct the path from source to sink
        ArrayList<Integer> path = new ArrayList<>();
        int current = sink;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }
        Collections.reverse(path);

        return path;
    }

    //ALGO:2
    public static ArrayList<Integer> dfsLike(int[][] graph, int source, int sink){
        int counter = 100;
        PriorityQueue<DistancePair> pq = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));

        int[] key = new int[graph.length];
        int[] parent = new int[graph.length];
        boolean[] visited = new boolean[graph.length];

        for (int i = 0; i < graph.length; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        key[source] = 0;
        
        pq.add(new DistancePair(source, Integer.MAX_VALUE));

        while (!pq.isEmpty()) {
            int u = pq.poll().node;

            // Skip if already visited
            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] > 0) {
                    if(key[v]==Integer.MAX_VALUE){
                        counter--;
                        key[v] = counter;
                        pq.add(new DistancePair(v, counter));
                        parent[v] = u;
                    }
                }
            }

            
        }

        // Reconstruct the augmenting path from source to sink
        ArrayList<Integer> augmentingPath = new ArrayList<>();
        int current = sink;
        while (current != -1) {
            augmentingPath.add(current);
            current = parent[current];
        }
        Collections.reverse(augmentingPath);

        return augmentingPath;


    }

    //ALGO:3
    public static ArrayList<Integer> maxCapacity(int[][] graph, int source, int sink) {
        PriorityQueue<CapacityPair> pq = new PriorityQueue<>((x, y) -> y.capacity - x.capacity);

        int[] capacity = new int[graph.length];
        int[] parent = new int[graph.length];
        boolean[] visited = new boolean[graph.length];

        for (int i = 0; i < graph.length; i++) {
            capacity[i] = Integer.MIN_VALUE;
            parent[i] = -1;
        }

        // Source initialized with capacity=Infinity.
        capacity[source] = Integer.MAX_VALUE;
        parent[source] = -1;
        pq.add(new CapacityPair(source, Integer.MAX_VALUE));

        while (!pq.isEmpty()) {
            int u = pq.poll().node;

            // Skip if already visited
            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] > 0) {
                    int edgeCapacity = graph[u][v];
                    int minCapacity = Math.min(capacity[u], edgeCapacity);
                    if (minCapacity > capacity[v]) {
                        capacity[v] = minCapacity;
                        parent[v] = u;
                        pq.add(new CapacityPair(v, minCapacity));
                    }
                }
            }
        }

        // Reconstruct the augmenting path from source to sink
        ArrayList<Integer> augmentingPath = new ArrayList<>();
        int current = sink;
        while (current != -1) {
            augmentingPath.add(current);
            current = parent[current];
        }
        Collections.reverse(augmentingPath);

        return augmentingPath;
    }

    //ALGO:4
    public static ArrayList<Integer> randomDfsLike(int[][] graph, int source, int sink){
        PriorityQueue<DistancePair> pq = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));

        int[] key = new int[graph.length];
        int[] parent = new int[graph.length];
        boolean[] visited = new boolean[graph.length];

        for (int i = 0; i < graph.length; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        key[source] = 0;
        parent[source] = -1;
        pq.add(new DistancePair(source, Integer.MAX_VALUE));

        while (!pq.isEmpty()) {
            int u = pq.poll().node;

            // Skip if already visited
            if (visited[u]) {
                continue;
            }
            visited[u] = true;

            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] > 0) {
                    if(key[v]==Integer.MAX_VALUE){
                        Random rand = new Random();
                        int randomKey = rand.nextInt(graph.length * 2);
                        key[v] = randomKey;
                        pq.add(new DistancePair(v, randomKey));
                        parent[v] = u;
                    }
                }
            }

            
        }

        // Reconstruct the augmenting path from source to sink
        ArrayList<Integer> augmentingPath = new ArrayList<>();
        int current = sink;
        while (current != -1) {
            augmentingPath.add(current);
            current = parent[current];
        }
        Collections.reverse(augmentingPath);

        return augmentingPath;


    }
    
    private static double distance(Node u, Node v) {
        return Math.sqrt(Math.pow(u.x - v.x, 2) + Math.pow(u.y - v.y, 2));
    }

    private static boolean edgeExists(int[][] graph, int u, int v) {
        return graph[u][v] != 0 || graph[v][u] != 0;
    }
}