package csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import entity.Node;

import entity.Graph;

public class GraphExporter {

    public Map<String,Object> readFromCSV(String filePath1, String filePath2) throws IOException {
		Map<String,Object> graphInfoMap = readGraphNodes(filePath2);
        int[][] graph = null;
		if (graphInfoMap != null) {
			graph = readGraphVertices(filePath1, (Integer)graphInfoMap.get("numberOfVertices"));
            graphInfoMap.put("graph", graph);
		}
		return graphInfoMap;
	}

	// Function to read graph from CSV file
	private Map<String,Object> readGraphNodes(String filePath) throws IOException {
        Map<String,Object> map = new HashMap<>();
        Node[] nodeArray = null;
        int index = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String readChars[] = line.split(",");
				if (nodeArray == null) {
					int numberOfVertices = Integer.parseInt(readChars[0]);
					int source = Integer.parseInt(readChars[1]);
					int sink = Integer.parseInt(readChars[2]);
					int upperCapacity = Integer.parseInt(readChars[3]);
					map.put("numberOfVertices", numberOfVertices);
                    map.put("source", source);
                    map.put("sink", sink);
                    map.put("upperCapacity", upperCapacity);
                    nodeArray = new Node[numberOfVertices];

				} else {
					double x = Double.parseDouble(readChars[0]);
					double y = Double.parseDouble(readChars[1]);
					Node node = new Node(x, y);
					nodeArray[index++] = node;
				}
			}
            map.put("nodesArray", nodeArray);
		}
		return map;
	}

	private int[][] readGraphVertices(String filePath, int numberOfVertices) throws IOException {
        int[][] graph = new int[numberOfVertices][numberOfVertices];
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String readChars[] = line.split(",");
				int u = Integer.parseInt(readChars[0]);
				int v = Integer.parseInt(readChars[1]);
				int capcity = Integer.parseInt(readChars[2]);
                graph[u][v] = capcity;
			}
		}

        return graph;
	}

    public void csvConverterForVertices(int[][] graph, String filePath) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			for (int i = 0; i < graph.length; i++) {
				for (int j = 0; j < graph.length; j++) {
                    if(graph[i][j] > 0){
                        bw.write(i + "," + j + ","+ (graph[i][j]));
					    bw.newLine();
                    }
					
				}
			}
		}
	}

	public void csvConverterForNodes(int[][] graph, Node[] nodeArray, int source, int sink, String filePath, int upperCapacity) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			Integer vertices = graph.length;
			bw.write(vertices + "," + source + "," + sink + "," + upperCapacity);
			bw.newLine();
			for (Node vertex:nodeArray) {
				bw.write(vertex.toString());
				bw.newLine();
			}
		}
	}
}
