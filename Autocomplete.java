/******************************************************************************
 *  Name:    Marcus Jonas
 *  NetID:   mj16
 *  Precept: P01
 *
 *  Partner Name:    Jens Clausen
 *  Partner NetID:   jclausen
 *  Partner Precept: P05A
 * 
 *  Description:  Data type that provides autocomplete functionality for a 
 *                given set of string and weights, using Term and 
 *                BinarySearchDeluxe. It sorts the terms in lexicographic order;
 *                uses binary search to find the all query strings that start 
 *                with a given prefix; and sorts the matching terms in 
 *                descending order by weight.
 *
 ******************************************************************************/

import java.util.Arrays;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Autocomplete {

    private final Term[] copy;     // Array of terms
    
    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        
        // if the array is null throw an exception
        if (terms == null) 
            throw new IllegalArgumentException("Invalid argument");  
        
        copy = new Term[terms.length]; 
        
        // check if entries are null and make defensive copy of terms 
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) 
                throw new IllegalArgumentException("An entry in terms is null");
            copy[i] = terms[i]; 
        }
        
        Arrays.sort(copy); 
    }
    

    // Returns all terms that start with the given prefix, in 
    // descending order of weight.
    public Term[] allMatches(String prefix) {
        
        if (prefix == null) 
            throw new IllegalArgumentException("Invalid argument");
        
        int length = prefix.length();
        
        Term searchTerm = new Term(prefix, 0);
        
        int first = BinarySearchDeluxe.firstIndexOf(copy, searchTerm, 
                                                    Term.byPrefixOrder(length));
        
        int last = BinarySearchDeluxe.lastIndexOf(copy, searchTerm, 
                                                    Term.byPrefixOrder(length));
        
        // if there is no match 
        if (first == -1 && last == -1)
            return new Term[0];
            
            
        Term[] matches = Arrays.copyOfRange(copy, first, last + 1);
        
        Arrays.sort(matches, Term.byReverseWeightOrder());
        
        return matches;
        
        
    }
    

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        
        if (prefix == null) 
            throw new IllegalArgumentException("Invalid argument");
        
        int length = prefix.length();
        
        Term searchTerm = new Term(prefix, 0);
        
        int first = BinarySearchDeluxe.firstIndexOf(copy, searchTerm, 
                                                    Term.byPrefixOrder(length));
        
        int last = BinarySearchDeluxe.lastIndexOf(copy, searchTerm, 
                                                  Term.byPrefixOrder(length));
        
        // if there are no matches return 0
        if (first == -1 && last == -1) 
            return 0;
        
        return last - first + 1; 
    }

    // unit testing (required)
    public static void main(String[] args) {
        
        // read in the terms from a file
        int n = StdIn.readInt();
        Term[] terms = new Term[n]; 
        String searchString = "Al M"; // use to change the search string
        for (int i = 0; i < n; i++) {
            long weight = StdIn.readLong();           // read the next weight
            StdIn.readChar();                         // scan past the tab
            String query = StdIn.readLine();          // read the next query
            terms[i] = new Term(query, weight);       // construct the term
        }
        
        // read in queries from StdIn and print out the top k matching terms
        int k = Integer.parseInt(args[0]);
        Autocomplete autocomplete = new Autocomplete(terms);
            Term[] results = autocomplete.allMatches(searchString);
            StdOut.printf("%d matches\n", 
                          autocomplete.numberOfMatches(searchString));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]); 
        }    
}
