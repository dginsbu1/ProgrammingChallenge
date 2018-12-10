package edu.yu.introtoalgs;

import javafx.concurrent.Task;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMerge extends RecursiveAction implements Mergesorter  {

    int lo, high;
    static Comparable[] list;
    public static final int CUTOFF = 10000;

    ParallelMerge() {}

    ParallelMerge(Comparable[] a, int lo, int high){
        this.lo = lo;
        this.high = high;
        this.list = a;
    }
    public static void main(String args[]){
        Comparable a[] = {"bed","bug","dad","yes","zoo","now","for","tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl","joy","rap",
                "gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
        Comparable b[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,0};

        ParallelMerge parM = new ParallelMerge();
        //ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();
        //pool.invoke(parM);
        parM.sortIt(a);
        //pool.execute(parM);
        long endTime = System.currentTimeMillis();
        MergeX.show(a);
        System.out.println("The list is sorted: " + isSorted(a));
        System.out.println("ParallelMerge took " + (endTime - startTime) +
                " milliseconds.");

    }

    @SuppressWarnings("unchecked")
    public void sortIt(Comparable[] a) {
        this.list = a;
        lo = 0;
        high = a.length-1;
        compute();
        //sort(a, 0, a.length-1);

    }
    static private boolean isSorted(Comparable[] a){
        for(int i = 1; i < a.length;i++){
            if(less(a[i],a[i-1])){
                return false;
            }
        }
        return true;
    }

//    private void sort(Comparable[] a, int lo, int high){
//        ParallelMerge pm = new ParallelMerge(a,lo,high);
//
//    }

    @Override
    protected void compute() {
        if(high-lo < CUTOFF) {
            computeDirectly(lo,high);
            return;
        }
        else{
            int split = (high + lo) / 2;
            ParallelMerge leftMerge = new ParallelMerge(list,lo,split);
            ParallelMerge rightMerge =  new ParallelMerge(list,split+1,high);
            invokeAll(leftMerge, rightMerge);
            merge(list, lo,split,high);
        }

    }

    private void computeDirectly(int lo, int high){
        Arrays.sort(list,lo,high+1);//uses index inclusive
    }

    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        Comparable[] aux = new Comparable[a.length];
        // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
        //assert isSorted(a, lo, mid);
        //assert isSorted(a, mid+1, hi);
        int i = lo, j = mid+1;
        // copy to aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        // merge back to a[]
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }

        // postcondition: a[lo .. hi] is sorted
        //assert isSorted(a, lo, hi);
    }
    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }


}