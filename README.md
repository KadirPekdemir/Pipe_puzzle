## Pipe Puzzle Game

This project is a desktop puzzle game developed in Java using JavaFX. The game consists of pipes that need to be connected in the correct order to create a continuous flow from the start point to the end point.

### How the Game Works

1. **Game Initialization:**

   - The game loads map data from a CSV file where each cell is represented by type and property values.
   - The grid is a 4x4 matrix of `Tiles`.

2. **Tile Types:**

   - `Starter`: Represents the starting pipe where the flow begins.
   - `End`: Represents the ending pipe where the flow should reach.
   - `Pipe`: Movable pipes that players can drag and swap.
   - `PipeStatic`: Fixed pipes that cannot be moved.

3. **Gameplay Features:**

   - The player can drag pipes to adjacent empty cells.
   - Pipes can only be moved if they are not static or starter/end tiles.
   - The game automatically checks if the pipes are connected correctly from the start to the end.

4. **Saving and Loading Progress:**

   - The game progress is saved to `save.dat`.
   - When the game is restarted, the number of unlocked levels is loaded from the file.

5. **Move Counter:**

   - Each move made by the player is counted and displayed using a `Label` component.

### How to Run the Project

1. Clone the repository.
2. Install Java and JavaFX.
3. Compile and run the project using your preferred IDE.

### Folder Structure

```
|-- src
|   |-- com/example/pipedeneme
|       |-- Game.java       # Game logic
|       |-- Tiles.java      # Tile data class
|       |-- Controller.java # Game controller
|-- save.dat                # Save file for unlocked levels
|-- mapData.csv             # Map data for levels
```

### Technologies Used

- Java
- JavaFX

### Author

[Your Name]

### License

This project is licensed under the MIT License.

