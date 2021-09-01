package main.forneymon.arena;

import main.forneymon.fmtypes.Forneymon;

/**
 * Contains methods for facing off Forneymonagerie against one another!
 * @author Brittany Steenbergen
 */
public class ForneymonArena {
    
    public static final int BASE_DAMAGE = 5;
    
    /**
     * Conducts a fight between two Forneymonageries, consisting of the following
     * steps:
     * <ol>
     *   <li>Forneymon from each Forneymonagerie are paired to fight, in sequence
     *     starting from index 0.</li>
     *  <li>Forneymon that faint (have 0 or less health) are removed from their
     *    respective Forneymonagerie.</li>
     *  <li>Repeat until one or both Forneymonagerie have no remaining Forneymon.</li>     
     * </ol>
     * @param fm1 One of the fighting Forneymonagerie
     * @param fm2 One of the fighting Forneymonagerie
     */
    public static void fight (Forneymonagerie fm1, Forneymonagerie fm2) {
        int fm1Index = 0;
        int fm2Index = 0;
        while (!fm1.empty() || !fm2.empty()) {
            int fm2GivenDamage = BASE_DAMAGE + fm2.get(fm2Index).getLevel();
            fm1.get(fm1Index).takeDamage(fm2GivenDamage, fm2.get(fm2Index).getDamageType());
            
            int fm1GivenDamage = BASE_DAMAGE + fm1.get(fm1Index).getLevel();
            fm2.get(fm2Index).takeDamage(fm1GivenDamage, fm1.get(fm1Index).getDamageType()); 
            
            if (fm1.get(fm1Index).getHealth() <= 0) {
                fm1.remove(fm1Index);
                fm1Index--;
            }
            if (fm2.get(fm2Index).getHealth() <= 0) {
                fm2.remove(fm2Index);
                fm2Index--;
            }
            
            if (fm1.empty()) {
                break;
            }
            if (fm2.empty()) {
                break;
            }
            fm1Index = (fm1Index + 1) % fm1.size();
            fm2Index = (fm2Index + 1) % fm2.size();
        }   
    }
}
