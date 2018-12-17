package edu.yu.introtoalgs;
import java.util.*;

public class Digraph
{
    private final int V;
    private int E;
    private ArrayList<Integer>[] adj;

    public Digraph(int V)
    {
        this.V = V;
        this.E = 0;
        adj = (ArrayList<Integer>[])new ArrayList[V];
        for (int v = 0; v < V; v++)
            adj[v] = new ArrayList<>();
    }

//    public Digraph(In in)
//    {
//        this(in.readInt()); // Read V and construct this graph.
//        int E = in.readInt(); // Read E.
//        for (int .i = 0; i < E; i++)
//        { // Add an edge.
//            int v = in.readInt(); // Read a vertex,
//            int w = in.readInt(); // read another vertex,
//            addEdge(v, w); // and add edge connecting them.
//        }
//    }

    public int V() { return V; }
    public int E() { return E; }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        E++;
    }

    public Iterable<Integer> adj(int v)
    { return adj[v]; }

    public Digraph reverse()  {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++)
            for (int w : adj(v))
                R.addEdge(w, v);
        return R;
    }
}