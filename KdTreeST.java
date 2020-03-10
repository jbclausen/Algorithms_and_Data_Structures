/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    Kiersten Marr
 *  Partner NetID:   kmarr
 *  Partner Precept: P04B
 * 
 *  Description: A mutable data type that represents a symbol table whose keys 
 *               are two-dimensional points with values. The points are stored
 *               in nodes which form a tree. at each level of the tree, the 
 *               part of the coordinate that we compare is different. for the 
 *               first level (the level of the root) we compare the x values 
 *               and then it alternates as you go down the tree. This 
 *               implementation uses recursion often to traverse the tree. 
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;


public class KdTreeST<Value> {
    
    private static final RectHV MAIN_RECT = new RectHV(Double.NEGATIVE_INFINITY, 
                                                       Double.NEGATIVE_INFINITY, 
                                                       Double.POSITIVE_INFINITY, 
                                                      Double.POSITIVE_INFINITY); 
    
    private Node root; // the root of the kd tree 
    private int size; // the size of the kd tree
    private Point2D best; // the nearest point to a query point
    private double closestSqDist; // closest squared distance found
    
    private class Node {
        private final Point2D p;     // the point
        private Value value;   // the symbol table maps the point to this value
        private final RectHV rect;   // the axis-aligned rectangle 
        private Node lb;       // the left/bottom subtree
        private Node rt;       // the right/top subtree
        private final boolean level; // the orientation of the node 
                                     // (true = compare x)
        
        // constructs a node that will be inserted into the KDTree 
        public Node(Point2D p, Value val, RectHV rectangle, boolean level) {
            this.p = p;
            this.value = val; 
            this.rect = rectangle;
            this.level = level;
            this.lb = null;
            this.rt = null;
        }
    }
    
    // construct an empty symbol table of points
    public KdTreeST() {
        
    }
    
    // how are points a and b positined in relation to each other 
    private int compare(Point2D a, Point2D b, boolean level) {
        // (current.p, p, level)
        // comp >= 0) current.rt
        int comp = 0; 
        if (level) {
            if (a.x() < b.x()) comp = 1;
            else if (a.x() > b.x()) comp = -1;
            else { comp = 0; }
        }
        
        else {
            if (a.y() < b.y()) comp = 1;
            else if (a.y() > b.y()) comp = -1;
            else { comp = 0; }
        }
        
        return comp;
    }
    
