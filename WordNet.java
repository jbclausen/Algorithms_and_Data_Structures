/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    Kiersten Marr
 *  Partner NetID:   kmarr
 *  Partner Precept: P04B
 * 
 *  Description: A WordNet data type that creates a word net using two red black
 *               BSTs and a digraph. Program uses files containing synsets and 
 *               their associated integer ids and then uses another file that 
 *               contains hypernyms to create the directed paths between 
 *               synsets. It can then return an iterable of nouns, determine 
 *               whether the word is a word net noun, and use 
 *               ShortestCommonAncestor.java to determine the shortes common 
 *               ancestor and shortest distance between two nouns. 
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    
    private final RedBlackBST<Integer, Queue<String>> synset; // RRBST - synsets
    private final RedBlackBST<String, Queue<Integer>> nouns; // RRBST for nouns
    private final ShortestCommonAncestor sc; // shortest common ancestor object
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) 
            throw new IllegalArgumentException("An argument is null");
        
        this.nouns = new RedBlackBST<String, Queue<Integer>>();
        this.synset = new RedBlackBST<Integer, Queue<String>>();
        
        // read in the inpute file 
        In in = new In(synsets);
        int vertices = 0;
        while (!in.isEmpty()) {
            vertices++;
            String temp = in.readLine();
            
            // split the id from the nouns
            String[] line = temp.split(",");
            int key = Integer.parseInt(line[0]);
            
            // create a queue of strings
            Queue<String> synQ = new Queue<String>();
            
            // break up the individual nouns
            String[] words = line[1].split(" ");
            
            // for each noun in the array of nouns, inset into data structures
            for (String s : words) {
                // add to synset RRBST
                synQ.enqueue(s);
                // add to noun RRBST
                if (nouns.contains(s)) {
                    Queue<Integer> nounQ = nouns.get(s);
                    nounQ.enqueue(key);
                    // nouns.put(s, nounQ);
                } else {
                    // if it is not in nouns, add it. 
                    Queue<Integer> nounQ = new Queue<Integer>();
                    nounQ.enqueue(key);
                    nouns.put(s, nounQ);
                }
            }
            // insert into the synset RBBST 
            synset.put(key, synQ);
        }
        
        // create a digraph 
        Digraph digraph = new Digraph(vertices);
        
        // read in the input file 
        in = new In(hypernyms);
        
        // create the connections between the hypernyms 
        while (!in.isEmpty()) {
            String temp = in.readLine();
            String[] line = temp.split(",");
            int id = Integer.parseInt(line[0]);
            
            // match all of the connections to the id
            for (int i = 1; i < line.length; i++) {
                int x = Integer.parseInt(line[i]);
                digraph.addEdge(id, x);
            }
               
        }
        // create an immutable SCA object
        sc = new ShortestCommonAncestor(digraph);
        
    }
    
   // all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keys();
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        // throw exception 
        if (word == null) 
            throw new IllegalArgumentException("An argument is null");
        
        return nouns.contains(word);
        
    }

   // a synset (second field of synsets.txt) that is a shortest common ancestor
   // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        // throw necessary exceptions 
        if (noun1 == null || noun2 == null) 
            throw new IllegalArgumentException("An argument is null");
        
        if (!isNoun(noun1) || !isNoun(noun2)) 
            throw new IllegalArgumentException("Input is not a WordNet noun");
        
        // create pointers to the queues in the RBBST
        Queue<Integer> v = nouns.get(noun1);
        Queue<Integer> w = nouns.get(noun2);
        
        // find the shortest common ancestor(s) 
        int ancestor = sc.ancestorSubset(v, w);
        Queue<String> temp = synset.get(ancestor);
        
        return temp.toString();
    }

   // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        // throw necessary exceptions
        if (noun1 == null || noun2 == null) 
            throw new IllegalArgumentException("An argument is null");
        
        if (!isNoun(noun1) || !isNoun(noun2)) 
            throw new IllegalArgumentException("Input is not a WordNet noun");
        
        // create pointers
        Queue<Integer> v = nouns.get(noun1);
        Queue<Integer> w = nouns.get(noun2);
        
        return sc.lengthSubset(v, w);
    }

   // unit testing (required)
    public static void main(String[] args) {
        // call wordNet
        WordNet wordnet = new WordNet(args[0], args[1]);
        
        // call distance() and sca() 
        StdOut.println(wordnet.distance("thoroughbred_race", "ear") + 
                       " \t \t \t - The distance should be = 11");
        StdOut.println(wordnet.sca("linked_genes", "Testudinata") + 
                                   " \t - The sca should be = group grouping");
        
        // call isNoun
        StdOut.println("Does it contain 'dog'? \t" + wordnet.isNoun("dog"));
        
        // call nouns()
        boolean noun = true;
        
        Queue<String> n = (Queue<String>) wordnet.nouns();
        
        for (String k : n) {
            if (!wordnet.isNoun(k)) {
                noun = false;
            }
        }
        StdOut.println(noun);
       
        
    }
}