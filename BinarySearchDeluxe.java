/******************************************************************************
 *  Name:    Marcus Jonas
 *  NetID:   mj16
 *  Precept: P01
 *
 *  Partner Name:    Jens Clausen
 *  Partner NetID:   jclausen
 *  Partner Precept: P05A
 * 
 *  Description: When binary searching a sorted array that contains more than 
 *               one key equal to the search key, the client may want to know 
 *               the index of either the first or the last such key. This data
 *               type provides this functionality. 
 *
 ******************************************************************************/
import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class BinarySearchDeluxe {

    // Returns the index of the first key in a[] that equals the search key, 
    // or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, 
                                         Comparator<Key> comparator) {
        
        if (a == null || key == null || comparator == null) 
            throw new IllegalArgumentException("An argument is null");
        
        int lo = 0;
        int high = a.length - 1;
        
        int firstIndex = -1;
        
        while (lo <= high) { 
            
            // middle key of the array
            int mid = lo + ((high - lo) / 2);
            
            // compare the terms 
            int compareResult = comparator.compare(key, a[mid]);
            if (compareResult < 0) high = mid - 1;
            if (compareResult > 0) lo = mid + 1;   
            
            if (compareResult == 0) {
                firstIndex = mid;
                high = mid - 1; }
             
        }
        return firstIndex;
        
    }
    // Returns the index of the last key in a[] that equals the search key, 
    // or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, 
                                        Comparator<Key> comparator) { 
        
        if (a == null || key == null || comparator == null) 
            throw new IllegalArgumentException("An argument is null");
        
        int lo = 0;
        int high = a.length - 1;
        
        int lastIndex = -1;
        
        while (lo <= high) { 
            
            // midle key of the array - round up if it is a .5 value
            int mid = (lo + ((high - lo) / 2)) + ((high - lo) % 2);
            
            // compare the terms 
            int compareResult = comparator.compare(key, a[mid]);
            if (compareResult < 0) high = mid - 1;
            if (compareResult > 0) lo = mid + 1;

            
            if (compareResult == 0) {
                lastIndex = mid;
                lo = mid + 1; }
        }

        return lastIndex;
        
    }
    
    
    // unit testing (required)
    public static void main(String[] args) {
        
       // read number of terms in StdIn
       int n = StdIn.readInt();
       
       // create array
       Term[] testArray = new Term[n];
       
       // counter for the indexes 
       int i = 0;
       
       Term key = new Term("Aa", 0);
       
       // fill the array
       while (!StdIn.isEmpty()) {
           int weight = StdIn.readInt();
           String query = StdIn.readString();
           testArray[i] = new Term(query, weight);
           i++;
       }
       
       // sort the array 
       Arrays.sort(testArray, Term.byPrefixOrder(10));
       
       // print the values of the first and last indecies 
       StdOut.println("First Index = " + firstIndexOf(testArray, key, 
                                                      Term.byPrefixOrder(10)));
       StdOut.println("Last Index = " + lastIndexOf(testArray, key, 
                                                    Term.byPrefixOrder(10)));
       StdOut.println();
           
       // print the first 200 terms in the sorted array to check answers for 
       // firstIndexOf and lastIndexOf
       for (int q = 0; q < 200; q++) {
           StdOut.println(q + " " + testArray[q].toString()); }
              StdOut.println();
                     StdOut.println();          
    
    }
}


