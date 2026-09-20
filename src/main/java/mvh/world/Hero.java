package mvh.world;

import mvh.enums.*;

/**
 * A Monster is an Entity with a user provide WEAPON STRENGTH and ARMOR STRENGTH
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class Hero extends Entity {

    /**
     * The user provided weapon strength
     */
    private final int weaponStrength;

    /**
     * The user provided armour strength
     */
    private final int armourStrength;

    /**
     * A Hero has regular health and symbol as well as a weapon strength and armour strength
     *
     * @param symbol         Symbol for map to show hero
     * @param health         Health of hero
     * @param weaponStrength The weapon strength of the hero
     * @param armourStrength  The armour strength of the hero
     */
    public Hero(char symbol, int health, int weaponStrength, int armourStrength) {
        super(symbol, health);
        this.weaponStrength = weaponStrength;
        this.armourStrength = armourStrength;
    }

    /**
     * The weapon strength of monster is from user value
     *
     * @return The weapon strength of monster is from user value
     */
    @Override
    public int weaponStrength() {
        return weaponStrength;
    }

    /**
     * The armour strength of monster is from user value
     *
     * @return The armour strength of monster is from user value
     */
    @Override
    public int armourStrength() {
        return armourStrength;
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
        return super.toString() + "\t" + weaponStrength + "\t" + armourStrength;
    }

    //chooseMove
    @Override
    public Direction chooseMove(World local) {
        return Direction.STAY;
    }

    //attackWhere
    @Override
    public Direction attackWhere(World local) {
        return Direction.STAY;
    }

}
