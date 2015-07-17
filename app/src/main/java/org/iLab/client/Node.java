package org.iLab.client;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private String nodeName;
	private List <Node> children;
	private Node currentNode;
	private static List <Node> graphVertices = new ArrayList <Node>();
	
	public Node(String name){
		
		nodeName = name;
		currentNode = this;
		graphVertices.add(this);
		children = new ArrayList <Node>();
		children.add(this);
	}
	
	public String getName()
	{
		return nodeName;
	}
	
	public static List <Node> getGraphVertices()
	{
		return graphVertices;
	}
	
	public List <Node> getChildren()
	{
		return children;
	}
	public boolean containsNode(Node child)
	{
		boolean result = false;
		for (Node node:children)
		{
			if(node==children)
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	protected void removeNode(Node child)
	{
		children.remove(child);
	}
	
	public void addEdge(Node child, String edgeName, float [] edgeCoordinates )
	{
		//Edge edge = new Edge(currentNode,child, edgeName);
		Edge.graphEdges.add(new Edge(currentNode,child,edgeName,edgeCoordinates,0));
		children.add(child);
	}
	
	
}
