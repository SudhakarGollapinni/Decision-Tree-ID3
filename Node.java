package decisionTree;

import java.util.*;

public class Node {
	
	DecisionTree dt = new DecisionTree();
	ArrayList<String[]> dataset = dt.readCSV();
	int numOfAttr = dataset.get(0).length;
	int attribute;
	HashMap<String,ArrayList<Integer>> possibleVals = new HashMap<String,ArrayList<Integer>>();
	HashMap<Integer,Node> childNodes = new HashMap<Integer,Node>();
	String label;
	
	Node()
	{
		
	}
	
	Node(int attr, HashMap<String,ArrayList<Integer>> posVals)
	{
		attribute = attr;
		possibleVals = posVals;		
	}
	
	Node(String label)
	{
		label = label;
	}
	
	public void getNextNode()
	{
		ArrayList<String[]> dataset = new ArrayList<String[]>();
		DecisionTree dt = new DecisionTree();
		dataset = dt.readCSV();
		ArrayList<Integer> indexArray = new ArrayList<Integer>();
		for(int i=0; i<dataset.size();i++)
		{
			indexArray.add(i);
		}
		HashMap<Integer,HashMap<String, ArrayList<Integer>>> index = dt.getInitialRoot(indexArray);
		buildTree(buildNode(index));
	}

	public Node buildNode(HashMap<Integer,HashMap<String, ArrayList<Integer>>> attr)
	{
		int att =0;
		HashMap<String, ArrayList<Integer>> posvals = new HashMap<String, ArrayList<Integer>>();
		
		DecisionTree dt = new DecisionTree();
		Iterator it = attr.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry pairs = (Map.Entry)it.next();
			att = (Integer)pairs.getKey();
			posvals = (HashMap<String, ArrayList<Integer>>)pairs.getValue();
		}
		Node node = new Node(att,posvals);
		return node;
	}
	
	public void buildTree(Node node)
	{
	   
	   DecisionTree dt = new DecisionTree();
	   Iterator it = node.possibleVals.entrySet().iterator();
	   while(it.hasNext())
	   {
		   Map.Entry pairs = (Map.Entry)it.next();
		   if(dt.calculateEntropy((ArrayList<Integer>)pairs.getValue(), dataset) > 0)
		   {
              buildNode(dt.getInitialRoot((ArrayList<Integer>)pairs.getValue()));
		      childNodes.put(node.attribute,buildNode(dt.getInitialRoot((ArrayList<Integer>)pairs.getValue())));
		   }
		   else
		   {
			   ArrayList<Integer> al = (ArrayList<Integer>)pairs.getValue();
			   String[] row = dataset.get(al.get(0));
			   String label = row[numOfAttr];
			   Node leaf = new Node(label);
			   childNodes.put(node.attribute,leaf);
		   }
	   }   
	}
}
