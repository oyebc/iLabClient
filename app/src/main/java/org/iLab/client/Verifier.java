package org.iLab.client;

import java.util.ArrayList;
import java.util.List;

public class Verifier {

	
//	public static Node[] resistorNodeArray;
	
	public static ArrayList<Node> resistorNodeArray;

	protected static boolean getConnection(Node startNode, Node endNode)
	   {
	  	 Boolean isConnected = false;
	  	 Node start = startNode;
	  	 Node end = endNode;
	  	 List <Node> openList = new ArrayList<Node>();
	  	 List <Node> forbiddenList = new ArrayList<Node>();
	  	 
	  	 openList.add(start);
	  	 
	  	 while(openList.size()!=0 )
	  	 {
	  		 
	  		 Node n = openList.remove(0);
	  		 
	  		 forbiddenList.add(n);
	  		 
	  		 
	  		 List <Node> neighbors = n.getChildren();
	  		 int neighborLength = neighbors.size();
	  		 
	  		 
	  		 if(n.equals(end))
	  		 {
	  			 isConnected = true;
	  			 break;
	  		 }
	  		 
	  		 //Add neighbors to forbidden stack
	  		 for(int j=0;j<neighborLength;j++)
				 {
					 if(!forbiddenList.contains(neighbors.get(neighborLength-j-1)))
						 openList.add(0,neighbors.get(neighborLength-j-1));
				 }
	  		 
	  	 }
	  	 
	  	 return isConnected;
	   }

	protected static int getConnectionCount(Node startNode, Node endNode)
	   {
	  	 int isConnected = 0;
	  	 Node start = startNode;
	  	 Node end = endNode;
	  	 List <Node> openList = new ArrayList<Node>();
	  	 List <Node> forbiddenList = new ArrayList<Node>();
	  	 
	  	 openList.add(start);
	  	 
	  	 while(openList.size()!=0 )
	  	 {
	  		 
	  		 Node n = openList.remove(0);
	  		 
	  		 forbiddenList.add(n);
	  		 
	  		 
	  		 List <Node> neighbors = n.getChildren();
	  		 int neighborLength = neighbors.size();
	  		 
	  		 
	  		 if(n.equals(end))
	  		 {
	  			 isConnected = 1;
	  			 break;
	  		 }
	  		 
	  		 //Add neighbors to forbidden stack
	  		 for(int j=0;j<neighborLength;j++)
				 {
					 if(!forbiddenList.contains(neighbors.get(neighborLength-j-1)))
						 openList.add(0,neighbors.get(neighborLength-j-1));
				 }
	  		 
	  	 }
	  	 
	  	 return isConnected;
	   }
	protected static int [] getConnectionAndResistorCount(Node startNode, Node endNode)
	   {
	  	 int isConnected = 0;
	  	 int resistorCount=-1;
	  	 Node start = startNode;
	  	 Node end = endNode;
	  	 List <Node> openList = new ArrayList<Node>();
	  	 List <Node> forbiddenList = new ArrayList<Node>();
	  	 
	  	 openList.add(start);
	  	 
	  	 while(openList.size()!=0 )
	  	 {
	  		 
	  		 Node n = openList.remove(0);
	  		 
	  		 forbiddenList.add(n);
	  		 
	  		 if (resistorNodeArray.contains(n))
	  			 resistorCount++;
	  		 
	  		 List <Node> neighbors = n.getChildren();
	  		// int neighborLength = neighbors.size();
	  		 
	  		 
	  		 if(n.equals(end))
	  		 {
	  			 isConnected = 1;
	  			 break;
	  		 }
	  		 
	  		 //Add neighbors to forbidden stack
	  		 for(int j=0;j<neighbors.size();j++)
				 {
					 if(!forbiddenList.contains(neighbors.get(neighbors.size()-j-1)))
						 openList.add(0,neighbors.get(neighbors.size()-j-1));
				 }
	  		 
	  	 }
	  	 
	  	 return new int []{isConnected,resistorCount};
	   }
	
	
}
