package edu.yu.introtoalgs;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set.*;

//You must implement the Digraph API from Sedgewick (page 569) as
//a class named Digraph.java, copying his code if you want.

public class TopologicalQ {
   // private Iterable<Integer> adj;
    private ArrayList<Integer> list;
    private boolean hasOrder;
    private Queue<Integer> sourceQueue = new Queue<>();
    private int inDegrees[];
    private Digraph G;

    public static void main(String args[]) throws FileNotFoundException {
        Digraph dg = new Digraph(13);
//        dg.addEdge(0,1);
//        dg.addEdge(0,2);
//        dg.addEdge(4,3);
//        dg.addEdge(3,2);
//        dg.addEdge(2,5);
//        dg.addEdge(5,0);
//        dg.addEdge(5,0);

        File file = new File("C:\\Users\\dgmon\\YU\\nums.txt");
        Scanner in;
        in = new Scanner (file);

        while(in.hasNextLine()) {
            String string = in.nextLine();
            String[] line = string.split(" ");
            int v = Integer.parseInt(line[0]);
            int w = Integer.parseInt(line[1]);
            dg.addEdge(v,w);
        }

        //dg.addEdge(5,0);
        TopologicalQ tq = new TopologicalQ(dg);
        Iterable<Integer> order = tq.order();
        System.out.println("has order: "+ tq.hasOrder());
        System.out.println("order is ");
        for(Integer s : order){
            System.out.println(s);
        }

    }
//    This constructor determines whether the digraph has a cycle or
//    not. If it does not have a cycle, the constructor determines a
//    valid topological sort for the digraph.
    public TopologicalQ (Digraph G) {
        list = new ArrayList<>();
        //marked = new boolean[G.V()];
        this.G = G;
        setDegreeAndSource();
        sort();
        hasOrder = (list.size() == G.V());

//        System.out.println("Indegrees");
//        for (int j = 0; j < G.V(); j++) {
//            System.out.println(j + ": " +inDegrees[j]+ " in degrees");
//        }
//        System.out.println("now for the queue");
//        int srcQSize = sourceQueue.size();
//        for (int k = 0; k < srcQSize; k++) {
//            System.out.println(sourceQueue.dequeue());
//        }
    }
    private void sort(){
        while (sourceQueue.size() > 0){
            markNext();
        }

    }
    private void markNext(){
        int src = sourceQueue.dequeue();
        //marked[src] = true;
        list.add(src);
        for (Integer in : G.adj(src)) {
            if(--inDegrees[in] == 0){
                sourceQueue.enqueue(in);
            }
        }
    }
    private void setDegreeAndSource(){
        Digraph reverseDigraph = G.reverse();
        inDegrees = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            int inDegree = 0;
            //finds total in degrees
            for (Integer in : reverseDigraph.adj(i)) {
                inDegree++;
            }
            inDegrees[i] = inDegree;
            if (inDegree == 0) {
                sourceQueue.enqueue(i);
            }
        }

    }
//    This method returns true iff G has a topological order, false
//    otherwise.

    public boolean hasOrder (){
        return hasOrder;
    }

//    This method returns an java.util.Iterator over the vertices in a
//    valid topological sort. The method returns null if no topological
//    order exists.
    public Iterable<Integer> order(){
        if(hasOrder){
            return list;
        }
        return null;
    }









}
