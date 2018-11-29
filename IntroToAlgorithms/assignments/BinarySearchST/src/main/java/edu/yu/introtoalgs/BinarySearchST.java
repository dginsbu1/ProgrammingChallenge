package edu.yu.introtoalgs;

import java.util.NoSuchElementException;
import java.util.LinkedList;



//Almost all code copied from this website (obviously, delete and floor were not and I did not look at them)
//https://algs4.cs.princeton.edu/31elementary/BinarySearchST.java.html
public class BinarySearchST<Key extends Comparable<Key>, Value> {
    private static final int INIT_CAPACITY = 1000;
    private Key[] keys;
    private Value[] vals;
    private int n = 0;



// public static void main(String[] args){
   

//     BinarySearchST table = new BinarySearchST();
//     System.out.println("table 1:");
//     table.put(1,"a");
//     table.delete(1);
//     table.print();//0
//     table.delete(1);
//     table.print();//0

//     Integer[] ints1 = {0,2,3,4,5,6,7,8};
//     String[] strs1 = {"a", "b", "c", "d", "e", "f","g","h"};
//     BinarySearchST<Integer, String> table2 = new BinarySearchST<> (ints1, strs1);
//     //System.out.println("table 2:");

//     Integer[] ints2 = {9,7,6,5,4,3,2,1};
//     String[] strs2 = {"a", "b", "c", "d", "e","f","g","h"};
//     //Iterable<Key> keyz;
//     BinarySearchST<Integer, String> table3 = new BinarySearchST<> (ints2, strs2);
//     while(table2.keys().hasNext()){
//         System.out.println(table2.keys().getNext());   
//     }
    

//     //System.out.println(table3.select(2));
//     //table3.print();
//     //table3.delete(9);
//     //table3.print();
//     //System.out.println();
//     System.out.println(table3.floor(table2.select(7)));//7
//     System.out.println(table3.floor(table3.select(5)));//6
//     System.out.println(table3.floor(table3.select(3)));//4
//     System.out.println(table3.floor(table2.select(0)));//null


//     // table2.delete(3);
//     // table2.print();//1-8 no 3
//     // table2.delete(1);
//     // table2.print();//2,4,5,6,7,8
//     // table2.delete(8);
//     // table2.print();//2,4,5,6,7
//     // table2.delete(9);
//     // table2.print();//2,4,5,6,7
//     // table2.delete(-2);
//     // table2.print();////2,4,5,6,7


//     //table.print();
//     //table2.print();
//     //table3.print();
// }

    /** Initializes the ST with initial keys and
     * corresponding values . The keys and values
     * are inserted in array order : i.e. , first
     * ( initialKeys [0] , initialValues [0] , then
     * ( initialKeys [1] , initialValues [1])
     * .... ( initialKeys [n -1] ,
     * initialValues [n -1])
     */
    public BinarySearchST (Key[] initialKeys, Value[] initialValues){
        this(INIT_CAPACITY);
        for(int i = 0; i < initialKeys.length; i++){
            put(initialKeys[i], initialValues[i]);
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public BinarySearchST() {
        this(INIT_CAPACITY);
    }
    public void print(){
        for(Key key: keys){
            if(key != null)
            System.out.print(key);
        }
        System.out.println();
        for(Value val: vals){
            if(val != null)
            System.out.print(val);
        }
        System.out.println();
    }

    /**
     * Initializes an empty symbol table with the specified initial capacity.
     *
     * @param capacity the maximum capacity
     */
    public BinarySearchST(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
    }

    // resize the underlying arrays
    private void resize(int capacity) {
        assert capacity >= n;
        Key[] tempk = (Key[]) new Comparable[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        vals = tempv;
        keys = tempk;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }


    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        if (isEmpty()) return null;
        int i = rank(key);
        if (i < n && keys[i].compareTo(key) == 0) return vals[i];
        return null;
    }


    /**
     * Returns the number of keys in this symbol table strictly less than {@code key}.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");

        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }




    public void delete(Key key) {
        if (key == null) {throw new IllegalArgumentException("argument to delete() is null");}
        if (isEmpty()) {return;}
        if(!contains(key)){return;}

        int i = rank(key);//rank = position
        //if it's there
        if (i < n && keys[i].compareTo(key) == 0){
            for (int j = i; j < n-1; j++) {
                keys[j] = keys[j + 1];
                vals[j] = vals[j + 1];
            }
            //sets the last values to null
            keys[n-1] = null;
            vals[n-1] = null;
            n--;
        }
    }
    /**
     * Returns the smallest key in this symbol table greater than or equal to {@code key}.
     *
     * @param key the key
     * @return the smallest key in this symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        int i = rank(key);
        if (i == n) return null;
        else return keys[i];
    }
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        int i = rank(key);
        if(i == 0 && (keys[0].compareTo(key)) != 0){//it's smallest value 
            return null;
        }
        //if equal return, otherwise return one less
        if(keys[i].compareTo(key) == 0){//same thing
            return keys[i];
        }
        //if it's larger then return the eone right beloew
        return keys[i-1];
    }
    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");

        if (val == null) {
            delete(key);
            return;
        }
        int i = rank(key);
        // key is already in table
        if (i < n && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }
        // insert new key-value pair
        if (n == keys.length) resize(2 * keys.length);

        for (int j = n; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }
        keys[i] = key;
        vals[i] = val;
        n++;

        //assert check();
    }


    /**
     * Removes the smallest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(min());
    }

    /**
     * Removes the largest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow error");
        delete(max());
    }


    /***************************************************************************
     *  Ordered symbol table methods.
     ***************************************************************************/

    /**
     * Returns the smallest key in this symbol table.
     *
     * @return the smallest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return keys[0];
    }

    /**
     * Returns the largest key in this symbol table.
     *
     * @return the largest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return keys[n - 1];
    }

    /**
     * Return the kth smallest key in this symbol table.
     *
     * @param k the order statistic
     * @return the {@code k}th smallest key in this symbol table
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("called select() with invalid argument: " + k);
        }
        return keys[k];
    }




    /**
     * Returns the number of keys in this symbol table in the specified range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in this symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    /**
     * Returns all keys in this symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in this symbol table
     */
    public Iterable<Key> keys() {
        return keys(min(), max());
    }


    /**
     * Returns all keys in this symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in this symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");
        LinkedList<Key> queue = new LinkedList<Key>();
        if (lo.compareTo(hi) > 0) return queue;
        for (int i = rank(lo); i < rank(hi); i++)
            queue.add(keys[i]);
        if (contains(hi)) queue.add(keys[rank(hi)]);
        return queue;
        // for(Key key: queue){
        //     System.out.print(queue.get(Key));
        // }
    }
}
