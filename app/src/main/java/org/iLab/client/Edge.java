package org.iLab.client;

import java.util.ArrayList;
import java.util.List;

public class Edge {

	private Node startNode, endNode;
	private Edge currentEdge;
	private String edgeName;
	private float startX,startY,endX,endY;
	public static List<Edge> graphEdges = new ArrayList<Edge>();
	private int type;
	
	public Edge(Node fromNode, Node toNode, String edgeName, float [] coordinates, int edgeType)
	{
		setStartNode(fromNode);
		setEndNode(toNode);
		startX = coordinates[0];
		startY = coordinates[1];
		endX = coordinates[2];
		endY = coordinates[3];		
		//graphEdges.add(this);

		this.edgeName = edgeName;
		currentEdge = this;
		type = edgeType;
	}

	public String getName()
	{
		return edgeName;
	}

	public float getStartX()
	{
		return startX;
	}
	
	public float getStartY()
	{
		return startY;
	}
	
	public float getEndX()
	{
		return endX;
	}

	public float getEndY()
	{
		return endY;
	}
	
	public int getEdgeType()
	{
		return type;
}
	public static Edge getEdgefromCoordinate(float x, float y)
	{
		Edge resultEdge = null;
		for (int i =0;i<graphEdges.size();i++)
		{
			Edge tempEdge= graphEdges.get(graphEdges.size()-i-1);
			if(x>=tempEdge.startX && x<=tempEdge.endX)
				if(y>=tempEdge.startY && y<=tempEdge.endY)
				{
					resultEdge = tempEdge;
					break;
				}
		}
		
		return resultEdge;
	}
	

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Node getEndNode() {
		return endNode;
	}
}
