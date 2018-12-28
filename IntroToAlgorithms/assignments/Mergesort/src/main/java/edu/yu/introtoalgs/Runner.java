package edu.yu.introtoalgs;
import static edu.yu.introtoalgs.MergeImplementations.*;

import com.sun.javafx.binding.StringFormatter;
import edu.yu.introtoalgs.MergeImplementations;
import jdk.internal.dynalink.beans.StaticClass;
import java.lang.*;
import java.util.Arrays;
import java.io.*;
import java.util.Scanner;

public class Runner {
    private static Comparable a[] = {"bed","bug","dad","yes","zoo","now","for","tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl","joy","rap",
            "gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
    public static void main (String args[]) throws Exception{

        File file = new File("C:\\Users\\dgmon\\GIT\\IntroToAlgorithms\\assignments\\Mergesort\\src\\main\\java\\edu\\yu\\introtoalgs\\words.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] words = new String[645288];
        String st;
        int i = 0;
        while ((st = br.readLine()) != null){
            words[i] = st;
            i++;
        }
//        for(int j = 0; j < 1000; j++){
//            System.out.println(words[j]);
//        }

//        Comparable b[] = Arrays.copyOf(a, a.length);
//        Comparable c[] = Arrays.copyOf(a, a.length);
//        Comparable d[] = Arrays.copyOf(a, a.length);
//        Comparable e[] = Arrays.copyOf(a, a.length);

        run20times(MergesorterFactory(JDKParallelMerge),words);
        run20times(MergesorterFactory(ParallelMerge), words);
        run20times(MergesorterFactory(MergeX),words);
        run20times(MergesorterFactory(Merge),words);

//        MergesorterFactory(ParallelMerge).sortIt(a);
//        System.out.println("Parallel Merge  ");
//        isSorted(a);
//        MergesorterFactory(Merge).sortIt(b);
//        System.out.println("Merge  ");
//        isSorted(b);
//        MergesorterFactory(MergeX).sortIt(c);
//        System.out.println("MergeX  ");
//        isSorted(c);
//        MergesorterFactory(JDKParallelMerge).sortIt(d);
//        System.out.println("JDKParellelmerge  ");
//        isSorted(d);


    }

    static private double run20times(Mergesorter sorter, Comparable a[])throws Exception{
        double total = 0;
        for (int i = 0; i < 20; i++) {
            Comparable b[] = Arrays.copyOf(a, a.length);
            if( isSorted(b)){ throw new Exception("Already Sorted");}
            long startTime = System.currentTimeMillis();
            sorter.sortIt(b);
            long endTime = System.currentTimeMillis();
            if( !isSorted(b)){ throw new Exception("NOT Sorted");}
            total+= (endTime-startTime);
        }
        System.out.println("Using "+ sorter.getClass()+": the average was "+ total/20 +  " milliseconds");
        return total;
    }
    // print array to standard output
    protected static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
    static private boolean isSorted(Comparable[] a){
        for(int i = 1; i < a.length;i++){
            if(less(a[i],a[i-1])){
                //System.out.println("NOT SORTED");
                return false;
            }
        }
        return true;
        //System.out.println("SORTED");
    }
}