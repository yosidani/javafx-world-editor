package mvh.world;

import mvh.shell.*;
import mvh.enums.*;

import java.util.*;

/**
 * A World is a 2D grid of entities, null Spots are floor spots
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public class World {

    /**
     * The local view of world will be 3x3 grid for attacking
     */
    private static final int ATTACK_WORLD_SIZE = 3;
    /**
     * The local view of world will be 5x5 grid for moving
     */
    private static final int MOVE_WORLD_SIZE = 5;
    /**
     * A program-wide random number generator
     */
    public static Random random = new Random(12345);
    /**
     * The storage of entities in World, floor is null, Dead entities can be moved on top of (deleting them essentially from the map)
     */
    private final Entity[][] world;
    /**
     * We track the order that entities were added (this is used to determine order of actions each turn)
     * Entities remain in this list (Even if DEAD) ,unlike the world Entity[][] where they can be moved on top of causing deletion.
     */
    private final ArrayList<Entity> entities;
    /**
     * We use a HashMap to track entity location in world {row, column}
     * We will update this every time an Entity is shifted in the world Entity[][]
     */
    private final HashMap<Entity, Integer[]> locations;
    /**
     * The World starts ACTIVE
     */
    private State state;

    /**
     * A new world of ROWS x COLUMNS in size
     *
     * @param rows    The 1D of the 2D world (rows)
     * @param columns The 2D of the 2D world (columns)
     */
    public World(int rows, int columns) {
        this.world = new Entity[rows][columns];
        this.entities = new ArrayList<>();
        this.locations = new HashMap<>();
        //Starts active
        this.state = State.ACTIVE;
    }

    /**
     * Is this simulation still considered ACTIVE
     *
     * @return True if the simulation still active, otherwise False
     */
    public boolean isActive() {
        return state == State.ACTIVE;
    }

    /**
     * End the simulation, (Set in INACTIVE)
     */
    public void endSimulation() {
        this.state = State.INACTIVE;
    }

    /**
     * Return a sub-world Entity[][] grid focused around given row,col of odd grid size 3,5,7,9...
     *
     * @param size         The size of grid
     * @param centreRow    The row to generate grid around
     * @param centreColumn The column to generate grid around
     * @return World of given size centred around given location
     */
    public World getLocal(int size, int centreRow, int centreColumn) {
        //TODO: Finish
        return null;
    }

    /**
     * Advance the simulation one step
     */
    public void advanceSimulation() {
        //Do not advance if simulation is done
        if (state == State.INACTIVE) {
            return;
        }
        //If not done go through all entities (this will be in order read and added from file)
        for (Entity entity : entities) {
            //If entity is something that is ALIVE, we want to give it a turn to ATTACK or MOVE
            if (entity.isAlive()) {
                //Get location of entity (only the world knows this, the entity does not itself)
                Integer[] location = locations.get(entity);
                //Pull out row,column
                int row = location[0];
                int column = location[1];
                //Determine if/where an entity wants to attack
                World attackWorld3X3 = getLocal(ATTACK_WORLD_SIZE, row, column);
                Direction attackWhere = entity.attackWhere(attackWorld3X3);
                //If I don't attack, then I must be moving
                if (attackWhere == null) {
                    //Figure out where entity wants to move
                    World moveWorld5x5 = getLocal(MOVE_WORLD_SIZE, row, column);
                    Direction moveWhere = entity.chooseMove(moveWorld5x5);
                    //Log moving
                    Menu.println(String.format("%s moving %s", entity.shortString(), moveWhere));
                    //If this move is valid, then move it
                    if (canMoveOnTopOf(row, column, moveWhere)) {
                        moveEntity(row, column, moveWhere);
                    } else {
                        //Otherwise, indicate an invalid attempt to move
                        Menu.println(String.format("%s  tried to move somewhere it could not!", entity.shortString()));
                    }
                } else {
                    //If we are here our earlier attack question was not null, and we are attacking a nearby entity
                    //Get the entity we are attacking
                    Entity attacked = getEntity(row, column, attackWhere);
                    Menu.println(String.format("%s attacking %s in direction %s", entity.shortString(), attackWhere, attacked.shortString()));
                    //Can we attack this entity
                    if (canBeAttacked(row, column, attackWhere)) {
                        //Determine damage using RNG
                        int damage = 1 + World.random.nextInt(entity.weaponStrength());
                        int true_damage = Math.max(0, damage - attacked.armourStrength());
                        Menu.println(String.format("%s attacked %s for %d damage against %d defense for %d", entity.shortString(), attacked.shortString(), damage, attacked.armourStrength(), true_damage));
                        attacked.damage(true_damage);
                        if (!attacked.isAlive()) {
                            locations.remove(attacked);
                            Menu.println(String.format("%s died!", attacked.shortString()));
                        }
                    } else {
                        Menu.println(String.format("%s  tried to attack somewhere it could not!", entity.shortString()));
                    }
                }
            }
        }
        checkActive();
    }

    /**
     * Check if simulation has now ended (only one of two versus Entity types is alive
     */
    private void checkActive() {
        //TODO: checkActive
    }

    /**
     * Move an existing entity
     *
     * @param row    The  row location of existing entity
     * @param column The  column location of existing entity
     * @param d      The direction to move the entity in
     */
    public void moveEntity(int row, int column, Direction d) {
        Entity entity = getEntity(row, column);
        int moveRow = row + d.getRowChange();
        int moveColumn = column + d.getColumnChange();
        this.world[moveRow][moveColumn] = entity;
        this.world[row][column] = null;
        if (!(entity instanceof Wall)) {
            this.locations.put(entity, new Integer[]{moveRow, moveColumn});
        }
    }

    /**
     * Add a new entity
     *
     * @param row    The  row location of new entity
     * @param column The  column location of new entity
     * @param entity The entity to add
     */
    public void addEntity(int row, int column, Entity entity) {
        this.world[row][column] = entity;
        if (!(entity instanceof Wall)) {
            this.entities.add(entity);
            this.locations.put(entity, new Integer[]{row, column});
        }
    }

    /**
     * Get entity at a location
     *
     * @param row    The row of the entity
     * @param column The column of the entity
     * @return The Entity at the given row, column
     */
    public Entity getEntity(int row, int column) {
        return this.world[row][column];
    }

    /**
     * Get entity at a location
     *
     * @param row    The row of the entity
     * @param column The column of the entity
     * @param d      The direction adjust look up towards
     * @return The Entity at the given row, column
     */
    public Entity getEntity(int row, int column, Direction d) {
        return getEntity(row + d.getRowChange(), column + d.getColumnChange());
    }

    /**
     * Remove entity by row and column
     *
     * @param row    Row to remove at
     * @param column Column to remove at
     */
    public void removeEntity(int row, int column) {
        if (!(this.world[row][column] instanceof Wall)) {
            this.locations.remove(world[row][column]);
            this.entities.remove(world[row][column]);
        }
        this.world[row][column] = null;
    }

    /**
     * See if we can move to location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if we can move to that location
     */
    public boolean canMoveOnTopOf(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return true;
        }
        return entity.canMoveOnTopOf();
    }

    /**
     * See if we can move to location
     *
     * @param row    The row to check
     * @param column The column to check
     * @param d      The direction adjust look up towards
     * @return True if we can move to that location
     */
    public boolean canMoveOnTopOf(int row, int column, Direction d) {
        return canMoveOnTopOf(row + d.getRowChange(), column + d.getColumnChange());
    }

    /**
     * See if we can attack entity at a location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if we can attack entity at that location
     */
    public boolean canBeAttacked(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return false;
        }
        return entity.canBeAttacked();

    }

    /**
     * See if we can attack entity at a location
     *
     * @param row    The row to check
     * @param column The column to check
     * @param d      The direction adjust look up towards
     * @return True if we can attack entity at that location
     */
    public boolean canBeAttacked(int row, int column, Direction d) {
        return canBeAttacked(row + d.getRowChange(), column + d.getColumnChange());

    }

    /**
     * See if entity is hero at this location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if entity is a hero at that location
     */
    public boolean isHero(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return false;
        }
        return entity instanceof Hero;
    }

    /**
     * See if entity is monster at this location
     *
     * @param row    The row to check
     * @param column The column to check
     * @return True if entity is a monster at that location
     */
    public boolean isMonster(int row, int column) {
        Entity entity = getEntity(row, column);
        if (entity == null) {
            return false;
        }
        return entity instanceof Monster;
    }

    /**
     * A String version of the world
     *
     * @return String version of the world, '#' for walls, '.' for empty floor, '$' for dead entities
     */
    public String worldString() {
        //TODO: Finish
        return "worldString";
    }

    /**
     * A String version of the world
     * Consists of the game map followed by information about the entities in the world
     *
     * @return String version of the game
     */
    public String gameString() {
        //TODO: Finish
        return "gameString";
    }

    @Override
    public String toString() {
        return gameString();
    }

    /**
     * The rows of the world
     *
     * @return The rows of the world
     */
    public int getRows() {
        return world.length;
    }

    /**
     * The columns of the world
     *
     * @return The columns of the world
     */
    public int getColumns() {
        return world[0].length;
    }

    /**
     * World starts ACTIVE, but will turn INACTIVE after a simulation ends with only one type of Entity still ALIVE
     */
    private enum State {
        ACTIVE, INACTIVE
    }

}