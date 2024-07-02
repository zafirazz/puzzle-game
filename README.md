[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/f0r53tPY)
# Four Coin Puzzle Game


Consider 4 coins and a game board consisting of 4x4 cells. Initially, the coins are placed on the 4 cells in the center of the board as shown below:

![img.png](src/main/resources/images/img.png)

The goal of the game is to move each coin to a corner cell. In a move, one of the coins must be moved either horizontally or vertically by 1, 2, or 3 cells. A coin can be moved if and only if all of the following conditions hold:

1. It is adjacent to one or more coins either horizontally or vertically.
2. The target cell is empty and all cells between the coin and the target cell are empty.

The solution to the puzzle that was provided by BFS:

![img_1.png](src/main/resources/images/img_1.png)

1. [from=Move: (1, 1), to=Move: (0, 1)]

![img_2.png](src/main/resources/images/img_2.png)

2. [from=Move: (2, 1), to=Move: (1, 1)]

![img_3.png](src/main/resources/images/img_3.png)

3. [from=Move: (2, 2), to=Move: (2, 3)]

![img_4.png](src/main/resources/images/img_4.png)

4. [from=Move: (1, 2), to=Move: (1, 3)]

![img_5.png](src/main/resources/images/img_5.png)

5. [from=Move: (1, 1), to=Move: (1, 0)]

![img_6.png](src/main/resources/images/img_6.png)

6. [from=Move: (1, 3), to=Move: (1, 1)]

![img_7.png](src/main/resources/images/img_7.png)

7. [from=Move: (0, 1), to=Move: (0, 0)]

![img_8.png](src/main/resources/images/img_8.png)

8. [from=Move: (1, 1), to=Move: (3, 1)]

![img_9.png](src/main/resources/images/img_9.png)

9. [from=Move: (1, 0), to=Move: (3, 0)]

![img_10.png](src/main/resources/images/img_10.png)

10. [from=Move: (3, 1), to=Move: (3, 3)]

![img_11.png](src/main/resources/images/img_11.png)

11. [from=Move: (2, 3), to=Move: (0, 3)]

![img_12.png](src/main/resources/images/img_12.png)


