/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jae_S
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// The program implements adjacency matrix representation of the graph 

class SPath { 
    
    int vertices; 
    static double adjacencyMatrix[][];
    private static final int NO_PARENT = -1;
    static List<Double> edgeList = new ArrayList<Double>();
    private static DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) 
    {
        /*create instances of required objects*/
        List<Edge> eList = new ArrayList<Edge>();
        Scanner scan = new Scanner(System.in);
        
        try{
            /*File path and empty string variable to store each line of the file into below*/
            File file = new File("../GraphDistances.csv");
            String path = "GraphDistances.csv";
            String line = "";
            
            /*Read and store the csv file into an arrayList of Edge objects*/
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null){
                String[] values = line.split(",");
                    Edge edge = new Edge(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Double.parseDouble(values[2]));
                    eList.add(edge);        
            }
            
            /*Take properties of each edge in the list of Edges --> add it to adjacency Matrix*/
            SPath sp = new SPath (25);
            for(int i=0; i<eList.size();i++){
                int s = eList.get(i).getSource();       
                int d = eList.get(i).getDestination();
                double w = eList.get(i).getWeight();
                sp.addEdge(s,d,w);
            }
            
//              ***TESTING PURPOSE***print out your adjacency matrix to test that all is well*/
            print2D(adjacencyMatrix);

            boolean done=false;     //use this to loop if user enters invalid number
            int fNumber=0;  
            int sNumber=0;  
            while(!done){       
                try{
                    System.out.println("Please enter two positive integer numbers, each representing a vertex in our graph");
                    System.out.println("Enter your first number");
                    String fInput = scan.nextLine();
                    fNumber = Integer.parseInt(fInput);
                    if(fNumber<0){
                        throw new NumberOutOfRangeException();      //if number is negative, throw the custom exception
                    }
                    int [] vList = new int[25];     //create an array of size same as the graph
                    int i = vList[fNumber-1];       //try access the array just created to trigger exception if user enters invalid number
                    done=true;      //set it to true so that we don't loop if the number is okay
                    fNumber--;      //decrement the number as array used 0 indexing
                }catch(ArrayIndexOutOfBoundsException e){   //this will catch the error when user enters a vertex that does not exist
                    System.out.println("Your chosen Edge does not exist in the graph");
                }catch (NumberOutOfRangeException e) {  //trigger custom exception
                    processError(e);    //use the method created in this class to call the error message
                }
            }
            done=false;
            
            /*same logic below as the above loop to prompt user to input second number*/
            while(!done){
                try{
                    System.out.println("Enter your second number");
                    String sInput = scan.nextLine();
                    sNumber = Integer.parseInt(sInput);
                    if(sNumber<0){
                        throw new NumberOutOfRangeException();
                    }
                    int [] vList = new int[25];
                    int i = vList[sNumber-1];
                    done=true;
                    sNumber--;
                }catch(IndexOutOfBoundsException e){
                    System.out.println("Your chosen Edge does not exist in the graph");
                } catch (NumberOutOfRangeException e) {
                    processError(e);
                }
            }

            //TRIGGER THE DIJKSTRA METHOD WHICH WILL RETURN 
            //SUM/AVERAGE/STANDARD DEVIATION 
            //OF THE EDGES WITHIN THE SHORTEST PATH BETWEEN THE SOURCE AND DESTINATION VERTEX
            sp.dijkstra(adjacencyMatrix, fNumber, sNumber);


        }catch (FileNotFoundException e){       //CATCH ERROR IF FILE DOESN'T EXIST
            e.printStackTrace();
        }catch (IOException e){     //CATCH ANY INPUT/OUTPUT ERROR
            e.printStackTrace();
        }
    }    
    /******************************END OF MAIN METHOD**************************************/
    
    public SPath(int vertices) {
        this.vertices = vertices;
        adjacencyMatrix = new double[vertices][vertices];
        //initialize adjacency lists for all the vertices. set each space in matrix to empty double number
        for (int i = 0; i <vertices ; i++) {
            for (int j = 0; j <vertices ; j++) {
                adjacencyMatrix[i][j] = 0.0;
            }
        }
    }
    
    //This method returns the vertex in an array of vertices which has the minimum distance
    public int minDistance(double dist[], Boolean sptSet[]) 
    { 
        // Initialize min value to infinity
        double min = Double.MAX_VALUE;
        int min_index = -1; 

        for (int v = 0; v < vertices; v++) 
          
            if (sptSet[v] == false && dist[v] <= min) { 
                min = dist[v]; 
                min_index = v; 
            }
        return min_index; 
    } 

    

    
    // The important method that implements Dijkstra's single source shortest path 
    // accepts the adjacency matrix, source and destination as parameters
    public void dijkstra(double graph[][], int source, int destination) 
    { 
            // dist[i] will hold  the shortest distance from src to i 
            double dist[] = new double[vertices]; //this is our D[v],   

            // sptSet[i] ;aka cloud; will true if vertex i is included in shortest path tree or shortest distance from src to i is finalized 
            Boolean sptSet[] = new Boolean[vertices];

            // Initialize all distances as INFINITE and stpSet[] as false 
            for (int i = 0; i < vertices; i++) { 
                    dist[i] = Double.MAX_VALUE; 
                    sptSet[i] = false; 
            } 

            // Distance of source vertex from itself is always 0 
            dist[source] = 0.0; 
            
            //parent array to store parent node for each vertices
            int[] parents = new int[vertices];
            //The source vertex has no parent
            parents[source] = NO_PARENT;
            
            //In this arraylist, we'll hold edges that make up the fatest path between source/destination
            List<Double> edgeList = new ArrayList<Double>();
            
            // Find shortest path for all vertices 
            for (int count = 0; count < vertices - 1; count++) {
                
                // Pick the minimum distance vertex from the set of vertices not yet processed. 
                // u is always equal to source in first iteration.

                int u = minDistance(dist, sptSet);            
                // Mark the picked vertex as processed 
                sptSet[u] = true;
                
                // Update dist value of the adjacent vertices of the picked vertex. 
               
                for (int v = 0; v < vertices; v++) 
                {

                    // Update dist[v] if 
                    if (!sptSet[v] && //it is not in sptSet and
                        graph[u][v] != 0 && //there is an edge from u to v and
                        dist[u] != Double.MAX_VALUE && //
                        dist[u] + graph[u][v] < dist[v]) //total weight of path from source to v through u is smaller than current value of dist[v]
                    {
                        //formula --> D[z] = D[u] + weight(u, z) 
                        //This is the Relaxation Procedure of the dijkstrra algorithm
                        dist[v] = dist[u] + graph[u][v];
                        parents[v] = u;
                    }
                }
            } 
            //Follow the fastest path using the parents array as a guide.
            //Take the distance of vertex away from the distance of parent vertex
            //to find the distance of the edge in between the two vertices
            
            int v=destination, u=source, m=0;
            System.out.println("\n**The edges within the shortest path between your two chosen vertices are as follows**\n");
            while(v!=u){
                edgeList.add(dist[v]-dist[parents[v]]);     //store the edges of the shortest path in to an arrayList
                int vertex = v+1, parent = parents[v]+1;
                //print out each edge within the fastest path
                System.out.println("The distance between "+ vertex + " and " + parent + " is " + edgeList.get(m));
                v=parents[v];
                m++;
            }
            
//            **TESTING PURPOSE**
//              Print out the list of edges within the fastest path to check all is well
//              for(int i=0;i<edgeList.size();i++){
//                    System.out.println(edgeList.get(i));
//                }
//            
            double sum = 0.0, std = 0.0;
            
            //add each element in list to get sum
            for(Double edge : edgeList){
            sum += edge;
            }
            
            //import DecimalFormatter to format each result into decimal number
            sum = Double.valueOf(df.format(sum));
            
            //use mapToDouble to return in DoubleStream format and use the average function that comes with it
            Double average = edgeList.stream().mapToDouble(edge -> edge).average().orElse(0.0);
            average = Double.valueOf(df.format(average));
            
            //get stream of double from the edGeList and turn instances of double to primitive value and call toArray to convert
            double[] edgeArray = edgeList.stream().mapToDouble(Double::doubleValue).toArray();    
            std = Double.valueOf(df.format(calcSD(edgeArray, average)));    //use the std calc custom method created below
            
            System.out.println("\n***The sum of your edges within the shortest path are = \t" + sum);
            System.out.println("***The average of your edges within the shortest path are = \t" + average);
            System.out.println("***The std of your edges within the shortest path are = \t" + std);
            // print the constructed distance array 
            printSolution(dist, source, destination);
            printAllSolutions(dist, parents);
            
    }
    
    //Method to calculate standard deviation
    public static double calcSD(double[] sd, double mean){

        if (sd.length == 0) return 0; // we don't want to divide by 0 here
        double total = 0;
        for (int i = 0; i<sd.length; i++){
            total = total + (sd[i] - mean) * (sd[i] - mean);
        }
        double squaredDiffMean = (total) / (sd.length); 
        double standardDev = (Math.sqrt(squaredDiffMean)); 

        return standardDev; 
    }
    
    //Print the dijkstra table that shows shortest distance and parent node of each vertex
    public void printAllSolutions(double dist[], int[] parent) 
    { 
            System.out.println("\nVertex \t\t Distance from Source \t\t Parent Node"); 
            for (int i = 0; i < vertices; i++){
                if(parent[i]==NO_PARENT){
                    int j=0;
                }
                    int j = parent[i]+1;
                    System.out.println(i+1 + " \t\t " + dist[i] + "\t\t\t\t" + j);
            }
    } 

        // A method function to print the constructed distance array 
    public void printSolution(double dist[], int source, int destination) 
    {   
        int s = source+1;
        int d = destination+1;
        System.out.println("\nShortest Path from Vertex " + s + " to Vertex " + d); 
        System.out.println(dist[destination]);
    }
//  no longer required
//    public void printPath(int currentV, int[] parents){
//        if(currentV == NO_PARENT){
//            return;
//        }
////        printPath(parents[currentV], parents[]);
//        System.out.print(currentV + parents[currentV]);
//    }
    
//used to test and see if matrices looks okay
    public static void print2D(double mat[][]){
        for(double[] row : mat){
                System.out.println(Arrays.toString(row));
            }
        
    } 
    
    //method to addEdges. It's undirected graph so we update both upper half and lower half
    public void addEdge(int source, int destination, double weight) {
        adjacencyMatrix[source-1][destination-1] = weight;
        adjacencyMatrix[destination-1][source-1] = weight;
    }   

    //method to process custom error
    public static void processError(NumberOutOfRangeException e){
        
        e.printStackTrace();
    }
    
   
}




