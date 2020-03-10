/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    Kiersten Marr
 *  Partner NetID:   kmarr
 *  Partner Precept: P04B
 * 
 *  Description: Program to find the shortest ancestral path between two 
 *               vertices v and w in a rooted DAG. In directed paths, this 
 *               means the path of minimum total length. Can also be 
 *               generalized to find the shortest common ancestor to subsets 
 *               of vertices. 
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Queue;

public class ShortestCommonAncestor {

    private final Digraph graph; // digraph created using input digraph
    private int shortestDist; // distance to shortest common ancestor 
    private int sca; // shortest common ancestor 
    private BreadthFirstDirectedPaths pathsOne; // bfs for one input
    private BreadthFirstDirectedPaths pathsTwo; // bfs for other input
    
    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        
        graph = new Digraph(G);
        int size = graph.V();
        boolean rooted = false;
        // check if acyclic
        DirectedCycle temp = new DirectedCycle(graph);
        if (temp.hasCycle()) { 
            // throw exception 
            throw new IllegalArgumentException();
        }
        int counter = 0;
        // check is rooted
        // if multiple have outdegree zero 
        for (int i = 0; i < size; i++) {
            if (graph.outdegree(i) == 0) {
                if (counter == 0) {
                    rooted = true;
                    counter++;
                } else { rooted = false; }
            }
        }
        
        if (!rooted) {
            // throw exception
            throw new IllegalArgumentException();
        }
    }
    
    // private method to compute closest ancestor and distance from paths
    private void compute(BreadthFirstDirectedPaths pathOne, 
                         BreadthFirstDirectedPaths pathTwo) {
        shortestDist = Integer.MAX_VALUE;
        for (int i = 0; i < graph.V(); i++) {
            if (pathOne.hasPathTo(i) && pathTwo.hasPathTo(i)) {
                int tempDist = pathOne.distTo(i) + pathTwo.distTo(i);
                if (tempDist < shortestDist) {
                    shortestDist = tempDist;
                    sca = i;
                }
            }
            
        } 
    }
    
    
    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v > graph.V()) {
            throw new IllegalArgumentException();
        }
        if (w < 0 || w > graph.V()) {
            throw new IllegalArgumentException();
        }
        
        pathsOne = new BreadthFirstDirectedPaths(graph, v);
        pathsTwo = new BreadthFirstDirectedPaths(graph, w);
        
        compute(pathsOne, pathsTwo); 
        
        return shortestDist; 
    }
    
    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        
        pathsOne = new BreadthFirstDirectedPaths(graph, v);
        pathsTwo = new BreadthFirstDirectedPaths(graph, w);
        
        compute(pathsOne, pathsTwo); 
        
        return sca; 
    }
    
    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, 
                            Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException();
        }
        
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) {
            throw new IllegalArgumentException();
        }
        
        for (Integer i : subsetA) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
            if (i < 0 || i > graph.V())
                throw new IllegalArgumentException();
        }
        for (Integer i : subsetB) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
            if (i < 0 || i > graph.V())
                throw new IllegalArgumentException();
        }
        
        pathsOne = new BreadthFirstDirectedPaths(graph, subsetA);
        pathsTwo = new BreadthFirstDirectedPaths(graph, subsetB);
        
        compute(pathsOne, pathsTwo);
        
        return shortestDist;
    }
    
    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, 
                              Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException();
        }
        
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext()) {
            throw new IllegalArgumentException();
        }
        
        for (Integer i : subsetA) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
            if (i < 0 || i > graph.V())
                throw new IllegalArgumentException();
        }
        for (Integer i : subsetB) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
            if (i < 0 || i > graph.V())
                throw new IllegalArgumentException();
        }
        
        pathsOne = new BreadthFirstDirectedPaths(graph, subsetA);
        pathsTwo = new BreadthFirstDirectedPaths(graph, subsetB);
        
        compute(pathsOne, pathsTwo); 
        
        return sca;
    }
    
    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            
        }
        
        Queue<Integer> q1 = new Queue<Integer>();
        q1.enqueue(23);
        q1.enqueue(5);
        q1.enqueue(10);
        
        Queue<Integer> q2 = new Queue<Integer>();
        q2.enqueue(1);
        q2.enqueue(6);
        q2.enqueue(12);
        
        // test ancestor subset
        int lengthsubset = sca.lengthSubset(q1, q2);
        StdOut.println(lengthsubset);
        // test length subset
        int ancestorsubset = sca.ancestorSubset(q1, q2);
        StdOut.println(ancestorsubset);
    }
    
}