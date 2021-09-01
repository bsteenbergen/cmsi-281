package main.textfill;

import java.util.*;

/**
 * A ternary-search-tree implementation of a text-autocompletion
 * trie, a simplified version of some autocomplete software.
 * @author Brittany Steenbergen
 */
public class TernaryTreeTextFiller implements TextFiller {

    // -----------------------------------------------------------
    // Fields
    // -----------------------------------------------------------
    private TTNode root;
    private int size;
    
    // -----------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------
    public TernaryTreeTextFiller () {
        this.root = null;
        this.size = 0;
    }
    
    
    // -----------------------------------------------------------
    // Methods
    // -----------------------------------------------------------
    
    /**
     * Method to return the number of stored terms in the TextFiller, equivalent
     * to the number of word ends
     * @return The number of stored terms
     */
    public int size () {
       return this.size;
    }

    /**
     * Checks to see if the TextFiller is empty
     * @return True if the TextFiller has no terms stored inside, false otherwise
     */
    public boolean empty () {
       return (this.root == null);
    }
    
    /**
     * Adds the given term to the TextFiller, and does nothing if the string already
     * exists inside
     * @param toAdd The given term to add into the TextFiller
     */
    public void add (String toAdd) {
        toAdd = normalizeTerm(toAdd);
        this.root = add(this.root, toAdd);
    }
    
    /**
     * Checks to see if the given term exists in the TextFiller
     * @param query The term to be checked
     * @return True if query exists in the TextFiller, false otherwise
     */
    public boolean contains (String query) {
        query = normalizeTerm(query);
        return contains(this.root, query, 0);
    }
    
    /**
     * Returns the first search term contained in the TextFiller that possesses
     * the query as a prefix, or null if the prefix does not exist inside
     * @param query The given term to check the prefix of
     * @return The first term with the matching prefix
     */
    public String textFill (String query) {
        query = normalizeTerm(query);
        return textFill(this.root, query, 0);
    }
    
    /**
     * Returns an ArrayList of strings in the TextFiller that is sorted 
     * alphabetically
     * @return An ArrayList of the stored strings
     */
    public List<String> getSortedList () {
        ArrayList<String> result = new ArrayList<>();
        getSortedList(this.root, result, "");
        return result;
    }
    
    // -----------------------------------------------------------
    // Helper Methods
    // -----------------------------------------------------------
    
    /**
     * Normalizes a term to either add or search for in the tree,
     * since we do not want to allow the addition of either null or
     * empty strings within, including empty spaces at the beginning
     * or end of the string (spaces in the middle are fine, as they
     * allow our tree to also store multi-word phrases).
     * @param s The string to sanitize
     * @return The sanitized version of s
     */
    private String normalizeTerm (String s) {
        // Edge case handling: empty Strings illegal
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
        return s.trim().toLowerCase();
    }
    
    /**
     * Given two characters, c1 and c2, determines whether c1 is
     * alphabetically less than, greater than, or equal to c2
     * @param c1 The first character
     * @param c2 The second character
     * @return
     *   - some int less than 0 if c1 is alphabetically less than c2
     *   - 0 if c1 is equal to c2
     *   - some int greater than 0 if c1 is alphabetically greater than c2
     */
    private int compareChars (char c1, char c2) {
        return Character.toLowerCase(c1) - Character.toLowerCase(c2);
    }
    
    // [!] Add your own helper methods here!
    
    /**
     * Recursively adds a string into the TextFiller, doing nothing if it
     * already exists within
     * @param current The current node
     * @param toAdd The string to be added
     * @return The TTNode of the added string
     */
    private TTNode add (TTNode current, String toAdd) {
        if (toAdd.isEmpty()) {
            return current;
        }
        if (current == null) {
            current = new TTNode(toAdd.charAt(0), false);
            
            if (toAdd.length() > 1) {                   
                current.mid = add(current.mid, toAdd.substring(1));
            }
            else {
               current.wordEnd = true;
               size++;
               return current;
            } 
        }
        
        int compareChars = compareChars(toAdd.charAt(0), current.letter);
        if (compareChars == 0) {
            if (toAdd.length() == 1) {
                current.wordEnd = true;
            }
            current.mid = add(current.mid, toAdd.substring(1));
        }        
        else if (compareChars < 0) {
            current.left = add(current.left, toAdd);
        } 
        else {
            current.right = add(current.right, toAdd);
        }
        return current;
    }
    
