package mvh.world;

import mvh.enums.*;
import mvh.enums.*;

/**
 * A Monster is an Entity with a set ARMOR STRENGTH and a user provided WEAPON TYPE
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class Monster extends Entity {

    /**
     * The set armour strength of a Monster
     */
    private static final int MONSTER_ARMOUR_STRENGTH = 2;

    /**
     * The user provided weapon type
     */
    private final WeaponType weaponType;

    /**
     * A Monster has regular health and symbol as well as a weapon type
     *
     * @param symbol     Symbol for map to show Monster
     * @param health     Health of Monster
     * @param weaponType The weapon type of the Monster
     */
    public Monster(char symbol, int health, WeaponType weaponType) {
        super(symbol, health);
        this.weaponType = weaponType;
    }

    /**
     * Gets Monster's weapon type
     *
     * @return The Monster's weapon type
     */
    public WeaponType getWeaponType() {
        return this.weaponType;
    }

    /**
     * The weapon strength of monster is from their weapon type
     *
     * @return The weapon strength of monster is from their weapon type
     */
    @Override
    public int weaponStrength() {
        return weaponType.getWeaponStrength();
    }

    /**
     * The armour strength of monster is from the stored constant
     *
     * @return The armour strength of monster is from the stored constant
     */
    @Override
    public int armourStrength() {
        return MONSTER_ARMOUR_STRENGTH;
    }

    /**
     * Can only be moved on top of if dad
     *
     * @return isDead()
     */
    @Override
    public boolean canMoveOnTopOf() {
        return isDead();
    }

    /**
     * Can only be attacked if alive
     *
     * @return isAlive()
     */
    @Override
    public boolean canBeAttacked() {
        return isAlive();
    }

    @Override
    public String toString() {
        return super.toString() + "\t" + weaponType;
    }

    //TODO: chooseMove (not your job for A3)
    @Override
    public Direction chooseMove(World local) {
        return Direction.STAY;
    }

    //TODO: attackWhere (not your job for A3)
    @Override
    public Direction attackWhere(World local) {
        return Direction.STAY;
    }
}
