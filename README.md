# **Tetris Clone**

This is a clone of the classic Tetris game, developed for the Android platform using Java in Android Studio. The project simulates the traditional gameplay, where pieces fall onto a board, and the goal is to complete horizontal lines to increase the score.

## Features

- **Piece Movement**: Pieces automatically fall and can be moved left, right, or down.
- **Piece Rotation**: Each piece can be rotated to better fit the board.
- **Line Clearing**: When a row is completely filled, it is cleared, and the player scores points.
- **Scoring**: Points increase with each cleared line.
- **Random Piece Generation**: Pieces are generated randomly to mimic the original game behavior.
- **Collision Detection**: The game detects collisions, preventing pieces from overlapping or going beyond the board limits.

## Technologies Used

- **Language**: Java
- **IDE**: Android Studio
- **Platform**: Android

## Project Structure

- **Jogo**: Contains the main game logic and manages the game loop.
- **GerarPeca**: Class responsible for creating and manipulating pieces.

## Controls

- **Move Left**: Click on the left arrow.
- **Move Right**: Click on the right arrow.
- **Move Down**: Press the button A.
- **Rotate Piece**: Click on the button B.

## License

This project is licensed under the MIT License. For more details, see the `LICENSE` file.