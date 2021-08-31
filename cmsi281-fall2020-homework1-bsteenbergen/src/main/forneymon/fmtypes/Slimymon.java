package main.forneymon.fmtypes;

/**
 * An especially moist Forneymon who loves all things DAMPY
 * and WORMY but takes extra damage from BURNY
 * @author Brittany Steenbergen
 *
 */
public class Slimymon extends Forneymon {
    
    public static final int START_HEALTH = 10;
    public static final int DMG_MODIFIER = 3;
    public static final DamageType DT = DamageType.SLIMY;
    
    /**
     * Constructs a new Slimymon at the given level
     * @param level The level of this Slimymon
     */
    public Slimymon (int level) {
        super(START_HEALTH, DT, level);
    }
    
    /**
     * Copy constructor for Slimymon that creates a deep-copy
     * of the given other toCopy
     * @param toCopy The other to copy from
     */
    public Slimymon (Slimymon toCopy) {
        super(toCopy.getHealth(), DT, toCopy.getLevel());
    }
    
    /**
     * @see Forneymon
     * Slimymon take bonus WORMY and DAMPY damage, but
     * reduced BURNY damage
     */
    @Override
    public int takeDamage (int dmg, DamageType type) {
        if (type == DamageType.DAMPY) {
            dmg += DMG_MODIFIER;
        }
        if (type == DamageType.WORMY) {
            dmg += DMG_MODIFIER;
        }
        if (type == DamageType.BURNY) {
            dmg -= DMG_MODIFIER;
        }
        return super.takeDamage(dmg, type);
    }
    
    @Override
    public Slimymon clone() {
        return new Slimymon(this);
    }
    
}
