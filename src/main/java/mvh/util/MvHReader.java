package mvh.util;

import mvh.world.*;
import mvh.enums.*;

import java.io.*;
import java.util.*;

/**
 * Class to assist reading in world file
 *
 * @author Jonathan Hudson
 * @version 1.0
 */
public final class MvHReader {

    /**
     * Load the world from the given file
     *
     * @param fileWorld The world file to load
     * @return A World created from the world file
     */
    public static World loadWorld(File fileWorld) {
        try {
            Scanner scanner = new Scanner(fileWorld);

            // get the dimentions of the world
            int World_rows = Integer.parseInt(scanner.nextLine().trim());
            int World_columns = Integer.parseInt(scanner.nextLine().trim());

            // Initialize the empty world
            World world = new World(World_rows, World_columns);

            // go line by line until the end
            while (scanner.hasNextLine()) {
                //If no line continue
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                // get the properties separated
                String[] properties = line.split(",");
                // get the position of the entity
                int row = Integer.parseInt(properties[0]);
                int col = Integer.parseInt(properties[1]);

                // continue if nothing after the position indicators
                if (properties.length == 2) {
                    continue;
                }

                // get the type of the entity
                String type = properties[2];

                // if it is a wall
                if (type.equals("WALL")) {

                    Wall wall = Wall.getWall();
                    world.addEntity(row, col, wall);

                }
                // if it is a Monster, get its symbol, health, weapon
                else if (type.equals("MONSTER")) {

                    char symbol = properties[3].charAt(0);
                    int health = Integer.parseInt(properties[4]);

                    WeaponType weapon;
                    // convert the letter of the weapon into the enum
                    switch (properties[5]) {
                        case "S": weapon = WeaponType.SWORD; break;
                        case "A": weapon = WeaponType.AXE; break;
                        default: weapon = WeaponType.CLUB;
                    }

                    Monster mon = new Monster(symbol, health, weapon);
                    // add the monster to the world
                    world.addEntity(row, col, mon);
                }
                // if it is a hero
                else if (type.equals("HERO")) {

                    char symbol = properties[3].charAt(0);
                    int health = Integer.parseInt(properties[4]);

                    // get weapon and armor type
                    int weapon_strength = Integer.parseInt(properties[5]);
                    int armor = Integer.parseInt(properties[6]);
                    // create the hero object
                    Hero hero = new Hero(symbol, health, weapon_strength, armor);
                    // add it to the world
                    world.addEntity(row, col, hero);
                }
            }
            scanner.close();
            return world;
            // if there is exception
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
