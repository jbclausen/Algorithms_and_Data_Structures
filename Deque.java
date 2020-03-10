/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 * 
 *  Description: A generalization of a stack and a queue that supports adding 
 *               and removing items from either the front or the back of the 
 *               data structure. The iterator for this class goes through the 
 *               Deque from front to back and does not change the order of the 
 *               items. Adapted code from the textbook or the booksite has a 
 *               citation as a comment above the code.
 * 
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private int size;            // stores the numer of items in the deque
    private Node<Item> first;    // the front of the queue
    private Node<Item> last;     // the back of the queue 
    
    // this code is adapted from Queue.java from the booksite
    // helper linked list class
    private static class Node<Item> {
        private Item item;       // the Item stored by the Node
        private Node<Item> f;    // adjacent Node nearest the front
        private Node<Item> b;    // adjacent Node nearest the back
    }
    
    // construct an empty deque
    public Deque() {

        // initialise the instance variables
        first = null;
        last = null;
        
        size = 0; 
    }
    
    // is the Deque empty?
    public boolean isEmpty() {
        return size == 0; 
    }
    
    // return the number of items on the Deque
    public int size() {
        return size;
    }
    
    // add the item to the front
    public void addFirst(Item item) {
        
        // throw exception if the argument is null
        if (item == null) 
            throw new java.lang.IllegalArgumentException();
        
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;
        
        // if the deque is empty, the new node is the first and the last
        if (isEmpty()) {
            first = newNode;
            last = newNode; }
        
        // otherwise, add it in front of the first
        else { 
            first.f = newNode;
            newNode.b = first;
            first = newNode; }
        
        size++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        
        // throw exception if the argument is null
        if (item == null) 
            throw new java.lang.IllegalArgumentException();
        
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;
        
        // if the deque is empty, the new node is the first and the last
        if (isEmpty()) {
            first = newNode;
            last = newNode; }
        
        // otherwise, add it to the back of the line
        else { 
            last.b = newNode;
            newNode.f = last;
            last = newNode; }
        
        size++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() { 
        
        // throw exception if the Deque is empty 
        if (isEmpty()) 
            throw new NoSuchElementException();
        
        Item returnValue = first.item;
        
        // if the size is 1, empty the deque
        if (size == 1) {
            // empty the deque
            first = null;
            last = null; }
        
        else { 
            Node<Item> temp = first.b;
            // avoid loitering
            first.b = null; 
            temp.f = null; 
            // update pointers
            first = temp; }
        
        size--;
        
        return returnValue;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        
        // throw exception if the Deque is empty 
        if (isEmpty()) 
            throw new NoSuchElementException();
        
        Item returnValue = last.item;
              
        // if the size is 1, empty the deque
        if (size == 1) { 
            // empty the deque    
            first = null; 
            last = null; }
        
        else {
            Node<Item> temp = last.f;
            // avoid loitering 
            last.f = null;
            temp.b = null;
            // update pointers
            last = temp; }
        
        size--;
        
        return returnValue; 
    }
    
    // all the code in this iterator is adapted from precept and Queue.java 
    // which can be found on the (booksite)
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {  
        return new DequeIterator(); 
    }
    
    private class DequeIterator implements Iterator<Item> { 
        private Node<Item> current = first; // the next node to be looked at 
        
        // is there a next item in the iterator?
        public boolean hasNext() {
            return current != null;
        }
        
        // this method is optional in Iterator interface
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        // returns the next item in the iterator (and advances the iterator)
        public Item next() { 
            
            if (!hasNext()) throw new NoSuchElementException();
            
            Item returnValue = current.item;
            current = current.b;
            return returnValue; 
        }
    }
    
    // unit testing, generates a random number and selects methods to test
    public static void main(String[] args) {
        
        Deque<Integer> d1 = new Deque<Integer>(); 
        
        int n = 500;
        
        // test random calls to functions 
        for (int i = 1; i <= n; i++) {
            
            double random = StdRandom.uniform();
            
            if (random < 0.25) { System.out.println("Adding: " + i);
                d1.addFirst(i); }
            
            else if (random < 0.5) { System.out.println("Adding: " + i);
                d1.addLast(i); }
            
            else if (random < 0.75 && d1.size() > 1) 
                System.out.println("Removing: " + d1.removeFirst() + ", " 
                                       + d1.removeLast());
            
            else { System.out.print("The Deque is: ");
                for (int j : d1) {
                System.out.print(j + " ");
            }
            System.out.println(d1.size() + " " + d1.isEmpty()); }
        }
    }
}