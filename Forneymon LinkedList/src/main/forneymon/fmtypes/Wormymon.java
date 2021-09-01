package main.forneymon.fmtypes;

/**
 * A worm-like Forneymon who thrives on DAMPY places and loves
 * to munch on some LEAFY, but will suffer from any ZAPPY damage
 * @author Brittany Steenbergen
 *
 */
public class Wormymon extends Forneymon {
    
    public static final int START_HEALTH = 15;
    public static final int DMG_MODIFIER = 5;
    public static final DamageType DT = DamageType.WORMY;
    
    /**
     * Constructs a new Wormymon at the given level
     * @param level The level of this Wormymon
     */
    public Wormymon (int level) {
        super(START_HEALTH, DT, level);
    }
    
    /**
     * Copy constructor for Wormymon that creates a deep-copy
     * of the given other toCopy
     * @param toCopy The other to copy from
     */
    public Wormymon (Wormymon toCopy) {
        super(toCopy.getHealth(), DT, toCopy.getLevel());
    }
    
    /**
     * @see Forneymon
     * Wormymon take bonus LEAFY and DAMPY damage, but
     * reduced ZAPPY damage
     */
    @Override
    public int takeDamage (int dmg, DamageType type) {
        if (type == DamageType.DAMPY) {
            dmg += DMG_MODIFIER;
        }
        if (type == DamageType.LEAFY) {
            dmg += DMG_MODIFIER;
        }
        if (type == DamageType.ZAPPY) {
            dmg -= DMG_MODIFIER;
        }
        return super.takeDamage(dmg, type);
    }
    
    @Override
    public Wormymon clone() {
        return new Wormymon(this);
    }
    
}
