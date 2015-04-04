package decisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class DecisionTree 
{    
    String line = "";
    public ArrayList<String[]> values = new ArrayList<String[]>();
    ArrayList<Integer> visitedAttr = new ArrayList<Integer>();
    int numOfAttr = 0;
    int numOfCols = 0;
public static void main(String[] args)
{
    
DecisionTree dt = new DecisionTree();

dt.readCSV();
//dt.printData();
//int initRoot = dt.getInitialRoot(dt.values);
//System.out.println("Root attribute = " + initRoot);
Node node = new Node();
node.getNextNode();

dt.numOfCols = dt.values.size();
}

public ArrayList<String[]> readCSV()
{
 try
 {
     BufferedReader reader = new BufferedReader(new FileReader("C:/Users/sudhakar553/workspace/DecisionTree/decisionTree/zoo-train.csv"));
     while ((line = reader.readLine()) != null){
     values.add(line.split(","));        
     }
 }
    
 catch(FileNotFoundException e){
 e.printStackTrace();
 }
 
 catch(IOException e)
 {
 e.printStackTrace();
 }
 numOfCols=values.size();
 return values;
}

public void printData()
{
for(String[] s : values)
{
//Get the num. of attributes in the Data Set
//numOfAttr = s.length-1; 
   for(int i=0;i<s.length;i++)
   {
       System.out.print(s[i]);
   }
   System.out.println();
}
System.out.println(numOfAttr);
}

public double calculateEntropy(List<Integer> valueS, ArrayList<String[]> data)
{
	int numOfAttr = data.get(0).length;
    HashMap<String,Integer> countMap = new HashMap<String,Integer>();
    DecisionTree dt = new DecisionTree();
    double ent=0;
    //System.out.println("Size of subset = " + values.size());
    //data = readCSV();
    //System.out.println("data.size ="+data.size());
    for(int i=0;i<data.size();i++)
    {
        //System.out.println("for loop above values.get(i) condition");
        //System.out.println("String s: values = " + s);
      if(valueS.contains(i))
      {
         // System.out.println("values.contains(i)" + i);
          String s[] = values.get(i);
          //System.out.println("s[numofattr]"+s[numOfAttr]);
                if (countMap.get(s[numOfAttr-1]) != null) {
                    int count = countMap.get(s[numOfAttr-1]);
                    count++;
                    countMap.put(s[numOfAttr-1], count);
                } else {
                    int count = 1;
                    countMap.put(s[numOfAttr-1], count);
                }
      }
    }
    
    double numerator=0;
    
    Iterator it = countMap.entrySet().iterator();
    while(it.hasNext())
    {
        Map.Entry pairs = (Map.Entry)it.next();
        numerator =(Integer) pairs.getValue();
        //System.out.println("num = " + numerator);
        double denominator = (Integer)values.size();
        //System.out.println("den = " + denominator);
        double probability = (Double)(numerator/denominator);
        //System.out.println("prob = " + probability);
        double product = -(probability * (Math.log(probability) / Math.log(2)));
        ent = ent + product;
    }
    //System.out.println("ent = " + ent);
    //System.out.println("dt.values.size = "+dt.numOfCols);
    //System.out.println("numOfCols =" + numOfCols);
    
        return ent;
}

  public HashMap<Integer,HashMap<String,ArrayList<Integer>>> getInitialRoot(ArrayList<Integer> indices)
  {
	ArrayList<String[]> data = new ArrayList<String[]>();	
    //System.out.println("num of attrs = "+numOfAttr);
    ArrayList<Integer> fullValues = new ArrayList<Integer>();
    ArrayList<Double> infoGainVals = new ArrayList<Double>();
    HashMap<Integer,HashMap<String, ArrayList<Integer>>> indeces = new HashMap<Integer,HashMap<String,ArrayList<Integer>>>(); 
    for(int i=0;i<indices.size();i++)
    {
    	if(indices.contains(i))
    	{
           data.add(values.get(i));    		
    	}
        fullValues.add(i);
        //System.out.println("fullValues =" + s[numOfAttr-1]);
    }
    int numOfAttr = data.get(0).length;
    double ent = calculateEntropy(fullValues,data);
    
    //System.out.println("ent= "+ent);
    double rhs = 0;
    for(int i=0;i<numOfAttr-1;i++)
    {
        double entSv=0;
        //System.out.println("For Attribute " + i);
        HashMap<String,ArrayList<Integer>> stringMap = new HashMap<String,ArrayList<Integer>>();
        //ArrayList<String[]> originalData = new ArrayList<String[]>();
        //originalData = readCSV();
        for(int j=0; j<data.size();j++)
        {
            String[] s = data.get(j);
            if(stringMap.get(s[i]) != null)
            {
                ArrayList<Integer> subValues = stringMap.get(s[i]);
                subValues.add(j);
                stringMap.put(s[i], subValues);
            }
            else
            {
                ArrayList<Integer> subValues = new ArrayList<Integer>();
                subValues.add(j);
                stringMap.put(s[i], subValues);
            }
        }
        //System.out.println("entS = " + ent);
        double entR=0;
        indeces.put(i,stringMap);
        Iterator it = stringMap.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pairs = (Map.Entry)it.next();
            ArrayList<Integer> subValues = (ArrayList<Integer>)pairs.getValue();
            //System.out.println("size of subValues from getInitRoot =" + subValues.size());
            entSv = calculateEntropy(subValues,data);
           // System.out.println("entSv =" + entSv);
            double numerator = (Integer)subValues.size();
           // System.out.println("subValues.size() = "+ subValues.size());
            double denominator = (Integer)fullValues.size();
           // System.out.println("fullValues.size() =" + fullValues.size());
            double lhs = (numerator/denominator);
            //System.out.println("lhs =" +lhs);
            double prod = lhs * entSv;
            //System.out.println("prod =" + prod);
            //System.out.println("rhs =" +rhs);
            entR = entR + prod;
        }
        //System.out.println(rhs);
        double infoGain = ent - entR;
        infoGainVals.add(infoGain);
    }
   
   /* for(Double d: infoGainVals)
    {
        System.out.println(d);
    }*/
    
    int maxindex = infoGainVals.indexOf(Collections.max(infoGainVals));
    System.out.println("next node =" + maxindex);
    return indeces;
    //TreeNode tn = new TreeNode(maxindex, indeces );
    
   /* HashMap <String,ArrayList<Integer>> stringMap = indeces.get(maxindex);
    Iterator posVals = stringMap.entrySet().iterator();*/
   /* if(ent <= 0)
    {
    	System.out.println("class lable =" + data.get(numOfAttr));
    }
    else
    {
      while(posVals.hasNext())
      {
    	Map.Entry pairs = (Map.Entry)posVals.next();
        ArrayList<Integer> subValues = (ArrayList<Integer>)pairs.getValue();//get the indices 
    	ArrayList<String[]> newDataSet = new ArrayList<String[]>();
    	for(int k=0;k<numOfCols;k++)
    	{
    		if(subValues.contains(k))
    		{
    		   String[] row = data.get(k);
    		   newDataSet.add(row);
    	    }
        }
    	if(values.size() - newDataSet.size() > 0)
    	{
           getInitialRoot(newDataSet);
    	}
      }*/
    
    
 

  }
  
  /*public void buildTree(ArrayList<String[]> values, int depth)
  {
      int iterator = 0;
      int nextNodegetInitialRoot(values);
  }*/
  
}