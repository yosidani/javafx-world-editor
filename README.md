# Monsters vs. Heroes (MvH) World Editor

A JavaFX-based graphical application for creating, editing, and managing a 2D grid world containing Heroes, Monsters, and Walls. This tool allows users to visually construct game environments and serialize the data to text files for game engine loading.

## Technical tools
* **Language:** Java 25
* **Framework:** JavaFX 25
* **Tools:** SceneBuilder 25, Git

---

## Features

### World Management
* **Load:** Import an existing world grid from a `.txt` file.
* **Save / Save As:** Export world modifications to existing or new files.
* **Session Management:** Safely quit the application with active state handling.

### Interactive Grid Editor
* Displays the world map as a dynamic visual grid.
* Map borders are automatically rendered as unpathable walls (`#`).
* **Left-Click:** Place a selected entity (Hero, Monster, or Wall) onto a tile.
* **Double-Click:** Remove an entity from a tile (resets to empty).
* **Modal Constraints:** Ensures only one configuration pop-up is active at a time.

### Entity Configuration
When placing a new Hero or Monster, a configuration window prompts for specific stats:
* **Heroes:** Symbol (char), Health (int), Attack (int), Armor (int)
* **Monsters:** Symbol (char), Health (int), Weapon Type (Sword [S], Axe [A], or Club [C])

---

## How to Run

### Running via IDE (IntelliJ IDEA / Eclipse)
1. Clone this repository to your local machine.
2. Open the project folder in your preferred Java IDE.
3. Ensure **Java 25** and **JavaFX 25** are configured in your Project Structure/Build Path.
4. If using VM options for JavaFX, add: 
   `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`
5. Locate the main application class `MvHApp.java` and click **Run**.

---

## File Format Architecture

The editor serializes the world into a custom `.txt` format. Invalid inputs are automatically rejected by the parser.

**Example File Structure:**
```text
3
3
0,0,MONSTER,M,10,S
0,1
0,2
1,0
1,1
1,2
2,0
2,1,WALL
2,2,HERO,H,10,3,1
```

**Parsing Rules:**
* `row,col` → Empty tile
* `row,col,WALL` → Wall block
* `row,col,MONSTER,symbol,health,weapon` → Monster entity
* `row,col,HERO,symbol,health,attack,armor` → Hero entity

## Authors

* Yosias Demoz
* Jonathan Hudson
