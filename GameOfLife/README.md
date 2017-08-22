# Conways's Game Of Life

[Conway's Game Of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life), in Java.

Rules:

    The universe of the Game of Life is an infinite two-dimensional orthogonal grid of square cells,
    each of which is in one of two possible states, alive or dead, or "populated" or "unpopulated".
    Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or
    diagonally adjacent. At each step in time, the following transitions occur:

- Any live cell with fewer than two live neighbours dies, as if caused by underpopulation.
- Any live cell with two or three live neighbours lives on to the next generation.
- Any live cell with more than three live neighbours dies, as if by overpopulation.
- Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

## Compile

### javac

- Requirements:
    1. You must have Java Development Kit (JDK) Installed.
    2. [You must have PATH environment variable configured](https://docs.oracle.com/javase/tutorial/essential/environment/paths.html).

- Instructions:
    1. Open the cmd/bash in the src/main/java directory.
    2. Run ``javac com/yousef/GameOfLife/*.java``
    3. Run  ``java com.yousef.GameOfLife.Main``

### Maven

- Requirements:
    1. You must have Java Development Kit (JDK) Installed.
    2. [You must have PATH environment variable configured](https://docs.oracle.com/javase/tutorial/essential/environment/paths.html).
    3. You must Have Maven installed and Configured.

- Instructions:
    1. Open cmd/bash in the projects directory
    2. Run ``mvn compile``
    3. Run ``mvn exec:java``

Or you can just import it into Intellij/Eclipse/Your favourite IDE and build from there.