    // is the symbol table empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points
    public int size() {
        return size; 
    }
    
    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) 
            throw new IllegalArgumentException("An argument is null");
        
        // true means that we compare x values (shows orientation of the nodes)
        boolean level = true; 
        
        // the rectangle associated with the root 
        RectHV rectangle = MAIN_RECT;

        // insert the new point
        root = put(root, p, val, level, rectangle);
    }
    
    // private method for inserting nodes to be able to use recursion
    private Node put(Node current, Point2D p, Value val, boolean level, 
                     RectHV rectangle) {
        
        if (current == null) {
            size++;
            return new Node(p, val, rectangle, level);
        }
        
        if (current.p.equals(p)) {
            current.value = val;
            return current;
        }
        
        // find how the nodes are positioned w.r.t. each other 
        int comp = compare(current.p, p, level); 
        
        // flip the level - the next level's orientation is opposite
        level = !level;
        
        RectHV rect = null;
             
        // update the size of the rectangle based on which direction we move in
        if (current.level) {
            if (comp >= 0) {
                rect = new RectHV(current.p.x(), current.rect.ymin(),
                                         current.rect.xmax(), 
                                         current.rect.ymax());
            } else {
                rect = new RectHV(current.rect.xmin(), current.rect.ymin(),
                                         current.p.x(), 
                                         current.rect.ymax());
            }
        }
        if (!current.level) {
            if (comp >= 0) {
                rect = new RectHV(current.rect.xmin(), current.p.y(),
                                         current.rect.xmax(), 
                                         current.rect.ymax());
            } else {
                rect = new RectHV(current.rect.xmin(), current.rect.ymin(),
                                         current.rect.xmax(), 
                                         current.p.y());
            }
        }
        
        // do the recursive calls to continue moving towards the end of the tree
        if (comp >= 0) current.rt = put(current.rt, p, val, level, rect);
        else current.lb = put(current.lb, p, val, level, rect);
        return current; 
    }
        
    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) 
            throw new IllegalArgumentException("An argument is null");
       
        if (root == null) return null; 
        
        // call recursive private method to find the point p
        return get(root, p);
       
    }
    
    // recursive private method that recursively moves towards a point p and 
    // returns the value associated with it or null if it is not in the KDTree
    private Value get(Node current, Point2D p) {
        
        if (p.equals(current.p))
            return current.value; 
        
        int comp = compare(current.p, p, current.level);
        
        if (comp >= 0 && current.rt != null) return get(current.rt, p);
         
        if (comp < 0 && current.lb != null) return get(current.lb, p); 
         
        return null; 
            
    }
        
    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) 
            throw new IllegalArgumentException("An argument is null");
        
        // if it does not contain p, get(p) will return null 
        return get(p) != null; 
    }
    
    // all points in the symbol table
    public Iterable<Point2D> points() {
        if (isEmpty()) return new Queue<Point2D>();
        
        // make a queue of Point2D objects that will eventually be returned 
        Queue<Point2D> pQ = new Queue<Point2D>();
        
        // make a queue to keep track of what order to process nodes 
        Queue<Node> temp = new Queue<Node>();
        
        // start at the root
        Node current = root;
        
        temp.enqueue(root);
        
        while (!temp.isEmpty()) {
            
            current = temp.dequeue();
            
            if (current == null) 
                continue;
            
            pQ.enqueue(current.p);
            
            if (current.lb != null) {
                temp.enqueue(current.lb);
            } 
            if (current.rt != null) {
                temp.enqueue(current.rt);
            }
        }
        
       // return the Queue of Point2D objects 
        return pQ;
    }
    
    // all points that are inside the rectangle (or on the boundary)
     public Iterable<Point2D> range(RectHV rect) { 
        if (rect == null) 
            throw new IllegalArgumentException("An argument is null");
        
        if (isEmpty()) return new Queue<Point2D>();
                      
        // create a Queue of points that are inside the query rectangle 
        Queue<Point2D> inRange = new Queue<Point2D>();
       
        // use the recursive private method to fill the queue 
        range(inRange, root, rect);
        
        // return the queue of points that are in the range
        return inRange;
     }
     
     // private helper method we call to recursively search for points in rect
     private void range(Queue<Point2D> inRange, Node current, RectHV rect) {
         if (current == null)
             return;
         
         if (!current.rect.intersects(rect))
             return;
         
         // if p is in the rectangle, add it to the queue
        if (rect.contains(current.p)) 
            inRange.enqueue(current.p);
        
        // recursively search the right and left subtrees 
        range(inRange, current.rt, rect);
        range(inRange, current.lb, rect);
        
    }
    
    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) { 
       if (p == null) 
            throw new IllegalArgumentException("An argument is null");
       
       // if the tree is empty there is no nearest point 
       if (root == null) 
           return null; 
       
       // start at the root 
       best = root.p;
       closestSqDist = Double.POSITIVE_INFINITY;
       
       // call the recursive private method to find the nearest point 
       nearest(root, p);
       
       return best; 
       
    }
    
    // the recursive method that moves towards the query point then uses pruning
    private void nearest(Node current, Point2D p) {
        
        if (current == null)
            return;
        
        // check if the point in current is closer than best 
        double distSq = current.p.distanceSquaredTo(p);
        
        if (distSq <= closestSqDist) {
            closestSqDist = distSq;
            best = current.p; }
    
        // check if we compare x or y values 
        if (current.level) {
            
            // go towards the point p
            if (current.p.x() <= p.x()) {
                if (current.rt != null) { 
                  if (closestSqDist > current.rt.rect.distanceSquaredTo(p))
                      nearest(current.rt, p); }
                if (current.lb != null) {
                  if (closestSqDist > current.lb.rect.distanceSquaredTo(p))
                      nearest(current.lb, p); }
            }
            
            
            else {
                if (current.lb != null) {
                    if (closestSqDist > current.lb.rect.distanceSquaredTo(p))
                        nearest(current.lb, p); }
                if (current.rt != null) {
                    if (closestSqDist > current.rt.rect.distanceSquaredTo(p))
                        nearest(current.rt, p); }
            }
        }
        
        // compare y values 
        else {
            
            // go towards the point p
            if (current.p.y() <= p.y()) {
                if (current.rt != null) {
                    if (closestSqDist > current.rt.rect.distanceSquaredTo(p))
                        nearest(current.rt, p); }
                if (current.lb != null) {
                    if (closestSqDist > current.lb.rect.distanceSquaredTo(p))
                        nearest(current.lb, p); }
            }
            
            else {
                if (current.lb != null) {
                    if (closestSqDist > current.lb.rect.distanceSquaredTo(p))
                        nearest(current.lb, p); }
                if (current.rt != null) {
                    if (closestSqDist > current.rt.rect.distanceSquaredTo(p))
                        nearest(current.rt, p); }
            }
        }
    }
    
    // unit testing
    public static void main(String[] args) {
        
        KdTreeST<Integer> test = new KdTreeST<Integer>();
        
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