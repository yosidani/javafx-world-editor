# Monsters vs Heroes (MvH) World Editor

## Overview

This is a JavaFX-based graphical application for creating, editing, and managing a world containing Heroes, Monsters, and Walls.

Users can:

* Load an existing world from a `.txt` file
* Create and modify the world visually
* Add or remove entities dynamically
* Save changes back to a file

---

## Features

### World Management

* Load world from file
* Save world to existing file
* Save As to a new file
* Quit application safely

### Grid Editor

* Displays world as a grid
* Borders are automatically rendered as walls (`#`)
* Click interactions:

  * **Single click** → place entity (based on active mode)
  * **Double click** → remove entity (set to null)

---

## Hero & Monster Creation

When selecting:

* **Add Hero**
* **Add Monster**

A popup window appears allowing configuration.

### Hero Inputs

* Symbol (char)
* Health (int)
* Attack (int)
* Armor (int)

### Monster Inputs

* Symbol (char)
* Health (int)
* Weapon Type:

  * Sword (S)
  * Axe (A)
  * Club (C)

---

## File Format

Example world file:

```
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

### Format Rules

  * `row,col` → empty
  * `row,col,WALL`
  * `row,col,MONSTER,symbol,health,weapon`
  * `row,col,HERO,symbol,health,attack,armor`

---

## How to Run

### Option 1: With file argument

---

### Option 2: Manual load

---

### Option 3: Drag and Drop

* Drag a `.txt` file onto the middle grid area

---

## Notes

* Grid automatically includes boundary walls
* Invalid inputs may be ignored or rejected
* Only one popup can be active at a time (modal behavior)

---

## Technical tools

* JavaFX 25
* Java 25
* Git
* Scenebuilder 25

## Author

* Yosias Demoz
* email - yosias.demoz@ucalgary.ca
