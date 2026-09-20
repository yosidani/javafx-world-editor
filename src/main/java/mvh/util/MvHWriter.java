package mvh.util;


import mvh.world.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author Yosias Demoz
 * @since April 4, 2026
 */

/**
 * Class to assist writing a .txt file from the world
 */
public class MvHWriter {
    /**
     * Write a .txt file from the world
     *
     * @param file  The file to write to
     * @param world The world to write from(read)
     */
    public static void saveWorld(File file, World world) {
        // set up writer
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println(world.getRows());
            writer.println(world.getColumns());

            for (int i = 0; i < world.getRows(); i++) {
                for (int j = 0; j < world.getColumns(); j++) {
                    // set up line builder
                    StringBuilder line = new StringBuilder();
                    //append the row,column
                    line.append(i).append(",").append(j);
                    //get entity at that row column and append its name followed by coma and properties
                    Entity entity = world.getEntity(i, j);
                    if (entity != null) {
                        if(entity instanceof Wall) {
                            line.append(",WALL");

                        } else if (entity instanceof Hero){
                            line.append(",HERO").append(",").append(entity.getSymbol()).append(",").append(entity.getHealth()).append(",").append(entity.weaponStrength()).append(",").append(entity.armourStrength());

                        } else if (entity instanceof Monster) {
                            Monster mon = (Monster) entity;
                            line.append(",MONSTER").append(",").append(mon.getSymbol()).append(",").append(mon.getHealth()).append(",").append(mon.getWeaponType().toString().charAt(0));
                        }
                    }
                    // write the line
                    writer.println(line.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