    /**
     * Recursively checks to see if the given string exists in the TextFiller
     * @param current The current node
     * @param query The string to be checked
     * @param index The index of the char that is being checked
     * @return True if the string exists, false otherwise
     */
    private boolean contains (TTNode current, String query, int index) {
        if (current == null || index >= query.length()) {
            return false;
        }

        int compareChars = compareChars(query.charAt(index), current.letter);
        if (compareChars == 0) {
            if (current.wordEnd && index == query.length() - 1) {
                return true;
            }
            return contains(current.mid, query, index + 1);
        }
        else if (compareChars < 0) {
            return contains(current.left, query, index);
        }
        else {
            return contains(current.right, query, index);
        }        
    }
    
    /**
     * Recursively returns the first term contained in the TextFiller that possesses
     * the query as a prefix, or null if it does not exist inside
     * @param current The current node
     * @param query The given prefix
     * @return The term with the matching prefix
     */
    private String textFill (TTNode current, String query, int index) {
        if (current == null) {
            return null;
        }          
        if (contains(current, query, 0)) {
            return query;
        }   
        
        if (index >= query.length()) {
            if (current.wordEnd) {
                query += current.letter;
                return query;
            }
            else if (current.mid != null && !current.wordEnd) {
                return textFill(current.mid, query += current.letter, index + 1);
            }
            else if (current.left != null && !current.wordEnd) {
                return textFill(current.left, query += current.letter, index + 1);
            }
            else if (current.right != null && !current.wordEnd) {
                return textFill(current.right, query += current.letter, index + 1);
            }
            else {
                return null;
            }
        }
        
        int compareChars = compareChars(query.charAt(index), current.letter);
        if (compareChars == 0) {
            return textFill(current.mid, query, index + 1);
        }
        else if (compareChars < 0) {
            return textFill(current.left, query, index);
        }
        else {
            return textFill(current.right, query, index);
        }
    }
    
    /**
     * Recursive method to return an ArrayList of the alphabetically sorted
     * strings in the TextFiller
     * @param current The current node
     * @param result The sorted ArrayList
     * @param s The string that will get added to the list
     */
    private void getSortedList (TTNode current, ArrayList<String> result, String s) {
        if (current == null) {
            return;
        }
        
        getSortedList(current.left, result, s);
        
        if (current.wordEnd) {
            result.add(s + current.letter);
        }
        
        getSortedList(current.mid, result, s + current.letter);
        getSortedList(current.right, result, s);
    }
    
    // -----------------------------------------------------------
    // Extra Credit Methods
    // -----------------------------------------------------------
    
    public void add (String toAdd, int priority) {
        throw new UnsupportedOperationException();
    }
    
    public String textFillPremium (String query) {
        throw new UnsupportedOperationException();
    }
    
    
    // -----------------------------------------------------------
    // TTNode Internal Storage
    // -----------------------------------------------------------
    
    /**
     * Internal storage of autocompleter search terms
     * as represented using a Ternary Tree with TTNodes
     */
    private class TTNode {
        
        boolean wordEnd;
        char letter;
        TTNode left, mid, right;
        
        /**
         * Constructs a new TTNode containing the given character
         * and whether or not it represents a word-end, which can
         * then be added to the existing tree.
         * @param c Letter to store at this node
         * @param w Whether or not this is a word-end
         */
        TTNode (char c, boolean w) {
            letter  = c;
            wordEnd = w;
        }
        
    }
    
}
