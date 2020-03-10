/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    Luke Wiggins 
 *  Partner NetID:   
 *  Partner Precept: 
 * 
 *  Description: 
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.awt.Color;

// Corner cases. Your code must throw an exception when a constructor or method is called with an invalid argument, as documented below:

public class SeamCarver {
    
    private Picture picture;  // picture
    private int width, height;       // width and height of the picture 
 
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) 
            throw new java.lang.IllegalArgumentException("Argument is null");
        
        this.picture = new Picture(picture); 
        width = picture.width();
        height = picture.height();
        
        
    }
    
    private int coOrdinateToIndex(int x, int y) {
        return y * width + x; 
    }
    
    private int indexToXCoOrdinate(int idx) {
        return idx % width;
    }
    
    private int indexToYCoOrdinate(int idx) {
        return idx / width;
        
    }
    // check to see if the pixel is on the edge of the Picture 
//    private boolean onXBoarder(int x) {
//        return x == 0 || x == width - 1; }
    
    // check to see if the pixel is on the edge of the Picture 
//    private boolean onYBoarder(int y) {
//        return y == 0 || y == height - 1; }
    
    // current picture 
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }
    
    // height of current picture 
    public int height() {
        return height;
    }

    
    // energy of pixel at column x and row y
    private double getHorizontal(int x, int y) {
        
        Color left;
        Color right;
        
        if (width == 1) {
            return 0;
        }
        
        if (x == 0) {
            left = picture.get(width - 1, y);
            right = picture.get(x + 1, y);
        }
        
        else if (x == width - 1) {
            left = picture.get(x - 1, y);
            right = picture.get(0, y);
        }
        
        else {
            left = picture.get(x - 1, y);
            right = picture.get(x + 1, y);
        }
        
        // avoid redundant .get() only call once for r,b,g??
        //Creating Color objects can be a bottleneck. Each call to the get() method in Picture creates a new Color object. You can avoid this overhead by using the getRGB() method in Picture, which returns the color, encoded as a 32-bit int. The companion setRGB() method sets the color of a given pixel using a 32-bit int to encode the color.
        // make function...?
        
        int r1 = left.getRed();
        int g1 = left.getGreen();
        int b1 = left.getBlue();
        
        int r2 = right.getRed();
        int g2 = right.getGreen();
        int b2 = right.getBlue(); 
        
        int deltaRSquared = (r1 - r2) * (r1 - r2); 
        int deltaBSquared = (b1 - b2) * (b1 - b2);
        int deltaGSquared = (g1 - g2) * (g1 - g2); 
        
        double xGrad = deltaRSquared + deltaBSquared + deltaGSquared;
        return xGrad;
    }
    
    private double getVertical(int x, int y){
        
        Color top;
        Color bot;
        
        if (height == 1){
            return 0;
        }
        
        if (y == 0) {
            top = picture.get(x, height - 1);
            bot = picture.get(x, y + 1);
        }
        
        else if (y == height - 1) {
            top = picture.get(x, y - 1);
            bot = picture.get(x, 0);
        }
        
        else {
            top = picture.get(x, y - 1);
            bot = picture.get(x, y + 1);
        }
        

        int r1 = top.getRed();
        int g1 = top.getGreen();
        int b1 = top.getBlue();
        
        int r2 = bot.getRed();
        int g2 = bot.getGreen();
        int b2 = bot.getBlue(); 
        
        int deltaRSquared = (r1 - r2) * (r1 - r2); 
        int deltaBSquared = (b1 - b2) * (b1 - b2);
        int deltaGSquared = (g1 - g2) * (g1 - g2); 
        
        double yGrad = deltaRSquared + deltaBSquared + deltaGSquared;
        return yGrad;  
    }
    
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1 || y < 0 || y > height - 1) 
            throw new java.lang.IllegalArgumentException("Out of range"); 
        
        // compute dx^2
        double xG = getHorizontal(x, y);
        //compute dy^2
        double yG = getVertical(x, y);
        
        double pixelenergy = Math.sqrt(xG + yG);

        return pixelenergy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        
        
        Picture trans = new Picture(height, width);
        
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++) {
                trans.set(j, i, picture.get(i, j));
            }
        }
        
        // change values of instance variables
        Picture temp = picture;
        picture = trans;
        int tempW = width;
        int tempH = height;
        width = height;
        height = tempW; 
        
        // call function
        int[] returnValue = findVerticalSeam();
        
        // change the variables back
        picture = temp;
        height = tempH;
        width = tempW;
        
        return returnValue;
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(width*height); // int or double?
        double[][] distTo = new double[width][height]; // single or a double array? NOT INSTANCE
        int[][] edgeTo = new int[width][height]; // edgeTo? NOT INSTANCE
 
        double[][] engarr = new double[width][height];
        
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                engarr[i][j] = energy(i, j);
                distTo[i][j] = Double.POSITIVE_INFINITY;
                if (j == 0){
                    distTo[i][j] = engarr[i][j];
                    int idx = coOrdinateToIndex(i, j);
//                    StdOut.print("idx is: " + idx);
                    pq.insert(idx, distTo[i][j]);
                }
            }
        }
        // initialize edgeto, distTo, pq (including dist = INF)
        // initialize to height? ... or all?

        while (!pq.isEmpty()){
            // delete the min from the pq ... add it to the int[]
            int vidx = pq.delMin();
            // row
            int vi = indexToXCoOrdinate(vidx);
            // col
            int vj = indexToYCoOrdinate(vidx);
            // relax each adjacent index [vi -1][vj + 1] [vi][vj +1] [vi + 1][vj + 1]
            // WHAT ABOUT IF ITS ON AN EDGE?
            
            int start;
            int stop;
            
            
            if (vi == 0){
                start = vi;
                stop = vi + 2;
            }
            else if (vi == width - 1){
                start = vi - 1;
                stop = vi + 1;
            }
            else {
                start = vi - 1;
                stop = vi + 2;
            }
            
            if (width == 1){
                start = 0;
                stop = 1;
            }
//            StdOut.println("vi: " + vi + " w " + width + " vj " + vj + " h " + height);
//            StdOut.println("start " + start + " stop " + stop);
            
            
          
            
            for (int i = start; i < stop; i++){
                //StdOut.println("distTo adj " + distTo[i][vj + 1] + " dist to current `+ eng " + distTo[vi][vj] + engarr[i][vj + 1]);
                // for each adjacent index update the distance
                // if distTo(adjacent) > distTo(current)
                if (vj != height - 1) {
                    if (distTo[i][vj + 1] > distTo[vi][vj] + engarr[i][vj + 1]) {
                        distTo[i][vj + 1] = distTo[vi][vj] + engarr[i][vj + 1];
                        
                        edgeTo[i][vj + 1] = vidx;
                        
                        // idx key of adjacent pixel
                        int idx = coOrdinateToIndex(i, vj + 1);
                        
//                        StdOut.println(pq.contains(idx));
//                        StdOut.println(distTo[i][vj + 1]);
                        
                        // update the current value or insert into pq
                        if (pq.contains(idx)) pq.decreaseKey(idx, distTo[i][vj + 1]);
                        else pq.insert(idx, distTo[i][vj + 1]);
                    }
                }
            }
            
        }
        
        int[] shortest = new int[height];
        
        // shortest path = pixel on last row with smallest distanceTo()
        // get path by returning the previous edgeTo()s
        int path = 0;
        //int shortest = Integer.MAX_VALUE;
        for (int i = 0; i < width; i++){
            if (distTo[i][height-1] < distTo[path][height - 1])
                path = i;
        }
        
        
        for (int i = height - 1; i >= 0; i--){
            shortest[i] = path;
            path = indexToXCoOrdinate(edgeTo[path][i]);
        }
        
        return shortest;
    }
    
    
    private boolean isHseamValid(int[] seam){
        //check to see if first pixel is in bounds
        if (seam[0] < 0 || seam[0] > height - 1)
                return false;
        
        for (int i = 1; i < seam.length; i++){
            
            // is index in bounds?
            if (seam[i] < 0 || seam[i] > height - 1)
                return false;
            
            // is adjacent to last pixel?
            if (seam[i] < seam[i-1] - 1 || seam[i] > seam[i-1] + 1)
                return false;
            
        }
        return true;
        
    }
    
    private boolean isVseamValid(int[] seam){
        //check to see if first pixel is in bounds
        if (seam[0] < 0 || seam[0] > width - 1)
                return false;
        
        for (int i = 1; i < seam.length; i++){
            
            // is index in bounds?
            if (seam[i] < 0 || seam[i] > width - 1)
                return false;
            
            // is adjacent to last pixel?
            if (seam[i] < seam[i-1] - 1 || seam[i] > seam[i-1] + 1)
                return false;
            
        }
        return true;
        
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) 
            throw new java.lang.IllegalArgumentException("Argument is null");
        
        if (seam.length != width)
            throw new java.lang.IllegalArgumentException("Seam length invalid");
        
        // CHECK IF IS A VALID SEAM 
        if (!isHseamValid(seam))
            throw new java.lang.IllegalArgumentException("Seam invalid");
        
        if (height == 1) 
            throw new java.lang.IllegalArgumentException();
        
        Picture newone = new Picture(width, height - 1);
        
        int x = 0;
        int y = 0;
        
        for (int i = 0; i < width; i++) {
            y = 0;
            for (int j = 0; j < height; j++) {
                if (j == seam[i]) 
                    continue;
                newone.set(x, y, picture.get(i, j));
                y++;
            }
            x++;
        }
        
        picture = newone;
        width = picture.width();
        height = picture.height();
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) 
            throw new java.lang.IllegalArgumentException("Argument is null");
        
        if (seam.length != height)
            throw new java.lang.IllegalArgumentException("Seam length invalid");
        
        //CHECK IF IS A VALID SEAM 
        if (!isVseamValid(seam))
            throw new java.lang.IllegalArgumentException("Seam invalid");
        
        if (width == 1) 
            throw new java.lang.IllegalArgumentException();
        
        Picture newone = new Picture(width - 1, height); 
        
        int x = 0;
        int y = 0;
        
        for (int j = 0; j < height; j++) {
            x = 0;
            for (int i = 0; i < width; i++) {
                if (i == seam[j]) 
                    continue;
                newone.set(x, y, picture.get(i, j));
                x++;
            }
            y++;
        }

        picture = newone;
        width = picture.width();
        height = picture.height();
    }
    
    // unit testing 
    public static void main(String[] args) {
        
        String filename = args[0];
        
        Picture testPic = new Picture(filename);
        
        SeamCarver test = new SeamCarver(testPic);
        
        int[] vertSeam = test.findVerticalSeam();
        int[] horSeam = test.findHorizontalSeam();
        
        StdOut.println("the width is " + test.width());
        StdOut.println("the height is " + test.height());
        
        StdOut.println("Vertical seam: ");
        for (int x : vertSeam) {
            StdOut.print(x+ " "); }
        StdOut.println();
        
        StdOut.println("Horizontal seam: ");
        for (int x : horSeam) {
            StdOut.print(x+ " "); }
        StdOut.println();
    
        test.removeHorizontalSeam(test.findHorizontalSeam());
        
        vertSeam = test.findVerticalSeam();
        horSeam = test.findHorizontalSeam();
        
        StdOut.println("the width is " + test.width());
        StdOut.println("the height is " + test.height());
        
        StdOut.println("Vertical seam: ");
        for (int x : vertSeam) {
            StdOut.print(x+ " "); }
        StdOut.println();
        
        StdOut.println("Horizontal seam: ");
        for (int x : horSeam) {
            StdOut.print(x+ " "); }
        StdOut.println();
    
    
    
    
    
    
    
    }
    
}















