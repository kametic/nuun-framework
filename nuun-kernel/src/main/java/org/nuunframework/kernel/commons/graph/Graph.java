/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuunframework.kernel.commons.graph;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph {

	Logger logger = LoggerFactory.getLogger(Graph.class);
	
	private final int MAX_VERTS;

	private Vertex vertexList[]; // list of vertices

	private int matrix[][]; // adjacency matrix

	private int numVerts; // current number of vertices

	private char sortedArray[];

	public Graph(int maxvert) {
		MAX_VERTS =maxvert;
		vertexList = new Vertex[MAX_VERTS];
		matrix = new int[MAX_VERTS][MAX_VERTS];
		numVerts = 0;
		for (int i = 0; i < MAX_VERTS; i++)
			for (int k = 0; k < MAX_VERTS; k++)
				matrix[i][k] = 0;
		sortedArray = new char[MAX_VERTS]; // sorted vert labels
	}

	public int addVertex(char lab) {
		vertexList[numVerts] = new Vertex(lab);
		return numVerts++;
	}

	/**
	 * 
	 * end depends on start
	 * 
	 * @param start dependee
	 * @param end dependent
	 */
	public void addEdge(int start, int end) {
		matrix[start][end] = 1;
	}

	public void displayVertex(int v) {
		System.out.print(vertexList[v].label);
	}

	public char[] topologicalSort() // Topological sort
	{
		int orig_nVerts = numVerts;

		while (numVerts > 0) // while vertices remain,
		{
			// get a vertex with no successors, or -1
			int currentVertex = noSuccessors();
			if (currentVertex == -1) // must be a cycle
			{
				System.out.println("ERROR: Graph has cycles");
				return null;
			}
			// insert vertex label in sorted array (start at end)
			sortedArray[numVerts - 1] = vertexList[currentVertex].label;

			deleteVertex(currentVertex); // delete vertex
		}

		// vertices all gone; display sortedArray
//		logger.debug("Topologically sorted order: ");
//		for (int j = 0; j < orig_nVerts; j++)
//			logger.debug("" + sortedArray[j]);
//		System.out.println("");
		return sortedArray;
	}

	public int noSuccessors() // returns vert with no successors (or -1 if no
								// such verts)
	{
		boolean isEdge; // edge from row to column in adjMat

		for (int row = 0; row < numVerts; row++) {
			isEdge = false; // check edges
			for (int col = 0; col < numVerts; col++) {
				if (matrix[row][col] > 0) // if edge to another,
				{
					isEdge = true;
					break; // this vertex has a successor try another
				}
			}
			if (!isEdge) // if no edges, has no successors
				return row;
		}
		return -1; // no
	}

	public void deleteVertex(int delVert) {
		if (delVert != numVerts - 1) // if not last vertex, delete from
										// vertexList
		{
			for (int j = delVert; j < numVerts - 1; j++)
				vertexList[j] = vertexList[j + 1];

			for (int row = delVert; row < numVerts - 1; row++)
				moveRowUp(row, numVerts);

			for (int col = delVert; col < numVerts - 1; col++)
				moveColLeft(col, numVerts - 1);
		}
		numVerts--; // one less vertex
	}

	private void moveRowUp(int row, int length) {
		for (int col = 0; col < length; col++)
			matrix[row][col] = matrix[row + 1][col];
	}

	private void moveColLeft(int col, int length) {
		for (int row = 0; row < length; row++)
			matrix[row][col] = matrix[row][col + 1];
	}

	public static void main(String[] args) {
		Graph g = new Graph(20);
		
		g.addVertex('A'); // 0
		g.addVertex('B'); // 1
		g.addVertex('C'); // 2
		g.addVertex('D'); // 3
		g.addVertex('E'); // 4
		g.addVertex('F'); // 5
		g.addVertex('G'); // 6
		g.addVertex('H'); // 7

		g.addEdge(0, 3); // AD - D depends de A 
		g.addEdge(0, 4); // AE - E depends de A
		g.addEdge(1, 4); // BE - E depends de B
		g.addEdge(2, 5); // CF - F depends de C
		g.addEdge(3, 6); // DG - G depends de D
		g.addEdge(4, 6); // EG - G depends de E
		g.addEdge(5, 7); // FH - H depends de F
		g.addEdge(6, 7); // GH - H depends de G

		g.topologicalSort(); // do the sort
	}
}