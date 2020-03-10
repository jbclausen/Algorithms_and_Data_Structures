/******************************************************************************
 *  Name:    Jens Clausen
 *  NetID:   jclausen
 *  Precept: P05A
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 * 
 *  Description: A randomized queue is similar to a stack or queue, except 
 *               the item removed is chosen uniformly at random from items 
 *               in the data structure. The iterator returns the Queue in a 
 *               random order. Items can be Enqueued like for a normal Queue 
 *               but they will always be dequeued in a random order. Adapted 
 *               code from the textbook or the booksite has a citation as a 
 *               comment above the code.
 * 
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] data; // stores the data provided 
    private int size;    // the number of items in the array
    
    // construct an empty randomized queue
    public RandomizedQueue() {
        data = (Item[]) new Object[2];
        size = 0;
    }
    
    // This code is from the booksite 
    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= size;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = data[i];
        }
        data = temp;
    }
    
    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    // return the number of items on the randomized queue
    public int size() {
        return size;
    }
    
    // add the item
    public void enqueue(Item item) {
        
        if (item == null)
            throw new java.lang.IllegalArgumentException();
        
        // This line is taken from the booksite 
        if (size == data.length) resize(2 * data.length);
        
        data[size++] = item;
    }
    
    // remove and return a random item
    public Item dequeue() {
        
        if (isEmpty()) 
            throw new NoSuchElementException();
        
        int random = StdRandom.uniform(size); 
        Item returnValue = data[random];
        data[random] = data[size - 1];
        data[size - 1] = null; // avoid loitering 
        size--;
        
        // resize if necessary
        if (size != 0 && size <= data.length / 4)
            resize(data.length / 2);
        
        return returnValue;
    }
    
    // return a random item (but do not remove it)
    public Item sample() {
        
       if (isEmpty()) 
            throw new NoSuchElementException();
       
       int random = StdRandom.uniform(size);
       return data[random];
    }
    
    // This code has been adapted from ResizingArrayStack.java on the booksite
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }
    
    private class RandomQueueIterator implements Iterator<Item> {
        private final Item[] iteratorData;  // data[] in a randomised order
        private int i; // keeps track of what items have been seen 
        
        // creates a new array of the same size as data[] and shuffles it
        public RandomQueueIterator() {
            i = 0;
            iteratorData = (Item[]) new Object[size];
            
            for (int j = 0; j < size; j++) {
                iteratorData[j] = data[j]; }
            
            StdRandom.shuffle(iteratorData);
        }
        
        public boolean hasNext()  { return i < size; }
        public void remove() { throw new UnsupportedOperationException(); }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return iteratorData[i++];
        }
    }
    
    // unit testing 
    public static void main(String[] args) {
        
        int n = 500;
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        
        // test random calls to functions 
        for (int i = 1; i <= n; i++) {
            
            double random = StdRandom.uniform();
            
            if (random < 0.25) { System.out.println("Adding: " + i);
                q.enqueue(i); }
            
            else if (random < 0.5) { System.out.println("Adding: " + i);
                q.enqueue(i); }
            
            else if (random < 0.75 && q.size() > 1) 
                System.out.println("Removing: " + q.dequeue() + ", " 
                                       + q.dequeue());
            
            else { System.out.print("The Array is (in random order): ");
                for (int j : q) {
                System.out.print(j + " ");
            }
                
            System.out.println(); }
            
            System.out.println(q.isEmpty() + " " + q.sample());
        
        }
        
        
    }
}  




