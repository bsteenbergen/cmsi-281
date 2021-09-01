package main.forneymon.arena;

import java.util.Objects;

import main.forneymon.fmtypes.*;

/**
 * Collections of Forneymon ready to fight in the arena!
 * @author Brittany Steenbergen
 */
public class Forneymonagerie implements ForneymonagerieInterface {

    // Constants
    // ----------------------------------------------------------
    // [!] DO NOT change this START_SIZE for your collection, and
    // your collection *must* be initialized to this size
    private static final int START_SIZE = 4;

    // Fields
    // ----------------------------------------------------------
    private Forneymon[] collection;
    private int size;
    
    
    // Constructor
    // ----------------------------------------------------------
    public Forneymonagerie () {
        this.collection = new Forneymon[START_SIZE];
        this.size = 0;
    }
    
    
    // Methods
    // ----------------------------------------------------------
    
    /**
     *  Checks for an empty collection of Forneymon
     *  which returns true if empty and false otherwise
     */
    public boolean empty () {
        return size == 0;
    }
    
    /**
     * Returns the current size of the Forneymongerie
     */
    public int size () {
        return size;
    }
    
    /**
     * Adds a Forneymon to the end of the collection if that type does not 
     * currently exist within and returns true, or, if it does exist, adds 
     * the levels of the given Forneymon to the one that already exists and 
     * returns false
     * @param toAdd The given Forneymon to be added to the collection
     */
    public boolean collect (Forneymon toAdd) {
        checkAndGrow();
        for (int i = 0; i < this.size; i++) {
            if (toAdd.equals(collection[i])) {
                return false;
            }
            if (toAdd.getFMType().equals(collection[i].getFMType())) {
                collection[i].addLevels(toAdd.getLevel());
                return false;
            }
        }
        collection[size] = toAdd;
        size++;
        return true;
    }
    
    /**
     * Removes the given Forneymon from the collection while still 
     * maintaining proper order
     * @param fmType The given forneymon to be removed
     */
    public boolean releaseType (String fmType) {
        for (int i = 0; i < this.size; i++) {
            if (collection[i].getFMType().equals(fmType)) {
                shiftLeft(i);
                size--;
                return true;
            }
        }
        {return false;}
    }
    
    /**
     * Checks if the given index is valid and if it is,
     * returns the Forneymon at the given index
     * @param index
     */
    public Forneymon get (int index) {
        indexCheck(index, 0, size);
        return collection[index];
    }
    
    /**
     * Removes and returns the Forneymon at the given index, 
     * maintaining relative order of the collection
     * @param index The given index
     */
    public Forneymon remove (int index) {
        indexCheck(index, 0, size);
        shiftLeft(index);
        size--;
        return collection[index];
    }
    
    /**
     * Returns the index of a given Forneymon in the collection, 
     * or if it does not exist, returns -1 
     * @param fmType The given Forneymon whose index will be returned
     */
    public int getTypeIndex (String fmType) {
        for (int i = 0; i < this.size; i++) {
            if (fmType.equals(collection[i].getFMType())) {
                return i;
            }
        }
        {return -1;}
    }
    
    /**
     * If the given Forneymon is found within the collection, 
     * returns true
     * @param toCheck The given Forneymon being checked
     */
    public boolean containsType (String toCheck) {
        for (int i = 0; i < this.size; i++) {
            if (collection[i].getFMType().contains(toCheck)) {
                return true;
            }
        }
        {return false;}        
    }
    
    /**
     * Swaps the contents of the calling Forneymonagerie
     * to the other that is specified
     * @param other The other Forneymonagerie
     */
    public void trade (Forneymonagerie other) {
        Forneymon[] tempCollection = new Forneymon[size];
        tempCollection = this.collection;
        this.collection = other.collection;
        other.collection = tempCollection; 
        
        int tempSize = this.size;
        this.size = other.size;
        other.size = tempSize;
    }
    
    /**
     * Moves the given Forneymon from it's current position in the 
     * Forneymonagerie to the one specified by the index, shifting the
     * items to preserve relative indexing
     * @param fmType Forneymon to be moved
     * @param index The position that the Forneymon is to be relocated
     */
    public void rearrange (String fmType, int index) {
        indexCheck(index, 0, size-1);
        int indexFM = this.getTypeIndex(fmType);
        Forneymon tempFM = collection[indexFM];
        if (index > indexFM) {
            shiftLeftRange(index, indexFM);
        }
        if (index < indexFM) {
            shiftRightRange(index, indexFM);
        }
        collection[index] = tempFM;
    }   
    
    @Override
    public Forneymonagerie clone () {
        Forneymonagerie newCopy = new Forneymonagerie();
        for (int i = 0; i < this.size; i++) {
            newCopy.collect(collection[i].clone());
        }
        return newCopy;
    } 
    
    @Override
    public boolean equals (Object other) {
        if (this.getClass() != other.getClass()) {
            return false;
        }
        Forneymonagerie otherFM = (Forneymonagerie) other;
        if (this.size != otherFM.size) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if (!(collection[i].equals(otherFM.collection[i]))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode () {
        // This one is a freebie, no changes necessary here
        return Objects.hash(this.collection, this.size);
    }
    
    @Override
    public String toString () {
        // This one's also freebie -- you don't have to add or
        // change anything here!
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = collection[i].toString();
        }
        return "[ " + String.join(", ", result) + " ]";
    }
    
    
    // Private helper methods
    // ----------------------------------------------------------
    
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
    
    /*
     * Expands the size of the list whenever it is at
     * capacity
     */
    private void checkAndGrow () {
        if (size < collection.length) {
            return;
        }        
        Forneymon[] newItems = new Forneymon[collection.length * 2];
        
        for (int i = 0; i < collection.length; i++) {
            newItems[i] = collection[i];
        }
        
        collection = newItems;
    }
 
    /*
     * Shifts all elements to the right of the given
     * index one left
     */
    private void shiftLeft (int index) {
        for (int i = index; i < size-1; i++) {
            collection[i] = collection[i+1];
        }
    }
    
    /*
     * Shifts specified elements to the right of the given
     * index one left 
     * @param startIndex The index to start at
     * @param stopIndex The index to stop at
     */
    private void shiftLeftRange (int stopIndex, int startIndex) {
        for (int i = startIndex; i < stopIndex; i++) {
            collection[i] = collection[i+1];
        }
    }
    
    /*
     * Shifts specified elements to the left of the given
     * index one right
     * @param startIndex The index to start at
     * @param stopIndex The index to stop at
     */
    private void shiftRightRange (int startIndex, int stopIndex) {
        for (int i = stopIndex; i > startIndex; i--) {
            collection[i] = collection[i-1];
        }
    }
}
