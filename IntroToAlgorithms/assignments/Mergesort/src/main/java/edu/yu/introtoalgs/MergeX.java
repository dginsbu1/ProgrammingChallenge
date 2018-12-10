package edu.yu.introtoalgs;

public class MergeX implements Mergesorter {
    private static Comparable[] aux;
    private static final int CUTOFF = 10;
    private static int counter = 0;//for testing
    private static int selectionCounter = 0;//for testing

       // This class should not be instantiated.
    MergeX() {}

    public static void main(String args[]){
        Comparable a[] = {"bed","bug","dad","yes","zoo","now","for","tip","ilk","dim","tag","jot","sob","nob","sky","hut","men","egg","few","jay","owl","joy","rap",
                "gig","wee","was","wad","fee","tap","tar","dug","jam","all","bad","yet"};
        Comparable b[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,0};
        sort(b);
        show(b);
        System.out.println("Merge used: "+counter+" times");
        System.out.println("Selection used: "+selectionCounter+" times");

    }
    // print array to standard output
    protected static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    @SuppressWarnings("unchecked")
    public void sortIt(Comparable[] a) {//TODO should this be static?????
        sort(a);
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a) {
        aux = new Comparable[a.length];
        sort(a, 0, a.length - 1);
        //assert isSorted(a);
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    protected static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        if((hi - lo + 1) < CUTOFF) {
            selectionSort(a, lo, hi);
            //show(a);//TODO for testing
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid);//sort left half
        sort(a, mid + 1, hi);//sort right half
        if(less(a[mid],a[mid+1])){return;}
        merge(a, lo, mid, hi);//Merge results
    }

    private static void selectionSort(Comparable[] a, int lo, int hi){
        selectionCounter++;
        for(int i = lo; i <= hi; i++){
            int min = i;
            for(int j = i; j <= hi; j++){
                 if(less(a[j], a[min])){
                     min = j;
                 }
            }
            //swap
            Comparable temp = a[i];//TODO do i need to cast??
            a[i] = a[min];
            a[min] = temp;
        }

    }
    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        counter++;
        // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
        //assert isSorted(a, lo, mid);
        //assert isSorted(a, mid+1, hi);
        int i = lo, j = mid + 1;
        // copy to aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        // merge back to a[]
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }

        // postcondition: a[lo .. hi] is sorted
        //assert isSorted(a, lo, hi);
    }

    /***************************************************************************
     *  Helper sorting function.
     ***************************************************************************/
    private static Boolean less(Comparable u, Comparable v){
        return u.compareTo(v) < 0;
    }

}