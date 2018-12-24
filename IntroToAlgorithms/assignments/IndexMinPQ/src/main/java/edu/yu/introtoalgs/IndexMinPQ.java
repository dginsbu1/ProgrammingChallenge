package edu.yu.introtoalgs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class IndexMinPQ <Key extends Comparable <Key>> implements Iterable<Integer>{
    private int maxN;
    private int n;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Key[] keys;      // keys[i] = priority of i
    //private List<Integer> list;

    public IndexMinPQ(int maxN){
        if(maxN < 1){
            throw new IllegalArgumentException(maxN+" is invalid. Input must be greater than zero.");
        }
        this.maxN = maxN;
        //check for error
        keys = ((Key[]) new Comparable[maxN+1]);
        pq = new int[maxN+1];
        qp = new int[maxN+1];
        for(int i = 0; i <= maxN; i++){ qp[i] = -1;}
    }

//    public static void main(String args[]) throws FileNotFoundException {
//        IndexMinPQ minPQ = new IndexMinPQ(6);
//        File file = new File("C:\\Users\\dgmon\\YU\\IndexMinPQTest.txt");
//        Scanner in;
//        in = new Scanner (file);
//        while(in.hasNextLine()) {
//            String string = in.nextLine();
//            String[] line = string.split(" ");
//            int i = Integer.parseInt(line[0]);
//            int k = Integer.parseInt(line[1]);
//            minPQ.insert(i,k);
//        }
//        Iterator iterator =  minPQ.iterator();
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//        iterator.next();
//        //Iterator q = minPQ.iterator();
//        while(!minPQ.isEmpty()){
//
//            System.out.println("Size is: "+minPQ.size()+" index of: " + minPQ.minIndex() + " value of :"+ minPQ.keyOf(minPQ.minIndex()) +
//                    " which should be equal to "+ minPQ.minKey());
//            minPQ.delMin();
//        }
//
//        System.out.println(minPQ.delMin());
//         System.out.println(minPQ.delMin());
//         System.out.println(minPQ.minKey());
//         System.out.println(minPQ.minIndex());
//
//    }

    public boolean isEmpty()
    { return n == 0; }
    //
    public boolean contains(int i ) {
        checkRange(i, maxN);
        return qp[i] != -1;
    }

    //insert key and associate it with index i
    public void insert(int i, Key key){
        checkRange(i, maxN);
        if(keys[i] != null) {
            changeKeys(i, key);
            return;
        }
        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        swim(n);
    }

    private void changeKeys(int i, Key key) {
        keys[i] = key;
        swim(qp[i]);
        sink(qp[i]);
    }

    public Key keyOf(int i)
    {
        checkRange(i, maxN);
        return keys[i];
    }

    private void checkRange(int i, int maxN) {
        if(i < 0 || i > maxN){
            throw new IllegalArgumentException(i + " cannot be negative or larger than max capacity of "+ maxN);
        }
    }

    public Key minKey()
    {return keys[pq[1]];}
    
    public int delMin(){
        if(isEmpty()){
            throw new IllegalStateException("The PQ is empty");
        }
        int indexOfMin = pq[1];
        exch(1,n--);
        sink(1);
        keys[pq[n+1]] = null;
        qp[pq[n+1]] = -1;
        return indexOfMin;
    }

    public int size() 
    {return n;}

    public int minIndex()
    {return pq[1];}

    //exchanges ints in pq and qp in position i,j
    private void exch(int i, int j){
        checkRange(i, maxN);
        checkRange(j, maxN);
        exchqp(pq[i], pq[j]);
        int t = pq[i]; pq[i] = pq[j]; pq[j] = t;

    }
    //exchanges ints in qp in position i,j
    private void exchqp(int i, int j){
        int t = qp[i]; qp[i] = qp[j]; qp[j] = t;
    }
    
    private boolean greater(int i, int j){
        checkRange(i, maxN);
        checkRange(j, maxN);
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }
    
    private void swim(int k) {
        while(k > 1 && greater(k/2,k)){
            exch(k/2,k);
            k = k/2;
        }
    }
    
    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    //returns indices of the keys in order of PQ
    public Iterator<Integer> iterator () {
        //int[] hold = new int[pq.length];
        List<Integer> list = new ArrayList();
        IndexMinPQ copy = new IndexMinPQ(this.maxN);
        for(int i = 1; i< pq.length;i++){
            if(this.keyOf(i) != null);
                copy.insert(pq[i], this.keyOf(pq[i]));
        }
        for(int i = 1; i< pq.length;i++){
            list.add(copy.delMin());
        }
        return list.iterator();
    }
}
