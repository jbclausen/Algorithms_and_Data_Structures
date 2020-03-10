/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    Kiersten Marr
 *  Partner NetID:   kmarr
 *  Partner Precept: P04B
 * 
 *  Description: Write a mutable data type PointST.java that represents a symbol
 *               table whose keys are two-dimensional points. It is a brute 
 *               force implementation. The keys have Values. 
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;


public class PointST<Value> {
    
    private final RedBlackBST<Point2D, Value> data; // The ST of points 
    
    // construct an empty symbol table of points
    public PointST() {
        data = new RedBlackBST<Point2D, Value>();
    }
    
    // is the symbol table empty?
    public boolean isEmpty() {
        return data.isEmpty();
    }
    
    // number of points
    public int size() {
        return data.size();
    }
    
    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("Argument is null");
        data.put(p, val);
    }
    
    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument is null");
        
        if (data.isEmpty())
            return null; 
        
        return data.get(p);
    }
        
    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Argument is null");
        
        return data.contains(p);
    }
    
    // all points in the symbol table
    public Iterable<Point2D> points() {
        return data.keys();
    }
    
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) { 
        if (rect == null)
            throw new IllegalArgumentException("Argument is null");
            
        Queue<Point2D> queue = new Queue<Point2D>();
        
        if (data.isEmpty())
            return queue; 
        
        for (Point2D p: data.keys()) {
            if (rect.contains(p)) {
                queue.enqueue(p);
            }
        }
        return queue; 
    }
    
    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) { 
        if (p == null || data == null)
            throw new IllegalArgumentException("Argument is null");
        
        if (data.isEmpty())
            return null; 
        
        // set closest distance to very far away 
        Point2D closest = null;
        double minDist = Double.POSITIVE_INFINITY; 
        
        for (Point2D point: data.keys()) {
            double temp = p.distanceSquaredTo(point);
            if (temp < minDist) {
                minDist = temp;
                closest = point;
            }
        }
        return closest;
    }
     
    // unit testing
    public static void main(String[] args) {
        PointST<Integer> test = new PointST<Integer>();
        
        int counter = 0;
        
        StdOut.println(test.isEmpty() + " " + test.size());
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double x = 0.0;
                double y = 0.0;
                Point2D temp = new Point2D(x, y);
                x += 0.5;
                y += 0.5;
                test.put(temp, counter); 
                counter++;
                StdOut.println(test.isEmpty() + " " + test.size()+" " +counter);
            }
        }
        
        Point2D testPoint = new Point2D(1.63, 1.63);
        
        StdOut.println("This is the nearest point to the testPoint: " +
                       test.nearest(testPoint));
        
        Point2D a = new Point2D(0.7, 0.2);
        Point2D b = new Point2D(0.5, 0.4);
        Point2D c = new Point2D(0.2, 0.3);
        Point2D d = new Point2D(0.4, 0.7);
        Point2D e = new Point2D(0.9, 0.6);
        
        KdTreeST<Integer> test2 = new KdTreeST<Integer>();
        
        test2.put(a, 1);
        test2.put(b, 2);
        test2.put(c, 3);
        test2.put(d, 4);
        test2.put(e, 5);
        
        StdOut.println("Does test2 have point 'a'? " + test2.contains(a));
        StdOut.println("This is the value that is associated with 'a': " + 
                       test2.get(a));
        
        RectHV testRect = new RectHV(0.25, 0.25, 0.75, 0.75);
        
        StdOut.println("These are the points in the testRect: " + 
                       test2.range(testRect));
        
        for (Point2D p : test2.points()) {
            StdOut.println(p); }

    }
    
    
}














