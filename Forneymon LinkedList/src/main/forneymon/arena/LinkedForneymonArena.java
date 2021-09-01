package main.forneymon.arena;

import main.forneymon.fmtypes.Forneymon;

/**
 * Contains methods for facing off LinkedForneymonagerie against one another!
 * @author Brittany Steenbergen
 */
public class LinkedForneymonArena {
    
    public static final int BASE_DAMAGE = 5;
    
    /**
     * Conducts a fight between two LinkedForneymonagerie, consisting of the following
     * steps, assisted by Iterators on each LinkedForneymonagerie:
     * <ol>
     *   <li>Forneymon from each LinkedForneymonagerie are paired to fight, in sequence
     *     starting from index 0.</li>
     *  <li>Forneymon that faint (have 0 or less health) are removed from their
     *    respective LinkedForneymonagerie.</li>
     *  <li>Repeat until one or both Forneymonagerie have no remaining Forneymon.</li>     
     * </ol>
     * @param fm1 One of the fighting LinkedForneymonagerie
     * @param fm2 One of the fighting LinkedForneymonagerie
     */
    public static void fight (LinkedForneymonagerie fm1, LinkedForneymonagerie fm2) {
        LinkedForneymonagerie.Iterator iterator1 = fm1.getIterator();
        LinkedForneymonagerie.Iterator iterator2 = fm2.getIterator();
        while (!fm1.empty() || !fm2.empty()) {
            int fm2GivenDamage = BASE_DAMAGE + iterator2.getCurrent().getLevel();
            iterator1.getCurrent().takeDamage(fm2GivenDamage, iterator2.getCurrent().getDamageType());
            
            int fm1GivenDamage = BASE_DAMAGE + iterator1.getCurrent().getLevel();
            iterator2.getCurrent().takeDamage(fm1GivenDamage, iterator1.getCurrent().getDamageType()); 
            
            if (iterator1.getCurrent().getHealth() <= 0) {
                iterator1.removeCurrent();
            }
            if (iterator2.getCurrent().getHealth() <= 0) {
                iterator2.removeCurrent();
            }
            
            if (fm1.empty()) {
                break;
            }
            if (fm2.empty()) {
                break;
            }
            iterator1.next();
            iterator2.next();
        }
    }
    
}
