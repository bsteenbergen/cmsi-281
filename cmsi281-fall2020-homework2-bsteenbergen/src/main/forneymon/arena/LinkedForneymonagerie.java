package main.forneymon.arena;

import java.util.Objects;

import main.forneymon.fmtypes.*;


/**
 * Collections of Forneymon ready to fight in the arena!
 * @author Brittany Steenbergen
 */
public class LinkedForneymonagerie implements ForneymonagerieInterface {

    // Fields
    // -----------------------------------------------------------
    private Node sentinel;
    private int size, modCount;
    
    
    // Constructor
    // -----------------------------------------------------------
    public LinkedForneymonagerie () {
        // [!] Leave this constructor as-is, you may not modify!
        this.size = this.modCount = 0;
        this.sentinel = new Node(null);
        this.sentinel.next = this.sentinel;
        this.sentinel.prev = this.sentinel;
    }
    
    
    // Methods
    // -----------------------------------------------------------
    
    /**
     *  Checks if LinkedForenymonagerie is empty
     *  which returns true if empty and false otherwise
     *  @return Whether LinkedForneymonagerie is empty
     */
    public boolean empty () {
        return size == 0;
    }
    
    /**
     * Returns the current size of the LinkedForneymongerie
     * @return The size of the LinkedForneymonagerie
     */
    public int size () {
        return this.size;
    }
    
    /**
     * Adds a node containing the given Forneymon to the end of the collection if that type does not 
     * currently exist within and returns true, or, if it does exist, adds 
     * the levels of the given Forneymon to the one that already exists and 
     * returns false
     * @param toAdd The given Forneymon to be added to the collection
     * @return Whether the new node was added to the end
     */
    public boolean collect (Forneymon toAdd) {
        Node toCollect = new Node(toAdd);
        int toAddIndex = getTypeIndex(toAdd.getFMType());
        
        if (toAddIndex != -1) {
            if (get(toAddIndex) == toAdd) {
                return false;
            }
            else {
                get(toAddIndex).addLevels(toAdd.getLevel());
                modCount++;
            }
            return false;
        }
        else {
            addNode(this.sentinel, toCollect);
            return true;
        }
    }
    
    /**
     * Removes the given node from the list while still 
     * maintaining proper order
     * @param fmType The given forneymon to be removed
     * @return Whether the node was released
     */
    public boolean releaseType (String fmType) {
        int fmIndex = getTypeIndex(fmType);
        if (fmIndex == -1) {
            return false;
        }
        else {
            remove(fmIndex);
            return true;
        }
    }
    
    /**
     * Checks if the given index is valid and if it is,
     * returns the Forneymon of the node at the given index
     * @param index The index of the node to get the Forneymon from
     * @return The Forneymon at that index
     */
    public Forneymon get (int index) {
        indexCheck(index, 0, size);
        Node current = this.sentinel.next;
        while (index > 0) {
            current = current.next;
            index--;
        }
        return current.fm;
    }
    
    /**
     * Removes and returns the Forneymon of the node at the given index, 
     * maintaining relative order of the list
     * @param index The given index
     * @return The Forneymon of the node that was removed
     */
    public Forneymon remove (int index) {
       indexCheck(index, 0, size);
       Node current = this.sentinel.next,
               prev = this.sentinel;
           
       while (current != this.sentinel && index > 0) {
           prev = current;
           current = current.next;
           index--;
       }       
       removeNode(current);
       return current.fm;
    }
    
    /**
     * Returns the index of a node with the given Forneymon in the list, 
     * or if it does not exist, returns -1 
     * @param fmType The given Forneymon whose index will be returned
     * @return The index of the node containing the given Forneymon, or -1 if
     * the Forneymon is not in the list
     */
    public int getTypeIndex (String fmType) {     
        Node n = this.sentinel.next;
        int index = 0;
        while (n != this.sentinel) { 
            if (fmType.equals(n.fm.getFMType())) {
                return index;
            }
            index++;
            n = n.next;
        }
        return -1;
    }
    
    /**
     * If the given Forneymon is found in a node within the list, 
     * returns true
     * @param toCheck The given Forneymon being checked
     * @return Whether the type is in the list
     */
    public boolean containsType (String toCheck) {
        return getTypeIndex(toCheck) != -1;
    }
    
    /**
     * Swaps the contents of the calling LinkedForneymonagerie
     * to the other that is specified
     * @param other The other LinkedForneymonagerie
     */
    public void trade (LinkedForneymonagerie other) {
        LinkedForneymonagerie holderList = new LinkedForneymonagerie();
        holderList.sentinel = this.sentinel;
        this.sentinel = other.sentinel;
        other.sentinel = holderList.sentinel;
        
        int holderSize = this.size;
        this.size = other.size;
        other.size = holderSize;
        
        int holderModCount = this.modCount;
        this.modCount = other.modCount;
        other.modCount = holderModCount;
        
        this.modCount++;
        other.modCount++;
    }
    
    /**
     * Moves the given node containing the Forneymon from it's current position in the 
     * LinkedForneymonagerie to the one specified by the index, preserving the order
     * of the list
     * @param fmType Forneymon to be moved
     * @param index The position that the node with the Forneymon is to be relocated
     */
    public void rearrange (String fmType, int index) {
        indexCheck(index, 0, size);
        int toRearrange = getTypeIndex(fmType);
        Node addIn = new Node(remove(toRearrange));
        
        Node current = this.sentinel.next,
                prev = this.sentinel;
        
        while (current != this.sentinel && index > 0) {
            prev = current;
            current = current.next;
            index--;
        }        
        addNode(current, addIn);
    }
    
