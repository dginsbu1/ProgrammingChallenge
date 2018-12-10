package edu.yu.introtoalgs;

import java.util.Arrays;

public class JDKParallelMerge implements Mergesorter {

    JDKParallelMerge() {}

    public static void main(String args[]){
        Comparable a[] = {"bed","bug","dad","yes","zoo","now","for","tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl","joy","rap",
                "gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
        Comparable b[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,0};
        JDKParallelMerge ob = new JDKParallelMerge();
        ob.sortIt(a);
        MergeX.show(a);
    }

    @SuppressWarnings("unchecked")
    public void sortIt(Comparable[] a) {
        Arrays.parallelSort(a);
    }

}