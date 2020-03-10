/******************************************************************************
 *  Name:    Marcus Jonas
 *  NetID:   mj16
 *  Precept: P01
 *
 *  Partner Name:    Jens Clausen
 *  Partner NetID:   jclausen
 *  Partner Precept: P05A
 * 
 *  Description:  Immutable data type that represents an autocomplete term 
 *                and has a query string and an associated integer weight. 
 *                Supports comparing terms by three different orders: 
 *                Lexicographic Order by query string (the natural order); in 
 *                descending order by weight (an alternate order); and 
 *                lexicographic order by query string but using only the first 
 *                r characters (a family of alternate orderings)
 *
 ******************************************************************************/
import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;

public class Term implements Comparable<Term> {

    // compares terms based on their weight 
    private static final Comparator<Term> REV_WEIGHT_ORDER = 
        new ByReverseWeightOrder();
    
    private final String query; // a query String 
    private final long weight;  // an integer weight

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        
        // Throw exception if Query or Weight is invalid 
        if (query == null || weight < 0)
            throw new IllegalArgumentException("Query or Weight is invalid");
        
        this.query = query;
        this.weight = weight;
    }
    
   
    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return REV_WEIGHT_ORDER;
    }
    
    // Nested class that compares two terms based on weight
    private static class ByReverseWeightOrder implements Comparator<Term> {
        
        public int compare(Term t1, Term t2) {
            if (t1.weight > t2.weight) return -1;
            if (t1.weight < t2.weight) return 1;
            return 0;
        }
    }
    
    // Compares the two terms in lexicographic order but using only the first r
    // characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        return new ByPrefixOrder(r);
    }
    
    // Nested class that compares two terms based on prefixes in the queries
    private static class ByPrefixOrder implements Comparator<Term> {
        private final int r; // the prefix size
        
        // constructor for a prefix size r
        public ByPrefixOrder(int r) {
            
            // Throw exception if r is negative 
            if (r < 0) 
                throw new IllegalArgumentException("The r value is negative");
            
            this.r = r; 
        }
        
        public int compare(Term t1, Term t2) {
            int n = r;
            
            if (t1.query.length() < r)
                n = t1.query.length();
            
            if (t2.query.length() < n) 
                n = t2.query.length(); 
            
            // go through the  substring until the letters are different
            for (int i = 0; i < n; i++) {
                if (t1.query.charAt(i) < t2.query.charAt(i)) return -1;    
                if (t1.query.charAt(i) > t2.query.charAt(i)) return 1;
            }
            
            // if both queries are longer than r and they are equal at this 
            // point, return 0 
            if (n == r)
                return 0;
            
            // if they are equal up to this point but one is shorter than r
            // return the shorter one as "smaller"
            return t1.query.length() - t2.query.length();
        }
    }
    
    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term term) {
        int r = Math.min(query.length(), term.query.length());
        
         // go through the  string until the letters are different
            for (int i = 0; i < r; i++) {
                if (query.charAt(i) < term.query.charAt(i)) return -1;
                if (query.charAt(i) > term.query.charAt(i)) return 1;
            }
            
            // if they are the same, return the difference in length of queries
            return query.length() - term.query.length();
    }
    
    
    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        String s = weight + "\t" + query;
        return s;
    }
    
    
    
    
    // unit testing (required)
    public static void main(String[] args) {
        
        
        // Test that is in the same format as the autograder - for future tests
        Term p = new Term("ACCCTGCT", 8);
        Term q = new Term("ACC", 9);
        
        ByPrefixOrder x = new ByPrefixOrder(100);
        
        StdOut.println("The autograder student output is:" + x.compare(p, q));
        
        
        // more complex testing for our own made up tests 
        int n = 4;
        
        StdOut.println("THIS IS NOW BY REV WEIGHT ORDER");
        
        // REV WEIGHT ORDER TEST
        Term test1 = new Term("aaa", 150);
        Term test2 = new Term("aa", 150);
        Term test3 = new Term("abcdef", 150);
        Term test4 = new Term("aabceef", 150);
        
        // put terms in a sortable array 
        Term[] test = new Term[n];
        
        test[0] = test1;
        test[1] = test2; 
        test[2] = test3;
        test[3] = test4; 
        
        Arrays.sort(test, Term.REV_WEIGHT_ORDER);
        
        // print the array based on weight order 
        for (int i = 0; i < n; i++) {
            StdOut.println(test[i]); }
        
        StdOut.println("THIS IS NOW BY PREFIX ORDER");
        
        Arrays.sort(test, new ByPrefixOrder(4));
        
        Arrays.sort(test, byPrefixOrder(10));
        Arrays.sort(test, byReverseWeightOrder());
        
        // print the array based on prefix order 
        for (int i = 0; i < n; i++) {
            StdOut.println(test[i]); }
        
        
        // TESTING COMPARETO
        StdOut.println(test1.compareTo(test2));
        
        
    }   
}