    /**
     * Creates a new iterator if the LinkedList is not empty
     * @return The new iterator
     */
    public LinkedForneymonagerie.Iterator getIterator () {
        if (this.size == 0) {
            throw new IllegalStateException();
        }
        return new Iterator(this);
    }
    
    /**
     * Creates a deep clone of the LinkedForneymonagerie
     * @return The clone of the list
     */
    @Override
    public LinkedForneymonagerie clone () {
        LinkedForneymonagerie toCopy = new LinkedForneymonagerie();
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            toCopy.collect(n.fm.clone());
        }
        return toCopy;
    }
    
    /**
     * Checks to see whether the lists are equal, that is, containing equal
     * Forneymon in the same order as this one
     * @return Whether the lists are equal
     */
    @Override
    public boolean equals (Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }        
        LinkedForneymonagerie otherFM = (LinkedForneymonagerie) other;
        if (this.size != otherFM.size) {
            return false;
        }
        
        int index = 0;
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            if (!n.fm.equals(otherFM.get(index))) {
                return false;
            }
            index++;
        }
        return true;
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(this.sentinel, this.size, this.modCount);
    }
    
    @Override
    public String toString () {
        String[] result = new String[size];
        int i = 0;
        for (Node curr = this.sentinel.next; curr != this.sentinel; curr = curr.next, i++) {
            result[i] = curr.fm.toString();
        }
        return "[ " + String.join(", ", result) + " ]";
    }
    
    
    // Private helper methods
    // -----------------------------------------------------------

    /**
     * Ensures that the requested index is within a legal range
     * @param index The index to check
     * @param lower The legal lower bound (exclusive)
     * @param upper The legal upper bound (inclusive)
     */
    private void indexCheck (int index, int lower, int upper) {
        if (index < lower || index >= upper) {
            throw new IllegalArgumentException("Index out of legal bounds");
        }
    }
    
    /**
     * Removes the node that is being pointed to, updating the size and modCount
     * @param current The node to be removed
     */
    private void removeNode (Node current) {
        current = current.prev;
        current.next = current.next.next;
        current.next.prev = current;
        
        this.size--;
        this.modCount++;
    }
    
    /**
     * Adds the node that is being pointed to, updating the size and modCount
     * @param current The node to be added
     */
    private void addNode (Node current, Node toAdd) {
        toAdd.prev = current.prev;
        toAdd.next = current;
        
        current.prev.next = toAdd;
        current.prev = toAdd;
       
        this.modCount++;
        this.size++;
    }
    
    // Inner Classes
    // -----------------------------------------------------------
    
    public class Iterator {
        
        // Fields
        // -----------------------------------------------------------
        private LinkedForneymonagerie host;
        private Node current;
        private int itModCount;
        
        // Constructor
        // -----------------------------------------------------------
        Iterator (LinkedForneymonagerie host) {
            current = host.sentinel.next;
            this.host = host;
            itModCount = host.modCount;
        }
        
        // Methods
        // -----------------------------------------------------------
        
        /**
         * Checks if the iterator has reached the end of the list
         * @return Whether the iterator is at the end
         */
        public boolean atEnd () {
            itValidityCheck();  
            return current.next == host.sentinel;
        }
        
        /**
         * Checks if the iterator is at the beginning of the list
         * @return Whether the iterator is at the start
         */
        public boolean atStart () {
            itValidityCheck();  
            return current.prev == host.sentinel;
        }
        
        /**
         * Checks if the iterator is valid and throws an exception if not
         * @return Whether the iterator is valid
         */
        public boolean isValid () {
            return itModCount == host.modCount && !host.empty();
        }
        
        /**
         * Gets the Forneymon from the node that the iterator is pointing to
         * @return The current Forneymon
         */
        public Forneymon getCurrent () {
            itValidityCheck();  
            return current.fm;
        }

        /**
         * Moves the iterator to the next node
         */
        public void next () {
            itValidityCheck();  
            if (current.next == host.sentinel) {
                current = current.next;
            }
            current = current.next;
        }
        
        /**
         * Moves the iterator to the previous node
         */
        public void prev () {
            itValidityCheck();  
            if (current.prev == host.sentinel) {
                current = current.prev;
            }
            current = current.prev;
        }
        
        /**
         * Removes the current node from the host list and moves the iterator to the node
         * that was before the one removed
         * @return The removed node
         */
        public Forneymon removeCurrent () {
            itValidityCheck();         
            Forneymon removed = getCurrent();

            removeNode(current);
            current = current.prev;
            
            itModCount++;
            return removed;
        }
        
        // Private helper methods
        // -----------------------------------------------------------
        
        /**
         * Checks if the iterator is valid and throws and exception if it
         * is not
         */
        private void itValidityCheck () {
            if (!isValid()) {
                throw new IllegalStateException();
            }
        }
    }
    
    
    private class Node {
        
        // Fields
        // -----------------------------------------------------------
        Node next, prev;
        Forneymon fm;
        
        // Constructor
        // -----------------------------------------------------------
        Node (Forneymon fm) {
            this.fm = fm;
        }
    }
    
}
